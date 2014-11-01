package buildcraft.api.core;

import net.minecraft.block.Block;
import net.minecraftforge.oredict.OreDictionary;

public class BlockMetaPair implements Comparable<BlockMetaPair> {
	private int id, meta;
	
	public BlockMetaPair(Block block, int meta) {
		this.id = Block.getIdFromBlock(block);
		this.meta = meta;
	}
	
	public Block getBlock() {
		return Block.getBlockById(id);
	}
	
	public int meta() {
		return meta;
	}
	
	@Override
	public int hashCode() {
		return 17 * meta + id;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof BlockMetaPair)) {
			return false;
		}
		
		return ((BlockMetaPair) other).id == id && ((BlockMetaPair) other).meta == meta;
	}
	
	@Override
	public int compareTo(BlockMetaPair arg) {
		if (arg.id != id) {
			return (id - arg.id) * 16;
		} else {
			return meta - arg.meta;
		}
	}
	
	@Override
	public String toString() {
		if (this.getBlock() == null) {
			return "invalid";
		}
		
		return Block.blockRegistry.getNameForObject(this.getBlock()) + ":" + (this.meta == OreDictionary.WILDCARD_VALUE ? "*" : this.meta);
	}
}
