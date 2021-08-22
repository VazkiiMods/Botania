/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.crafting.RecipeManaInfusion;

import javax.annotation.Nullable;

import java.util.Optional;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;

@Environment(EnvType.CLIENT)
public class ManaPoolREIDisplay extends BotaniaRecipeDisplay<RecipeManaInfusion> {
	@Nullable
	private BlockState catalyst;

	public ManaPoolREIDisplay(RecipeManaInfusion recipe) {
		super(recipe);
		this.catalyst = recipe.getCatalyst();
	}

	public Optional<BlockState> getCatalyst() {
		return Optional.ofNullable(this.catalyst);
	}

	@Override
	public int getManaCost() {
		return recipe.getManaToConsume();
	}

	@Override
	public @NotNull CategoryIdentifier<?> getCategoryIdentifier() {
		return BotaniaREICategoryIdentifiers.MANA_INFUSION;
	}
}
