package vazkii.botania.client.fx;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.particles.ParticleType;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class WispParticleType extends ParticleType<WispParticleData> {
    public WispParticleType() {
        super(false, WispParticleData.DESERIALIZER);
    }

    public static class Factory implements IParticleFactory<WispParticleData> {
        @Override
        public Particle makeParticle(WispParticleData data, World world, double x, double y, double z, double mx, double my, double mz) {
            return new FXWisp(world, x, y, z, mx, my, mz, data.size, data.r, data.g, data.b, data.depthTest, data.maxAgeMul);
        }
    }
}
