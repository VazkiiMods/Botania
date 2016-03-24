package vazkii.botania.common.core.handler;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import vazkii.botania.common.lib.LibObfuscation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ModSounds {

    public static void init() {
        try {
            Method m = ReflectionHelper.findMethod(SoundEvent.class, null, LibObfuscation.REGISTER_SOUND, String.class);
            m.invoke(null, "botania:airRod");
            m.invoke(null, "botania:agricarnation");
            m.invoke(null, "botania:altarCraft");
            m.invoke(null, "botania:babylonAttack");
            m.invoke(null, "botania:babylonSpawn");
            m.invoke(null, "botania:bellows");
            m.invoke(null, "botania:bifrostRod");
            m.invoke(null, "botania:blackLotus");
            m.invoke(null, "botania:dash");
            m.invoke(null, "botania:ding");
            m.invoke(null, "botania:divaCharm");
            m.invoke(null, "botania:divinationRod");
            m.invoke(null, "botania:doit");
            m.invoke(null, "botania:enchanterBlock");
            m.invoke(null, "botania:enchanterEnchant");
            m.invoke(null, "botania:endoflame");
            m.invoke(null, "botania:equipBauble");
            m.invoke(null, "botania:gaiaTrap");
            m.invoke(null, "botania:goldenLaurel");
            m.invoke(null, "botania:holyCloak");
            m.invoke(null, "botania:laputaStart");
            m.invoke(null, "botania:lexiconOpen");
            m.invoke(null, "botania:lexiconPage");
            m.invoke(null, "botania:lightRelay");
            m.invoke(null, "botania:manaBlaster");
            m.invoke(null, "botania:manaPoolCraft");
            m.invoke(null, "botania:missile");
            m.invoke(null, "botania:orechid");
            m.invoke(null, "botania:potionCreate");
            m.invoke(null, "botania:runeAltarCraft");
            m.invoke(null, "botania:runeAltarStart");
            m.invoke(null, "botania:spreaderFire");
            m.invoke(null, "botania:starcaller");
            m.invoke(null, "botania:terraBlade");
            m.invoke(null, "botania:terraformRod");
            m.invoke(null, "botania:terraPickMode");
            m.invoke(null, "botania:terrasteelCraft");
            m.invoke(null, "botania:thermalily");
            m.invoke(null, "botania:unholyCloak");
            m.invoke(null, "botania:way");
            m.invoke(null, "botania:music.gaia1");
            m.invoke(null, "botania:music.gaia2");
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private ModSounds() {}

}
