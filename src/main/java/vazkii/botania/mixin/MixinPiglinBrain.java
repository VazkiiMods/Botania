/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelArmor;

@Mixin(PiglinBrain.class)
public class MixinPiglinBrain {
	@Inject(at = @At("HEAD"), method = "wearsGoldArmor", cancellable = true)
	private static void terrasteelNeutral(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		for (ItemStack stack : entity.getArmorItems()) {
			if (stack.getItem() instanceof ItemTerrasteelArmor) {
				cir.setReturnValue(true);
				break;
			}
		}
	}
}
