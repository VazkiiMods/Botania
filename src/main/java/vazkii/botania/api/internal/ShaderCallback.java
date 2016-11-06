/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Oct 29, 2014, 6:31:35 PM (GMT)]
 */
package vazkii.botania.api.internal;

/**
 * A Callback for when a shader is called. Used to define shader uniforms.
 */
@FunctionalInterface
public interface ShaderCallback {

	void call(int shader);

}
