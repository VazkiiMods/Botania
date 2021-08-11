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
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

public class GuiFlowerBag extends AbstractContainerScreen<ContainerFlowerBag> {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_FLOWER_BAG);

	public GuiFlowerBag(ContainerFlowerBag container, Inventory playerInv, Component title) {
		super(container, playerInv, title);
	}

	@Override
	public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.renderTooltip(ms, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
		String s = I18n.get(ModItems.flowerBag.getDescriptionId());
		font.draw(ms, s, imageWidth / 2 - font.width(s) / 2, 6, 4210752);
		font.draw(ms, I18n.get("container.inventory"), 8, imageHeight - 96 + 2, 4210752);
	}

	@Override
	protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
		Minecraft mc = Minecraft.getInstance();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bind(texture);
		int k = (width - imageWidth) / 2;
		int l = (height - imageHeight) / 2;
		blit(ms, k, l, 0, 0, imageWidth, imageHeight);

		for (Slot slot : menu.slots) {
			if (slot.container == menu.flowerBagInv && !slot.hasItem()) {
				DyeColor color = DyeColor.byId(slot.index);
				ItemStack stack = new ItemStack(ModBlocks.getFlower(color));
				int x = this.leftPos + slot.x;
				int y = this.topPos + slot.y;
				mc.getItemRenderer().renderGuiItem(stack, x, y);
				mc.font.drawShadow(ms, "0", x + 11, y + 9, 0xFF6666);
			}
		}
	}

}
