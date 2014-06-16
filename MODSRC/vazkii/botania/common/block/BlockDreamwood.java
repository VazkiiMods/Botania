package vazkii.botania.common.block;

import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;
import vazkii.botania.common.item.block.ItemBlockDreamwood;
import vazkii.botania.common.item.block.ItemBlockPool;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockDreamwood extends BlockLivingwood {

	public BlockDreamwood() {
		super(LibBlockNames.DREAM_WOOD);
	}

	@Override
	void register(String name) {
		GameRegistry.registerBlock(this, ItemBlockDreamwood.class, name);
	}
}
