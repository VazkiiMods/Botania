/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 8, 2014, 1:11:48 PM (GMT)]
 */
package vazkii.botania.common.lexicon.page;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PageRuneRecipe extends PagePetalRecipe<RecipeRuneAltar> {

	public PageRuneRecipe(String unlocalizedName, List<RecipeRuneAltar> recipes) {
		super(unlocalizedName, recipes);
	}

	public PageRuneRecipe(String unlocalizedName, RecipeRuneAltar recipes) {
		super(unlocalizedName, recipes);
	}

	@Override
	ItemStack getMiddleStack() {
		return new ItemStack(ModBlocks.runeAltar);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderManaBar(IGuiLexiconEntry gui, RecipeRuneAltar recipe, int mx, int my) {
		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		String manaUsage = StatCollector.translateToLocal("botaniamisc.manaUsage");
		font.drawString(manaUsage, gui.getLeft() + gui.getWidth() / 2 - font.getStringWidth(manaUsage) / 2, gui.getTop() + 110, 0x66000000);

		int ratio = 10;
		int x = gui.getLeft() + gui.getWidth() / 2 - 50;
		int y = gui.getTop() + 120;

		if(mx > x + 1 && mx <= x + 101 && my > y - 14 && my <= y + 11)
			ratio = 1;

		HUDHandler.renderManaBar(x, y, 0x0000FF, 0.75F, recipe.getManaUsage(), TilePool.MAX_MANA / ratio);

		String ratioString = String.format(StatCollector.translateToLocal("botaniamisc.ratio"), ratio);
		String stopStr = StatCollector.translateToLocal("botaniamisc.shiftToStopSpin");

		boolean unicode = font.getUnicodeFlag();
		font.setUnicodeFlag(true);
		font.drawString(stopStr, x + 50 - font.getStringWidth(stopStr) / 2, y + 15, 0x99000000);
		font.drawString(ratioString, x + 50 - font.getStringWidth(ratioString) / 2, y + 5, 0x99000000);
		font.setUnicodeFlag(unicode);
		GL11.glDisable(GL11.GL_BLEND);
	}

}
