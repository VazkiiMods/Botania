/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 5:17:47 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.common.item.equipment.armor.elementium.ItemElementiumBoots;
import vazkii.botania.common.item.equipment.armor.elementium.ItemElementiumChest;
import vazkii.botania.common.item.equipment.armor.elementium.ItemElementiumHelm;
import vazkii.botania.common.item.equipment.armor.elementium.ItemElementiumLegs;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelBoots;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelChest;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelHelm;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelLegs;
import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelBoots;
import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelChest;
import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelHelm;
import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelLegs;
import vazkii.botania.common.item.equipment.bauble.ItemAuraRing;
import vazkii.botania.common.item.equipment.bauble.ItemDivaCharm;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.item.equipment.bauble.ItemGoldenLaurel;
import vazkii.botania.common.item.equipment.bauble.ItemGreaterAuraRing;
import vazkii.botania.common.item.equipment.bauble.ItemGreaterManaRing;
import vazkii.botania.common.item.equipment.bauble.ItemIcePendant;
import vazkii.botania.common.item.equipment.bauble.ItemKnockbackBelt;
import vazkii.botania.common.item.equipment.bauble.ItemLavaPendant;
import vazkii.botania.common.item.equipment.bauble.ItemMagnetRing;
import vazkii.botania.common.item.equipment.bauble.ItemManaRing;
import vazkii.botania.common.item.equipment.bauble.ItemMiningRing;
import vazkii.botania.common.item.equipment.bauble.ItemPixieRing;
import vazkii.botania.common.item.equipment.bauble.ItemSuperTravelBelt;
import vazkii.botania.common.item.equipment.bauble.ItemTinyPlanet;
import vazkii.botania.common.item.equipment.bauble.ItemTravelBelt;
import vazkii.botania.common.item.equipment.bauble.ItemWaterRing;
import vazkii.botania.common.item.equipment.tool.ItemEnderDagger;
import vazkii.botania.common.item.equipment.tool.ItemTerraPick;
import vazkii.botania.common.item.equipment.tool.ItemTerraSword;
import vazkii.botania.common.item.equipment.tool.elementium.ItemElementiumAxe;
import vazkii.botania.common.item.equipment.tool.elementium.ItemElementiumPick;
import vazkii.botania.common.item.equipment.tool.elementium.ItemElementiumShears;
import vazkii.botania.common.item.equipment.tool.elementium.ItemElementiumShovel;
import vazkii.botania.common.item.equipment.tool.elementium.ItemElementiumSword;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelAxe;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelPick;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelShears;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelShovel;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword;
import vazkii.botania.common.lib.LibOreDict;

public final class ModItems {

	public static Item lexicon;
	public static Item petal;
	public static Item dye;
	public static Item pestleAndMortar;
	public static Item twigWand;
	public static Item manaResource;
	public static Item lens;
	public static Item manaPetal;
	public static Item rune;
	public static Item signalFlare;
	public static Item manaTablet;
	public static Item manaGun;
	public static Item manaCookie;
	public static Item fertilizer;
	public static Item grassSeeds;
	public static Item dirtRod;
	public static Item terraformRod;
	public static Item grassHorn;
	public static Item manaMirror;
	public static Item manasteelHelm;
	public static Item manasteelChest;
	public static Item manasteelLegs;
	public static Item manasteelBoots;
	public static Item manasteelPick;
	public static Item manasteelShovel;
	public static Item manasteelAxe;
	public static Item manasteelSword;
	public static Item manasteelShears;
	public static Item terrasteelHelm;
	public static Item terrasteelChest;
	public static Item terrasteelLegs;
	public static Item terrasteelBoots;
	public static Item terraSword;
	public static Item tinyPlanet;
	public static Item manaRing;
	public static Item auraRing;
	public static Item manaRingGreater;
	public static Item auraRingGreater;
	public static Item travelBelt;
	public static Item knockbackBelt;
	public static Item icePendant;
	public static Item lavaPendant;
	public static Item goldLaurel;
	public static Item magnetRing;
	public static Item waterRing;
	public static Item miningRing;
	public static Item terraPick;
	public static Item divaCharm;
	public static Item flightTiara;
	public static Item enderDagger;
	public static Item quartz;
	public static Item waterRod;
	public static Item elementiumHelm;
	public static Item elementiumChest;
	public static Item elementiumLegs;
	public static Item elementiumBoots;
	public static Item elementiumPick;
	public static Item elementiumShovel;
	public static Item elementiumAxe;
	public static Item elementiumSword;
	public static Item elementiumShears;
	public static Item openBucket;
	public static Item spawnerMover;
	public static Item pixieRing;
	public static Item superTravelBelt;
	public static Item rainbowRod;

