package vazkii.botania.common.block.decor.stairs.prismarine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.PrismarineVariant;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.stairs.BlockLivingStairs;
import vazkii.botania.common.lexicon.LexiconData;

public class BlockPrismarineStairs extends BlockLivingStairs {

	public BlockPrismarineStairs() {
		this(PrismarineVariant.PRISMARINE);
	}

	public BlockPrismarineStairs(PrismarineVariant variant) {
		super(ModBlocks.prismarine.getDefaultState().withProperty(BotaniaStateProps.PRISMARINE_VARIANT, variant));
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.prismarine;
	}

}
