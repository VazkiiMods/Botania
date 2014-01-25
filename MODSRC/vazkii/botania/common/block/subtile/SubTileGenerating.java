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
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.common.Botania;

public class SubTileGenerating extends SubTileEntity {

	private static final String TAG_MANA = "mana";
	int mana;
	public int knownMana = -1;
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		int delay = getDelayBetweenPassiveGeneration();
		if(delay > 0 && supertile.worldObj.getWorldTime() % delay == 0)
			addMana(1);
		
		double particleChance = 1F - ((double) mana / (double) getMaxMana()) / 2F;
		Color color = new Color(getColor());
		if(Math.random() > particleChance)
			Botania.proxy.sparkleFX(supertile.worldObj, supertile.xCoord + 0.3 + Math.random() * 0.5, supertile.yCoord + 0.5 + Math.random()  * 0.5, supertile.zCoord + 0.3 + Math.random() * 0.5, (float) color.getRed() / 255F, (float) color.getGreen() / 255F, (float) color.getBlue() / 255F, (float) Math.random(), 5);

	}
	
	public void addMana(int mana) {
		this.mana = Math.min(getMaxMana(), this.mana + mana);
	}
	
	public int getDelayBetweenPassiveGeneration() {
		return 30;
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
		cmp.setInteger(TAG_MANA, mana);
	}
	
	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		mana = cmp.getInteger(TAG_MANA);
	}
	
	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res) {
		String name = StatCollector.translateToLocal("tile.botania:flower." + getUnlocalizedName() + ".name");
		int type = 0;
		if(knownMana >= 0) {
			type = 1;
			double percentage = (double) knownMana / (double) getMaxMana() * 100;
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
		int color = 0x66000000 | getColor();
		mc.fontRenderer.drawStringWithShadow(name, res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(name) / 2, res.getScaledHeight() / 2 + 10, color);
		mc.fontRenderer.drawStringWithShadow(filling, res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(filling) / 2, res.getScaledHeight() / 2 + 20, color);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
}
