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

import java.util.Collections;
import java.util.List;

public class SimpleManaGenRecipe extends AbstractGenerationCategory.ManaGenRecipe {

	protected final List<ItemStack> stacks;

	public SimpleManaGenRecipe(List<ItemStack> stacks, int mana) {
		super(mana);
		this.stacks = stacks;
	}

	public SimpleManaGenRecipe(ItemStack stack, int mana) {
		this(Collections.singletonList(stack), mana);
	}

	public List<ItemStack> getStacks() {
		return stacks;
	}

}
