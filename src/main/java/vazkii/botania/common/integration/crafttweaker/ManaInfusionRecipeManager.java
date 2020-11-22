package vazkii.botania.common.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.blamejared.crafttweaker.impl.blocks.MCBlockState;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;
import vazkii.botania.api.recipe.IManaInfusionRecipe;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.RecipeManaInfusion;

@ZenRegister
@ZenCodeType.Name("mods.botania.ManaInfusion")
public class ManaInfusionRecipeManager implements IRecipeManager {
	
	@ZenCodeType.Method
	public void addRecipe(String name, IItemStack output, IIngredient input, int mana,
			@ZenCodeType.Optional MCBlockState catalystState) {
		name = fixRecipeName(name);
		ResourceLocation resourceLocation = new ResourceLocation("crafttweaker", name);
		CraftTweakerAPI.apply(new ActionAddRecipe(this,
				new RecipeManaInfusion(resourceLocation, output.getInternal(), input.asVanillaIngredient(), mana, "",
						catalystState == null ? null : catalystState.getInternal()), ""));
	}
	
	@Override
	public IRecipeType<IManaInfusionRecipe> getRecipeType() {
		return ModRecipeTypes.MANA_INFUSION_TYPE;
	}
}
