package vazkii.botania.common.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.item.block.ItemBlockDreamwood;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockDreamwood extends BlockLivingwood {

	public BlockDreamwood() {
		super(LibBlockNames.DREAM_WOOD);
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.elvenResources;
	}
}
