/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.client.renderer.texture.AtlasSet;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(ModelBakery.class)
public interface AccessorModelBakery {
	@Accessor("UNREFERENCED_TEXTURES")
	static Set<Material> getMaterials() {
		throw new IllegalStateException();
	}

	@Accessor("atlasSet")
	AtlasSet getSpriteAtlasManager();
}
