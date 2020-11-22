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
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.patchouli.api.IMultiblock;

import java.util.function.Supplier;

public interface IProxy {
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
		Object game = FabricLoader.getInstance().getGameInstance();
		if (game instanceof MinecraftServer) {
			return ((MinecraftServer) game).getWorld(World.OVERWORLD).getTime();
		}
		return 0;
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

	/** Side-safe version of world.addParticle with the unlimited distance flag, ignoring reduced particle settings. */
	default void addParticleForce(World world, ParticleEffect particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {}

	/** A version of {@link IProxy#addParticleForce} that culls particles below 32 block distances. */
	default void addParticleForceNear(World world, ParticleEffect particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {}

	default void showMultiblock(IMultiblock mb, Text name, BlockPos anchor, BlockRotation rot) {}

	default void clearSextantMultiblock() {}

	default void runOnClient(Supplier<Runnable> thing) {}
}
