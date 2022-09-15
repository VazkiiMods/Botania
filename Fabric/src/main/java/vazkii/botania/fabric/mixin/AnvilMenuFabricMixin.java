/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.mixin;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.item.SpellbindingClothItem;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuFabricMixin extends ItemCombinerMenu {
	@Shadow
	@Final
	private DataSlot cost;

	public AnvilMenuFabricMixin(MenuType<?> type, int i, Inventory inv, ContainerLevelAccess cla) {
		super(type, i, inv, cla);
	}

	@Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
	private void checkIfCloth(CallbackInfo ci) {
		if (SpellbindingClothItem.shouldDenyAnvil(inputSlots.getItem(0), inputSlots.getItem(1))) {
			this.resultSlots.setItem(0, ItemStack.EMPTY);
			this.cost.set(0);
			ci.cancel();
		}
	}
}
