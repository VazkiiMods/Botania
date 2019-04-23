/**
 * This class was created by <codewarrior0>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.orechid;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.integration.jei.JEIBotaniaPlugin;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrechidRecipeCategory implements IRecipeCategory<OrechidRecipeWrapper> {

	public static final ResourceLocation UID = new ResourceLocation(LibMisc.MOD_ID, "orechid");
	private final IDrawableStatic background;
	private final String localizedName;
	private final IDrawableStatic overlay;
	private final IDrawable icon;

	public OrechidRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createBlankDrawable(168, 64);
		localizedName = I18n.format("botania.nei.orechid");
		overlay = guiHelper.createDrawable(new ResourceLocation("botania", "textures/gui/pureDaisyOverlay.png"),
				0, 0, 64, 46);
		icon = guiHelper.createDrawableIngredient(ItemBlockSpecialFlower.ofType(ModSubtiles.orechid));
	}

	@Nonnull
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public Class<? extends OrechidRecipeWrapper> getRecipeClass() {
		return OrechidRecipeWrapper.class;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return localizedName;
	}

	@Nonnull
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Nonnull
	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setIngredients(OrechidRecipeWrapper recipe, IIngredients ingredients) {
		ingredients.setInput(VanillaTypes.ITEM, new ItemStack(Blocks.STONE, 64));

		final int myWeight = recipe.entry.getValue();
		final int amount = Math.max(1, Math.round((float) myWeight * 64 / getTotalOreWeight(BotaniaAPI.oreWeights, myWeight)));

		// Shouldn't ever return an empty list since the ore weight
		// list is filtered to only have ores with ItemBlocks
		List<ItemStack> stackList = BlockTags.getCollection().getOrCreate(recipe.entry.getKey())
				.getAllElements()
				.stream()
				.filter(s -> s.asItem() != Items.AIR)
				.map(ItemStack::new)
				.collect(Collectors.toList());

		stackList.forEach(s -> s.setCount(amount));
		ingredients.setOutputLists(VanillaTypes.ITEM, Collections.singletonList(stackList));
	}

	public static float getTotalOreWeight(Map<ResourceLocation, Integer> weights, int myWeight) {
		return (weights.entrySet().stream()
				.filter(e -> JEIBotaniaPlugin.doesOreExist(e.getKey()))
				.map(Map.Entry::getValue)
				.reduce(Integer::sum)).orElse(myWeight * 64 * 64);
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull OrechidRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
		final IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

		itemStacks.init(0, true, 40, 12);
		itemStacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));

		itemStacks.init(1, true, 70, 12);
		itemStacks.set(1, ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ORECHID));

		itemStacks.init(2, true, 99, 12);
		itemStacks.set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
	}

	@Override
	public void draw(OrechidRecipeWrapper recipe, double mouseX, double mouseY) {
		GlStateManager.enableAlphaTest();
		GlStateManager.enableBlend();
		overlay.draw(48, 0);
		GlStateManager.disableBlend();
		GlStateManager.disableAlphaTest();
	}

}
