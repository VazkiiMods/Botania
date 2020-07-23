/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.proxy;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.patchouli.api.IMultiblock;

public interface IProxy {
	default void registerHandlers() {}

	default boolean isTheClientPlayer(LivingEntity entity) {
		return false;
	}

	default PlayerEntity getClientPlayer() {
		return null;
	}

	default boolean isClientPlayerWearingMonocle() {
		return false;
	}

	default long getWorldElapsedTicks() {
		return ServerLifecycleHooks.getCurrentServer().getWorld(World.field_234918_g_).getGameTime();
	}

	default void lightningFX(Vector3 vectorStart, Vector3 vectorEnd, float ticksPerMeter, int colorOuter, int colorInner) {
		lightningFX(vectorStart, vectorEnd, ticksPerMeter, System.nanoTime(), colorOuter, colorInner);
	}

	default void lightningFX(Vector3 vectorStart, Vector3 vectorEnd, float ticksPerMeter, long seed, int colorOuter, int colorInner) {

	}

	default void addBoss(EntityDoppleganger boss) {

	}

	default void removeBoss(EntityDoppleganger boss) {

	}

	default int getClientRenderDistance() {
		return 0;
	}

	// Side-safe version of world.addParticle with noDistanceLimit flag set to true
	default void addParticleForce(World world, IParticleData particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {}

	default void showMultiblock(IMultiblock mb, ITextComponent name, BlockPos anchor, Rotation rot) {}

	default void clearSextantMultiblock() {}
}
