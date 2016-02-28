package vazkii.botania.common.core.helper;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public final class InventoryHelper2 {
    
    public static IItemHandler getInventory(World world, BlockPos pos, EnumFacing side) {
        TileEntity te = world.getTileEntity(pos);

        if(te == null)
            return null;

        if(te instanceof TileEntityChest) {
            Block chestBlock = world.getBlockState(pos).getBlock();
            if(world.getBlockState(pos.west()).getBlock() == chestBlock)
                return new InvWrapper(new InventoryLargeChest("Large chest", (ILockableContainer) world.getTileEntity(pos.west()), (ILockableContainer) te));
            if(world.getBlockState(pos.east()).getBlock() == chestBlock)
                return new InvWrapper(new InventoryLargeChest("Large chest", (ILockableContainer) te, (ILockableContainer) world.getTileEntity(pos.east())));
            if(world.getBlockState(pos.north()).getBlock() == chestBlock)
                return new InvWrapper(new InventoryLargeChest("Large chest", (ILockableContainer) world.getTileEntity(pos.north()), (ILockableContainer) te));
            if(world.getBlockState(pos.south()).getBlock() == chestBlock)
                return new InvWrapper(new InventoryLargeChest("Large chest", (ILockableContainer) te, (ILockableContainer) world.getTileEntity(pos.south())));
        }
        return te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side) ?
                te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side) : null;
    }
    
    private InventoryHelper2() {}
    
}
