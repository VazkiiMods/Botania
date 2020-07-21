/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.SpriteIdentifier;

@Mixin(ModelLoader.class)
public interface AccessorModelBakery {
	@Accessor("LOCATIONS_BUILTIN_TEXTURES")
	static Set<SpriteIdentifier> getMaterials() {
		throw new IllegalStateException();
	}
}
