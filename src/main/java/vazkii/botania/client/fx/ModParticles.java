package vazkii.botania.client.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.common.lib.LibMisc;

import static vazkii.botania.common.block.ModBlocks.register;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModParticles {
    @ObjectHolder(LibMisc.MOD_ID + ":wisp")
    public static ParticleType<WispParticleData> WISP;

    @ObjectHolder(LibMisc.MOD_ID + ":sparkle")
    public static ParticleType<SparkleParticleData> SPARKLE;

    @SubscribeEvent
    public static void registerParticles(RegistryEvent.Register<ParticleType<?>> evt) {
        register(evt.getRegistry(), new WispParticleType(), "wisp");
        register(evt.getRegistry(), new SparkleParticleType(), "sparkle");
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class FactoryHandler {
        @SubscribeEvent
        public static void registerFactories(ParticleFactoryRegisterEvent evt) {
            Minecraft.getInstance().particles.registerFactory(ModParticles.WISP, new WispParticleType.Factory());
            Minecraft.getInstance().particles.registerFactory(ModParticles.SPARKLE, new SparkleParticleType.Factory());
        }
    }
}
