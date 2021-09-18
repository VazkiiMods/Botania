/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.IManaTooltipDisplay;
import vazkii.botania.api.wand.ICoordBoundItem;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemManaMirror extends Item implements IManaItem, ICoordBoundItem, IManaTooltipDisplay {

	private static final String TAG_MANA = "mana";
	private static final String TAG_MANA_BACKLOG = "manaBacklog";
	private static final String TAG_POS = "pos";

	private static final DummyPool fallbackPool = new DummyPool();

	public ItemManaMirror(Properties props) {
		super(props);
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return true;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		return Math.round(13 * getManaFractionForDisplay(stack));
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return Mth.hsvToRgb(getManaFractionForDisplay(stack) / 3.0F, 1.0F, 1.0F);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		if (world.isClientSide) {
			return;
		}

		IManaPool pool = getManaPool(world.getServer(), stack);
		if (!(pool instanceof DummyPool)) {
			if (pool == null) {
				setMana(stack, 0);
			} else {
				pool.receiveMana(getManaBacklog(stack));
				setManaBacklog(stack, 0);
				setMana(stack, pool.getCurrentMana());
			}
		}
	}

	@Nonnull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level world = ctx.getLevel();
		Player player = ctx.getPlayer();

		if (player != null && player.isShiftKeyDown() && !world.isClientSide) {
			BlockEntity tile = world.getBlockEntity(ctx.getClickedPos());
			if (tile instanceof IManaPool) {
				bindPool(ctx.getItemInHand(), tile);
				world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.ding, SoundSource.PLAYERS, 1F, 1F);
				return InteractionResult.SUCCESS;
			}
		}

		return InteractionResult.PASS;
	}

	@Override
	public int getMana(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_MANA, 0);
	}

	public void setMana(ItemStack stack, int mana) {
		ItemNBTHelper.setInt(stack, TAG_MANA, Math.max(0, mana));
	}

	public int getManaBacklog(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_MANA_BACKLOG, 0);
	}

	public void setManaBacklog(ItemStack stack, int backlog) {
		ItemNBTHelper.setInt(stack, TAG_MANA_BACKLOG, backlog);
	}

	@Override
	public int getMaxMana(ItemStack stack) {
		return TilePool.MAX_MANA;
	}

	@Override
	public void addMana(ItemStack stack, int mana) {
		setMana(stack, getMana(stack) + mana);
		setManaBacklog(stack, getManaBacklog(stack) + mana);
	}

	public void bindPool(ItemStack stack, BlockEntity pool) {
		GlobalPos pos = GlobalPos.of(pool.getLevel().dimension(), pool.getBlockPos());
		Tag ser = GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, pos).get().orThrow();
		ItemNBTHelper.set(stack, TAG_POS, ser);
	}

	@Nullable
	private static GlobalPos getBoundPos(ItemStack stack) {
		if (!stack.getOrCreateTag().contains(TAG_POS)) {
			return null;
		}

		return GlobalPos.CODEC.parse(NbtOps.INSTANCE, ItemNBTHelper.get(stack, TAG_POS))
				.result()
				.filter(pos -> pos.pos().getY() != Integer.MIN_VALUE)
				.orElse(null);
	}

	@Nullable
	private IManaPool getManaPool(@Nullable MinecraftServer server, ItemStack stack) {
		if (server == null) {
			return fallbackPool;
		}

		GlobalPos pos = getBoundPos(stack);
		if (pos == null) {
			return fallbackPool;
		}

		ResourceKey<Level> type = pos.dimension();
		Level world = server.getLevel(type);
		if (world != null) {
			BlockEntity tile = world.getBlockEntity(pos.pos());
			if (tile instanceof IManaPool) {
				return (IManaPool) tile;
			}
		}

		return null;
	}

	@Override
	public boolean canReceiveManaFromPool(ItemStack stack, BlockEntity pool) {
		return false;
	}

	@Override
	public boolean canReceiveManaFromItem(ItemStack stack, ItemStack otherStack) {
		return false;
	}

	@Override
	public boolean canExportManaToPool(ItemStack stack, BlockEntity pool) {
		return false;
	}

	@Override
	public boolean canExportManaToItem(ItemStack stack, ItemStack otherStack) {
		return true;
	}

	private static class DummyPool implements IManaPool {

		@Override
		public boolean isFull() {
			return false;
		}

		@Override
		public void receiveMana(int mana) {}

		@Override
		public boolean canReceiveManaFromBursts() {
			return false;
		}

		@Override
		public int getCurrentMana() {
			return 0;
		}

		@Override
		public boolean isOutputtingPower() {
			return false;
		}

		@Override
		public DyeColor getColor() {
			return DyeColor.WHITE;
		}

		@Override
		public void setColor(DyeColor color) {}

	}

	@Override
	public boolean isNoExport(ItemStack stack) {
		return false;
	}

	@Nullable
	@Override
	public BlockPos getBinding(Level world, ItemStack stack) {
		GlobalPos pos = getBoundPos(stack);
		if (pos == null) {
			return null;
		}

		if (pos.dimension() == world.dimension()) {
			return pos.pos();
		}

		return null;
	}

	@Override
	public float getManaFractionForDisplay(ItemStack stack) {
		return (float) getMana(stack) / (float) getMaxMana(stack);
	}

}
