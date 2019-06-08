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
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GLX;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.capability.CuriosCapability;
import top.theillusivec4.curios.api.inventory.CurioStackHandler;
import vazkii.botania.api.item.AccessoryRenderHelper;
import vazkii.botania.api.item.ICosmeticAttachable;
import vazkii.botania.api.item.IPhantomInkable;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import java.util.Map;

public final class BaubleRenderHandler implements LayerRenderer<PlayerEntity> {

	@Override
	public void render(@Nonnull PlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		dispatchRenders(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);

		// todo 1.13 recheck what this was checking for (make sure to account for config+invis)
		/*
		if(!h.getStackInSlot(3).isEmpty())
			renderManaTablet(player);
		*/
	}

	// Like LayerCurios, but with Botania-specific logic
	private void dispatchRenders(PlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
	    GlStateManager.pushMatrix();
		CuriosAPI.getCuriosHandler(player).ifPresent(handler -> {
			Map<String, CurioStackHandler> curios = handler.getCurioMap();
			if(player.isSneaking()) {
				GlStateManager.translatef(0, 0.2F, 0);
			}

			for(Map.Entry<String, CurioStackHandler> e : curios.entrySet()) {
			    for(int i = 0; i < e.getValue().getSlots(); i++) {
					ItemStack stack = e.getValue().getStackInSlot(i);
					if(!stack.isEmpty()) {
						Item item = stack.getItem();

						if(item instanceof IPhantomInkable) {
							IPhantomInkable inkable = (IPhantomInkable) item;
							if(inkable.hasPhantomInk(stack))
								continue;
						}

						if(item instanceof ICosmeticAttachable) {
							ICosmeticAttachable attachable = (ICosmeticAttachable) item;
							ItemStack cosmetic = attachable.getCosmeticItem(stack);
							cosmetic.getCapability(CuriosCapability.ITEM).ifPresent(c -> {
								if(c.hasRender(e.getKey(), player)) {
								    GlStateManager.pushMatrix();
									GlStateManager.color4f(1F, 1F, 1F, 1F);
									c.doRender(e.getKey(), player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
									GlStateManager.popMatrix();
								}
							});
						}
					}
				}
			}
		});
		GlStateManager.popMatrix();
	}

	@SuppressWarnings("deprecation")
	private void renderManaTablet(PlayerEntity player) {
		boolean renderedOne = false;
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if(!stack.isEmpty() && stack.getItem() == ModItems.manaTablet) {
				GlStateManager.pushMatrix();
				Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
				AccessoryRenderHelper.rotateIfSneaking(player);
				boolean armor = !player.getItemStackFromSlot(EquipmentSlotType.LEGS).isEmpty();
				GlStateManager.rotatef(90, 0, 1, 0);
				GlStateManager.rotatef(180, 0, 0, 1);
				GlStateManager.translated(0, -0.6, 0);
				GlStateManager.scaled(0.55, 0.55, 0.55);

				if (renderedOne)
					GlStateManager.translatef(0F, 0F, armor ? 0.55F : 0.5F);
				else
					GlStateManager.translatef(0F, 0F, armor ? -0.55F : -0.5F);

				GlStateManager.scalef(0.75F, 0.75F, 0.75F);

				GlStateManager.color3f(1F, 1F, 1F);
				int light = 15728880;
				int lightmapX = light % 65536;
				int lightmapY = light / 65536;
				GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, lightmapX, lightmapY);
				Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
				GlStateManager.popMatrix();

				if(renderedOne)
					return;
				renderedOne = true;
			}
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}
