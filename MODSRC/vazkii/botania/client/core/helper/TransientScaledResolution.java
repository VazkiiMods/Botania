/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 11, 2014, 2:18:35 PM (GMT)]
 */
package vazkii.botania.client.core.helper;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MathHelper;

// Copy of ScaledResolution with 1.7.2 and 1.7.10 support. Will drop once 1.7.10 is in full use
public class TransientScaledResolution {
	
	private int scaledWidth;
	private int scaledHeight;
	private double scaledWidthD;
	private double scaledHeightD;
	private int scaleFactor;

	public TransientScaledResolution(GameSettings par1GameSettings, int par2, int par3) {
		this.scaledWidth = par2;
		this.scaledHeight = par3;
		this.scaleFactor = 1;
		int k = par1GameSettings.guiScale;

		if (k == 0)
			k = 1000;

		while (this.scaleFactor < k && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240)
			++this.scaleFactor;

		this.scaledWidthD = (double)this.scaledWidth / (double)this.scaleFactor;
		this.scaledHeightD = (double)this.scaledHeight / (double)this.scaleFactor;
		this.scaledWidth = MathHelper.ceiling_double_int(this.scaledWidthD);
		this.scaledHeight = MathHelper.ceiling_double_int(this.scaledHeightD);
	}

	public int getScaledWidth() {
		return this.scaledWidth;
	}

	public int getScaledHeight() {
		return this.scaledHeight;
	}

	public double getScaledWidth_double() {
		return this.scaledWidthD;
	}

	public double getScaledHeight_double() {
		return this.scaledHeightD;
	}

	public int getScaleFactor() {
		return this.scaleFactor;
	}
}