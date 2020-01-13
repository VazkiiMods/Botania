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

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.capability.CuriosCapability;
import top.theillusivec4.curios.api.inventory.CurioStackHandler;
import vazkii.botania.api.item.AccessoryRenderHelper;
import vazkii.botania.api.item.ICosmeticAttachable;
import vazkii.botania.api.item.IPhantomInkable;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import java.util.Map;

public final class BaubleRenderHandler extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

	public BaubleRenderHandler(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> renderer) {
		super(renderer);
	}

	@Override
	public void render(MatrixStack ms, IRenderTypeBuffer buffers, int light, @Nonnull AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		dispatchRenders(ms, buffers, light, player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);

		if (ConfigHandler.CLIENT.renderAccessories.get() && player.getActivePotionEffect(Effects.INVISIBILITY) == null) {
			renderManaTablet(ms, buffers, player);
		}
	}

	// Like LayerCurios, but with Botania-specific logic
	private void dispatchRenders(MatrixStack ms, IRenderTypeBuffer buffers, int light, PlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ms.push();
		CuriosAPI.getCuriosHandler(player).ifPresent(handler -> {
			Map<String, CurioStackHandler> curios = handler.getCurioMap();

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
									ms.push();
									c.render(e.getKey(), ms, buffers, light, player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
									ms.pop();
								}
							});
						}
					}
				}
			}
		});
		ms.pop();
	}

	private void renderManaTablet(MatrixStack ms, IRenderTypeBuffer buffers, PlayerEntity player) {
		boolean renderedOne = false;
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if(!stack.isEmpty() && stack.getItem() == ModItems.manaTablet) {
				ms.push();
				AccessoryRenderHelper.rotateIfSneaking(player);
				boolean armor = !player.getItemStackFromSlot(EquipmentSlotType.LEGS).isEmpty();
				ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90));
				ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
				ms.translate(0, -0.6, 0);
				ms.scale(0.55F, 0.55F, 0.55F);

				if (renderedOne)
					ms.translate(0F, 0F, armor ? 0.55F : 0.5F);
				else
					ms.translate(0F, 0F, armor ? -0.55F : -0.5F);

				ms.scale(0.75F, 0.75F, 0.75F);

				Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.NONE, 0xF000F0, OverlayTexture.DEFAULT_UV, ms, buffers);
				ms.pop();

				if(renderedOne)
					return;
				renderedOne = true;
			}
		}
	}
}
