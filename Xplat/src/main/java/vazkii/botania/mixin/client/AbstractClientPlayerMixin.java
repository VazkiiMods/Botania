/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import vazkii.botania.common.item.BotaniaItems;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin {
	/**
	 * Applies the bow pull-back FOV effect to Botania's bows.
	 * 
	 * @param originalResult Original result of the check, typically whether a vanilla bow is being used.
	 * @param useStack       The item currently being used by the player.
	 * @return @{@code true} if the FOV zoom should be applied, otherwise {@code false}.
	 */
	@ModifyExpressionValue(method = "getFieldOfViewModifier", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
	private boolean isBowItem(boolean originalResult, @Local ItemStack useStack) {
		return originalResult || useStack.is(BotaniaItems.LIVINGWOOD_BOW) || useStack.is(BotaniaItems.CRYSTAL_BOW);
	}
}
