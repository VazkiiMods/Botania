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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.client.event.RenderPlayerEvent;

import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.api.item.IBaubleRender.Helper;
import vazkii.botania.api.item.IBaubleRender.RenderType;
import vazkii.botania.api.item.ICosmeticAttachable;
import vazkii.botania.api.item.IPhantomInkable;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelHelm;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class BaubleRenderHandler {

	@SubscribeEvent
	public void onPlayerRender(RenderPlayerEvent.Specials.Post event) {
		if(!ConfigHandler.renderBaubles || event.entityLiving.getActivePotionEffect(Potion.invisibility) != null)
			return;

		EntityPlayer player = event.entityPlayer;
		InventoryBaubles inv = PlayerHandler.getPlayerBaubles(player);

		dispatchRenders(inv, event, RenderType.BODY);
		if(inv.getStackInSlot(3) != null)
			renderManaTablet(event);

		float yaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * event.partialRenderTick;
		float yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * event.partialRenderTick;
		float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * event.partialRenderTick;

		GlStateManager.pushMatrix();
		GlStateManager.rotate(yawOffset, 0, -1, 0);
		GlStateManager.rotate(yaw - 270, 0, 1, 0);
		GlStateManager.rotate(pitch, 0, 0, 1);
		dispatchRenders(inv, event, RenderType.HEAD);

		ItemStack helm = player.inventory.armorItemInSlot(3);
		if(helm != null && helm.getItem() instanceof ItemTerrasteelHelm)
			ItemTerrasteelHelm.renderOnPlayer(helm, event);

		GlStateManager.popMatrix();
	}

	private void dispatchRenders(InventoryBaubles inv, RenderPlayerEvent event, RenderType type) {
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack != null) {
				Item item = stack.getItem();

				if(item instanceof IPhantomInkable) {
					IPhantomInkable inkable = (IPhantomInkable) item;
					if(inkable.hasPhantomInk(stack))
						continue;
				}

				if(item instanceof ICosmeticAttachable) {
					ICosmeticAttachable attachable = (ICosmeticAttachable) item;
					ItemStack cosmetic = attachable.getCosmeticItem(stack);
					if(cosmetic != null) {
						GlStateManager.pushMatrix();
						GlStateManager.color(1F, 1F, 1F, 1F);
						((IBaubleRender) cosmetic.getItem()).onPlayerBaubleRender(cosmetic, event, type);
						GlStateManager.popMatrix();
						continue;
					}
				}

				if(item instanceof IBaubleRender) {
					GlStateManager.pushMatrix();
					GlStateManager.color(1F, 1F, 1F, 1F);
					((IBaubleRender) stack.getItem()).onPlayerBaubleRender(stack, event, type);
					GlStateManager.popMatrix();
				}
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
				GlStateManager.pushMatrix();
				Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
				Helper.rotateIfSneaking(event.entityPlayer);
				boolean armor = event.entityPlayer.getCurrentArmor(1) != null;
				GlStateManager.rotate(180F, 1F, 0F, 0F);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-0.25F, -0.85F, renderedOne ? armor ? 0.2F : 0.28F : armor ? -0.3F : -0.25F);
				GlStateManager.scale(0.5F, 0.5F, 0.5F);

				GlStateManager.color(1F, 1F, 1F);
				int light = 15728880;
				int lightmapX = light % 65536;
				int lightmapY = light / 65536;
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);
				Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.THIRD_PERSON);
				GlStateManager.popMatrix();

				if(renderedOne)
					return;
				renderedOne = true;
			}
		}
	}


}
