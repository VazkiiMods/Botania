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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import org.lwjgl.opengl.ARBShaderObjects;
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
		for (PlayerEntity player : world.getPlayers()) {
			ItemStack currentStack = player.getHeldItemMainhand();
			if(currentStack.isEmpty() || !(currentStack.getItem() instanceof ItemAstrolabe))
				currentStack = player.getHeldItemOffhand();

			if(!currentStack.isEmpty() && currentStack.getItem() instanceof ItemAstrolabe) {
				Block block = ItemAstrolabe.getBlock(currentStack);
				if(block != Blocks.AIR)
					renderPlayerLook(player, currentStack);
			}
		}
	}

	private static void renderPlayerLook(PlayerEntity player, ItemStack stack) {
		List<BlockPos> coords = ItemAstrolabe.getBlocksToPlace(stack, player);
		if (ItemAstrolabe.hasBlocks(stack, player, coords)) {
			BlockState state = ItemAstrolabe.getBlockState(stack);

			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			ShaderHelper.useShader(ShaderHelper.alpha, shader -> {
				int alpha = ARBShaderObjects.glGetUniformLocationARB(shader, "alpha");
				ARBShaderObjects.glUniform1fARB(alpha, 0.4F);
			});
			
			for(BlockPos coord : coords)
				renderBlockAt(state, coord);
			
			ShaderHelper.releaseShader();
			GL11.glPopMatrix();
		}
	}

	private static void renderBlockAt(BlockState state, BlockPos pos) {
		double renderPosX = Minecraft.getInstance().getRenderManager().renderPosX;
		double renderPosY = Minecraft.getInstance().getRenderManager().renderPosY;
		double renderPosZ = Minecraft.getInstance().getRenderManager().renderPosZ;

		GlStateManager.pushMatrix();
		GlStateManager.translated(-renderPosX, -renderPosY, -renderPosZ);
		GlStateManager.disableDepthTest();
		
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		BlockRendererDispatcher brd = Minecraft.getInstance().getBlockRendererDispatcher();
		GlStateManager.translatef(pos.getX(), pos.getY(), pos.getZ() + 1);
		GlStateManager.color4f(1, 1, 1, 1);
		brd.renderBlockBrightness(state, 1.0F);

		GlStateManager.color4f(1F, 1F, 1F, 1F);
		GlStateManager.enableDepthTest();
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
	}

}
