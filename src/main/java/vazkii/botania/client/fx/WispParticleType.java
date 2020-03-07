package vazkii.botania.client.fx;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.particles.ParticleType;
import net.minecraft.world.World;

public class WispParticleType extends ParticleType<WispParticleData> {
    public WispParticleType() {
        super(false, WispParticleData.DESERIALIZER);
    }

    public static class Factory implements IParticleFactory<WispParticleData> {
        private final IAnimatedSprite sprite;

        public Factory(IAnimatedSprite sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle makeParticle(WispParticleData data, World world, double x, double y, double z, double mx, double my, double mz) {
            FXWisp ret = new FXWisp(world, x, y, z, mx, my, mz, data.size, data.r, data.g, data.b, data.depthTest, data.maxAgeMul, data.noClip);
            ret.selectSpriteRandomly(sprite);
            return ret;
        }
    }
}
