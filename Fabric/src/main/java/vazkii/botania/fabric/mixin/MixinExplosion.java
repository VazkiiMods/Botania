/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.item.equipment.bauble.ItemGoddessCharm;

import java.util.List;

@Mixin(Explosion.class)
public class MixinExplosion {
	@Shadow
	@Final
	private Level level;

	@Shadow
	@Final
	private double x;

	@Shadow
	@Final
	private double y;

	@Shadow
	@Final
	private double z;

	@Shadow
	@Final
	private List<BlockPos> toBlow;

	@Inject(method = "finalizeExplosion", at = @At("HEAD"))
	private void onAffectWorld(boolean particles, CallbackInfo ci) {
		ItemGoddessCharm.onExplosion(level, new Vec3(x, y, z), toBlow);
	}
}
