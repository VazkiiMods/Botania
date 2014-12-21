/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Dec 21, 2014, 12:33:12 AM (GMT)]
 */
package vazkii.botania.common.block.tile;

import vazkii.botania.common.Botania;
import net.minecraft.nbt.NBTTagCompound;

public class TileManaFlame extends TileMod {

	private static final String TAG_COLOR = "color";
	
	int color = 0x20FF20;
	
	public void setColor(int color) {
		this.color = color;
	}
	
	public int getColor() {
		return color;
	}
	
	@Override
	public void updateEntity() {
		float c = 0.3F;
		
		if(Math.random() < c) {
			float v = 0.1F;
			
			float r = (float) ((color >> 16) & 0xFF) / 0xFF + (float) (Math.random() - 0.5) * v;
			float g = (float) ((color >> 8) & 0xFF) / 0xFF + (float) (Math.random() - 0.5) * v;
			float b = (float) (color & 0xFF) / 0xFF + (float) (Math.random() - 0.5) * v;

			float w = 0.15F;
			float h = 0.05F;
			double x = xCoord + 0.5 + (Math.random() - 0.5) * w;
			double y = yCoord + 0.25 + (Math.random() - 0.5) * h;
			double z = zCoord + 0.5 + (Math.random() - 0.5) * w;
			
			float s = 0.2F + (float) Math.random() * 0.1F;
			float m = 0.03F + (float) Math.random() * 0.015F;
			
			Botania.proxy.wispFX(worldObj, x, y, z, r, g, b, s, -m);
		}
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_COLOR, color);
	}
	
	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		color = cmp.getInteger(TAG_COLOR);
	}
	
}
