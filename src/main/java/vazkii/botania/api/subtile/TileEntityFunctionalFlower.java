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

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.common.core.helper.MathHelper;

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

	private @Nullable BlockPos bindingPos = null;

	public TileEntityFunctionalFlower(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
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

	public void bindToNearestPool() {
		IManaNetwork network = BotaniaAPI.instance().getManaNetworkInstance();
		BlockEntity closestPool = network.getClosestPool(getBlockPos(), getLevel(), LINK_RANGE);
		if (closestPool instanceof IManaPool) {
			setBindingForcefully(closestPool.getBlockPos());
		}
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

	public void setBindingForcefully(BlockPos bindingPos) {
		boolean changed = !Objects.equals(this.bindingPos, bindingPos);

		this.bindingPos = bindingPos;

		if (changed) {
			setChanged();
			sync();
		}
	}

	public @Nullable IManaPool findPoolAt(@Nullable BlockPos pos) {
		if (level == null || pos == null) {
			return null;
		}

		BlockEntity be = level.getBlockEntity(pos);
		return be instanceof IManaPool && !be.isRemoved() ? (IManaPool) be : null;
	}

	public @Nullable IManaPool findBoundPool() {
		return findPoolAt(bindingPos);
	}

	public boolean wouldBeValidBinding(@Nullable BlockPos pos) {
		if (level == null || pos == null || !level.isLoaded(pos) || MathHelper.distSqr(getBlockPos(), pos) > LINK_RANGE * LINK_RANGE) {
			return false;
		} else {
			return findPoolAt(pos) != null;
		}
	}

	public boolean isValidBinding() {
		return wouldBeValidBinding(bindingPos);
	}

	public int getMana() {
		return mana;
	}

	public void addMana(int mana) {
		this.mana = Mth.clamp(this.mana + mana, 0, getMaxMana());
		setChanged();
	}

	@Override
	public boolean onWanded(Player player, ItemStack wand) {
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
	public void readFromPacketNBT(CompoundTag cmp) {
		super.readFromPacketNBT(cmp);
		mana = cmp.getInt(TAG_MANA);

		int x = cmp.getInt(TAG_POOL_X);
		int y = cmp.getInt(TAG_POOL_Y);
		int z = cmp.getInt(TAG_POOL_Z);

		//Older versions of the mod (1.16, early 1.17) sometimes used a blockpos with y == -1 to mean "unbound".
		bindingPos = y < 0 ? null : new BlockPos(x, y, z);
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp) {
		super.writeToPacketNBT(cmp);
		cmp.putInt(TAG_MANA, mana);

		if (bindingPos != null) {
			cmp.putInt(TAG_POOL_X, bindingPos.getX());
			cmp.putInt(TAG_POOL_Y, bindingPos.getY());
			cmp.putInt(TAG_POOL_Z, bindingPos.getZ());
		}
	}

	@Override
	public BlockPos getBinding() {
		return isValidBinding() ? bindingPos : null;
	}

	@Override
	public boolean canSelect(Player player, ItemStack wand, BlockPos pos, Direction side) {
		return true;
	}

	@Override
	public boolean bindTo(Player player, ItemStack wand, BlockPos pos, Direction side) {
		if (wouldBeValidBinding(pos)) {
			setBindingForcefully(pos);
			return true;
		}

		return false;
	}

	public ItemStack getHudIcon() {
		return Registry.ITEM.getOptional(POOL_ID).map(ItemStack::new).orElse(ItemStack.EMPTY);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void renderHUD(PoseStack ms, Minecraft mc) {
		String name = I18n.get(getBlockState().getBlock().getDescriptionId());
		int color = getColor();
		BotaniaAPIClient.instance().drawComplexManaHUD(ms, color, getMana(), getMaxMana(), name, getHudIcon(), isValidBinding());
	}
}
