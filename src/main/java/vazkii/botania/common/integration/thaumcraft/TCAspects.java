/**
 * This class was created by <Hubry>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 19, 2018, 8:25:10 PM (GMT)]
 */

package vazkii.botania.common.integration.thaumcraft;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectEventProxy;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.AspectRegistryEvent;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibEntityNames;
import vazkii.botania.common.lib.LibOreDict;

import java.util.Arrays;
import java.util.List;

import static thaumcraft.api.aspects.Aspect.*;

public class TCAspects {
	private static final int ANY = OreDictionary.WILDCARD_VALUE;
	private final AspectEventProxy proxy;

	private TCAspects(AspectEventProxy proxy) {
		this.proxy = proxy;
	}

	@SubscribeEvent
	public static void registerAspects(AspectRegistryEvent event) {
		TCAspects handler = new TCAspects(event.register);
		handler.registerFlowerAspects();
		handler.registerItemAspects();
		handler.registerBrewAspects();
		handler.registerEntityAspects();
	}

	private void registerFlowerAspects() {
		registerFlower(LibBlockNames.SUBTILE_PUREDAISY,      new AspectList().add(LIFE, 10).add(EXCHANGE, 5));
		registerFlower(LibBlockNames.SUBTILE_MANASTAR,       new AspectList().add(SENSES, 5).add(AURA, 5));

		registerFlower(LibBlockNames.SUBTILE_ENDOFLAME,      new AspectList().add(FIRE, 10));
		registerFlower(LibBlockNames.SUBTILE_HYDROANGEAS,    new AspectList().add(WATER, 10));
		registerFlower(LibBlockNames.SUBTILE_THERMALILY,     new AspectList().add(FIRE, 5).add(EARTH, 5));
		registerFlower(LibBlockNames.SUBTILE_ARCANE_ROSE,    new AspectList().add(MIND, 15));
		registerFlower(LibBlockNames.SUBTILE_MUNCHDEW,       new AspectList().add(PLANT, 5).add(DESIRE, 5));
		registerFlower(LibBlockNames.SUBTILE_ENTROPINNYUM,   new AspectList().add(ENTROPY, 5).add(ALCHEMY, 5));
		registerFlower(LibBlockNames.SUBTILE_KEKIMURUS,      new AspectList().add(DESIRE, 10));
		registerFlower(LibBlockNames.SUBTILE_GOURMARYLLIS,   new AspectList().add(DESIRE, 5).add(LIFE, 5));
		registerFlower(LibBlockNames.SUBTILE_NARSLIMMUS,     new AspectList().add(WATER, 5).add(LIFE, 5));
		registerFlower(LibBlockNames.SUBTILE_SPECTROLUS,     new AspectList().add(SENSES, 10).add(BEAST, 5));
		registerFlower(LibBlockNames.SUBTILE_RAFFLOWSIA,     new AspectList().add(VOID, 10));
		registerFlower(LibBlockNames.SUBTILE_SHULK_ME_NOT,   new AspectList().add(FLIGHT, 5).add(TRAP, 5));
		registerFlower(LibBlockNames.SUBTILE_DANDELIFEON,    new AspectList().add(ORDER, 5).add(MECHANISM, 5).add(LIFE, 5));

		registerFlower(LibBlockNames.SUBTILE_JADED_AMARANTHUS, new AspectList().add(LIFE, 5).add(PLANT, 5).add(SENSES, 5));
		registerFlower(LibBlockNames.SUBTILE_BELLETHORN,     new AspectList().add(AVERSION, 10));
		registerFlower(LibBlockNames.SUBTILE_DREADTHORN,     new AspectList().add(AVERSION, 5).add(BEAST, 5));
		registerFlower(LibBlockNames.SUBTILE_HEISEI_DREAM,   new AspectList().add(SOUL, 5).add(BEAST, 5));
		registerFlower(LibBlockNames.SUBTILE_TIGERSEYE,      new AspectList().add(MOTION, 5).add(TRAP, 5));
		registerFlower(LibBlockNames.SUBTILE_ORECHID,        new AspectList().add(DESIRE, 10).add(METAL, 5).add(EARTH, 5));
		registerFlower(LibBlockNames.SUBTILE_ORECHID_IGNEM,  new AspectList().add(DESIRE, 10).add(METAL, 5).add(FIRE, 5));
		registerFlower(LibBlockNames.SUBTILE_FALLEN_KANADE,  new AspectList().add(LIFE, 10));
		registerFlower(LibBlockNames.SUBTILE_EXOFLAME,       new AspectList().add(FIRE, 5).add(MOTION, 5));
		registerFlower(LibBlockNames.SUBTILE_AGRICARNATION,  new AspectList().add(LIFE, 5).add(PLANT, 5));
		registerFlower(LibBlockNames.SUBTILE_HOPPERHOCK,     new AspectList().add(VOID, 5).add(EXCHANGE, 5));
		registerFlower(LibBlockNames.SUBTILE_TANGLEBERRIE,   new AspectList().add(TRAP, 10));
		registerFlower(LibBlockNames.SUBTILE_JIYUULIA,       new AspectList().add(PROTECT, 5).add(TRAP, 5));
		registerFlower(LibBlockNames.SUBTILE_RANNUNCARPUS,   new AspectList().add(MAN, 5).add(EXCHANGE, 5));
		registerFlower(LibBlockNames.SUBTILE_HYACIDUS,       new AspectList().add(DEATH, 10));
		registerFlower(LibBlockNames.SUBTILE_POLLIDISIAC,    new AspectList().add(BEAST, 5).add(DESIRE, 5));
		registerFlower(LibBlockNames.SUBTILE_CLAYCONIA,      new AspectList().add(WATER, 5).add(EXCHANGE, 5));
		registerFlower(LibBlockNames.SUBTILE_LOONIUM,        new AspectList().add(DESIRE, 10).add(BEAST, 5).add(UNDEAD, 5));
		registerFlower(LibBlockNames.SUBTILE_DAFFOMILL,      new AspectList().add(MOTION, 10));
		registerFlower(LibBlockNames.SUBTILE_VINCULOTUS,     new AspectList().add(ELDRITCH, 5).add(TRAP, 10));
		registerFlower(LibBlockNames.SUBTILE_SPECTRANTHEMUM, new AspectList().add(ELDRITCH, 5).add(MOTION, 5));
		registerFlower(LibBlockNames.SUBTILE_MEDUMONE,       new AspectList().add(TRAP, 5).add(COLD, 5));
		registerFlower(LibBlockNames.SUBTILE_MARIMORPHOSIS,  new AspectList().add(EARTH, 5).add(SENSES, 5));
		registerFlower(LibBlockNames.SUBTILE_BUBBELL,        new AspectList().add(AIR, 5).add(VOID, 5));
		registerFlower(LibBlockNames.SUBTILE_SOLEGNOLIA,     new AspectList().add(METAL, 5).add(TRAP, 5));
		registerFlower(LibBlockNames.SUBTILE_BERGAMUTE,      new AspectList().add(SENSES, 5).add(TRAP, 5));
	}

