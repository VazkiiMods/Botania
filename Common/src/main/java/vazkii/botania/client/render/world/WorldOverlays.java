package vazkii.botania.client.render.world;

import com.mojang.blaze3d.vertex.PoseStack;

import vazkii.botania.client.core.handler.AstrolabePreviewHandler;
import vazkii.botania.client.core.handler.BoundTileRenderer;
import vazkii.botania.client.fx.BoltRenderer;
import vazkii.botania.client.render.entity.RenderMagicLandmine;
import vazkii.botania.common.item.ItemCraftingHalo;

public final class WorldOverlays {
	public static void renderWorldLast(float tickDelta, PoseStack matrix) {
		// todo 1.17 these don't work in fabulous, need a better injection site
		BoltRenderer.onWorldRenderLast(tickDelta, matrix);
		ItemCraftingHalo.Rendering.onRenderWorldLast(tickDelta, matrix);
		BoundTileRenderer.onWorldRenderLast(matrix);
		AstrolabePreviewHandler.onWorldRenderLast(matrix);
		RenderMagicLandmine.onWorldRenderLast();
	}

	private WorldOverlays() {}
}
