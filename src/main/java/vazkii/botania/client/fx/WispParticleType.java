package vazkii.botania.client.fx;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.particles.ParticleType;

public class WispParticleType extends ParticleType<ParticleData> {
    public WispParticleType() {
        super(false, ParticleData.DESERIALIZER);
    }

    public static IParticleFactory<ParticleData> FACTORY =
            (data, world, x, y, z, xSpeed, ySpeed, zSpeed) ->
                    new FXWisp(world, x, y, z, xSpeed, ySpeed, zSpeed, data.size, data.r, data.g, data.b, data.depthTest, data.maxAgeMul);
}
