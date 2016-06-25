package vazkii.botania.common.core.proxy;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.client.core.handler.BotaniaPlayerController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CrossVersionProxy_110 implements ICrossVersionProxy {
    @Override
    public TileEntity createTeFromCompound(World world, NBTTagCompound compound) {
        Method m = ReflectionHelper.findMethod(TileEntity.class, null, new String[] { "func_190200_a", "a" }, World.class, NBTTagCompound.class);
        try {
            return (TileEntity) m.invoke(null, world, compound);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void copyGameType(PlayerControllerMP from, BotaniaPlayerController to) {
        try {
            Class<?> gameTypeClazz = Class.forName("net.minecraft.world.GameType");
            Method m = ReflectionHelper.findMethod(PlayerControllerMP.class, null, new String[]{ "getCurrentGameType", "func_178889_l", "l" });
            Object gameType = m.invoke(from);
            m = ReflectionHelper.findMethod(PlayerControllerMP.class, null, new String[]{ "setGameType", "func_78746_a", "a" }, gameTypeClazz);
            m.invoke(to, gameType);
        } catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException ignored) {}
    }

    @Override
    public int getSkeletonTypeInt(EntitySkeleton skeleton) {
        try {
            Method m = ReflectionHelper.findMethod(EntitySkeleton.class, null, new String[] { "func_189771_df", "df" });
            Enum e = (Enum) m.invoke(skeleton);
            return e.ordinal();
        } catch (IllegalAccessException | InvocationTargetException e1) {
            return 0;
        }
    }

    @Override
    public void setSkeletonTypeInt(EntitySkeleton skeleton, int type) {
        try {
            Class skeletonTypeClazz = Class.forName("net.minecraft.entity.monster.SkeletonType");
            Object typeToSet = skeletonTypeClazz.getEnumConstants()[type];
            Method m = ReflectionHelper.findMethod(EntitySkeleton.class, null, new String[] { "func_189768_a", "a" }, skeletonTypeClazz);
            m.invoke(skeleton, typeToSet);
        } catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {}
    }
}
