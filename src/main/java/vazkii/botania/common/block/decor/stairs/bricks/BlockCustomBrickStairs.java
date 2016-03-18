package vazkii.botania.common.block.decor.stairs.bricks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.CustomBrickVariant;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.stairs.BlockLivingStairs;
import vazkii.botania.common.lexicon.LexiconData;

public class BlockCustomBrickStairs extends BlockLivingStairs {

	public BlockCustomBrickStairs() {
		this(CustomBrickVariant.HELLISH_BRICK);
	}

	public BlockCustomBrickStairs(CustomBrickVariant variant) {
		super(ModBlocks.customBrick.getDefaultState().withProperty(BotaniaStateProps.CUSTOMBRICK_VARIANT, variant));
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.decorativeBlocks;
	}

}
