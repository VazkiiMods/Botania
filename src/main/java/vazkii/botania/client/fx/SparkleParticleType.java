package vazkii.botania.client.fx;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.particles.ParticleType;
import net.minecraft.world.World;

public class SparkleParticleType extends ParticleType<SparkleParticleData> {
    public SparkleParticleType() {
        super(false, SparkleParticleData.DESERIALIZER);
    }

    public static class Factory implements IParticleFactory<SparkleParticleData> {
        @Override
        public Particle makeParticle(SparkleParticleData data, World world, double x, double y, double z, double mx, double my, double mz) {
            return new FXSparkle(world, x, y, z, data.size, data.r, data.g, data.b, data.m, data.fake, data.noClip, data.corrupt);
        }
    }
}
