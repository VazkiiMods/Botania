/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 26, 2014, 12:23:55 AM (GMT)]
 */
package vazkii.botania.common.block.tile;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import vazkii.botania.api.internal.ManaNetworkEvent;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lib.LibBlockNames;

public class TilePool extends TileSimpleInventory implements IManaPool {

	public static final int MAX_MANA = 1000000;

	private static final String TAG_MANA = "mana";
	private static final String TAG_KNOWN_MANA = "knownMana";

	int mana;
	int knownMana = -1;
	boolean added = false;

	@Override
	public boolean isFull() {
		return mana >= MAX_MANA;
	}

	@Override
	public void recieveMana(int mana) {
		this.mana = Math.min(this.mana + mana, MAX_MANA);
	}

	@Override
	public void invalidate() {
		super.invalidate();
		ManaNetworkEvent.removePool(this);
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		ManaNetworkEvent.removePool(this);
	}

	@Override
	public void updateEntity() {
		if(!added) {
			ManaNetworkEvent.addPool(this);
			added = true;
		}
		if(worldObj.isRemote) {
			double particleChance = 1F - ((double) getCurrentMana() / (double) MAX_MANA) * 0.25;
			Color color = new Color(0x00C6FF);
			if(Math.random() > particleChance)
				Botania.proxy.wispFX(worldObj, xCoord + 0.3 + Math.random() * 0.5, yCoord + 0.6 + Math.random() * 0.25, zCoord + Math.random(), (float) color.getRed() / 255F, (float) color.getGreen() / 255F, (float) color.getBlue() / 255F, (float) Math.random() / 3F, (float) -Math.random() / 25F);
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_MANA, mana);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		mana = cmp.getInteger(TAG_MANA);

		if(cmp.hasKey(TAG_KNOWN_MANA))
			knownMana = cmp.getInteger(TAG_KNOWN_MANA);
	}

	public void onWanded(EntityPlayer player, ItemStack wand) {
		if(!worldObj.isRemote) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			writeCustomNBT(nbttagcompound);
			nbttagcompound.setInteger(TAG_KNOWN_MANA, getCurrentMana());
			PacketDispatcher.sendPacketToPlayer(new Packet132TileEntityData(xCoord, yCoord, zCoord, -999, nbttagcompound), (Player) player);
		}
		worldObj.playSoundAtEntity(player, "random.orb", 1F, 1F);
	}

	public void renderHUD(Minecraft mc, ScaledResolution res) {
		String name = ModBlocks.pool.getLocalizedName();
		int color = 0x660000FF;
		HUDHandler.drawSimpleManaHUD(color, knownMana, MAX_MANA, name, res);
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return true;
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public String getInvName() {
		return LibBlockNames.POOL;
	}
}
