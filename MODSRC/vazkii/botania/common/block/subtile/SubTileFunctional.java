/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 24, 2014, 8:03:44 PM (GMT)]
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
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ManaNetworkHandler;

public class SubTileFunctional extends SubTileEntity {

	private static final String TAG_MANA = "mana";

	private static final String TAG_POOL_X = "poolX";
	private static final String TAG_POOL_Y = "poolY";
	private static final String TAG_POOL_Z = "poolZ";

	public int mana;

	int sizeLastCheck = -1;
	TileEntity linkedPool = null;
	public int knownMana = -1;

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		linkPool();

		if(linkedPool != null) {
			IManaPool pool = (IManaPool) linkedPool;
			int manaInPool = pool.getCurrentMana();
			int manaMissing = getMaxMana() - mana;
			int manaToRemove = Math.min(manaMissing, manaInPool);
			pool.recieveMana(-manaToRemove);
			addMana(manaToRemove);
		}

		if(supertile.worldObj.isRemote) {
			double particleChance = 1F - (double) mana / (double) getMaxMana() / 2F;
			Color color = new Color(getColor());
			if(Math.random() > particleChance)
				Botania.proxy.sparkleFX(supertile.worldObj, supertile.xCoord + 0.3 + Math.random() * 0.5, supertile.yCoord + 0.5 + Math.random()  * 0.5, supertile.zCoord + 0.3 + Math.random() * 0.5, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, (float) Math.random(), 5);
		}
	}

	public void linkPool() {
		final int range = 6;

		boolean needsNew = false;
		if(linkedPool == null)
			needsNew = true;

		if(!needsNew) {
			TileEntity tileAt = supertile.worldObj.getBlockTileEntity(linkedPool.xCoord, linkedPool.yCoord, linkedPool.zCoord);
			if(!(tileAt instanceof IManaPool)) {
				linkedPool = null;
				needsNew = true;
			} else linkedPool = tileAt;
		}

		if(needsNew) {
			int size = ManaNetworkHandler.instance.getAmount(ManaNetworkHandler.instance.manaPools, supertile.worldObj.provider.dimensionId);
			if(size != sizeLastCheck) {
				ChunkCoordinates coords = new ChunkCoordinates(supertile.xCoord, supertile.yCoord, supertile.zCoord);
				linkedPool = ManaNetworkHandler.instance.getClosestPool(coords, supertile.worldObj.provider.dimensionId, range);
				sizeLastCheck = size;
			}
		}
	}

	public void addMana(int mana) {
		this.mana = Math.min(getMaxMana(), this.mana + mana);
	}

	@Override
	public boolean onWanded(EntityPlayer player, ItemStack wand) {
		knownMana = mana;
		player.worldObj.playSoundAtEntity(player, "random.orb", 1F, 1F);

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

		if(supertile.worldObj != null) {
			TileEntity tileAt = supertile.worldObj.getBlockTileEntity(x, y, z);
			if(tileAt != null && tileAt instanceof IManaPool)
				linkedPool = tileAt;
		}
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_MANA, mana);

		int x = linkedPool == null ? 0 : linkedPool.xCoord;
		int y = linkedPool == null ? -1 : linkedPool.yCoord;
		int z = linkedPool == null ? 0 : linkedPool.zCoord;

		cmp.setInteger(TAG_POOL_X, x);
		cmp.setInteger(TAG_POOL_Y, y);
		cmp.setInteger(TAG_POOL_Z, z);
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res) {
		String name = StatCollector.translateToLocal("tile.botania:flower." + getUnlocalizedName() + ".name");
		int color = 0x66000000 | getColor();
		HUDHandler.drawSimpleManaHUD(color, knownMana, getMaxMana(), name, res);
	}

}
