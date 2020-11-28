/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.SpriteAtlasManager;
import net.minecraft.client.util.SpriteIdentifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(ModelLoader.class)
public interface AccessorModelBakery {
	@Accessor("DEFAULT_TEXTURES")
	static Set<SpriteIdentifier> getMaterials() {
		throw new IllegalStateException();
	}

	@Accessor
	SpriteAtlasManager getSpriteAtlasManager();
}
