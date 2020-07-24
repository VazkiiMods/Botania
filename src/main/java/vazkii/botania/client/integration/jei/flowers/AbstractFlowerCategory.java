/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.flowers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IIngredientManager;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import vazkii.botania.client.integration.jei.flowers.generating.*;
import vazkii.botania.common.item.ModItems;
import vazkii.patchouli.api.PatchouliAPI;

import javax.annotation.Nonnull;

import java.util.*;

public abstract class AbstractFlowerCategory<T> implements IRecipeCategory<T> {

	private static final Map<ResourceLocation, AbstractFlowerCategory<?>> categories = new HashMap<>();
	protected static final int LEXICON_X = 148;
	protected static final int LEXICON_Y = 44;

	protected final ItemStack flower;
	protected final ItemStack floatingFlower;
	private final IDrawable icon;
	private final IDrawable lexicon;

	@Nonnull
	public final ClientWorld world;

	public AbstractFlowerCategory(IGuiHelper guiHelper, Block flower, Block floatingFlower) {
		this.flower = new ItemStack(flower);
		this.floatingFlower = new ItemStack(floatingFlower);
		icon = guiHelper.createDrawableIngredient(this.flower);
		categories.put(getUid(), this);
		lexicon = guiHelper.createDrawableIngredient(new ItemStack(ModItems.lexicon));
		world = Objects.requireNonNull(Minecraft.getInstance().world);
	}

	@Override
	public ResourceLocation getUid() {
		return Registry.ITEM.getKey(flower.getItem());
	}

	@Override
	public void draw(T recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		RenderSystem.pushMatrix();
		RenderSystem.multMatrix(matrixStack.getLast().getMatrix());
		lexicon.draw(matrixStack, LEXICON_X, LEXICON_Y);
		RenderSystem.popMatrix();
	}

	@Override
	public boolean handleClick(T recipe, double mouseX, double mouseY, int mouseButton) {
		if (isLexiconHovered(mouseX, mouseY)) {
			PatchouliAPI.instance.openBookEntry(Registry.ITEM.getKey(ModItems.lexicon), getLexiconPage(), 0);
			return true;
		}
		return false;
	}

	@Override
	public List<ITextComponent> getTooltipStrings(T recipe, double mouseX, double mouseY) {
		if (isLexiconHovered(mouseX, mouseY)) {
			ArrayList<ITextComponent> tooltip = new ArrayList<>();
			tooltip.add(ModItems.lexicon.getName());
			tooltip.add(new TranslationTextComponent("patchouli.gui.lexicon.view").func_240701_a_(TextFormatting.ITALIC, TextFormatting.GRAY));
			return tooltip;
		}
		return Collections.emptyList();
	}

	protected boolean isLexiconHovered(double mouseX, double mouseY) {
		return LEXICON_X <= mouseX && mouseX <= LEXICON_X + lexicon.getWidth() &&
				LEXICON_Y <= mouseY && mouseY <= LEXICON_Y + lexicon.getHeight();
	}

	protected abstract ResourceLocation getLexiconPage();

	@Override
	public String getTitle() {
		return flower.getDisplayName().getString();
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	protected abstract Collection<T> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers);

	public static void registerCategories(IRecipeCategoryRegistration registry) {
		IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
		registry.addRecipeCategories(
				new EndoflameCategory(guiHelper),
				new HydroangeasCategory(guiHelper),
				new ThermalilyCategory(guiHelper),
				new GourmaryllisCategory(guiHelper),
				new MunchdewCategory(guiHelper),
				new ArcaneRoseCategory(guiHelper),
				new NarslimmusCategory(guiHelper),
				new EntropinnyumCategory(guiHelper),
				new KekimurusCategory(guiHelper),
				new ShulkMeNotCategory(guiHelper),
				new SpectrolusCategory(guiHelper),
				new DandelifeonCategory(guiHelper),
				new RafflowsiaCategory(guiHelper)
		);
	}

	public static void registerRecipes(@Nonnull IRecipeRegistration registry) {
		categories.forEach((uid, category) -> registry.addRecipes(category.makeRecipes(registry.getIngredientManager(), registry.getJeiHelpers()), uid));
	}

	public static void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
		categories.forEach((uid, category) -> {
			registry.addRecipeCatalyst(category.flower, uid);
			registry.addRecipeCatalyst(category.floatingFlower, uid);
		});
	}

}
