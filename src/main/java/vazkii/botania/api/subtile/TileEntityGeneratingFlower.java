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
	protected TileEntity linkedCollector = null;
	public int passiveDecayTicks;

	BlockPos cachedCollectorCoordinates = null;

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
		boolean needsNew = false;
		if (linkedCollector == null) {
			needsNew = true;

			if (cachedCollectorCoordinates != null) {
				needsNew = false;
				if (getWorld().isBlockLoaded(cachedCollectorCoordinates)) {
					needsNew = true;
					TileEntity tileAt = getWorld().getTileEntity(cachedCollectorCoordinates);
					if (tileAt instanceof IManaCollector && !tileAt.isRemoved()) {
						linkedCollector = tileAt;
						needsNew = false;
					}
					cachedCollectorCoordinates = null;
				}
			}
		} else {
			TileEntity tileAt = getWorld().getTileEntity(linkedCollector.getPos());
			if (tileAt instanceof IManaCollector) {
				linkedCollector = tileAt;
			}
		}

		if (needsNew && ticksExisted == 1) { // New flowers only
			IManaNetwork network = BotaniaAPI.instance().getManaNetworkInstance();
			int size = network.getAllCollectorsInWorld(getWorld()).size();
			if (BotaniaAPI.instance().shouldForceCheck() || size != sizeLastCheck) {
				linkedCollector = network.getClosestCollector(getPos(), getWorld(), LINK_RANGE);
				sizeLastCheck = size;
			}
		}
	}

	public void linkToForcefully(TileEntity collector) {
		linkedCollector = collector;
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
				addMana(manaval);
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

		Registry.SOUND_EVENT.getValue(DING_SOUND_EVENT).ifPresent(evt -> player.playSound(evt, 0.1F, 1F));

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

		int x = cmp.getInt(TAG_COLLECTOR_X);
		int y = cmp.getInt(TAG_COLLECTOR_Y);
		int z = cmp.getInt(TAG_COLLECTOR_Z);

		cachedCollectorCoordinates = y < 0 ? null : new BlockPos(x, y, z);
	}

	@Override
	public void writeToPacketNBT(CompoundNBT cmp) {
		super.writeToPacketNBT(cmp);
		cmp.putInt(TAG_MANA, getMana());
		cmp.putInt(TAG_TICKS_EXISTED, ticksExisted);
		cmp.putInt(TAG_PASSIVE_DECAY_TICKS, passiveDecayTicks);

		if (cachedCollectorCoordinates != null) {
			cmp.putInt(TAG_COLLECTOR_X, cachedCollectorCoordinates.getX());
			cmp.putInt(TAG_COLLECTOR_Y, cachedCollectorCoordinates.getY());
			cmp.putInt(TAG_COLLECTOR_Z, cachedCollectorCoordinates.getZ());
		} else {
			int x = linkedCollector == null ? 0 : linkedCollector.getPos().getX();
			int y = linkedCollector == null ? -1 : linkedCollector.getPos().getY();
			int z = linkedCollector == null ? 0 : linkedCollector.getPos().getZ();

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
		return Registry.ITEM.getValue(SPREADER_ID).map(ItemStack::new).orElse(ItemStack.EMPTY);
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
