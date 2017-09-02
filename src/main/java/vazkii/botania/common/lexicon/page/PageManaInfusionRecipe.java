/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 8, 2014, 1:11:42 PM (GMT)]
 */
package vazkii.botania.common.lexicon.page;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PageManaInfusionRecipe extends PageRecipe {

	private static final ResourceLocation manaInfusionOverlay = new ResourceLocation(LibResources.GUI_MANA_INFUSION_OVERLAY);

	private final List<RecipeManaInfusion> recipes;
	private int ticksElapsed = 0;
	private int recipeAt = 0;
	private final ItemStack renderStack;

	public PageManaInfusionRecipe(String unlocalizedName, List<RecipeManaInfusion> recipes) {
		super(unlocalizedName);
		this.recipes = recipes;
		renderStack = new ItemStack(ModBlocks.pool, 1, 0);
		ItemNBTHelper.setBoolean(renderStack, "RenderFull", true);
	}

	public PageManaInfusionRecipe(String unlocalizedName, RecipeManaInfusion recipe) {
		this(unlocalizedName, Collections.singletonList(recipe));
	}

	@Override
	public void onPageAdded(LexiconEntry entry, int index) {
		for(RecipeManaInfusion recipe : recipes)
			LexiconRecipeMappings.map(recipe.getOutput(), entry, index);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderRecipe(IGuiLexiconEntry gui, int mx, int my) {
		RecipeManaInfusion recipe = recipes.get(recipeAt);
		TextureManager render = Minecraft.getMinecraft().renderEngine;
		FontRenderer font = Minecraft.getMinecraft().fontRenderer;

		Object input = recipe.getInput();
		if(input instanceof String)
			input = OreDictionary.getOres((String) input).get(0);

		renderItemAtGridPos(gui, 1, 1, (ItemStack) input, false);

		renderStack.setItemDamage(recipe.getOutput().getItem() == Item.getItemFromBlock(ModBlocks.pool) ? 2 : 0);
		renderItemAtGridPos(gui, 2, 1, renderStack, false);

		renderItemAtGridPos(gui, 3, 1, recipe.getOutput(), false);

		if(recipe.getCatalyst() != null) {
			Block block = recipe.getCatalyst().getBlock();
			if (Item.getItemFromBlock(block) != Items.AIR) {
				renderItemAtGridPos(gui, 1, 2, new ItemStack(block, 1, block.getMetaFromState(recipe.getCatalyst())), false);
			}
		}

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		String manaUsage = I18n.format("botaniamisc.manaUsage");
		font.drawString(manaUsage, gui.getLeft() + gui.getWidth() / 2 - font.getStringWidth(manaUsage) / 2, gui.getTop() + 105, 0x66000000);

		int ratio = 10;
		int x = gui.getLeft() + gui.getWidth() / 2 - 50;
		int y = gui.getTop() + 115;

		if(mx > x + 1 && mx <= x + 101 && my > y - 14 && my <= y + 11)
			ratio = 1;

		HUDHandler.renderManaBar(x, y, 0x0000FF, 0.75F, recipe.getManaToConsume(), TilePool.MAX_MANA / ratio);

		String ratioString = I18n.format("botaniamisc.ratio", ratio);
		String dropString = I18n.format("botaniamisc.drop") + " " + TextFormatting.BOLD + "(?)";

		boolean hoveringOverDrop = false;

		boolean unicode = font.getUnicodeFlag();
		font.setUnicodeFlag(true);
		int dw = font.getStringWidth(dropString);
		int dx = x + 35 - dw / 2;
		int dy = gui.getTop() + 30;

		if(mx > dx && mx <= dx + dw && my > dy && my <= dy + 10)
			hoveringOverDrop = true;

		font.drawString(dropString, dx, dy, 0x77000000);
		font.drawString(ratioString, x + 50 - font.getStringWidth(ratioString) / 2, y + 5, 0x99000000);
		font.setUnicodeFlag(unicode);

		GlStateManager.disableBlend();

		render.bindTexture(manaInfusionOverlay);

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1F, 1F, 1F, 1F);
		((GuiScreen) gui).drawTexturedModalRect(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());
		GlStateManager.disableBlend();

		if(hoveringOverDrop) {
			String key = RenderHelper.getKeyDisplayString("key.drop");
			String tip0 = I18n.format("botaniamisc.dropTip0", TextFormatting.GREEN + key + TextFormatting.WHITE);
			String tip1 = I18n.format("botaniamisc.dropTip1", TextFormatting.GREEN + key + TextFormatting.WHITE);
			RenderHelper.renderTooltip(mx, my, Arrays.asList(tip0, tip1));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateScreen() {
		if(GuiScreen.isShiftKeyDown())
			return;

		if(ticksElapsed % 20 == 0) {
			recipeAt++;

			if(recipeAt == recipes.size())
				recipeAt = 0;
		}
		++ticksElapsed;
	}

	@Override
	public List<ItemStack> getDisplayedRecipes() {
		ArrayList<ItemStack> list = new ArrayList<>();
		for(RecipeManaInfusion r : recipes)
			list.add(r.getOutput());

		return list;
	}

}
