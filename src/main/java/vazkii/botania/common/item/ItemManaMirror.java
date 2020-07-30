/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

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

public class ItemManaMirror extends Item implements IManaItem, ICoordBoundItem, IManaTooltipDisplay {

	private static final String TAG_MANA = "mana";
	private static final String TAG_MANA_BACKLOG = "manaBacklog";
	private static final String TAG_POS = "pos";

	private static final DummyPool fallbackPool = new DummyPool();

	public ItemManaMirror(Properties props) {
		super(props);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1 - getManaFractionForDisplay(stack);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (world.isRemote) {
			return;
		}

		IManaPool pool = getManaPool(stack);
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
	public ActionResultType onItemUse(ItemUseContext ctx) {
		World world = ctx.getWorld();
		PlayerEntity player = ctx.getPlayer();

		if (player != null && player.isSneaking() && !world.isRemote) {
			TileEntity tile = world.getTileEntity(ctx.getPos());
			if (tile instanceof IManaPool) {
				bindPool(ctx.getItem(), tile);
				world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), ModSounds.ding, SoundCategory.PLAYERS, 1F, 1F);
				return ActionResultType.SUCCESS;
			}
		}

		return ActionResultType.PASS;
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

	public void bindPool(ItemStack stack, TileEntity pool) {
		GlobalPos pos = GlobalPos.func_239648_a_(pool.getWorld().func_234923_W_(), pool.getPos());
		INBT ser = GlobalPos.field_239645_a_.encodeStart(NBTDynamicOps.INSTANCE, pos).get().orThrow();
		ItemNBTHelper.set(stack, TAG_POS, ser);
	}

	@Nullable
	public IManaPool getManaPool(ItemStack stack) {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if (server == null) {
			return fallbackPool;
		}

		if (!stack.getOrCreateTag().contains(TAG_POS)) {
			return fallbackPool;
		}

		Optional<GlobalPos> pos = GlobalPos.field_239645_a_.parse(NBTDynamicOps.INSTANCE, ItemNBTHelper.get(stack, TAG_POS)).result();
		if (!pos.isPresent()) {
			return fallbackPool;
		}

		BlockPos coords = pos.get().getPos();
		if (coords.getY() == -1) {
			return null;
		}

		RegistryKey<World> type = pos.get().func_239646_a_();
		World world = server.getWorld(type);
		if (world != null) {
			TileEntity tile = world.getTileEntity(coords);
			if (tile instanceof IManaPool) {
				return (IManaPool) tile;
			}
		}

		return null;
	}

	@Override
	public boolean canReceiveManaFromPool(ItemStack stack, TileEntity pool) {
		return false;
	}

	@Override
	public boolean canReceiveManaFromItem(ItemStack stack, ItemStack otherStack) {
		return false;
	}

	@Override
	public boolean canExportManaToPool(ItemStack stack, TileEntity pool) {
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

	@Override
	public BlockPos getBinding(ItemStack stack) {
		IManaPool pool = getManaPool(stack);

		return pool == null || pool instanceof DummyPool ? null : ((TileEntity) pool).getPos();
	}

	@Override
	public float getManaFractionForDisplay(ItemStack stack) {
		return (float) getMana(stack) / (float) getMaxMana(stack);
	}

}
