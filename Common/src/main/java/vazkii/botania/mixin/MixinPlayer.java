package vazkii.botania.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import vazkii.botania.common.ModStats;
import vazkii.botania.common.entity.ModEntities;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity {
	protected MixinPlayer(EntityType<? extends LivingEntity> type, Level level) {
		super(type, level);
	}

	@Shadow
	public abstract void awardStat(ResourceLocation stat, int i);

	/**
	 * Updates the distance by luminizer stat
	 */
	@Inject(
		at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/player/Player;getVehicle()Lnet/minecraft/world/entity/Entity;"),
		method = "checkRidingStatistics", locals = LocalCapture.CAPTURE_FAILSOFT
	)
	private void trackLuminizerTravel(double dx, double dy, double dz, CallbackInfo ci, int cm, Entity mount) {
		if (mount.getType() == ModEntities.PLAYER_MOVER) {
			awardStat(ModStats.LUMINIZER_ONE_CM, cm);
		}
	}

}
