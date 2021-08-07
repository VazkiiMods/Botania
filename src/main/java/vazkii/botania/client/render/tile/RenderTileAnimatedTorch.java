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
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.block.tile.TileAnimatedTorch;

import java.util.Random;

public class RenderTileAnimatedTorch implements BlockEntityRenderer<TileAnimatedTorch> {

	public RenderTileAnimatedTorch(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public void render(TileAnimatedTorch te, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		Minecraft mc = Minecraft.getInstance();
		ms.pushPose();

		boolean hasWorld = te != null && te.getLevel() != null;
		int wtime = !hasWorld ? 0 : ClientTickHandler.ticksInGame;
		if (wtime != 0) {
			wtime += new Random(te.getBlockPos().hashCode()).nextInt(360);
		}

		float time = wtime == 0 ? 0 : wtime + partialTicks;
		float xt = 0.5F + (float) Math.cos(time * 0.05F) * 0.025F;
		float yt = 0.1F + (float) (Math.sin(time * 0.04F) + 1F) * 0.05F;
		float zt = 0.5F + (float) Math.sin(time * 0.05F) * 0.025F;
		ms.translate(xt, yt, zt);

		ms.scale(2, 2, 2);
		ms.mulPose(Vector3f.XP.rotationDegrees(90));
		float rotation = (float) te.rotation;
		if (te.rotating) {
			rotation += te.anglePerTick * partialTicks;
		}

		ms.mulPose(Vector3f.ZP.rotationDegrees(rotation));
		mc.getItemRenderer().renderStatic(new ItemStack(Blocks.REDSTONE_TORCH), ItemTransforms.TransformType.GROUND, light, overlay, ms, buffers);
		ms.popPose();
	}

}
