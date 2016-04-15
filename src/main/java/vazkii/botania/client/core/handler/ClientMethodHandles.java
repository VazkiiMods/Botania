package vazkii.botania.client.core.handler;

import com.google.common.base.Throwables;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import vazkii.botania.common.lib.LibObfuscation;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class ClientMethodHandles {

    public static final MethodHandle
            renderPosX_getter, renderPosY_getter, renderPosZ_getter, // RenderManager
            farPlaneDistance_getter,
            hurtCameraEffect, setupViewBobbing, getFOVModifier,
            enableLightmap, disableLightmap; // EntityRenderer
            //prevEquippedProgress_getter, equippedProgress_getter; // ItemRenderer

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

            f = ReflectionHelper.findField(EntityRenderer.class, LibObfuscation.FAR_PLANE_DISTANCE);
            f.setAccessible(true);
            farPlaneDistance_getter = MethodHandles.publicLookup().unreflectGetter(f);

            Method m = ReflectionHelper.findMethod(EntityRenderer.class, null, LibObfuscation.HURT_CAMERA_EFFECT, float.class);
            m.setAccessible(true);
            hurtCameraEffect = MethodHandles.publicLookup().unreflect(m);

            m = ReflectionHelper.findMethod(EntityRenderer.class, null, LibObfuscation.SETUP_VIEW_BOBBING, float.class);
            m.setAccessible(true);
            setupViewBobbing = MethodHandles.publicLookup().unreflect(m);

            m = ReflectionHelper.findMethod(EntityRenderer.class, null, LibObfuscation.ENABLE_LIGHTMAP);
            m.setAccessible(true);
            enableLightmap = MethodHandles.publicLookup().unreflect(m);

            m = ReflectionHelper.findMethod(EntityRenderer.class, null, LibObfuscation.DISABLE_LIGHTMAP);
            m.setAccessible(true);
            disableLightmap = MethodHandles.publicLookup().unreflect(m);

            m = ReflectionHelper.findMethod(EntityRenderer.class, null, LibObfuscation.GET_FOV_MODIFIER, float.class, boolean.class);
            m.setAccessible(true);
            getFOVModifier = MethodHandles.publicLookup().unreflect(m);

//            f = ReflectionHelper.findField(ItemRenderer.class, LibObfuscation.PREV_EQUIPPED_PROGRESS);
//            f.setAccessible(true);
//            prevEquippedProgress_getter = MethodHandles.publicLookup().unreflectGetter(f);
//
//            f = ReflectionHelper.findField(ItemRenderer.class, LibObfuscation.EQUIPPED_PROGRESS);
//            f.setAccessible(true);
//            equippedProgress_getter = MethodHandles.publicLookup().unreflectGetter(f);
        } catch (IllegalAccessException e) {
            FMLLog.severe("[Botania]: Couldn't initialize client methodhandles! Things will be broken!");
            e.printStackTrace();
            throw Throwables.propagate(e);
        }
    }

    private ClientMethodHandles() {}
}
