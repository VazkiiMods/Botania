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

/*
 * This is an implementation of ILightHelper for when the colored lights mod is installed
 */
public class LightHelperColored implements ILightHelper {

	@Override
	public int makeRGBLightValue(float r, float g, float b, int currentLightValue) {
		return ColoredLightHelper.makeRGBLightValue(r, g, b, currentLightValue);
	}

	@Override
	public int getPackedColor(int meta, int light) {
		return ColoredLightHelper.getPackedColor(meta, light);
	}

}
