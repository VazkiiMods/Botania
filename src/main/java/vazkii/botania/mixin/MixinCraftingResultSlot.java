/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.item.ItemCraftingHalo;

@Mixin(CraftingResultSlot.class)
public class MixinCraftingResultSlot {
	@Shadow
	@Final
	private CraftingInventory input;

	@Shadow
	@Final
	private PlayerEntity player;

	@Inject(
		at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;onCraft(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;I)V"),
		method = "onCrafted(Lnet/minecraft/item/ItemStack;)V"
	)
	private void onCraft(ItemStack stack, CallbackInfo ci) {
		ItemCraftingHalo.onItemCrafted(player, input);
	}
}
