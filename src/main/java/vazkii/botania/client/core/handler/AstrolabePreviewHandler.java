/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [27/10/2016, 17:55:20 (GMT)]
 */
package vazkii.botania.client.core.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.common.item.ItemAstrolabe;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = LibMisc.MOD_ID)
public final class AstrolabePreviewHandler {
	@SubscribeEvent
	public static void onWorldRenderLast(RenderWorldLastEvent event) {
		World world = Minecraft.getInstance().world;
		MatrixStack ms = event.getMatrixStack();
		IRenderTypeBuffer.Impl buffers = Minecraft.getInstance().getBufferBuilders().getEntityVertexConsumers();
		for (PlayerEntity player : world.getPlayers()) {
			ItemStack currentStack = player.getHeldItemMainhand();
			if(currentStack.isEmpty() || !(currentStack.getItem() instanceof ItemAstrolabe))
				currentStack = player.getHeldItemOffhand();

			if(!currentStack.isEmpty() && currentStack.getItem() instanceof ItemAstrolabe) {
				Block block = ItemAstrolabe.getBlock(currentStack);
				if(block != Blocks.AIR)
					renderPlayerLook(ms, buffers, player, currentStack);
			}
		}

		ShaderHelper.useShader(ShaderHelper.alpha, shader -> {
			int alpha = GlStateManager.getUniformLocation(shader, "alpha");
			ShaderHelper.FLOAT_BUF.position(0);
			ShaderHelper.FLOAT_BUF.put(0, 0.4F);
			GlStateManager.uniform1(alpha, ShaderHelper.FLOAT_BUF);
		});
		// todo 1.15 need to disable depth test
		buffers.draw();
		ShaderHelper.releaseShader();
	}

	private static void renderPlayerLook(MatrixStack ms, IRenderTypeBuffer buffers, PlayerEntity player, ItemStack stack) {
		List<BlockPos> coords = ItemAstrolabe.getBlocksToPlace(stack, player);
		if (ItemAstrolabe.hasBlocks(stack, player, coords)) {
			BlockState state = ItemAstrolabe.getBlockState(stack);

			for(BlockPos coord : coords)
				renderBlockAt(ms, buffers, state, coord);
		}
	}

	private static void renderBlockAt(MatrixStack ms, IRenderTypeBuffer buffers, BlockState state, BlockPos pos) {
		double renderPosX = Minecraft.getInstance().getRenderManager().info.getProjectedView().getX();
		double renderPosY = Minecraft.getInstance().getRenderManager().info.getProjectedView().getY();
		double renderPosZ = Minecraft.getInstance().getRenderManager().info.getProjectedView().getZ();

		ms.push();
		ms.translate(-renderPosX, -renderPosY, -renderPosZ);

		BlockRendererDispatcher brd = Minecraft.getInstance().getBlockRendererDispatcher();
		ms.translate(pos.getX(), pos.getY(), pos.getZ() + 1);
		IBakedModel model = brd.getModelForState(state);
		int color = Minecraft.getInstance().getBlockColors().getColor(state, null, null, 0);
		float r = (float)(color >> 16 & 255) / 255.0F;
		float g = (float)(color >> 8 & 255) / 255.0F;
		float b = (float)(color & 255) / 255.0F;
		// always use entity translucent layer so blending is turned on
		brd.getBlockModelRenderer().render(ms.peek(), buffers.getBuffer(Atlases.getEntityTranslucent()), state, model, r, g, b, 0xF000F0, OverlayTexture.DEFAULT_UV);

		ms.pop();
	}

}
