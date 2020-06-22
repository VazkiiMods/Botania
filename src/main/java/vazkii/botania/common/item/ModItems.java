/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.botania.api.item.IAncientWillContainer;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.mana.spark.SparkUpgradeType;
import vazkii.botania.api.state.enums.CratePattern;
import vazkii.botania.client.gui.bag.ContainerFlowerBag;
import vazkii.botania.client.gui.bag.GuiFlowerBag;
import vazkii.botania.client.gui.box.ContainerBaubleBox;
import vazkii.botania.client.gui.box.GuiBaubleBox;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.crafting.FluxfieldCondition;
import vazkii.botania.common.crafting.FuzzyNBTIngredient;
import vazkii.botania.common.crafting.recipe.*;
import vazkii.botania.common.item.brew.ItemBrewBase;
import vazkii.botania.common.item.brew.ItemIncenseStick;
import vazkii.botania.common.item.brew.ItemVial;
import vazkii.botania.common.item.equipment.armor.elementium.ItemElementiumBoots;
import vazkii.botania.common.item.equipment.armor.elementium.ItemElementiumChest;
import vazkii.botania.common.item.equipment.armor.elementium.ItemElementiumHelm;
import vazkii.botania.common.item.equipment.armor.elementium.ItemElementiumLegs;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelHelm;
import vazkii.botania.common.item.equipment.armor.manaweave.ItemManaweaveArmor;
import vazkii.botania.common.item.equipment.armor.manaweave.ItemManaweaveHelm;
import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelArmor;
import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelHelm;
import vazkii.botania.common.item.equipment.bauble.*;
import vazkii.botania.common.item.equipment.tool.ItemEnderDagger;
import vazkii.botania.common.item.equipment.tool.ItemGlassPick;
import vazkii.botania.common.item.equipment.tool.ItemStarSword;
import vazkii.botania.common.item.equipment.tool.ItemThunderSword;
import vazkii.botania.common.item.equipment.tool.bow.ItemCrystalBow;
import vazkii.botania.common.item.equipment.tool.bow.ItemLivingwoodBow;
import vazkii.botania.common.item.equipment.tool.elementium.*;
import vazkii.botania.common.item.equipment.tool.manasteel.*;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraAxe;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraSword;
import vazkii.botania.common.item.interaction.thaumcraft.ItemElementiumHelmRevealing;
import vazkii.botania.common.item.interaction.thaumcraft.ItemManaInkwell;
import vazkii.botania.common.item.interaction.thaumcraft.ItemManasteelHelmRevealing;
import vazkii.botania.common.item.interaction.thaumcraft.ItemTerrasteelHelmRevealing;
import vazkii.botania.common.item.lens.*;
import vazkii.botania.common.item.material.*;
import vazkii.botania.common.item.record.ItemModRecord;
import vazkii.botania.common.item.relic.*;
import vazkii.botania.common.item.rod.*;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

import java.util.Locale;

import static vazkii.botania.common.block.ModBlocks.register;
import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@ObjectHolder(LibMisc.MOD_ID)
public final class ModItems {
	@ObjectHolder(LibItemNames.LEXICON) public static Item lexicon;
	@ObjectHolder("white" + LibItemNames.PETAL_SUFFIX) public static Item whitePetal;
	@ObjectHolder("orange" + LibItemNames.PETAL_SUFFIX) public static Item orangePetal;
	@ObjectHolder("magenta" + LibItemNames.PETAL_SUFFIX) public static Item magentaPetal;
	@ObjectHolder("light_blue" + LibItemNames.PETAL_SUFFIX) public static Item lightBluePetal;
	@ObjectHolder("yellow" + LibItemNames.PETAL_SUFFIX) public static Item yellowPetal;
	@ObjectHolder("lime" + LibItemNames.PETAL_SUFFIX) public static Item limePetal;
	@ObjectHolder("pink" + LibItemNames.PETAL_SUFFIX) public static Item pinkPetal;
	@ObjectHolder("gray" + LibItemNames.PETAL_SUFFIX) public static Item grayPetal;
	@ObjectHolder("light_gray" + LibItemNames.PETAL_SUFFIX) public static Item lightGrayPetal;
	@ObjectHolder("cyan" + LibItemNames.PETAL_SUFFIX) public static Item cyanPetal;
	@ObjectHolder("purple" + LibItemNames.PETAL_SUFFIX) public static Item purplePetal;
	@ObjectHolder("blue" + LibItemNames.PETAL_SUFFIX) public static Item bluePetal;
	@ObjectHolder("brown" + LibItemNames.PETAL_SUFFIX) public static Item brownPetal;
	@ObjectHolder("green" + LibItemNames.PETAL_SUFFIX) public static Item greenPetal;
	@ObjectHolder("red" + LibItemNames.PETAL_SUFFIX) public static Item redPetal;
	@ObjectHolder("black" + LibItemNames.PETAL_SUFFIX) public static Item blackPetal;

	@ObjectHolder("white" + LibItemNames.DYE_SUFFIX) public static Item whiteDye;
	@ObjectHolder("orange" + LibItemNames.DYE_SUFFIX) public static Item orangeDye;
	@ObjectHolder("magenta" + LibItemNames.DYE_SUFFIX) public static Item magentaDye;
	@ObjectHolder("light_blue" + LibItemNames.DYE_SUFFIX) public static Item lightBlueDye;
	@ObjectHolder("yellow" + LibItemNames.DYE_SUFFIX) public static Item yellowDye;
	@ObjectHolder("lime" + LibItemNames.DYE_SUFFIX) public static Item limeDye;
	@ObjectHolder("pink" + LibItemNames.DYE_SUFFIX) public static Item pinkDye;
	@ObjectHolder("gray" + LibItemNames.DYE_SUFFIX) public static Item grayDye;
	@ObjectHolder("light_gray" + LibItemNames.DYE_SUFFIX) public static Item lightGrayDye;
	@ObjectHolder("cyan" + LibItemNames.DYE_SUFFIX) public static Item cyanDye;
	@ObjectHolder("purple" + LibItemNames.DYE_SUFFIX) public static Item purpleDye;
	@ObjectHolder("blue" + LibItemNames.DYE_SUFFIX) public static Item blueDye;
	@ObjectHolder("brown" + LibItemNames.DYE_SUFFIX) public static Item brownDye;
	@ObjectHolder("green" + LibItemNames.DYE_SUFFIX) public static Item greenDye;
	@ObjectHolder("red" + LibItemNames.DYE_SUFFIX) public static Item redDye;
	@ObjectHolder("black" + LibItemNames.DYE_SUFFIX) public static Item blackDye;

	@ObjectHolder(LibItemNames.PESTLE_AND_MORTAR) public static Item pestleAndMortar;
	@ObjectHolder(LibItemNames.TWIG_WAND) public static Item twigWand;

	@ObjectHolder(LibItemNames.MANASTEEL_INGOT) public static Item manaSteel;
	@ObjectHolder(LibItemNames.MANA_PEARL) public static Item manaPearl;
	@ObjectHolder(LibItemNames.MANA_DIAMOND) public static Item manaDiamond;
	@ObjectHolder(LibItemNames.LIVINGWOOD_TWIG) public static Item livingwoodTwig;
	@ObjectHolder(LibItemNames.TERRASTEEL_INGOT) public static Item terrasteel;
	@ObjectHolder(LibItemNames.LIFE_ESSENCE) public static Item lifeEssence;
	@ObjectHolder(LibItemNames.REDSTONE_ROOT) public static Item redstoneRoot;
	@ObjectHolder(LibItemNames.ELEMENTIUM_INGOT) public static Item elementium;
	@ObjectHolder(LibItemNames.PIXIE_DUST) public static Item pixieDust;
	@ObjectHolder(LibItemNames.DRAGONSTONE) public static Item dragonstone;
	@ObjectHolder(LibItemNames.PLACEHOLDER) public static Item placeholder;
	@ObjectHolder(LibItemNames.RED_STRING) public static Item redString;
	@ObjectHolder(LibItemNames.DREAMWOOD_TWIG) public static Item dreamwoodTwig;
	@ObjectHolder(LibItemNames.GAIA_INGOT) public static Item gaiaIngot;
	@ObjectHolder(LibItemNames.ENDER_AIR_BOTTLE) public static Item enderAirBottle;
	@ObjectHolder(LibItemNames.MANA_STRING) public static Item manaString;
	@ObjectHolder(LibItemNames.MANASTEEL_NUGGET) public static Item manasteelNugget;
	@ObjectHolder(LibItemNames.TERRASTEEL_NUGGET) public static Item terrasteelNugget;
	@ObjectHolder(LibItemNames.ELEMENTIUM_NUGGET) public static Item elementiumNugget;
	@ObjectHolder(LibItemNames.LIVING_ROOT) public static Item livingroot;
	@ObjectHolder(LibItemNames.PEBBLE) public static Item pebble;
	@ObjectHolder(LibItemNames.MANAWEAVE_CLOTH) public static Item manaweaveCloth;
	@ObjectHolder(LibItemNames.MANA_POWDER) public static Item manaPowder;

