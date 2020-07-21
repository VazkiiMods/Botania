/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.block.tile.corporea.TileCorporeaCrystalCube;
import vazkii.botania.mixin.AccessorItemEntity;

import javax.annotation.Nullable;

public class RenderTileCorporeaCrystalCube extends BlockEntityRenderer<TileCorporeaCrystalCube> {
	// Ugly but there's no other way to get the model besides grabbing it from the event
	public static BakedModel cubeModel = null;
	private ItemEntity entity = null;
	private ItemEntityRenderer itemRenderer = null;

	public RenderTileCorporeaCrystalCube(BlockEntityRenderDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nullable TileCorporeaCrystalCube cube, float f, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay) {
		ItemStack stack = ItemStack.EMPTY;
		if (cube != null) {
			if (entity == null) {
				entity = new ItemEntity(cube.getWorld(), cube.getPos().getX(), cube.getPos().getY(), cube.getPos().getZ(), new ItemStack(Blocks.STONE));
			}

			if (itemRenderer == null) {
				itemRenderer = new ItemEntityRenderer(MinecraftClient.getInstance().getEntityRenderManager(), MinecraftClient.getInstance().getItemRenderer()) {
					@Override
					public boolean shouldBob() {
						return false;
					}
				};
			}

			((AccessorItemEntity) entity).setAge(ClientTickHandler.ticksInGame);
			stack = cube.getRequestTarget();
			entity.setStack(stack);
		}

		double time = ClientTickHandler.ticksInGame + f;
		double worldTicks = cube == null || cube.getWorld() == null ? 0 : time;

		MinecraftClient mc = MinecraftClient.getInstance();
		ms.push();
		ms.translate(0.5F, 1.5F, 0.5F);
		ms.scale(1F, -1F, -1F);
		ms.translate(0F, (float) Math.sin(worldTicks / 20.0 * 1.55) * 0.025F, 0F);

		if (!stack.isEmpty()) {
			ms.push();
			float s = stack.getItem() instanceof BlockItem ? 0.7F : 0.5F;
			ms.translate(0F, 0.8F, 0F);
			ms.scale(s, s, s);
			ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180F));
			itemRenderer.render(entity, 0, f, ms, buffers, light);
			ms.pop();
		}

		if (cubeModel != null) {
			ms.push();
			ms.translate(-0.5F, 0.25F, -0.5F);
			VertexConsumer buffer = buffers.getBuffer(TexturedRenderLayers.getEntityTranslucentCull());
			MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(ms.peek(), buffer, null, cubeModel, 1, 1, 1, light, overlay);
			ms.pop();
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
			int l = mc.textRenderer.getWidth(countStr);

			ms.translate(0F, 55F, 0F);
			float tr = -16.5F;
			for (int i = 0; i < 4; i++) {
				ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90F));
				ms.translate(0F, 0F, tr);
				mc.textRenderer.draw(countStr, -l / 2, 0, color, false, ms.peek().getModel(), buffers, false, 0, light);
				ms.translate(0F, 0F, 0.1F);
				mc.textRenderer.draw(countStr, -l / 2 + 1, 1, colorShade, false, ms.peek().getModel(), buffers, false, 0, light);
				ms.translate(0F, 0F, -tr - 0.1F);
			}
		}

		ms.pop();
	}
}
