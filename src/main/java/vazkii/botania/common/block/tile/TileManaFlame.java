/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Dec 21, 2014, 12:33:12 AM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.nbt.NBTTagCompound;
import vazkii.botania.common.Botania;
import vazkii.botania.common.integration.coloredlights.ColoredLightHelper;

public class TileManaFlame extends TileMod {

	private static final String TAG_COLOR = "color";

	int color = 0x20FF20;

	int lightColor = -1;

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

			float r = (float) (color >> 16 & 0xFF) / 0xFF + (float) (Math.random() - 0.5) * v;
			float g = (float) (color >> 8 & 0xFF) / 0xFF + (float) (Math.random() - 0.5) * v;
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

	public int getLightColor() {
		if(lightColor == -1) {
			float r = (float) (color >> 16 & 0xFF) / 0xFF;
			float g = (float) (color >> 8 & 0xFF) / 0xFF;
			float b = (float) (color & 0xFF) / 0xFF;
			lightColor = ColoredLightHelper.makeRGBLightValue(r, g, b, 1F);
		}

		return lightColor;
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
