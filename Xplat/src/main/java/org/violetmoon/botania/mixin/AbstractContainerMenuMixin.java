/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

import vazkii.botania.api.inventory.BotaniaMenuWithLockedSlot;

/**
 * Menus with locked slots, like those of the trinket case, flower/petal pouch, and hand of ender, want to prevent
 * their source item from being moved out of its slot in any way. They do that with a special slot type for simple
 * pickup with the mouse cursor. However, the slot may not even part of the menu if the off-hand swap key is used
 * and the menu was opened via the off-hand. This mixing handles that special case.
 *
 * @see BotaniaMenuWithLockedSlot
 */
@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {
	@WrapOperation(
		method = "doClick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/inventory/Slot;mayPlace(Lnet/minecraft/world/item/ItemStack;)Z"
		),
		slice = @Slice(
			from = @At(
				value = "FIELD",
				target = "Lnet/minecraft/world/inventory/ClickType;SWAP:Lnet/minecraft/world/inventory/ClickType;",
				opcode = Opcodes.GETSTATIC
			),
			to = @At(
				value = "FIELD",
				target = "Lnet/minecraft/world/inventory/ClickType;CLONE:Lnet/minecraft/world/inventory/ClickType;",
				opcode = Opcodes.GETSTATIC
			)
		),
		expect = 2
	)
	private boolean isAllowedSwap(Slot cursorSlot, ItemStack stackToSwapToCursor, Operation<Boolean> mayPlaceInSlot) {
		if (!mayPlaceInSlot.call(cursorSlot, stackToSwapToCursor)) {
			return false;
		}
		if (!(this instanceof BotaniaMenuWithLockedSlot menuWithLockedSlot)) {
			return true;
		}
		return menuWithLockedSlot.canMoveItem(stackToSwapToCursor);
	}
}
