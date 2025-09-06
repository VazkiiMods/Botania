package vazkii.botania.client.render.world;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.world.level.Level;

import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.fx.BoltRenderer;
import vazkii.botania.common.item.AssemblyHaloItem;

public final class WorldOverlays {
	public static void renderWorldLast(Camera camera, float entityPartialTick, float playerPartialTick, PoseStack matrix, RenderBuffers buffers, Level level) {
		BoltRenderer.onWorldRenderLast(camera, entityPartialTick, matrix, buffers);
		AssemblyHaloItem.Rendering.onRenderWorldLast(camera, playerPartialTick, matrix, buffers);
		BoundBlockRenderer.onWorldRenderLast(camera, playerPartialTick, matrix, level);
		AstrolabePreviewHandler.onWorldRenderLast(matrix, buffers, level);
		RenderHelper.onWorldRenderLast();
	}

	private WorldOverlays() {}
}
