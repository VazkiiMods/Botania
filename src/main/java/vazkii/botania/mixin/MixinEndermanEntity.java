/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import vazkii.botania.common.block.subtile.functional.SubTileVinculotus;

@Mixin(EnderMan.class)
public class MixinEndermanEntity {
	/**
	 * Implements the vinculotus for random teleports
	 */
	@ModifyArgs(method = "teleport()Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/EnderMan;teleport(DDD)Z"))
	private void randomVinculotus(Args args) {
		checkForVincs(args);
	}

	/**
	 * Implements the vinculotus for teleports towards a specific entity
	 */
	@ModifyArgs(method = "teleportTowards", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/EnderMan;teleport(DDD)Z"))
	private void entityVinculotus(Args args) {
		checkForVincs(args);
	}

	@Unique
	private void checkForVincs(Args args) {
		double x = args.get(0);
		double y = args.get(1);
		double z = args.get(2);
		Vec3 vincPos = SubTileVinculotus.onEndermanTeleport((EnderMan) (Object) this, x, y, z);
		if (vincPos != null) {
			args.set(0, vincPos.x());
			args.set(1, vincPos.y());
			args.set(2, vincPos.z());
		}
	}
}
