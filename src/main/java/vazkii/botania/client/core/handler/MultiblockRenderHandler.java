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
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.lexicon.multiblock.IMultiblockRenderHook;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent;
import vazkii.botania.common.block.ModMultiblocks;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class MultiblockRenderHandler {

	public static boolean rendering = false;
	
	private static RenderBlocks blockRender = RenderBlocks.getInstance();
	public static MultiblockSet currentMultiblock;
	public static ChunkCoordinates anchor;
	public static int angle;

	public static void setMultiblock(MultiblockSet set) {
		currentMultiblock = set;
		anchor = null;
		angle = 0;
	}
	
	@SubscribeEvent
	public void onWorldRenderLast(RenderWorldLastEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if(mc.thePlayer != null && mc.objectMouseOver != null && (!mc.thePlayer.isSneaking() || anchor != null)) {
			ItemStack currentStack = mc.thePlayer.getCurrentEquippedItem();
			renderPlayerLook(mc.thePlayer, mc.objectMouseOver);
		}
	}
	
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(currentMultiblock != null && anchor == null && event.action == Action.RIGHT_CLICK_BLOCK) {
			anchor = new ChunkCoordinates(event.x, event.y, event.z);
			angle = MathHelper.floor_double((event.entityPlayer.rotationYaw * 4.0 / 360.0) + 0.5) & 3;
			event.setCanceled(true);
		}
	}

	private void renderPlayerLook(EntityPlayer player, MovingObjectPosition src) {
		if(currentMultiblock != null) {
			int anchorX = anchor != null ? anchor.posX : src.blockX;
			int anchorY = anchor != null ? anchor.posY : src.blockY;
			int anchorZ = anchor != null ? anchor.posZ : src.blockZ;
			
			rendering = true;
			Multiblock mb = anchor != null ? currentMultiblock.getForIndex(angle) : currentMultiblock.getForEntity(player);
			boolean didAny = false;
			for(MultiblockComponent comp : mb.getComponents())
				if(renderComponent(player.worldObj, mb, comp, anchorX, anchorY, anchorZ))
					didAny = true;
			rendering = false;
			
			if(!didAny) {
				setMultiblock(null);
				player.addChatComponentMessage(new ChatComponentTranslation("botaniamisc.structureComplete").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
			}
		}
	}

	private boolean renderComponent(World world, Multiblock mb, MultiblockComponent comp, int anchorX, int anchorY, int anchorZ) {
		ChunkCoordinates pos = comp.getRelativePosition();
		int x = pos.posX + anchorX;
		int y = pos.posY + anchorY;
		int z = pos.posZ + anchorZ;
		if(anchor != null && comp.matches(world, x, y, z))
			return false;
		
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glColor4f(1F, 1F, 1F, 0.4F);
		GL11.glTranslated(x + 0.5 - RenderManager.renderPosX, y + 0.5 - RenderManager.renderPosY, z + 0.5 - RenderManager.renderPosZ);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		
		blockRender.useInventoryTint = false;
		Block block = comp.getBlock();
		if(IMultiblockRenderHook.renderHooks.containsKey(block))
			IMultiblockRenderHook.renderHooks.get(block).renderBlockForMultiblock(world, mb, block, comp.getMeta(), blockRender);
		else blockRender.renderBlockAsItem(comp.getBlock(), comp.getMeta(), 1F);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();
		return true;
	}

}
