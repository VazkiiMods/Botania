package vazkii.botania.common.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;
import vazkii.botania.api.item.IPetalApothecary;
import vazkii.botania.api.recipe.IElvenTradeRecipe;
import vazkii.botania.api.recipe.IPetalRecipe;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.RecipeElvenTrade;
import vazkii.botania.common.crafting.RecipePetals;

import java.util.Arrays;

@ZenRegister
@ZenCodeType.Name("mods.botania.PetalApothecary")
public class PetalApothecaryRecipeManager implements IRecipeManager {
	
	@ZenCodeType.Method
	public void addRecipe(String name, IItemStack output, IIngredient... inputs) {
		name = fixRecipeName(name);
		ResourceLocation resourceLocation = new ResourceLocation("crafttweaker", name);
		CraftTweakerAPI.apply(new ActionAddRecipe(this,
				new RecipePetals(resourceLocation,
						output.getInternal(),
						Arrays.stream(inputs).map(IIngredient::asVanillaIngredient).toArray(Ingredient[]::new)), ""));
	}
	
	@Override
	public IRecipeType<IPetalRecipe> getRecipeType() {
		return ModRecipeTypes.PETAL_TYPE;
	}
}
