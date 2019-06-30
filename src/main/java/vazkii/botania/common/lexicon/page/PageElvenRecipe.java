package vazkii.botania.common.lexicon.page;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.Screen;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.lib.LibResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PageElvenRecipe extends PageRecipe {

	private static final ResourceLocation elvenTradeOverlay = new ResourceLocation(LibResources.GUI_ELVEN_TRADE_OVERLAY);

	private final List<RecipeElvenTrade> recipes;
	private int ticksElapsed = 0;
	private int recipeAt = 0;

	public PageElvenRecipe(String unlocalizedName, List<RecipeElvenTrade> recipes) {
		super(unlocalizedName);
		this.recipes = recipes;
	}

	public PageElvenRecipe(String unlocalizedName, RecipeElvenTrade recipe) {
		this(unlocalizedName, Collections.singletonList(recipe));
	}

	@Override
	public void onPageAdded(LexiconEntry entry, int index) {
		for(RecipeElvenTrade recipe : recipes)
			for(ItemStack output : recipe.getOutputs())
				LexiconRecipeMappings.map(output, entry, index);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderRecipe(IGuiLexiconEntry gui, int mx, int my) {
		RecipeElvenTrade recipe = recipes.get(recipeAt);
		TextureManager render = Minecraft.getInstance().textureManager;
		render.bindTexture(elvenTradeOverlay);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color4f(1F, 1F, 1F, 1F);
		((Screen) gui).blit(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());
		GlStateManager.disableBlend();

		List<ItemStack> outputs = recipe.getOutputs();
		for(int i = 0; i < outputs.size(); i++) {
			renderItemAtOutputPos(gui, i % 2, i / 2, outputs.get(i));
		}

		List<Ingredient> inputs = recipe.getInputs();
		int i = 0;
		for(Ingredient input : inputs) {
			renderItemAtInputPos(gui, i, input.getMatchingStacks()[0]);
			i++;
		}

		TextureAtlasSprite portalIcon = MiscellaneousIcons.INSTANCE.alfPortalTex;
		Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		AbstractGui.blit(gui.getLeft() + 22, gui.getTop() + 36, 48, 48, 0, portalIcon); // todo 1.14 zlevel?
	}

	@OnlyIn(Dist.CLIENT)
	public void renderItemAtInputPos(IGuiLexiconEntry gui, int x, ItemStack stack) {
		if(stack.isEmpty())
			return;

		int xPos = gui.getLeft() + x * 20 + 45;
		int yPos = gui.getTop() + 14;

		renderItem(gui, xPos, yPos, stack, false);
	}

	@OnlyIn(Dist.CLIENT)
	public void renderItemAtOutputPos(IGuiLexiconEntry gui, int x, int y, ItemStack stack) {
		if(stack.isEmpty())
			return;

		int xPos = gui.getLeft() + x * 20 + 94;
		int yPos = gui.getTop() + y * 20 + 52;

		renderItem(gui, xPos, yPos, stack, false);
	}


	@Override
	@OnlyIn(Dist.CLIENT)
	public void updateScreen() {
		if(Screen.hasShiftDown())
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
		for(RecipeElvenTrade r : recipes)
			list.addAll(r.getOutputs());

		return list;
	}

}
