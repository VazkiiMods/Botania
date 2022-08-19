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
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.lib.LibResources;

public class GuiBaubleBox extends InventoryEffectRenderer {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_BAUBLE_BOX);

	public GuiBaubleBox(EntityPlayer player) {
		super(new ContainerBaubleBox(player));
	}

	@Override
	protected boolean checkHotbarKeys(int p_146983_1_) {
		return false;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(texture);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);

		for(int i1 = 0; i1 < 4; ++i1) {
			Slot slot = (Slot) inventorySlots.inventorySlots.get(i1);
			if(slot.getHasStack() && slot.getSlotStackLimit() == 1)
				drawTexturedModalRect(k+slot.xDisplayPosition, l+slot.yDisplayPosition, 200, 0, 16, 16);
		}

		GuiInventory.func_147046_a(guiLeft + 43, guiTop + 61, 20, guiLeft + 43 - p_146976_2_, guiTop + 45 - 30 - p_146976_3_, mc.thePlayer);
	}

}
