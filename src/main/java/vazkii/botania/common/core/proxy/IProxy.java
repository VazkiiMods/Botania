package vazkii.botania.common.core.proxy;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.world.World;
import vazkii.botania.api.boss.IBotaniaBoss;
import vazkii.botania.common.core.helper.Vector3;

public interface IProxy {
	default void registerHandlers() {}

	boolean isTheClientPlayer(LivingEntity entity);

	PlayerEntity getClientPlayer();

	boolean isClientPlayerWearingMonocle();

	String getLastVersion();

	long getWorldElapsedTicks();

	default void lightningFX(Vector3 vectorStart, Vector3 vectorEnd, float ticksPerMeter, int colorOuter, int colorInner) {
		lightningFX(vectorStart, vectorEnd, ticksPerMeter, System.nanoTime(), colorOuter, colorInner);
	}

	void lightningFX(Vector3 vectorStart, Vector3 vectorEnd, float ticksPerMeter, long seed, int colorOuter, int colorInner);

	void addBoss(IBotaniaBoss boss);

	void removeBoss(IBotaniaBoss boss);

	int getClientRenderDistance();

	Object getEmptyModelBiped();

	// Side-safe version of world.addParticle with noDistanceLimit flag set to true
	default void addParticleForce(World world, IParticleData particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {}
}
