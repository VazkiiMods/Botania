package vazkii.botania.common.block;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

import vazkii.botania.xplat.XplatAbstractions;

public class BotaniaBlockSetTypes {
	public static final BlockSetType LIVINGWOOD_BLOCK_SET = XplatAbstractions.INSTANCE.registerWoodBlockSetType("livingwood");
	public static final BlockSetType DREAMWOOD_BLOCK_SET = XplatAbstractions.INSTANCE.registerWoodBlockSetType("dreamwood");

	public static final WoodType LIVINGWOOD = XplatAbstractions.INSTANCE.registerWoodType("livingwood", LIVINGWOOD_BLOCK_SET);
	public static final WoodType DREAMWOOD = XplatAbstractions.INSTANCE.registerWoodType("dreamwood", DREAMWOOD_BLOCK_SET);
}
