package vazkii.botania.client.integration.albedo;

import elucent.albedo.event.GatherLightsEvent;
import elucent.albedo.lighting.ILightProvider;
import elucent.albedo.lighting.Light;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.common.block.tile.TileLightRelay;
import vazkii.botania.common.entity.EntityBabylonWeapon;
import vazkii.botania.common.entity.EntityFallingStar;
import vazkii.botania.common.entity.EntityFlameRing;
import vazkii.botania.common.entity.EntityMagicLandmine;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.entity.EntityPixie;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Color;
import java.util.function.Function;

public class AlbedoCompat {
	private static ResourceLocation CAP_NAME = new ResourceLocation(LibMisc.MOD_ID, "albedo_light");

	@CapabilityInject(ILightProvider.class)
	private static final Capability<ILightProvider> LIGHT_CAPABILITY = null;

	@SubscribeEvent
	public static void attachLights(AttachCapabilitiesEvent<Entity> event) {
		Entity e = event.getObject();
		if (e instanceof EntityFlameRing) {
			addLight(event, (entity) -> Light.builder().pos(entity).color(1F, 0.5F, 0F).radius(20).build());
		} else if (e instanceof EntityPixie) {
			addLight(event, (EntityPixie pixie) -> pixie.getType() == 1 ? null : Light.builder().pos(pixie).color(1F, 0F, 0.5F).radius(8).build());
		} else if (e instanceof EntityManaBurst) {
			addLight(event, (EntityManaBurst burst) -> {
				int color = burst.getColor();
				return Light.builder()
						.pos(burst.posX - burst.motionX, burst.posY - burst.motionY, burst.posZ - burst.motionZ)
						.color(color, false)
						.radius(burst.getParticleSize() * 8)
						.build();
			});
		} else if (e instanceof TileLightRelay.EntityPlayerMover) {
			addLight(event, (TileLightRelay.EntityPlayerMover mover) -> {
				if (mover.getPassengers().isEmpty())
					return null;
				Entity passenger = mover.getPassengers().get(0);
				Color color = Color.getHSBColor(passenger.ticksExisted / 36F, 1F, 1F);
				return Light.builder().pos(mover).color(color.getRGB(), false).radius(5).build();
			});
		} else if (e instanceof EntityFallingStar) {
			addLight(event, entity -> Light.builder().pos(entity).color(1F, 0F, 1F).radius(12).build());
		} else if (e instanceof EntityBabylonWeapon) {
			addLight(event, entity -> Light.builder().pos(entity).color(1F, 1F, 0F).radius(8).build());
		} else if (e instanceof EntityMagicLandmine) {
			addLight(event, entity -> Light.builder().pos(entity).color(0.6F, 0F, 1F).radius(15).build());
		}
	}

	private static <T extends Entity> void addLight(AttachCapabilitiesEvent<Entity> event, Function<T, Light> lightFunction) {
		event.addCapability(CAP_NAME, new BotaniaLightProvider<>(lightFunction));
	}

	private static class BotaniaLightProvider<E extends Entity> implements ILightProvider, ICapabilityProvider {
		private Function<E, Light> lightFunction;

		BotaniaLightProvider(Function<E, Light> light) {
			this.lightFunction = light;
		}

		@Override
		public void gatherLights(GatherLightsEvent event, Entity context) {
			@SuppressWarnings("unchecked")
			Light light = lightFunction.apply((E) context);
			if(light != null)
				event.add(light);
		}

		@Override
		public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
			return capability == LIGHT_CAPABILITY;
		}

		@Nullable
		@Override
		public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
			return capability == LIGHT_CAPABILITY ? LIGHT_CAPABILITY.cast(this) : null;
		}
	}
}
