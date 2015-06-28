/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 27, 2015, 7:28:29 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.lexicon.multiblock.IMultiblockRenderHook;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent;
import vazkii.botania.common.block.ModMultiblocks;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class MultiblockRenderHandler {

	RenderBlocks blockRender = new RenderBlocks();
	MultiblockSet currentMultiblock;
	ChunkCoordinates anchor;
	int angle;
	int blocksPlaced;

	@SubscribeEvent
	public void onWorldRenderLast(RenderWorldLastEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if(mc.thePlayer != null && mc.objectMouseOver != null) {
			ItemStack currentStack = mc.thePlayer.getCurrentEquippedItem();
			renderPlayerLook(mc.thePlayer, mc.objectMouseOver);
		}
	}

	private void renderPlayerLook(EntityPlayer player, MovingObjectPosition src) {
		if(currentMultiblock != null) {
			Multiblock mb = currentMultiblock.getForEntity(player);
			for(MultiblockComponent comp : mb.getComponents())
				renderComponent(player.worldObj, mb, comp, src);
		}
	}

	private void renderComponent(World world, Multiblock mb, MultiblockComponent comp, MovingObjectPosition src) {
		ChunkCoordinates pos = comp.getRelativePosition();
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 0.6F);
		GL11.glTranslated(pos.posX + src.blockX + 0.5 - RenderManager.renderPosX, pos.posY + src.blockY + 0.55 - RenderManager.renderPosY, pos.posZ + src.blockZ + 0.5 - RenderManager.renderPosZ);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		
		blockRender.useInventoryTint = false;
		Block block = comp.getBlock();
		if(IMultiblockRenderHook.renderHooks.containsKey(block))
			IMultiblockRenderHook.renderHooks.get(block).renderBlockForMultiblock(world, mb, block, comp.getMeta(), blockRender);
		else blockRender.renderBlockAsItem(comp.getBlock(), comp.getMeta(), 1F);
		GL11.glPopMatrix();
	}

}
