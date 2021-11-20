/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import it.unimi.dsi.fastutil.objects.ObjectList;

import net.minecraft.client.renderer.model.ModelRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ModelRenderer.class)
public interface AccessorModelRenderer {
	@Accessor("cubeList")
	ObjectList<ModelRenderer.ModelBox> botania_getCubeList();
}
