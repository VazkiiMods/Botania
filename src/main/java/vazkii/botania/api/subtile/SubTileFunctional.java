/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 24, 2014, 8:03:44 PM (GMT)]
 */
package vazkii.botania.api.subtile;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.api.mana.IManaPool;

/**
 * The basic class for a Functional Flower.
 */
public class SubTileFunctional extends SubTileEntity {

	public static final int RANGE = 10;

	private static final String TAG_MANA = "mana";

	private static final String TAG_POOL_X = "poolX";
	private static final String TAG_POOL_Y = "poolY";
	private static final String TAG_POOL_Z = "poolZ";

	public int mana;

	public int redstoneSignal = 0;

	int sizeLastCheck = -1;
	TileEntity linkedPool = null;
	public int knownMana = -1;

	ChunkCoordinates cachedPoolCoordinates = null;

	/**
	 * If set to true, redstoneSignal will be updated every tick.
	 */
	public boolean acceptsRedstone() {
		return false;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		linkPool();

		if(linkedPool != null && isValidBinding()) {
			IManaPool pool = (IManaPool) linkedPool;
			int manaInPool = pool.getCurrentMana();
			int manaMissing = getMaxMana() - mana;
			int manaToRemove = Math.min(manaMissing, manaInPool);
			pool.recieveMana(-manaToRemove);
			addMana(manaToRemove);
		}

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
	}

	public void linkPool() {
		boolean needsNew = false;
		if(linkedPool == null) {
			needsNew = true;

			if(cachedPoolCoordinates != null) {
				needsNew = false;
				if(supertile.getWorldObj().blockExists(cachedPoolCoordinates.posX, cachedPoolCoordinates.posY, cachedPoolCoordinates.posZ)) {
					needsNew = true;
					TileEntity tileAt = supertile.getWorldObj().getTileEntity(cachedPoolCoordinates.posX, cachedPoolCoordinates.posY, cachedPoolCoordinates.posZ);
					if(tileAt != null && tileAt instanceof IManaPool && !tileAt.isInvalid()) {
						linkedPool = tileAt;
						needsNew = false;
					}
					cachedPoolCoordinates = null;
				}
			}
		} else {
			TileEntity tileAt = supertile.getWorldObj().getTileEntity(linkedPool.xCoord, linkedPool.yCoord, linkedPool.zCoord);
			if(tileAt != null && tileAt instanceof IManaPool)
				linkedPool = tileAt;
		}

		if(needsNew && ticksExisted == 1) { // Only for new flowers
			IManaNetwork network = BotaniaAPI.internalHandler.getManaNetworkInstance();
			int size = network.getAllPoolsInWorld(supertile.getWorldObj()).size();
			if(BotaniaAPI.internalHandler.shouldForceCheck() || size != sizeLastCheck) {
				ChunkCoordinates coords = new ChunkCoordinates(supertile.xCoord, supertile.yCoord, supertile.zCoord);
				linkedPool = network.getClosestPool(coords, supertile.getWorldObj(), RANGE);
				sizeLastCheck = size;
			}
		}
	}

	public void linkToForcefully(TileEntity pool) {
		linkedPool = pool;
	}

	public void addMana(int mana) {
		this.mana = Math.min(getMaxMana(), this.mana + mana);
	}

	@Override
	public boolean onWanded(EntityPlayer player, ItemStack wand) {
		if(player == null)
			return false;

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

		int x = cmp.getInteger(TAG_POOL_X);
		int y = cmp.getInteger(TAG_POOL_Y);
		int z = cmp.getInteger(TAG_POOL_Z);

		cachedPoolCoordinates = y < 0 ? null : new ChunkCoordinates(x, y, z);
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_MANA, mana);

		if(cachedPoolCoordinates != null) {
			cmp.setInteger(TAG_POOL_X, cachedPoolCoordinates.posX);
			cmp.setInteger(TAG_POOL_Y, cachedPoolCoordinates.posY);
			cmp.setInteger(TAG_POOL_Z, cachedPoolCoordinates.posZ);
		} else {
			int x = linkedPool == null ? 0 : linkedPool.xCoord;
			int y = linkedPool == null ? -1 : linkedPool.yCoord;
			int z = linkedPool == null ? 0 : linkedPool.zCoord;

			cmp.setInteger(TAG_POOL_X, x);
			cmp.setInteger(TAG_POOL_Y, y);
			cmp.setInteger(TAG_POOL_Z, z);
		}
	}

	@Override
	public ChunkCoordinates getBinding() {
		if(linkedPool == null)
			return null;
		return new ChunkCoordinates(linkedPool.xCoord, linkedPool.yCoord, linkedPool.zCoord);
	}

	@Override
	public boolean canSelect(EntityPlayer player, ItemStack wand, int x, int y, int z, int side) {
		return true;
	}

	@Override
	public boolean bindTo(EntityPlayer player, ItemStack wand, int x, int y, int z, int side) {
		int range = 10;
		range *= range;

		double dist = (x - supertile.xCoord) * (x - supertile.xCoord) + (y - supertile.yCoord) * (y - supertile.yCoord) + (z - supertile.zCoord) * (z - supertile.zCoord);
		if(range >= dist) {
			TileEntity tile = player.worldObj.getTileEntity(x, y, z);
			if(tile instanceof IManaPool) {
				linkedPool = tile;
				return true;
			}
		}

		return false;
	}

	public boolean isValidBinding() {
		return linkedPool != null && !linkedPool.isInvalid() && supertile.getWorldObj().getTileEntity(linkedPool.xCoord, linkedPool.yCoord, linkedPool.zCoord) == linkedPool;
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res) {
		String name = StatCollector.translateToLocal("tile.botania:flower." + getUnlocalizedName() + ".name");
		int color = getColor();
		BotaniaAPI.internalHandler.drawComplexManaHUD(color, knownMana, getMaxMana(), name, res, BotaniaAPI.internalHandler.getBindDisplayForFlowerType(this), isValidBinding());
	}

}
