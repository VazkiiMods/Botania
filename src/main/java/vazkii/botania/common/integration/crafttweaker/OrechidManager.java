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
import com.blamejared.crafttweaker.api.annotations.ZenRegister;

import net.minecraft.block.BlockState;

import org.openzen.zencode.java.ZenCodeType;

import vazkii.botania.api.internal.OrechidOutput;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.impl.BotaniaAPIImpl;
import vazkii.botania.common.integration.crafttweaker.actions.ActionAddOrechidOre;
import vazkii.botania.common.integration.crafttweaker.actions.ActionClearOrechidList;
import vazkii.botania.common.integration.crafttweaker.actions.ActionRemoveOrechidOre;

import java.util.List;
import java.util.function.Supplier;

@ZenRegister
@ZenCodeType.Name("mods.botania.Orechid")
public class OrechidManager {
	private final Supplier<List<OrechidOutput>> target;
	private final String name;

	public OrechidManager(Supplier<List<OrechidOutput>> target, String name) {
		this.target = target;
		this.name = name;
	}

	@ZenCodeType.Field("main")
	public static final OrechidManager MAIN = new OrechidManager(() -> BotaniaAPIImpl.weights, "Orechid");

	@ZenCodeType.Field("nether")
	public static final OrechidManager NETHER = new OrechidManager(() -> BotaniaAPIImpl.netherWeights, "Orechid Ignem");

	/** Adds the specified ingredient with the specified weight at the end of the ore list. */
	@ZenCodeType.Method
	public void registerOreWeight(StateIngredient ingredient, int weight) {
		CraftTweakerAPI.apply(new ActionAddOrechidOre(ingredient, weight, this));
	}

	/** Removes all outputs that contain the specified state. */
	@ZenCodeType.Method
	public void removeOreWeight(BlockState state) {
		CraftTweakerAPI.apply(new ActionRemoveOrechidOre(state, this));
	}

	/** Completely clears the ore list. */
	@ZenCodeType.Method
	public void clear() {
		CraftTweakerAPI.apply(new ActionClearOrechidList(this));
	}

	public List<OrechidOutput> get() {
		return target.get();
	}

	public String getName() {
		return name;
	}
}
