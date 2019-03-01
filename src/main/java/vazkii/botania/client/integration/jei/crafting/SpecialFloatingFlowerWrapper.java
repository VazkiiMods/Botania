package vazkii.botania.client.integration.jei.crafting;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.crafting.recipe.SpecialFloatingFlowerRecipe;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SpecialFloatingFlowerWrapper implements ICraftingRecipeWrapper {
	private final List<List<ItemStack>> inputs = new ArrayList<>();
	private final ItemStack output;
	private final ResourceLocation name;

	public SpecialFloatingFlowerWrapper(SpecialFloatingFlowerRecipe recipe) {
		name = recipe.getRegistryName();
		inputs.add(ImmutableList.of(ItemBlockSpecialFlower.ofType(recipe.type)));
		List<ItemStack> normalFloaters = new ArrayList<>();
		for(EnumDyeColor color : EnumDyeColor.values()) {
			normalFloaters.add(new ItemStack(ModBlocks.floatingFlower, 1, color.getMetadata()));
		}
		inputs.add(normalFloaters);
		output = ItemBlockSpecialFlower.ofType(new ItemStack(ModBlocks.floatingSpecialFlower), recipe.type);
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