	//Each category is mostly ordered by order of its recipe registration.
	private void registerItemAspects() {
		//Pure Daisy
		register(LibOreDict.LIVING_WOOD, new AspectList().add(PLANT, 20).add(LIFE, 5));
		register(LibOreDict.LIVING_ROCK, new AspectList().add(EARTH, 5).add(LIFE, 3));

		//Mana Infusion
		register(LibOreDict.MANA_STEEL, new AspectList().add(METAL, 10).add(MAGIC, 5));
		register(LibOreDict.MANA_PEARL, new AspectList().add(ELDRITCH, 10).add(MAGIC, 5).add(MOTION, 5));
		register(LibOreDict.MANA_DIAMOND, new AspectList(new ItemStack(Items.DIAMOND)).add(MAGIC, 5));
		register(LibOreDict.MANA_POWDER, new AspectList().add(ENERGY, 3).add(MAGIC, 3));
		register(ModBlocks.pistonRelay, 0, new AspectList(new ItemStack(Blocks.PISTON)).add(AURA, 10));
		register(ModItems.manaCookie, 0, new AspectList(new ItemStack(Items.COOKIE)).merge(LIFE, 20).add(MAGIC, 5));

		AspectList grassSeedAspects =  new AspectList().add(PLANT, 5).add(LIFE, 5).add(EXCHANGE, 2);
		register(ModItems.grassSeeds, 0, grassSeedAspects); //Pasture Seed
		register(ModItems.grassSeeds, 1, grassSeedAspects); //Boreal Seed
		register(ModItems.grassSeeds, 2, grassSeedAspects.copy().add(DARKNESS, 5).add(FLUX, 1)); //Infestation Spore

		register(ModItems.quartz, 1, new AspectList(new ItemStack(Items.QUARTZ)).add(MAGIC, 2)); //Mana Quartz
		register(ModBlocks.tinyPotato, ANY, new AspectList(new ItemStack(Items.POTATO)).add(LIFE, 5).add(MOTION, 2));
		register(ModItems.manaInkwell, ANY, new AspectList().add(SENSES, 5).add(MAGIC, 5).add(WATER, 2).add(BEAST, 1));
		register(ModBlocks.manaGlass, 0, new AspectList(new ItemStack(Blocks.GLASS)).add(LIGHT, 5).add(MAGIC, 1));
		register(ModItems.manaResource, 16, new AspectList(new ItemStack(Items.STRING)).merge(CRAFT, 2).add(MAGIC, 2)); //Mana Infused String
		register(ModItems.manaBottle, ANY, new AspectList().add(MAGIC, 5).add(CRYSTAL, 5));

		//Elven Trade
		register(LibOreDict.DREAM_WOOD, new AspectList().add(PLANT, 20).add(ELDRITCH, 3));
		register(LibOreDict.ELEMENTIUM, new AspectList().add(METAL, 10).add(ELDRITCH, 5));
		register(LibOreDict.PIXIE_DUST, new AspectList(new ItemStack(Items.ENDER_PEARL)).add(MAGIC, 10));
		register(LibOreDict.DRAGONSTONE, new AspectList(new ItemStack(Items.DIAMOND)).add(ELDRITCH, 10));
		register(ModItems.quartz, 5, new AspectList(new ItemStack(Items.QUARTZ)).add(ELDRITCH, 1));
		register(ModBlocks.elfGlass, 0, new AspectList(new ItemStack(Blocks.GLASS)).add(LIGHT, 5).add(ELDRITCH, 1));

		//Runic Altar
		AspectList runeAspects = new AspectList().add(EARTH, 5).add(MAGIC, 5);
		register(ModItems.rune,  0, runeAspects.copy().add(WATER,  25));  //Water
		register(ModItems.rune,  1, runeAspects.copy().add(FIRE,   25));  //Fire
		register(ModItems.rune,  2, runeAspects.copy().add(EARTH,  25));  //Earth
		register(ModItems.rune,  3, runeAspects.copy().add(AIR,    25));  //Air
		register(ModItems.rune,  4, runeAspects.copy().add(LIFE,   10).add(PLANT, 15));  //Spring
		register(ModItems.rune,  5, runeAspects.copy().add(LIFE,   10).add(FIRE,  15));  //Summer
		register(ModItems.rune,  6, runeAspects.copy().add(LIFE,   10).add(DEATH, 15));  //Autumn
		register(ModItems.rune,  7, runeAspects.copy().add(LIFE,   10).add(COLD,  15));  //Winter
		register(ModItems.rune,  8, runeAspects.copy().add(AURA,   25));                 //Mana
		register(ModItems.rune,  9, runeAspects.copy().add(DESIRE, 10).add(MAN,  15));     //Lust
		register(ModItems.rune, 10, runeAspects.copy().add(DESIRE, 10).add(LIFE, 15));     //Gluttony
		register(ModItems.rune, 11, runeAspects.copy().add(DESIRE, 25));                   //Greed
		register(ModItems.rune, 12, runeAspects.copy().add(DESIRE, 10).add(TRAP, 15));     //Sloth
		register(ModItems.rune, 13, runeAspects.copy().add(DESIRE, 10).add(AVERSION, 15)); //Wrath
		register(ModItems.rune, 14, runeAspects.copy().add(DESIRE, 10).add(EXCHANGE, 15)); //Envy
		register(ModItems.rune, 15, runeAspects.copy().add(DESIRE, 10).add(SENSES, 15));   //Pride

		//The rest - items with no recipes, loot, overrides of autoassigned aspects, etc.
		//Ordered mostly by ModItems / ModBlocks
		register(LibOreDict.TERRA_STEEL, new AspectList().add(METAL, 15).add(MAGIC, 20).add(EARTH, 10));
		register(LibOreDict.LIFE_ESSENCE, new AspectList().add(LIFE, 15).add(EARTH, 10).add(SOUL, 10).add(ORDER, 5).add(MAGIC, 5).add(ELDRITCH, 5));
		register(LibOreDict.ENDER_AIR_BOTTLE, new AspectList().add(ELDRITCH, 5).add(AIR, 5));
		register(LibOreDict.PLACEHOLDER, new AspectList().add(CRAFT, 1));

		for(String name : LibOreDict.FLOWER)
			register(name, new AspectList(new ItemStack(Blocks.RED_FLOWER)));
		for(String name : LibOreDict.DOUBLE_FLOWER)
			register(name, new AspectList(new ItemStack(Blocks.DOUBLE_PLANT)));
		for(String name : LibOreDict.PETAL)
			register(name, new AspectList().add(SENSES, 5).add(PLANT, 5));

		for(int i = 0; i < 15; i++)
			register(ModItems.dye, i, new AspectList().add(SENSES, 5).add(PLANT, 1));
		registerComplex(ModItems.lexicon, 0, new AspectList().add(MAGIC, 5).add(MIND, 10));

		registerComplex(ModItems.pestleAndMortar, 0, new AspectList().add(TOOL, 4));
		register(LibOreDict.LIVINGWOOD_TWIG, new AspectList().add(PLANT, 8).add(LIFE, 4));
		register(LibOreDict.DREAMWOOD_TWIG, new AspectList().add(PLANT, 8).add(ELDRITCH, 4));
		registerComplex(ModItems.twigWand, 0, new AspectList().add(TOOL, 8));

		//Lenses
		registerComplex(ModItems.lens,  1, new AspectList().add(MOTION, 10));              //Velocity
		registerComplex(ModItems.lens,  2, new AspectList().add(ENERGY, 10));              //Potency
		registerComplex(ModItems.lens,  3, new AspectList().add(PROTECT, 10));             //Resistance
		registerComplex(ModItems.lens,  4, new AspectList().add(TRAP, 10));                //Efficiency
		registerComplex(ModItems.lens,  5, new AspectList().add(MOTION, 10));              //Bounce
		registerComplex(ModItems.lens,  6, new AspectList().add(EARTH, 10));               //Gravity
		registerComplex(ModItems.lens,  7, new AspectList().add(TOOL, 10));                //Bore
		registerComplex(ModItems.lens,  8, new AspectList().add(AVERSION, 10));            //Damaging
		registerComplex(ModItems.lens,  9, new AspectList().add(ELDRITCH, 10));            //Phantom
		registerComplex(ModItems.lens, 10, new AspectList().add(DESIRE, 10));              //Magnetizing
		registerComplex(ModItems.lens, 11, new AspectList().add(ENTROPY, 10));             //Entropic
		registerComplex(ModItems.lens, 12, new AspectList().add(DESIRE, 10));              //Influence
		registerComplex(ModItems.lens, 13, new AspectList().add(METAL, 10));               //Weight
		registerComplex(ModItems.lens, 14, new AspectList().add(SENSES, 10));              //Paintslinger
		registerComplex(ModItems.lens, 15, new AspectList().add(FIRE, 10));                //Kindle
		registerComplex(ModItems.lens, 16, new AspectList().add(MECHANISM, 10));           //Force
		registerComplex(ModItems.lens, 18, new AspectList().add(ELDRITCH, 10));            //Warp
		registerComplex(ModItems.lens, 19, new AspectList().add(SOUL, 10));                //Redirective
		registerComplex(ModItems.lens, 20, new AspectList().add(MAN, 10));                 //Celebratory
		registerComplex(ModItems.lens, 21, new AspectList().add(LIGHT, 5).add(SENSES, 5)); //Flare
		registerComplex(ModItems.lens, 22, new AspectList().add(MOTION, 10));              //Messenger
		registerComplex(ModItems.lens, 23, new AspectList().add(MAN, 10));                 //Tripwire

		//Rods
		registerComplex(ModItems.terraformRod, 0, new AspectList().add(EXCHANGE, 15));
		registerComplex(ModItems.rainbowRod,   0, new AspectList().add(SENSES, 10).add(LIGHT, 10));
		registerComplex(ModItems.tornadoRod,   0, new AspectList().add(FLIGHT, 20));
		registerComplex(ModItems.diviningRod,  0, new AspectList().add(SENSES, 20));
		registerComplex(ModItems.gravityRod,   0, new AspectList().add(MOTION, 20));
		registerComplex(ModItems.missileRod,   0, new AspectList().add(AVERSION, 30));
		registerComplex(ModItems.cobbleRod,    0, new AspectList().add(EARTH, 20));
		registerComplex(ModItems.exchangeRod,  0, new AspectList().add(EXCHANGE, 30));

		register(LibOreDict.RED_STRING, new AspectList()
				.add(ENERGY, 30).add(EXCHANGE, 20).add(ELDRITCH, 15).add(MECHANISM, 15).add(MAGIC, 10));
		registerComplex(ModItems.manaTablet, ANY, new AspectList().add(VOID, 5).add(MAGIC, 5));
		registerComplex(ModItems.fertilizer, 0, new AspectList().add(LIFE, 4));
		registerComplex(ModItems.grassHorn, 0, new AspectList().add(SENSES, 10).add(TOOL, 8));
		register(ModItems.manaMirror, ANY, new AspectList()
				.add(EARTH, 10).add(PLANT, 10).add(ELDRITCH, 10).add(EXCHANGE, 5).add(MAGIC, 10).add(METAL, 10));
		registerComplex(ModItems.travelBelt, 0, new AspectList().add(MOTION, 10));
		registerComplex(ModItems.magnetRing, 0, new AspectList().add(DESIRE, 10));
		registerComplex(ModItems.flightTiara, ANY, new AspectList().add(FLIGHT, 38));

		if(ConfigHandler.darkQuartzEnabled)
			register(ModItems.quartz, 0, new AspectList(new ItemStack(Items.QUARTZ)).add(ENERGY, 1).add(DARKNESS, 1)); //Smokey
		register(ModItems.quartz, 2, new AspectList(new ItemStack(Items.QUARTZ)).add(FIRE, 1).add(ALCHEMY, 1)); //Blaze
		register(ModItems.quartz, 3, new AspectList(new ItemStack(Items.QUARTZ)).add(SENSES, 1).add(PLANT, 1)); //Lavender
		register(ModItems.quartz, 4, new AspectList(new ItemStack(Items.QUARTZ)).add(ENERGY, 2));               //Redquartz
		register(ModItems.quartz, 6, new AspectList(new ItemStack(Items.QUARTZ)).add(SENSES, 1).add(PLANT, 1)); //Sunny

		registerComplex(ModItems.openBucket, 0, new AspectList().add(VOID, 15));
		registerComplex(ModItems.pixieRing, 0, new AspectList().add(AURA, 5).add(FLIGHT, 5));
		registerComplex(ModItems.superTravelBelt, 0, new AspectList().add(MOTION, 10));
		registerComplex(ModItems.laputaShard, ANY, new AspectList().add(FLIGHT, 20));
		registerComplex(ModItems.itemFinder, 0, new AspectList().add(SENSES, 20));
		register(ModItems.enderHand, 0, new AspectList(new ItemStack(Blocks.ENDER_CHEST)).add(BEAST, 15));
		register(ModItems.spark, 0, new AspectList().add(AURA, 15).add(MOTION, 15).add(FIRE, 10).add(SENSES, 10));
		register(ModItems.vial, 0, new AspectList(new ItemStack(ModBlocks.manaGlass)).add(VOID, 2));
		register(ModItems.vial, 1, new AspectList(new ItemStack(ModBlocks.elfGlass)).add(VOID, 3));
		registerComplex(ModItems.holyCloak, 0, new AspectList().add(PROTECT, 15));
		registerComplex(ModItems.unholyCloak, 0, new AspectList().add(AVERSION, 15));
		registerComplex(ModItems.balanceCloak, 0, new AspectList().add(PROTECT, 10).add(AVERSION, 10));
		registerComplex(ModItems.craftingHalo, 0, new AspectList().add(CRAFT, 15));
		register(ModItems.blackLotus, 0, new AspectList().add(MAGIC, 10).add(ORDER, 10).add(ELDRITCH, 5));
		register(ModItems.blackLotus, 1, new AspectList().add(MAGIC, 30).add(ORDER, 20).add(ELDRITCH, 10));
		registerComplex(ModItems.monocle, 0, new AspectList().add(SENSES, 10));
		register(ModItems.clip, 0, new AspectList(new ItemStack(ModBlocks.dreamwood)).add(VOID, 5).add(MECHANISM, 2));
		registerComplex(ModItems.worldSeed, 0, new AspectList().add(MOTION, 10).add(PLANT, 5).add(ELDRITCH, 3));
		registerComplex(ModItems.thornChakram, 0, new AspectList().add(AVERSION, 12).add(DEATH, 6));
		registerComplex(ModItems.thornChakram, 1, new AspectList().merge(AVERSION, 7)); //Flare Chakram
		register(ModItems.overgrowthSeed, 0, new AspectList().add(LIFE, 30).add(MAGIC, 25));
		register(ModItems.craftPattern, ANY, new AspectList().add(CRAFT, 10).add(ENERGY, 10));

		registerComplex(ModItems.swapRing, 0, new AspectList().add(TOOL, 8).add(EXCHANGE, 8));
		registerComplex(ModItems.flowerBag, 0, new AspectList().add(VOID, 5));
		registerComplex(ModItems.phantomInk, 0, new AspectList().add(SENSES, 9).add(VOID, 4));
		register(ModItems.corporeaSpark, 0, new AspectList().add(AURA, 15).add(MOTION, 15).add(SENSES, 10).add(EXCHANGE, 15).add(ELDRITCH, 10));

		AspectList willAspects = new AspectList().add(MAGIC, 10).add(AVERSION, 10).add(SOUL, 10);
		register(ModItems.ancientWill, 0, willAspects.copy().add(PROTECT, 10)); //Ahrim - weakness
		register(ModItems.ancientWill, 1, willAspects.copy().add(BEAST, 10));   //Dharok - low HP damage boost
		register(ModItems.ancientWill, 2, willAspects.copy().add(UNDEAD, 10));  //Guthan - lifesteal
		register(ModItems.ancientWill, 3, willAspects.copy().add(TRAP, 10));    //Torag - slowness
		register(ModItems.ancientWill, 4, willAspects.copy().add(DEATH, 10));   //Verac - armor pierce
		register(ModItems.ancientWill, 5, willAspects.copy().add(FLUX, 10));    //Karil - withering

		register(ModItems.pinkinator, 0, new AspectList().add(LIFE, 30).add(ORDER, 20).add(EXCHANGE, 20));

		AspectList relicAspects = new AspectList().add(MAGIC, 30).add(DESIRE, 25).add(ELDRITCH, 15);
		register(ModItems.dice,          0, relicAspects.copy().add(ELDRITCH, 15));
		register(ModItems.infiniteFruit, 0, relicAspects.copy().add(LIFE,     50).add(PLANT,   30));
		register(ModItems.kingKey,       0, relicAspects.copy().add(AVERSION, 50).add(TRAP,    30));
		register(ModItems.flugelEye,     0, relicAspects.copy().add(MOTION,   50).add(FLIGHT,  30));
		register(ModItems.thorRing,      0, relicAspects.copy().add(TOOL,     50).add(ENTROPY, 30));
		register(ModItems.odinRing,      0, relicAspects.copy().add(PROTECT,  50).add(LIFE,    30));
		register(ModItems.lokiRing,      0, relicAspects.copy().add(AURA,     50).add(MAN,     30));

		register(ModItems.recordGaia1, 0, new AspectList().add(SENSES, 15).add(DESIRE, 10).add(AIR, 5).add(VOID, 5));     //Endure Emptiness
		register(ModItems.recordGaia2, 0, new AspectList().add(SENSES, 15).add(DESIRE, 10).add(AIR, 5).add(AVERSION, 5)); //Fight For Quiescence

		registerComplex(ModItems.blackHoleTalisman, 0, new AspectList().add(VOID, 30));
		registerComplex(ModItems.temperanceStone, ANY, new AspectList().add(TRAP, 5));
		registerComplex(ModItems.obedienceStick, 0, new AspectList().add(TOOL, 4));
		registerComplex(ModItems.slimeBottle, 0, new AspectList(new ItemStack(Items.SLIME_BALL)).add(SENSES, 10));
		registerComplex(ModItems.magnetRingGreater, 0, new AspectList().add(DESIRE, 15));
		registerComplex(ModItems.thunderSword, 0, new AspectList().add(ENERGY, 15));
		registerComplex(ModItems.autocraftingHalo, 0, new AspectList().add(CRAFT, 15).add(EXCHANGE, 10));
		register(ModItems.gaiaHead, 0, new AspectList().add(DEATH, 10).add(SOUL, 15).add(ELDRITCH, 10).add(EARTH, 10));
		registerComplex(ModItems.sextant, 0, new AspectList().add(TOOL, 8));
		registerComplex(ModItems.speedUpBelt, 0, new AspectList().add(MOTION, 10));
		registerComplex(ModItems.baubleBox, 0, new AspectList().add(VOID, 5));
		registerComplex(ModItems.dodgeRing, 0, new AspectList().add(MOTION, 10));
		registerComplex(ModItems.invisibilityCloak, 0, new AspectList().add(SENSES, 15));

		//Blocks
		registerComplex(ModBlocks.altar, 0, new AspectList().add(CRAFT, 10));
		registerComplex(ModBlocks.runeAltar, 0, new AspectList().add(CRAFT, 10).add(MAGIC, 5));

		registerComplex(ModBlocks.spreader, 0, new AspectList().add(MOTION, 10).add(MAGIC, 10));
		registerComplex(ModBlocks.spreader, 1, new AspectList(new ItemStack(ModBlocks.spreader)).add(ENERGY, 10).add(MECHANISM, 5));
		registerComplex(ModBlocks.spreader, 2, new AspectList().add(MOTION, 15).add(MAGIC, 15));
		registerComplex(ModBlocks.spreader, 3, new AspectList().add(MOTION, 8).add(MAGIC, 8).add(SENSES, 10));

		registerComplex(ModBlocks.pool, 0, new AspectList().add(VOID, 10).add(MAGIC, 5)); //Mana Pool
		registerComplex(ModBlocks.pool, 2, new AspectList().add(VOID, 3).add(MAGIC, 3)); //Diluted
		//Next entry is made with shimmerrock, and proper autoassignment depends on this
		register(ModBlocks.bifrostPerm, 0, new AspectList(new ItemStack(ModBlocks.elfGlass)).add(SENSES, 5));
		registerComplex(ModBlocks.pool, 3, new AspectList().add(VOID, 10).add(MAGIC, 5)); //Fabulous

		registerComplex(ModBlocks.distributor, 0, new AspectList().add(MECHANISM, 5).add(MAGIC, 5));
		registerComplex(ModBlocks.manaVoid, 0, new AspectList().add(VOID, 20));
		registerComplex(ModBlocks.manaDetector, 0, new AspectList().add(SENSES, 10));
		register(ModBlocks.enchanter, 0, new AspectList(new ItemStack(Blocks.LAPIS_BLOCK)).add(MAGIC, 25).add(CRAFT, 15));
		registerComplex(ModBlocks.tinyPlanet, 0, new AspectList().add(EARTH, 50));
		registerComplex(ModBlocks.openCrate, 0, new AspectList().add(VOID, 5));
		registerComplex(ModBlocks.openCrate, 1, new AspectList().add(MECHANISM, 5)); //Crafty Crate
		registerComplex(ModBlocks.forestEye, 0, new AspectList().add(SENSES, 10).add(BEAST, 10));
		registerComplex(ModBlocks.forestDrum, 1, new AspectList().add(SENSES, 5).add(TOOL, 5));
		register(ModBlocks.bifrost, 0, new AspectList(new ItemStack(ModBlocks.bifrostPerm)));
		registerComplex(ModBlocks.floatingFlower, ANY, new AspectList().add(FLIGHT, 5));
		registerComplex(ModBlocks.spawnerClaw, 0, new AspectList().add(LIFE, 30));
		registerComplex(ModBlocks.alfPortal, 0, new AspectList().merge(EXCHANGE, 20).add(ELDRITCH, 20));

		register(ModBlocks.customBrick, ANY, new AspectList(new ItemStack(Blocks.QUARTZ_BLOCK))
				.add(SENSES, 3).add(DESIRE, 1).add(EARTH, 1)); //Azulejo
		registerComplex(ModBlocks.enderEye, 0, new AspectList().add(MECHANISM, 10));
		registerComplex(ModBlocks.starfield, 0, new AspectList().add(DARKNESS, 20).add(LIGHT, 20));
		registerComplex(ModBlocks.rfGenerator, 0, new AspectList().add(MECHANISM, 20));
		registerComplex(ModBlocks.terraPlate, 0, new AspectList().add(CRAFT, 20).add(EXCHANGE, 20));
		register(ModBlocks.enchantedSoil, 0, new AspectList().add(LIFE, 20).add(MAGIC, 15).add(EARTH, 10));

		AspectList corporeaAspects = new AspectList(new ItemStack(ModItems.corporeaSpark)).add(MECHANISM, 15).merge(ELDRITCH, 15).remove(AURA);
		register(ModBlocks.corporeaIndex, 0, corporeaAspects.copy().add(DESIRE, 25).add(MIND, 25));
		register(ModBlocks.corporeaCrystalCube, 0, corporeaAspects.copy().add(SENSES, 15).add(CRYSTAL, 15));
		register(ModBlocks.corporeaFunnel, 0, corporeaAspects.copy().add(DESIRE, 20));
		register(ModBlocks.corporeaInterceptor, 0, corporeaAspects.copy().add(ENERGY, 20));
		register(ModBlocks.corporeaRetainer, 0, corporeaAspects.copy().add(VOID, 20));

		registerComplex(ModBlocks.pump, 0, new AspectList().add(MECHANISM, 10).add(EXCHANGE, 10));
		registerComplex(ModBlocks.incensePlate, 0, new AspectList().add(ALCHEMY, 15));
		registerComplex(ModBlocks.hourglass, 0, new AspectList().add(MECHANISM, 10));
		registerComplex(ModBlocks.ghostRail, 0, new AspectList().add(SOUL, 10));
		registerComplex(ModBlocks.sparkChanger, 0, new AspectList().add(MECHANISM, 10));
		register(ModBlocks.felPumpkin, 0, new AspectList().add(ENTROPY, 15).add(SOUL, 10).add(LIFE, 5).add(BEAST, 5).add(PLANT, 5));
		registerComplex(ModBlocks.cocoon, 0, new AspectList().add(LIFE, 10));
		registerComplex(ModBlocks.lightRelay, 0, new AspectList().add(MOTION, 15));
		register(ModBlocks.cacophonium, 0, new AspectList(new ItemStack(Blocks.NOTEBLOCK)).add(DESIRE, 10));
		registerComplex(ModBlocks.teruTeruBozu, 0, new AspectList().add(EXCHANGE, 10).add(SENSES, 10));
		registerComplex(ModBlocks.avatar, 0, new AspectList().add(MECHANISM, 15).add(MAN, 10));

		register(ModBlocks.altGrass, ANY, new AspectList(new ItemStack(Blocks.GRASS)).add(SENSES, 3));
		registerComplex(ModBlocks.animatedTorch, 0, new AspectList().add(MOTION, 5));

		//Livingrock Brick variants
		register(ModBlocks.livingrock, 2, new AspectList(new ItemStack(ModBlocks.livingrock, 1, 1)).add(PLANT, 3));   //Mossy
		register(ModBlocks.livingrock, 3, new AspectList(new ItemStack(ModBlocks.livingrock, 1, 1)).add(ENTROPY, 1)); //Cracked
		register(ModBlocks.livingrock, 4, new AspectList(new ItemStack(ModBlocks.livingrock, 1, 1)).add(ORDER, 1));   //Chiseled

		//Metamorphic stone from the Marimorphosis
		AspectList stoneAspects = new AspectList().add(EARTH, 5);
		register(ModFluffBlocks.biomeStoneA, 0, stoneAspects.copy().add(PLANT, 1));   //Forest
		register(ModFluffBlocks.biomeStoneA, 1, stoneAspects.copy().add(AIR, 1));     //Plains
		register(ModFluffBlocks.biomeStoneA, 2, stoneAspects.copy().add(EARTH, 1));   //Mountain
		register(ModFluffBlocks.biomeStoneA, 3, stoneAspects.copy().add(FLUX, 1));    //Fungal
		register(ModFluffBlocks.biomeStoneA, 4, stoneAspects.copy().add(WATER, 1));   //Swamp
		register(ModFluffBlocks.biomeStoneA, 5, stoneAspects.copy().add(ENTROPY, 1)); //Desert
		register(ModFluffBlocks.biomeStoneA, 6, stoneAspects.copy().add(COLD, 1));    //Taiga
		register(ModFluffBlocks.biomeStoneA, 7, stoneAspects.copy().add(FIRE, 1));    //Mesa

		//Metamorphic cobble and chiseled bricks
		for(int i = 0; i < 8; i++) {
			register(ModFluffBlocks.biomeStoneA, i + 8,
					new AspectList(new ItemStack(ModFluffBlocks.biomeStoneA, 1, i)).add(ENTROPY, 1));
			register(ModFluffBlocks.biomeStoneB, i + 8,
					new AspectList(new ItemStack(ModFluffBlocks.biomeStoneB, 1, i)).add(ORDER, 1));
		}

		//Garden of Glass
		if(Botania.gardenOfGlassLoaded) {
			register(ModItems.waterBowl, 0, new AspectList(new ItemStack(Items.BOWL)).add(WATER, 10));
			register(LibOreDict.PEBBLE, new AspectList().add(EARTH, 1));
			register(ModBlocks.root, 0, new AspectList().add(PLANT, 15).add(LIFE, 5));
			register(ModItems.manaResource, 20, new AspectList().add(PLANT, 5).add(LIFE, 2)); //Living Root item
		}
	}

