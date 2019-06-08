package vazkii.botania.common.core.handler;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Blocks;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import vazkii.botania.api.ColorHelper;
import vazkii.botania.api.imc.IMC;
import vazkii.botania.api.imc.ModWikiMessage;
import vazkii.botania.api.imc.PaintableBlockMessage;
import vazkii.botania.api.wiki.IWikiProvider;
import vazkii.botania.api.wiki.SimpleWikiProvider;
import vazkii.botania.common.crafting.ModManaAlchemyRecipes;
import vazkii.botania.common.crafting.ModManaConjurationRecipes;
import vazkii.botania.common.crafting.ModManaInfusionRecipes;
import vazkii.botania.common.lib.LibMisc;

public class IMCSender {
	public static void enqueue(InterModEnqueueEvent evt) {
		/* Ore weights todo 1.13
		addOreWeight("oreAluminum", 3940); // Tinkers' Construct
		addOreWeight("oreAmber", 2075); // Thaumcraft
		addOreWeight("oreApatite", 1595); // Forestry
		addOreWeight("oreBlueTopaz", 3195); // Ars Magica
		addOreWeight("oreCertusQuartz", 3975); // Applied Energistics
		addOreWeight("oreChimerite", 3970); // Ars Magica
		addOreWeight("oreCinnabar",  2585); // Thaumcraft
		addOreWeight("oreCoal", 46525); // Vanilla
		addOreWeight("oreCopper", 8325); // IC2, Thermal Expansion, Tinkers' Construct, etc.
		addOreWeight("oreDark", 1350); // EvilCraft
		addOreWeight("oreDarkIron", 1700); // Factorization (older versions)
		addOreWeight("oreFzDarkIron", 1700); // Factorization (newer versions)
		addOreWeight("oreDiamond", 1265); // Vanilla
		addOreWeight("oreEmerald", 780); // Vanilla
		addOreWeight("oreGalena", 1000); // Factorization
		addOreWeight("oreGold", 2970); // Vanilla
		addOreWeight("oreInfusedAir", 925); // Thaumcraft
		addOreWeight("oreInfusedEarth", 925); // Thaumcraft
		addOreWeight("oreInfusedEntropy", 925); // Thaumcraft
		addOreWeight("oreInfusedFire", 925); // Thaumcraft
		addOreWeight("oreInfusedOrder", 925); // Thaumcraft
		addOreWeight("oreInfusedWater", 925); // Thaumcraft
		addOreWeight("oreIron", 20665); // Vanilla
		addOreWeight("oreLapis", 1285); // Vanilla
		addOreWeight("oreLead", 7985); // IC2, Thermal Expansion, Factorization, etc.
		addOreWeight("oreMCropsEssence", 3085); // Magical Crops
		addOreWeight("oreMithril", 8); // Thermal Expansion
		addOreWeight("oreNickel", 2275); // Thermal Expansion
		addOreWeight("oreOlivine", 1100); // Project RED
		addOreWeight("orePlatinum", 365); // Thermal Expansion
		addOreWeight("oreRedstone", 6885); // Vanilla
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

		addOreWeightNether("oreQuartz", 19600); // Vanilla
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

		IWikiProvider technicWiki = new SimpleWikiProvider("Technic Wiki", "http://wiki.technicpack.net/%s");
		IWikiProvider mekanismWiki = new SimpleWikiProvider("Mekanism Wiki", "http://wiki.aidancbrady.com/wiki/%s");
		send(IMC.REGISTER_MOD_WIKI, new ModWikiMessage("minecraft", new SimpleWikiProvider("Minecraft Wiki", "https://minecraft.gamepedia.com/%s")));
		send(IMC.REGISTER_MOD_WIKI, new ModWikiMessage("mekanism", mekanismWiki));
		send(IMC.REGISTER_MOD_WIKI, new ModWikiMessage("mekanismgenerators", mekanismWiki));
		send(IMC.REGISTER_MOD_WIKI, new ModWikiMessage("mekanismtools", mekanismWiki));
		send(IMC.REGISTER_MOD_WIKI, new ModWikiMessage("enderio", new SimpleWikiProvider("EnderIO Wiki", "http://wiki.enderio.com/%s")));
		send(IMC.REGISTER_MOD_WIKI, new ModWikiMessage("tropicraft", new SimpleWikiProvider("Tropicraft Wiki", "http://wiki.tropicraft.net/wiki/%s")));
		send(IMC.REGISTER_MOD_WIKI, new ModWikiMessage("randomthings", new SimpleWikiProvider("Random Things Wiki", "https://lumien.net/rtwiki/blocks/%s/", "-", true)));
		send(IMC.REGISTER_MOD_WIKI, new ModWikiMessage("appliedenergistics2", new SimpleWikiProvider("AE2 Wiki", "http://ae-mod.info/%s")));
		send(IMC.REGISTER_MOD_WIKI, new ModWikiMessage("bigreactors", technicWiki));

		// Can't iterate over ColorHelper map values yet since remap event hasn't fired yet -.-
		Block[] stainedGlass = { Blocks.WHITE_STAINED_GLASS, Blocks.ORANGE_STAINED_GLASS, Blocks.MAGENTA_STAINED_GLASS, Blocks.LIGHT_BLUE_STAINED_GLASS,
				Blocks.YELLOW_STAINED_GLASS, Blocks.LIME_STAINED_GLASS, Blocks.PINK_STAINED_GLASS, Blocks.GRAY_STAINED_GLASS, Blocks.LIGHT_GRAY_STAINED_GLASS,
				Blocks.CYAN_STAINED_GLASS, Blocks.PURPLE_STAINED_GLASS, Blocks.BLUE_STAINED_GLASS, Blocks.BROWN_STAINED_GLASS, Blocks.GREEN_STAINED_GLASS, Blocks.RED_STAINED_GLASS, Blocks.BLACK_STAINED_GLASS };
		for(Block b : stainedGlass)
			send(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(ColorHelper.STAINED_GLASS_MAP::get, b));

		Block[] sgPanes = { Blocks.WHITE_STAINED_GLASS_PANE, Blocks.ORANGE_STAINED_GLASS_PANE, Blocks.MAGENTA_STAINED_GLASS_PANE, Blocks.LIGHT_BLUE_STAINED_GLASS_PANE,
				Blocks.YELLOW_STAINED_GLASS_PANE, Blocks.LIME_STAINED_GLASS_PANE, Blocks.PINK_STAINED_GLASS_PANE, Blocks.GRAY_STAINED_GLASS_PANE, Blocks.LIGHT_GRAY_STAINED_GLASS_PANE,
				Blocks.CYAN_STAINED_GLASS_PANE, Blocks.PURPLE_STAINED_GLASS_PANE, Blocks.BLUE_STAINED_GLASS_PANE, Blocks.BROWN_STAINED_GLASS_PANE, Blocks.GREEN_STAINED_GLASS_PANE, Blocks.RED_STAINED_GLASS_PANE, Blocks.BLACK_STAINED_GLASS_PANE };
		for(Block b : sgPanes)
			send(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(ColorHelper.STAINED_GLASS_PANE_MAP::get, b));
		
		Block[] terracottas = { Blocks.WHITE_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA, Blocks.LIGHT_BLUE_TERRACOTTA,
				Blocks.YELLOW_TERRACOTTA, Blocks.LIME_TERRACOTTA, Blocks.PINK_TERRACOTTA, Blocks.GRAY_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA,
				Blocks.CYAN_TERRACOTTA, Blocks.PURPLE_TERRACOTTA, Blocks.BLUE_TERRACOTTA, Blocks.BROWN_TERRACOTTA, Blocks.GREEN_TERRACOTTA, Blocks.RED_TERRACOTTA, Blocks.BLACK_TERRACOTTA };
		for(Block b : terracottas)
			send(IMC.REGISTER_PAINTABLE_BLOCK, new PaintableBlockMessage(ColorHelper.TERRACOTTA_MAP::get, b));

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

		ModManaInfusionRecipes.send();
		ModManaAlchemyRecipes.send();
		ModManaConjurationRecipes.send();
	}

	public static void send(String method, Object thing) {
		InterModComms.sendTo(LibMisc.MOD_ID, method, () -> thing);
	}
}
