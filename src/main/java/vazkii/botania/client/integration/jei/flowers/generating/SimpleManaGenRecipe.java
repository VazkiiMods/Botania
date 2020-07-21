/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.flowers.generating;

import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class SimpleManaGenRecipe extends AbstractGenerationCategory.ManaGenRecipe {

	protected final List<ItemStack> stacks;

	protected SimpleManaGenRecipe(List<ItemStack> stacks, int mana) {
		super(mana);
		this.stacks = stacks;
	}

	public List<ItemStack> getStacks() {
		return stacks;
	}

}
