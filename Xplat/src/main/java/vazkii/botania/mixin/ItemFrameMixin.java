/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.internal_caps.PhantomInked;

@Mixin(ItemFrame.class)
public class ItemFrameMixin {
	@Inject(
		method = "setItem(Lnet/minecraft/world/item/ItemStack;Z)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/decoration/ItemFrame;onItemChanged(Lnet/minecraft/world/item/ItemStack;)V"
		)
	)
	private void botania_updatePhantomInkedFrame(ItemStack stack, boolean updateNeighbours, CallbackInfo ci) {
		PhantomInked.updateItemFrame((ItemFrame) (Object) this, stack);
	}

	@Inject(method = "interact", at = @At("HEAD"), cancellable = true)
	private void botania_applyPhantomInk(Player player, InteractionHand hand,
			CallbackInfoReturnable<InteractionResult> cir) {
		if (PhantomInked.applyToItemFrame((ItemFrame) (Object) this, player, hand)) {
			cir.setReturnValue(InteractionResult.CONSUME);
		}
	}
}
