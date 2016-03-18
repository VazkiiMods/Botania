package vazkii.botania.common.core.helper;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import vazkii.botania.api.corporea.InvWithLocation;

import java.util.Iterator;

public final class InventoryHelper2 {

    public static InvWithLocation getInventoryWithLocation(World world, BlockPos pos, EnumFacing side) {
        IItemHandler ret = getInventory(world, pos, side);
        if(ret == null)
            return null;
        else return new InvWithLocation(ret, world, pos);
    }

    public static IItemHandler getInventory(World world, BlockPos pos, EnumFacing side) {
        TileEntity te = world.getTileEntity(pos);

        if(te == null)
            return null;
        IItemHandler ret = te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side) ?
                te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side) : null;
        if(ret == null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
            ret = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if(te instanceof TileEntityChest) {
            Block chestBlock = world.getBlockState(pos).getBlock();
            if(world.getBlockState(pos.west()).getBlock() == chestBlock)
                ret = new InvWrapper(new InventoryLargeChest("Large chest", (ILockableContainer) world.getTileEntity(pos.west()), (ILockableContainer) te));
            if(world.getBlockState(pos.east()).getBlock() == chestBlock)
                ret = new InvWrapper(new InventoryLargeChest("Large chest", (ILockableContainer) te, (ILockableContainer) world.getTileEntity(pos.east())));
            if(world.getBlockState(pos.north()).getBlock() == chestBlock)
                ret = new InvWrapper(new InventoryLargeChest("Large chest", (ILockableContainer) world.getTileEntity(pos.north()), (ILockableContainer) te));
            if(world.getBlockState(pos.south()).getBlock() == chestBlock)
                ret = new InvWrapper(new InventoryLargeChest("Large chest", (ILockableContainer) te, (ILockableContainer) world.getTileEntity(pos.south())));
        }

        return ret;
    }

    public static Iterable<ItemStack> toIterable(IItemHandler itemHandler) {
        return () -> new Iterator<ItemStack>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < itemHandler.getSlots();
            }

            @Override
            public ItemStack next() {
                return itemHandler.getStackInSlot(index++);
            }
        };
    }
    
    private InventoryHelper2() {}
    
}
