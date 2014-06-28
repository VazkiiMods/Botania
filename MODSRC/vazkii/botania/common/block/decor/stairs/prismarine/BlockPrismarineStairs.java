package vazkii.botania.common.block.decor.stairs.prismarine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.stairs.BlockLivingStairs;
import vazkii.botania.common.lexicon.LexiconData;

public class BlockPrismarineStairs extends BlockLivingStairs {

	public BlockPrismarineStairs() {
		this(0);
	}

	public BlockPrismarineStairs(int meta) {
		super(ModBlocks.prismarine, meta);
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z,	EntityPlayer player, ItemStack lexicon) {
		return LexiconData.prismarine;
	}

}
