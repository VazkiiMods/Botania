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
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Matrix4f;
import vazkii.botania.api.item.IPetalApothecary;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.block.tile.TileAltar;

import javax.annotation.Nonnull;

public class RenderTileAltar extends BlockEntityRenderer<TileAltar> {

	public RenderTileAltar(BlockEntityRenderDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nonnull TileAltar altar, float pticks, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay) {
		ms.push();
		ms.translate(0.5, 1.5, 0.5);

		boolean water = altar.getFluid() == IPetalApothecary.State.WATER;
		boolean lava = altar.getFluid() == IPetalApothecary.State.LAVA;
		if (water || lava) {
			ms.push();
			float s = 1F / 256F * 10F;
			float v = 1F / 8F;
			float w = -v * 2.5F;

			if (water) {
				int petals = 0;
				for (int i = 0; i < altar.inventorySize(); i++) {
					if (!altar.getItemHandler().getStack(i).isEmpty()) {
						petals++;
					} else {
						break;
					}
				}

				if (petals > 0) {
					final float modifier = 6F;
					final float rotationModifier = 0.25F;
					final float radiusBase = 1.2F;
					final float radiusMod = 0.1F;

					double ticks = (ClientTickHandler.ticksInGame + pticks) * 0.5;
					float offsetPerPetal = 360 / petals;

					ms.push();
					ms.translate(-0.05F, -0.5F, 0F);
					ms.scale(v, v, v);
					for (int i = 0; i < petals; i++) {
						float offset = offsetPerPetal * i;
						float deg = (int) (ticks / rotationModifier % 360F + offset);
						float rad = deg * (float) Math.PI / 180F;
						float radiusX = (float) (radiusBase + radiusMod * Math.sin(ticks / modifier));
						float radiusZ = (float) (radiusBase + radiusMod * Math.cos(ticks / modifier));
						float x = (float) (radiusX * Math.cos(rad));
						float z = (float) (radiusZ * Math.sin(rad));
						float y = (float) Math.cos((ticks + 50 * i) / 5F) / 10F;

						ms.push();
						ms.translate(x, y, z);
						float xRotate = (float) Math.sin(ticks * rotationModifier) / 2F;
						float yRotate = (float) Math.max(0.6F, Math.sin(ticks * 0.1F) / 2F + 0.5F);
						float zRotate = (float) Math.cos(ticks * rotationModifier) / 2F;

						v /= 2F;
						ms.translate(v, v, v);
						ms.multiply(new Vector3f(xRotate, yRotate, zRotate).getDegreesQuaternion(deg));
						ms.translate(-v, -v, -v);
						v *= 2F;

						ItemStack stack = altar.getItemHandler().getStack(i);
						MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, light, overlay, ms, buffers);
						ms.pop();
					}

					ms.pop();
				}
			}

			float alpha = lava ? 1F : 0.7F;

			ms.translate(w, -0.3F, w);
			ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90));
			ms.scale(s, s, s);

			Sprite sprite = lava ? MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(Blocks.LAVA.getDefaultState()).getSprite()
					: MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(Blocks.WATER.getDefaultState()).getSprite();
			int color = lava ? Fluids.LAVA.getAttributes().getColor(altar.getWorld(), altar.getPos())
					: Fluids.WATER.getAttributes().getColor(altar.getWorld(), altar.getPos());
			VertexConsumer buffer = buffers.getBuffer(TexturedRenderLayers.getEntityTranslucentCull());
			renderIcon(ms, buffer, sprite, color, alpha, overlay, lava ? 0xF000F0 : light);
			ms.pop();
		}
		ms.pop();
	}

	private void renderIcon(MatrixStack ms, VertexConsumer builder, Sprite sprite, int color, float alpha, int overlay, int light) {
		int red = ((color >> 16) & 0xFF);
		int green = ((color >> 8) & 0xFF);
		int blue = (color & 0xFF);
		Matrix4f mat = ms.peek().getModel();
		builder.vertex(mat, 0, 16, 0).color(red, green, blue, (int) (alpha * 255F)).texture(sprite.getMinU(), sprite.getMaxV()).overlay(overlay).light(light).normal(0, 0, 1).next();
		builder.vertex(mat, 16, 16, 0).color(red, green, blue, (int) (alpha * 255F)).texture(sprite.getMaxU(), sprite.getMaxV()).overlay(overlay).light(light).normal(0, 0, 1).next();
		builder.vertex(mat, 16, 0, 0).color(red, green, blue, (int) (alpha * 255F)).texture(sprite.getMaxU(), sprite.getMinV()).overlay(overlay).light(light).normal(0, 0, 1).next();
		builder.vertex(mat, 0, 0, 0).color(red, green, blue, (int) (alpha * 255F)).texture(sprite.getMinU(), sprite.getMinV()).overlay(overlay).light(light).normal(0, 0, 1).next();
	}

}
