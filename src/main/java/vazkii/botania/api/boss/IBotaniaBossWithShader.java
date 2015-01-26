/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Oct 29, 2014, 6:13:21 PM (GMT)]
 */
package vazkii.botania.api.boss;

import vazkii.botania.api.internal.ShaderCallback;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * A Botania boss whose HP bar makes use of shaders. Shaders
 * used through this have a few uniforms available:
 * <li><b><i>int</i> time</b> - The amount of total ticks elapsed.</li>
 * <li><b><i>int</i> startX</b> - The U coordinate passed in for the render of this bar's texture.</li>
 * <li><b><i>int</i> startY</b> - The V coordinate passed in for the render of this bar's texture.</li>
 */
public interface IBotaniaBossWithShader extends IBotaniaBoss {

	/**
	 * The Shader Program to use for this boss bar. Return 0 case
	 * you don't want a shader to be used. You can use separate shaders
	 * for the background and foreground.
	 * @param background True if rendering the background of the boss bar,
	 * false if rendering the bar itself that shows the HP.
	 */
	@SideOnly(Side.CLIENT)
	public int getBossBarShaderProgram(boolean background);

	/**
	 * A callback for the shader, used to pass in uniforms. Return null for no callback.
	 */
	@SideOnly(Side.CLIENT)
	public ShaderCallback getBossBarShaderCallback(boolean background, int shader);

}
