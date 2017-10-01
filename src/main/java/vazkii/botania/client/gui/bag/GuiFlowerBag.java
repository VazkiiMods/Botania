/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 16, 2015, 6:42:43 PM (GMT)]
 */
package vazkii.botania.client.gui.bag;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.SlotItemHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.ModBlocks;

import java.util.List;

public class GuiFlowerBag extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_FLOWER_BAG);

	public GuiFlowerBag(InventoryPlayer playerInv, InventoryFlowerBag flowerBagInv) {
		super(new ContainerFlowerBag(playerInv, flowerBagInv));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = I18n.format("item.botania:flowerBag.name");
		fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(texture);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);

		List<Slot> slotList = inventorySlots.inventorySlots;
		for(Slot slot : slotList)
			if(slot instanceof SlotItemHandler) {
				SlotItemHandler slotf = (SlotItemHandler) slot;
				if(!slotf.getHasStack()) {
					ItemStack stack = new ItemStack(ModBlocks.flower, 1, slotf.getSlotIndex()); // index matches colors
					int x = guiLeft + slotf.xPos;
					int y = guiTop + slotf.yPos;
					RenderHelper.enableGUIStandardItemLighting();
					mc.getRenderItem().renderItemIntoGUI(stack, x, y);
					RenderHelper.disableStandardItemLighting();
					mc.fontRenderer.drawStringWithShadow("0", x + 11, y + 9, 0xFF6666);
				}
			}
	}

}
