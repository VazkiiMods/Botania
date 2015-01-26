/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 17, 2014, 4:52:37 PM (GMT)]
 */
package vazkii.botania.common.core.helper;

import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.lib.LibObfuscation;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class ObfuscationHelper {

	public static ResourceLocation getParticleTexture() {
		return ReflectionHelper.getPrivateValue(EffectRenderer.class, null, LibObfuscation.PARTICLE_TEXTURES);
	}
}
