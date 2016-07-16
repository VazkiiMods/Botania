package vazkii.botania.common.block.subtile.functional;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.tile.mana.TilePool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Color;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class BergamuteEventHandler {
    private static final Random RAND = new Random();
    private static final float MULTIPLIER = 0.15F;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onSoundEvent(PlaySoundEvent evt) {
        ISound sound = evt.getResultSound();

        if (sound != null && shouldSilence(sound.getCategory())) {
            SubTileBergamute berg = SubTileBergamute.getBergamuteNearby(sound.getXPosF(), sound.getYPosF(), sound.getZPosF());

            if (berg != null) {
                evt.setResultSound(new WrappedSound(sound, MULTIPLIER));

                if (RAND.nextBoolean()) {
                    Color color = TilePool.PARTICLE_COLOR;
                    BotaniaAPI.internalHandler.sparkleFX(berg.getWorld(), berg.getPos().getX() + 0.3 + Math.random() * 0.5, berg.getPos().getY() + 0.5 + Math.random()  * 0.5, berg.getPos().getZ() + 0.3 + Math.random() * 0.5, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, (float) Math.random(), 5);
                }
            }
        }
    }

    private static boolean shouldSilence(SoundCategory category) {
        return category != SoundCategory.VOICE && category != SoundCategory.MUSIC;
    }

    @SideOnly(Side.CLIENT)
    private static class WrappedSound implements ISound {

        private final ISound compose;
        private final float volMult;

        private WrappedSound(ISound toWrap, float volMult) {
            this.compose = toWrap;
            this.volMult = volMult;
        }

        @Override
        public float getVolume() {
            return compose.getVolume() * volMult;
        }

        @Nonnull @Override public ResourceLocation getSoundLocation() { return compose.getSoundLocation(); }
        @Nullable @Override public SoundEventAccessor createAccessor(@Nonnull SoundHandler handler) { return compose.createAccessor(handler); }
        @Nonnull @Override public Sound getSound() { return compose.getSound(); }
        @Nonnull @Override public SoundCategory getCategory() { return compose.getCategory(); }
        @Override public boolean canRepeat() { return compose.canRepeat(); }
        @Override public int getRepeatDelay() { return compose.getRepeatDelay(); }
        @Override public float getPitch() { return compose.getPitch(); }
        @Override public float getXPosF() { return compose.getXPosF(); }
        @Override public float getYPosF() { return compose.getYPosF(); }
        @Override public float getZPosF() { return compose.getZPosF(); }
        @Nonnull @Override public AttenuationType getAttenuationType() { return compose.getAttenuationType(); }
    }
}
