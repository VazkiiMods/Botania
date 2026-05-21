package vazkii.botania.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.block.block_entity.flower.generating.RosaArcanaBlockEntity;
import vazkii.botania.common.helper.MathHelper;

@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbMixin extends Entity {
	@Unique
	@Nullable
	private BlockPos botania_followingFlower;

	public ExperienceOrbMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "scanForEntities", at = @At("HEAD"))
	private void scanForFlower(CallbackInfo ci) {
		if (this.botania_followingFlower == null
				|| MathHelper.distSqr(this.botania_followingFlower, this.blockPosition()) > 64.0) {
			this.botania_followingFlower = RosaArcanaBlockEntity.getClosestMatchingBlockEntity(
					this.level(), this.blockPosition(), 8, RosaArcanaBlockEntity.class::isInstance);
		}
	}

	@WrapOperation(
		method = "tick",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isSpectator()Z")
	)
	private boolean skipFollowingPlayerIfFollowingFlower(Player instance, Operation<Boolean> original) {
		return this.botania_followingFlower != null || original.call(instance);
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/ExperienceOrb;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V"
		)
	)
	private void setMovementTowardsFlower(CallbackInfo ci) {
		// [VanillaCopy] ExperienceOrb::tick, section "if (this.followingPlayer != null)"
		if (this.botania_followingFlower != null) {
			Vec3 vec3 = this.botania_followingFlower.getCenter().subtract(this.position());
			double d0 = vec3.lengthSqr();
			if (d0 < 64.0) {
				double d1 = 1.0 - Math.sqrt(d0) / 8.0;
				this.setDeltaMovement(this.getDeltaMovement().add(vec3.normalize().scale(d1 * d1 * 0.1)));
			}
		}
	}
}
