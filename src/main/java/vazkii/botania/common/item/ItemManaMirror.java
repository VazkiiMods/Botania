/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.dynamic.GlobalPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.IManaTooltipDisplay;
import vazkii.botania.api.wand.ICoordBoundItem;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Optional;

public class ItemManaMirror extends Item implements IManaItem, ICoordBoundItem, IManaTooltipDisplay, IDurabilityExtension {

	private static final String TAG_MANA = "mana";
	private static final String TAG_MANA_BACKLOG = "manaBacklog";
	private static final String TAG_POS = "pos";

	private static final DummyPool fallbackPool = new DummyPool();

	public ItemManaMirror(Settings props) {
		super(props);
	}

	@Override
	public boolean showDurability(ItemStack stack) {
		return true;
	}

	@Override
	public double getDurability(ItemStack stack) {
		return 1 - getManaFractionForDisplay(stack);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (world.isClient) {
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
	public ActionResult useOnBlock(ItemUsageContext ctx) {
		World world = ctx.getWorld();
		PlayerEntity player = ctx.getPlayer();

		if (player != null && player.isSneaking() && !world.isClient) {
			BlockEntity tile = world.getBlockEntity(ctx.getBlockPos());
			if (tile instanceof IManaPool) {
				bindPool(ctx.getStack(), tile);
				world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.ding, SoundCategory.PLAYERS, 1F, 1F);
				return ActionResult.SUCCESS;
			}
		}

		return ActionResult.PASS;
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
		GlobalPos pos = GlobalPos.create(pool.getWorld().getRegistryKey(), pool.getPos());
		Tag ser = GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, pos).get().orThrow();
		ItemNBTHelper.set(stack, TAG_POS, ser);
	}

	@Nullable
	private static GlobalPos getBoundPos(ItemStack stack) {
		if (!stack.getOrCreateTag().contains(TAG_POS)) {
			return null;
		}

		Optional<GlobalPos> pos = GlobalPos.CODEC.parse(NbtOps.INSTANCE, ItemNBTHelper.get(stack, TAG_POS)).result();
		if (!pos.isPresent()) {
			return null;
		}

		BlockPos coords = pos.get().getPos();
		if (coords.getY() == -1) {
			return null;
		}
		return pos.get();
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

		RegistryKey<World> type = pos.getDimension();
		World world = server.getWorld(type);
		if (world != null) {
			BlockEntity tile = world.getBlockEntity(pos.getPos());
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
	public BlockPos getBinding(World world, ItemStack stack) {
		GlobalPos pos = getBoundPos(stack);
		if (pos == null) {
			return null;
		}

		if (pos.getDimension() == world.getRegistryKey()) {
			return pos.getPos();
		}

		return null;
	}

	@Override
	public float getManaFractionForDisplay(ItemStack stack) {
		return (float) getMana(stack) / (float) getMaxMana(stack);
	}

}
