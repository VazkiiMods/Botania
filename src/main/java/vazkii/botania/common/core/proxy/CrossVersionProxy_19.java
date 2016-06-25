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

public class CrossVersionProxy_19 implements ICrossVersionProxy {
    @Override
    public TileEntity createTeFromCompound(World world, NBTTagCompound compound) {
        Method m = ReflectionHelper.findMethod(TileEntity.class, null, new String[] { "create", "func_189514_c", "c" }, NBTTagCompound.class);
        try {
            return (TileEntity) m.invoke(null, compound);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void copyGameType(PlayerControllerMP from, BotaniaPlayerController to) {
        try {
            Class<?> gameTypeClazz = Class.forName("net.minecraft.world.WorldSettings.GameType");
            Object gameType = from.getCurrentGameType();
            Method m = ReflectionHelper.findMethod(PlayerControllerMP.class, null, new String[]{ "setGameType", "func_78746_a", "a" }, gameTypeClazz);
            m.invoke(to, gameType);
        } catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException ignored) {}
    }

    @Override
    public int getSkeletonTypeInt(EntitySkeleton skeleton) {
        try {
            Method m = ReflectionHelper.findMethod(EntitySkeleton.class, null, new String[]{ "getSkeletonType", "func_82202_m", "db" });
            return (int) m.invoke(skeleton);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return 0;
        }
    }

    @Override
    public void setSkeletonTypeInt(EntitySkeleton skeleton, int type) {
        try {
            Method m = ReflectionHelper.findMethod(EntitySkeleton.class, null, new String[]{ "setSkeletonType", "func_82201_a", "a" }, int.class);
            m.invoke(skeleton, type);
        } catch (IllegalAccessException | InvocationTargetException e) {}
    }
}
