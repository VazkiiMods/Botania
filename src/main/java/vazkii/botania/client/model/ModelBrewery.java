/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Oct 31, 2014, 4:33:55 PM (GMT)]
 */
package vazkii.botania.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;
import vazkii.botania.client.render.tile.RenderTileBrewery;
import vazkii.botania.common.block.tile.TileBrewery;

import javax.annotation.Nullable;

public class ModelBrewery extends Model {

	final ModelRenderer top;
	final ModelRenderer pole;
	final ModelRenderer bottom;
	
	final ModelRenderer plate;

	public ModelBrewery() {
		super(RenderType::getEntitySolid);

		textureWidth = 32;
		textureHeight = 16;
		
		top = new ModelRenderer(this, 8, 0);
        top.setRotationPoint(0.0F, 16.0F, 0.0F);
        top.addCuboid(-2.0F, -7.0F, -2.0F, 4, 1, 4, 0.0F);
		pole = new ModelRenderer(this, 0, 0);
        pole.setRotationPoint(0.0F, 16.0F, 0.0F);
        pole.addCuboid(-1.0F, -6.0F, -1.0F, 2, 10, 2, 0.0F);
        bottom = new ModelRenderer(this, 8, 5);
        bottom.setRotationPoint(0.0F, 16.0F, 0.0F);
        bottom.addCuboid(-2.0F, 4.0F, -2.0F, 4, 1, 4, 0.0F);
        
        plate = new ModelRenderer(this, 8, 5);
        plate.setRotationPoint(0.0F, 17.0F, 0.0F);
        plate.addCuboid(5.0F, 0.0F, -2.0F, 4, 1, 4, 0.0F);
	}

	public void render(@Nullable TileBrewery brewery, double time, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		float f = 1F / 16F; // todo 1.15

		float offset = (float) Math.sin(time / 40) * 0.1F + 0.05F;
		boolean hasTile = brewery != null;
		int plates = hasTile ? brewery.getSizeInventory() - 1 : 7;
		float deg = (float) time / 16F;
		float polerot = -deg * 25F;

		ms.translate(0F, offset, 0F);
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(polerot));
		if(hasTile && !brewery.getItemHandler().getStackInSlot(0).isEmpty()) {
			ms.push();
			ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
			ms.translate(0, -0.45F, 0);
			renderItemStack(brewery.getItemHandler().getStackInSlot(0), ms, buffers, light, overlay);
			ms.pop();
		}

		IVertexBuilder buffer = buffers.getBuffer(getLayer(RenderTileBrewery.TEXTURE));
		pole.render(ms, buffer, light, overlay);
		top.render(ms, buffer, light, overlay);
		bottom.render(ms, buffer, light, overlay);
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-polerot));

		float degper = (float) (2F * Math.PI) / plates;
		for(int i = 0; i < plates; i++) {
			plate.rotateAngleY = deg;
			float offset1 = (float) Math.sin(time / 20 + i * 40F) * 0.2F - 0.2F;
			if(time == -1)
				offset1 = 0F;

			ms.translate(0F, offset1, 0F);
			if(hasTile && !brewery.getItemHandler().getStackInSlot(i + 1).isEmpty()) {
				float rot = plate.rotateAngleY * 180F / (float) Math.PI;
				float transX = 0.3125F;
				float transY = 1.06F;
				float transZ = 0.1245F;
				ms.push();
				ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(rot));
				ms.translate(transX, transY, transZ);
				ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-90F));
				ms.translate(0.125F, 0.125F, 0);
				renderItemStack(brewery.getItemHandler().getStackInSlot(i + 1), ms, buffers, light, overlay);
				ms.pop();
			}
			plate.render(ms, buffer, light, overlay);
			ms.translate(0F, -offset1, 0F);

			deg += degper;
		}
		ms.translate(0F, -offset, 0F);
	}

	private void renderItemStack(ItemStack stack, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		if(!stack.isEmpty()) {
			Minecraft mc = Minecraft.getInstance();
			ms.push();

			float s = 0.25F;
			ms.scale(s, s, s);
			mc.getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND, light, overlay, ms, buffers);
			ms.pop();
		}
	}

	@Override
	public void render(MatrixStack ms, IVertexBuilder buffer, int light, int overlay, float r, float g, float b, float a) {
		throw new UnsupportedOperationException("unimplemented, call using other render method");
	}
}
