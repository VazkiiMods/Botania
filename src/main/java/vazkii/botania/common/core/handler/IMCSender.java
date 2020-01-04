package vazkii.botania.common.core.handler;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import vazkii.botania.api.ColorHelper;
import vazkii.botania.api.imc.IMC;
import vazkii.botania.api.imc.OreWeightMessage;
import vazkii.botania.api.imc.PaintableBlockMessage;
import vazkii.botania.common.lib.LibMisc;

public class IMCSender {
	public static void enqueue(InterModEnqueueEvent evt) {
		// Vanilla
		addOreWeight("coal", 46525);
		addOreWeight("diamond", 1265);
		addOreWeight("emerald", 780);
		addOreWeight("gold", 2970);
		addOreWeight("iron", 20665);
		addOreWeight("lapis", 1285);
		addOreWeight("redstone", 6885);
		// Common Metals
		addOreWeight("aluminium", 3940);
		addOreWeight("aluminum", 3940);
		addOreWeight("copper", 8325);
		addOreWeight("galena", 1000);
		addOreWeight("lead", 7985);
		addOreWeight("mithril", 8);
		addOreWeight("nickel", 2275);
		addOreWeight("osmium", 6915);
		addOreWeight("platinum", 365);
		addOreWeight("silver", 6300);
		addOreWeight("tin", 9450);
		addOreWeight("tungsten", 3520);
		addOreWeight("uranium", 1337);
		addOreWeight("zinc", 6485);
		// Common Gems
		addOreWeight("amber", 2075);
		addOreWeight("ruby", 1100);
		addOreWeight("sapphire", 1100);
		addOreWeight("topaz", 1100);
		// Ars Magica
		addOreWeight("chimerite", 3970);
		addOreWeight("vinteum", 5925);
		// Big Reactors / Extreme Reactors
		addOreWeight("yellorite", 3520);
		// EvilCraft
		addOreWeight("dark", 1350);
		// Forestry
		addOreWeight("apatite", 1595);
		// Mystical Agriculture
		addOreWeight("inferium", 10000);
		addOreWeight("prosperity", 7420);
		// Project RED
		addOreWeight("olivine", 1100);
		// Railcraft
		addOreWeight("sulfur", 1105);
		addOreWeight("sulfphur", 1105);
		// Simple Ores 2
		addOreWeight("mythril", 6485);
		addOreWeight("adamantium", 2275);
		// Thaumcraft
		addOreWeight("cinnabar",  2585);

		// Vanilla
		addOreWeightNether("quartz", 19600);
		// Magical Crops
		addOreWeightNether("nether/essence", 2460);
		// Mystical Agriculture
		addOreWeight("nether/inferium", 10000);
		addOreWeight("nether/prosperity", 7420);
		// Nether Ores
		addOreWeightNether("nether/coal", 17000);
		addOreWeightNether("nether/copper", 4700);
		addOreWeightNether("nether/diamond", 175);
		addOreWeightNether("nether/gold", 3635);
		addOreWeightNether("nether/iron", 5790);
		addOreWeightNether("nether/lapis", 3250);
		addOreWeightNether("nether/lead", 2790);
		addOreWeightNether("nether/nickel", 1790);
		addOreWeightNether("nether/platinum", 170);
		addOreWeightNether("nether/redstone", 5600);
		addOreWeightNether("nether/silver", 1550);
		addOreWeightNether("nether/steel", 1690);
		addOreWeightNether("nether/tin", 3750);
		// Netherrocks
		addOreWeightNether("argonite", 1000);
		addOreWeightNether("ashstone", 1000);
		addOreWeightNether("dragonstone", 175);
		addOreWeightNether("fyrite", 1000);
		// Railcraft
		addOreWeightNether("firestone", 5);
		// Simple Ores 2
		addOreWeightNether("onyx", 500);
		// Tinkers Construct
		addOreWeightNether("ardite", 500);
		addOreWeightNether("cobalt", 500);

		/* Ore weights TODO 1.14
		addOreWeight("oreBlueTopaz", 3195); // Ars Magica blueTopaz, blue/topaz or topaz/blue?
		addOreWeight("oreCertusQuartz", 3975); // Applied Energistics certusQuartz, certus/quartz or quartz/certus?
		addOreWeight("oreDarkIron", 1700); // Factorization (older versions) darkIron, dark/iron or iron/dark?
		addOreWeight("oreFzDarkIron", 1700); // Factorization (newer versions) fzDarkIron, fz/darkIron, fz/dark/iron, fz/iron/dark, dark/iron/fz or iron/dark/fz?
		addOreWeight("oreInfusedAir", 925); // Thaumcraft infused/air or infusedAir?
		addOreWeight("oreInfusedEarth", 925); // Thaumcraft infused/earth or infusedEarth?
		addOreWeight("oreInfusedEntropy", 925); // Thaumcraft infused/entropy or infusedEntropy?
		addOreWeight("oreInfusedFire", 925); // Thaumcraft infused/fire or infusedFire?
		addOreWeight("oreInfusedOrder", 925); // Thaumcraft infused/order or infusedOrder?
		addOreWeight("oreInfusedWater", 925); // Thaumcraft infused/water or infusedWater?
		addOreWeight("oreMCropsEssence", 3085); // Magical Crops mcropsEssence, mcrops/essence or essence/mcrops?
		addOreWeight("oreQuartzBlack", 5535); // Actually Additions quartzBlock, quartz/black or black/quartz?
		addOreWeightNether("oreHaditeCoal", 500); // Hadite haditeCoal, hadite/coal or coal/hadite?
		*/

		// Can't iterate over ColorHelper map values yet since remap event hasn't fired yet -.-
		Block[] glass = { Blocks.GLASS, Blocks.WHITE_STAINED_GLASS, Blocks.ORANGE_STAINED_GLASS, Blocks.MAGENTA_STAINED_GLASS, Blocks.LIGHT_BLUE_STAINED_GLASS,
				Blocks.YELLOW_STAINED_GLASS, Blocks.LIME_STAINED_GLASS, Blocks.PINK_STAINED_GLASS, Blocks.GRAY_STAINED_GLASS, Blocks.LIGHT_GRAY_STAINED_GLASS,
				Blocks.CYAN_STAINED_GLASS, Blocks.PURPLE_STAINED_GLASS, Blocks.BLUE_STAINED_GLASS, Blocks.BROWN_STAINED_GLASS, Blocks.GREEN_STAINED_GLASS, Blocks.RED_STAINED_GLASS, Blocks.BLACK_STAINED_GLASS };
		for(Block b : glass)
			send(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(ColorHelper.STAINED_GLASS_MAP::get, b));

		Block[] glassPanes = { Blocks.GLASS_PANE, Blocks.WHITE_STAINED_GLASS_PANE, Blocks.ORANGE_STAINED_GLASS_PANE, Blocks.MAGENTA_STAINED_GLASS_PANE, Blocks.LIGHT_BLUE_STAINED_GLASS_PANE,
				Blocks.YELLOW_STAINED_GLASS_PANE, Blocks.LIME_STAINED_GLASS_PANE, Blocks.PINK_STAINED_GLASS_PANE, Blocks.GRAY_STAINED_GLASS_PANE, Blocks.LIGHT_GRAY_STAINED_GLASS_PANE,
				Blocks.CYAN_STAINED_GLASS_PANE, Blocks.PURPLE_STAINED_GLASS_PANE, Blocks.BLUE_STAINED_GLASS_PANE, Blocks.BROWN_STAINED_GLASS_PANE, Blocks.GREEN_STAINED_GLASS_PANE, Blocks.RED_STAINED_GLASS_PANE, Blocks.BLACK_STAINED_GLASS_PANE };
		for(Block b : glassPanes)
			send(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(ColorHelper.STAINED_GLASS_PANE_MAP::get, b));

		Block[] terracottas = { Blocks.TERRACOTTA, Blocks.WHITE_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA, Blocks.LIGHT_BLUE_TERRACOTTA,
				Blocks.YELLOW_TERRACOTTA, Blocks.LIME_TERRACOTTA, Blocks.PINK_TERRACOTTA, Blocks.GRAY_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA,
				Blocks.CYAN_TERRACOTTA, Blocks.PURPLE_TERRACOTTA, Blocks.BLUE_TERRACOTTA, Blocks.BROWN_TERRACOTTA, Blocks.GREEN_TERRACOTTA, Blocks.RED_TERRACOTTA, Blocks.BLACK_TERRACOTTA };
		for(Block b : terracottas)
			send(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(ColorHelper.TERRACOTTA_MAP::get, b));

		Block[] glazedTerracottas = { Blocks.WHITE_GLAZED_TERRACOTTA, Blocks.ORANGE_GLAZED_TERRACOTTA, Blocks.MAGENTA_GLAZED_TERRACOTTA, Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA,
				Blocks.YELLOW_GLAZED_TERRACOTTA, Blocks.LIME_GLAZED_TERRACOTTA, Blocks.PINK_GLAZED_TERRACOTTA, Blocks.GRAY_GLAZED_TERRACOTTA, Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA,
				Blocks.CYAN_GLAZED_TERRACOTTA, Blocks.PURPLE_GLAZED_TERRACOTTA, Blocks.BLUE_GLAZED_TERRACOTTA, Blocks.BROWN_GLAZED_TERRACOTTA, Blocks.GREEN_GLAZED_TERRACOTTA, Blocks.RED_GLAZED_TERRACOTTA, Blocks.BLACK_GLAZED_TERRACOTTA };
		for(Block b : glazedTerracottas)
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
		
		Block[] concretePowders = { Blocks.WHITE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE_POWDER,
				Blocks.YELLOW_CONCRETE_POWDER, Blocks.LIME_CONCRETE_POWDER, Blocks.PINK_CONCRETE_POWDER, Blocks.GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE_POWDER,
				Blocks.CYAN_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE_POWDER, Blocks.BROWN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER, Blocks.RED_CONCRETE_POWDER, Blocks.BLACK_CONCRETE_POWDER};
		for(Block b : concretePowders)
			send(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(ColorHelper.CONCRETE_POWDER_MAP::get, b));
	}

	public static void send(String method, Object thing) {
		InterModComms.sendTo(LibMisc.MOD_ID, method, () -> thing);
	}

	private static void addOreWeight(String oreTag, int weight) {
		send(IMC.REGISTER_ORE_WEIGHT, new OreWeightMessage(new ResourceLocation("forge", "ores/" + oreTag), weight));
	}

	private static void addOreWeightNether(String oreTag, int weight) {
		send(IMC.REGISTER_NETHER_ORE_WEIGHT, new OreWeightMessage(new ResourceLocation("forge", "ores/" + oreTag), weight));
	}
}
