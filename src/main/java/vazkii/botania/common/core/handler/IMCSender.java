package vazkii.botania.common.core.handler;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.registries.IRegistryDelegate;
import vazkii.botania.api.ColorHelper;
import vazkii.botania.api.imc.IMC;
import vazkii.botania.api.imc.OreWeightMessage;
import vazkii.botania.api.imc.PaintableBlockMessage;
import vazkii.botania.common.lib.LibMisc;

public class IMCSender {
	public static void enqueue(InterModEnqueueEvent evt) {
		// Vanilla
		addOreWeight("coal", 67415);
		addOreWeight("diamond", 883);
		addOreWeight("emerald", 1239);
		addOreWeight("gold", 2647);
		addOreWeight("iron", 29371);
		addOreWeight("lapis", 1079);
		addOreWeight("redstone", 7654);
		// Common Metals
		addOreWeight("aluminium", 13762);
		addOreWeight("aluminum", 13762);
		addOreWeight("copper", 5567);
		addOreWeight("ferrous", 558);
		addOreWeight("galena", 4096);
		addOreWeight("lead", 4093);
		addOreWeight("mithril", 6485);
		addOreWeight("mythril", 6485);
		addOreWeight("nickel", 2275);
		addOreWeight("osmium", 6915);
		addOreWeight("platinum", 956);
		addOreWeight("silver", 4315);
		addOreWeight("tin", 8251);
		addOreWeight("tungsten", 140);
		addOreWeight("uranium", 230);
		addOreWeight("zinc", 838);
		// Common Gems
		addOreWeight("amber", 902);
		addOreWeight("ruby", 1384);
		addOreWeight("sapphire", 1287);
		addOreWeight("topaz", 6436);
		addOreWeight("amethyst", 1307);
		addOreWeight("malachite", 160);
		// Extreme Reactors
		addOreWeight("yellorite", 3520);
		// Blue Power
		addOreWeight("teslatite", 4312);
		// EvilCraft
		addOreWeight("dark", 1350);
		// Forestry
		addOreWeight("apatite", 250);
		// Mystical Agriculture
		addOreWeight("inferium", 10000);
		addOreWeight("prosperity", 7420);
		// Project RED
		addOreWeight("olivine", 1100);
		// Railcraft
		addOreWeight("sulfur", 1105);
		addOreWeight("sulphur", 1105);
		// Simple Ores 2
		addOreWeight("adamantium", 1469);
		// Silent Mechanisms
		addOreWeight("bismuth", 2407);
		// Thaumcraft
		addOreWeight("cinnabar",  2585);

		// Vanilla
		addOreWeightNether("quartz", 19600);
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

		/* Ore weights TODO 1.14 / 1.15
		addOreWeight("oreCertusQuartz", 3975); // Applied Energistics certusQuartz, certus/quartz or quartz/certus?
		addOreWeight("oreQuartzBlack", 5535); // Actually Additions quartzBlock, quartz/black or black/quartz?
		addOreWeightNether("oreHaditeCoal", 500); // Hadite haditeCoal, hadite/coal or coal/hadite?
		*/

		sendToSelf(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(color -> ColorHelper.STAINED_GLASS_MAP.get(color).get(), Blocks.GLASS));
		for(IRegistryDelegate<Block> b : ColorHelper.STAINED_GLASS_MAP.values())
			sendToSelf(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(color -> ColorHelper.STAINED_GLASS_MAP.get(color).get(), b));

		sendToSelf(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(color -> ColorHelper.STAINED_GLASS_PANE_MAP.get(color).get(), Blocks.GLASS_PANE));
		for(IRegistryDelegate<Block> b : ColorHelper.STAINED_GLASS_PANE_MAP.values())
			sendToSelf(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(color -> ColorHelper.STAINED_GLASS_PANE_MAP.get(color).get(), b));

		sendToSelf(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(color -> ColorHelper.TERRACOTTA_MAP.get(color).get(), Blocks.TERRACOTTA));
		for(IRegistryDelegate<Block> b : ColorHelper.TERRACOTTA_MAP.values())
			sendToSelf(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(color -> ColorHelper.TERRACOTTA_MAP.get(color).get(), b.get()));

		for(IRegistryDelegate<Block> b : ColorHelper.GLAZED_TERRACOTTA_MAP.values())
			sendToSelf(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(color -> ColorHelper.GLAZED_TERRACOTTA_MAP.get(color).get(), b));

		for(IRegistryDelegate<Block> b : ColorHelper.WOOL_MAP.values())
			sendToSelf(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(color -> ColorHelper.WOOL_MAP.get(color).get(), b));

		for(IRegistryDelegate<Block> b : ColorHelper.CARPET_MAP.values())
			sendToSelf(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(color -> ColorHelper.CARPET_MAP.get(color).get(), b));
		
		for(IRegistryDelegate<Block> b : ColorHelper.CONCRETE_MAP.values())
			sendToSelf(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(color -> ColorHelper.CONCRETE_MAP.get(color).get(), b));
		
		for(IRegistryDelegate<Block> b : ColorHelper.CONCRETE_POWDER_MAP.values())
			sendToSelf(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(color -> ColorHelper.CONCRETE_POWDER_MAP.get(color).get(), b));
	}

	public static void sendToSelf(String method, Object thing) {
		InterModComms.sendTo(LibMisc.MOD_ID, method, () -> thing);
	}

	private static void addOreWeight(String oreTag, int weight) {
		sendToSelf(IMC.REGISTER_ORE_WEIGHT, new OreWeightMessage(new ResourceLocation("forge", "ores/" + oreTag), weight));
	}

	private static void addOreWeightNether(String oreTag, int weight) {
		sendToSelf(IMC.REGISTER_NETHER_ORE_WEIGHT, new OreWeightMessage(new ResourceLocation("forge", "ores/" + oreTag), weight));
	}
}