	private final List<ItemStack> brewContainers = Arrays.asList(new ItemStack(ModItems.vial),
		new ItemStack(ModItems.vial, 1, 1), new ItemStack(ModItems.incenseStick), new ItemStack(ModItems.bloodPendant));

	private void registerBrewAspects() {
		for(RecipeBrew brewRecipe : BotaniaAPI.brewRecipes) {
			AspectList aspects = new AspectList();
			
			for(Object input : brewRecipe.getInputs()) {
				ItemStack toAdd = ItemStack.EMPTY;
				if(input instanceof ItemStack) {
					toAdd = (ItemStack) input;
				} else if(input instanceof String) {
					toAdd = OreDictionary.getOres((String) input).get(0);
				}
				AspectList ingredientAspects = AspectHelper.getObjectAspects(toAdd);
				for (Aspect a : ingredientAspects.getAspects())
					aspects.add(a, ingredientAspects.getAmount(a));
			}
			for(Aspect aspect : aspects.getAspects()) {
				aspects.reduce(aspect, aspects.getAmount(aspect) / 2);
			}
			aspects.add(ALCHEMY, 8);
			
			for(ItemStack brewContainer : brewContainers) {
				ItemStack brewed = brewRecipe.getOutput(brewContainer);
				if(!brewed.isEmpty()) {
					proxy.registerObjectTag(brewed, new AspectList().add(aspects));
				}
			}
		}
	}

