package vazkii.botania.common.core.proxy;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.client.core.handler.BotaniaPlayerController;

// horrible hax to try to run 1.9/10 from the same jar :D
// Taking this as an example is heavily discouraged
public interface ICrossVersionProxy {

    TileEntity createTeFromCompound(World world, NBTTagCompound compound);

    @SideOnly(Side.CLIENT)
    void copyGameType(PlayerControllerMP from, BotaniaPlayerController to);

    int getSkeletonTypeInt(EntitySkeleton skeleton);

    void setSkeletonTypeInt(EntitySkeleton skeleton, int type);

}
