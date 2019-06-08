/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Oct 11, 2015, 7:04:46 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon.button;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

// Ssssshhhhhhh
// I should've done this last year
public class GuiButtonDoot extends GuiButtonLexicon {

	public GuiButtonDoot(int id, int x, int y) {
		super(id, x, y, 16, 16, "");
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		int k = getHoverState(hovered);

		Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.color4f(1F, 1F, 1F, 1F);
		Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(new ItemStack(ModItems.cacophonium), x, y);
		Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(new ItemStack(Items.FIREWORK_ROCKET), x + 8, y + 2);

		GlStateManager.disableLighting();


		List<String> tooltip = new ArrayList<>();
		tooltip.add(TextFormatting.LIGHT_PURPLE + "Happy Birthday Vazkii!");
		tooltip.add(TextFormatting.GRAY + "doot doot");

		if(k == 2)
			RenderHelper.renderTooltip(x - 100, y + 36, tooltip);
		GlStateManager.enableAlphaTest();
	}

}

