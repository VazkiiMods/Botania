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
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.opengl.GL11;

import vazkii.botania.api.lexicon.multiblock.IMultiblockRenderHook;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.common.lib.LibObfuscation;

public final class MultiblockRenderHandler {

	public static boolean rendering = false;

	private static MultiblockBlockAccess blockAccess = new MultiblockBlockAccess();

	public static MultiblockSet currentMultiblock;
	public static BlockPos anchor;
	public static int angle;
	public static int dimension;

	public static void setMultiblock(MultiblockSet set) {
		currentMultiblock = set;
		anchor = null;
		angle = 0;

		Minecraft mc = Minecraft.getMinecraft();
		if(mc.theWorld != null)
			dimension = mc.theWorld.provider.getDimensionId();
	}

	@SubscribeEvent
	public void onWorldRenderLast(RenderWorldLastEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if(mc.thePlayer != null && mc.objectMouseOver != null && (!mc.thePlayer.isSneaking() || anchor != null)) {
			mc.thePlayer.getCurrentEquippedItem();
			renderPlayerLook(mc.thePlayer, mc.objectMouseOver);
		}
	}

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(currentMultiblock != null && anchor == null && event.action == Action.RIGHT_CLICK_BLOCK && event.entityPlayer == Minecraft.getMinecraft().thePlayer) {
			anchor = event.pos;
			angle = MathHelper.floor_double(event.entityPlayer.rotationYaw * 4.0 / 360.0 + 0.5) & 3;
			event.setCanceled(true);
		}
	}

	private void renderPlayerLook(EntityPlayer player, MovingObjectPosition src) {
		if(currentMultiblock != null && dimension == player.worldObj.provider.getDimensionId()) {
			BlockPos anchorPos = anchor != null ? anchor : src.getBlockPos();

			GL11.glPushMatrix();
			GL11.glPushAttrib(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glDisable(GL11.GL_LIGHTING);
			rendering = true;
			Multiblock mb = anchor != null ? currentMultiblock.getForFacing(EnumFacing.getHorizontal(angle)) : currentMultiblock.getForEntity(player);
			boolean didAny = false;

			blockAccess.update(player.worldObj, mb, anchorPos);

			for(MultiblockComponent comp : mb.getComponents())
				if(renderComponentInWorld(player.worldObj, mb, comp, anchorPos))
					didAny = true;
			rendering = false;
			GL11.glPopAttrib();
			GL11.glPopMatrix();
			
			if(!didAny) {
				setMultiblock(null);
				player.addChatComponentMessage(new ChatComponentTranslation("botaniamisc.structureComplete").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
			}
		}
	}

	private double getRenderPosX() { // todo 1.8
		return ObfuscationReflectionHelper.getPrivateValue(RenderManager.class, Minecraft.getMinecraft().getRenderManager(), LibObfuscation.RENDERPOSX);
	}

	private double getRenderPosY() {
		return ObfuscationReflectionHelper.getPrivateValue(RenderManager.class, Minecraft.getMinecraft().getRenderManager(), LibObfuscation.RENDERPOSY);
	}

	private double getRenderPosZ() {
		return ObfuscationReflectionHelper.getPrivateValue(RenderManager.class, Minecraft.getMinecraft().getRenderManager(), LibObfuscation.RENDERPOSZ);
	}

	private boolean renderComponentInWorld(World world, Multiblock mb, MultiblockComponent comp, BlockPos anchorPos) {
		BlockPos pos = comp.getRelativePosition();
		BlockPos pos_ = pos.add(anchorPos);
		if(anchor != null && comp.matches(world, pos_))
			return false;

		GlStateManager.pushMatrix();
		GlStateManager.translate(-getRenderPosX(), -getRenderPosY(), -getRenderPosZ());
		GlStateManager.disableDepth();
		doRenderComponent(mb, comp, pos_, 0.4F);
		GlStateManager.popMatrix();
		return true;
	}

	public static void renderMultiblockOnPage(Multiblock mb) {
		GlStateManager.translate(-0.5, -0.5, -0.5);
		blockAccess.update(null, mb, mb.offPos);
		for(MultiblockComponent comp : mb.getComponents()) {
			BlockPos pos = comp.getRelativePosition();
			doRenderComponent(mb, comp, pos.add(mb.offPos), 1);
		}
	}

	private static void doRenderComponent(Multiblock mb, MultiblockComponent comp, BlockPos pos, float alpha) {
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		IBlockState state = comp.getBlockState();
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		if(state == null)
			return;
		if(IMultiblockRenderHook.renderHooks.containsKey(state.getBlock())) {
			GlStateManager.color(1F, 1F, 1F, alpha);
			IMultiblockRenderHook renderHook = IMultiblockRenderHook.renderHooks.get(state.getBlock());
			if(renderHook.needsTranslate(state)) {
				GlStateManager.translate(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
			}
			renderHook.renderBlockForMultiblock(blockAccess, mb, state, comp, alpha);
		}
		else {
			BlockRendererDispatcher brd = Minecraft.getMinecraft().getBlockRendererDispatcher();
			brd.renderBlockBrightness(state, 1.0F); // todo 1.8 check brightness
//			if(comp.shouldDoFancyRender()) {
//				int color = block.colorMultiplier(blockAccess, x, y, z);
//				float red = (color >> 16 & 255) / 255.0F;
//				float green = (color >> 8 & 255) / 255.0F;
//				float blue = (color & 255) / 255.0F;
//				GlStateManager.color(red, green, blue, alpha);
//				IBlockAccess oldBlockAccess = blockRender.blockAccess;
//				blockRender.blockAccess = blockAccess;
//				Tessellator tessellator = Tessellator.getInstance();
//				blockRender.renderAllFaces = true;
//				tessellator.getWorldRenderer().startDrawingQuads();
//				tessellator.disableColor();
//				try {
//					blockRender.renderBlockByRenderType(block, x, y, z);
//				}
//				catch(Exception e) {
//					comp.doFancyRender = false;
//				}
//				tessellator.draw();
//				blockRender.renderAllFaces = false;
//				blockRender.blockAccess = oldBlockAccess;
//			}
//			else {
//				int color = block.getRenderColor(meta);
//				float red = (color >> 16 & 255) / 255.0F;
//				float green = (color >> 8 & 255) / 255.0F;
//				float blue = (color & 255) / 255.0F;
//				GlStateManager.color(red, green, blue, alpha);
//				GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
//				blockRender.renderBlockAsItem(block, meta, 1F);
//			}
		}
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.enableDepth();
		GlStateManager.popMatrix();
	}
}
