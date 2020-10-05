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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import vazkii.botania.client.lib.LibResources;

public class GuiBaubleBox extends HandledScreen<ContainerBaubleBox> {

	private static final Identifier texture = new Identifier(LibResources.GUI_BAUBLE_BOX);
	private int mouseX;
	private int mouseY;

	public GuiBaubleBox(ContainerBaubleBox container, PlayerInventory player, Text title) {
		super(container, player, title);
	}

	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.drawMouseoverTooltip(ms, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
		// No-op, there's no space for gui titles
	}

	@Override
	protected void drawBackground(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		MinecraftClient.getInstance().getTextureManager().bindTexture(texture);
		drawTexture(ms, x, y, 0, 0, backgroundWidth, backgroundHeight);
		InventoryScreen.drawEntity(x + 31, y + 75, 30, x + 31 - this.mouseX, y + 75 - 50 - this.mouseY, client.player);
	}

}
