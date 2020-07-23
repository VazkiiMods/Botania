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
import net.minecraft.util.IItemProvider;

import java.util.Collections;
import java.util.List;

public class SimpleManaGenRecipe extends AbstractGenerationCategory.ManaGenRecipe {

	private final List<ItemStack> stacks;

	public SimpleManaGenRecipe(List<ItemStack> stacks, int mana) {
		this(stacks, new int[] { mana });
	}

	public SimpleManaGenRecipe(List<ItemStack> stacks, int[] mana) {
		super(mana);
		this.stacks = stacks;
	}

	public SimpleManaGenRecipe(IItemProvider stack, int mana) {
		this(Collections.singletonList(new ItemStack(stack)), mana);
	}

	public List<ItemStack> getStacks() {
		return stacks;
	}

}
