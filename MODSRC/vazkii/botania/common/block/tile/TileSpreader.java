/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 25, 2014, 9:40:57 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.internal.ManaNetworkEvent;
import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ItemTwigWand;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class TileSpreader extends TileMod implements IManaCollector {

	private static final int MAX_MANA = 1000;
	private static final String TAG_MANA = "mana";
	private static final String TAG_KNOWN_MANA = "knownMana";
	private static final String TAG_ROTATION_X = "rotationX";
	private static final String TAG_ROTATION_Y = "rotationY";

	int mana;
	int knownMana = -1;
	public float rotationX, rotationY;
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
		ManaNetworkEvent.removeCollector(this);
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		ManaNetworkEvent.removeCollector(this);
	}

	@Override
	public void updateEntity() {
		if(!added) {
			ManaNetworkEvent.addCollector(this);
			added = true;
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_MANA, mana);
		cmp.setFloat(TAG_ROTATION_X, rotationX);
		cmp.setFloat(TAG_ROTATION_Y, rotationY);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		mana = cmp.getInteger(TAG_MANA);
		rotationX = cmp.getFloat(TAG_ROTATION_X);
		rotationY = cmp.getFloat(TAG_ROTATION_Y);

		if(cmp.hasKey(TAG_KNOWN_MANA))
			knownMana = cmp.getInteger(TAG_KNOWN_MANA);
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return true;
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	public void onWanded(EntityPlayer player, ItemStack wand) {
		if(!player.isSneaking()) {
			if(!worldObj.isRemote) {
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				writeCustomNBT(nbttagcompound);
				nbttagcompound.setInteger(TAG_KNOWN_MANA, mana);
				PacketDispatcher.sendPacketToPlayer(new Packet132TileEntityData(xCoord, yCoord, zCoord, -999, nbttagcompound), (Player) player);
			}
			worldObj.playSoundAtEntity(player, "random.orb", 1F, 1F);
		} else {
			rotationX = -player.rotationYaw;
			rotationY = player.rotationPitch;
			PacketDispatcher.sendPacketToAllInDimension(getDescriptionPacket(), worldObj.provider.dimensionId);
		}
	}

	public void renderHUD(Minecraft mc, ScaledResolution res) {
		String name = ModBlocks.spreader.getLocalizedName();
		int type = 0;
		if(knownMana >= 0) {
			type = 1;
			double percentage = (double) knownMana / (double) MAX_MANA * 100;
			if(percentage == 100)
				type = 5;
			else if(percentage >= 75)
				type = 4;
			else if(percentage >= 50)
				type = 3;
			else if(percentage > 0)
				type = 2;
		}
		String filling = StatCollector.translateToLocal("botaniamisc.status" + type);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		int color = 0x66FFFFFF;
		mc.fontRenderer.drawStringWithShadow(name, res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(name) / 2, res.getScaledHeight() / 2 + 10, color);
		mc.fontRenderer.drawStringWithShadow(filling, res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(filling) / 2, res.getScaledHeight() / 2 + 20, color);
		GL11.glDisable(GL11.GL_BLEND);
	}

}
