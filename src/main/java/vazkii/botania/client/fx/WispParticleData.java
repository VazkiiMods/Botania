package vazkii.botania.client.fx;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

import javax.annotation.Nonnull;
import java.util.Locale;

public class WispParticleData implements IParticleData {
    public final float size;
    public final float r, g, b;
    public final float maxAgeMul;
    public final boolean depthTest;

    public static WispParticleData wisp(float size, float r, float g, float b) {
        return wisp(size, r, g, b, 1);
    }

    public static WispParticleData wisp(float size, float r, float g, float b, float maxAgeMul) {
        return wisp(size, r, g, b, maxAgeMul, true);
    }

    public static WispParticleData wisp(float size, float r, float g, float b, boolean depth) {
        return wisp(size, r, g, b, 1, depth);
    }

    public static WispParticleData wisp(float size, float r, float g, float b, float maxAgeMul, boolean depthTest) {
        return new WispParticleData(size, r, g, b, maxAgeMul, depthTest);
    }

    private WispParticleData(float size, float r, float g, float b, float maxAgeMul, boolean depthTest) {
        this.size = size;
        this.r = r;
        this.g = g;
        this.b = b;
        this.maxAgeMul = maxAgeMul;
        this.depthTest = depthTest;
    }


    @Nonnull
    @Override
    public ParticleType<WispParticleData> getType() {
        return ModParticles.WISP;
    }

    @Override
    public void write(PacketBuffer buf) {
        buf.writeFloat(size);
        buf.writeFloat(r);
        buf.writeFloat(g);
        buf.writeFloat(b);
        buf.writeFloat(maxAgeMul);
        buf.writeBoolean(depthTest);
    }

    @Nonnull
    @Override
    public String getParameters() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %s",
                this.getType().getRegistryName(), this.size, this.r, this.g, this.b, this.maxAgeMul, this.depthTest);
    }

    public static final IDeserializer<WispParticleData> DESERIALIZER = new IDeserializer<WispParticleData>() {
        @Nonnull
        @Override
        public WispParticleData deserialize(@Nonnull ParticleType<WispParticleData> type, @Nonnull StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float size = reader.readFloat();
            reader.expect(' ');
            float r = reader.readFloat();
            reader.expect(' ');
            float g = reader.readFloat();
            reader.expect(' ');
            float b = reader.readFloat();
            reader.expect(' ');
            float mam = reader.readFloat();
            boolean depth = true;
            if (reader.canRead()) {
                reader.expect(' ');
                depth = reader.readBoolean();
            }
            return new WispParticleData(size, r, g, b, mam, depth);
        }

        @Override
        public WispParticleData read(@Nonnull ParticleType<WispParticleData> type, PacketBuffer buf) {
            return new WispParticleData(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readBoolean());
        }
    };
}
