package vazkii.botania.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.item.equipment.bauble.TectonicGirdleItem;

@Mixin(ProtectionEnchantment.class)
public class ProtectionEnchantmentMixin {
	@Inject(method = "getExplosionKnockbackAfterDampener", at = @At("HEAD"), cancellable = true)
	private static void cancelExplosionKnockback(LivingEntity living, double knockback, CallbackInfoReturnable<Double> cir) {
		if (TectonicGirdleItem.negateExplosionKnockback(living)) {
			cir.setReturnValue(0.0);
		}
	}
}
