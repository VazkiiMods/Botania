/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec2;

import vazkii.botania.api.recipe.ITerraPlateRecipe;
import vazkii.botania.client.gui.HUDHandler;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class TerraPlateRecipeCategory implements IRecipeCategory<ITerraPlateRecipe> {
	public static final ResourceLocation UID = prefix("terra_plate");

	private final Component localizedName;
	private final IDrawable background;
	private final IDrawable overlay;
	private final IDrawable icon;

	private final IDrawable terraPlate;

	public TerraPlateRecipeCategory(IGuiHelper guiHelper) {
		ResourceLocation location = prefix("textures/gui/terrasteel_jei_overlay.png");
		background = guiHelper.createBlankDrawable(114, 131);
		overlay = guiHelper.createDrawable(location, 42, 29, 64, 64);
		icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.terraPlate));
		localizedName = new TranslatableComponent("botania.nei.terraPlate");

		IDrawable livingrock = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.livingrock));
		terraPlate = new TerraPlateDrawable(livingrock, livingrock,
				guiHelper.createDrawableIngredient(new ItemStack(Blocks.LAPIS_BLOCK))
		);
	}

	@Nonnull
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public Class<? extends ITerraPlateRecipe> getRecipeClass() {
		return ITerraPlateRecipe.class;
	}

	@Nonnull
	@Override
	public Component getTitle() {
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
	public void draw(@Nonnull ITerraPlateRecipe recipe, @Nonnull PoseStack ms, double mouseX, double mouseY) {
		RenderSystem.enableBlend();
		overlay.draw(ms, 25, 14);
		HUDHandler.renderManaBar(ms, 6, 126, 0x0000FF, 0.75F, recipe.getMana(), 100000);
		terraPlate.draw(ms, 35, 92);
		RenderSystem.disableBlend();
	}

	@Override
	public void setIngredients(@Nonnull ITerraPlateRecipe recipe, @Nonnull IIngredients iIngredients) {
		List<List<ItemStack>> list = new ArrayList<>();
		for (Ingredient ingr : recipe.getIngredients()) {
			list.add(Arrays.asList(ingr.getItems()));
		}
		iIngredients.setInputLists(VanillaTypes.ITEM, list);
		iIngredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull ITerraPlateRecipe recipe, @Nonnull IIngredients ingredients) {
		layout.getItemStacks().init(0, false, 48, 37);
		layout.getItemStacks().set(0, ingredients.getOutputs(VanillaTypes.ITEM).get(0));

		int index = 1;
		double angleBetweenEach = 360.0 / ingredients.getInputs(VanillaTypes.ITEM).size();
		Vec2 point = new Vec2(48, 5), center = new Vec2(48, 37);

		for (List<ItemStack> o : ingredients.getInputs(VanillaTypes.ITEM)) {
			layout.getItemStacks().init(index, true, (int) point.x, (int) point.y);
			layout.getItemStacks().set(index, o);
			index += 1;
			point = PetalApothecaryRecipeCategory.rotatePointAbout(point, center, angleBetweenEach);
		}

		layout.getItemStacks().init(index, true, 48, 92);
		layout.getItemStacks().set(index, new ItemStack(ModBlocks.terraPlate));
	}
}
