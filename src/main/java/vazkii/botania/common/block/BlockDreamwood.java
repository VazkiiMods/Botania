package vazkii.botania.common.block;

import vazkii.botania.common.item.block.ItemBlockDreamwood;
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
}
