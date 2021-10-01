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

import net.minecraft.block.Blocks;
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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.api.mana.IManaCollector;

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

	private @Nullable TileEntity linkedCollector = null;
	private @Nullable BlockPos collectorCoordinates = null;

	public TileEntityGeneratingFlower(TileEntityType<?> type) {
		super(type);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		linkCollector();

		if (!getWorld().isRemote && canGeneratePassively()) {
			int delay = getDelayBetweenPassiveGeneration();
			if (delay > 0 && ticksExisted % delay == 0) {
				addMana(getValueForPassiveGeneration());
			}
		}
		emptyManaIntoCollector();

		if (getWorld().isRemote) {
			double particleChance = 1F - (double) getMana() / (double) getMaxMana() / 3.5F;
			int color = getColor();
			float red = (color >> 16 & 0xFF) / 255F;
			float green = (color >> 8 & 0xFF) / 255F;
			float blue = (color & 0xFF) / 255F;

			if (Math.random() > particleChance) {
				Vector3d offset = getWorld().getBlockState(getPos()).getOffset(getWorld(), getPos());
				double x = getPos().getX() + offset.x;
				double y = getPos().getY() + offset.y;
				double z = getPos().getZ() + offset.z;
				BotaniaAPI.instance().sparkleFX(getWorld(), x + 0.3 + Math.random() * 0.5, y + 0.5 + Math.random() * 0.5, z + 0.3 + Math.random() * 0.5, red, green, blue, (float) Math.random(), 5);
			}
		} else {
			boolean passive = isPassiveFlower();
			int muhBalance = BotaniaAPI.instance().getPassiveFlowerDecay();

			if (passive) {
				passiveDecayTicks++;
			}

			if (passive && muhBalance > 0 && passiveDecayTicks > muhBalance) {
				getWorld().destroyBlock(getPos(), false);
				if (Blocks.DEAD_BUSH.getDefaultState().isValidPosition(getWorld(), getPos())) {
					getWorld().setBlockState(getPos(), Blocks.DEAD_BUSH.getDefaultState());
				}
			}
		}
	}

	public void linkCollector() {
		if (ticksExisted == 1) {
			IManaNetwork network = BotaniaAPI.instance().getManaNetworkInstance();
			linkedCollector = network.getClosestCollector(getPos(), getWorld(), LINK_RANGE);
		}

		if (collectorCoordinates != null && getWorld().isBlockLoaded(collectorCoordinates)) {
			TileEntity linkedTo = getWorld().getTileEntity(collectorCoordinates);
			if (linkedTo instanceof IManaCollector) {
				linkedCollector = linkedTo;
			}
		}

		if (linkedCollector != null && linkedCollector.isRemoved()) {
			linkedCollector = null;
		}
	}

	public void linkToForcefully(TileEntity collector) {
		if (linkedCollector != collector) {
			linkedCollector = collector;
			markDirty();
			sync();
		}
	}

	public void addMana(int mana) {
		this.mana = Math.min(getMaxMana(), this.getMana() + mana);
		markDirty();
	}

	public void emptyManaIntoCollector() {
		if (isValidBinding()) {
			IManaCollector collector = (IManaCollector) linkedCollector;
			if (!collector.isFull() && getMana() > 0) {
				int manaval = Math.min(getMana(), collector.getMaxMana() - collector.getCurrentMana());
				addMana(-manaval);
				collector.receiveMana(manaval);
				sync();
			}
		}
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
	public boolean onWanded(PlayerEntity player, ItemStack wand) {
		if (player == null) {
			return false;
		}

		if (!player.world.isRemote) {
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
	public void readFromPacketNBT(CompoundNBT cmp) {
		super.readFromPacketNBT(cmp);
		mana = cmp.getInt(TAG_MANA);
		passiveDecayTicks = cmp.getInt(TAG_PASSIVE_DECAY_TICKS);

		BlockPos collectorCoordinates = null;
		if (cmp.contains(TAG_COLLECTOR_X)) {
			collectorCoordinates = new BlockPos(cmp.getInt(TAG_COLLECTOR_X), cmp.getInt(TAG_COLLECTOR_Y), cmp.getInt(TAG_COLLECTOR_Z));
			//Older versions of the mod sometimes used this to denote an unbound collector.
			if (collectorCoordinates.getY() == -1)
				collectorCoordinates = null;
		}

		if (!Objects.equals(this.collectorCoordinates, collectorCoordinates)) {
			linkedCollector = null; //Force a refresh of the linked collector
		}

		this.collectorCoordinates = collectorCoordinates;
	}

	@Override
	public void writeToPacketNBT(CompoundNBT cmp) {
		super.writeToPacketNBT(cmp);
		cmp.putInt(TAG_MANA, getMana());
		cmp.putInt(TAG_TICKS_EXISTED, ticksExisted);
		cmp.putInt(TAG_PASSIVE_DECAY_TICKS, passiveDecayTicks);

		if (collectorCoordinates != null) {
			cmp.putInt(TAG_COLLECTOR_X, collectorCoordinates.getX());
			cmp.putInt(TAG_COLLECTOR_Y, collectorCoordinates.getY());
			cmp.putInt(TAG_COLLECTOR_Z, collectorCoordinates.getZ());
		}
	}

	@Override
	public BlockPos getBinding() {
		if (linkedCollector == null) {
			return null;
		}
		return linkedCollector.getPos();
	}

	@Override
	public boolean canSelect(PlayerEntity player, ItemStack wand, BlockPos pos, Direction side) {
		return true;
	}

	@Override
	public boolean bindTo(PlayerEntity player, ItemStack wand, BlockPos pos, Direction side) {
		int range = 6;
		range *= range;

		double dist = pos.distanceSq(getPos());
		if (range >= dist) {
			TileEntity tile = player.world.getTileEntity(pos);
			if (tile instanceof IManaCollector) {
				linkedCollector = tile;
				sync();
				return true;
			}
		}

		return false;
	}

	public boolean isValidBinding() {
		return linkedCollector != null && !linkedCollector.isRemoved() && getWorld().getTileEntity(linkedCollector.getPos()) == linkedCollector;
	}

	public ItemStack getHudIcon() {
		return Registry.ITEM.getOptional(SPREADER_ID).map(ItemStack::new).orElse(ItemStack.EMPTY);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(MatrixStack ms, Minecraft mc) {
		String name = I18n.format(getBlockState().getBlock().getTranslationKey());
		int color = getColor();
		BotaniaAPIClient.instance().drawComplexManaHUD(ms, color, getMana(), getMaxMana(), name, getHudIcon(), isValidBinding());
	}

	@Override
	public boolean isOvergrowthAffected() {
		return !isPassiveFlower();
	}

	public int getMana() {
		return mana;
	}
}