	private void registerEntityAspects() {
		registerEntity(LibEntityNames.MANA_BURST,       new AspectList().add(ENERGY, 5).add(MOTION, 5).add(AURA, 5));
		registerEntity(LibEntityNames.PIXIE,            new AspectList().add(FLIGHT, 5).add(ELDRITCH, 5).add(LIGHT, 5));
		registerEntity(LibEntityNames.FLAME_RING,       new AspectList().add(FIRE, 20));
		registerEntity(LibEntityNames.DOPPLEGANGER,     new AspectList().add(MAN, 30).add(EARTH, 30).add(DARKNESS, 30).add(ELDRITCH, 30).add(PROTECT, 30));
		registerEntity(LibEntityNames.MAGIC_LANDMINE,   new AspectList().add(AVERSION, 10).add(DEATH, 10));
		registerEntity(LibEntityNames.MAGIC_MISSILE,    new AspectList().add(AVERSION, 5).add(MAGIC, 5));
		registerEntity(LibEntityNames.PINK_WITHER,      new AspectList().add(LIFE, 50).add(UNDEAD, 30).add(ORDER, 25).add(FIRE, 25));
		registerEntity(LibEntityNames.PLAYER_MOVER,     new AspectList().add(MOTION, 15).add(LIGHT, 5));
		registerEntity(LibEntityNames.MANA_STORM,       new AspectList().add(ENTROPY, 150).add(FIRE, 100).add(MAGIC, 50).add(ALCHEMY, 50));
		registerEntity(LibEntityNames.BABYLON_WEAPON,   new AspectList().add(AVERSION, 30).add(ELDRITCH, 20));
		registerEntity(LibEntityNames.FALLING_STAR,     new AspectList().add(AVERSION, 10).add(ELDRITCH, 10).add(LIGHT, 5));
	}

