package vazkii.botania.common.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.item.block.ItemBlockDreamwood;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockDreamwood extends BlockLivingwood {

	public BlockDreamwood() {
		super(LibBlockNames.DREAM_WOOD);
	}

	@Override
	void register(String name) {
		GameRegistry.registerBlock(this, ItemBlockDreamwood.class, name);
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.elvenResources;
	}
}
