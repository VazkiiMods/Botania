package vazkii.botania.common.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.blamejared.crafttweaker.impl.blocks.MCBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;
import vazkii.botania.api.recipe.IElvenTradeRecipe;
import vazkii.botania.api.recipe.IManaInfusionRecipe;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.RecipeElvenTrade;
import vazkii.botania.common.crafting.RecipeManaInfusion;
import vazkii.botania.common.integration.crafttweaker.actions.ActionRemoveElvenTradeRecipe;

import java.util.Arrays;

@ZenRegister
@ZenCodeType.Name("mods.botania.ElvenTrade")
public class ElvenTradeRecipeManager implements IRecipeManager {
	
	@ZenCodeType.Method
	public void addRecipe(String name, IItemStack[] outputs, IIngredient... inputs) {
		name = fixRecipeName(name);
		ResourceLocation resourceLocation = new ResourceLocation("crafttweaker", name);
		CraftTweakerAPI.apply(new ActionAddRecipe(this,
				new RecipeElvenTrade(resourceLocation,
						Arrays.stream(outputs).map(IItemStack::getInternal).toArray(ItemStack[]::new),
						Arrays.stream(inputs).map(IIngredient::asVanillaIngredient).toArray(Ingredient[]::new)), ""));
	}
	
	@Override public void removeRecipe(IItemStack output) {
		removeRecipe(new IItemStack[] { output });
	}
	
	@ZenCodeType.Method
	public void removeRecipe(IItemStack[] outputs) {
		CraftTweakerAPI.apply(new ActionRemoveElvenTradeRecipe(this, outputs));
	}
	
	@Override
	public IRecipeType<IElvenTradeRecipe> getRecipeType() {
		return ModRecipeTypes.ELVEN_TRADE_TYPE;
	}
}
