/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import com.google.common.collect.ImmutableSet;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import vazkii.botania.common.item.ModItems;

import java.util.HashSet;
import java.util.Set;

@Mixin(ItemPredicate.class)
public abstract class MixinItemPredicate {
	@ModifyVariable(at = @At("HEAD"), method = "<init>(Lnet/minecraft/tags/Tag;Ljava/util/Set;Lnet/minecraft/advancements/critereon/MinMaxBounds$Ints;Lnet/minecraft/advancements/critereon/MinMaxBounds$Ints;[Lnet/minecraft/advancements/critereon/EnchantmentPredicate;[Lnet/minecraft/advancements/critereon/EnchantmentPredicate;Lnet/minecraft/world/item/alchemy/Potion;Lnet/minecraft/advancements/critereon/NbtPredicate;)V")
	private static Set<Item> addBotaniaShears(Set<Item> set) {
		if (set != null && set.contains(Items.SHEARS)) {
			set = new HashSet<>(set);
			set.add(ModItems.manasteelShears);
			set.add(ModItems.elementiumShears);
			set = ImmutableSet.copyOf(set);
		}
		return set;
	}
}
