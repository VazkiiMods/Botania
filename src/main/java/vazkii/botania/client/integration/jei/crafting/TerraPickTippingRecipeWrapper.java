package vazkii.botania.client.integration.jei.crafting;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.crafting.recipe.TerraPickTippingRecipe;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TerraPickTippingRecipeWrapper implements ICraftingRecipeWrapper {
	private final List<List<ItemStack>> inputs;
	private final ItemStack output;
	private final ResourceLocation name;
	
	public TerraPickTippingRecipeWrapper(TerraPickTippingRecipe recipe) {
		inputs = ImmutableList.of(ImmutableList.of(new ItemStack(ModItems.terraPick)), ImmutableList.of(new ItemStack(ModItems.elementiumPick)));
		output = new ItemStack(ModItems.terraPick);
		ItemTerraPick.setTipped(output);
		
		this.name = recipe.getRegistryName();
	}
	
	@Override
	public void getIngredients(@Nonnull IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, inputs);
		ingredients.setOutput(VanillaTypes.ITEM, output);
	}

	@Nullable
	@Override
	public ResourceLocation getRegistryName() {
		return name;
	}
}
