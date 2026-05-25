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
import vazkii.botania.common.block.AnimatedTorchBlock;
import vazkii.botania.common.block.block_entity.AnimatedTorchBlockEntity;
import vazkii.botania.common.helper.VecHelper;

import java.util.Random;

public class AnimatedTorchBlockEntityRenderer implements BlockEntityRenderer<AnimatedTorchBlockEntity> {
	private static final double ANIM_HORIZONTAL_TICK_SCALE = 0.05;
	private static final double ANIM_VERTICAL_TICK_SCALE = 0.04;
	private static final double ANIM_TICK_CYCLE = 2 * Math.PI / ANIM_HORIZONTAL_TICK_SCALE / ANIM_VERTICAL_TICK_SCALE;

	public AnimatedTorchBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public void render(AnimatedTorchBlockEntity torch, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		ms.pushPose();

		Level level = torch.getLevel();
		double time = level == null
				? 0
				: ClientTickHandler.getEntityTicksInGame() % ANIM_TICK_CYCLE
						+ partialTicks
						+ new Random(torch.getBlockState().getSeed(torch.getBlockPos())).nextDouble(ANIM_TICK_CYCLE);

		float xt = 0.5F + (float) Math.cos(time * ANIM_HORIZONTAL_TICK_SCALE) * 0.025F;
		float yt = 0.1F + (float) (Math.sin(time * ANIM_VERTICAL_TICK_SCALE) + 1) * 0.05F;
		float zt = 0.5F + (float) Math.sin(time * ANIM_HORIZONTAL_TICK_SCALE) * 0.025F;
		ms.translate(xt, yt, zt);

		ms.scale(2, 2, 2);
		ms.mulPose(VecHelper.rotateX(90));

		if (level != null) {
			float targetRotation = torch.getBlockState().getValue(AnimatedTorchBlock.FACING).toYRot();
			if (torch.lastRotation == -1) {
				// clientside rotation was not initialized yet
				torch.lastRotation = targetRotation;
				torch.rotation = targetRotation;
			} else if (torch.rotation != targetRotation) {
				float rotationDiff = targetRotation - torch.lastRotation;
				if (rotationDiff > 180) {
					rotationDiff -= 360;
				} else if (rotationDiff < -180) {
					rotationDiff += 360;
				}
				float rotationProgress = Math.clamp(
						((level.getGameTime() - torch.rotationStartTime) + partialTicks)
								/ AnimatedTorchBlockEntity.ROTATION_TICKS,
						0, 1);
				if (rotationProgress >= 1) {
					torch.lastRotation = targetRotation;
					torch.rotation = targetRotation;
				} else {
					torch.rotation = torch.lastRotation + rotationProgress * rotationDiff;
				}
			}
			ms.mulPose(VecHelper.rotateZ(torch.rotation));
		}
		Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(Blocks.REDSTONE_TORCH),
				ItemDisplayContext.GROUND, light, overlay, ms, buffers, level, 0);
		ms.popPose();
	}

}
