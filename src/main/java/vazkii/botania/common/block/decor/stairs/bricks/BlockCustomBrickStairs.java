package vazkii.botania.common.block.decor.stairs.bricks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.stairs.BlockLivingStairs;
import vazkii.botania.common.lexicon.LexiconData;

public class BlockCustomBrickStairs extends BlockLivingStairs {

	public BlockCustomBrickStairs() {
		this(0);
	}

	public BlockCustomBrickStairs(int meta) {
		super(ModBlocks.customBrick, meta);
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z,	EntityPlayer player, ItemStack lexicon) {
		return LexiconData.decorativeBlocks;
	}

}
