package vazkii.botania.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.block.subtile.functional.SubTileLoonuim;
import vazkii.botania.common.components.EntityComponents;
import vazkii.botania.common.components.LooniumComponent;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
	@Shadow
	public abstract void dropStack(ItemStack stack);

	@Inject(at = @At("HEAD"), cancellable = true, method = "dropLoot")
	private void dropLoonium(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
		LooniumComponent comp = EntityComponents.LOONIUM_DROP.getNullable(this);
		if (comp != null && !comp.getDrop().isEmpty()) {
			dropStack(comp.getDrop());
			ci.cancel();
		}

	}
}
