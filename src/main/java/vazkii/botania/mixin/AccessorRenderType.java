/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.RenderType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderType.class)
public interface AccessorRenderType {
	@Invoker("create")
	static RenderType.CompositeRenderType create(String string, VertexFormat vertexFormat,
			VertexFormat.Mode mode, int bufSize, boolean hasCrumbling, boolean sortOnUpload,
			RenderType.CompositeState compositeState) {
		throw new IllegalStateException();
	}
}
