/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.gui.box;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import vazkii.botania.client.lib.ResourcesLib;

public class TrinketCaseScreen extends AbstractContainerScreen<TrinketCaseMenu> {

	private static final ResourceLocation texture = ResourceLocation.parse(ResourcesLib.GUI_BAUBLE_BOX);
	private int mouseX;
	private int mouseY;

	public TrinketCaseScreen(TrinketCaseMenu container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		// No-op, there's no space for gui titles
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		guiGraphics.blit(texture, leftPos, topPos, 0, 0, imageWidth, imageHeight);
		InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics,
				leftPos + 8, topPos + 8, leftPos + 57, topPos + 78,
				30, 0.0625F, this.mouseX, this.mouseY, minecraft.player);
	}

}
