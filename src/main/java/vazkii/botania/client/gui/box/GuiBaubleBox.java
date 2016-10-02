/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [25/11/2015, 19:59:11 (GMT)]
 */
package vazkii.botania.client.gui.box;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.lib.LibResources;

public class GuiBaubleBox extends InventoryEffectRenderer {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_BAUBLE_BOX);

	public GuiBaubleBox(EntityPlayer player, InventoryBaubleBox box) {
		super(new ContainerBaubleBox(player, box));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		for(int i1 = 0; i1 < 4; ++i1) {
			Slot slot = inventorySlots.inventorySlots.get(i1);
			if(slot.getHasStack() && slot.getSlotStackLimit() == 1)
				drawTexturedModalRect(guiLeft+slot.xDisplayPosition, guiTop+slot.yDisplayPosition, 200, 0, 16, 16);
		}

		GuiInventory.drawEntityOnScreen(guiLeft + 43, guiTop + 61, 20, guiLeft + 43 - mouseX, guiTop + 45 - 30 - mouseY, mc.thePlayer);
	}

}
