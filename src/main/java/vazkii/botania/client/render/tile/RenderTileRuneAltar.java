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
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.block.tile.TileRuneAltar;

import javax.annotation.Nonnull;

public class RenderTileRuneAltar extends TileEntityRenderer<TileRuneAltar> {
	private final ModelRenderer spinningCube = new ModelRenderer(64, 32, 42, 0);

	public RenderTileRuneAltar(TileEntityRendererDispatcher manager) {
		super(manager);
		spinningCube.addCuboid(0F, 0F, 0F, 1, 1, 1);
		spinningCube.setRotationPoint(0F, 0F, 0F);
		spinningCube.setTextureSize(64, 64);
	}

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

		ms.push();
		ms.translate(0.5F, 1.8F, 0.5F);
		ms.multiply(new Vector3f(1, 0, 1).getDegreesQuaternion(180F));
		renderSpinningCubes(ms, buffers, overlay, 2, 15);
		ms.pop();

		ms.translate(0F, 0.2F, 0F);
		float scale = altar.getTargetMana() == 0 ? 0 : (float) altar.getCurrentMana() / (float) altar.getTargetMana() / 75F;

		if(scale != 0) {
			int seed = altar.getPos().getX() ^ altar.getPos().getY() ^ altar.getPos().getZ();
			ms.translate(0.5F, 0.7F, 0.5F);
			RenderHelper.renderStar(ms, buffers, 0x00E4D7, scale, scale, scale, seed);
		}

		ms.pop();
	}

	private void renderSpinningCubes(MatrixStack ms, IRenderTypeBuffer buffers, int overlay, int cubes, int iters) {
		for (int curIter = iters; curIter > 0; curIter--) {
			final float modifier = 6F;
			final float rotationModifier = 0.2F;
			final float radiusBase = 0.35F;
			final float radiusMod = 0.05F;

			double ticks = ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks - 1.3 * (iters - curIter);
			float offsetPerCube = 360 / cubes;

			ms.push();
			ms.translate(-0.025F, 0.85F, -0.025F);
			for(int i = 0; i < cubes; i++) {
				float offset = offsetPerCube * i;
				float deg = (int) (ticks / rotationModifier % 360F + offset);
				float rad = deg * (float) Math.PI / 180F;
				float radiusX = (float) (radiusBase + radiusMod * Math.sin(ticks / modifier));
				float radiusZ = (float) (radiusBase + radiusMod * Math.cos(ticks / modifier));
				float x =  (float) (radiusX * Math.cos(rad));
				float z = (float) (radiusZ * Math.sin(rad));
				float y = (float) Math.cos((ticks + 50 * i) / 5F) / 10F;

				ms.push();
				ms.translate(x, y, z);
				float xRotate = (float) Math.sin(ticks * rotationModifier) / 2F;
				float yRotate = (float) Math.max(0.6F, Math.sin(ticks * 0.1F) / 2F + 0.5F);
				float zRotate = (float) Math.cos(ticks * rotationModifier) / 2F;

				ms.multiply(new Vector3f(xRotate, yRotate, zRotate).getDegreesQuaternion(deg));
				float alpha = 1;
				if(curIter < iters) {
					alpha = (float) curIter / (float) iters * 0.4F;
				}

				IVertexBuilder buffer = buffers.getBuffer(curIter < iters ? RenderHelper.SPINNING_CUBE_GHOST : RenderHelper.SPINNING_CUBE);
				spinningCube.render(ms, buffer, 0xF000F0, overlay, 1, 1, 1, alpha);

				ms.pop();
			}
			ms.pop();
		}
	}
}
