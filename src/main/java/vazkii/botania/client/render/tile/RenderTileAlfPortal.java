/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix3f;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.AlfPortalState;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.common.block.tile.TileAlfPortal;

import javax.annotation.Nonnull;

public class RenderTileAlfPortal extends TileEntityRenderer<TileAlfPortal> {

	public RenderTileAlfPortal(TileEntityRendererDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nonnull TileAlfPortal portal, float f, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		AlfPortalState state = portal.getBlockState().get(BotaniaStateProps.ALFPORTAL_STATE);
		if (state == AlfPortalState.OFF) {
			return;
		}

		ms.push();
		ms.translate(-1F, 1F, 0.25F);

		float alpha = (float) Math.min(1F, (Math.sin((ClientTickHandler.ticksInGame + f) / 8D) + 1D) / 7D + 0.6D) * (Math.min(60, portal.ticksOpen) / 60F) * 0.5F;

		if (state == AlfPortalState.ON_X) {
			ms.translate(1.25F, 0F, 1.75F);
			ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90F));
		}

		renderIcon(ms, buffers, MiscellaneousIcons.INSTANCE.alfPortalTex, 0, 0, 3, 3, alpha, overlay);

		ms.translate(0F, 0F, 0.5F);
		renderIcon(ms, buffers, MiscellaneousIcons.INSTANCE.alfPortalTex, 0, 0, 3, 3, alpha, overlay);
		ms.pop();
	}

	public void renderIcon(MatrixStack ms, IRenderTypeBuffer buffers, TextureAtlasSprite icon, int x, int y, int width, int height, float alpha, int overlay) {
		IVertexBuilder buffer = buffers.getBuffer(Atlases.getEntityTranslucent());
		Matrix4f model = ms.peek().getModel();
		Matrix3f normal = ms.peek().getNormal();
		buffer.vertex(model, x, y + height, 0).color(1, 1, 1, alpha).texture(icon.getMinU(), icon.getMaxV()).overlay(overlay).light(0xF000F0).normal(normal, 1, 0, 0).endVertex();
		buffer.vertex(model, x + width, y + height, 0).color(1, 1, 1, alpha).texture(icon.getMaxU(), icon.getMaxV()).overlay(overlay).light(0xF000F0).normal(normal, 1, 0, 0).endVertex();
		buffer.vertex(model, x + width, y, 0).color(1, 1, 1, alpha).texture(icon.getMaxU(), icon.getMinV()).overlay(overlay).light(0xF000F0).normal(normal, 1, 0, 0).endVertex();
		buffer.vertex(model, x, y, 0).color(1, 1, 1, alpha).texture(icon.getMinU(), icon.getMinV()).overlay(overlay).light(0xF000F0).normal(normal, 1, 0, 0).endVertex();
	}

}
