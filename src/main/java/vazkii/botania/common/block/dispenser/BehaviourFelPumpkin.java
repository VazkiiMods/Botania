package vazkii.botania.common.block.dispenser;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.common.block.ModBlocks;

// Taken from vanilla pumpkin dispense behaviour
public class BehaviourFelPumpkin extends BehaviorDefaultDispenseItem {
    private boolean field_179241_b = true;

    @Override
    protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
    {
        World world = source.getWorld();
        BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
        Block blockpumpkin = ModBlocks.felPumpkin;

        if (world.isAirBlock(blockpos) && world.getBlockState(blockpos.down()).getBlock() == Blocks.IRON_BARS
                && world.getBlockState(blockpos.down(2)).getBlock() == Blocks.IRON_BARS) // Botania - Check for iron bars
        {
            if (!world.isRemote)
            {
                world.setBlockState(blockpos, blockpumpkin.getDefaultState(), 3);
            }

            --stack.stackSize;
        }
        else
        {
            this.field_179241_b = false;
        }

        return stack;
    }

    @Override
    protected void playDispenseSound(IBlockSource source)
    {
        if (this.field_179241_b)
        {
            source.getWorld().playEvent(1000, source.getBlockPos(), 0);
        }
        else
        {
            source.getWorld().playEvent(1001, source.getBlockPos(), 0);
        }
    }
}
