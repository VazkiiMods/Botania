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
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import vazkii.botania.client.lib.LibResources;

public class GuiBaubleBox extends AbstractContainerScreen<ContainerBaubleBox> {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.GUI_BAUBLE_BOX);
	private int mouseX;
	private int mouseY;

	public GuiBaubleBox(ContainerBaubleBox container, Inventory player, Component title) {
		super(container, player, title);
	}

	@Override
	public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.renderTooltip(ms, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(PoseStack matrixStack, int x, int y) {
		// No-op, there's no space for gui titles
	}

	@Override
	protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getInstance().getTextureManager().bind(texture);
		blit(ms, leftPos, topPos, 0, 0, imageWidth, imageHeight);
		InventoryScreen.renderEntityInInventory(leftPos + 31, topPos + 75, 30, leftPos + 31 - this.mouseX, topPos + 75 - 50 - this.mouseY, minecraft.player);
	}

}
