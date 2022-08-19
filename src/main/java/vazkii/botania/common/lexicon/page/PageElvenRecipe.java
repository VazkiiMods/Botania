package vazkii.botania.common.lexicon.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.BlockAlfPortal;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PageElvenRecipe extends PageRecipe {

	private static final ResourceLocation elvenTradeOverlay = new ResourceLocation(LibResources.GUI_ELVEN_TRADE_OVERLAY);

	List<RecipeElvenTrade> recipes;
	int ticksElapsed = 0;
	int recipeAt = 0;

	public PageElvenRecipe(String unlocalizedName, List<RecipeElvenTrade> recipes) {
		super(unlocalizedName);
		this.recipes = filterRecipes(recipes);
	}

	public PageElvenRecipe(String unlocalizedName, RecipeElvenTrade recipe) {
		this(unlocalizedName, Arrays.asList(recipe));
	}

	@Override
	public void onPageAdded(LexiconEntry entry, int index) {
		for(RecipeElvenTrade recipe : recipes)
			if (recipe != null)
				LexiconRecipeMappings.map(recipe.getOutput(), entry, index);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderRecipe(IGuiLexiconEntry gui, int mx, int my) {
		if (recipes.size() == 0) return;
		RecipeElvenTrade recipe = recipes.get(recipeAt);

		TextureManager render = Minecraft.getMinecraft().renderEngine;
		render.bindTexture(elvenTradeOverlay);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		((GuiScreen) gui).drawTexturedModalRect(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());
		GL11.glDisable(GL11.GL_BLEND);

		renderItemAtGridPos(gui, 3, 1, recipe.getOutput(), false);

		List<Object> inputs = recipe.getInputs();
		int i = 0;
		for(Object obj : inputs) {
			Object input = obj;
			if(input instanceof String)
				input = OreDictionary.getOres((String) input).get(0);

			renderItemAtInputPos(gui, i, (ItemStack) input);
			i++;
		}

		IIcon portalIcon = BlockAlfPortal.portalTex;
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		RenderItem.getInstance().renderIcon(gui.getLeft() + 22, gui.getTop() + 36, portalIcon, 48, 48);
	}

	@SideOnly(Side.CLIENT)
	public void renderItemAtInputPos(IGuiLexiconEntry gui, int x, ItemStack stack) {
		if(stack == null || stack.getItem() == null)
			return;
		stack = stack.copy();

		if(stack.getItemDamage() == Short.MAX_VALUE)
			stack.setItemDamage(0);

		int xPos = gui.getLeft() + x * 20 + 45;
		int yPos = gui.getTop() + 14;
		ItemStack stack1 = stack.copy();
		if(stack1.getItemDamage() == -1)
			stack1.setItemDamage(0);

		renderItem(gui, xPos, yPos, stack1, false);
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
		ArrayList<ItemStack> list = new ArrayList();
		for(RecipeElvenTrade r : recipes)
			list.add(r.getOutput());

		return list;
	}

}
