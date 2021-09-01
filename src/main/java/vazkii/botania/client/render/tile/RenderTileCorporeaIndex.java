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

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;

import javax.annotation.Nullable;

public class RenderTileCorporeaIndex extends TileEntityRenderer<TileCorporeaIndex> {
	private static final RenderType LAYER = RenderType.getEntityCutoutNoCull(new ResourceLocation(LibResources.MODEL_CORPOREA_INDEX));
	private static final float ANGLE = (float) Math.sin(Math.toRadians(45));
	private final ModelRenderer ring;
	private final ModelRenderer cube;

	public RenderTileCorporeaIndex(TileEntityRendererDispatcher manager) {
		super(manager);
		this.ring = new ModelRenderer(64, 32, 0, 0);
		this.ring.addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);
		this.cube = new ModelRenderer(64, 32, 32, 0);
		this.cube.addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);
	}

	@Override
	public void render(@Nullable TileCorporeaIndex index, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		ms.push();
		ms.translate(0.5, 0, 0.5);
		
		float rotation = (ClientTickHandler.ticksInGame + partialTicks) * 2;
		float translation;
		if(index == null) { //TEISR
			ms.scale(1.3f, 1.3f, 1.3f);
			ms.translate(0, -0.1, 0);
			translation = 0;
		} else {
			translation = (float) ((Math.cos((index.ticksWithCloseby + (index.hasCloseby ? partialTicks : 0)) / 10F) * 0.5 + 0.5) * 0.25);
		}
		
		IVertexBuilder buffer = buffers.getBuffer(LAYER);
		ms.push();
		ms.translate(0.0D, -1, 0.0D);

		ms.rotate(Vector3f.YP.rotationDegrees(rotation));
		ms.translate(0.0D, 1.5F + translation / 2.0F, 0.0D);
		ms.rotate(new Quaternion(new Vector3f(ANGLE, 0.0F, ANGLE), 60.0F, true));
		this.ring.render(ms, buffer, light, overlay);
		ms.scale(0.875F, 0.875F, 0.875F);
		ms.rotate(new Quaternion(new Vector3f(ANGLE, 0.0F, ANGLE), 60.0F, true));
		ms.rotate(Vector3f.YP.rotationDegrees(rotation));
		this.ring.render(ms, buffer, light, overlay);
		ms.scale(0.875F, 0.875F, 0.875F);
		ms.rotate(new Quaternion(new Vector3f(ANGLE, 0.0F, ANGLE), 60.0F, true));
		ms.rotate(Vector3f.YP.rotationDegrees(rotation));
		this.cube.render(ms, buffer, light, overlay);
		ms.pop();

		if (index != null && index.closeby > 0F) {
			float starScale = 0.02F;
			float starRadius = (float) TileCorporeaIndex.RADIUS * index.closeby + (index.closeby == 1F ? 0F : index.hasCloseby ? partialTicks : -partialTicks) * 0.2F;
			double rads = (index.ticksWithCloseby + partialTicks) * 2 * Math.PI / 180;
			double starX = Math.cos(rads) * starRadius;
			double starZ = Math.sin(rads) * starRadius;
			int color = 0xFF00FF;
			int seed = index.getPos().getX() ^ index.getPos().getY() ^ index.getPos().getZ();

			ms.translate(starX, 0.3, starZ);
			RenderHelper.renderStar(ms, buffers, color, starScale, starScale, starScale, seed);
			ms.translate(-starX * 2, 0, -starZ * 2);
			RenderHelper.renderStar(ms, buffers, color, starScale, starScale, starScale, seed);
			ms.translate(starX, 0, starZ);

			rads = -rads;
			starX = Math.cos(rads) * starRadius;
			starZ = Math.sin(rads) * starRadius;
			ms.translate(starX, 0, starZ);
			RenderHelper.renderStar(ms, buffers, color, starScale, starScale, starScale, seed);
			ms.translate(-starX * 2, 0, -starZ * 2);
			RenderHelper.renderStar(ms, buffers, color, starScale, starScale, starScale, seed);
			ms.translate(starX, 0, starZ);
		}
		ms.pop();
	}

}
