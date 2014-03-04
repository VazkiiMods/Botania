/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 24, 2014, 8:03:36 PM (GMT)]
 */
package vazkii.botania.common.block.subtile;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.SubTileEntity;
import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ManaNetworkHandler;

public class SubTileGenerating extends SubTileEntity {

	private static final String TAG_MANA = "mana";

	private static final String TAG_COLLECTOR_X = "collectorX";
	private static final String TAG_COLLECTOR_Y = "collectorY";
	private static final String TAG_COLLECTOR_Z = "collectorZ";

	protected int mana;

	int sizeLastCheck = -1;
	protected TileEntity linkedCollector = null;
	public int knownMana = -1;

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		linkCollector();

		if(canGeneratePassively()) {
			int delay = getDelayBetweenPassiveGeneration();
			if(delay > 0 && supertile.worldObj.getWorldTime() % delay == 0)
				addMana(getValueForPassiveGeneration());
			emptyManaIntoCollector();
		}

		if(supertile.worldObj.isRemote) {
			double particleChance = 1F - (double) mana / (double) getMaxMana() / 2F;
			Color color = new Color(getColor());
			if(Math.random() > particleChance)
				Botania.proxy.sparkleFX(supertile.worldObj, supertile.xCoord + 0.3 + Math.random() * 0.5, supertile.yCoord + 0.5 + Math.random()  * 0.5, supertile.zCoord + 0.3 + Math.random() * 0.5, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, (float) Math.random(), 5);
		}
	}

	public void linkCollector() {
		final int range = 6;

		boolean needsNew = false;
		if(linkedCollector == null)
			needsNew = true;

		if(!needsNew) {
			TileEntity tileAt = supertile.worldObj.getBlockTileEntity(linkedCollector.xCoord, linkedCollector.yCoord, linkedCollector.zCoord);
			if(!(tileAt instanceof IManaCollector)) {
				linkedCollector = null;
				needsNew = true;
			} else linkedCollector = tileAt;
		}

		if(needsNew) {
			int size = ManaNetworkHandler.instance.getAmount(ManaNetworkHandler.instance.manaCollectors, supertile.worldObj.provider.dimensionId);
			if(size != sizeLastCheck) {
				ChunkCoordinates coords = new ChunkCoordinates(supertile.xCoord, supertile.yCoord, supertile.zCoord);
				linkedCollector = ManaNetworkHandler.instance.getClosestCollector(coords, supertile.worldObj.provider.dimensionId, range);
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
			if(!collector.isFull()) {
				collector.recieveMana(mana);
				mana = 0;
			}
		}
	}

	public boolean canGeneratePassively() {
		boolean rain = supertile.worldObj.getWorldChunkManager().getBiomeGenAt(supertile.xCoord, supertile.zCoord).getIntRainfall() > 0 && (supertile.worldObj.isRaining() || supertile.worldObj.isThundering());
		return supertile.worldObj.isDaytime() && !rain && supertile.worldObj.canBlockSeeTheSky(supertile.xCoord, supertile.yCoord + 1, supertile.zCoord);
	}

	public int getDelayBetweenPassiveGeneration() {
		return 20;
	}

	public int getValueForPassiveGeneration() {
		return 1;
	}

	@Override
	public boolean onWanded(EntityPlayer player, ItemStack wand) {
		knownMana = mana;
		player.worldObj.playSoundAtEntity(player, "random.orb", 0.1F, 1F);

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

		int x = cmp.getInteger(TAG_COLLECTOR_X);
		int y = cmp.getInteger(TAG_COLLECTOR_Y);
		int z = cmp.getInteger(TAG_COLLECTOR_Z);

		if(supertile.worldObj != null) {
			TileEntity tileAt = supertile.worldObj.getBlockTileEntity(x, y, z);
			if(tileAt != null && tileAt instanceof IManaCollector)
				linkedCollector = tileAt;
		}
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_MANA, mana);

		int x = linkedCollector == null ? 0 : linkedCollector.xCoord;
		int y = linkedCollector == null ? -1 : linkedCollector.yCoord;
		int z = linkedCollector == null ? 0 : linkedCollector.zCoord;

		cmp.setInteger(TAG_COLLECTOR_X, x);
		cmp.setInteger(TAG_COLLECTOR_Y, y);
		cmp.setInteger(TAG_COLLECTOR_Z, z);
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res) {
		String name = StatCollector.translateToLocal("tile.botania:flower." + getUnlocalizedName() + ".name");
		int color = 0x66000000 | getColor();
		HUDHandler.drawSimpleManaHUD(color, knownMana, getMaxMana(), name, res);
	}

}
