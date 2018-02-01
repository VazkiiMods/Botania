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
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.common.item.ItemAstrolabe;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = LibMisc.MOD_ID)
public final class AstrolabePreviewHandler {

	@SubscribeEvent
	public static void onWorldRenderLast(RenderWorldLastEvent event) {
		World world = Minecraft.getMinecraft().world;
		List<EntityPlayer> playerEntities = world.playerEntities;
		for (EntityPlayer player : playerEntities) {
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

	private static void renderPlayerLook(EntityPlayer player, ItemStack stack) {
		List<BlockPos> coords = ItemAstrolabe.getBlocksToPlace(stack, player);
		if (ItemAstrolabe.hasBlocks(stack, player, coords)) {
			Block block = ItemAstrolabe.getBlock(stack);
			int meta = ItemAstrolabe.getBlockMeta(stack);

			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			ShaderHelper.useShader(ShaderHelper.alpha, shader -> {
				int alpha = ARBShaderObjects.glGetUniformLocationARB(shader, "alpha");
				ARBShaderObjects.glUniform1fARB(alpha, 0.4F);
			});
			
			for(BlockPos coord : coords)
				renderBlockAt(block, meta, coord);
			
			ShaderHelper.releaseShader();
			GL11.glPopMatrix();
		}
	}

	private static void renderBlockAt(Block block, int meta, BlockPos pos) {
		IBlockState state = block.getStateFromMeta(meta);

		double renderPosX = Minecraft.getMinecraft().getRenderManager().renderPosX;
		double renderPosY = Minecraft.getMinecraft().getRenderManager().renderPosY;
		double renderPosZ = Minecraft.getMinecraft().getRenderManager().renderPosZ;

		GlStateManager.pushMatrix();
		GlStateManager.translate(-renderPosX, -renderPosY, -renderPosZ);
		GlStateManager.disableDepth();
		
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		BlockRendererDispatcher brd = Minecraft.getMinecraft().getBlockRendererDispatcher();
		GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ() + 1);
		GlStateManager.color(1, 1, 1, 1);
		brd.renderBlockBrightness(state, 1.0F);

		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.enableDepth();
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
	}

}
