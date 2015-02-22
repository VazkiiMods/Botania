/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 27, 2014, 8:55:00 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.RenderPlayerEvent;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.api.item.IBaubleRender.Helper;
import vazkii.botania.api.item.IBaubleRender.RenderType;
import vazkii.botania.common.block.BlockModFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelHelm;
import vazkii.botania.common.item.material.ItemManaResource;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class BaubleRenderHandler {

	@SubscribeEvent
	public void onPlayerRender(RenderPlayerEvent.Specials.Post event) {
		if(!ConfigHandler.renderBaubles)
			return;

		EntityPlayer player = event.entityPlayer;
		InventoryBaubles inv = PlayerHandler.getPlayerBaubles(player);

		dispatchRenders(inv, event, RenderType.BODY);
		if(inv.getStackInSlot(3) != null)
			renderManaTablet(event);

		float yaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * event.partialRenderTick;
		float yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * event.partialRenderTick;
		float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * event.partialRenderTick;

		GL11.glPushMatrix();
		GL11.glRotatef(yawOffset, 0, -1, 0);
		GL11.glRotatef(yaw - 270, 0, 1, 0);
		GL11.glRotatef(pitch, 0, 0, 1);
		dispatchRenders(inv, event, RenderType.HEAD);

		ItemStack helm = player.inventory.armorItemInSlot(3);
		if(helm != null && helm.getItem() instanceof ItemTerrasteelHelm)
			ItemTerrasteelHelm.renderOnPlayer(helm, event);
		//if(event.entityPlayer.getCommandSenderName().equals("Vazkii"))
			//renderFancyExclusiveDevStuffBecauseImAnEvilDevWhoDoesntCareAboutTheCommunityBooo(event);
		GL11.glPopMatrix();
	}

	private void dispatchRenders(InventoryBaubles inv, RenderPlayerEvent event, RenderType type) {
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack != null && stack.getItem() instanceof IBaubleRender) {
				GL11.glPushMatrix();
				GL11.glColor4f(1F, 1F, 1F, 1F);
				((IBaubleRender) stack.getItem()).onPlayerBaubleRender(stack, event, type);
				GL11.glPopMatrix();
			}
		}
	}

	private void renderManaTablet(RenderPlayerEvent event) {
		EntityPlayer player = event.entityPlayer;
		boolean renderedOne = false;
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if(stack != null && stack.getItem() == ModItems.manaTablet) {
				Item item = stack.getItem();
				GL11.glPushMatrix();
				Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
				Helper.rotateIfSneaking(event.entityPlayer);
				boolean armor = event.entityPlayer.getCurrentArmor(1) != null;
				GL11.glRotatef(180F, 1F, 0F, 0F);
				GL11.glRotatef(90F, 0F, 1F, 0F);
				GL11.glTranslatef(-0.25F, -0.85F, renderedOne ? armor ? 0.2F : 0.28F : armor ? -0.3F : -0.25F);
				GL11.glScalef(0.5F, 0.5F, 0.5F);

				GL11.glColor3f(1F, 1F, 1F);
				int light = 15728880;
				int lightmapX = light % 65536;
				int lightmapY = light / 65536;
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);
				for(int j = 0; j < 2; j++) {
					IIcon icon = item.getIcon(stack, j);
					float f = icon.getMinU();
					float f1 = icon.getMaxU();
					float f2 = icon.getMinV();
					float f3 = icon.getMaxV();
					ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);

					Color color = new Color(item.getColorFromItemStack(stack, 1));
					GL11.glColor3ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
				}
				GL11.glPopMatrix();

				if(renderedOne)
					return;
				renderedOne = true;
			}
		}
	}
	
	private void renderFancyExclusiveDevStuffBecauseImAnEvilDevWhoDoesntCareAboutTheCommunityBooo(RenderPlayerEvent event) {
		GL11.glPushMatrix();
		IIcon icon = ((ItemManaResource) ModItems.manaResource).tailIcon;
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
		Helper.translateToHeadLevel(event.entityPlayer);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
		GL11.glPushMatrix();
		GL11.glRotatef(90F, 0F, 1F, 0F);
		float t = 0.13F;
		GL11.glTranslatef(t, -0.5F, -0.1F);
		if(event.entityPlayer.motionY < 0)
			GL11.glRotatef((float) event.entityPlayer.motionY * 20F, 1F, 0F, 0F);

		float r = -18F + (float) Math.sin(((float) ClientTickHandler.ticksInGame + event.partialRenderTick) * 0.05F) * 2F;
		GL11.glRotatef(r, 0F, 0F, 1F);
		float s = 0.9F;
		GL11.glScalef(s, s, s);
		ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);
		GL11.glRotatef(-r, 0F, 0F, 1F);
		GL11.glTranslatef(-t, -0F, 0F);
		GL11.glScalef(-1F, 1F, 1F);
		GL11.glTranslatef(t, -0F, 0F);
		GL11.glRotatef(r, 0F, 0F, 1F);
		ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);
		GL11.glPopMatrix();
		
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		icon = ((BlockModFlower) ModBlocks.flower).icons[6];
		f = icon.getMinU();
		f1 = icon.getMaxU();
		f2 = icon.getMinV();
		f3 = icon.getMaxV();
		GL11.glRotatef(180F, 0F, 0F, 1F);
		GL11.glRotatef(90F, 0F, 1F, 0F);
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		GL11.glTranslatef(-0.5F, 0.7F, 0F);

		ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);

		GL11.glPopMatrix();
	}

}
