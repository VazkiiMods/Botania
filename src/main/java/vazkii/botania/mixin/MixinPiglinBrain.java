/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelArmor;

@Mixin(PiglinAi.class)
public class MixinPiglinBrain {
	@Inject(at = @At("HEAD"), method = "isWearingGold", cancellable = true)
	private static void terrasteelNeutral(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		for (ItemStack stack : entity.getArmorSlots()) {
			if (stack.getItem() instanceof ItemTerrasteelArmor) {
				cir.setReturnValue(true);
				break;
			}
		}
	}
}
