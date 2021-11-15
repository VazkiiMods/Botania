/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.recipes.GatherReplacementExclusionEvent;
import com.blamejared.crafttweaker.impl.commands.CTCommandCollectionEvent;
import com.blamejared.crafttweaker.impl.commands.CommandCaller;
import com.blamejared.crafttweaker.impl.tag.manager.TagManagerBlock;
import com.blamejared.crafttweaker.impl_native.blocks.ExpandBlock;
import com.blamejared.crafttweaker.impl_native.blocks.ExpandBlockState;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.block.BlockState;
import net.minecraft.command.CommandSource;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.OrechidOutput;
import vazkii.botania.api.recipe.IElvenTradeRecipe;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.crafting.LexiconElvenTradeRecipe;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.StateIngredientBlock;
import vazkii.botania.common.crafting.StateIngredientBlockState;
import vazkii.botania.common.crafting.StateIngredientBlocks;
import vazkii.botania.common.crafting.StateIngredientTag;

import java.util.stream.Collectors;

public class CTIntegration {
	public static void register() {
		MinecraftForge.EVENT_BUS.addListener((CTCommandCollectionEvent e) -> e.registerDump("orechidOutputs",
				"Dumps all Botania Orechid outputs", (CommandCaller) CTIntegration::dumpOrechid));

		MinecraftForge.EVENT_BUS.addListener((GatherReplacementExclusionEvent e) -> {
			IRecipeType<?> type = e.getTargetedManager().getRecipeType();
			if (type == ModRecipeTypes.ELVEN_TRADE_TYPE) {
				e.getTargetedManager().getRecipes().values().stream()
						.map(r -> (IElvenTradeRecipe) r)
						.filter(r -> (r instanceof LexiconElvenTradeRecipe))
						.forEach(e::addExclusion);
			}
		});
	}

	private static int dumpOrechid(CommandContext<CommandSource> ctx) {
		CraftTweakerAPI.logDump("Orechid: ");
		for (OrechidOutput output : BotaniaAPI.instance().getOrechidWeights()) {
			CraftTweakerAPI.logDump("- " + ingredientToCommandString(output.getOutput()) + " : " + output.getWeight());
		}
		CraftTweakerAPI.logDump("Orechid Ignem: ");
		for (OrechidOutput output : BotaniaAPI.instance().getNetherOrechidWeights()) {
			CraftTweakerAPI.logDump("- " + ingredientToCommandString(output.getOutput()) + " : " + output.getWeight());
		}
		String msg = "List of Orechid ores generated! Check the crafttweaker.log file.";
		ctx.getSource().sendFeedback(new StringTextComponent(msg).mergeStyle(TextFormatting.GREEN), false);
		CraftTweakerAPI.logInfo(msg);
		return 0;
	}

	public static String ingredientToCommandString(StateIngredient ingr) {
		if (ingr instanceof StateIngredientTag) {
			ResourceLocation id = ((StateIngredientTag) ingr).getTagId();
			return TagManagerBlock.INSTANCE.getTag(id).getCommandString();
		}
		if (ingr instanceof StateIngredientBlock) {
			return ExpandBlock.getCommandString(((StateIngredientBlock) ingr).getBlock());
		}
		if (ingr instanceof StateIngredientBlockState) {
			return ExpandBlockState.getCommandString(((StateIngredientBlockState) ingr).getState());
		}
		if (ingr instanceof StateIngredientBlocks) {
			return ingr.getDisplayed().stream().map(BlockState::getBlock)
					.map(ExpandBlock::getCommandString)
					.collect(Collectors.joining(", ", "[", "]"));
		}
		return ingr.toString();
	}
}
