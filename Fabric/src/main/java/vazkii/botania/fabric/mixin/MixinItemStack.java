/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.item.ModItems;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {
	@Shadow
	public abstract boolean is(Item item);

	@Inject(at = @At("HEAD"), method = "is(Lnet/minecraft/world/item/Item;)Z", cancellable = true)
	private void isBotaniaShears(Item item, CallbackInfoReturnable<Boolean> cir) {
		if (item == Items.SHEARS) {
			if (is(ModItems.manasteelShears) || is(ModItems.elementiumShears)) {
				cir.setReturnValue(true);
			}
		}
	}
}
