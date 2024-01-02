package vazkii.botania.fabric.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.PowderSnowBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.item.BotaniaItems;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin {
	@Inject(method = "canEntityWalkOnPowderSnow", at = @At("RETURN"), cancellable = true)
	private static void canWalkOnPowderSnowWithBotaniaItems(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValueZ() && entity instanceof LivingEntity living
				&& (living.getItemBySlot(EquipmentSlot.FEET).is(BotaniaItems.manaweaveBoots)
						|| !EquipmentHandler.findOrEmpty(BotaniaItems.icePendant, living).isEmpty())) {
			cir.setReturnValue(true);
		}
	}
}
