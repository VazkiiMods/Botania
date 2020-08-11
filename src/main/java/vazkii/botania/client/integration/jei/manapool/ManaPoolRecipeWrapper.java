/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.manapool;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.block.tile.mana.TilePool;

import javax.annotation.Nonnull;
import java.util.List;

public class ManaPoolRecipeWrapper implements IRecipeWrapper {

	private final List<List<ItemStack>> input;
	private final ItemStack output;
	private final int mana;

	public ManaPoolRecipeWrapper(RecipeManaInfusion recipe) {
		ImmutableList.Builder<List<ItemStack>> builder = ImmutableList.builder();

		if(recipe.getInput() instanceof ItemStack) {
			builder.add(ImmutableList.of((ItemStack) recipe.getInput()));
		} else if(recipe.getInput() instanceof String) {
			builder.add(OreDictionary.getOres((String) recipe.getInput()));
		}

		if(recipe.getCatalyst() != null) {
			Block block = recipe.getCatalyst().getBlock();
			if (Item.getItemFromBlock(block) != Items.AIR) {
				builder.add(ImmutableList.of(new ItemStack(block, 1, block.getMetaFromState(recipe.getCatalyst()))));
			}
		}

		input = builder.build();
		output = recipe.getOutput();
		mana = recipe.getManaToConsume();
	}

	@Override
	public void getIngredients(@Nonnull IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, input);
		ingredients.setOutput(VanillaTypes.ITEM, output);
	}

	@Override
	public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		GlStateManager.enableAlpha();
		HUDHandler.renderManaBar(20, 50, 0x0000FF, 0.75F, mana, TilePool.MAX_MANA / 10);
		GlStateManager.disableAlpha();
	}

	@Nonnull
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		return ImmutableList.of();
	}

	@Override
	public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
		return false;
	}

}
