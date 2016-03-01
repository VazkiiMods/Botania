package vazkii.botania.api.brew;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.registry.GameData;

// Version of PotionEffect that doesn't use number ID's
// See https://github.com/MinecraftForge/MinecraftForge/issues/2374
// todo 1.9 remove?
public class PotionEffectShim {

    public final Potion potion;
    public final int duration;
    public final int amplifier;

    public PotionEffectShim(Potion potion, int duration, int amplifier) {
        this.potion = potion;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    public PotionEffect toVanilla() {
        return new PotionEffect(GameData.getPotionRegistry().getId(potion), duration, amplifier, true, true);
    }

}
