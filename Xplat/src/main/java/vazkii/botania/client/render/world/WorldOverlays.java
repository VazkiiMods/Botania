package vazkii.botania.client.render.world;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.world.level.Level;

import vazkii.botania.client.fx.BoltRenderer;
import vazkii.botania.client.render.entity.RenderMagicLandmine;
import vazkii.botania.common.item.ItemCraftingHalo;

public final class WorldOverlays {
	public static void renderWorldLast(Camera camera, float tickDelta, PoseStack matrix, RenderBuffers buffers, Level level) {
		BoltRenderer.onWorldRenderLast(camera, tickDelta, matrix, buffers);
		ItemCraftingHalo.Rendering.onRenderWorldLast(camera, tickDelta, matrix, buffers);
		BoundTileRenderer.onWorldRenderLast(camera, matrix, level);
		AstrolabePreviewHandler.onWorldRenderLast(matrix, buffers, level);
		RenderMagicLandmine.onWorldRenderLast();
	}

	private WorldOverlays() {}
}
