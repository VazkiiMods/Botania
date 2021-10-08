/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.proxy;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.patchouli.api.IMultiblock;

public interface IProxy {
	default boolean isTheClientPlayer(LivingEntity entity) {
		return false;
	}

	default Player getClientPlayer() {
		return null;
	}

	default boolean isClientPlayerWearingMonocle() {
		return false;
	}

	default long getWorldElapsedTicks() {
		Object game = FabricLoader.getInstance().getGameInstance();
		if (game instanceof MinecraftServer) {
			return ((MinecraftServer) game).getLevel(Level.OVERWORLD).getGameTime();
		}
		return 0;
	}

	default void lightningFX(Vec3 vectorStart, Vec3 vectorEnd, float ticksPerMeter, int colorOuter, int colorInner) {
		lightningFX(vectorStart, vectorEnd, ticksPerMeter, System.nanoTime(), colorOuter, colorInner);
	}

	default void lightningFX(Vec3 vectorStart, Vec3 vectorEnd, float ticksPerMeter, long seed, int colorOuter, int colorInner) {

	}

	default void addBoss(EntityDoppleganger boss) {

	}

	default void removeBoss(EntityDoppleganger boss) {

	}

	default int getClientRenderDistance() {
		return 0;
	}

	/** Side-safe version of world.addParticle with the unlimited distance flag, ignoring reduced particle settings. */
	default void addParticleForce(Level world, ParticleOptions particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {}

	/** A version of {@link IProxy#addParticleForce} that culls particles below 32 block distances. */
	default void addParticleForceNear(Level world, ParticleOptions particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {}

	default void showMultiblock(IMultiblock mb, Component name, BlockPos anchor, Rotation rot) {}

	default void clearSextantMultiblock() {}
}
