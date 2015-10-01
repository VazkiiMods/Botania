/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 24, 2014, 8:03:36 PM (GMT)]
 */
package vazkii.botania.api.subtile;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;

/**
 * The basic class for a Generating Flower.
 */
public class SubTileGenerating extends SubTileEntity {

	public static final int RANGE = 6;

	private static final String TAG_MANA = "mana";

	private static final String TAG_COLLECTOR_X = "collectorX";
	private static final String TAG_COLLECTOR_Y = "collectorY";
	private static final String TAG_COLLECTOR_Z = "collectorZ";
	private static final String TAG_PASSIVE_DECAY_TICKS = "passiveDecayTicks";

	protected int mana;

	public int redstoneSignal = 0;

	int sizeLastCheck = -1;
	protected TileEntity linkedCollector = null;
	public int knownMana = -1;
	public int passiveDecayTicks;

	ChunkCoordinates cachedCollectorCoordinates = null;

	/**
	 * If set to true, redstoneSignal will be updated every tick.
	 */
	public boolean acceptsRedstone() {
		return false;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		linkCollector();

		if(canGeneratePassively()) {
			int delay = getDelayBetweenPassiveGeneration();
			if(delay > 0 && ticksExisted % delay == 0 && !supertile.getWorldObj().isRemote) {
				if(shouldSyncPassiveGeneration())
					sync();
				addMana(getValueForPassiveGeneration());
			}
		}
		emptyManaIntoCollector();

		if(acceptsRedstone()) {
			redstoneSignal = 0;
			for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				int redstoneSide = supertile.getWorldObj().getIndirectPowerLevelTo(supertile.xCoord + dir.offsetX, supertile.yCoord + dir.offsetY, supertile.zCoord + dir.offsetZ, dir.ordinal());
				redstoneSignal = Math.max(redstoneSignal, redstoneSide);
			}
		}

		if(supertile.getWorldObj().isRemote) {
			double particleChance = 1F - (double) mana / (double) getMaxMana() / 3.5F;
			Color color = new Color(getColor());
			if(Math.random() > particleChance)
				BotaniaAPI.internalHandler.sparkleFX(supertile.getWorldObj(), supertile.xCoord + 0.3 + Math.random() * 0.5, supertile.yCoord + 0.5 + Math.random()  * 0.5, supertile.zCoord + 0.3 + Math.random() * 0.5, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, (float) Math.random(), 5);
		}

		boolean passive = isPassiveFlower();
		if(!supertile.getWorldObj().isRemote) {
			int muhBalance = BotaniaAPI.internalHandler.getPassiveFlowerDecay();

			if(passive && muhBalance > 0 && passiveDecayTicks > muhBalance) {
				supertile.getWorldObj().playAuxSFX(2001, supertile.xCoord, supertile.yCoord, supertile.zCoord, Block.getIdFromBlock(supertile.getBlockType()));
				if(supertile.getWorldObj().getBlock(supertile.xCoord, supertile.yCoord - 1, supertile.zCoord).isSideSolid(supertile.getWorldObj(), supertile.xCoord, supertile.yCoord - 1, supertile.zCoord, ForgeDirection.UP))
					supertile.getWorldObj().setBlock(supertile.xCoord, supertile.yCoord, supertile.zCoord, Blocks.deadbush);
				else supertile.getWorldObj().setBlockToAir(supertile.xCoord, supertile.yCoord, supertile.zCoord);
			}
		}

