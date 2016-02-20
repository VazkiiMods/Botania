package vazkii.botania.common.core.handler;

import com.google.common.base.Throwables;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import vazkii.botania.common.lib.LibObfuscation;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

public final class BotaniaMethodHandles {

    public static final MethodHandle
        GETRENDERPOSX, GETRENDERPOSY, GETRENDERPOSZ,
        GETITEMAGE, SETITEMAGE,
        GETPICKUPDELAY; // Already has setter in vanilla

    static {
        try {
            Field f = ReflectionHelper.findField(RenderManager.class, LibObfuscation.RENDERPOSX);
            f.setAccessible(true);
            GETRENDERPOSX = MethodHandles.publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(RenderManager.class, LibObfuscation.RENDERPOSY);
            f.setAccessible(true);
            GETRENDERPOSY = MethodHandles.publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(RenderManager.class, LibObfuscation.RENDERPOSZ);
            f.setAccessible(true);
            GETRENDERPOSZ = MethodHandles.publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(EntityItem.class, LibObfuscation.AGE);
            f.setAccessible(true);
            GETITEMAGE = MethodHandles.publicLookup().unreflectGetter(f);
            SETITEMAGE = MethodHandles.publicLookup().unreflectSetter(f);

            f = ReflectionHelper.findField(EntityItem.class, LibObfuscation.PICKUP_DELAY);
            f.setAccessible(true);
            GETPICKUPDELAY = MethodHandles.publicLookup().unreflectGetter(f);
        } catch (Throwable t) {
            throw Throwables.propagate(t);
        }
    }

    private BotaniaMethodHandles() {}
}
