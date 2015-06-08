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

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.api.mana.IManaCollector;

/**
 * The basic class for a Generating Flower.
 */
public class SubTileGenerating extends SubTileEntity {

	private static final String TAG_MANA = "mana";

	private static final String TAG_COLLECTOR_X = "collectorX";
	private static final String TAG_COLLECTOR_Y = "collectorY";
	private static final String TAG_COLLECTOR_Z = "collectorZ";

	protected int mana;

	int sizeLastCheck = -1;
	protected TileEntity linkedCollector = null;
	public int knownMana = -1;

	ChunkCoordinates cachedCollectorCoordinates = null;

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

		if(supertile.getWorldObj().isRemote) {
			double particleChance = 1F - (double) mana / (double) getMaxMana() / 3.5F;
			Color color = new Color(getColor());
			if(Math.random() > particleChance)
				BotaniaAPI.internalHandler.sparkleFX(supertile.getWorldObj(), supertile.xCoord + 0.3 + Math.random() * 0.5, supertile.yCoord + 0.5 + Math.random()  * 0.5, supertile.zCoord + 0.3 + Math.random() * 0.5, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, (float) Math.random(), 5);
		}

		if(!supertile.getWorldObj().isRemote) {
			int muhBalance = BotaniaAPI.internalHandler.getPassiveFlowerDecay();

			if(isPassiveFlower() && muhBalance > 0 && ticksExisted > muhBalance) {
				supertile.getWorldObj().playAuxSFX(2001, supertile.xCoord, supertile.yCoord, supertile.zCoord, Block.getIdFromBlock(supertile.getBlockType()));
				supertile.getWorldObj().setBlockToAir(supertile.xCoord, supertile.yCoord, supertile.zCoord);
			}
		}
	}

	public void linkCollector() {
		final int range = 6;

		boolean needsNew = false;
		if(linkedCollector == null) {
			needsNew = true;

			if(cachedCollectorCoordinates != null) {
				needsNew = false;
				if(supertile.getWorldObj().blockExists(cachedCollectorCoordinates.posX, cachedCollectorCoordinates.posY, cachedCollectorCoordinates.posZ)) {
					needsNew = true;
					TileEntity tileAt = supertile.getWorldObj().getTileEntity(cachedCollectorCoordinates.posX, cachedCollectorCoordinates.posY, cachedCollectorCoordinates.posZ);
					if(tileAt != null && tileAt instanceof IManaCollector) {
						linkedCollector = tileAt;
						needsNew = false;
					}
					cachedCollectorCoordinates = null;
				}
			}
		}

		if(!needsNew && linkedCollector != null) {
			TileEntity tileAt = supertile.getWorldObj().getTileEntity(linkedCollector.xCoord, linkedCollector.yCoord, linkedCollector.zCoord);
			if(!(tileAt instanceof IManaCollector)) {
				linkedCollector = null;
				needsNew = true;
			} else linkedCollector = tileAt;
		}

		if(needsNew) {
			IManaNetwork network = BotaniaAPI.internalHandler.getManaNetworkInstance();
			int size = network.getAllCollectorsInWorld(supertile.getWorldObj()).size();
			if(BotaniaAPI.internalHandler.shouldForceCheck() || size != sizeLastCheck) {
				ChunkCoordinates coords = new ChunkCoordinates(supertile.xCoord, supertile.yCoord, supertile.zCoord);
				linkedCollector = network.getClosestCollector(coords, supertile.getWorldObj(), range);
				sizeLastCheck = size;
			}
		}
	}

	public void addMana(int mana) {
		this.mana = Math.min(getMaxMana(), this.mana + mana);
	}

	public void emptyManaIntoCollector() {
		if(linkedCollector != null) {
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
		if(!cmp.hasKey(TAG_TICKS_EXISTED))
			ticksExisted = cmp.getInteger(TAG_TICKS_EXISTED);

		int x = cmp.getInteger(TAG_COLLECTOR_X);
		int y = cmp.getInteger(TAG_COLLECTOR_Y);
		int z = cmp.getInteger(TAG_COLLECTOR_Z);

		cachedCollectorCoordinates = new ChunkCoordinates(x, y, z);
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_MANA, mana);
		cmp.setInteger(TAG_TICKS_EXISTED, ticksExisted);

		int x = linkedCollector == null ? 0 : linkedCollector.xCoord;
		int y = linkedCollector == null ? -1 : linkedCollector.yCoord;
		int z = linkedCollector == null ? 0 : linkedCollector.zCoord;

		cmp.setInteger(TAG_COLLECTOR_X, x);
		cmp.setInteger(TAG_COLLECTOR_Y, y);
		cmp.setInteger(TAG_COLLECTOR_Z, z);
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

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res) {
		String name = StatCollector.translateToLocal("tile.botania:flower." + getUnlocalizedName() + ".name");
		int color = getColor();
		BotaniaAPI.internalHandler.drawSimpleManaHUD(color, knownMana, getMaxMana(), name, res);
	}

}
