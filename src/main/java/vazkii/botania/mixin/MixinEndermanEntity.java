package vazkii.botania.mixin;

import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import vazkii.botania.common.block.subtile.functional.SubTileVinculotus;

@Mixin(EndermanEntity.class)
public class MixinEndermanEntity {
	/**
	 * Implements the vinculotus for random teleports
	 */
	@ModifyArgs(method = "teleportRandomly", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/EndermanEntity;teleportTo(DDD)Z"))
	private void randomVinculotus(Args args) {
		checkForVincs(args);
	}

	/**
	 * Implements the vinculotus for teleports towards a specific entity
	 */
	@ModifyArgs(method = "teleportTo(Lnet/minecraft/entity/Entity;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/EndermanEntity;teleportTo(DDD)Z"))
	private void entityVinculotus(Args args) {
		checkForVincs(args);
	}

	@Unique
	private void checkForVincs(Args args) {
		double x = args.get(0);
		double y = args.get(1);
		double z = args.get(2);
		Vec3d vincPos = SubTileVinculotus.onEndermanTeleport((EndermanEntity) (Object) this, x, y, z);
		if (vincPos != null) {
			args.set(0, vincPos.getX());
			args.set(1, vincPos.getY());
			args.set(2, vincPos.getZ());
		}
	}
}