	private void registerFlower(String flowerId, AspectList extraAspects) {
		AspectList aspects = extraAspects.copy().add(PLANT, 15).add(SENSES, 15).add(MAGIC, 5);
		AspectList floatingAspects = aspects.copy().add(FLIGHT, 5).add(LIGHT, 5);

		proxy.registerObjectTag(ItemBlockSpecialFlower.ofType(flowerId), aspects);
		proxy.registerObjectTag(ItemBlockSpecialFlower.ofType(new ItemStack(ModBlocks.floatingSpecialFlower), flowerId), floatingAspects);

		if (BotaniaAPI.miniFlowers.containsKey(flowerId)) {
			String miniFlowerId = BotaniaAPI.miniFlowers.get(flowerId);
			proxy.registerObjectTag(ItemBlockSpecialFlower.ofType(miniFlowerId), petiteAspects(aspects));
			proxy.registerObjectTag(ItemBlockSpecialFlower.ofType(new ItemStack(ModBlocks.floatingSpecialFlower), miniFlowerId), petiteAspects(floatingAspects));
		}
	}

	private static AspectList petiteAspects(AspectList aspects) {
		AspectList list = new AspectList();
		for (Aspect a : aspects.getAspects())
			list.add(a, aspects.getAmount(a) / 2 + 1);
		return list;
	}

	//Convenience aspect registration methods
	private void register(Item item, int meta, AspectList aspects) {
		proxy.registerObjectTag(new ItemStack(item, 1, meta), aspects);
	}

	private void register(Block block, int meta, AspectList aspects) {
		proxy.registerObjectTag(new ItemStack(block, 1, meta), aspects);
	}

	private void register(String oreDict, AspectList aspects) {
		proxy.registerObjectTag(oreDict, aspects);
	}

	private void registerComplex(Item item, int meta, AspectList aspects) {
		proxy.registerComplexObjectTag(new ItemStack(item, 1, meta), aspects);
	}

	private void registerComplex(Block block, int meta, AspectList aspects) {
		proxy.registerComplexObjectTag(new ItemStack(block, 1, meta), aspects);
	}

	private void registerEntity(String entityName, AspectList aspects) {
		ThaumcraftApi.registerEntityTag(entityName, aspects); //Not in the event (as of TC beta 21)
	}
}
