package vazkii.botania.common.core.handler;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import vazkii.botania.api.ColorHelper;
import vazkii.botania.api.imc.IMC;
import vazkii.botania.api.imc.OreWeightMessage;
import vazkii.botania.api.imc.PaintableBlockMessage;
import vazkii.botania.common.lib.LibMisc;

public class IMCSender {
	public static void enqueue(InterModEnqueueEvent evt) {
		send(IMC.REGISTER_ORE_WEIGHT, new OreWeightMessage(Tags.Blocks.ORES_COAL.getId(), 46525));
		send(IMC.REGISTER_ORE_WEIGHT, new OreWeightMessage(Tags.Blocks.ORES_DIAMOND.getId(), 1265));
		send(IMC.REGISTER_ORE_WEIGHT, new OreWeightMessage(Tags.Blocks.ORES_EMERALD.getId(), 780));
		send(IMC.REGISTER_ORE_WEIGHT, new OreWeightMessage(Tags.Blocks.ORES_GOLD.getId(), 2970));
		send(IMC.REGISTER_ORE_WEIGHT, new OreWeightMessage(Tags.Blocks.ORES_IRON.getId(), 20665));
		send(IMC.REGISTER_ORE_WEIGHT, new OreWeightMessage(Tags.Blocks.ORES_LAPIS.getId(), 1285));
		send(IMC.REGISTER_ORE_WEIGHT, new OreWeightMessage(Tags.Blocks.ORES_REDSTONE.getId(), 6885));
		send(IMC.REGISTER_NETHER_ORE_WEIGHT, new OreWeightMessage(Tags.Blocks.ORES_QUARTZ.getId(), 19600));
		/* Ore weights todo 1.13
		addOreWeight("oreAluminum", 3940); // Tinkers' Construct
		addOreWeight("oreAmber", 2075); // Thaumcraft
		addOreWeight("oreApatite", 1595); // Forestry
		addOreWeight("oreBlueTopaz", 3195); // Ars Magica
		addOreWeight("oreCertusQuartz", 3975); // Applied Energistics
		addOreWeight("oreChimerite", 3970); // Ars Magica
		addOreWeight("oreCinnabar",  2585); // Thaumcraft
		addOreWeight("oreCopper", 8325); // IC2, Thermal Expansion, Tinkers' Construct, etc.
		addOreWeight("oreDark", 1350); // EvilCraft
		addOreWeight("oreDarkIron", 1700); // Factorization (older versions)
		addOreWeight("oreFzDarkIron", 1700); // Factorization (newer versions)
		addOreWeight("oreGalena", 1000); // Factorization
		addOreWeight("oreInfusedAir", 925); // Thaumcraft
		addOreWeight("oreInfusedEarth", 925); // Thaumcraft
		addOreWeight("oreInfusedEntropy", 925); // Thaumcraft
		addOreWeight("oreInfusedFire", 925); // Thaumcraft
		addOreWeight("oreInfusedOrder", 925); // Thaumcraft
		addOreWeight("oreInfusedWater", 925); // Thaumcraft
		addOreWeight("oreLead", 7985); // IC2, Thermal Expansion, Factorization, etc.
		addOreWeight("oreMCropsEssence", 3085); // Magical Crops
		addOreWeight("oreMithril", 8); // Thermal Expansion
		addOreWeight("oreNickel", 2275); // Thermal Expansion
		addOreWeight("oreOlivine", 1100); // Project RED
		addOreWeight("orePlatinum", 365); // Thermal Expansion
		addOreWeight("oreRuby", 1100); // Project RED
		addOreWeight("oreSapphire", 1100); // Project RED
		addOreWeight("oreSilver", 6300); // Thermal Expansion, Factorization, etc.
		addOreWeight("oreSulfur", 1105); // Railcraft
		addOreWeight("oreTin", 9450); // IC2, Thermal Expansion, etc.
		addOreWeight("oreUranium", 1337); // IC2
		addOreWeight("oreVinteum", 5925); // Ars Magica
		addOreWeight("oreYellorite", 3520); // Big Reactors
		addOreWeight("oreZinc", 6485); // Flaxbeard's Steam Power
		addOreWeight("oreMythril", 6485); // Simple Ores2
		addOreWeight("oreAdamantium", 2275); // Simple Ores2
		addOreWeight("oreTungsten", 3520); // Simple Tungsten
		addOreWeight("oreOsmium", 6915); // Mekanism
		addOreWeight("oreQuartzBlack", 5535); // Actually Additions

		addOreWeightNether("oreCobalt", 500); // Tinker's Construct
		addOreWeightNether("oreArdite", 500); // Tinker's Construct
		addOreWeightNether("oreFirestone", 5); // Railcraft
		addOreWeightNether("oreNetherCoal", 17000); // Nether Ores
		addOreWeightNether("oreNetherCopper", 4700); // Nether Ores
		addOreWeightNether("oreNetherDiamond", 175); // Nether Ores
		addOreWeightNether("oreNetherEssence", 2460); // Magical Crops
		addOreWeightNether("oreNetherGold", 3635); // Nether Ores
		addOreWeightNether("oreNetherIron", 5790); // Nether Ores
		addOreWeightNether("oreNetherLapis", 3250); // Nether Ores
		addOreWeightNether("oreNetherLead", 2790); // Nether Ores
		addOreWeightNether("oreNetherNickel", 1790); // Nether Ores
		addOreWeightNether("oreNetherPlatinum", 170); // Nether Ores
		addOreWeightNether("oreNetherRedstone", 5600); // Nether Ores
		addOreWeightNether("oreNetherSilver", 1550); // Nether Ores
		addOreWeightNether("oreNetherSteel", 1690); // Nether Ores
		addOreWeightNether("oreNetherTin", 3750); // Nether Ores
		addOreWeightNether("oreFyrite", 1000); // Netherrocks
		addOreWeightNether("oreAshstone", 1000); // Netherrocks
		addOreWeightNether("oreDragonstone", 175); // Netherrocks
		addOreWeightNether("oreArgonite", 1000); // Netherrocks
		addOreWeightNether("oreOnyx", 500); // SimpleOres 2
		addOreWeightNether("oreHaditeCoal", 500); // Hadite
		*/

		// Can't iterate over ColorHelper map values yet since remap event hasn't fired yet -.-
		Block[] glass = { Blocks.GLASS, Blocks.WHITE_STAINED_GLASS, Blocks.ORANGE_STAINED_GLASS, Blocks.MAGENTA_STAINED_GLASS, Blocks.LIGHT_BLUE_STAINED_GLASS,
				Blocks.YELLOW_STAINED_GLASS, Blocks.LIME_STAINED_GLASS, Blocks.PINK_STAINED_GLASS, Blocks.GRAY_STAINED_GLASS, Blocks.LIGHT_GRAY_STAINED_GLASS,
				Blocks.CYAN_STAINED_GLASS, Blocks.PURPLE_STAINED_GLASS, Blocks.BLUE_STAINED_GLASS, Blocks.BROWN_STAINED_GLASS, Blocks.GREEN_STAINED_GLASS, Blocks.RED_STAINED_GLASS, Blocks.BLACK_STAINED_GLASS };
		for(Block b : glass)
			send(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(ColorHelper.STAINED_GLASS_MAP::get, b));

		Block[] glass_panes = { Blocks.GLASS_PANE, Blocks.WHITE_STAINED_GLASS_PANE, Blocks.ORANGE_STAINED_GLASS_PANE, Blocks.MAGENTA_STAINED_GLASS_PANE, Blocks.LIGHT_BLUE_STAINED_GLASS_PANE,
				Blocks.YELLOW_STAINED_GLASS_PANE, Blocks.LIME_STAINED_GLASS_PANE, Blocks.PINK_STAINED_GLASS_PANE, Blocks.GRAY_STAINED_GLASS_PANE, Blocks.LIGHT_GRAY_STAINED_GLASS_PANE,
				Blocks.CYAN_STAINED_GLASS_PANE, Blocks.PURPLE_STAINED_GLASS_PANE, Blocks.BLUE_STAINED_GLASS_PANE, Blocks.BROWN_STAINED_GLASS_PANE, Blocks.GREEN_STAINED_GLASS_PANE, Blocks.RED_STAINED_GLASS_PANE, Blocks.BLACK_STAINED_GLASS_PANE };
		for(Block b : glass_panes)
			send(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(ColorHelper.STAINED_GLASS_PANE_MAP::get, b));

		Block[] terracottas = { Blocks.TERRACOTTA, Blocks.WHITE_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA, Blocks.LIGHT_BLUE_TERRACOTTA,
				Blocks.YELLOW_TERRACOTTA, Blocks.LIME_TERRACOTTA, Blocks.PINK_TERRACOTTA, Blocks.GRAY_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA,
				Blocks.CYAN_TERRACOTTA, Blocks.PURPLE_TERRACOTTA, Blocks.BLUE_TERRACOTTA, Blocks.BROWN_TERRACOTTA, Blocks.GREEN_TERRACOTTA, Blocks.RED_TERRACOTTA, Blocks.BLACK_TERRACOTTA };
		for(Block b : terracottas)
			send(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(ColorHelper.TERRACOTTA_MAP::get, b));

		Block[] glazed_terracottas = { Blocks.WHITE_GLAZED_TERRACOTTA, Blocks.ORANGE_GLAZED_TERRACOTTA, Blocks.MAGENTA_GLAZED_TERRACOTTA, Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA,
				Blocks.YELLOW_GLAZED_TERRACOTTA, Blocks.LIME_GLAZED_TERRACOTTA, Blocks.PINK_GLAZED_TERRACOTTA, Blocks.GRAY_GLAZED_TERRACOTTA, Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA,
				Blocks.CYAN_GLAZED_TERRACOTTA, Blocks.PURPLE_GLAZED_TERRACOTTA, Blocks.BLUE_GLAZED_TERRACOTTA, Blocks.BROWN_GLAZED_TERRACOTTA, Blocks.GREEN_GLAZED_TERRACOTTA, Blocks.RED_GLAZED_TERRACOTTA, Blocks.BLACK_GLAZED_TERRACOTTA };
		for(Block b : glazed_terracottas)
			send(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(ColorHelper.GLAZED_TERRACOTTA_MAP::get, b));

		Block[] wools = { Blocks.WHITE_WOOL, Blocks.ORANGE_WOOL, Blocks.MAGENTA_WOOL, Blocks.LIGHT_BLUE_WOOL,
				Blocks.YELLOW_WOOL, Blocks.LIME_WOOL, Blocks.PINK_WOOL, Blocks.GRAY_WOOL, Blocks.LIGHT_GRAY_WOOL,
				Blocks.CYAN_WOOL, Blocks.PURPLE_WOOL, Blocks.BLUE_WOOL, Blocks.BROWN_WOOL, Blocks.GREEN_WOOL, Blocks.RED_WOOL, Blocks.BLACK_WOOL };
		for(Block b : wools)
			send(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(ColorHelper.WOOL_MAP::get, b));

		Block[] carpets = { Blocks.WHITE_CARPET, Blocks.ORANGE_CARPET, Blocks.MAGENTA_CARPET, Blocks.LIGHT_BLUE_CARPET,
				Blocks.YELLOW_CARPET, Blocks.LIME_CARPET, Blocks.PINK_CARPET, Blocks.GRAY_CARPET, Blocks.LIGHT_GRAY_CARPET,
				Blocks.CYAN_CARPET, Blocks.PURPLE_CARPET, Blocks.BLUE_CARPET, Blocks.BROWN_CARPET, Blocks.GREEN_CARPET, Blocks.RED_CARPET, Blocks.BLACK_CARPET };
		for(Block b : carpets)
			send(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(ColorHelper.CARPET_MAP::get, b));
		
		Block[] concretes = { Blocks.WHITE_CONCRETE, Blocks.ORANGE_CONCRETE, Blocks.MAGENTA_CONCRETE, Blocks.LIGHT_BLUE_CONCRETE,
				Blocks.YELLOW_CONCRETE, Blocks.LIME_CONCRETE, Blocks.PINK_CONCRETE, Blocks.GRAY_CONCRETE, Blocks.LIGHT_GRAY_CONCRETE,
				Blocks.CYAN_CONCRETE, Blocks.PURPLE_CONCRETE, Blocks.BLUE_CONCRETE, Blocks.BROWN_CONCRETE, Blocks.GREEN_CONCRETE, Blocks.RED_CONCRETE, Blocks.BLACK_CONCRETE};
		for(Block b : concretes)
			send(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(ColorHelper.CONCRETE_MAP::get, b));
		
		Block[] concrete_powders = { Blocks.WHITE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE_POWDER,
				Blocks.YELLOW_CONCRETE_POWDER, Blocks.LIME_CONCRETE_POWDER, Blocks.PINK_CONCRETE_POWDER, Blocks.GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE_POWDER,
				Blocks.CYAN_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE_POWDER, Blocks.BROWN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER, Blocks.RED_CONCRETE_POWDER, Blocks.BLACK_CONCRETE_POWDER};
		for(Block b : concrete_powders)
			send(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(ColorHelper.CONCRETE_POWDER_MAP::get, b));
	}

	public static void send(String method, Object thing) {
		InterModComms.sendTo(LibMisc.MOD_ID, method, () -> thing);
	}
}
