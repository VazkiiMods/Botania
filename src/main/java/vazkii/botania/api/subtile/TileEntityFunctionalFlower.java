/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.subtile;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.api.mana.IManaPool;

import javax.annotation.Nullable;

import java.util.Objects;

/**
 * The basic class for a Functional Flower.
 */
public class TileEntityFunctionalFlower extends TileEntitySpecialFlower {
	private static final ResourceLocation POOL_ID = new ResourceLocation(BotaniaAPI.MODID, "mana_pool");
	public static final int LINK_RANGE = 10;

	private static final String TAG_MANA = "mana";

	private static final String TAG_POOL_X = "poolX";
	private static final String TAG_POOL_Y = "poolY";
	private static final String TAG_POOL_Z = "poolZ";

	private int mana;

	public int redstoneSignal = 0;

	private @Nullable BlockPos poolCoordinates = null;

	public TileEntityFunctionalFlower(TileEntityType<?> type) {
		super(type);
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

		if (ticksExisted == 1) {
			bindToNearestPool();
		}
		drawManaFromPool();

		redstoneSignal = 0;
		if (acceptsRedstone()) {
			for (Direction dir : Direction.values()) {
				int redstoneSide = getWorld().getRedstonePower(getPos().offset(dir), dir);
				redstoneSignal = Math.max(redstoneSignal, redstoneSide);
			}
		}

		if (getWorld().isRemote) {
			double particleChance = 1F - (double) mana / (double) getMaxMana() / 3.5F;
			int color = getColor();
			float red = (color >> 16 & 0xFF) / 255F;
			float green = (color >> 8 & 0xFF) / 255F;
			float blue = (color & 0xFF) / 255F;
			if (Math.random() > particleChance) {
				BotaniaAPI.instance().sparkleFX(getWorld(), getPos().getX() + 0.3 + Math.random() * 0.5, getPos().getY() + 0.5 + Math.random() * 0.5, getPos().getZ() + 0.3 + Math.random() * 0.5, red, green, blue, (float) Math.random(), 5);
			}
		}
	}

	public void setBindingForcefully(BlockPos poolCoordinates) {
		boolean changed = !Objects.equals(this.poolCoordinates, poolCoordinates);

		this.poolCoordinates = poolCoordinates;

		if (changed) {
			markDirty();
			sync();
		}
	}

	public void bindToNearestPool() {
		IManaNetwork network = BotaniaAPI.instance().getManaNetworkInstance();
		TileEntity pool = network.getClosestPool(getPos(), getWorld(), LINK_RANGE);
		if (pool != null) {
			setBindingForcefully(pool.getPos());
		}
	}

	public @Nullable IManaPool findPoolAt(@Nullable BlockPos pos) {
		if (world == null || pos == null) {
			return null;
		}

		TileEntity tile = world.getTileEntity(pos);
		return tile instanceof IManaPool && !tile.isRemoved() ? (IManaPool) tile : null;
	}

	public @Nullable IManaPool findBoundPool() {
		return findPoolAt(poolCoordinates);
	}

	public boolean wouldBeValidBinding(@Nullable BlockPos pos) {
		//Same caveat as in TileEntityGeneratingFlower#wouldBeValidBinding -quat
		if (world == null || pos == null || !world.isBlockLoaded(pos) || vazkii.botania.common.core.helper.MathHelper.distanceSq(this.pos, pos) > LINK_RANGE * LINK_RANGE) {
			return false;
		} else {
			return findPoolAt(pos) != null;
		}
	}

	public boolean isValidBinding() {
		return wouldBeValidBinding(poolCoordinates);
	}

	public void drawManaFromPool() {
		IManaPool pool = findBoundPool();
		if (pool != null) {
			int manaInPool = pool.getCurrentMana();
			int manaMissing = getMaxMana() - mana;
			int manaToRemove = Math.min(manaMissing, manaInPool);
			pool.receiveMana(-manaToRemove);
			addMana(manaToRemove);
		}
	}

	public void addMana(int mana) {
		this.mana = MathHelper.clamp(this.mana + mana, 0, getMaxMana());
		markDirty();
	}

	@Override
	public boolean onWanded(PlayerEntity player, ItemStack wand) {
		if (player == null) {
			return false;
		}

		Registry.SOUND_EVENT.getOptional(DING_SOUND_EVENT).ifPresent(evt -> player.playSound(evt, 0.1F, 1F));

		return super.onWanded(player, wand);
	}

	public int getMaxMana() {
		return 20;
	}

	public int getColor() {
		return 0xFFFFFF;
	}

	@Override
	public void readFromPacketNBT(CompoundNBT cmp) {
		super.readFromPacketNBT(cmp);
		mana = cmp.getInt(TAG_MANA);

		BlockPos poolCoordinates = null;
		if (cmp.contains(TAG_POOL_X)) {
			poolCoordinates = new BlockPos(cmp.getInt(TAG_POOL_X), cmp.getInt(TAG_POOL_Y), cmp.getInt(TAG_POOL_Z));
			//Older versions of the mod sometimes used this to denote an unbound pool.
			if (poolCoordinates.getY() == -1) {
				poolCoordinates = null;
			}
		}

		this.poolCoordinates = poolCoordinates;
	}

	@Override
	public void writeToPacketNBT(CompoundNBT cmp) {
		super.writeToPacketNBT(cmp);
		cmp.putInt(TAG_MANA, mana);

		if (poolCoordinates != null) {
			cmp.putInt(TAG_POOL_X, poolCoordinates.getX());
			cmp.putInt(TAG_POOL_Y, poolCoordinates.getY());
			cmp.putInt(TAG_POOL_Z, poolCoordinates.getZ());
		}
	}

	@Override
	public BlockPos getBinding() {
		//This method is used for Wand of the Forest overlays, so it should return null when the binding isn't valid.
		return isValidBinding() ? poolCoordinates : null;
	}

	@Override
	public boolean canSelect(PlayerEntity player, ItemStack wand, BlockPos pos, Direction side) {
		return true;
	}

	@Override
	public boolean bindTo(PlayerEntity player, ItemStack wand, BlockPos pos, Direction side) {
		if (wouldBeValidBinding(pos)) {
			setBindingForcefully(pos);
			return true;
		}

		return false;
	}

	public ItemStack getHudIcon() {
		return Registry.ITEM.getOptional(POOL_ID).map(ItemStack::new).orElse(ItemStack.EMPTY);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(MatrixStack ms, Minecraft mc) {
		String name = I18n.format(getBlockState().getBlock().getTranslationKey());
		int color = getColor();
		BotaniaAPIClient.instance().drawComplexManaHUD(ms, color, getMana(), getMaxMana(), name, getHudIcon(), isValidBinding());
	}

	public int getMana() {
		return mana;
	}
}
