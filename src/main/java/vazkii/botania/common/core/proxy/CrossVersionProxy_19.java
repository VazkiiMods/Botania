package vazkii.botania.common.core.proxy;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.client.core.handler.BotaniaPlayerController;
import vazkii.botania.common.Botania;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CrossVersionProxy_19 implements ICrossVersionProxy {
    @Override
    public TileEntity createTeFromCompound(World world, NBTTagCompound compound) {
        Method m = ReflectionHelper.findMethod(TileEntity.class, null, new String[] { "create", "func_189514_c", "c" }, NBTTagCompound.class);
        try {
            return (TileEntity) m.invoke(null, compound);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Botania.LOGGER.error("Couldn't create TE in 1.9 crossversion proxy");
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void copyGameType(PlayerControllerMP from, BotaniaPlayerController to) {
        try {
            Class<?> gameTypeClazz = Class.forName("net.minecraft.world.WorldSettings$GameType");
            Method m = ReflectionHelper.findMethod(PlayerControllerMP.class, null, new String[]{ "getCurrentGameType", "func_178889_l", "l" });
            Object gameType = m.invoke(from);
            m = ReflectionHelper.findMethod(PlayerControllerMP.class, null, new String[]{ "setGameType", "func_78746_a", "a" }, gameTypeClazz);
            m.invoke(to, gameType);
        } catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException ignored) {
            Botania.LOGGER.error("Couldn't copy game type in 1.9 crossversion proxy");
        }
    }

    @Override
    public int getSkeletonTypeInt(EntitySkeleton skeleton) {
        try {
            Method m = ReflectionHelper.findMethod(EntitySkeleton.class, null, new String[]{ "getSkeletonType", "func_82202_m", "db" });
            return (int) m.invoke(skeleton);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Botania.LOGGER.error("Couldn't get skeleton type in 1.9 crossversion proxy");
            return 0;
        }
    }

    @Override
    public void setSkeletonTypeInt(EntitySkeleton skeleton, int type) {
        try {
            Method m = ReflectionHelper.findMethod(EntitySkeleton.class, null, new String[]{ "setSkeletonType", "func_82201_a", "a" }, int.class);
            m.invoke(skeleton, type);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Botania.LOGGER.error("Couldn't set skeleton type in 1.9 crossversion proxy");
        }
    }

    @Override
    public boolean isSurvivalOrAdventure(EntityPlayerMP playerMP) {
        try {
            Class gameTypeClazz = Class.forName("net.minecraft.world.WorldSettings$GameType");
            Method m = ReflectionHelper.findMethod(PlayerInteractionManager.class, null, new String[] { "getGameType", "func_73081_b", "b" });
            Object gameType = m.invoke(playerMP.interactionManager);
            m = ReflectionHelper.findMethod(gameTypeClazz, null, new String[] { "isSurvivalOrAdventure", "func_77144_e", "e" });
            return (Boolean) m.invoke(gameType);
        } catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException ignored) {
            Botania.LOGGER.error("Couldn't determine survival or adventure in 1.9 crossversion proxy");
            return true;
        }
    }
}
