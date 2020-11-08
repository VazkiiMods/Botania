/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.block.tile.mana.TilePrism;

import javax.annotation.Nonnull;

public class RenderTilePrism extends BlockEntityRenderer<TilePrism> {

	public RenderTilePrism(BlockEntityRenderDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nonnull TilePrism prism, float partTicks, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay) {
		float pos = (float) Math.sin((ClientTickHandler.ticksInGame + partTicks) * 0.05F) * 0.5F * (1F - 1F / 16F) - 0.5F;

		ItemStack stack = prism.getItemHandler().getStack(0);

		if (!stack.isEmpty()) {
			MinecraftClient.getInstance().getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
			if (stack.getItem() instanceof ILens) {
				ms.push();
				ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90));
				ms.translate(0.5F, 0.5F, pos);
				MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.NONE, light, overlay, ms, buffers);
				ms.pop();
			}
		}
	}

}
