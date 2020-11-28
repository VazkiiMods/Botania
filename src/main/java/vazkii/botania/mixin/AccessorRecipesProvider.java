/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.data.server.RecipesProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.tag.Tag;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RecipesProvider.class)
public interface AccessorRecipesProvider {
	@Invoker("conditionsFromItem")
	static InventoryChangedCriterion.Conditions botania_condition(ItemConvertible item) {
		throw new IllegalStateException("");
	}

	@Invoker("conditionsFromTag")
	static InventoryChangedCriterion.Conditions botania_condition(Tag<Item> tag) {
		throw new IllegalStateException("");
	}

	@Invoker("conditionsFromItemPredicates")
	static InventoryChangedCriterion.Conditions botania_condition(ItemPredicate... predicates) {
		throw new IllegalStateException("");
	}
}
