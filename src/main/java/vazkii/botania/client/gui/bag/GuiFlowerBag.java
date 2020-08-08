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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

public class GuiFlowerBag extends HandledScreen<ContainerFlowerBag> {

	private static final Identifier texture = new Identifier(LibResources.GUI_FLOWER_BAG);

	public GuiFlowerBag(ContainerFlowerBag container, PlayerInventory playerInv, Text title) {
		super(container, playerInv, title);
	}

	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.drawMouseoverTooltip(ms, mouseX, mouseY);
	}

	@Override
	protected void drawForeground(MatrixStack ms, int mouseX, int mouseY) {
		String s = I18n.translate(ModItems.flowerBag.getTranslationKey());
		textRenderer.draw(ms, s, backgroundWidth / 2 - textRenderer.getWidth(s) / 2, 6, 4210752);
		textRenderer.draw(ms, I18n.translate("container.inventory"), 8, backgroundHeight - 96 + 2, 4210752);
	}

	@Override
	protected void drawBackground(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
		MinecraftClient mc = MinecraftClient.getInstance();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(texture);
		int k = (width - backgroundWidth) / 2;
		int l = (height - backgroundHeight) / 2;
		drawTexture(ms, k, l, 0, 0, backgroundWidth, backgroundHeight);

		for (Slot slot : handler.slots) {
			if (slot.inventory == handler.flowerBagInv && !slot.hasStack()) {
				DyeColor color = DyeColor.byId(slot.id);
				ItemStack stack = new ItemStack(ModBlocks.getFlower(color));
				int x = this.x + slot.x;
				int y = this.y + slot.y;
				mc.getItemRenderer().renderGuiItemIcon(stack, x, y);
				mc.textRenderer.drawWithShadow(ms, "0", x + 11, y + 9, 0xFF6666);
			}
		}
	}

}
