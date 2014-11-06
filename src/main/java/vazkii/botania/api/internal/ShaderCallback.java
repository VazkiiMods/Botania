/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Oct 29, 2014, 6:31:35 PM (GMT)]
 */
package vazkii.botania.api.internal;

/**
 * A Callback for when a shader is called. Used to define shader uniforms.
 */
public abstract class ShaderCallback {
	
	public abstract void call(int shader);
	
}
