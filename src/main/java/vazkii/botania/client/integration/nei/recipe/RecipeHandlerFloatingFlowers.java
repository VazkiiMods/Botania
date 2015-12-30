package vazkii.botania.client.integration.nei.recipe;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.common.block.BlockFloatingSpecialFlower;
import vazkii.botania.common.block.BlockSpecialFlower;
import vazkii.botania.common.block.ModBlocks;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class RecipeHandlerFloatingFlowers extends TemplateRecipeHandler {

	public class CachedFloatingFlowerRecipe extends CachedRecipe {

		public List<PositionedStack> inputs = new ArrayList<PositionedStack>();
		public PositionedStack output;

		public CachedFloatingFlowerRecipe(ItemStack floatingFlower, ItemStack specialFlower, ItemStack output) {
			inputs.add(new PositionedStack(floatingFlower, 25, 6));
			inputs.add(new PositionedStack(specialFlower, 43, 6));
			this.output = new PositionedStack(output, 119, 24);
			this.output.setMaxSize(1);
		}

		@Override
		public List<PositionedStack> getIngredients() {
			return getCycledIngredients(cycleticks / 20, inputs);
		}

		@Override
		public PositionedStack getResult() {
			return output;
		}

	}

	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal("botania.nei.floatingFlowers");
	}

	@Override
	public void loadTransferRects() {
		transferRects.add(new RecipeTransferRect(new Rectangle(84, 23, 24, 18), "crafting"));
	}

	@Override
	public String getGuiTexture() {
		return "textures/gui/container/crafting_table.png";
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		if(Block.getBlockFromItem(result.getItem()) instanceof BlockFloatingSpecialFlower) {
			ItemStack floatingFlower = new ItemStack(ModBlocks.floatingFlower, 1, OreDictionary.WILDCARD_VALUE);
			ItemStack specialFlower = new ItemStack(ModBlocks.specialFlower);
			specialFlower.setTagCompound((NBTTagCompound) result.getTagCompound().copy());

			arecipes.add(new CachedFloatingFlowerRecipe(floatingFlower, specialFlower, result.copy()));
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		if(Block.getBlockFromItem(ingredient.getItem()) instanceof BlockSpecialFlower) {
			ItemStack floatingFlower = new ItemStack(ModBlocks.floatingFlower, 1, OreDictionary.WILDCARD_VALUE);
			ItemStack result = new ItemStack(ModBlocks.floatingSpecialFlower);
			result.setTagCompound((NBTTagCompound) ingredient.getTagCompound().copy());

			arecipes.add(new CachedFloatingFlowerRecipe(floatingFlower, ingredient.copy(), result));
		}
	}

}
