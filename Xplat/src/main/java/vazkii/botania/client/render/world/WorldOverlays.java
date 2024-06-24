package vazkii.botania.client.render.world;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.world.level.Level;

import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.fx.BoltRenderer;
import vazkii.botania.common.item.AssemblyHaloItem;

public final class WorldOverlays {
	public static void renderWorldLast(Camera camera, float tickDelta, PoseStack matrix, RenderBuffers buffers, Level level) {
		BoltRenderer.onWorldRenderLast(camera, tickDelta, matrix, buffers);
		AssemblyHaloItem.Rendering.onRenderWorldLast(camera, tickDelta, matrix, buffers);
		BoundBlockRenderer.onWorldRenderLast(camera, matrix, level);
		AstrolabePreviewHandler.onWorldRenderLast(matrix, buffers, level);
		RenderHelper.onWorldRenderLast();
	}

	private WorldOverlays() {}
}
