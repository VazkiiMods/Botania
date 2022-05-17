/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.proxy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.xplat.IXplatAbstractions;
import vazkii.patchouli.api.IMultiblock;

import javax.annotation.Nullable;

import java.util.function.Supplier;

public interface IProxy {
	IProxy INSTANCE = make();

	private static IProxy make() {
		if (IXplatAbstractions.INSTANCE.isPhysicalClient()) {
			return new ClientProxy();
		} else {
			return new IProxy() {};
		}
	}

	default void runOnClient(Supplier<Runnable> s) {}

	@Nullable
	default Player getClientPlayer() {
		return null;
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

	/** Same as {@code world.addParticle(data, true, ...)}, but culls particles below 32 block distances. */
	default void addParticleForceNear(Level world, ParticleOptions particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {}

	default void showMultiblock(IMultiblock mb, Component name, BlockPos anchor, Rotation rot) {}

	default void clearSextantMultiblock() {}
}
