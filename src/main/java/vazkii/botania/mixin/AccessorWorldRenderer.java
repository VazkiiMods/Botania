/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldRenderer.class)
public interface AccessorWorldRenderer {
	@Accessor
	VertexFormat getSkyVertexFormat();

	@Accessor("lightSkyBuffer")
	VertexBuffer getSkyVBO();

	@Accessor("starsBuffer")
	VertexBuffer getStarVBO();
}
