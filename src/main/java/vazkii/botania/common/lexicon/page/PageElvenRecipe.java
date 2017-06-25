package vazkii.botania.common.lexicon.page;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
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

	final List<RecipeElvenTrade> recipes;
	int ticksElapsed = 0;
	int recipeAt = 0;

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
	@SideOnly(Side.CLIENT)
	public void renderRecipe(IGuiLexiconEntry gui, int mx, int my) {
		RecipeElvenTrade recipe = recipes.get(recipeAt);
		TextureManager render = Minecraft.getMinecraft().renderEngine;
		render.bindTexture(elvenTradeOverlay);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1F, 1F, 1F, 1F);
		((GuiScreen) gui).drawTexturedModalRect(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());
		GlStateManager.disableBlend();

		List<ItemStack> outputs = recipe.getOutputs();
		for(int i = 0; i < outputs.size(); i++) {
			renderItemAtOutputPos(gui, i % 2, i / 2, outputs.get(i));
		}

		List<Object> inputs = recipe.getInputs();
		int i = 0;
		for(Object obj : inputs) {
			Object input = obj;
			if(input instanceof String)
				input = OreDictionary.getOres((String) input).get(0);

			renderItemAtInputPos(gui, i, (ItemStack) input);
			i++;
		}

		TextureAtlasSprite portalIcon = MiscellaneousIcons.INSTANCE.alfPortalTex;
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		((GuiScreen) gui).drawTexturedModalRect(gui.getLeft() + 22, gui.getTop() + 36, portalIcon, 48, 48);
	}

	@SideOnly(Side.CLIENT)
	public void renderItemAtInputPos(IGuiLexiconEntry gui, int x, ItemStack stack) {
		if(stack.isEmpty())
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

	@SideOnly(Side.CLIENT)
	public void renderItemAtOutputPos(IGuiLexiconEntry gui, int x, int y, ItemStack stack) {
		if(stack.isEmpty())
			return;
		stack = stack.copy();

		if(stack.getItemDamage() == Short.MAX_VALUE)
			stack.setItemDamage(0);

		int xPos = gui.getLeft() + x * 20 + 94;
		int yPos = gui.getTop() + y * 20 + 52;

		ItemStack stack1 = stack.copy();
		if (stack1.getItemDamage() == -1) {
			stack1.setItemDamage(0);
		}

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
		ArrayList<ItemStack> list = new ArrayList<>();
		for(RecipeElvenTrade r : recipes)
			list.addAll(r.getOutputs());

		return list;
	}

}
