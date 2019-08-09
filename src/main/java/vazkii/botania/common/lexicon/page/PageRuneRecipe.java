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

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.lib.LibMisc;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class PageRuneRecipe extends PageModRecipe<RecipeRuneAltar> {
	
	private static final List<RecipeRuneAltar> DUMMY = Collections.singletonList(new RecipeRuneAltar(new ResourceLocation(LibMisc.MOD_ID, "dummy"), ItemStack.EMPTY, 0));

	public PageRuneRecipe(String unlocalizedName, Item... outputs) {
		super(unlocalizedName, outputs);
	}

	public PageRuneRecipe(String unlocalizedName, Predicate<RecipeRuneAltar> filter, Item... outputs) {
		super(unlocalizedName, filter, outputs);
	}

	@Override
	ItemStack getMiddleStack() {
		return new ItemStack(ModBlocks.runeAltar);
	}

	@OnlyIn(Dist.CLIENT)
	public void renderManaBar(IGuiLexiconEntry gui, RecipeRuneAltar recipe, int mx, int my) {
		FontRenderer font = Minecraft.getInstance().fontRenderer;
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		String manaUsage = I18n.format("botaniamisc.manaUsage");
		font.drawString(manaUsage, gui.getLeft() + gui.getWidth() / 2 - font.getStringWidth(manaUsage) / 2, gui.getTop() + 110, 0x66000000);

		int ratio = 10;
		int x = gui.getLeft() + gui.getWidth() / 2 - 50;
		int y = gui.getTop() + 120;

		if(mx > x + 1 && mx <= x + 101 && my > y - 14 && my <= y + 11)
			ratio = 1;

		HUDHandler.renderManaBar(x, y, 0x0000FF, 0.75F, recipe.getManaUsage(), TilePool.MAX_MANA / ratio);

		String ratioString = I18n.format("botaniamisc.ratio", ratio);
		String stopStr = I18n.format("botaniamisc.shiftToStopSpin");

		font.drawString(stopStr, x + 50 - font.getStringWidth(stopStr) / 2, y + 15, 0x99000000);
		font.drawString(ratioString, x + 50 - font.getStringWidth(ratioString) / 2, y + 5, 0x99000000);
		GlStateManager.disableBlend();
	}

	@Override
	protected List<RecipeRuneAltar> findRecipes() {
		List<RecipeRuneAltar> recipes = findRecipes(BotaniaAPI.runeAltarRecipes.values());
		if(recipes.isEmpty()) {	
			Botania.LOGGER.warn("Could not find runic altar recipes for items {}, using dummy", (Object) outputItems);
			return DUMMY;
		}
		return recipes;
	}
}
