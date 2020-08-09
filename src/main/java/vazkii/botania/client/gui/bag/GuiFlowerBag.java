/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.gui.bag;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

public class GuiFlowerBag extends ContainerScreen<ContainerFlowerBag> {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_FLOWER_BAG);

	public GuiFlowerBag(ContainerFlowerBag container, PlayerInventory playerInv, ITextComponent title) {
		super(container, playerInv, title);
	}

	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.func_230459_a_(ms, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
		String s = I18n.format(ModItems.flowerBag.getTranslationKey());
		font.drawString(ms, s, xSize / 2 - font.getStringWidth(s) / 2, 6, 4210752);
		font.drawString(ms, I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
		Minecraft mc = Minecraft.getInstance();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(texture);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		blit(ms, k, l, 0, 0, xSize, ySize);

		for (Slot slot : container.inventorySlots) {
			if (slot.inventory == container.flowerBagInv && !slot.getHasStack()) {
				DyeColor color = DyeColor.byId(slot.getSlotIndex());
				ItemStack stack = new ItemStack(ModBlocks.getFlower(color));
				int x = guiLeft + slot.xPos;
				int y = guiTop + slot.yPos;
				mc.getItemRenderer().renderItemIntoGUI(stack, x, y);
				mc.fontRenderer.drawStringWithShadow(ms, "0", x + 11, y + 9, 0xFF6666);
			}
		}
	}

}
