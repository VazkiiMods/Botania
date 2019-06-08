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

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.SlotItemHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.ModBlocks;

import java.util.List;

public class GuiFlowerBag extends ContainerScreen {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_FLOWER_BAG);

	public GuiFlowerBag(PlayerInventory playerInv, InventoryFlowerBag flowerBagInv) {
		super(new ContainerFlowerBag(playerInv, flowerBagInv));
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.render(mouseX, mouseY, partialTicks);
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
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(texture);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);

		List<Slot> slotList = inventorySlots.inventorySlots;
		for(Slot slot : slotList)
			if(slot instanceof SlotItemHandler) {
				SlotItemHandler slotf = (SlotItemHandler) slot;
				if(!slotf.getHasStack()) {
					DyeColor color = DyeColor.byId(slotf.getSlotIndex());
					ItemStack stack = new ItemStack(ModBlocks.getFlower(color));
					int x = guiLeft + slotf.xPos;
					int y = guiTop + slotf.yPos;
					RenderHelper.enableGUIStandardItemLighting();
					mc.getItemRenderer().renderItemIntoGUI(stack, x, y);
					RenderHelper.disableStandardItemLighting();
					mc.fontRenderer.drawStringWithShadow("0", x + 11, y + 9, 0xFF6666);
				}
			}
	}

}
