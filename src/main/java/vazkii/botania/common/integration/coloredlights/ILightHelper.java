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
 * This interface is use for wrappers for compatibility with the colored lights mod.
 * It has one version for colored lights, and one version which leaves the light values unmodified for vanilla
 */
public interface ILightHelper {

	public int makeRGBLightValue(float r, float g, float b, int currentLightValue);
	public int getPackedColor(int meta, int light);
}
