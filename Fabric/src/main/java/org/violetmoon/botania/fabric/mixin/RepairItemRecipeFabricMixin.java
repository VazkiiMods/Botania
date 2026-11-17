/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.fabric.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RepairItemRecipe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import vazkii.botania.common.item.BotaniaItems;

@Mixin(RepairItemRecipe.class)
public class RepairItemRecipeFabricMixin {
	@WrapOperation(
		method = "getItemsToCombine",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/crafting/RepairItemRecipe;canCombine(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"
		)
	)
	private boolean preventSpellClothRepair(ItemStack stack1, ItemStack stack2, Operation<Boolean> original) {
		return !stack1.is(BotaniaItems.SPELLBINDING_CLOTH) && !stack2.is(BotaniaItems.SPELLBINDING_CLOTH)
				&& original.call(stack1, stack2);
	}
}
