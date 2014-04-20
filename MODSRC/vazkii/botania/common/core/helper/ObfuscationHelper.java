/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 *
 * File Created @ [Jan 17, 2014, 4:52:37 PM (GMT)]
 */
package vazkii.botania.common.core.helper;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.lib.LibObfuscation;

public class ObfuscationHelper {

    public static ResourceLocation getParticleTexture() {
        return ReflectionHelper.getPrivateValue(EffectRenderer.class, null, LibObfuscation.PARTICLE_TEXTURES);
    }
}