	public static void init() {
		lexicon = new ItemLexicon();
		petal = new ItemPetal();
		dye = new ItemDye();
		pestleAndMortar = new ItemPestleAndMortar();
		twigWand = new ItemTwigWand();
		manaResource = new ItemManaResource();
		lens = new ItemLens();
		manaPetal = new ItemManaPetal();
		rune = new ItemRune();
		signalFlare = new ItemSignalFlare();
		manaTablet = new ItemManaTablet();
		manaGun = new ItemManaGun();
		manaCookie = new ItemManaCookie();
		fertilizer = new ItemFertilizer();
		grassSeeds = new ItemGrassSeeds();
		dirtRod = new ItemDirtRod();
		terraformRod = new ItemTerraformRod();
		grassHorn = new ItemGrassHorn();
		manaMirror = new ItemManaMirror();
		manasteelHelm = new ItemManasteelHelm();
		manasteelChest = new ItemManasteelChest();
		manasteelLegs = new ItemManasteelLegs();
		manasteelBoots = new ItemManasteelBoots();
		manasteelPick = new ItemManasteelPick();
		manasteelShovel = new ItemManasteelShovel();
		manasteelAxe = new ItemManasteelAxe();
		manasteelSword = new ItemManasteelSword();
		manasteelShears = new ItemManasteelShears();
		terrasteelHelm = new ItemTerrasteelHelm();
		terrasteelChest = new ItemTerrasteelChest();
		terrasteelLegs = new ItemTerrasteelLegs();
		terrasteelBoots = new ItemTerrasteelBoots();
		terraSword = new ItemTerraSword();
		tinyPlanet = new ItemTinyPlanet();
		manaRing = new ItemManaRing();
		auraRing = new ItemAuraRing();
		manaRingGreater = new ItemGreaterManaRing();
		auraRingGreater = new ItemGreaterAuraRing();
		travelBelt = new ItemTravelBelt();
		knockbackBelt = new ItemKnockbackBelt();
		icePendant = new ItemIcePendant();
		lavaPendant = new ItemLavaPendant();
		goldLaurel = new ItemGoldenLaurel();
		magnetRing = new ItemMagnetRing();
		waterRing = new ItemWaterRing();
		miningRing = new ItemMiningRing();
		terraPick = new ItemTerraPick();
		divaCharm = new ItemDivaCharm();
		flightTiara = new ItemFlightTiara();
		enderDagger	= new ItemEnderDagger();
		quartz = new ItemQuartz();
		waterRod = new ItemWaterRod();
		elementiumHelm = new ItemElementiumHelm();
		elementiumChest = new ItemElementiumChest();
		elementiumLegs = new ItemElementiumLegs();
		elementiumBoots = new ItemElementiumBoots();
		elementiumPick = new ItemElementiumPick();
		elementiumShovel = new ItemElementiumShovel();
		elementiumAxe = new ItemElementiumAxe();
		elementiumSword = new ItemElementiumSword();
		elementiumShears = new ItemElementiumShears();
		openBucket = new ItemOpenBucket();
		spawnerMover = new ItemSpawnerMover();
		pixieRing = new ItemPixieRing();
		superTravelBelt = new ItemSuperTravelBelt();
		rainbowRod = new ItemRainbowRod();

		OreDictionary.registerOre(LibOreDict.LEXICON, lexicon);
		for(int i = 0; i < 16; i++) {
			OreDictionary.registerOre(LibOreDict.PETAL[i], new ItemStack(petal, 1, i));
			OreDictionary.registerOre(LibOreDict.DYE[i], new ItemStack(dye, 1, i));
			OreDictionary.registerOre(LibOreDict.MANA_PETAL[i], new ItemStack(manaPetal, 1, i));
			OreDictionary.registerOre(LibOreDict.RUNE[i], new ItemStack(rune, 1, i));
		}
		OreDictionary.registerOre(LibOreDict.PESTLE_AND_MORTAR, pestleAndMortar);
		OreDictionary.registerOre(LibOreDict.MANA_STEEL, new ItemStack(manaResource, 1, 0));
		OreDictionary.registerOre(LibOreDict.MANA_PEARL, new ItemStack(manaResource, 1, 1));
		OreDictionary.registerOre(LibOreDict.MANA_DIAMOND, new ItemStack(manaResource, 1, 2));
		OreDictionary.registerOre(LibOreDict.LIVINGWOOD_TWIG, new ItemStack(manaResource, 1, 3));
		OreDictionary.registerOre(LibOreDict.TERRA_STEEL, new ItemStack(manaResource, 1, 4));
		OreDictionary.registerOre(LibOreDict.LIFE_ESSENCE, new ItemStack(manaResource, 1, 5));
		OreDictionary.registerOre(LibOreDict.REDSTONE_ROOT, new ItemStack(manaResource, 1, 6));
		OreDictionary.registerOre(LibOreDict.ELEMENTIUM, new ItemStack(manaResource, 1, 7));
		OreDictionary.registerOre(LibOreDict.PIXIE_DUST, new ItemStack(manaResource, 1, 8));
		OreDictionary.registerOre(LibOreDict.DRAGONSTONE, new ItemStack(manaResource, 1, 9));
	}
}
