package vazkii.botania.common.block.subtile.functional;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.Random;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = LibMisc.MOD_ID)
public class BergamuteEventHandler {

	private BergamuteEventHandler() {}

	private static final Random RAND = new Random();
	private static final float MULTIPLIER = 0.15F;

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onSoundEvent(PlaySoundEvent evt) {
		ISound sound = evt.getResultSound();

		if (sound != null && shouldSilence(sound)) {
			if(sound instanceof ITickableSound) {
				evt.setResultSound(new WrappedTickableSound((ITickableSound) sound, MULTIPLIER));
			} else {
				SubTileBergamute berg = SubTileBergamute.getBergamuteNearby(sound.getX(), sound.getY(), sound.getZ());

				if (berg != null) {
					evt.setResultSound(new WrappedSound(sound, MULTIPLIER));

					if (RAND.nextBoolean()) {
						Color color = TilePool.PARTICLE_COLOR;
						BotaniaAPI.internalHandler.sparkleFX(berg.getWorld(), berg.getPos().getX() + 0.3 + Math.random() * 0.5, berg.getPos().getY() + 0.5 + Math.random()  * 0.5, berg.getPos().getZ() + 0.3 + Math.random() * 0.5, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, (float) Math.random(), 5);
					}
				}
			}
		}
	}

	private static boolean shouldSilence(ISound sound) {
		return sound.getCategory() != SoundCategory.VOICE
				&& sound.getCategory() != SoundCategory.MUSIC
				&& sound.getClass().getName().startsWith("net.minecraft.client.audio");
	}

	@OnlyIn(Dist.CLIENT)
	private static class WrappedSound implements ISound {

		private final ISound compose;
		private final float volMult;
		private final boolean recheck;

		private WrappedSound(ISound toWrap, float volMult) {
			compose = toWrap;
			this.volMult = volMult;
			recheck = toWrap instanceof TickableSound;
		}

		@Override
		public float getVolume() {
			float mult = recheck && SubTileBergamute.getBergamuteNearby(getX(), getY(), getZ()) == null ? 1F : volMult;
			return compose.getVolume() * mult;
		}

		@Nonnull @Override public ResourceLocation getSoundLocation() { return compose.getSoundLocation(); }
		@Nullable @Override public SoundEventAccessor createAccessor(@Nonnull SoundHandler handler) { return compose.createAccessor(handler); }
		@Nonnull @Override public Sound getSound() { return compose.getSound(); }
		@Nonnull @Override public SoundCategory getCategory() { return compose.getCategory(); }
		@Override public boolean canRepeat() { return compose.canRepeat(); }
		@Override public boolean isGlobal() { return compose.isGlobal(); }
		@Override public int getRepeatDelay() { return compose.getRepeatDelay(); }
		@Override public float getPitch() { return compose.getPitch(); }
		@Override public float getX() { return compose.getX(); }
		@Override public float getY() { return compose.getY(); }
		@Override public float getZ() { return compose.getZ(); }
		@Nonnull @Override public AttenuationType getAttenuationType() { return compose.getAttenuationType(); }
	}

	@OnlyIn(Dist.CLIENT)
	private static class WrappedTickableSound extends WrappedSound implements ITickableSound {

		private final ITickableSound compose;

		private WrappedTickableSound(ITickableSound toWrap, float volMult) {
			super(toWrap, volMult);
			compose = toWrap;
		}

		@Override public boolean isDonePlaying() { return compose.isDonePlaying(); }
		@Override public void tick() { compose.tick(); }
	}
}
