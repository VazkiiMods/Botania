/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.block.block_entity.AnimatedTorchBlockEntity;
import vazkii.botania.common.helper.VecHelper;

import java.util.Random;

public class AnimatedTorchBlockEntityRenderer implements BlockEntityRenderer<AnimatedTorchBlockEntity> {

	public AnimatedTorchBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public void render(AnimatedTorchBlockEntity torch, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		ms.pushPose();

		Level level = torch.getLevel();
		int wtime = level == null ? 0 : ClientTickHandler.getEntityTicksInGame();
		if (wtime != 0) {
			wtime += new Random(torch.getBlockPos().asLong()).nextInt(360);
		}

		float time = wtime == 0 ? 0 : wtime + partialTicks;
		float xt = 0.5F + (float) Math.cos(time * 0.05F) * 0.025F;
		float yt = 0.1F + (float) (Math.sin(time * 0.04F) + 1F) * 0.05F;
		float zt = 0.5F + (float) Math.sin(time * 0.05F) * 0.025F;
		ms.translate(xt, yt, zt);

		ms.scale(2, 2, 2);
		ms.mulPose(VecHelper.rotateX(90));
		float rotation = (float) torch.rotation;
		if (torch.rotating) {
			rotation += (float) torch.anglePerTick * partialTicks;
		}

		ms.mulPose(VecHelper.rotateZ(rotation));
		Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(Blocks.REDSTONE_TORCH),
				ItemDisplayContext.GROUND, light, overlay, ms, buffers, level, 0);
		ms.popPose();
	}

}
