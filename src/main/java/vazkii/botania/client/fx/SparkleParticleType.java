package vazkii.botania.client.fx;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.particles.ParticleType;

public class SparkleParticleType extends ParticleType<ParticleData> {
    public SparkleParticleType() {
        super(false, ParticleData.DESERIALIZER);
    }

    public static IParticleFactory<ParticleData> FACTORY =
            (data, world, x, y, z, xSpeed, ySpeed, zSpeed)
                    -> new FXSparkle(world, x, y, z, data.size, data.r, data.g, data.b, (int) data.maxAgeMul);
}