	@ObjectHolder(LibItemNames.LENS_NORMAL) public static Item lensNormal;
	@ObjectHolder(LibItemNames.LENS_SPEED) public static Item lensSpeed;
	@ObjectHolder(LibItemNames.LENS_POWER) public static Item lensPower;
	@ObjectHolder(LibItemNames.LENS_TIME) public static Item lensTime;
	@ObjectHolder(LibItemNames.LENS_EFFICIENCY) public static Item lensEfficiency;
	@ObjectHolder(LibItemNames.LENS_BOUNCE) public static Item lensBounce;
	@ObjectHolder(LibItemNames.LENS_GRAVITY) public static Item lensGravity;
	@ObjectHolder(LibItemNames.LENS_MINE) public static Item lensMine;
	@ObjectHolder(LibItemNames.LENS_DAMAGE) public static Item lensDamage;
	@ObjectHolder(LibItemNames.LENS_PHANTOM) public static Item lensPhantom;
	@ObjectHolder(LibItemNames.LENS_MAGNET) public static Item lensMagnet;
	@ObjectHolder(LibItemNames.LENS_EXPLOSIVE) public static Item lensExplosive;
	@ObjectHolder(LibItemNames.LENS_INFLUENCE) public static Item lensInfluence;
	@ObjectHolder(LibItemNames.LENS_WEIGHT) public static Item lensWeight;
	@ObjectHolder(LibItemNames.LENS_PAINT) public static Item lensPaint;
	@ObjectHolder(LibItemNames.LENS_FIRE) public static Item lensFire;
	@ObjectHolder(LibItemNames.LENS_PISTON) public static Item lensPiston;
	@ObjectHolder(LibItemNames.LENS_LIGHT) public static Item lensLight;
	@ObjectHolder(LibItemNames.LENS_WARP) public static Item lensWarp;
	@ObjectHolder(LibItemNames.LENS_REDIRECT) public static Item lensRedirect;
	@ObjectHolder(LibItemNames.LENS_FIREWORK) public static Item lensFirework;
	@ObjectHolder(LibItemNames.LENS_FLARE) public static Item lensFlare;
	@ObjectHolder(LibItemNames.LENS_MESSENGER) public static Item lensMessenger;
	@ObjectHolder(LibItemNames.LENS_TRIPWIRE) public static Item lensTripwire;
	@ObjectHolder(LibItemNames.LENS_STORM) public static Item lensStorm;

	@ObjectHolder(LibItemNames.RUNE_WATER) public static Item runeWater;
	@ObjectHolder(LibItemNames.RUNE_FIRE) public static Item runeFire;
	@ObjectHolder(LibItemNames.RUNE_EARTH) public static Item runeEarth;
	@ObjectHolder(LibItemNames.RUNE_AIR) public static Item runeAir;
	@ObjectHolder(LibItemNames.RUNE_SPRING) public static Item runeSpring;
	@ObjectHolder(LibItemNames.RUNE_SUMMER) public static Item runeSummer;
	@ObjectHolder(LibItemNames.RUNE_AUTUMN) public static Item runeAutumn;
	@ObjectHolder(LibItemNames.RUNE_WINTER) public static Item runeWinter;
	@ObjectHolder(LibItemNames.RUNE_MANA) public static Item runeMana;
	@ObjectHolder(LibItemNames.RUNE_LUST) public static Item runeLust;
	@ObjectHolder(LibItemNames.RUNE_GLUTTONY) public static Item runeGluttony;
	@ObjectHolder(LibItemNames.RUNE_GREED) public static Item runeGreed;
	@ObjectHolder(LibItemNames.RUNE_SLOTH) public static Item runeSloth;
	@ObjectHolder(LibItemNames.RUNE_WRATH) public static Item runeWrath;
	@ObjectHolder(LibItemNames.RUNE_ENVY) public static Item runeEnvy;
	@ObjectHolder(LibItemNames.RUNE_PRIDE) public static Item runePride;

	@ObjectHolder(LibItemNames.MANA_TABLET) public static Item manaTablet;
	@ObjectHolder(LibItemNames.MANA_GUN) public static Item manaGun;
	@ObjectHolder(LibItemNames.MANA_COOKIE) public static Item manaCookie;
	@ObjectHolder(LibItemNames.FERTILIZER) public static Item fertilizer;

	@ObjectHolder(LibItemNames.GRASS_SEEDS) public static Item grassSeeds;
	@ObjectHolder(LibItemNames.PODZOL_SEEDS) public static Item podzolSeeds;
	@ObjectHolder(LibItemNames.MYCEL_SEEDS) public static Item mycelSeeds;
	@ObjectHolder(LibItemNames.DRY_SEEDS) public static Item drySeeds;
	@ObjectHolder(LibItemNames.GOLDEN_SEEDS) public static Item goldenSeeds;
	@ObjectHolder(LibItemNames.VIVID_SEEDS) public static Item vividSeeds;
	@ObjectHolder(LibItemNames.SCORCHED_SEEDS) public static Item scorchedSeeds;
	@ObjectHolder(LibItemNames.INFUSED_SEEDS) public static Item infusedSeeds;
	@ObjectHolder(LibItemNames.MUTATED_SEEDS) public static Item mutatedSeeds;

