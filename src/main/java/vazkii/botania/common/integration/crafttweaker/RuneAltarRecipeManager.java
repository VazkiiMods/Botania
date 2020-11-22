package vazkii.botania.common.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;
import vazkii.botania.api.recipe.IPetalRecipe;
import vazkii.botania.api.recipe.IRuneAltarRecipe;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.RecipePetals;
import vazkii.botania.common.crafting.RecipeRuneAltar;

import java.util.Arrays;

@ZenRegister
@ZenCodeType.Name("mods.botania.RuneAltar")
public class RuneAltarRecipeManager implements IRecipeManager {
	
	@ZenCodeType.Method
	public void addRecipe(String name, IItemStack output, int mana, IIngredient... inputs) {
		name = fixRecipeName(name);
		ResourceLocation resourceLocation = new ResourceLocation("crafttweaker", name);
		CraftTweakerAPI.apply(new ActionAddRecipe(this,
				new RecipeRuneAltar(resourceLocation,
						output.getInternal(),
						mana,
						Arrays.stream(inputs).map(IIngredient::asVanillaIngredient).toArray(Ingredient[]::new)), ""));
	}
	
	@Override
	public IRecipeType<IRuneAltarRecipe> getRecipeType() {
		return ModRecipeTypes.RUNE_TYPE;
	}
}
