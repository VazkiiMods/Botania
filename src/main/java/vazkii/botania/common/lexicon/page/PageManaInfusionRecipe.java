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

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class PageManaInfusionRecipe extends PageRecipe {

	private static final ResourceLocation manaInfusionOverlay = new ResourceLocation(LibResources.GUI_MANA_INFUSION_OVERLAY);
	private static final List<RecipeManaInfusion> DUMMY = Collections.singletonList(new RecipeManaInfusion(new ResourceLocation(LibMisc.MOD_ID, "dummy"), ItemStack.EMPTY, Ingredient.EMPTY, 0));

	private List<RecipeManaInfusion> recipes = null;
	private final Item[] outputItems;
	private final Predicate<RecipeManaInfusion> recipeFilter;
	private int ticksElapsed = 0;
	private int recipeAt = 0;
	private final ItemStack renderStack;


	public PageManaInfusionRecipe(String unlocalizedName, Item... outputs) {
		this(unlocalizedName, r -> true, outputs);
	}
	
	public PageManaInfusionRecipe(String unlocalizedName, Predicate<RecipeManaInfusion> filter, Item... outputs) {
		super(unlocalizedName);
		this.outputItems = outputs;
		this.recipeFilter = filter;
		renderStack = new ItemStack(ModBlocks.manaPool);
		ItemNBTHelper.setBoolean(renderStack, "RenderFull", true);
	}

	public PageManaInfusionRecipe(String unlocalizedName, Predicate<RecipeManaInfusion> filter, Tag<Item> tag) {
		this(unlocalizedName, filter, tag.getAllElements().toArray(new Item[0]));
	}

	@Override
	public void onPageAdded(LexiconEntry entry, int index) {
		for(Item item : outputItems) {
			LexiconRecipeMappings.map(new ItemStack(item), entry, index);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderRecipe(IGuiLexiconEntry gui, int mx, int my) {
		RecipeManaInfusion recipe = getRecipes().get(recipeAt);
		TextureManager render = Minecraft.getInstance().textureManager;
		FontRenderer font = Minecraft.getInstance().fontRenderer;

		if(recipe.getInput().getMatchingStacks().length > 0)
			renderItemAtGridPos(gui, 1, 1, recipe.getInput().getMatchingStacks()[0], false);

		renderItemAtGridPos(gui, 2, 1, renderStack, false);

		renderItemAtGridPos(gui, 3, 1, recipe.getOutput(), false);

		if(recipe.getCatalyst() != null) {
			Block block = recipe.getCatalyst().getBlock();
			if (block.asItem() != Items.AIR) {
				renderItemAtGridPos(gui, 1, 2, new ItemStack(block), false);
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

		int dw = font.getStringWidth(dropString);
		int dx = x + 35 - dw / 2;
		int dy = gui.getTop() + 30;

		if(mx > dx && mx <= dx + dw && my > dy && my <= dy + 10)
			hoveringOverDrop = true;

		font.drawString(dropString, dx, dy, 0x77000000);
		font.drawString(ratioString, x + 50 - font.getStringWidth(ratioString) / 2, y + 5, 0x99000000);

		GlStateManager.disableBlend();

		render.bindTexture(manaInfusionOverlay);

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color4f(1F, 1F, 1F, 1F);
		((Screen) gui).blit(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());
		GlStateManager.disableBlend();

		if(hoveringOverDrop) {
			String key = RenderHelper.getKeyDisplayString("key.drop");
			String tip0 = I18n.format("botaniamisc.dropTip0", TextFormatting.GREEN + key + TextFormatting.WHITE);
			String tip1 = I18n.format("botaniamisc.dropTip1", TextFormatting.GREEN + key + TextFormatting.WHITE);
			RenderHelper.renderTooltip(mx, my, Arrays.asList(tip0, tip1));
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void updateScreen() {
		if(Screen.hasShiftDown())
			return;

		if(ticksElapsed % 20 == 0) {
			recipeAt++;

			if(recipeAt == getRecipes().size())
				recipeAt = 0;
		}
		++ticksElapsed;
	}

	@Override
	public List<ItemStack> getDisplayedRecipes() {
		ArrayList<ItemStack> list = new ArrayList<>();
		for(RecipeManaInfusion r : getRecipes())
			list.add(r.getOutput());

		return list;
	}

	private List<RecipeManaInfusion> findRecipes() {
		List<RecipeManaInfusion> list = new ArrayList<>();
		for(RecipeManaInfusion recipe : BotaniaAPI.manaInfusionRecipes.values()) {
			if(recipeFilter.test(recipe)) {
				Item recipeOutput = recipe.getOutput().getItem();
				for(Item output : outputItems) {
					if(recipeOutput == output) {
						list.add(recipe);
						break;
					}
				}
			}
		}
		if(list.isEmpty()) {
			Botania.LOGGER.warn("Could not find mana infusion recipes for items {}, using dummy", (Object) outputItems);
			return DUMMY;
		} else {
			list.sort((r1, r2) -> {
				Item output1 = r1.getOutput().getItem();
				Item output2 = r2.getOutput().getItem();
				if(output1 == output2)
					return 0;
				for(Item outputItem : outputItems) {
					if(outputItem == output1) {
						return -1;
					} else if(outputItem == output2) {
						return 1;
					}
				}
				return 0;
			});
		}
		return list;
	}

	public List<RecipeManaInfusion> getRecipes() {
		if(recipes == null) {
			recipes = findRecipes();
		}
		return recipes;
	}
}
