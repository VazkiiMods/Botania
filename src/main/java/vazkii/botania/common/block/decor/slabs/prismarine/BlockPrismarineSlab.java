package vazkii.botania.common.block.decor.slabs.prismarine;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.PrismarineVariant;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.decor.slabs.BlockLivingSlab;
import vazkii.botania.common.lexicon.LexiconData;

public class BlockPrismarineSlab extends BlockLivingSlab {

	public BlockPrismarineSlab(boolean full) {
		this(full, PrismarineVariant.PRISMARINE);
	}

	public BlockPrismarineSlab(boolean full, PrismarineVariant variant) {
		super(full, ModBlocks.prismarine.getDefaultState().withProperty(BotaniaStateProps.PRISMARINE_VARIANT, variant));
		setHardness(2.0F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
	}

	@Override
	public BlockSlab getFullBlock() {
		return (BlockSlab) ModFluffBlocks.prismarineSlabFull;
	}

	@Override
	public BlockSlab getSingleBlock() {
		return (BlockSlab) ModFluffBlocks.prismarineSlab;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.prismarine;
	}

}
