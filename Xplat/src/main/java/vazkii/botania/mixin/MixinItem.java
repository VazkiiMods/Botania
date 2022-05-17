/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.world.item.Item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.item.material.ItemSelfReturning;

@Mixin(Item.class)
public class MixinItem {
	@Inject(at = @At("HEAD"), method = "getCraftingRemainingItem", cancellable = true)
	private void returnSelf(CallbackInfoReturnable<Item> cir) {
		Item self = (Item) (Object) this;
		if (self instanceof ItemSelfReturning) {
			cir.setReturnValue(self);
		}
	}
}
