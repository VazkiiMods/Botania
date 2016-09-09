package vazkii.botania.client.core.handler;

import com.google.common.base.Throwables;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibObfuscation;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class ClientMethodHandles {

    public static final MethodHandle
            renderPosX_getter, renderPosY_getter, renderPosZ_getter, // RenderManager
            starGLCallList_getter, starVBO_getter, glSkyList_getter, skyVBO_getter; // RenderGlobal

    static {
        try {
            Field f = ReflectionHelper.findField(RenderManager.class, LibObfuscation.RENDERPOSX);
            f.setAccessible(true);
            renderPosX_getter = MethodHandles.publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(RenderManager.class, LibObfuscation.RENDERPOSY);
            f.setAccessible(true);
            renderPosY_getter = MethodHandles.publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(RenderManager.class, LibObfuscation.RENDERPOSZ);
            f.setAccessible(true);
            renderPosZ_getter = MethodHandles.publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(RenderGlobal.class, LibObfuscation.STAR_GL_CALL_LIST);
            f.setAccessible(true);
            starGLCallList_getter = MethodHandles.publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(RenderGlobal.class, LibObfuscation.STAR_VBO);
            f.setAccessible(true);
            starVBO_getter = MethodHandles.publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(RenderGlobal.class, LibObfuscation.GL_SKY_LIST);
            f.setAccessible(true);
            glSkyList_getter = MethodHandles.publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(RenderGlobal.class, LibObfuscation.SKY_VBO);
            f.setAccessible(true);
            skyVBO_getter = MethodHandles.publicLookup().unreflectGetter(f);
        } catch (IllegalAccessException e) {
            Botania.LOGGER.fatal("Couldn't initialize client methodhandles! Things will be broken!");
            e.printStackTrace();
            throw Throwables.propagate(e);
        }
    }

    private ClientMethodHandles() {}
}
