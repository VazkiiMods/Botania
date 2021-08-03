/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.block.tile.corporea.TileCorporeaCrystalCube;
import vazkii.botania.mixin.AccessorItemEntity;

import javax.annotation.Nullable;

public class RenderTileCorporeaCrystalCube extends BlockEntityRenderer<TileCorporeaCrystalCube> {
	// Ugly but there's no other way to get the model besides grabbing it from the event
	public static BakedModel cubeModel = null;
	private ItemEntity entity = null;

	public RenderTileCorporeaCrystalCube(BlockEntityRenderDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nullable TileCorporeaCrystalCube cube, float f, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		ItemStack stack = ItemStack.EMPTY;
		if (cube != null) {
			if (entity == null) {
				entity = new ItemEntity(cube.getLevel(), cube.getBlockPos().getX(), cube.getBlockPos().getY(), cube.getBlockPos().getZ(), new ItemStack(Blocks.STONE));
			}

			((AccessorItemEntity) entity).setAge(ClientTickHandler.ticksInGame);
			stack = cube.getRequestTarget();
			entity.setItem(stack);
		}

		double time = ClientTickHandler.ticksInGame + f;
		double worldTicks = cube == null || cube.getLevel() == null ? 0 : time;

		Minecraft mc = Minecraft.getInstance();
		ms.pushPose();
		ms.translate(0.5F, 1.5F, 0.5F);
		ms.scale(1F, -1F, -1F);
		ms.translate(0F, (float) Math.sin(worldTicks / 20.0 * 1.55) * 0.025F, 0F);

		if (!stack.isEmpty()) {
			ms.pushPose();
			float s = stack.getItem() instanceof BlockItem ? 0.7F : 0.5F;
			ms.translate(0F, 0.8F, 0F);
			ms.scale(s, s, s);
			ms.mulPose(Vector3f.ZP.rotationDegrees(180F));
			Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity).render(entity, 0, f, ms, buffers, light);
			ms.popPose();
		}

		if (cubeModel != null) {
			ms.pushPose();
			ms.translate(-0.5F, 0.25F, -0.5F);
			VertexConsumer buffer = buffers.getBuffer(Sheets.translucentCullBlockSheet());
			Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(ms.last(), buffer, null, cubeModel, 1, 1, 1, light, overlay);
			ms.popPose();
		}

		if (!stack.isEmpty()) {
			int count = cube.getItemCount();
			String countStr = "" + count;
			int color = 0xFFFFFF;
			if (count > 9999) {
				countStr = count / 1000 + "K";
				color = 0xFFFF00;
				if (count > 9999999) {
					countStr = count / 10000000 + "M";
					color = 0x00FF00;
				}
			}
			color |= 0xA0 << 24;
			int colorShade = (color & 16579836) >> 2 | color & -16777216;

			float s = 1F / 64F;
			ms.scale(s, s, s);
			int l = mc.font.width(countStr);

			ms.translate(0F, 55F, 0F);
			float tr = -16.5F;
			for (int i = 0; i < 4; i++) {
				ms.mulPose(Vector3f.YP.rotationDegrees(90F));
				ms.translate(0F, 0F, tr);
				mc.font.drawInBatch(countStr, -l / 2, 0, color, false, ms.last().pose(), buffers, false, 0, light);
				ms.translate(0F, 0F, 0.1F);
				mc.font.drawInBatch(countStr, -l / 2 + 1, 1, colorShade, false, ms.last().pose(), buffers, false, 0, light);
				ms.translate(0F, 0F, -tr - 0.1F);
			}
		}

		ms.popPose();
	}
}
