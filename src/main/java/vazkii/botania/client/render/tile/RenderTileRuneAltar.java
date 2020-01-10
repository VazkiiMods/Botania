/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 2, 2014, 6:34:45 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.model.ModelSpinningCubes;
import vazkii.botania.common.block.tile.TileRuneAltar;

import javax.annotation.Nonnull;

public class RenderTileRuneAltar extends TileEntityRenderer<TileRuneAltar> {

	private final ModelSpinningCubes cubes = new ModelSpinningCubes();

	@Override
	public void render(@Nonnull TileRuneAltar altar, float partticks, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		ms.push();

		int items = 0;
		for(int i = 0; i < altar.getSizeInventory(); i++)
			if(altar.getItemHandler().getStackInSlot(i).isEmpty())
				break;
			else items++;
		float[] angles = new float[altar.getSizeInventory()];

		float anglePer = 360F / items;
		float totalAngle = 0F;
		for(int i = 0; i < angles.length; i++)
			angles[i] = totalAngle += anglePer;

		double time = ClientTickHandler.ticksInGame + partticks;

		for(int i = 0; i < altar.getSizeInventory(); i++) {
			ms.push();
			ms.translate(0.5F, 1.25F, 0.5F);
			ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(angles[i] + (float) time));
			ms.translate(1.125F, 0F, 0.25F);
			ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90F));
			ms.translate(0D, 0.075 * Math.sin((time + i * 10) / 5D), 0F);
			ItemStack stack = altar.getItemHandler().getStackInSlot(i);
			Minecraft mc = Minecraft.getInstance();
			if(!stack.isEmpty()) {
				mc.getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND, light, overlay, ms, buffers);
			}
			ms.pop();
		}

		GlStateManager.pushMatrix();
		GlStateManager.translatef(0.5F, 1.8F, 0.5F);
		GlStateManager.rotatef(180F, 1F, 0F, 1F);
		int repeat = 15;
		cubes.renderSpinningCubes(2, repeat, repeat);
		GlStateManager.popMatrix();

		GlStateManager.translatef(0F, 0.2F, 0F);
		float scale = altar.getTargetMana() == 0 ? 0 : (float) altar.getCurrentMana() / (float) altar.getTargetMana() / 75F;

		if(scale != 0) {
			int seed = altar.getPos().getX() ^ altar.getPos().getY() ^ altar.getPos().getZ();
			GlStateManager.translatef(0.5F, 0.7F, 0.5F);
			RenderHelper.renderStar(0x00E4D7, scale, scale, scale, seed);
		}
		GlStateManager.enableAlphaTest();

		GlStateManager.popMatrix();
	}
}
