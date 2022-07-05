/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.RecipeSerializer;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.block.IFloatingFlower;
import vazkii.botania.api.item.IAncientWillContainer;
import vazkii.botania.api.mana.spark.SparkUpgradeType;
import vazkii.botania.api.state.enums.CratePattern;
import vazkii.botania.client.gui.bag.ContainerFlowerBag;
import vazkii.botania.client.gui.box.ContainerBaubleBox;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModPatterns;
import vazkii.botania.common.crafting.recipe.*;
import vazkii.botania.common.handler.ModSounds;
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
import vazkii.botania.common.item.lens.*;
import vazkii.botania.common.item.material.*;
import vazkii.botania.common.item.record.ItemModRecord;
import vazkii.botania.common.item.relic.*;
import vazkii.botania.common.item.rod.*;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.xplat.IXplatAbstractions;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public final class ModItems {
	private static final Map<ResourceLocation, Item> ALL = new LinkedHashMap<>(); // Preserve insertion order
	public static final ItemLexicon lexicon = make(prefix(LibItemNames.LEXICON), new ItemLexicon(unstackable().rarity(Rarity.UNCOMMON)));
	public static final Item twigWand = make(prefix(LibItemNames.TWIG_WAND), new ItemTwigWand(ChatFormatting.DARK_GREEN, unstackable().rarity(Rarity.RARE)));
	public static final Item dreamwoodWand = make(prefix(LibItemNames.DREAMWOOD_WAND), new ItemTwigWand(ChatFormatting.LIGHT_PURPLE, unstackable().rarity(Rarity.RARE)));
	public static final Item obedienceStick = make(prefix(LibItemNames.OBEDIENCE_STICK), new ItemObedienceStick(unstackable()));
	public static final Item fertilizer = make(prefix(LibItemNames.FERTILIZER), new ItemFertilizer(defaultBuilder()));

	public static final Item whitePetal = make(prefix("white" + LibItemNames.PETAL_SUFFIX), new ItemPetal(ModBlocks.getBuriedPetal(DyeColor.WHITE), DyeColor.WHITE, defaultBuilder()));
	public static final Item orangePetal = make(prefix("orange" + LibItemNames.PETAL_SUFFIX), new ItemPetal(ModBlocks.getBuriedPetal(DyeColor.ORANGE), DyeColor.ORANGE, defaultBuilder()));
	public static final Item magentaPetal = make(prefix("magenta" + LibItemNames.PETAL_SUFFIX), new ItemPetal(ModBlocks.getBuriedPetal(DyeColor.MAGENTA), DyeColor.MAGENTA, defaultBuilder()));
	public static final Item lightBluePetal = make(prefix("light_blue" + LibItemNames.PETAL_SUFFIX), new ItemPetal(ModBlocks.getBuriedPetal(DyeColor.LIGHT_BLUE), DyeColor.LIGHT_BLUE, defaultBuilder()));
	public static final Item yellowPetal = make(prefix("yellow" + LibItemNames.PETAL_SUFFIX), new ItemPetal(ModBlocks.getBuriedPetal(DyeColor.YELLOW), DyeColor.YELLOW, defaultBuilder()));
	public static final Item limePetal = make(prefix("lime" + LibItemNames.PETAL_SUFFIX), new ItemPetal(ModBlocks.getBuriedPetal(DyeColor.LIME), DyeColor.LIME, defaultBuilder()));
	public static final Item pinkPetal = make(prefix("pink" + LibItemNames.PETAL_SUFFIX), new ItemPetal(ModBlocks.getBuriedPetal(DyeColor.PINK), DyeColor.PINK, defaultBuilder()));
	public static final Item grayPetal = make(prefix("gray" + LibItemNames.PETAL_SUFFIX), new ItemPetal(ModBlocks.getBuriedPetal(DyeColor.GRAY), DyeColor.GRAY, defaultBuilder()));
	public static final Item lightGrayPetal = make(prefix("light_gray" + LibItemNames.PETAL_SUFFIX), new ItemPetal(ModBlocks.getBuriedPetal(DyeColor.LIGHT_GRAY), DyeColor.LIGHT_GRAY, defaultBuilder()));
	public static final Item cyanPetal = make(prefix("cyan" + LibItemNames.PETAL_SUFFIX), new ItemPetal(ModBlocks.getBuriedPetal(DyeColor.CYAN), DyeColor.CYAN, defaultBuilder()));
	public static final Item purplePetal = make(prefix("purple" + LibItemNames.PETAL_SUFFIX), new ItemPetal(ModBlocks.getBuriedPetal(DyeColor.PURPLE), DyeColor.PURPLE, defaultBuilder()));
	public static final Item bluePetal = make(prefix("blue" + LibItemNames.PETAL_SUFFIX), new ItemPetal(ModBlocks.getBuriedPetal(DyeColor.BLUE), DyeColor.BLUE, defaultBuilder()));
	public static final Item brownPetal = make(prefix("brown" + LibItemNames.PETAL_SUFFIX), new ItemPetal(ModBlocks.getBuriedPetal(DyeColor.BROWN), DyeColor.BROWN, defaultBuilder()));
	public static final Item greenPetal = make(prefix("green" + LibItemNames.PETAL_SUFFIX), new ItemPetal(ModBlocks.getBuriedPetal(DyeColor.GREEN), DyeColor.GREEN, defaultBuilder()));
	public static final Item redPetal = make(prefix("red" + LibItemNames.PETAL_SUFFIX), new ItemPetal(ModBlocks.getBuriedPetal(DyeColor.RED), DyeColor.RED, defaultBuilder()));
	public static final Item blackPetal = make(prefix("black" + LibItemNames.PETAL_SUFFIX), new ItemPetal(ModBlocks.getBuriedPetal(DyeColor.BLACK), DyeColor.BLACK, defaultBuilder()));

	public static final Item manaSteel = make(prefix(LibItemNames.MANASTEEL_INGOT), new Item(defaultBuilder()));
	public static final Item manaPearl = make(prefix(LibItemNames.MANA_PEARL), new Item(defaultBuilder()));
	public static final Item manaDiamond = make(prefix(LibItemNames.MANA_DIAMOND), new Item(defaultBuilder()));
	public static final Item livingwoodTwig = make(prefix(LibItemNames.LIVINGWOOD_TWIG), new ItemModPattern(/*ModPatterns.FLOWER, */defaultBuilder()));
	public static final Item terrasteel = make(prefix(LibItemNames.TERRASTEEL_INGOT), new ItemTerrasteel(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item lifeEssence = make(prefix(LibItemNames.LIFE_ESSENCE), new Item(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item redstoneRoot = make(prefix(LibItemNames.REDSTONE_ROOT), new Item(defaultBuilder()));
	public static final Item elementium = make(prefix(LibItemNames.ELEMENTIUM_INGOT), new Item(defaultBuilder()));
	public static final Item pixieDust = make(prefix(LibItemNames.PIXIE_DUST), new Item(defaultBuilder()));
	public static final Item dragonstone = make(prefix(LibItemNames.DRAGONSTONE), new Item(defaultBuilder()));
	public static final Item redString = make(prefix(LibItemNames.RED_STRING), new Item(defaultBuilder()));
	public static final Item dreamwoodTwig = make(prefix(LibItemNames.DREAMWOOD_TWIG), new ItemModPattern(/*ModPatterns.SAPLING, */defaultBuilder()));
	public static final Item gaiaIngot = make(prefix(LibItemNames.GAIA_INGOT), new ItemManaResource(defaultBuilder().rarity(Rarity.RARE)));
	public static final Item enderAirBottle = make(prefix(LibItemNames.ENDER_AIR_BOTTLE), new ItemEnderAir(defaultBuilder()));
	public static final Item manaString = make(prefix(LibItemNames.MANA_STRING), new Item(defaultBuilder()));
	public static final Item manasteelNugget = make(prefix(LibItemNames.MANASTEEL_NUGGET), new Item(defaultBuilder()));
	public static final Item terrasteelNugget = make(prefix(LibItemNames.TERRASTEEL_NUGGET), new Item(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item elementiumNugget = make(prefix(LibItemNames.ELEMENTIUM_NUGGET), new Item(defaultBuilder()));
	public static final Item livingroot = make(prefix(LibItemNames.LIVING_ROOT), new ItemManaResource(defaultBuilder()));
	public static final Item pebble = make(prefix(LibItemNames.PEBBLE), new Item(defaultBuilder()));
	public static final Item manaweaveCloth = make(prefix(LibItemNames.MANAWEAVE_CLOTH), new Item(defaultBuilder()));
	public static final Item manaPowder = make(prefix(LibItemNames.MANA_POWDER), new Item(defaultBuilder()));

	public static final Item darkQuartz = make(prefix(LibItemNames.QUARTZ_DARK), new Item(defaultBuilder()));
	public static final Item manaQuartz = make(prefix(LibItemNames.QUARTZ_MANA), new Item(defaultBuilder()));
	public static final Item blazeQuartz = make(prefix(LibItemNames.QUARTZ_BLAZE), new Item(defaultBuilder()));
	public static final Item lavenderQuartz = make(prefix(LibItemNames.QUARTZ_LAVENDER), new Item(defaultBuilder()));
	public static final Item redQuartz = make(prefix(LibItemNames.QUARTZ_RED), new Item(defaultBuilder()));
	public static final Item elfQuartz = make(prefix(LibItemNames.QUARTZ_ELVEN), new Item(defaultBuilder()));
	public static final Item sunnyQuartz = make(prefix(LibItemNames.QUARTZ_SUNNY), new Item(defaultBuilder()));

	public static final Item lensNormal = make(prefix(LibItemNames.LENS_NORMAL), new ItemLens(stackTo16(), new Lens(), ItemLens.PROP_NONE));
	public static final Item lensSpeed = make(prefix(LibItemNames.LENS_SPEED), new ItemLens(stackTo16(), new LensSpeed(), ItemLens.PROP_NONE));
	public static final Item lensPower = make(prefix(LibItemNames.LENS_POWER), new ItemLens(stackTo16(), new LensPower(), ItemLens.PROP_POWER));
	public static final Item lensTime = make(prefix(LibItemNames.LENS_TIME), new ItemLens(stackTo16(), new LensTime(), ItemLens.PROP_NONE));
	public static final Item lensEfficiency = make(prefix(LibItemNames.LENS_EFFICIENCY), new ItemLens(stackTo16(), new LensEfficiency(), ItemLens.PROP_NONE));
	public static final Item lensBounce = make(prefix(LibItemNames.LENS_BOUNCE), new ItemLens(stackTo16(), new LensBounce(), ItemLens.PROP_TOUCH));
	public static final Item lensGravity = make(prefix(LibItemNames.LENS_GRAVITY), new ItemLens(stackTo16(), new LensGravity(), ItemLens.PROP_ORIENTATION));
	public static final Item lensMine = make(prefix(LibItemNames.LENS_MINE), new ItemLens(stackTo16(), new LensMine(), ItemLens.PROP_TOUCH | ItemLens.PROP_INTERACTION));
	public static final Item lensDamage = make(prefix(LibItemNames.LENS_DAMAGE), new ItemLens(stackTo16(), new LensDamage(), ItemLens.PROP_DAMAGE));
	public static final Item lensPhantom = make(prefix(LibItemNames.LENS_PHANTOM), new ItemLens(stackTo16(), new LensPhantom(), ItemLens.PROP_TOUCH));
	public static final Item lensMagnet = make(prefix(LibItemNames.LENS_MAGNET), new ItemLens(stackTo16(), new LensMagnet(), ItemLens.PROP_ORIENTATION));
	public static final Item lensExplosive = make(prefix(LibItemNames.LENS_EXPLOSIVE), new ItemLens(stackTo16(), new LensExplosive(), ItemLens.PROP_DAMAGE | ItemLens.PROP_TOUCH | ItemLens.PROP_INTERACTION));
	public static final Item lensInfluence = make(prefix(LibItemNames.LENS_INFLUENCE), new ItemLens(stackTo16(), new LensInfluence(), ItemLens.PROP_NONE));
	public static final Item lensWeight = make(prefix(LibItemNames.LENS_WEIGHT), new ItemLens(stackTo16(), new LensWeight(), ItemLens.PROP_TOUCH | ItemLens.PROP_INTERACTION));
	public static final Item lensPaint = make(prefix(LibItemNames.LENS_PAINT), new ItemLens(stackTo16(), new LensPaint(), ItemLens.PROP_TOUCH | ItemLens.PROP_INTERACTION));
	public static final Item lensFire = make(prefix(LibItemNames.LENS_FIRE), new ItemLens(stackTo16(), new LensFire(), ItemLens.PROP_DAMAGE | ItemLens.PROP_TOUCH | ItemLens.PROP_INTERACTION));
	public static final Item lensPiston = make(prefix(LibItemNames.LENS_PISTON), new ItemLens(stackTo16(), new LensPiston(), ItemLens.PROP_TOUCH | ItemLens.PROP_INTERACTION));
	public static final Item lensLight = make(prefix(LibItemNames.LENS_LIGHT), new ItemLens(stackTo16(), new LensLight(), ItemLens.PROP_TOUCH | ItemLens.PROP_INTERACTION));
	public static final Item lensWarp = make(prefix(LibItemNames.LENS_WARP), new ItemLens(stackTo16(), new LensWarp(), ItemLens.PROP_NONE));
	public static final Item lensRedirect = make(prefix(LibItemNames.LENS_REDIRECT), new ItemLens(stackTo16(), new LensRedirect(), ItemLens.PROP_TOUCH | ItemLens.PROP_INTERACTION));
	public static final Item lensFirework = make(prefix(LibItemNames.LENS_FIREWORK), new ItemLens(stackTo16(), new LensFirework(), ItemLens.PROP_TOUCH));
	public static final Item lensFlare = make(prefix(LibItemNames.LENS_FLARE), new ItemLens(stackTo16(), new LensFlare(), ItemLens.PROP_CONTROL));
	public static final Item lensMessenger = make(prefix(LibItemNames.LENS_MESSENGER), new ItemLens(stackTo16(), new LensMessenger(), ItemLens.PROP_POWER));
	public static final Item lensTripwire = make(prefix(LibItemNames.LENS_TRIPWIRE), new ItemLens(stackTo16(), new LensTripwire(), ItemLens.PROP_CONTROL));
	public static final Item lensStorm = make(prefix(LibItemNames.LENS_STORM), new ItemLens(stackTo16().rarity(Rarity.EPIC), new LensStorm(), ItemLens.PROP_NONE));

	public static final Item runeWater = make(prefix(LibItemNames.RUNE_WATER), new ItemRune(defaultBuilder()));
	public static final Item runeFire = make(prefix(LibItemNames.RUNE_FIRE), new ItemRune(defaultBuilder()));
	public static final Item runeEarth = make(prefix(LibItemNames.RUNE_EARTH), new ItemRune(defaultBuilder()));
	public static final Item runeAir = make(prefix(LibItemNames.RUNE_AIR), new ItemRune(defaultBuilder()));
	public static final Item runeSpring = make(prefix(LibItemNames.RUNE_SPRING), new ItemRune(defaultBuilder()));
	public static final Item runeSummer = make(prefix(LibItemNames.RUNE_SUMMER), new ItemRune(defaultBuilder()));
	public static final Item runeAutumn = make(prefix(LibItemNames.RUNE_AUTUMN), new ItemRune(defaultBuilder()));
	public static final Item runeWinter = make(prefix(LibItemNames.RUNE_WINTER), new ItemRune(defaultBuilder()));
	public static final Item runeMana = make(prefix(LibItemNames.RUNE_MANA), new ItemRune(defaultBuilder()));
	public static final Item runeLust = make(prefix(LibItemNames.RUNE_LUST), new ItemRune(defaultBuilder()));
	public static final Item runeGluttony = make(prefix(LibItemNames.RUNE_GLUTTONY), new ItemRune(defaultBuilder()));
	public static final Item runeGreed = make(prefix(LibItemNames.RUNE_GREED), new ItemRune(defaultBuilder()));
	public static final Item runeSloth = make(prefix(LibItemNames.RUNE_SLOTH), new ItemRune(defaultBuilder()));
	public static final Item runeWrath = make(prefix(LibItemNames.RUNE_WRATH), new ItemRune(defaultBuilder()));
	public static final Item runeEnvy = make(prefix(LibItemNames.RUNE_ENVY), new ItemRune(defaultBuilder()));
	public static final Item runePride = make(prefix(LibItemNames.RUNE_PRIDE), new ItemRune(defaultBuilder()));

	public static final Item grassSeeds = make(prefix(LibItemNames.GRASS_SEEDS), new ItemGrassSeeds(IFloatingFlower.IslandType.GRASS, defaultBuilder()));
	public static final Item podzolSeeds = make(prefix(LibItemNames.PODZOL_SEEDS), new ItemGrassSeeds(IFloatingFlower.IslandType.PODZOL, defaultBuilder()));
	public static final Item mycelSeeds = make(prefix(LibItemNames.MYCEL_SEEDS), new ItemGrassSeeds(IFloatingFlower.IslandType.MYCEL, defaultBuilder()));
	public static final Item drySeeds = make(prefix(LibItemNames.DRY_SEEDS), new ItemGrassSeeds(IFloatingFlower.IslandType.DRY, defaultBuilder()));
	public static final Item goldenSeeds = make(prefix(LibItemNames.GOLDEN_SEEDS), new ItemGrassSeeds(IFloatingFlower.IslandType.GOLDEN, defaultBuilder()));
	public static final Item vividSeeds = make(prefix(LibItemNames.VIVID_SEEDS), new ItemGrassSeeds(IFloatingFlower.IslandType.VIVID, defaultBuilder()));
	public static final Item scorchedSeeds = make(prefix(LibItemNames.SCORCHED_SEEDS), new ItemGrassSeeds(IFloatingFlower.IslandType.SCORCHED, defaultBuilder()));
	public static final Item infusedSeeds = make(prefix(LibItemNames.INFUSED_SEEDS), new ItemGrassSeeds(IFloatingFlower.IslandType.INFUSED, defaultBuilder()));
	public static final Item mutatedSeeds = make(prefix(LibItemNames.MUTATED_SEEDS), new ItemGrassSeeds(IFloatingFlower.IslandType.MUTATED, defaultBuilder()));

	public static final Item dirtRod = make(prefix(LibItemNames.DIRT_ROD), new ItemDirtRod(unstackable()));
	public static final Item skyDirtRod = make(prefix(LibItemNames.SKY_DIRT_ROD), new ItemSkyDirtRod(unstackable()));
	public static final Item terraformRod = make(prefix(LibItemNames.TERRAFORM_ROD), new ItemTerraformRod(unstackable().rarity(Rarity.UNCOMMON)));
	public static final Item cobbleRod = make(prefix(LibItemNames.COBBLE_ROD), new ItemCobbleRod(unstackable()));
	public static final Item waterRod = make(prefix(LibItemNames.WATER_ROD), new ItemWaterRod(unstackable()));
	public static final Item tornadoRod = make(prefix(LibItemNames.TORNADO_ROD), new ItemTornadoRod(unstackable()));
	public static final Item fireRod = make(prefix(LibItemNames.FIRE_ROD), new ItemFireRod(unstackable()));
	public static final Item diviningRod = make(prefix(LibItemNames.DIVINING_ROD), new ItemDiviningRod(unstackable()));
	public static final Item smeltRod = make(prefix(LibItemNames.SMELT_ROD), new ItemSmeltRod(unstackable()));
	public static final Item exchangeRod = make(prefix(LibItemNames.EXCHANGE_ROD), new ItemExchangeRod(unstackable()));
	public static final Item rainbowRod = make(prefix(LibItemNames.RAINBOW_ROD), new ItemRainbowRod(unstackable()));
	public static final Item gravityRod = make(prefix(LibItemNames.GRAVITY_ROD), new ItemGravityRod(unstackable()));
	public static final Item missileRod = make(prefix(LibItemNames.MISSILE_ROD), new ItemMissileRod(unstackable().rarity(Rarity.UNCOMMON)));

	// Equipment
	public static final Item manasteelHelm = make(prefix(LibItemNames.MANASTEEL_HELM), new ItemManasteelHelm(unstackableCustomDamage()));
	public static final Item manasteelChest = make(prefix(LibItemNames.MANASTEEL_CHEST), new ItemManasteelArmor(EquipmentSlot.CHEST, unstackableCustomDamage()));
	public static final Item manasteelLegs = make(prefix(LibItemNames.MANASTEEL_LEGS), new ItemManasteelArmor(EquipmentSlot.LEGS, unstackableCustomDamage()));
	public static final Item manasteelBoots = make(prefix(LibItemNames.MANASTEEL_BOOTS), new ItemManasteelArmor(EquipmentSlot.FEET, unstackableCustomDamage()));
	public static final Item manasteelPick = make(prefix(LibItemNames.MANASTEEL_PICK), new ItemManasteelPick(unstackableCustomDamage()));
	public static final Item manasteelShovel = make(prefix(LibItemNames.MANASTEEL_SHOVEL), new ItemManasteelShovel(unstackableCustomDamage()));
	public static final Item manasteelAxe = make(prefix(LibItemNames.MANASTEEL_AXE), new ItemManasteelAxe(unstackableCustomDamage()));
	public static final Item manasteelHoe = make(prefix(LibItemNames.MANASTEEL_HOE), new ItemManasteelHoe(unstackableCustomDamage()));
	public static final Item manasteelSword = make(prefix(LibItemNames.MANASTEEL_SWORD), new ItemManasteelSword(unstackableCustomDamage()));
	public static final Item manasteelShears = make(prefix(LibItemNames.MANASTEEL_SHEARS), new ItemManasteelShears(unstackableCustomDamage().defaultDurability(238)));
	public static final Item elementiumHelm = make(prefix(LibItemNames.ELEMENTIUM_HELM), new ItemElementiumHelm(unstackableCustomDamage()));
	public static final Item elementiumChest = make(prefix(LibItemNames.ELEMENTIUM_CHEST), new ItemElementiumChest(unstackableCustomDamage()));
	public static final Item elementiumLegs = make(prefix(LibItemNames.ELEMENTIUM_LEGS), new ItemElementiumLegs(unstackableCustomDamage()));
	public static final Item elementiumBoots = make(prefix(LibItemNames.ELEMENTIUM_BOOTS), new ItemElementiumBoots(unstackableCustomDamage()));
	public static final Item elementiumPick = make(prefix(LibItemNames.ELEMENTIUM_PICK), new ItemElementiumPick(unstackableCustomDamage()));
	public static final Item elementiumShovel = make(prefix(LibItemNames.ELEMENTIUM_SHOVEL), new ItemElementiumShovel(unstackableCustomDamage()));
	public static final Item elementiumAxe = make(prefix(LibItemNames.ELEMENTIUM_AXE), new ItemElementiumAxe(unstackableCustomDamage()));
	public static final Item elementiumHoe = make(prefix(LibItemNames.ELEMENTIUM_HOE), new ItemElementiumHoe(unstackableCustomDamage()));
	public static final Item elementiumSword = make(prefix(LibItemNames.ELEMENTIUM_SWORD), new ItemElementiumSword(unstackableCustomDamage()));
	public static final Item elementiumShears = make(prefix(LibItemNames.ELEMENTIUM_SHEARS), new ItemElementiumShears(unstackableCustomDamage().defaultDurability(238)));
	public static final Item terrasteelHelm = make(prefix(LibItemNames.TERRASTEEL_HELM), new ItemTerrasteelHelm(unstackableCustomDamage().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final Item terrasteelChest = make(prefix(LibItemNames.TERRASTEEL_CHEST), new ItemTerrasteelArmor(EquipmentSlot.CHEST, unstackableCustomDamage().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final Item terrasteelLegs = make(prefix(LibItemNames.TERRASTEEL_LEGS), new ItemTerrasteelArmor(EquipmentSlot.LEGS, unstackableCustomDamage().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final Item terrasteelBoots = make(prefix(LibItemNames.TERRASTEEL_BOOTS), new ItemTerrasteelArmor(EquipmentSlot.FEET, unstackableCustomDamage().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final Item terraPick = make(prefix(LibItemNames.TERRA_PICK), new ItemTerraPick(unstackableCustomDamage().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final Item terraAxe = make(prefix(LibItemNames.TERRA_AXE), new ItemTerraAxe(unstackableCustomDamage().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final Item terraSword = make(prefix(LibItemNames.TERRA_SWORD), new ItemTerraSword(unstackableCustomDamage().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final Item starSword = make(prefix(LibItemNames.STAR_SWORD), new ItemStarSword(unstackableCustomDamage().rarity(Rarity.UNCOMMON)));
	public static final Item thunderSword = make(prefix(LibItemNames.THUNDER_SWORD), new ItemThunderSword(unstackableCustomDamage().rarity(Rarity.UNCOMMON)));
	public static final Item manaweaveHelm = make(prefix(LibItemNames.MANAWEAVE_HELM), new ItemManaweaveHelm(unstackableCustomDamage()));
	public static final Item manaweaveChest = make(prefix(LibItemNames.MANAWEAVE_CHEST), new ItemManaweaveArmor(EquipmentSlot.CHEST, unstackableCustomDamage()));
	public static final Item manaweaveLegs = make(prefix(LibItemNames.MANAWEAVE_LEGS), new ItemManaweaveArmor(EquipmentSlot.LEGS, unstackableCustomDamage()));
	public static final Item manaweaveBoots = make(prefix(LibItemNames.MANAWEAVE_BOOTS), new ItemManaweaveArmor(EquipmentSlot.FEET, unstackableCustomDamage()));
	public static final Item enderDagger = make(prefix(LibItemNames.ENDER_DAGGER), new ItemEnderDagger(unstackable().defaultDurability(69))); // What you looking at?
	public static final Item glassPick = make(prefix(LibItemNames.GLASS_PICK), new ItemGlassPick(unstackableCustomDamage()));
	public static final Item livingwoodBow = make(prefix(LibItemNames.LIVINGWOOD_BOW), new ItemLivingwoodBow(defaultBuilderCustomDamage().defaultDurability(500)));
	public static final Item crystalBow = make(prefix(LibItemNames.CRYSTAL_BOW), new ItemCrystalBow(defaultBuilderCustomDamage().defaultDurability(500)));
	public static final Item thornChakram = make(prefix(LibItemNames.THORN_CHAKRAM), new ItemThornChakram(defaultBuilder().stacksTo(6)));
	public static final Item flareChakram = make(prefix(LibItemNames.FLARE_CHAKRAM), new ItemThornChakram(defaultBuilder().stacksTo(6)));

	// Misc tools
	public static final Item manaTablet = make(prefix(LibItemNames.MANA_TABLET), new ItemManaTablet(unstackable()));
	public static final Item manaMirror = make(prefix(LibItemNames.MANA_MIRROR), new ItemManaMirror(unstackable()));
	public static final Item manaGun = make(prefix(LibItemNames.MANA_GUN), new ItemManaGun(unstackable()));
	public static final Item clip = make(prefix(LibItemNames.CLIP), new Item(unstackable()));
	public static final Item grassHorn = make(prefix(LibItemNames.GRASS_HORN), new ItemHorn(unstackable()));
	public static final Item leavesHorn = make(prefix(LibItemNames.LEAVES_HORN), new ItemHorn(unstackable()));
	public static final Item snowHorn = make(prefix(LibItemNames.SNOW_HORN), new ItemHorn(unstackable()));
	public static final Item vineBall = make(prefix(LibItemNames.VINE_BALL), new ItemVineBall(defaultBuilder()));
	public static final Item slingshot = make(prefix(LibItemNames.SLINGSHOT), new ItemSlingshot(unstackable()));
	public static final Item openBucket = make(prefix(LibItemNames.OPEN_BUCKET), new ItemOpenBucket(unstackable()));
	public static final Item spawnerMover = make(prefix(LibItemNames.SPAWNER_MOVER), new ItemSpawnerMover(unstackable().rarity(Rarity.UNCOMMON)));
	public static final Item enderHand = make(prefix(LibItemNames.ENDER_HAND), new ItemEnderHand(unstackable()));
	public static final Item craftingHalo = make(prefix(LibItemNames.CRAFTING_HALO), new ItemCraftingHalo(unstackable()));
	public static final Item autocraftingHalo = make(prefix(LibItemNames.AUTOCRAFTING_HALO), new ItemAutocraftingHalo(unstackable()));
	public static final Item spellCloth = make(prefix(LibItemNames.SPELL_CLOTH), new ItemSpellCloth(unstackable().defaultDurability(35)/* todo 1.16-fabric.setNoRepair()*/));
	public static final Item flowerBag = make(prefix(LibItemNames.FLOWER_BAG), new ItemFlowerBag(unstackable()));
	public static final Item blackHoleTalisman = make(prefix(LibItemNames.BLACK_HOLE_TALISMAN), new ItemBlackHoleTalisman(unstackable()));
	public static final Item temperanceStone = make(prefix(LibItemNames.TEMPERANCE_STONE), new ItemTemperanceStone(unstackable()));
	public static final Item waterBowl = make(prefix(LibItemNames.WATER_BOWL), new ItemWaterBowl(unstackable()));
	public static final Item cacophonium = make(prefix(LibItemNames.CACOPHONIUM), new ItemCacophonium(unstackable()));
	public static final Item slimeBottle = make(prefix(LibItemNames.SLIME_BOTTLE), new ItemSlimeBottle(unstackable()));
	public static final Item sextant = make(prefix(LibItemNames.SEXTANT), new ItemSextant(unstackable()));
	public static final Item astrolabe = make(prefix(LibItemNames.ASTROLABE), new ItemAstrolabe(unstackable().rarity(Rarity.UNCOMMON)));
	public static final Item baubleBox = make(prefix(LibItemNames.BAUBLE_BOX), new ItemBaubleBox(unstackable()));

	// Baubles / trinkets / curios / etc.
	public static final Item manaRing = make(prefix(LibItemNames.MANA_RING), new ItemManaRing(unstackable()));
	public static final Item manaRingGreater = make(prefix(LibItemNames.MANA_RING_GREATER), new ItemGreaterManaRing(unstackable()));
	public static final Item auraRing = make(prefix(LibItemNames.AURA_RING), new ItemAuraRing(unstackable(), 10));
	public static final Item auraRingGreater = make(prefix(LibItemNames.AURA_RING_GREATER), new ItemAuraRing(unstackable(), 2));
	public static final Item magnetRing = make(prefix(LibItemNames.MAGNET_RING), new ItemMagnetRing(unstackable()));
	public static final Item magnetRingGreater = make(prefix(LibItemNames.MAGNET_RING_GREATER), new ItemMagnetRing(unstackable(), 16));
	public static final Item waterRing = make(prefix(LibItemNames.WATER_RING), new ItemWaterRing(unstackable().rarity(Rarity.RARE)));
	public static final Item swapRing = make(prefix(LibItemNames.SWAP_RING), new ItemSwapRing(unstackable()));
	public static final Item dodgeRing = make(prefix(LibItemNames.DODGE_RING), new ItemDodgeRing(unstackable()));
	public static final Item miningRing = make(prefix(LibItemNames.MINING_RING), new ItemMiningRing(unstackable()));
	public static final Item pixieRing = make(prefix(LibItemNames.PIXIE_RING), new ItemPixieRing(unstackable()));
	public static final Item reachRing = make(prefix(LibItemNames.REACH_RING), new ItemReachRing(unstackable()));
	public static final Item travelBelt = make(prefix(LibItemNames.TRAVEL_BELT), new ItemTravelBelt(unstackable()));
	public static final Item superTravelBelt = make(prefix(LibItemNames.SUPER_TRAVEL_BELT), new ItemSuperTravelBelt(unstackable()));
	public static final Item speedUpBelt = make(prefix(LibItemNames.SPEED_UP_BELT), new ItemSpeedUpBelt(unstackable()));
	public static final Item knockbackBelt = make(prefix(LibItemNames.KNOCKBACK_BELT), new ItemKnockbackBelt(unstackable()));
	public static final Item icePendant = make(prefix(LibItemNames.ICE_PENDANT), new ItemIcePendant(unstackable()));
	public static final Item lavaPendant = make(prefix(LibItemNames.LAVA_PENDANT), new ItemLavaPendant(unstackable()));
	public static final Item superLavaPendant = make(prefix(LibItemNames.SUPER_LAVA_PENDANT), new ItemSuperLavaPendant(unstackable()));
	public static final Item cloudPendant = make(prefix(LibItemNames.CLOUD_PENDANT), new ItemCloudPendant(unstackable()));
	public static final Item superCloudPendant = make(prefix(LibItemNames.SUPER_CLOUD_PENDANT), new ItemSuperCloudPendant(unstackable()));
	public static final Item holyCloak = make(prefix(LibItemNames.HOLY_CLOAK), new ItemHolyCloak(unstackable()));
	public static final Item unholyCloak = make(prefix(LibItemNames.UNHOLY_CLOAK), new ItemUnholyCloak(unstackable()));
	public static final Item balanceCloak = make(prefix(LibItemNames.BALANCE_CLOAK), new ItemBalanceCloak(unstackable()));
	public static final Item invisibilityCloak = make(prefix(LibItemNames.INVISIBILITY_CLOAK), new ItemInvisibilityCloak(unstackable()));
	public static final Item thirdEye = make(prefix(LibItemNames.THIRD_EYE), new ItemThirdEye(unstackable()));
	public static final Item monocle = make(prefix(LibItemNames.MONOCLE), new ItemMonocle(unstackable()));
	public static final Item tinyPlanet = make(prefix(LibItemNames.TINY_PLANET), new ItemTinyPlanet(unstackable()));
	public static final Item goddessCharm = make(prefix(LibItemNames.GODDESS_CHARM), new ItemGoddessCharm(unstackable()));
	public static final Item divaCharm = make(prefix(LibItemNames.DIVA_CHARM), new ItemDivaCharm(unstackable().rarity(Rarity.UNCOMMON)));
	public static final Item itemFinder = make(prefix(LibItemNames.ITEM_FINDER), new ItemItemFinder(unstackable()));
	public static final Item flightTiara = make(prefix(LibItemNames.FLIGHT_TIARA), new ItemFlightTiara(unstackable().rarity(Rarity.UNCOMMON)));

	// Misc
	public static final Item manaCookie = make(prefix(LibItemNames.MANA_COOKIE), new Item(defaultBuilder().food(new FoodProperties.Builder().nutrition(0).saturationMod(0.1F).effect(new MobEffectInstance(MobEffects.SATURATION, 20, 0), 1).build())));
	public static final Item manaBottle = make(prefix(LibItemNames.MANA_BOTTLE), new ItemBottledMana(
			// Mark as food just to fool foxes into using it
			unstackable().food(new FoodProperties.Builder().alwaysEat().build())));
	public static final Item laputaShard = make(prefix(LibItemNames.LAPUTA_SHARD), new ItemLaputaShard(unstackable().rarity(Rarity.UNCOMMON)));
	public static final Item necroVirus = make(prefix(LibItemNames.NECRO_VIRUS), new ItemVirus(defaultBuilder()));
	public static final Item nullVirus = make(prefix(LibItemNames.NULL_VIRUS), new ItemVirus(defaultBuilder()));
	public static final Item spark = make(prefix(LibItemNames.SPARK), new ItemManaSpark(defaultBuilder()));
	public static final Item sparkUpgradeDispersive = make(prefix(LibItemNames.SPARK_UPGRADE + "_" + SparkUpgradeType.DISPERSIVE.name().toLowerCase(Locale.ROOT)), new ItemSparkUpgrade(/*ModPatterns.SPARK_DISPERSIVE, */defaultBuilder(), SparkUpgradeType.DISPERSIVE));
	public static final Item sparkUpgradeDominant = make(prefix(LibItemNames.SPARK_UPGRADE + "_" + SparkUpgradeType.DOMINANT.name().toLowerCase(Locale.ROOT)), new ItemSparkUpgrade(/*ModPatterns.SPARK_DOMINANT, */defaultBuilder(), SparkUpgradeType.DOMINANT));
	public static final Item sparkUpgradeRecessive = make(prefix(LibItemNames.SPARK_UPGRADE + "_" + SparkUpgradeType.RECESSIVE.name().toLowerCase(Locale.ROOT)), new ItemSparkUpgrade(/*ModPatterns.SPARK_RECESSIVE, */defaultBuilder(), SparkUpgradeType.RECESSIVE));
	public static final Item sparkUpgradeIsolated = make(prefix(LibItemNames.SPARK_UPGRADE + "_" + SparkUpgradeType.ISOLATED.name().toLowerCase(Locale.ROOT)), new ItemSparkUpgrade(/*ModPatterns.SPARK_ISOLATED, */defaultBuilder(), SparkUpgradeType.ISOLATED));
	public static final Item corporeaSpark = make(prefix(LibItemNames.CORPOREA_SPARK), new ItemCorporeaSpark(defaultBuilder()));
	public static final Item corporeaSparkMaster = make(prefix(LibItemNames.CORPOREA_SPARK_MASTER), new ItemCorporeaSpark(defaultBuilder()));
	public static final Item corporeaSparkCreative = make(prefix(LibItemNames.CORPOREA_SPARK_CREATIVE), new ItemCorporeaSpark(defaultBuilder().rarity(Rarity.EPIC)));
	public static final Item blackLotus = make(prefix(LibItemNames.BLACK_LOTUS), new ItemBlackLotus(defaultBuilder().rarity(Rarity.RARE)));
	public static final Item blackerLotus = make(prefix(LibItemNames.BLACKER_LOTUS), new ItemBlackLotus(defaultBuilder().rarity(Rarity.EPIC)));
	public static final Item worldSeed = make(prefix(LibItemNames.WORLD_SEED), new ItemWorldSeed(defaultBuilder()));
	public static final Item overgrowthSeed = make(prefix(LibItemNames.OVERGROWTH_SEED), new ItemOvergrowthSeed(defaultBuilder().rarity(Rarity.RARE)));
	public static final Item phantomInk = make(prefix(LibItemNames.PHANTOM_INK), new Item(defaultBuilder()));
	public static final Item poolMinecart = make(prefix(LibItemNames.POOL_MINECART), new ItemPoolMinecart(unstackable()));
	public static final Item keepIvy = make(prefix(LibItemNames.KEEP_IVY), new ItemKeepIvy(defaultBuilder()));
	public static final Item placeholder = make(prefix(LibItemNames.PLACEHOLDER), new ItemSelfReturning(defaultBuilder()));
	public static final Item craftPattern1_1 = make(prefix(LibItemNames.CRAFT_PATTERN_PREFIX + "1_1"), new ItemCraftPattern(CratePattern.CRAFTY_1_1, unstackable()));
	public static final Item craftPattern2_2 = make(prefix(LibItemNames.CRAFT_PATTERN_PREFIX + "2_2"), new ItemCraftPattern(CratePattern.CRAFTY_2_2, unstackable()));
	public static final Item craftPattern1_2 = make(prefix(LibItemNames.CRAFT_PATTERN_PREFIX + "1_2"), new ItemCraftPattern(CratePattern.CRAFTY_1_2, unstackable()));
	public static final Item craftPattern2_1 = make(prefix(LibItemNames.CRAFT_PATTERN_PREFIX + "2_1"), new ItemCraftPattern(CratePattern.CRAFTY_2_1, unstackable()));
	public static final Item craftPattern1_3 = make(prefix(LibItemNames.CRAFT_PATTERN_PREFIX + "1_3"), new ItemCraftPattern(CratePattern.CRAFTY_1_3, unstackable()));
	public static final Item craftPattern3_1 = make(prefix(LibItemNames.CRAFT_PATTERN_PREFIX + "3_1"), new ItemCraftPattern(CratePattern.CRAFTY_3_1, unstackable()));
	public static final Item craftPattern2_3 = make(prefix(LibItemNames.CRAFT_PATTERN_PREFIX + "2_3"), new ItemCraftPattern(CratePattern.CRAFTY_2_3, unstackable()));
	public static final Item craftPattern3_2 = make(prefix(LibItemNames.CRAFT_PATTERN_PREFIX + "3_2"), new ItemCraftPattern(CratePattern.CRAFTY_3_2, unstackable()));
	public static final Item craftPatternDonut = make(prefix(LibItemNames.CRAFT_PATTERN_PREFIX + "donut"), new ItemCraftPattern(CratePattern.CRAFTY_DONUT, unstackable()));

	// Guardian of Gaia drops
	public static final Item dice = make(prefix(LibItemNames.DICE), new ItemDice(unstackable().fireResistant().rarity(BotaniaAPI.instance().getRelicRarity())));
	public static final Item infiniteFruit = make(prefix(LibItemNames.INFINITE_FRUIT), new ItemInfiniteFruit(unstackable().fireResistant().rarity(BotaniaAPI.instance().getRelicRarity())));
	public static final Item kingKey = make(prefix(LibItemNames.KING_KEY), new ItemKingKey(unstackable().fireResistant().rarity(BotaniaAPI.instance().getRelicRarity())));
	public static final Item flugelEye = make(prefix(LibItemNames.FLUGEL_EYE), new ItemFlugelEye(unstackable().fireResistant().rarity(BotaniaAPI.instance().getRelicRarity())));
	public static final Item thorRing = make(prefix(LibItemNames.THOR_RING), new ItemThorRing(unstackable().fireResistant().rarity(BotaniaAPI.instance().getRelicRarity())));
	public static final Item odinRing = make(prefix(LibItemNames.ODIN_RING), new ItemOdinRing(unstackable().fireResistant().rarity(BotaniaAPI.instance().getRelicRarity())));
	public static final Item lokiRing = make(prefix(LibItemNames.LOKI_RING), new ItemLokiRing(unstackable().fireResistant().rarity(BotaniaAPI.instance().getRelicRarity())));
	public static final Item recordGaia1 = make(prefix(LibItemNames.RECORD_GAIA1), new ItemModRecord(1, ModSounds.gaiaMusic1, unstackable().rarity(Rarity.RARE)));
	public static final Item recordGaia2 = make(prefix(LibItemNames.RECORD_GAIA2), new ItemModRecord(1, ModSounds.gaiaMusic2, unstackable().rarity(Rarity.RARE)));
	public static final Item ancientWillAhrim = make(prefix(LibItemNames.ANCIENT_WILL_PREFIX + "ahrim"), new ItemAncientWill(IAncientWillContainer.AncientWillType.AHRIM, unstackable().rarity(Rarity.UNCOMMON)));
	public static final Item ancientWillDharok = make(prefix(LibItemNames.ANCIENT_WILL_PREFIX + "dharok"), new ItemAncientWill(IAncientWillContainer.AncientWillType.DHAROK, unstackable().rarity(Rarity.UNCOMMON)));
	public static final Item ancientWillGuthan = make(prefix(LibItemNames.ANCIENT_WILL_PREFIX + "guthan"), new ItemAncientWill(IAncientWillContainer.AncientWillType.GUTHAN, unstackable().rarity(Rarity.UNCOMMON)));
	public static final Item ancientWillTorag = make(prefix(LibItemNames.ANCIENT_WILL_PREFIX + "torag"), new ItemAncientWill(IAncientWillContainer.AncientWillType.TORAG, unstackable().rarity(Rarity.UNCOMMON)));
	public static final Item ancientWillVerac = make(prefix(LibItemNames.ANCIENT_WILL_PREFIX + "verac"), new ItemAncientWill(IAncientWillContainer.AncientWillType.VERAC, unstackable().rarity(Rarity.UNCOMMON)));
	public static final Item ancientWillKaril = make(prefix(LibItemNames.ANCIENT_WILL_PREFIX + "karil"), new ItemAncientWill(IAncientWillContainer.AncientWillType.KARIL, unstackable().rarity(Rarity.UNCOMMON)));
	public static final Item pinkinator = make(prefix(LibItemNames.PINKINATOR), new ItemPinkinator(unstackable().rarity(Rarity.UNCOMMON)));

	// Brewing
	public static final Item vial = make(prefix(LibItemNames.VIAL), new ItemVial(defaultBuilder()));
	public static final Item flask = make(prefix(LibItemNames.FLASK), new ItemVial(defaultBuilder()));
	public static final Item brewVial = make(prefix(LibItemNames.BREW_VIAL), new ItemBrewBase(unstackable(), 4, 32, () -> vial));
	public static final Item brewFlask = make(prefix(LibItemNames.BREW_FLASK), new ItemBrewBase(unstackable(), 6, 24, () -> flask));
	public static final Item bloodPendant = make(prefix(LibItemNames.BLOOD_PENDANT), new ItemBloodPendant(unstackable()));
	public static final Item incenseStick = make(prefix(LibItemNames.INCENSE_STICK), new ItemIncenseStick(unstackable()));

	// Cosmetics
	public static final Item blackBowtie = make(prefix(LibItemNames.COSMETIC_PREFIX + "black_bowtie"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.BLACK_BOWTIE, unstackable()));
	public static final Item blackTie = make(prefix(LibItemNames.COSMETIC_PREFIX + "black_tie"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.BLACK_TIE, unstackable()));
	public static final Item redGlasses = make(prefix(LibItemNames.COSMETIC_PREFIX + "red_glasses"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.RED_GLASSES, unstackable()));
	public static final Item puffyScarf = make(prefix(LibItemNames.COSMETIC_PREFIX + "puffy_scarf"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.PUFFY_SCARF, unstackable()));
	public static final Item engineerGoggles = make(prefix(LibItemNames.COSMETIC_PREFIX + "engineer_goggles"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.ENGINEER_GOGGLES, unstackable()));
	public static final Item eyepatch = make(prefix(LibItemNames.COSMETIC_PREFIX + "eyepatch"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.EYEPATCH, unstackable()));
	public static final Item wickedEyepatch = make(prefix(LibItemNames.COSMETIC_PREFIX + "wicked_eyepatch"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.WICKED_EYEPATCH, unstackable()));
	public static final Item redRibbons = make(prefix(LibItemNames.COSMETIC_PREFIX + "red_ribbons"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.RED_RIBBONS, unstackable()));
	public static final Item pinkFlowerBud = make(prefix(LibItemNames.COSMETIC_PREFIX + "pink_flower_bud"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.PINK_FLOWER_BUD, unstackable()));
	public static final Item polkaDottedBows = make(prefix(LibItemNames.COSMETIC_PREFIX + "polka_dotted_bows"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.POLKA_DOTTED_BOWS, unstackable()));
	public static final Item blueButterfly = make(prefix(LibItemNames.COSMETIC_PREFIX + "blue_butterfly"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.BLUE_BUTTERFLY, unstackable()));
	public static final Item catEars = make(prefix(LibItemNames.COSMETIC_PREFIX + "cat_ears"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.CAT_EARS, unstackable()));
	public static final Item witchPin = make(prefix(LibItemNames.COSMETIC_PREFIX + "witch_pin"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.WITCH_PIN, unstackable()));
	public static final Item devilTail = make(prefix(LibItemNames.COSMETIC_PREFIX + "devil_tail"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.DEVIL_TAIL, unstackable()));
	public static final Item kamuiEye = make(prefix(LibItemNames.COSMETIC_PREFIX + "kamui_eye"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.KAMUI_EYE, unstackable()));
	public static final Item googlyEyes = make(prefix(LibItemNames.COSMETIC_PREFIX + "googly_eyes"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.GOOGLY_EYES, unstackable()));
	public static final Item fourLeafClover = make(prefix(LibItemNames.COSMETIC_PREFIX + "four_leaf_clover"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.FOUR_LEAF_CLOVER, unstackable()));
	public static final Item clockEye = make(prefix(LibItemNames.COSMETIC_PREFIX + "clock_eye"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.CLOCK_EYE, unstackable()));
	public static final Item unicornHorn = make(prefix(LibItemNames.COSMETIC_PREFIX + "unicorn_horn"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.UNICORN_HORN, unstackable()));
	public static final Item devilHorns = make(prefix(LibItemNames.COSMETIC_PREFIX + "devil_horns"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.DEVIL_HORNS, unstackable()));
	public static final Item hyperPlus = make(prefix(LibItemNames.COSMETIC_PREFIX + "hyper_plus"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.HYPER_PLUS, unstackable()));
	public static final Item botanistEmblem = make(prefix(LibItemNames.COSMETIC_PREFIX + "botanist_emblem"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.BOTANIST_EMBLEM, unstackable()));
	public static final Item ancientMask = make(prefix(LibItemNames.COSMETIC_PREFIX + "ancient_mask"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.ANCIENT_MASK, unstackable()));
	public static final Item eerieMask = make(prefix(LibItemNames.COSMETIC_PREFIX + "eerie_mask"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.EERIE_MASK, unstackable()));
	public static final Item alienAntenna = make(prefix(LibItemNames.COSMETIC_PREFIX + "alien_antenna"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.ALIEN_ANTENNA, unstackable()));
	public static final Item anaglyphGlasses = make(prefix(LibItemNames.COSMETIC_PREFIX + "anaglyph_glasses"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.ANAGLYPH_GLASSES, unstackable()));
	public static final Item orangeShades = make(prefix(LibItemNames.COSMETIC_PREFIX + "orange_shades"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.ORANGE_SHADES, unstackable()));
	public static final Item grouchoGlasses = make(prefix(LibItemNames.COSMETIC_PREFIX + "groucho_glasses"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.GROUCHO_GLASSES, unstackable()));
	public static final Item thickEyebrows = make(prefix(LibItemNames.COSMETIC_PREFIX + "thick_eyebrows"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.THICK_EYEBROWS, unstackable()));
	public static final Item lusitanicShield = make(prefix(LibItemNames.COSMETIC_PREFIX + "lusitanic_shield"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.LUSITANIC_SHIELD, unstackable()));
	public static final Item tinyPotatoMask = make(prefix(LibItemNames.COSMETIC_PREFIX + "tiny_potato_mask"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.TINY_POTATO_MASK, unstackable()));
	public static final Item questgiverMark = make(prefix(LibItemNames.COSMETIC_PREFIX + "questgiver_mark"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.QUESTGIVER_MARK, unstackable()));
	public static final Item thinkingHand = make(prefix(LibItemNames.COSMETIC_PREFIX + "thinking_hand"), new ItemBaubleCosmetic(ItemBaubleCosmetic.Variant.THINKING_HAND, unstackable()));

	public static final MenuType<ContainerBaubleBox> BAUBLE_BOX_CONTAINER = IXplatAbstractions.INSTANCE.createMenuType(ContainerBaubleBox::fromNetwork);
	public static final MenuType<ContainerFlowerBag> FLOWER_BAG_CONTAINER = IXplatAbstractions.INSTANCE.createMenuType(ContainerFlowerBag::fromNetwork);

	private static <T extends Item> T make(ResourceLocation id, T item) {
		var old = ALL.put(id, item);
		if (old != null) {
			throw new IllegalArgumentException("Typo? Duplicate id " + id);
		}
		return item;
	}

	public static Item.Properties defaultBuilder() {
		return IXplatAbstractions.INSTANCE.defaultItemBuilder();
	}

	// Forge does custom damage by just implementing a method on Item,
	// Fabric does it by an extra lambda to the Properties object
	public static Item.Properties defaultBuilderCustomDamage() {
		return IXplatAbstractions.INSTANCE.defaultItemBuilderWithCustomDamageOnFabric();
	}

	public static Item.Properties unstackableCustomDamage() {
		return defaultBuilderCustomDamage().stacksTo(1);
	}

	private static Item.Properties stackTo16() {
		return defaultBuilder().stacksTo(16);

	}

	private static Item.Properties unstackable() {
		return defaultBuilder().stacksTo(1);
	}

	public static void registerItems(BiConsumer<Item, ResourceLocation> r) {
		for (var e : ALL.entrySet()) {
			r.accept(e.getValue(), e.getKey());
		}
	}

	public static void registerMenuTypes(BiConsumer<MenuType<?>, ResourceLocation> consumer) {
		consumer.accept(BAUBLE_BOX_CONTAINER, prefix(LibItemNames.BAUBLE_BOX));
		consumer.accept(FLOWER_BAG_CONTAINER, prefix(LibItemNames.FLOWER_BAG));
	}

	public static void registerRecipeSerializers(BiConsumer<RecipeSerializer<?>, ResourceLocation> r) {
		r.accept(AncientWillRecipe.SERIALIZER, prefix("ancient_will_attach"));
		r.accept(ArmorUpgradeRecipe.SERIALIZER, prefix("armor_upgrade"));
		r.accept(BlackHoleTalismanExtractRecipe.SERIALIZER, prefix("black_hole_talisman_extract"));
		r.accept(CompositeLensRecipe.SERIALIZER, prefix("composite_lens"));
		r.accept(CosmeticAttachRecipe.SERIALIZER, prefix("cosmetic_attach"));
		r.accept(CosmeticRemoveRecipe.SERIALIZER, prefix("cosmetic_remove"));
		r.accept(GogAlternationRecipe.SERIALIZER, prefix("gog_alternation"));
		r.accept(KeepIvyRecipe.SERIALIZER, prefix("keep_ivy"));
		r.accept(LaputaShardUpgradeRecipe.SERIALIZER, prefix("laputa_shard_upgrade"));
		r.accept(LensDyeingRecipe.SERIALIZER, prefix("lens_dye"));
		r.accept(ManaGunClipRecipe.SERIALIZER, prefix("mana_gun_add_clip"));
		r.accept(ManaGunLensRecipe.SERIALIZER, prefix("mana_gun_add_lens"));
		r.accept(ManaGunRemoveLensRecipe.SERIALIZER, prefix("mana_gun_remove_lens"));
		r.accept(ManaUpgradeRecipe.SERIALIZER, prefix("mana_upgrade"));
		r.accept(ShapelessManaUpgradeRecipe.SERIALIZER, prefix("mana_upgrade_shapeless"));
		r.accept(MergeVialRecipe.SERIALIZER, prefix("merge_vial"));
		r.accept(NbtOutputRecipe.SERIALIZER, prefix("nbt_output_wrapper"));
		r.accept(PhantomInkRecipe.SERIALIZER, prefix("phantom_ink_apply"));
		r.accept(SpellClothRecipe.SERIALIZER, prefix("spell_cloth_apply"));
		r.accept(SplitLensRecipe.SERIALIZER, prefix("split_lens"));
		r.accept(TerraPickTippingRecipe.SERIALIZER, prefix("terra_pick_tipping"));
		r.accept(TwigWandRecipe.SERIALIZER, prefix("twig_wand"));
		r.accept(WaterBottleMatchingRecipe.SERIALIZER, prefix("water_bottle_matching_shaped"));

		ModPatterns.init();
	}

	public static Item getPetal(DyeColor color) {
		return switch (color) {
			case WHITE -> whitePetal;
			case ORANGE -> orangePetal;
			case MAGENTA -> magentaPetal;
			case LIGHT_BLUE -> lightBluePetal;
			case YELLOW -> yellowPetal;
			case LIME -> limePetal;
			case PINK -> pinkPetal;
			case GRAY -> grayPetal;
			case LIGHT_GRAY -> lightGrayPetal;
			case CYAN -> cyanPetal;
			case PURPLE -> purplePetal;
			case BLUE -> bluePetal;
			case BROWN -> brownPetal;
			case GREEN -> greenPetal;
			case RED -> redPetal;
			case BLACK -> blackPetal;
		};
	}

	public static boolean isNoDespawn(Item item) {
		return item instanceof ItemManaTablet || item instanceof ItemManaRing || item instanceof ItemTerraPick
				|| item instanceof ItemRelic || item instanceof ItemRelicBauble;
	}

}
