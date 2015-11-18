package thaumcraft.api.research;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

public class ScanBlockState implements IScanThing {
	
	String research;	
	IBlockState blockState;

	public ScanBlockState(String research, IBlockState blockState) {
		this.research = research;
		this.blockState = blockState;
	}
	
	public ScanBlockState(String research, IBlockState blockState, boolean item) {
		this.research = research;
		this.blockState = blockState;
		if (item) 
			ScanningManager.addScannableThing(new ScanItem(research,
				new ItemStack(blockState.getBlock(),1,blockState.getBlock().getMetaFromState(blockState))));
	}

	@Override
	public boolean checkThing(EntityPlayer player, Object obj) {		
		if (obj!=null && obj instanceof BlockPos && player.worldObj.getBlockState((BlockPos) obj)==blockState) {
				return true;
		}
		return false;
	}
	
	@Override
	public String getResearchKey() {
		return research;
	}
}
