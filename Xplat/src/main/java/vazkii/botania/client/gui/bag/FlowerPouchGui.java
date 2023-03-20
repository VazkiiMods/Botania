/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.gui.bag;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.lib.ResourcesLib;
import vazkii.botania.common.block.BotaniaBlocks;

public class FlowerPouchGui extends AbstractContainerScreen<FlowerPouchContainer> {

	private static final ResourceLocation texture = new ResourceLocation(ResourcesLib.GUI_FLOWER_BAG);

	public FlowerPouchGui(FlowerPouchContainer container, Inventory playerInv, Component title) {
		super(container, playerInv, title);
		imageHeight += 36;

		// recompute, same as super
		inventoryLabelY = imageHeight - 94;
	}

	@Override
	public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.renderTooltip(ms, mouseX, mouseY);
	}

	@Override
	protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
		Minecraft mc = Minecraft.getInstance();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, texture);
		int k = (width - imageWidth) / 2;
		int l = (height - imageHeight) / 2;
		blit(ms, k, l, 0, 0, imageWidth, imageHeight);

		for (Slot slot : menu.slots) {
			if (slot.container == menu.flowerBagInv) {
				int x = this.leftPos + slot.x;
				int y = this.topPos + slot.y;
				if (!slot.hasItem()) {
					ItemStack missingFlower;
					if (slot.index < 16) {
						DyeColor color = DyeColor.byId(slot.index);
						missingFlower = new ItemStack(BotaniaBlocks.getFlower(color));
					} else {
						DyeColor color = DyeColor.byId(slot.index - 16);
						missingFlower = new ItemStack(BotaniaBlocks.getDoubleFlower(color));
					}
					RenderHelper.renderGuiItemAlpha(missingFlower, x, y, 0x5F, mc.getItemRenderer());
				} else if (slot.getItem().getCount() == 1) {
					ms.pushPose();
					ms.translate(0, 0, mc.getItemRenderer().blitOffset + 300); // similar to ItemRenderer.renderGuiItemDecorations
					mc.font.drawShadow(ms, "1", x + 11, y + 9, 0xFFFFFF);
					ms.popPose();
				}
			}
		}
	}

}
