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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.common.core.helper.MathHelper;

import javax.annotation.Nullable;

import java.util.Objects;

/**
 * The basic class for a Generating Flower.
 */
public class TileEntityGeneratingFlower extends TileEntitySpecialFlower {
	private static final ResourceLocation SPREADER_ID = new ResourceLocation(BotaniaAPI.MODID, "mana_spreader");

	public static final int LINK_RANGE = 6;

	private static final String TAG_MANA = "mana";

	private static final String TAG_COLLECTOR_X = "collectorX";
	private static final String TAG_COLLECTOR_Y = "collectorY";
	private static final String TAG_COLLECTOR_Z = "collectorZ";
	public static final String TAG_PASSIVE_DECAY_TICKS = "passiveDecayTicks";

	private int mana;

	public int passiveDecayTicks;

	private @Nullable BlockPos bindingPos = null;

	public TileEntityGeneratingFlower(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (ticksExisted == 1) {
			bindToNearestCollector();
		}

		if (!getLevel().isClientSide && canGeneratePassively()) {
			int delay = getDelayBetweenPassiveGeneration();
			if (delay > 0 && ticksExisted % delay == 0) {
				addMana(getValueForPassiveGeneration());
			}
		}
		emptyManaIntoCollector();

		if (getLevel().isClientSide) {
			double particleChance = 1F - (double) getMana() / (double) getMaxMana() / 3.5F;
			int color = getColor();
			float red = (color >> 16 & 0xFF) / 255F;
			float green = (color >> 8 & 0xFF) / 255F;
			float blue = (color & 0xFF) / 255F;

			if (Math.random() > particleChance) {
				Vec3 offset = getLevel().getBlockState(getBlockPos()).getOffset(getLevel(), getBlockPos());
				double x = getBlockPos().getX() + offset.x;
				double y = getBlockPos().getY() + offset.y;
				double z = getBlockPos().getZ() + offset.z;
				BotaniaAPI.instance().sparkleFX(getLevel(), x + 0.3 + Math.random() * 0.5, y + 0.5 + Math.random() * 0.5, z + 0.3 + Math.random() * 0.5, red, green, blue, (float) Math.random(), 5);
			}
		} else {
			boolean passive = isPassiveFlower();
			int muhBalance = BotaniaAPI.instance().getPassiveFlowerDecay();

			if (passive) {
				passiveDecayTicks++;
			}

			if (passive && muhBalance > 0 && passiveDecayTicks > muhBalance) {
				getLevel().destroyBlock(getBlockPos(), false);
				if (Blocks.DEAD_BUSH.defaultBlockState().canSurvive(getLevel(), getBlockPos())) {
					getLevel().setBlockAndUpdate(getBlockPos(), Blocks.DEAD_BUSH.defaultBlockState());
				}
			}
		}
	}

	public void bindToNearestCollector() {
		IManaNetwork network = BotaniaAPI.instance().getManaNetworkInstance();
		BlockEntity closestCollector = network.getClosestCollector(getBlockPos(), getLevel(), LINK_RANGE);
		if (closestCollector != null) {
			setBindingForcefully(closestCollector.getBlockPos());
		}
	}

	public void emptyManaIntoCollector() {
		IManaCollector collector = findBoundCollector();
		if (collector != null && !collector.isFull() && getMana() > 0) {
			int manaval = Math.min(getMana(), collector.getMaxMana() - collector.getCurrentMana());
			addMana(-manaval);
			collector.receiveMana(manaval);
			sync();
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

	public @Nullable IManaCollector findCollectorAt(@Nullable BlockPos pos) {
		if (level == null || pos == null) {
			return null;
		}

		BlockEntity be = level.getBlockEntity(pos);
		return be instanceof IManaCollector && !be.isRemoved() ? (IManaCollector) be : null;
	}

	public @Nullable IManaCollector findBoundCollector() {
		return findCollectorAt(bindingPos);
	}

	public boolean wouldBeValidBinding(@Nullable BlockPos pos) {
		if (level == null || pos == null || !level.isLoaded(pos) || MathHelper.distSqr(getBlockPos(), pos) > LINK_RANGE * LINK_RANGE) {
			return false;
		} else {
			return findCollectorAt(pos) != null;
		}
	}

	public boolean isValidBinding() {
		return wouldBeValidBinding(bindingPos);
	}

	public void addMana(int mana) {
		this.mana = Math.min(getMaxMana(), this.getMana() + mana);
		setChanged();
	}

	public int getMana() {
		return mana;
	}

	public boolean isPassiveFlower() {
		return false;
	}

	public boolean canGeneratePassively() {
		return false;
	}

	public int getDelayBetweenPassiveGeneration() {
		return 20;
	}

	public int getValueForPassiveGeneration() {
		return 1;
	}

	@Override
	public boolean onWanded(Player player, ItemStack wand) {
		if (player == null) {
			return false;
		}

		if (!player.level.isClientSide) {
			sync();
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
		passiveDecayTicks = cmp.getInt(TAG_PASSIVE_DECAY_TICKS);

		int x = cmp.getInt(TAG_COLLECTOR_X);
		int y = cmp.getInt(TAG_COLLECTOR_Y);
		int z = cmp.getInt(TAG_COLLECTOR_Z);

		//Older versions of the mod (1.16, early 1.17) sometimes used a blockpos with y == -1 to mean "unbound".
		bindingPos = y < 0 ? null : new BlockPos(x, y, z);
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp) {
		super.writeToPacketNBT(cmp);
		cmp.putInt(TAG_MANA, getMana());
		cmp.putInt(TAG_TICKS_EXISTED, ticksExisted);
		cmp.putInt(TAG_PASSIVE_DECAY_TICKS, passiveDecayTicks);

		if (bindingPos != null) {
			cmp.putInt(TAG_COLLECTOR_X, bindingPos.getX());
			cmp.putInt(TAG_COLLECTOR_Y, bindingPos.getY());
			cmp.putInt(TAG_COLLECTOR_Z, bindingPos.getZ());
		}
	}

	@Override
	public BlockPos getBinding() {
		//Used for Wand of the Forest rendering, so this should return null when the binding is not valid.
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
		return Registry.ITEM.getOptional(SPREADER_ID).map(ItemStack::new).orElse(ItemStack.EMPTY);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void renderHUD(PoseStack ms, Minecraft mc) {
		String name = I18n.get(getBlockState().getBlock().getDescriptionId());
		int color = getColor();
		BotaniaAPIClient.instance().drawComplexManaHUD(ms, color, getMana(), getMaxMana(), name, getHudIcon(), isValidBinding());
	}

	@Override
	public boolean isOvergrowthAffected() {
		return !isPassiveFlower();
	}
}
