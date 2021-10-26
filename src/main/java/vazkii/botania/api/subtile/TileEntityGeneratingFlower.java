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

	int sizeLastCheck = -1;
	protected BlockEntity linkedCollector = null;
	public int passiveDecayTicks;

	private BlockPos cachedCollectorCoordinates = null;

	public TileEntityGeneratingFlower(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		linkCollector();

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

	public void linkCollector() {
		boolean needsNew = false;
		if (linkedCollector == null) {
			needsNew = true;

			if (cachedCollectorCoordinates != null) {
				needsNew = false;
				if (getLevel().hasChunkAt(cachedCollectorCoordinates)) {
					needsNew = true;
					BlockEntity tileAt = getLevel().getBlockEntity(cachedCollectorCoordinates);
					if (tileAt instanceof IManaCollector && !tileAt.isRemoved()) {
						linkedCollector = tileAt;
						needsNew = false;
					}
					cachedCollectorCoordinates = null;
				}
			}
		} else {
			BlockEntity tileAt = getLevel().getBlockEntity(linkedCollector.getBlockPos());
			if (tileAt instanceof IManaCollector) {
				linkedCollector = tileAt;
			}
		}

		if (needsNew && ticksExisted == 1) { // New flowers only
			IManaNetwork network = BotaniaAPI.instance().getManaNetworkInstance();
			int size = network.getAllCollectorsInWorld(getLevel()).size();
			if (BotaniaAPI.instance().shouldForceCheck() || size != sizeLastCheck) {
				linkedCollector = network.getClosestCollector(getBlockPos(), getLevel(), LINK_RANGE);
				sizeLastCheck = size;
			}
		}
	}

	public void linkToForcefully(BlockEntity collector) {
		if (linkedCollector != collector) {
			linkedCollector = collector;
			setChanged();
			sync();
		}
	}

	public void addMana(int mana) {
		this.mana = Math.min(getMaxMana(), this.getMana() + mana);
		setChanged();
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

		cachedCollectorCoordinates = y < 0 ? null : new BlockPos(x, y, z);
		if (linkedCollector != null && !Objects.equals(linkedCollector.getBlockPos(), cachedCollectorCoordinates)) {
			linkedCollector = null; //Force a refresh of the linked collector
		}
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp) {
		super.writeToPacketNBT(cmp);
		cmp.putInt(TAG_MANA, getMana());
		cmp.putInt(TAG_TICKS_EXISTED, ticksExisted);
		cmp.putInt(TAG_PASSIVE_DECAY_TICKS, passiveDecayTicks);

		if (cachedCollectorCoordinates != null) {
			cmp.putInt(TAG_COLLECTOR_X, cachedCollectorCoordinates.getX());
			cmp.putInt(TAG_COLLECTOR_Y, cachedCollectorCoordinates.getY());
			cmp.putInt(TAG_COLLECTOR_Z, cachedCollectorCoordinates.getZ());
		} else {
			int x = linkedCollector == null ? 0 : linkedCollector.getBlockPos().getX();
			int y = linkedCollector == null ? -1 : linkedCollector.getBlockPos().getY();
			int z = linkedCollector == null ? 0 : linkedCollector.getBlockPos().getZ();

			cmp.putInt(TAG_COLLECTOR_X, x);
			cmp.putInt(TAG_COLLECTOR_Y, y);
			cmp.putInt(TAG_COLLECTOR_Z, z);
		}
	}

	@Override
	public BlockPos getBinding() {
		if (linkedCollector == null) {
			return null;
		}
		return linkedCollector.getBlockPos();
	}

	@Override
	public boolean canSelect(Player player, ItemStack wand, BlockPos pos, Direction side) {
		return true;
	}

	@Override
	public boolean bindTo(Player player, ItemStack wand, BlockPos pos, Direction side) {
		int range = 6;
		range *= range;

		double dist = pos.distSqr(getBlockPos());
		if (range >= dist) {
			BlockEntity tile = player.level.getBlockEntity(pos);
			if (tile instanceof IManaCollector) {
				linkedCollector = tile;
				sync();
				return true;
			}
		}

		return false;
	}

	public boolean isValidBinding() {
		return linkedCollector != null && !linkedCollector.isRemoved() && getLevel().getBlockEntity(linkedCollector.getBlockPos()) == linkedCollector;
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

	public int getMana() {
		return mana;
	}
}
