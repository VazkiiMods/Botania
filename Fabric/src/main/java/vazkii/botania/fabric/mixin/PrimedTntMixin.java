package vazkii.botania.fabric.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.internal_caps.UnethicalTnt;

@Mixin(PrimedTnt.class)
public class PrimedTntMixin {
	@Inject(
		method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V",
		at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/item/PrimedTnt;blocksBuilding:Z", opcode = Opcodes.PUTFIELD)
	)
	void trackTntEntity(EntityType<? extends PrimedTnt> entityType, Level level, CallbackInfo ci) {
		UnethicalTnt.addTrackedTntEntity((PrimedTnt) (Object) this);
	}
}