		if(!overgrowth && passive)
			passiveDecayTicks++;
	}

	public void linkCollector() {
		boolean needsNew = false;
		if(linkedCollector == null) {
			needsNew = true;

			if(cachedCollectorCoordinates != null) {
				needsNew = false;
				if(supertile.getWorldObj().blockExists(cachedCollectorCoordinates.posX, cachedCollectorCoordinates.posY, cachedCollectorCoordinates.posZ)) {
					needsNew = true;
					TileEntity tileAt = supertile.getWorldObj().getTileEntity(cachedCollectorCoordinates.posX, cachedCollectorCoordinates.posY, cachedCollectorCoordinates.posZ);
					if(tileAt != null && tileAt instanceof IManaCollector && !tileAt.isInvalid()) {
						linkedCollector = tileAt;
						needsNew = false;
					}
					cachedCollectorCoordinates = null;
				}
			}
		} else {
			TileEntity tileAt = supertile.getWorldObj().getTileEntity(linkedCollector.xCoord, linkedCollector.yCoord, linkedCollector.zCoord);
			if(tileAt != null && tileAt instanceof IManaCollector)
				linkedCollector = tileAt;
		}

		if(needsNew && ticksExisted == 1) { // New flowers only
			IManaNetwork network = BotaniaAPI.internalHandler.getManaNetworkInstance();
			int size = network.getAllCollectorsInWorld(supertile.getWorldObj()).size();
			if(BotaniaAPI.internalHandler.shouldForceCheck() || size != sizeLastCheck) {
				ChunkCoordinates coords = new ChunkCoordinates(supertile.xCoord, supertile.yCoord, supertile.zCoord);
				linkedCollector = network.getClosestCollector(coords, supertile.getWorldObj(), RANGE);
				sizeLastCheck = size;
			}
		}
	}

	public void linkToForcefully(TileEntity collector) {
		linkedCollector = collector;
	}

	public void addMana(int mana) {
		this.mana = Math.min(getMaxMana(), this.mana + mana);
	}

	public void emptyManaIntoCollector() {
		if(linkedCollector != null && isValidBinding()) {
			IManaCollector collector = (IManaCollector) linkedCollector;
			if(!collector.isFull() && mana > 0) {
				int manaval = Math.min(mana, collector.getMaxMana() - collector.getCurrentMana());
				mana -= manaval;
				collector.recieveMana(manaval);
			}
		}
	}

	public boolean isPassiveFlower() {
		return false;
	}

	public boolean shouldSyncPassiveGeneration() {
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
	public ArrayList<ItemStack> getDrops(ArrayList<ItemStack> list) {
		ArrayList<ItemStack> drops = super.getDrops(list);
		if(isPassiveFlower())
			populateDropStackNBTs(drops);
		return drops;
	}

	public void populateDropStackNBTs(List<ItemStack> drops) {
		if(isPassiveFlower() && ticksExisted > 0 && ConfigHandler.hardcorePassiveGeneration > 0)
			ItemNBTHelper.setInt(drops.get(0), TAG_PASSIVE_DECAY_TICKS, passiveDecayTicks);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
		super.onBlockPlacedBy(world, x, y, z, entity, stack);
		if(isPassiveFlower())
			passiveDecayTicks = ItemNBTHelper.getInt(stack, TAG_PASSIVE_DECAY_TICKS, 0);
	}

	@Override
	public boolean onWanded(EntityPlayer player, ItemStack wand) {
		if(player == null)
			return false;

		if(!player.worldObj.isRemote)
			sync();

		knownMana = mana;
		player.worldObj.playSoundAtEntity(player, "botania:ding", 0.1F, 1F);

		return super.onWanded(player, wand);
	}

	public int getMaxMana() {
		return 20;
	}

	public int getColor() {
		return 0xFFFFFF;
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		mana = cmp.getInteger(TAG_MANA);
		passiveDecayTicks = cmp.getInteger(TAG_PASSIVE_DECAY_TICKS);

		int x = cmp.getInteger(TAG_COLLECTOR_X);
		int y = cmp.getInteger(TAG_COLLECTOR_Y);
		int z = cmp.getInteger(TAG_COLLECTOR_Z);

		cachedCollectorCoordinates = y < 0 ? null : new ChunkCoordinates(x, y, z);
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_MANA, mana);
		cmp.setInteger(TAG_TICKS_EXISTED, ticksExisted);
		cmp.setInteger(TAG_PASSIVE_DECAY_TICKS, passiveDecayTicks);

		if(cachedCollectorCoordinates != null) {
			cmp.setInteger(TAG_COLLECTOR_X, cachedCollectorCoordinates.posX);
			cmp.setInteger(TAG_COLLECTOR_Y, cachedCollectorCoordinates.posY);
			cmp.setInteger(TAG_COLLECTOR_Z, cachedCollectorCoordinates.posZ);
		} else {
			int x = linkedCollector == null ? 0 : linkedCollector.xCoord;
			int y = linkedCollector == null ? -1 : linkedCollector.yCoord;
			int z = linkedCollector == null ? 0 : linkedCollector.zCoord;

			cmp.setInteger(TAG_COLLECTOR_X, x);
			cmp.setInteger(TAG_COLLECTOR_Y, y);
			cmp.setInteger(TAG_COLLECTOR_Z, z);
		}
	}

	@Override
	public ChunkCoordinates getBinding() {
		if(linkedCollector == null)
			return null;
		return new ChunkCoordinates(linkedCollector.xCoord, linkedCollector.yCoord, linkedCollector.zCoord);
	}

	@Override
	public boolean canSelect(EntityPlayer player, ItemStack wand, int x, int y, int z, int side) {
		return true;
	}

	@Override
	public boolean bindTo(EntityPlayer player, ItemStack wand, int x, int y, int z, int side) {
		int range = 6;
		range *= range;

		double dist = (x - supertile.xCoord) * (x - supertile.xCoord) + (y - supertile.yCoord) * (y - supertile.yCoord) + (z - supertile.zCoord) * (z - supertile.zCoord);
		if(range >= dist) {
			TileEntity tile = player.worldObj.getTileEntity(x, y, z);
			if(tile instanceof IManaCollector) {
				linkedCollector = tile;
				return true;
			}
		}

		return false;
	}


	public boolean isValidBinding() {
		return linkedCollector != null && !linkedCollector.isInvalid() && supertile.getWorldObj().getTileEntity(linkedCollector.xCoord, linkedCollector.yCoord, linkedCollector.zCoord) == linkedCollector;
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res) {
		String name = StatCollector.translateToLocal("tile.botania:flower." + getUnlocalizedName() + ".name");
		int color = getColor();
		BotaniaAPI.internalHandler.drawComplexManaHUD(color, knownMana, getMaxMana(), name, res, BotaniaAPI.internalHandler.getBindDisplayForFlowerType(this), isValidBinding());
	}

	@Override
	public boolean isOvergrowthAffected() {
		return !isPassiveFlower();
	}

}