	@ObjectHolder(LibItemNames.DIRT_ROD) public static Item dirtRod;
	@ObjectHolder(LibItemNames.TERRAFORM_ROD) public static Item terraformRod;
	@ObjectHolder(LibItemNames.GRASS_HORN) public static Item grassHorn;
	@ObjectHolder(LibItemNames.LEAVES_HORN) public static Item leavesHorn;
	@ObjectHolder(LibItemNames.SNOW_HORN) public static Item snowHorn;
	@ObjectHolder(LibItemNames.MANA_MIRROR) public static Item manaMirror;
	@ObjectHolder(LibItemNames.MANASTEEL_HELM) public static Item manasteelHelm;
	@ObjectHolder(LibItemNames.MANASTEEL_HELM_R) public static Item manasteelHelmRevealing;
	@ObjectHolder(LibItemNames.MANASTEEL_CHEST) public static Item manasteelChest;
	@ObjectHolder(LibItemNames.MANASTEEL_LEGS) public static Item manasteelLegs;
	@ObjectHolder(LibItemNames.MANASTEEL_BOOTS) public static Item manasteelBoots;
	@ObjectHolder(LibItemNames.MANASTEEL_PICK) public static Item manasteelPick;
	@ObjectHolder(LibItemNames.MANASTEEL_SHOVEL) public static Item manasteelShovel;
	@ObjectHolder(LibItemNames.MANASTEEL_AXE) public static Item manasteelAxe;
	@ObjectHolder(LibItemNames.MANASTEEL_SWORD) public static Item manasteelSword;
	@ObjectHolder(LibItemNames.MANASTEEL_SHEARS) public static Item manasteelShears;
	@ObjectHolder(LibItemNames.TERRASTEEL_HELM) public static Item terrasteelHelm;
	@ObjectHolder(LibItemNames.TERRASTEEL_HELM_R) public static Item terrasteelHelmRevealing;
	@ObjectHolder(LibItemNames.TERRASTEEL_CHEST) public static Item terrasteelChest;
	@ObjectHolder(LibItemNames.TERRASTEEL_LEGS) public static Item terrasteelLegs;
	@ObjectHolder(LibItemNames.TERRASTEEL_BOOTS) public static Item terrasteelBoots;
	@ObjectHolder(LibItemNames.TERRA_SWORD) public static Item terraSword;
	@ObjectHolder(LibItemNames.TERRA_PICK) public static Item terraPick;
	@ObjectHolder(LibItemNames.TERRA_AXE) public static Item terraAxe;
	@ObjectHolder(LibItemNames.TINY_PLANET) public static Item tinyPlanet;
	@ObjectHolder(LibItemNames.MANA_RING) public static Item manaRing;
	@ObjectHolder(LibItemNames.AURA_RING) public static Item auraRing;
	@ObjectHolder(LibItemNames.MANA_RING_GREATER) public static Item manaRingGreater;
	@ObjectHolder(LibItemNames.AURA_RING_GREATER) public static Item auraRingGreater;
	@ObjectHolder(LibItemNames.TRAVEL_BELT) public static Item travelBelt;
	@ObjectHolder(LibItemNames.KNOCKBACK_BELT) public static Item knockbackBelt;
	@ObjectHolder(LibItemNames.ICE_PENDANT) public static Item icePendant;
	@ObjectHolder(LibItemNames.LAVA_PENDANT) public static Item lavaPendant;
	@ObjectHolder(LibItemNames.MAGNET_RING) public static Item magnetRing;
	@ObjectHolder(LibItemNames.WATER_RING) public static Item waterRing;
	@ObjectHolder(LibItemNames.MINING_RING) public static Item miningRing;
	@ObjectHolder(LibItemNames.DIVA_CHARM) public static Item divaCharm;
	@ObjectHolder(LibItemNames.FLIGHT_TIARA) public static Item flightTiara;
	@ObjectHolder(LibItemNames.ENDER_DAGGER) public static Item enderDagger; // What you looking at?
	@ObjectHolder(LibItemNames.QUARTZ_DARK) public static Item darkQuartz;
	@ObjectHolder(LibItemNames.QUARTZ_MANA) public static Item manaQuartz;
	@ObjectHolder(LibItemNames.QUARTZ_BLAZE) public static Item blazeQuartz;
	@ObjectHolder(LibItemNames.QUARTZ_LAVENDER) public static Item lavenderQuartz;
	@ObjectHolder(LibItemNames.QUARTZ_RED) public static Item redQuartz;
	@ObjectHolder(LibItemNames.QUARTZ_ELVEN) public static Item elfQuartz;
	@ObjectHolder(LibItemNames.QUARTZ_SUNNY) public static Item sunnyQuartz;
	@ObjectHolder(LibItemNames.WATER_ROD) public static Item waterRod;
	@ObjectHolder(LibItemNames.ELEMENTIUM_HELM) public static Item elementiumHelm;
	@ObjectHolder(LibItemNames.ELEMENTIUM_HELM_R) public static Item elementiumHelmRevealing;
	@ObjectHolder(LibItemNames.ELEMENTIUM_CHEST) public static Item elementiumChest;
	@ObjectHolder(LibItemNames.ELEMENTIUM_LEGS) public static Item elementiumLegs;
	@ObjectHolder(LibItemNames.ELEMENTIUM_BOOTS) public static Item elementiumBoots;
	@ObjectHolder(LibItemNames.ELEMENTIUM_PICK) public static Item elementiumPick;
	@ObjectHolder(LibItemNames.ELEMENTIUM_SHOVEL) public static Item elementiumShovel;
	@ObjectHolder(LibItemNames.ELEMENTIUM_AXE) public static Item elementiumAxe;
	@ObjectHolder(LibItemNames.ELEMENTIUM_SWORD) public static Item elementiumSword;
	@ObjectHolder(LibItemNames.ELEMENTIUM_SHEARS) public static Item elementiumShears;
	@ObjectHolder(LibItemNames.OPEN_BUCKET) public static Item openBucket;
	@ObjectHolder(LibItemNames.SPAWNER_MOVER) public static Item spawnerMover;
	@ObjectHolder(LibItemNames.PIXIE_RING) public static Item pixieRing;
	@ObjectHolder(LibItemNames.SUPER_TRAVEL_BELT) public static Item superTravelBelt;
	@ObjectHolder(LibItemNames.RAINBOW_ROD) public static Item rainbowRod;
	@ObjectHolder(LibItemNames.TORNADO_ROD) public static Item tornadoRod;
	@ObjectHolder(LibItemNames.FIRE_ROD) public static Item fireRod;
	@ObjectHolder(LibItemNames.VINE_BALL) public static Item vineBall;
	@ObjectHolder(LibItemNames.SLINGSHOT) public static Item slingshot;
	@ObjectHolder(LibItemNames.MANA_BOTTLE) public static Item manaBottle;
	@ObjectHolder(LibItemNames.LAPUTA_SHARD) public static Item laputaShard;
	@ObjectHolder(LibItemNames.NECRO_VIRUS) public static Item necroVirus;
	@ObjectHolder(LibItemNames.NULL_VIRUS) public static Item nullVirus;
	@ObjectHolder(LibItemNames.REACH_RING) public static Item reachRing;
	@ObjectHolder(LibItemNames.SKY_DIRT_ROD) public static Item skyDirtRod;
	@ObjectHolder(LibItemNames.ITEM_FINDER) public static Item itemFinder;
	@ObjectHolder(LibItemNames.SUPER_LAVA_PENDANT) public static Item superLavaPendant;
	@ObjectHolder(LibItemNames.ENDER_HAND) public static Item enderHand;
	@ObjectHolder(LibItemNames.GLASS_PICK) public static Item glassPick;
	@ObjectHolder(LibItemNames.SPARK) public static Item spark;
	@ObjectHolder(LibItemNames.SPARK_UPGRADE + "_dispersive") public static Item sparkUpgradeDispersive;
	@ObjectHolder(LibItemNames.SPARK_UPGRADE + "_dominant") public static Item sparkUpgradeDominant;
	@ObjectHolder(LibItemNames.SPARK_UPGRADE + "_recessive") public static Item sparkUpgradeRecessive;
	@ObjectHolder(LibItemNames.SPARK_UPGRADE + "_isolated") public static Item sparkUpgradeIsolated;
	@ObjectHolder(LibItemNames.DIVINING_ROD) public static Item diviningRod;
	@ObjectHolder(LibItemNames.GRAVITY_ROD) public static Item gravityRod;
	@ObjectHolder(LibItemNames.MANA_INKWELL) public static Item manaInkwell;
	@ObjectHolder(LibItemNames.VIAL) public static Item vial;
	@ObjectHolder(LibItemNames.FLASK) public static Item flask;
	@ObjectHolder(LibItemNames.BREW_VIAL) public static Item brewVial;
	@ObjectHolder(LibItemNames.BREW_FLASK) public static Item brewFlask;
	@ObjectHolder(LibItemNames.BLOOD_PENDANT) public static Item bloodPendant;
	@ObjectHolder(LibItemNames.MISSILE_ROD) public static Item missileRod;
	@ObjectHolder(LibItemNames.HOLY_CLOAK) public static Item holyCloak;
	@ObjectHolder(LibItemNames.UNHOLY_CLOAK) public static Item unholyCloak;
	@ObjectHolder(LibItemNames.BALANCE_CLOAK) public static Item balanceCloak;
	@ObjectHolder(LibItemNames.CRAFTING_HALO) public static Item craftingHalo;
	@ObjectHolder(LibItemNames.BLACK_LOTUS) public static Item blackLotus;
	@ObjectHolder(LibItemNames.BLACKER_LOTUS) public static Item blackerLotus;
	@ObjectHolder(LibItemNames.MONOCLE) public static Item monocle;
	@ObjectHolder(LibItemNames.CLIP) public static Item clip;
	@ObjectHolder(LibItemNames.COBBLE_ROD) public static Item cobbleRod;
	@ObjectHolder(LibItemNames.SMELT_ROD) public static Item smeltRod;
	@ObjectHolder(LibItemNames.WORLD_SEED) public static Item worldSeed;
	@ObjectHolder(LibItemNames.SPELL_CLOTH) public static Item spellCloth;
	@ObjectHolder(LibItemNames.THORN_CHAKRAM) public static Item thornChakram;
	@ObjectHolder(LibItemNames.FLARE_CHAKRAM) public static Item flareChakram;
	@ObjectHolder(LibItemNames.OVERGROWTH_SEED) public static Item overgrowthSeed;
	@ObjectHolder(LibItemNames.CRAFT_PATTERN_PREFIX + "1_1") public static Item craftPattern1_1;
	@ObjectHolder(LibItemNames.CRAFT_PATTERN_PREFIX + "2_2") public static Item craftPattern2_2;
	@ObjectHolder(LibItemNames.CRAFT_PATTERN_PREFIX + "1_2") public static Item craftPattern1_2;
	@ObjectHolder(LibItemNames.CRAFT_PATTERN_PREFIX + "2_1") public static Item craftPattern2_1;
	@ObjectHolder(LibItemNames.CRAFT_PATTERN_PREFIX + "1_3") public static Item craftPattern1_3;
	@ObjectHolder(LibItemNames.CRAFT_PATTERN_PREFIX + "3_1") public static Item craftPattern3_1;
	@ObjectHolder(LibItemNames.CRAFT_PATTERN_PREFIX + "2_3") public static Item craftPattern2_3;
	@ObjectHolder(LibItemNames.CRAFT_PATTERN_PREFIX + "3_2") public static Item craftPattern3_2;
	@ObjectHolder(LibItemNames.CRAFT_PATTERN_PREFIX + "donut") public static Item craftPatternDonut;
	@ObjectHolder(LibItemNames.ANCIENT_WILL_PREFIX + "ahrim") public static Item ancientWillAhrim;
	@ObjectHolder(LibItemNames.ANCIENT_WILL_PREFIX + "dharok") public static Item ancientWillDharok;
	@ObjectHolder(LibItemNames.ANCIENT_WILL_PREFIX + "guthan") public static Item ancientWillGuthan;
	@ObjectHolder(LibItemNames.ANCIENT_WILL_PREFIX + "torag") public static Item ancientWillTorag;
	@ObjectHolder(LibItemNames.ANCIENT_WILL_PREFIX + "verac") public static Item ancientWillVerac;
	@ObjectHolder(LibItemNames.ANCIENT_WILL_PREFIX + "karil") public static Item ancientWillKaril;
	@ObjectHolder(LibItemNames.CORPOREA_SPARK) public static Item corporeaSpark;
	@ObjectHolder(LibItemNames.CORPOREA_SPARK_MASTER) public static Item corporeaSparkMaster;
	@ObjectHolder(LibItemNames.LIVINGWOOD_BOW) public static Item livingwoodBow;
	@ObjectHolder(LibItemNames.CRYSTAL_BOW) public static Item crystalBow;

	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "black_bowtie") public static Item blackBowtie;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "black_tie") public static Item blackTie;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "red_glasses") public static Item redGlasses;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "puffy_scarf") public static Item puffyScarf;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "engineer_goggles") public static Item engineerGoggles;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "eyepatch") public static Item eyepatch;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "wicked_eyepatch") public static Item wickedEyepatch;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "red_ribbons") public static Item redRibbons;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "pink_flower_bud") public static Item pinkFlowerBud;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "polka_dotted_bows") public static Item polkaDottedBows;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "blue_butterfly") public static Item blueButterfly;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "cat_ears") public static Item catEars;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "witch_pin") public static Item witchPin;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "devil_tail") public static Item devilTail;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "kamui_eye") public static Item kamuiEye;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "googly_eyes") public static Item googlyEyes;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "four_leaf_clover") public static Item fourLeafClover;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "clock_eye") public static Item clockEye;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "unicorn_horn") public static Item unicornHorn;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "devil_horns") public static Item devilHorns;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "hyper_plus") public static Item hyperPlus;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "botanist_emblem") public static Item botanistEmblem;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "ancient_mask") public static Item ancientMask;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "eerie_mask") public static Item eerieMask;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "alien_antenna") public static Item alienAntenna;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "anaglyph_glasses") public static Item anaglyphGlasses;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "orange_shades") public static Item orangeShades;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "groucho_glasses") public static Item grouchoGlasses;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "thick_eyebrows") public static Item thickEyebrows;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "lusitanic_shield") public static Item lusitanicShield;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "tiny_potato_mask") public static Item tinyPotatoMask;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "questgiver_mark") public static Item questgiverMark;
	@ObjectHolder(LibItemNames.COSMETIC_PREFIX + "thinking_hand") public static Item thinkingHand;

	@ObjectHolder(LibItemNames.SWAP_RING) public static Item swapRing;
	@ObjectHolder(LibItemNames.FLOWER_BAG) public static Item flowerBag;
	@ObjectHolder(LibItemNames.PHANTOM_INK) public static Item phantomInk;
	@ObjectHolder(LibItemNames.POOL_MINECART) public static Item poolMinecart;
	@ObjectHolder(LibItemNames.PINKINATOR) public static Item pinkinator;
	@ObjectHolder(LibItemNames.INFINITE_FRUIT) public static Item infiniteFruit;
	@ObjectHolder(LibItemNames.KING_KEY) public static Item kingKey;
	@ObjectHolder(LibItemNames.FLUGEL_EYE) public static Item flugelEye;
	@ObjectHolder(LibItemNames.THOR_RING) public static Item thorRing;
	@ObjectHolder(LibItemNames.ODIN_RING) public static Item odinRing;
	@ObjectHolder(LibItemNames.LOKI_RING) public static Item lokiRing;
	@ObjectHolder(LibItemNames.DICE) public static Item dice;
	@ObjectHolder(LibItemNames.KEEP_IVY) public static Item keepIvy;
	@ObjectHolder(LibItemNames.BLACK_HOLE_TALISMAN) public static Item blackHoleTalisman;
	@ObjectHolder(LibItemNames.RECORD_GAIA1) public static Item recordGaia1;
	@ObjectHolder(LibItemNames.RECORD_GAIA2) public static Item recordGaia2;
	@ObjectHolder(LibItemNames.TEMPERANCE_STONE) public static Item temperanceStone;
	@ObjectHolder(LibItemNames.INCENSE_STICK) public static Item incenseStick;
	@ObjectHolder(LibItemNames.WATER_BOWL) public static Item waterBowl;
	@ObjectHolder(LibItemNames.OBEDIENCE_STICK) public static Item obedienceStick;
	@ObjectHolder(LibItemNames.CACOPHONIUM) public static Item cacophonium;
	@ObjectHolder(LibItemNames.SLIME_BOTTLE) public static Item slimeBottle;
	@ObjectHolder(LibItemNames.STAR_SWORD) public static Item starSword;
	@ObjectHolder(LibItemNames.EXCHANGE_ROD) public static Item exchangeRod;
	@ObjectHolder(LibItemNames.MAGNET_RING_GREATER) public static Item magnetRingGreater;
	@ObjectHolder(LibItemNames.THUNDER_SWORD) public static Item thunderSword;
	@ObjectHolder(LibItemNames.MANAWEAVE_HELM) public static Item manaweaveHelm;
	@ObjectHolder(LibItemNames.MANAWEAVE_CHEST) public static Item manaweaveChest;
	@ObjectHolder(LibItemNames.MANAWEAVE_LEGS) public static Item manaweaveLegs;
	@ObjectHolder(LibItemNames.MANAWEAVE_BOOTS) public static Item manaweaveBoots;
	@ObjectHolder(LibItemNames.AUTOCRAFTING_HALO) public static Item autocraftingHalo;
	@ObjectHolder(LibItemNames.SEXTANT) public static Item sextant;
	@ObjectHolder(LibItemNames.SPEED_UP_BELT) public static Item speedUpBelt;
	@ObjectHolder(LibItemNames.BAUBLE_BOX) public static Item baubleBox;
	@ObjectHolder(LibItemNames.DODGE_RING) public static Item dodgeRing;
	@ObjectHolder(LibItemNames.INVISIBILITY_CLOAK) public static Item invisibilityCloak;
	@ObjectHolder(LibItemNames.CLOUD_PENDANT) public static Item cloudPendant;
	@ObjectHolder(LibItemNames.SUPER_CLOUD_PENDANT) public static Item superCloudPendant;
	@ObjectHolder(LibItemNames.THIRD_EYE) public static Item thirdEye;
	@ObjectHolder(LibItemNames.ASTROLABE) public static Item astrolabe;
	@ObjectHolder(LibItemNames.GODDESS_CHARM) public static Item goddessCharm;

	public static Item.Properties defaultBuilder() {
		return new Item.Properties().group(BotaniaCreativeTab.INSTANCE);
	}

	private static Item.Properties unstackable() {
		return defaultBuilder().maxStackSize(1);
	}

	public static void registerItems(RegistryEvent.Register<Item> evt) {
		IForgeRegistry<Item> r = evt.getRegistry();

		register(r, LibItemNames.LEXICON, new ItemLexicon(unstackable().rarity(Rarity.UNCOMMON)));
		for (DyeColor color : DyeColor.values()) {
			register(r, color.getName() + LibItemNames.PETAL_SUFFIX, new ItemPetal(ModBlocks.getBuriedPetal(color), color, defaultBuilder()));
			register(r, color.getName() + LibItemNames.DYE_SUFFIX, new ItemDye(color, defaultBuilder()));
		}
		register(r, LibItemNames.PESTLE_AND_MORTAR, new ItemSelfReturning(unstackable()));
		register(r, LibItemNames.TWIG_WAND, new ItemTwigWand(unstackable().rarity(Rarity.RARE)));
		register(r, LibItemNames.MANASTEEL_INGOT, new Item(defaultBuilder()));
		register(r, LibItemNames.MANA_PEARL, new Item(defaultBuilder()));
		register(r, LibItemNames.MANA_DIAMOND, new Item(defaultBuilder()));
		register(r, LibItemNames.LIVINGWOOD_TWIG, new Item(defaultBuilder()));
		register(r, LibItemNames.TERRASTEEL_INGOT, new ItemManaResource(defaultBuilder()));
		register(r, LibItemNames.LIFE_ESSENCE, new Item(defaultBuilder()));
		register(r, LibItemNames.REDSTONE_ROOT, new Item(defaultBuilder()));
		register(r, LibItemNames.ELEMENTIUM_INGOT, new ItemElven(defaultBuilder()));
		register(r, LibItemNames.PIXIE_DUST, new ItemElven(defaultBuilder()));
		register(r, LibItemNames.DRAGONSTONE, new ItemElven(defaultBuilder()));
		register(r, LibItemNames.PLACEHOLDER, new ItemSelfReturning(defaultBuilder()));
		register(r, LibItemNames.RED_STRING, new Item(defaultBuilder()));
		register(r, LibItemNames.DREAMWOOD_TWIG, new Item(defaultBuilder()));
		register(r, LibItemNames.GAIA_INGOT, new ItemManaResource(defaultBuilder()));
		register(r, LibItemNames.ENDER_AIR_BOTTLE, new ItemEnderAir(defaultBuilder()));
		register(r, LibItemNames.MANA_STRING, new Item(defaultBuilder()));
		register(r, LibItemNames.MANASTEEL_NUGGET, new Item(defaultBuilder()));
		register(r, LibItemNames.TERRASTEEL_NUGGET, new Item(defaultBuilder()));
		register(r, LibItemNames.ELEMENTIUM_NUGGET, new Item(defaultBuilder()));
		register(r, LibItemNames.LIVING_ROOT, new ItemManaResource(defaultBuilder()));
		register(r, LibItemNames.PEBBLE, new Item(defaultBuilder()));
		register(r, LibItemNames.MANAWEAVE_CLOTH, new Item(defaultBuilder()));
		register(r, LibItemNames.MANA_POWDER, new Item(defaultBuilder()));
		register(r, LibItemNames.LENS_NORMAL, new ItemLens(unstackable(), new Lens(), ItemLens.PROP_NONE));
		register(r, LibItemNames.LENS_SPEED, new ItemLens(unstackable(), new LensSpeed(), ItemLens.PROP_NONE));
		register(r, LibItemNames.LENS_POWER, new ItemLens(unstackable(), new LensPower(), ItemLens.PROP_POWER));
		register(r, LibItemNames.LENS_TIME, new ItemLens(unstackable(), new LensTime(), ItemLens.PROP_NONE));
		register(r, LibItemNames.LENS_EFFICIENCY, new ItemLens(unstackable(), new LensEfficiency(), ItemLens.PROP_NONE));
		register(r, LibItemNames.LENS_BOUNCE, new ItemLens(unstackable(), new LensBounce(), ItemLens.PROP_TOUCH));
		register(r, LibItemNames.LENS_GRAVITY, new ItemLens(unstackable(), new LensGravity(), ItemLens.PROP_ORIENTATION));
		register(r, LibItemNames.LENS_MINE, new ItemLens(unstackable(), new LensMine(), ItemLens.PROP_TOUCH | ItemLens.PROP_ORIENTATION));
		register(r, LibItemNames.LENS_DAMAGE, new ItemLens(unstackable(), new LensDamage(), ItemLens.PROP_DAMAGE));
		register(r, LibItemNames.LENS_PHANTOM, new ItemLens(unstackable(), new LensPhantom(), ItemLens.PROP_TOUCH));
		register(r, LibItemNames.LENS_MAGNET, new ItemLens(unstackable(), new LensMagnet(), ItemLens.PROP_ORIENTATION));
		register(r, LibItemNames.LENS_EXPLOSIVE, new ItemLens(unstackable(), new LensExplosive(), ItemLens.PROP_DAMAGE | ItemLens.PROP_TOUCH | ItemLens.PROP_INTERACTION));
		register(r, LibItemNames.LENS_INFLUENCE, new ItemLens(unstackable(), new LensInfluence(), ItemLens.PROP_NONE));
		register(r, LibItemNames.LENS_WEIGHT, new ItemLens(unstackable(), new LensWeight(), ItemLens.PROP_TOUCH | ItemLens.PROP_INTERACTION));
		register(r, LibItemNames.LENS_PAINT, new ItemLens(unstackable(), new LensPaint(), ItemLens.PROP_TOUCH | ItemLens.PROP_INTERACTION));
		register(r, LibItemNames.LENS_FIRE, new ItemLens(unstackable(), new LensFire(), ItemLens.PROP_DAMAGE | ItemLens.PROP_TOUCH | ItemLens.PROP_INTERACTION));
		register(r, LibItemNames.LENS_PISTON, new ItemLens(unstackable(), new LensPiston(), ItemLens.PROP_TOUCH | ItemLens.PROP_INTERACTION));
		register(r, LibItemNames.LENS_LIGHT, new ItemLens(unstackable(), new LensLight(), ItemLens.PROP_TOUCH | ItemLens.PROP_INTERACTION));
		register(r, LibItemNames.LENS_WARP, new ItemLens(unstackable(), new LensWarp(), ItemLens.PROP_NONE));
		register(r, LibItemNames.LENS_REDIRECT, new ItemLens(unstackable(), new LensRedirect(), ItemLens.PROP_TOUCH | ItemLens.PROP_INTERACTION));
		register(r, LibItemNames.LENS_FIREWORK, new ItemLens(unstackable(), new LensFirework(), ItemLens.PROP_TOUCH));
		register(r, LibItemNames.LENS_FLARE, new ItemLens(unstackable(), new LensFlare(), ItemLens.PROP_CONTROL));
		register(r, LibItemNames.LENS_MESSENGER, new ItemLens(unstackable(), new LensMessenger(), ItemLens.PROP_POWER));
		register(r, LibItemNames.LENS_TRIPWIRE, new ItemLens(unstackable(), new LensTripwire(), ItemLens.PROP_CONTROL));
		register(r, LibItemNames.LENS_STORM, new ItemLens(unstackable(), new LensStorm(), ItemLens.PROP_NONE));
		register(r, LibItemNames.RUNE_WATER, new ItemRune(defaultBuilder()));
		register(r, LibItemNames.RUNE_FIRE, new ItemRune(defaultBuilder()));
		register(r, LibItemNames.RUNE_EARTH, new ItemRune(defaultBuilder()));
		register(r, LibItemNames.RUNE_AIR, new ItemRune(defaultBuilder()));
		register(r, LibItemNames.RUNE_SPRING, new ItemRune(defaultBuilder()));
		register(r, LibItemNames.RUNE_SUMMER, new ItemRune(defaultBuilder()));
		register(r, LibItemNames.RUNE_AUTUMN, new ItemRune(defaultBuilder()));
		register(r, LibItemNames.RUNE_WINTER, new ItemRune(defaultBuilder()));
		register(r, LibItemNames.RUNE_MANA, new ItemRune(defaultBuilder()));
		register(r, LibItemNames.RUNE_LUST, new ItemRune(defaultBuilder()));
		register(r, LibItemNames.RUNE_GLUTTONY, new ItemRune(defaultBuilder()));
		register(r, LibItemNames.RUNE_GREED, new ItemRune(defaultBuilder()));
		register(r, LibItemNames.RUNE_SLOTH, new ItemRune(defaultBuilder()));
		register(r, LibItemNames.RUNE_WRATH, new ItemRune(defaultBuilder()));
		register(r, LibItemNames.RUNE_ENVY, new ItemRune(defaultBuilder()));
		register(r, LibItemNames.RUNE_PRIDE, new ItemRune(defaultBuilder()));
		register(r, LibItemNames.MANA_TABLET, new ItemManaTablet(unstackable()));
		register(r, LibItemNames.MANA_GUN, new ItemManaGun(unstackable().setNoRepair()));
		register(r, LibItemNames.MANA_COOKIE, new ItemManaCookie(defaultBuilder().food(new Food.Builder().hunger(0).saturation(0.1F).effect(new EffectInstance(Effects.SATURATION, 20, 0), 1).build())));
		register(r, LibItemNames.FERTILIZER, new ItemFertilizer(defaultBuilder()));
		register(r, LibItemNames.GRASS_SEEDS, new ItemGrassSeeds(IFloatingFlower.IslandType.GRASS, defaultBuilder()));
		register(r, LibItemNames.PODZOL_SEEDS, new ItemGrassSeeds(IFloatingFlower.IslandType.PODZOL, defaultBuilder()));
		register(r, LibItemNames.MYCEL_SEEDS, new ItemGrassSeeds(IFloatingFlower.IslandType.MYCEL, defaultBuilder()));
		register(r, LibItemNames.DRY_SEEDS, new ItemGrassSeeds(IFloatingFlower.IslandType.DRY, defaultBuilder()));
		register(r, LibItemNames.GOLDEN_SEEDS, new ItemGrassSeeds(IFloatingFlower.IslandType.GOLDEN, defaultBuilder()));
		register(r, LibItemNames.VIVID_SEEDS, new ItemGrassSeeds(IFloatingFlower.IslandType.VIVID, defaultBuilder()));
		register(r, LibItemNames.SCORCHED_SEEDS, new ItemGrassSeeds(IFloatingFlower.IslandType.SCORCHED, defaultBuilder()));
		register(r, LibItemNames.INFUSED_SEEDS, new ItemGrassSeeds(IFloatingFlower.IslandType.INFUSED, defaultBuilder()));
		register(r, LibItemNames.MUTATED_SEEDS, new ItemGrassSeeds(IFloatingFlower.IslandType.MUTATED, defaultBuilder()));
		register(r, LibItemNames.DIRT_ROD, new ItemDirtRod(unstackable()));
		register(r, LibItemNames.TERRAFORM_ROD, new ItemTerraformRod(unstackable()));
		register(r, LibItemNames.GRASS_HORN, new ItemHorn(unstackable()));
		register(r, LibItemNames.LEAVES_HORN, new ItemHorn(unstackable()));
		register(r, LibItemNames.SNOW_HORN, new ItemHorn(unstackable()));
		register(r, LibItemNames.MANA_MIRROR, new ItemManaMirror(unstackable()));
		register(r, LibItemNames.MANASTEEL_HELM, new ItemManasteelHelm(unstackable()));
		register(r, LibItemNames.MANASTEEL_HELM_R, new ItemManasteelHelmRevealing(unstackable()));
		register(r, LibItemNames.MANASTEEL_CHEST, new ItemManasteelArmor(EquipmentSlotType.CHEST, unstackable()));
		register(r, LibItemNames.MANASTEEL_LEGS, new ItemManasteelArmor(EquipmentSlotType.LEGS, unstackable()));
		register(r, LibItemNames.MANASTEEL_BOOTS, new ItemManasteelArmor(EquipmentSlotType.FEET, unstackable()));
		register(r, LibItemNames.MANASTEEL_PICK, new ItemManasteelPick(unstackable()));
		register(r, LibItemNames.MANASTEEL_SHOVEL, new ItemManasteelShovel(unstackable()));
		register(r, LibItemNames.MANASTEEL_AXE, new ItemManasteelAxe(unstackable()));
		register(r, LibItemNames.MANASTEEL_SWORD, new ItemManasteelSword(unstackable()));
		register(r, LibItemNames.MANASTEEL_SHEARS, new ItemManasteelShears(unstackable()));
		register(r, LibItemNames.TERRASTEEL_HELM, new ItemTerrasteelHelm(unstackable()));
		register(r, LibItemNames.TERRASTEEL_HELM_R, new ItemTerrasteelHelmRevealing(unstackable()));
		register(r, LibItemNames.TERRASTEEL_CHEST, new ItemTerrasteelArmor(EquipmentSlotType.CHEST, unstackable()));
		register(r, LibItemNames.TERRASTEEL_LEGS, new ItemTerrasteelArmor(EquipmentSlotType.LEGS, unstackable()));
		register(r, LibItemNames.TERRASTEEL_BOOTS, new ItemTerrasteelArmor(EquipmentSlotType.FEET, unstackable()));
		register(r, LibItemNames.TERRA_SWORD, new ItemTerraSword(unstackable()));
		register(r, LibItemNames.TERRA_PICK, new ItemTerraPick(unstackable()));
		register(r, LibItemNames.TERRA_AXE, new ItemTerraAxe(unstackable()));
		register(r, LibItemNames.TINY_PLANET, new ItemTinyPlanet(unstackable()));
		register(r, LibItemNames.MANA_RING, new ItemManaRing(unstackable()));
		register(r, LibItemNames.AURA_RING, new ItemAuraRing(unstackable()));
		register(r, LibItemNames.MANA_RING_GREATER, new ItemGreaterManaRing(unstackable()));
		register(r, LibItemNames.AURA_RING_GREATER, new ItemGreaterAuraRing(unstackable()));
		register(r, LibItemNames.TRAVEL_BELT, new ItemTravelBelt(unstackable()));
		register(r, LibItemNames.KNOCKBACK_BELT, new ItemKnockbackBelt(unstackable()));
		register(r, LibItemNames.ICE_PENDANT, new ItemIcePendant(unstackable()));
		register(r, LibItemNames.LAVA_PENDANT, new ItemLavaPendant(unstackable()));
		register(r, LibItemNames.MAGNET_RING, new ItemMagnetRing(unstackable()));
		register(r, LibItemNames.WATER_RING, new ItemWaterRing(unstackable()));
		register(r, LibItemNames.MINING_RING, new ItemMiningRing(unstackable()));
		register(r, LibItemNames.DIVA_CHARM, new ItemDivaCharm(unstackable()));
		register(r, LibItemNames.FLIGHT_TIARA, new ItemFlightTiara(unstackable()));
		register(r, LibItemNames.ENDER_DAGGER, new ItemEnderDagger(unstackable().defaultMaxDamage(69).setNoRepair()));
		register(r, LibItemNames.QUARTZ_DARK, new Item(defaultBuilder()));
		register(r, LibItemNames.QUARTZ_MANA, new Item(defaultBuilder()));
		register(r, LibItemNames.QUARTZ_BLAZE, new Item(defaultBuilder()));
		register(r, LibItemNames.QUARTZ_LAVENDER, new Item(defaultBuilder()));
		register(r, LibItemNames.QUARTZ_RED, new Item(defaultBuilder()));
		register(r, LibItemNames.QUARTZ_ELVEN, new Item(defaultBuilder()));
		register(r, LibItemNames.QUARTZ_SUNNY, new Item(defaultBuilder()));
		register(r, LibItemNames.WATER_ROD, new ItemWaterRod(unstackable()));
		register(r, LibItemNames.ELEMENTIUM_HELM, new ItemElementiumHelm(unstackable()));
		register(r, LibItemNames.ELEMENTIUM_HELM_R, new ItemElementiumHelmRevealing(unstackable()));
		register(r, LibItemNames.ELEMENTIUM_CHEST, new ItemElementiumChest(unstackable()));
		register(r, LibItemNames.ELEMENTIUM_LEGS, new ItemElementiumLegs(unstackable()));
		register(r, LibItemNames.ELEMENTIUM_BOOTS, new ItemElementiumBoots(unstackable()));
		register(r, LibItemNames.ELEMENTIUM_PICK, new ItemElementiumPick(unstackable()));
		register(r, LibItemNames.ELEMENTIUM_SHOVEL, new ItemElementiumShovel(unstackable()));
		register(r, LibItemNames.ELEMENTIUM_AXE, new ItemElementiumAxe(unstackable()));
		register(r, LibItemNames.ELEMENTIUM_SWORD, new ItemElementiumSword(unstackable()));
		register(r, LibItemNames.ELEMENTIUM_SHEARS, new ItemElementiumShears(unstackable()));
		register(r, LibItemNames.OPEN_BUCKET, new ItemOpenBucket(unstackable()));
		register(r, LibItemNames.SPAWNER_MOVER, new ItemSpawnerMover(unstackable()));
		register(r, LibItemNames.PIXIE_RING, new ItemPixieRing(unstackable()));
		register(r, LibItemNames.SUPER_TRAVEL_BELT, new ItemSuperTravelBelt(unstackable()));
		register(r, LibItemNames.RAINBOW_ROD, new ItemRainbowRod(unstackable()));
		register(r, LibItemNames.TORNADO_ROD, new ItemTornadoRod(unstackable()));
		register(r, LibItemNames.FIRE_ROD, new ItemFireRod(unstackable()));
		register(r, LibItemNames.VINE_BALL, new ItemVineBall(defaultBuilder()));
		register(r, LibItemNames.SLINGSHOT, new ItemSlingshot(unstackable()));
		register(r, LibItemNames.MANA_BOTTLE, new ItemBottledMana(unstackable()));
		register(r, LibItemNames.LAPUTA_SHARD, new ItemLaputaShard(unstackable()));
		register(r, LibItemNames.NECRO_VIRUS, new ItemVirus(defaultBuilder()));
		register(r, LibItemNames.NULL_VIRUS, new ItemVirus(defaultBuilder()));
		register(r, LibItemNames.REACH_RING, new ItemReachRing(unstackable()));
		register(r, LibItemNames.SKY_DIRT_ROD, new ItemSkyDirtRod(unstackable()));
		register(r, LibItemNames.ITEM_FINDER, new ItemItemFinder(unstackable()));
		register(r, LibItemNames.SUPER_LAVA_PENDANT, new ItemSuperLavaPendant(unstackable()));
		register(r, LibItemNames.ENDER_HAND, new ItemEnderHand(unstackable()));
		register(r, LibItemNames.GLASS_PICK, new ItemGlassPick(unstackable()));
		register(r, LibItemNames.SPARK, new ItemSpark(defaultBuilder()));
		register(r, LibItemNames.SPARK_UPGRADE + "_" + SparkUpgradeType.DISPERSIVE.name().toLowerCase(Locale.ROOT), new ItemSparkUpgrade(defaultBuilder(), SparkUpgradeType.DISPERSIVE));
		register(r, LibItemNames.SPARK_UPGRADE + "_" + SparkUpgradeType.DOMINANT.name().toLowerCase(Locale.ROOT), new ItemSparkUpgrade(defaultBuilder(), SparkUpgradeType.DOMINANT));
		register(r, LibItemNames.SPARK_UPGRADE + "_" + SparkUpgradeType.RECESSIVE.name().toLowerCase(Locale.ROOT), new ItemSparkUpgrade(defaultBuilder(), SparkUpgradeType.RECESSIVE));
		register(r, LibItemNames.SPARK_UPGRADE + "_" + SparkUpgradeType.ISOLATED.name().toLowerCase(Locale.ROOT), new ItemSparkUpgrade(defaultBuilder(), SparkUpgradeType.ISOLATED));
		register(r, LibItemNames.DIVINING_ROD, new ItemDiviningRod(unstackable()));
		register(r, LibItemNames.GRAVITY_ROD, new ItemGravityRod(unstackable()));
		register(r, LibItemNames.MANA_INKWELL, new ItemManaInkwell(unstackable().setNoRepair()));
		register(r, LibItemNames.VIAL, new ItemVial(defaultBuilder()));
		register(r, LibItemNames.FLASK, new ItemVial(defaultBuilder()));
		register(r, LibItemNames.BREW_VIAL, new ItemBrewBase(unstackable(), 4, 32, () -> vial));
		register(r, LibItemNames.BREW_FLASK, new ItemBrewBase(unstackable(), 6, 24, () -> flask));
		register(r, LibItemNames.BLOOD_PENDANT, new ItemBloodPendant(unstackable()));
		register(r, LibItemNames.MISSILE_ROD, new ItemMissileRod(unstackable()));
		register(r, LibItemNames.HOLY_CLOAK, new ItemHolyCloak(unstackable()));
		register(r, LibItemNames.UNHOLY_CLOAK, new ItemUnholyCloak(unstackable()));
		register(r, LibItemNames.BALANCE_CLOAK, new ItemBalanceCloak(unstackable()));
		register(r, LibItemNames.CRAFTING_HALO, new ItemCraftingHalo(unstackable()));
		register(r, LibItemNames.BLACK_LOTUS, new ItemBlackLotus(defaultBuilder()));
		register(r, LibItemNames.BLACKER_LOTUS, new ItemBlackLotus(defaultBuilder()));
		register(r, LibItemNames.MONOCLE, new ItemMonocle(unstackable()));
		register(r, LibItemNames.CLIP, new Item(unstackable()));
		register(r, LibItemNames.COBBLE_ROD, new ItemCobbleRod(unstackable()));
		register(r, LibItemNames.SMELT_ROD, new ItemSmeltRod(unstackable()));
		register(r, LibItemNames.WORLD_SEED, new ItemWorldSeed(defaultBuilder()));
		register(r, LibItemNames.SPELL_CLOTH, new ItemSpellCloth(unstackable().defaultMaxDamage(35).setNoRepair()));
		register(r, LibItemNames.THORN_CHAKRAM, new ItemThornChakram(defaultBuilder().maxStackSize(6)));
		register(r, LibItemNames.FLARE_CHAKRAM, new ItemThornChakram(defaultBuilder().maxStackSize(6)));
		register(r, LibItemNames.OVERGROWTH_SEED, new ItemOvergrowthSeed(defaultBuilder()));
		register(r, LibItemNames.CRAFT_PATTERN_PREFIX + "1_1", new ItemCraftPattern(CratePattern.CRAFTY_1_1, unstackable()));
		register(r, LibItemNames.CRAFT_PATTERN_PREFIX + "2_2", new ItemCraftPattern(CratePattern.CRAFTY_2_2, unstackable()));
		register(r, LibItemNames.CRAFT_PATTERN_PREFIX + "1_2", new ItemCraftPattern(CratePattern.CRAFTY_1_2, unstackable()));
		register(r, LibItemNames.CRAFT_PATTERN_PREFIX + "2_1", new ItemCraftPattern(CratePattern.CRAFTY_2_1, unstackable()));
		register(r, LibItemNames.CRAFT_PATTERN_PREFIX + "1_3", new ItemCraftPattern(CratePattern.CRAFTY_1_3, unstackable()));
		register(r, LibItemNames.CRAFT_PATTERN_PREFIX + "3_1", new ItemCraftPattern(CratePattern.CRAFTY_3_1, unstackable()));
		register(r, LibItemNames.CRAFT_PATTERN_PREFIX + "2_3", new ItemCraftPattern(CratePattern.CRAFTY_2_3, unstackable()));
		register(r, LibItemNames.CRAFT_PATTERN_PREFIX + "3_2", new ItemCraftPattern(CratePattern.CRAFTY_3_2, unstackable()));
		register(r, LibItemNames.CRAFT_PATTERN_PREFIX + "donut", new ItemCraftPattern(CratePattern.CRAFTY_DONUT, unstackable()));
		register(r, LibItemNames.ANCIENT_WILL_PREFIX + "ahrim", new ItemAncientWill(IAncientWillContainer.AncientWillType.AHRIM, unstackable()));
		register(r, LibItemNames.ANCIENT_WILL_PREFIX + "dharok", new ItemAncientWill(IAncientWillContainer.AncientWillType.DHAROK, unstackable()));
		register(r, LibItemNames.ANCIENT_WILL_PREFIX + "guthan", new ItemAncientWill(IAncientWillContainer.AncientWillType.GUTHAN, unstackable()));
		register(r, LibItemNames.ANCIENT_WILL_PREFIX + "torag", new ItemAncientWill(IAncientWillContainer.AncientWillType.TORAG, unstackable()));
		register(r, LibItemNames.ANCIENT_WILL_PREFIX + "verac", new ItemAncientWill(IAncientWillContainer.AncientWillType.VERAC, unstackable()));
		register(r, LibItemNames.ANCIENT_WILL_PREFIX + "karil", new ItemAncientWill(IAncientWillContainer.AncientWillType.KARIL, unstackable()));
		register(r, LibItemNames.CORPOREA_SPARK, new ItemCorporeaSpark(defaultBuilder()));
		register(r, LibItemNames.CORPOREA_SPARK_MASTER, new ItemCorporeaSpark(defaultBuilder()));
		register(r, LibItemNames.LIVINGWOOD_BOW, new ItemLivingwoodBow(defaultBuilder().defaultMaxDamage(500)));
		register(r, LibItemNames.CRYSTAL_BOW, new ItemCrystalBow(defaultBuilder().defaultMaxDamage(500)));

		for (ItemBaubleCosmetic.Variant v : ItemBaubleCosmetic.Variant.values()) {
			register(r, LibItemNames.COSMETIC_PREFIX + v.name().toLowerCase(Locale.ROOT), new ItemBaubleCosmetic(v, unstackable()));
		}

		register(r, LibItemNames.SWAP_RING, new ItemSwapRing(unstackable()));
		register(r, LibItemNames.FLOWER_BAG, new ItemFlowerBag(unstackable()));
		register(r, LibItemNames.PHANTOM_INK, new Item(defaultBuilder()));
		register(r, LibItemNames.POOL_MINECART, new ItemPoolMinecart(unstackable()));
		register(r, LibItemNames.PINKINATOR, new ItemPinkinator(unstackable()));
		register(r, LibItemNames.INFINITE_FRUIT, new ItemInfiniteFruit(unstackable()));
		register(r, LibItemNames.KING_KEY, new ItemKingKey(unstackable()));
		register(r, LibItemNames.FLUGEL_EYE, new ItemFlugelEye(unstackable()));
		register(r, LibItemNames.THOR_RING, new ItemThorRing(unstackable()));
		register(r, LibItemNames.ODIN_RING, new ItemOdinRing(unstackable()));
		register(r, LibItemNames.LOKI_RING, new ItemLokiRing(unstackable()));
		register(r, LibItemNames.DICE, new ItemDice(unstackable()));
		register(r, LibItemNames.KEEP_IVY, new ItemKeepIvy(defaultBuilder()));
		register(r, LibItemNames.BLACK_HOLE_TALISMAN, new ItemBlackHoleTalisman(unstackable()));
		register(r, LibItemNames.RECORD_GAIA1, new ItemModRecord(1, ModSounds.gaiaMusic1, unstackable()));
		register(r, LibItemNames.RECORD_GAIA2, new ItemModRecord(1, ModSounds.gaiaMusic2, unstackable()));
		register(r, LibItemNames.TEMPERANCE_STONE, new ItemTemperanceStone(unstackable()));
		register(r, LibItemNames.INCENSE_STICK, new ItemIncenseStick(unstackable()));
		register(r, LibItemNames.WATER_BOWL, new ItemWaterBowl(unstackable()));
		register(r, LibItemNames.OBEDIENCE_STICK, new ItemObedienceStick(unstackable()));
		register(r, LibItemNames.CACOPHONIUM, new ItemCacophonium(unstackable()));
		register(r, LibItemNames.SLIME_BOTTLE, new ItemSlimeBottle(unstackable()));
		register(r, LibItemNames.STAR_SWORD, new ItemStarSword(unstackable()));
		register(r, LibItemNames.EXCHANGE_ROD, new ItemExchangeRod(unstackable()));
		register(r, LibItemNames.MAGNET_RING_GREATER, new ItemMagnetRing(unstackable(), 16));
		register(r, LibItemNames.THUNDER_SWORD, new ItemThunderSword(unstackable()));
		register(r, LibItemNames.MANAWEAVE_HELM, new ItemManaweaveHelm(unstackable()));
		register(r, LibItemNames.MANAWEAVE_CHEST, new ItemManaweaveArmor(EquipmentSlotType.CHEST, unstackable()));
		register(r, LibItemNames.MANAWEAVE_LEGS, new ItemManaweaveArmor(EquipmentSlotType.LEGS, unstackable()));
		register(r, LibItemNames.MANAWEAVE_BOOTS, new ItemManaweaveArmor(EquipmentSlotType.FEET, unstackable()));
		register(r, LibItemNames.AUTOCRAFTING_HALO, new ItemAutocraftingHalo(unstackable()));
		register(r, LibItemNames.SEXTANT, new ItemSextant(unstackable()));
		register(r, LibItemNames.SPEED_UP_BELT, new ItemSpeedUpBelt(unstackable()));
		register(r, LibItemNames.BAUBLE_BOX, new ItemBaubleBox(unstackable()));
		register(r, LibItemNames.DODGE_RING, new ItemDodgeRing(unstackable()));
		register(r, LibItemNames.INVISIBILITY_CLOAK, new ItemInvisibilityCloak(unstackable()));
		register(r, LibItemNames.CLOUD_PENDANT, new ItemCloudPendant(unstackable()));
		register(r, LibItemNames.SUPER_CLOUD_PENDANT, new ItemSuperCloudPendant(unstackable()));
		register(r, LibItemNames.THIRD_EYE, new ItemThirdEye(unstackable()));
		register(r, LibItemNames.ASTROLABE, new ItemAstrolabe(unstackable()));
		register(r, LibItemNames.GODDESS_CHARM, new ItemGoddessCharm(unstackable()));
	}

	public static void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> evt) {
		IForgeRegistry<IRecipeSerializer<?>> r = evt.getRegistry();
		register(r, "ancient_will_attach", AncientWillRecipe.SERIALIZER);
		register(r, "armor_upgrade", ArmorUpgradeRecipe.SERIALIZER);
		register(r, "banner_pattern_apply", BannerRecipe.SERIALIZER);
		register(r, "black_hole_talisman_extract", BlackHoleTalismanExtractRecipe.SERIALIZER);
		register(r, "composite_lens", CompositeLensRecipe.SERIALIZER);
		register(r, "cosmetic_attach", CosmeticAttachRecipe.SERIALIZER);
		register(r, "cosmetic_remove", CosmeticRemoveRecipe.SERIALIZER);
		register(r, "helm_revealing", HelmRevealingRecipe.SERIALIZER);
		register(r, "keep_ivy", KeepIvyRecipe.SERIALIZER);
		register(r, "lens_dye", LensDyeingRecipe.SERIALIZER);
		register(r, "mana_gun_add_clip", ManaGunClipRecipe.SERIALIZER);
		register(r, "mana_gun_add_lens", ManaGunLensRecipe.SERIALIZER);
		register(r, "mana_gun_remove_lens", ManaGunRemoveLensRecipe.SERIALIZER);
		register(r, "mana_upgrade", ManaUpgradeRecipe.SERIALIZER);
		register(r, "mana_upgrade_shapeless", ShapelessManaUpgradeRecipe.SERIALIZER);
		register(r, "phantom_ink_apply", PhantomInkRecipe.SERIALIZER);
		register(r, "spell_cloth_apply", SpellClothRecipe.SERIALIZER);
		register(r, "terra_pick_tipping", TerraPickTippingRecipe.SERIALIZER);
		register(r, "twig_wand", TwigWandRecipe.SERIALIZER);

		CraftingHelper.register(FluxfieldCondition.SERIALIZER);
		CraftingHelper.register(prefix("fuzzy_nbt"), FuzzyNBTIngredient.SERIALIZER);
	}

	public static void registerContainers(RegistryEvent.Register<ContainerType<?>> evt) {
		IForgeRegistry<ContainerType<?>> r = evt.getRegistry();

		ContainerType<ContainerFlowerBag> bag = IForgeContainerType.create(ContainerFlowerBag::fromNetwork);
		register(r, flowerBag.getRegistryName(), bag);

		ContainerType<ContainerBaubleBox> box = IForgeContainerType.create(ContainerBaubleBox::fromNetwork);
		register(r, baubleBox.getRegistryName(), box);

		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			ScreenManager.registerFactory(bag, GuiFlowerBag::new);
			ScreenManager.registerFactory(box, GuiBaubleBox::new);
		});
	}

	public static Item getPetal(DyeColor color) {
		switch (color) {
		default:
		case WHITE:
			return whitePetal;
		case ORANGE:
			return orangePetal;
		case MAGENTA:
			return magentaPetal;
		case LIGHT_BLUE:
			return lightBluePetal;
		case YELLOW:
			return yellowPetal;
		case LIME:
			return limePetal;
		case PINK:
			return pinkPetal;
		case GRAY:
			return grayPetal;
		case LIGHT_GRAY:
			return lightGrayPetal;
		case CYAN:
			return cyanPetal;
		case PURPLE:
			return purplePetal;
		case BLUE:
			return bluePetal;
		case BROWN:
			return brownPetal;
		case GREEN:
			return greenPetal;
		case RED:
			return redPetal;
		case BLACK:
			return blackPetal;
		}
	}

	public static Item getDye(DyeColor color) {
		switch (color) {
		default:
		case WHITE:
			return whiteDye;
		case ORANGE:
			return orangeDye;
		case MAGENTA:
			return magentaDye;
		case LIGHT_BLUE:
			return lightBlueDye;
		case YELLOW:
			return yellowDye;
		case LIME:
			return limeDye;
		case PINK:
			return pinkDye;
		case GRAY:
			return grayDye;
		case LIGHT_GRAY:
			return lightGrayDye;
		case CYAN:
			return cyanDye;
		case PURPLE:
			return purpleDye;
		case BLUE:
			return blueDye;
		case BROWN:
			return brownDye;
		case GREEN:
			return greenDye;
		case RED:
			return redDye;
		case BLACK:
			return blackDye;
		}
	}
}
