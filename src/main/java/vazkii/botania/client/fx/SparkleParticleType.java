package vazkii.botania.client.fx;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.particles.ParticleType;

public class SparkleParticleType extends ParticleType<SparkleParticleData> {
    public SparkleParticleType() {
        super(false, SparkleParticleData.DESERIALIZER);
    }

    public static IParticleFactory<SparkleParticleData> FACTORY =
            (data, world, x, y, z, xSpeed, ySpeed, zSpeed)
                    -> new FXSparkle(world, x, y, z, data.size, data.r, data.g, data.b, data.m, data.fake, data.noClip, data.corrupt);
}
