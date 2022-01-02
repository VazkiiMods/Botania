/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.subtile;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.block.IWandHUD;
import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.api.mana.IManaPool;

import javax.annotation.Nullable;

/**
 * The basic class for a Functional Flower.
 */
public class TileEntityFunctionalFlower extends TileEntityBindableSpecialFlower<IManaPool> {
	private static final ResourceLocation POOL_ID = new ResourceLocation(BotaniaAPI.MODID, "mana_pool");

	public static final int LINK_RANGE = 10;
	private static final String TAG_MANA = "mana";

	private int mana;
	public int redstoneSignal = 0;

	public TileEntityFunctionalFlower(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state, IManaPool.class);
	}

	/**
	 * If set to true, redstoneSignal will be updated every tick.
	 */
	public boolean acceptsRedstone() {
		return false;
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		drawManaFromPool();

		redstoneSignal = 0;
		if (acceptsRedstone()) {
			for (Direction dir : Direction.values()) {
				int redstoneSide = getLevel().getSignal(getBlockPos().relative(dir), dir);
				redstoneSignal = Math.max(redstoneSignal, redstoneSide);
			}
		}

		if (getLevel().isClientSide) {
			double particleChance = 1F - (double) mana / (double) getMaxMana() / 3.5F;
			int color = getColor();
			float red = (color >> 16 & 0xFF) / 255F;
			float green = (color >> 8 & 0xFF) / 255F;
			float blue = (color & 0xFF) / 255F;
			if (Math.random() > particleChance) {
				BotaniaAPI.instance().sparkleFX(getLevel(), getBlockPos().getX() + 0.3 + Math.random() * 0.5, getBlockPos().getY() + 0.5 + Math.random() * 0.5, getBlockPos().getZ() + 0.3 + Math.random() * 0.5, red, green, blue, (float) Math.random(), 5);
			}
		}
	}

	@Override
	public int getBindingRadius() {
		return LINK_RANGE;
	}

	@Override
	public @Nullable BlockPos findClosestTarget() {
		IManaNetwork network = BotaniaAPI.instance().getManaNetworkInstance();
		BlockEntity closestPool = network.getClosestPool(getBlockPos(), getLevel(), getBindingRadius());
		return closestPool == null ? null : closestPool.getBlockPos();
	}

	public void drawManaFromPool() {
		IManaPool pool = findBoundTile();
		if (pool != null) {
			int manaInPool = pool.getCurrentMana();
			int manaMissing = getMaxMana() - mana;
			int manaToRemove = Math.min(manaMissing, manaInPool);
			pool.receiveMana(-manaToRemove);
			addMana(manaToRemove);
		}
	}

	public int getMana() {
		return mana;
	}

	public void addMana(int mana) {
		this.mana = Mth.clamp(this.mana + mana, 0, getMaxMana());
		setChanged();
	}

	public int getMaxMana() {
		return 20;
	}

	public int getColor() {
		return 0xFFFFFF;
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp) {
		super.readFromPacketNBT(cmp);
		mana = cmp.getInt(TAG_MANA);
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp) {
		super.writeToPacketNBT(cmp);
		cmp.putInt(TAG_MANA, mana);
	}

	public ItemStack getHudIcon() {
		return Registry.ITEM.getOptional(POOL_ID).map(ItemStack::new).orElse(ItemStack.EMPTY);
	}

	public static class FunctionalWandHud<T extends TileEntityFunctionalFlower> implements IWandHUD {
		protected final T flower;

		public FunctionalWandHud(T flower) {
			this.flower = flower;
		}

		@Override
		public void renderHUD(PoseStack ms, Minecraft mc) {
			String name = I18n.get(flower.getBlockState().getBlock().getDescriptionId());
			int color = flower.getColor();
			BotaniaAPIClient.instance().drawComplexManaHUD(ms, color, flower.getMana(), flower.getMaxMana(),
					name, flower.getHudIcon(), flower.isValidBinding());
		}
	}
}
