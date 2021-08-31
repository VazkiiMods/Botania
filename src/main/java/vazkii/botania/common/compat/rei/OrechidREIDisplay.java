/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.internal.OrechidOutput;

import java.util.Collections;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryIngredients;

public class OrechidREIDisplay extends OrechidBaseREIDisplay {

	public OrechidREIDisplay(OrechidOutput recipe, int totalWeight) {
		super(recipe, totalWeight);
		this.stone = Collections.singletonList(EntryIngredients.of(new ItemStack(Blocks.STONE, 64)));
	}

	@Override
	public @NotNull CategoryIdentifier<?> getCategoryIdentifier() {
		return BotaniaREICategoryIdentifiers.ORECHID;
	}
}
