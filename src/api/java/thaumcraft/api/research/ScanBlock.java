package thaumcraft.api.research;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

public class ScanBlock implements IScanThing {
	
	String research;	
	Block block;

	public ScanBlock(String research, Block block) {
		this.research = research;
		this.block = block;
	}
	

	@Override
	public boolean checkThing(EntityPlayer player, Object obj) {		
		if (obj!=null && obj instanceof BlockPos && player.worldObj.getBlockState((BlockPos) obj).getBlock()==block) {
				return true;
		}
		return false;
	}
	
	@Override
	public String getResearchKey() {
		return research;
	}
}
