/**
 * This class was created by <SoundLogic>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 16, 2015, 8:30:41 PM (GMT)]
 */
package vazkii.botania.common.integration.coloredlights;

import vazkii.botania.common.Botania;

/*
 * This class acts as ColoredLightHelper if the colored lights mod is installed, and leaves the light values unmodified otherwise
 */
public class LightHelper {

	public static int makeRGBLightValue(float r, float g, float b, float currentLightValue) {
		return Botania.lightHelper.makeRGBLightValue(r, g, b, (int) currentLightValue);
	}

	public static int getPackedColor(int meta, int light) {
		return Botania.lightHelper.getPackedColor(meta, light);
	}
}
