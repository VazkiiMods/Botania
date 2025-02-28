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
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeSerializer;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.block.FloatingFlower;
import vazkii.botania.api.item.AncientWillContainer;
import vazkii.botania.api.mana.spark.SparkUpgradeType;
import vazkii.botania.api.state.enums.CraftyCratePattern;
import vazkii.botania.client.gui.bag.FlowerPouchContainer;
import vazkii.botania.client.gui.box.BaubleBoxContainer;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.crafting.recipe.*;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.item.brew.BaseBrewItem;
import vazkii.botania.common.item.brew.IncenseStickItem;
import vazkii.botania.common.item.brew.VialItem;
import vazkii.botania.common.item.equipment.armor.elementium.ElementiumBootsItem;
import vazkii.botania.common.item.equipment.armor.elementium.ElementiumChestItem;
import vazkii.botania.common.item.equipment.armor.elementium.ElementiumHelmItem;
import vazkii.botania.common.item.equipment.armor.elementium.ElementiumLegsItem;
import vazkii.botania.common.item.equipment.armor.manasteel.ManasteelArmorItem;
import vazkii.botania.common.item.equipment.armor.manasteel.ManasteelHelmItem;
import vazkii.botania.common.item.equipment.armor.manaweave.ManaweaveArmorItem;
import vazkii.botania.common.item.equipment.armor.manaweave.ManaweaveHelmItem;
import vazkii.botania.common.item.equipment.armor.terrasteel.TerrasteelArmorItem;
import vazkii.botania.common.item.equipment.armor.terrasteel.TerrasteelHelmItem;
import vazkii.botania.common.item.equipment.bauble.*;
import vazkii.botania.common.item.equipment.tool.SoulscribeItem;
import vazkii.botania.common.item.equipment.tool.StarcallerItem;
import vazkii.botania.common.item.equipment.tool.ThundercallerItem;
import vazkii.botania.common.item.equipment.tool.VitreousPickaxeItem;
import vazkii.botania.common.item.equipment.tool.bow.CrystalBowItem;
import vazkii.botania.common.item.equipment.tool.bow.LivingwoodBowItem;
import vazkii.botania.common.item.equipment.tool.elementium.*;
import vazkii.botania.common.item.equipment.tool.manasteel.*;
import vazkii.botania.common.item.equipment.tool.terrasteel.TerraBladeItem;
import vazkii.botania.common.item.equipment.tool.terrasteel.TerraShattererItem;
import vazkii.botania.common.item.equipment.tool.terrasteel.TerraTruncatorItem;
import vazkii.botania.common.item.lens.*;
import vazkii.botania.common.item.material.*;
import vazkii.botania.common.item.record.BotaniaRecordItem;
import vazkii.botania.common.item.relic.*;
import vazkii.botania.common.item.rod.*;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaItems {
	private static final Map<ResourceLocation, Item> ALL = new LinkedHashMap<>(); // Preserve insertion order
	public static final LexicaBotaniaItem lexicon = make(prefix(LibItemNames.LEXICON), new LexicaBotaniaItem(unstackable().rarity(Rarity.UNCOMMON)));
	public static final Item twigWand = make(prefix(LibItemNames.TWIG_WAND), new WandOfTheForestItem(ChatFormatting.DARK_GREEN, unstackable().rarity(Rarity.RARE)));
	public static final Item dreamwoodWand = make(prefix(LibItemNames.DREAMWOOD_WAND), new WandOfTheForestItem(ChatFormatting.LIGHT_PURPLE, unstackable().rarity(Rarity.RARE)));
	public static final Item obedienceStick = make(prefix(LibItemNames.OBEDIENCE_STICK), new FloralObedienceStickItem(unstackable()));
	public static final Item fertilizer = make(prefix(LibItemNames.FERTILIZER), new FloralFertilizerItem(defaultBuilder()));

	public static final Item whitePetal = makePetal(DyeColor.WHITE);
	public static final Item lightGrayPetal = makePetal(DyeColor.LIGHT_GRAY);
	public static final Item grayPetal = makePetal(DyeColor.GRAY);
	public static final Item blackPetal = makePetal(DyeColor.BLACK);
	public static final Item brownPetal = makePetal(DyeColor.BROWN);
	public static final Item redPetal = makePetal(DyeColor.RED);
	public static final Item orangePetal = makePetal(DyeColor.ORANGE);
	public static final Item yellowPetal = makePetal(DyeColor.YELLOW);
	public static final Item limePetal = makePetal(DyeColor.LIME);
	public static final Item greenPetal = makePetal(DyeColor.GREEN);
	public static final Item cyanPetal = makePetal(DyeColor.CYAN);
	public static final Item lightBluePetal = makePetal(DyeColor.LIGHT_BLUE);
	public static final Item bluePetal = makePetal(DyeColor.BLUE);
	public static final Item purplePetal = makePetal(DyeColor.PURPLE);
	public static final Item magentaPetal = makePetal(DyeColor.MAGENTA);
	public static final Item pinkPetal = makePetal(DyeColor.PINK);

	public static final Item manaSteel = make(prefix(LibItemNames.MANASTEEL_INGOT), new Item(defaultBuilder()));
	public static final Item manaPearl = make(prefix(LibItemNames.MANA_PEARL), new Item(defaultBuilder()));
	public static final Item manaDiamond = make(prefix(LibItemNames.MANA_DIAMOND), new Item(defaultBuilder()));
	public static final Item livingwoodTwig = make(prefix(LibItemNames.LIVINGWOOD_TWIG), new BotaniaBannerPatternItem(BotaniaTags.BannerPatterns.PATTERN_ITEM_LIVINGWOOD_TWIG, defaultBuilder()));
	public static final Item terrasteel = make(prefix(LibItemNames.TERRASTEEL_INGOT), new TerrasteelIngotItem(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item lifeEssence = make(prefix(LibItemNames.LIFE_ESSENCE), new Item(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item redstoneRoot = make(prefix(LibItemNames.REDSTONE_ROOT), new Item(defaultBuilder()));
	public static final Item elementium = make(prefix(LibItemNames.ELEMENTIUM_INGOT), new Item(defaultBuilder()));
	public static final Item pixieDust = make(prefix(LibItemNames.PIXIE_DUST), new Item(defaultBuilder()));
	public static final Item dragonstone = make(prefix(LibItemNames.DRAGONSTONE), new Item(defaultBuilder()));
	public static final Item redString = make(prefix(LibItemNames.RED_STRING), new Item(defaultBuilder()));
	public static final Item dreamwoodTwig = make(prefix(LibItemNames.DREAMWOOD_TWIG), new BotaniaBannerPatternItem(BotaniaTags.BannerPatterns.PATTERN_ITEM_DREAMWOOD_TWIG, defaultBuilder()));
	public static final Item gaiaIngot = make(prefix(LibItemNames.GAIA_INGOT), new ManaResourceItem(defaultBuilder().rarity(Rarity.RARE)));
	public static final Item enderAirBottle = make(prefix(LibItemNames.ENDER_AIR_BOTTLE), new EnderAirItem(defaultBuilder()));
	public static final Item manaString = make(prefix(LibItemNames.MANA_STRING), new Item(defaultBuilder()));
	public static final Item manasteelNugget = make(prefix(LibItemNames.MANASTEEL_NUGGET), new Item(defaultBuilder()));
	public static final Item terrasteelNugget = make(prefix(LibItemNames.TERRASTEEL_NUGGET), new Item(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item elementiumNugget = make(prefix(LibItemNames.ELEMENTIUM_NUGGET), new Item(defaultBuilder()));
	public static final Item livingroot = make(prefix(LibItemNames.LIVING_ROOT), new ManaResourceItem(defaultBuilder()));
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

	public static final Item lensNormal = make(prefix(LibItemNames.LENS_NORMAL), new LensItem(stackTo16(), new Lens(), LensItem.PROP_NONE));
	public static final Item lensSpeed = make(prefix(LibItemNames.LENS_SPEED), new LensItem(stackTo16(), new VelocityLens(), LensItem.PROP_NONE));
	public static final Item lensPower = make(prefix(LibItemNames.LENS_POWER), new LensItem(stackTo16(), new PotencyLens(), LensItem.PROP_POWER));
	public static final Item lensTime = make(prefix(LibItemNames.LENS_TIME), new LensItem(stackTo16(), new ResistanceLens(), LensItem.PROP_NONE));
	public static final Item lensEfficiency = make(prefix(LibItemNames.LENS_EFFICIENCY), new LensItem(stackTo16(), new EfficiencyLens(), LensItem.PROP_NONE));
	public static final Item lensBounce = make(prefix(LibItemNames.LENS_BOUNCE), new LensItem(stackTo16(), new BounceLens(), LensItem.PROP_TOUCH));
	public static final Item lensGravity = make(prefix(LibItemNames.LENS_GRAVITY), new LensItem(stackTo16(), new GravityLens(), LensItem.PROP_ORIENTATION));
	public static final Item lensMine = make(prefix(LibItemNames.LENS_MINE), new LensItem(stackTo16(), new BoreLens(), LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));
	public static final Item lensDamage = make(prefix(LibItemNames.LENS_DAMAGE), new LensItem(stackTo16(), new DamagingLens(), LensItem.PROP_DAMAGE));
	public static final Item lensPhantom = make(prefix(LibItemNames.LENS_PHANTOM), new LensItem(stackTo16(), new PhantomLens(), LensItem.PROP_TOUCH));
	public static final Item lensMagnet = make(prefix(LibItemNames.LENS_MAGNET), new LensItem(stackTo16(), new MagnetizingLens(), LensItem.PROP_ORIENTATION));
	public static final Item lensExplosive = make(prefix(LibItemNames.LENS_EXPLOSIVE), new LensItem(stackTo16(), new EntropicLens(), LensItem.PROP_DAMAGE | LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));
	public static final Item lensInfluence = make(prefix(LibItemNames.LENS_INFLUENCE), new LensItem(stackTo16(), new InfluenceLens(), LensItem.PROP_NONE));
	public static final Item lensWeight = make(prefix(LibItemNames.LENS_WEIGHT), new LensItem(stackTo16(), new WeightLens(), LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));
	public static final Item lensPaint = make(prefix(LibItemNames.LENS_PAINT), new LensItem(stackTo16(), new PaintslingerLens(), LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));
	public static final Item lensFire = make(prefix(LibItemNames.LENS_FIRE), new LensItem(stackTo16(), new KindleLens(), LensItem.PROP_DAMAGE | LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));
	public static final Item lensPiston = make(prefix(LibItemNames.LENS_PISTON), new LensItem(stackTo16(), new ForceLens(), LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));
	public static final Item lensLight = make(prefix(LibItemNames.LENS_LIGHT), new LensItem(stackTo16(), new FlashLens(), LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));
	public static final Item lensWarp = make(prefix(LibItemNames.LENS_WARP), new LensItem(stackTo16(), new WarpLens(), LensItem.PROP_NONE));
	public static final Item lensRedirect = make(prefix(LibItemNames.LENS_REDIRECT), new LensItem(stackTo16(), new RedirectiveLens(), LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));
	public static final Item lensFirework = make(prefix(LibItemNames.LENS_FIREWORK), new LensItem(stackTo16(), new CelebratoryLens(), LensItem.PROP_TOUCH));
	public static final Item lensFlare = make(prefix(LibItemNames.LENS_FLARE), new LensItem(stackTo16(), new FlareLens(), LensItem.PROP_CONTROL));
	public static final Item lensMessenger = make(prefix(LibItemNames.LENS_MESSENGER), new LensItem(stackTo16(), new MessengerLens(), LensItem.PROP_POWER));
	public static final Item lensTripwire = make(prefix(LibItemNames.LENS_TRIPWIRE), new LensItem(stackTo16(), new TripwireLens(), LensItem.PROP_CONTROL));
	public static final Item lensStorm = make(prefix(LibItemNames.LENS_STORM), new LensItem(stackTo16().rarity(Rarity.EPIC), new StormLens(), LensItem.PROP_NONE));

	public static final Item runeWater = make(prefix(LibItemNames.RUNE_WATER), new RuneItem(defaultBuilder()));
	public static final Item runeFire = make(prefix(LibItemNames.RUNE_FIRE), new RuneItem(defaultBuilder()));
	public static final Item runeEarth = make(prefix(LibItemNames.RUNE_EARTH), new RuneItem(defaultBuilder()));
	public static final Item runeAir = make(prefix(LibItemNames.RUNE_AIR), new RuneItem(defaultBuilder()));
	public static final Item runeSpring = make(prefix(LibItemNames.RUNE_SPRING), new RuneItem(defaultBuilder()));
	public static final Item runeSummer = make(prefix(LibItemNames.RUNE_SUMMER), new RuneItem(defaultBuilder()));
	public static final Item runeAutumn = make(prefix(LibItemNames.RUNE_AUTUMN), new RuneItem(defaultBuilder()));
	public static final Item runeWinter = make(prefix(LibItemNames.RUNE_WINTER), new RuneItem(defaultBuilder()));
	public static final Item runeMana = make(prefix(LibItemNames.RUNE_MANA), new RuneItem(defaultBuilder()));
	public static final Item runeLust = make(prefix(LibItemNames.RUNE_LUST), new RuneItem(defaultBuilder()));
	public static final Item runeGluttony = make(prefix(LibItemNames.RUNE_GLUTTONY), new RuneItem(defaultBuilder()));
	public static final Item runeGreed = make(prefix(LibItemNames.RUNE_GREED), new RuneItem(defaultBuilder()));
	public static final Item runeSloth = make(prefix(LibItemNames.RUNE_SLOTH), new RuneItem(defaultBuilder()));
	public static final Item runeWrath = make(prefix(LibItemNames.RUNE_WRATH), new RuneItem(defaultBuilder()));
	public static final Item runeEnvy = make(prefix(LibItemNames.RUNE_ENVY), new RuneItem(defaultBuilder()));
	public static final Item runePride = make(prefix(LibItemNames.RUNE_PRIDE), new RuneItem(defaultBuilder()));

	public static final Item grassSeeds = make(prefix(LibItemNames.GRASS_SEEDS), new GrassSeedsItem(FloatingFlower.IslandType.GRASS, defaultBuilder()));
	public static final Item podzolSeeds = make(prefix(LibItemNames.PODZOL_SEEDS), new GrassSeedsItem(FloatingFlower.IslandType.PODZOL, defaultBuilder()));
	public static final Item mycelSeeds = make(prefix(LibItemNames.MYCEL_SEEDS), new GrassSeedsItem(FloatingFlower.IslandType.MYCEL, defaultBuilder()));
	public static final Item drySeeds = make(prefix(LibItemNames.DRY_SEEDS), new GrassSeedsItem(FloatingFlower.IslandType.DRY, defaultBuilder()));
	public static final Item goldenSeeds = make(prefix(LibItemNames.GOLDEN_SEEDS), new GrassSeedsItem(FloatingFlower.IslandType.GOLDEN, defaultBuilder()));
	public static final Item vividSeeds = make(prefix(LibItemNames.VIVID_SEEDS), new GrassSeedsItem(FloatingFlower.IslandType.VIVID, defaultBuilder()));
	public static final Item scorchedSeeds = make(prefix(LibItemNames.SCORCHED_SEEDS), new GrassSeedsItem(FloatingFlower.IslandType.SCORCHED, defaultBuilder()));
	public static final Item infusedSeeds = make(prefix(LibItemNames.INFUSED_SEEDS), new GrassSeedsItem(FloatingFlower.IslandType.INFUSED, defaultBuilder()));
	public static final Item mutatedSeeds = make(prefix(LibItemNames.MUTATED_SEEDS), new GrassSeedsItem(FloatingFlower.IslandType.MUTATED, defaultBuilder()));

	public static final Item dirtRod = make(prefix(LibItemNames.DIRT_ROD), new LandsRodItem(unstackable()));
	public static final Item skyDirtRod = make(prefix(LibItemNames.SKY_DIRT_ROD), new HighlandsRodItem(unstackable()));
	public static final Item terraformRod = make(prefix(LibItemNames.TERRAFORM_ROD), new TerraFirmaRodItem(unstackable().rarity(Rarity.UNCOMMON)));
	public static final Item cobbleRod = make(prefix(LibItemNames.COBBLE_ROD), new DepthsRodItem(unstackable()));
	public static final Item waterRod = make(prefix(LibItemNames.WATER_ROD), new SeasRodItem(unstackable()));
	public static final Item tornadoRod = make(prefix(LibItemNames.TORNADO_ROD), new SkiesRodItem(unstackable()));
	public static final Item fireRod = make(prefix(LibItemNames.FIRE_ROD), new HellsRodItem(unstackable()));
	public static final Item diviningRod = make(prefix(LibItemNames.DIVINING_ROD), new PlentifulMantleRodItem(unstackable()));
	public static final Item smeltRod = make(prefix(LibItemNames.SMELT_ROD), new MoltenCoreRodItem(unstackable()));
	public static final Item exchangeRod = make(prefix(LibItemNames.EXCHANGE_ROD), new ShiftingCrustRodItem(unstackable()));
	public static final Item rainbowRod = make(prefix(LibItemNames.RAINBOW_ROD), new BifrostRodItem(unstackable()));
	public static final Item gravityRod = make(prefix(LibItemNames.GRAVITY_ROD), new ShadedMesaRodItem(unstackable()));
	public static final Item missileRod = make(prefix(LibItemNames.MISSILE_ROD), new UnstableReservoirRodItem(unstackable().rarity(Rarity.UNCOMMON)));

	// Equipment
	public static final Item manasteelHelm = make(prefix(LibItemNames.MANASTEEL_HELM), new ManasteelHelmItem(unstackableCustomDamage()));
	public static final Item manasteelChest = make(prefix(LibItemNames.MANASTEEL_CHEST), new ManasteelArmorItem(ArmorItem.Type.CHESTPLATE, unstackableCustomDamage()));
	public static final Item manasteelLegs = make(prefix(LibItemNames.MANASTEEL_LEGS), new ManasteelArmorItem(ArmorItem.Type.LEGGINGS, unstackableCustomDamage()));
	public static final Item manasteelBoots = make(prefix(LibItemNames.MANASTEEL_BOOTS), new ManasteelArmorItem(ArmorItem.Type.BOOTS, unstackableCustomDamage()));
	public static final Item manasteelPick = make(prefix(LibItemNames.MANASTEEL_PICK), new ManasteelPickaxeItem(unstackableCustomDamage()));
	public static final Item manasteelShovel = make(prefix(LibItemNames.MANASTEEL_SHOVEL), new ManasteelShovelItem(unstackableCustomDamage()));
	public static final Item manasteelAxe = make(prefix(LibItemNames.MANASTEEL_AXE), new ManasteelAxeItem(unstackableCustomDamage()));
	public static final Item manasteelHoe = make(prefix(LibItemNames.MANASTEEL_HOE), new ManasteelHoeItem(unstackableCustomDamage()));
	public static final Item manasteelSword = make(prefix(LibItemNames.MANASTEEL_SWORD), new ManasteelSwordItem(unstackableCustomDamage()));
	public static final Item manasteelShears = make(prefix(LibItemNames.MANASTEEL_SHEARS), new ManasteelShearsItem(unstackableCustomDamage().defaultDurability(238)));
	public static final Item elementiumHelm = make(prefix(LibItemNames.ELEMENTIUM_HELM), new ElementiumHelmItem(unstackableCustomDamage()));
	public static final Item elementiumChest = make(prefix(LibItemNames.ELEMENTIUM_CHEST), new ElementiumChestItem(unstackableCustomDamage()));
	public static final Item elementiumLegs = make(prefix(LibItemNames.ELEMENTIUM_LEGS), new ElementiumLegsItem(unstackableCustomDamage()));
	public static final Item elementiumBoots = make(prefix(LibItemNames.ELEMENTIUM_BOOTS), new ElementiumBootsItem(unstackableCustomDamage()));
	public static final Item elementiumPick = make(prefix(LibItemNames.ELEMENTIUM_PICK), new ElementiumPickaxeItem(unstackableCustomDamage()));
	public static final Item elementiumShovel = make(prefix(LibItemNames.ELEMENTIUM_SHOVEL), new ElementiumShovelItem(unstackableCustomDamage()));
	public static final Item elementiumAxe = make(prefix(LibItemNames.ELEMENTIUM_AXE), new ElementiumAxeItem(unstackableCustomDamage()));
	public static final Item elementiumHoe = make(prefix(LibItemNames.ELEMENTIUM_HOE), new ElementiumHoeItem(unstackableCustomDamage()));
	public static final Item elementiumSword = make(prefix(LibItemNames.ELEMENTIUM_SWORD), new ElementiumSwordItem(unstackableCustomDamage()));
	public static final Item elementiumShears = make(prefix(LibItemNames.ELEMENTIUM_SHEARS), new ElementiumShearsItem(unstackableCustomDamage().defaultDurability(238)));
	public static final Item terrasteelHelm = make(prefix(LibItemNames.TERRASTEEL_HELM), new TerrasteelHelmItem(unstackableCustomDamage().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final Item terrasteelChest = make(prefix(LibItemNames.TERRASTEEL_CHEST), new TerrasteelArmorItem(ArmorItem.Type.CHESTPLATE, unstackableCustomDamage().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final Item terrasteelLegs = make(prefix(LibItemNames.TERRASTEEL_LEGS), new TerrasteelArmorItem(ArmorItem.Type.LEGGINGS, unstackableCustomDamage().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final Item terrasteelBoots = make(prefix(LibItemNames.TERRASTEEL_BOOTS), new TerrasteelArmorItem(ArmorItem.Type.BOOTS, unstackableCustomDamage().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final Item terraPick = make(prefix(LibItemNames.TERRA_PICK), new TerraShattererItem(unstackableCustomDamage().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final Item terraAxe = make(prefix(LibItemNames.TERRA_AXE), new TerraTruncatorItem(unstackableCustomDamage().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final Item terraSword = make(prefix(LibItemNames.TERRA_SWORD), new TerraBladeItem(unstackableCustomDamage().fireResistant().rarity(Rarity.UNCOMMON)));
	public static final Item starSword = make(prefix(LibItemNames.STAR_SWORD), new StarcallerItem(unstackableCustomDamage().rarity(Rarity.UNCOMMON)));
	public static final Item thunderSword = make(prefix(LibItemNames.THUNDER_SWORD), new ThundercallerItem(unstackableCustomDamage().rarity(Rarity.UNCOMMON)));
	public static final Item manaweaveHelm = make(prefix(LibItemNames.MANAWEAVE_HELM), new ManaweaveHelmItem(unstackableCustomDamage()));
	public static final Item manaweaveChest = make(prefix(LibItemNames.MANAWEAVE_CHEST), new ManaweaveArmorItem(ArmorItem.Type.CHESTPLATE, unstackableCustomDamage()));
	public static final Item manaweaveLegs = make(prefix(LibItemNames.MANAWEAVE_LEGS), new ManaweaveArmorItem(ArmorItem.Type.LEGGINGS, unstackableCustomDamage()));
	public static final Item manaweaveBoots = make(prefix(LibItemNames.MANAWEAVE_BOOTS), new ManaweaveArmorItem(ArmorItem.Type.BOOTS, unstackableCustomDamage()));
	public static final Item enderDagger = make(prefix(LibItemNames.ENDER_DAGGER), new SoulscribeItem(unstackable().defaultDurability(69))); // What you looking at?
	public static final Item glassPick = make(prefix(LibItemNames.GLASS_PICK), new VitreousPickaxeItem(unstackableCustomDamage()));
	public static final Item livingwoodBow = make(prefix(LibItemNames.LIVINGWOOD_BOW), new LivingwoodBowItem(defaultBuilderCustomDamage().defaultDurability(500)));
	public static final Item crystalBow = make(prefix(LibItemNames.CRYSTAL_BOW), new CrystalBowItem(defaultBuilderCustomDamage().defaultDurability(500)));
	public static final Item thornChakram = make(prefix(LibItemNames.THORN_CHAKRAM), new ThornChakramItem(defaultBuilder().stacksTo(6)));
	public static final Item flareChakram = make(prefix(LibItemNames.FLARE_CHAKRAM), new ThornChakramItem(defaultBuilder().stacksTo(6)));

	// Misc tools
	public static final Item manaTablet = make(prefix(LibItemNames.MANA_TABLET), new ManaTabletItem(unstackable()));
	public static final Item manaMirror = make(prefix(LibItemNames.MANA_MIRROR), new ManaMirrorItem(unstackable()));
	public static final Item manaGun = make(prefix(LibItemNames.MANA_GUN), new ManaBlasterItem(unstackable()));
	public static final Item clip = make(prefix(LibItemNames.CLIP), new Item(unstackable()));
	public static final Item grassHorn = make(prefix(LibItemNames.GRASS_HORN), new HornItem(unstackable()));
	public static final Item leavesHorn = make(prefix(LibItemNames.LEAVES_HORN), new HornItem(unstackable()));
	public static final Item snowHorn = make(prefix(LibItemNames.SNOW_HORN), new HornItem(unstackable()));
	public static final Item vineBall = make(prefix(LibItemNames.VINE_BALL), new VineBallItem(defaultBuilder()));
	public static final Item slingshot = make(prefix(LibItemNames.SLINGSHOT), new LivingwoodSlingshotItem(unstackable()));
	public static final Item openBucket = make(prefix(LibItemNames.OPEN_BUCKET), new ExtrapolatedBucketItem(unstackable()));
	public static final Item spawnerMover = make(prefix(LibItemNames.SPAWNER_MOVER), new LifeAggregatorItem(unstackable().rarity(Rarity.UNCOMMON)));
	public static final Item enderHand = make(prefix(LibItemNames.ENDER_HAND), new EnderHandItem(unstackable()));
	public static final Item craftingHalo = make(prefix(LibItemNames.CRAFTING_HALO), new AssemblyHaloItem(unstackable()));
	public static final Item autocraftingHalo = make(prefix(LibItemNames.AUTOCRAFTING_HALO), new ManufactoryHaloItem(unstackable()));
	public static final Item spellCloth = make(prefix(LibItemNames.SPELL_CLOTH), new SpellbindingClothItem(XplatAbstractions.INSTANCE.noRepairOnForge(unstackable().defaultDurability(35))));
	public static final Item flowerBag = make(prefix(LibItemNames.FLOWER_BAG), new FlowerPouchItem(unstackable()));
	public static final Item blackHoleTalisman = make(prefix(LibItemNames.BLACK_HOLE_TALISMAN), new BlackHoleTalismanItem(unstackable()));
	public static final Item temperanceStone = make(prefix(LibItemNames.TEMPERANCE_STONE), new StoneOfTemperanceItem(unstackable()));
	public static final Item waterBowl = make(prefix(LibItemNames.WATER_BOWL), new WaterBowlItem(unstackable().craftRemainder(Items.BOWL)));
	public static final Item cacophonium = make(prefix(LibItemNames.CACOPHONIUM), new CacophoniumItem(unstackable()));
	public static final Item slimeBottle = make(prefix(LibItemNames.SLIME_BOTTLE), new SlimeInABottleItem(unstackable()));
	public static final Item sextant = make(prefix(LibItemNames.SEXTANT), new WorldshaperssSextantItem(unstackable()));
	public static final Item astrolabe = make(prefix(LibItemNames.ASTROLABE), new AstrolabeItem(unstackable().rarity(Rarity.UNCOMMON)));
	public static final Item baubleBox = make(prefix(LibItemNames.BAUBLE_BOX), new BaubleBoxItem(unstackable()));

	// Baubles / trinkets / curios / etc.
	public static final Item manaRing = make(prefix(LibItemNames.MANA_RING), new BandOfManaItem(unstackable()));
	public static final Item manaRingGreater = make(prefix(LibItemNames.MANA_RING_GREATER), new GreaterBandOfManaItem(unstackable()));
	public static final Item auraRing = make(prefix(LibItemNames.AURA_RING), new BandOfAuraItem(unstackable(), 10));
	public static final Item auraRingGreater = make(prefix(LibItemNames.AURA_RING_GREATER), new BandOfAuraItem(unstackable(), 2));
	public static final Item magnetRing = make(prefix(LibItemNames.MAGNET_RING), new RingOfMagnetizationItem(unstackable()));
	public static final Item magnetRingGreater = make(prefix(LibItemNames.MAGNET_RING_GREATER), new RingOfMagnetizationItem(unstackable(), 16));
	public static final Item waterRing = make(prefix(LibItemNames.WATER_RING), new RingOfChordataItem(unstackable().rarity(Rarity.RARE)));
	public static final Item swapRing = make(prefix(LibItemNames.SWAP_RING), new RingOfCorrectionItem(unstackable()));
	public static final Item dodgeRing = make(prefix(LibItemNames.DODGE_RING), new RingOfDexterousMotionItem(unstackable()));
	public static final Item miningRing = make(prefix(LibItemNames.MINING_RING), new RingOfTheMantleItem(unstackable()));
	public static final Item pixieRing = make(prefix(LibItemNames.PIXIE_RING), new GreatFairyRingItem(unstackable()));
	public static final Item reachRing = make(prefix(LibItemNames.REACH_RING), new RingOfFarReachItem(unstackable()));
	public static final Item travelBelt = make(prefix(LibItemNames.TRAVEL_BELT), new SojournersSashItem(unstackable()));
	public static final Item superTravelBelt = make(prefix(LibItemNames.SUPER_TRAVEL_BELT), new GlobetrottersSashItem(unstackable()));
	public static final Item speedUpBelt = make(prefix(LibItemNames.SPEED_UP_BELT), new PlanestridersSashItem(unstackable()));
	public static final Item knockbackBelt = make(prefix(LibItemNames.KNOCKBACK_BELT), new TectonicGirdleItem(unstackable()));
	public static final Item icePendant = make(prefix(LibItemNames.ICE_PENDANT), new SnowflakePendantItem(unstackable()));
	public static final Item lavaPendant = make(prefix(LibItemNames.LAVA_PENDANT), new PyroclastPendantItem(unstackable()));
	public static final Item superLavaPendant = make(prefix(LibItemNames.SUPER_LAVA_PENDANT), new CrimsonPendantItem(unstackable()));
	public static final Item cloudPendant = make(prefix(LibItemNames.CLOUD_PENDANT), new CirrusAmuletItem(unstackable()));
	public static final Item superCloudPendant = make(prefix(LibItemNames.SUPER_CLOUD_PENDANT), new NimbusAmuletItem(unstackable()));
	public static final Item holyCloak = make(prefix(LibItemNames.HOLY_CLOAK), new CloakOfVirtueItem(unstackable()));
	public static final Item unholyCloak = make(prefix(LibItemNames.UNHOLY_CLOAK), new CloakOfSinItem(unstackable()));
	public static final Item balanceCloak = make(prefix(LibItemNames.BALANCE_CLOAK), new CloakOfBalanceItem(unstackable()));
	public static final Item invisibilityCloak = make(prefix(LibItemNames.INVISIBILITY_CLOAK), new InvisibilityCloakItem(unstackable()));
	public static final Item thirdEye = make(prefix(LibItemNames.THIRD_EYE), new ThirdEyeItem(unstackable()));
	public static final Item monocle = make(prefix(LibItemNames.MONOCLE), new ManaseerMonocleItem(unstackable()));
	public static final Item tinyPlanet = make(prefix(LibItemNames.TINY_PLANET), new TinyPlanetItem(unstackable()));
	public static final Item goddessCharm = make(prefix(LibItemNames.GODDESS_CHARM), new BenevolentGoddessCharmItem(unstackable()));
	public static final Item divaCharm = make(prefix(LibItemNames.DIVA_CHARM), new CharmOfTheDivaItem(unstackable().rarity(Rarity.UNCOMMON)));
	public static final Item itemFinder = make(prefix(LibItemNames.ITEM_FINDER), new SpectatorItem(unstackable()));
	public static final Item flightTiara = make(prefix(LibItemNames.FLIGHT_TIARA), new FlugelTiaraItem(unstackable().rarity(Rarity.UNCOMMON)));

	// Misc
	public static final Item manaCookie = make(prefix(LibItemNames.MANA_COOKIE), new Item(defaultBuilder().food(new FoodProperties.Builder().nutrition(0).saturationMod(0.1F).effect(new MobEffectInstance(MobEffects.SATURATION, 20, 0), 1).build())));
	public static final Item manaBottle = make(prefix(LibItemNames.MANA_BOTTLE), new BottledManaItem(
			// Mark as food just to fool foxes into using it
			unstackable().food(new FoodProperties.Builder().alwaysEat().build())));
	public static final Item laputaShard = make(prefix(LibItemNames.LAPUTA_SHARD), new LaputaShardItem(unstackable().rarity(Rarity.UNCOMMON)));
	public static final Item necroVirus = make(prefix(LibItemNames.NECRO_VIRUS), new EquestrianVirusItem(defaultBuilder()));
	public static final Item nullVirus = make(prefix(LibItemNames.NULL_VIRUS), new EquestrianVirusItem(defaultBuilder()));
	public static final Item spark = make(prefix(LibItemNames.SPARK), new ManaSparkItem(defaultBuilder()));
	public static final Item sparkUpgradeDispersive = make(prefix(LibItemNames.SPARK_UPGRADE + "_" + SparkUpgradeType.DISPERSIVE.name().toLowerCase(Locale.ROOT)), new SparkAugmentItem(defaultBuilder(), SparkUpgradeType.DISPERSIVE));
	public static final Item sparkUpgradeDominant = make(prefix(LibItemNames.SPARK_UPGRADE + "_" + SparkUpgradeType.DOMINANT.name().toLowerCase(Locale.ROOT)), new SparkAugmentItem(defaultBuilder(), SparkUpgradeType.DOMINANT));
	public static final Item sparkUpgradeRecessive = make(prefix(LibItemNames.SPARK_UPGRADE + "_" + SparkUpgradeType.RECESSIVE.name().toLowerCase(Locale.ROOT)), new SparkAugmentItem(defaultBuilder(), SparkUpgradeType.RECESSIVE));
	public static final Item sparkUpgradeIsolated = make(prefix(LibItemNames.SPARK_UPGRADE + "_" + SparkUpgradeType.ISOLATED.name().toLowerCase(Locale.ROOT)), new SparkAugmentItem(defaultBuilder(), SparkUpgradeType.ISOLATED));
	public static final Item corporeaSpark = make(prefix(LibItemNames.CORPOREA_SPARK), new CorporeaSparkItem(defaultBuilder()));
	public static final Item corporeaSparkMaster = make(prefix(LibItemNames.CORPOREA_SPARK_MASTER), new CorporeaSparkItem(defaultBuilder()));
	public static final Item corporeaSparkCreative = make(prefix(LibItemNames.CORPOREA_SPARK_CREATIVE), new CorporeaSparkItem(defaultBuilder().rarity(Rarity.EPIC)));
	public static final Item blackLotus = make(prefix(LibItemNames.BLACK_LOTUS), new BlackLotusItem(defaultBuilder().rarity(Rarity.RARE)));
	public static final Item blackerLotus = make(prefix(LibItemNames.BLACKER_LOTUS), new BlackLotusItem(defaultBuilder().rarity(Rarity.EPIC)));
	public static final Item worldSeed = make(prefix(LibItemNames.WORLD_SEED), new WorldSeedItem(defaultBuilder()));
	public static final Item overgrowthSeed = make(prefix(LibItemNames.OVERGROWTH_SEED), new OvergrowthSeedItem(defaultBuilder().rarity(Rarity.RARE)));
	public static final Item phantomInk = make(prefix(LibItemNames.PHANTOM_INK), new Item(defaultBuilder()));
	public static final Item poolMinecart = make(prefix(LibItemNames.POOL_MINECART), new ManaPoolMinecartItem(unstackable()));
	public static final Item keepIvy = make(prefix(LibItemNames.KEEP_IVY), new ResoluteIvyItem(defaultBuilder()));
	public static final Item placeholder = make(prefix(LibItemNames.PLACEHOLDER), new SelfReturningItem(defaultBuilder()));
	public static final Item craftPattern1_1 = make(prefix(LibItemNames.CRAFT_PATTERN_PREFIX + "1_1"), new CraftingPatternItem(CraftyCratePattern.CRAFTY_1_1, unstackable()));
	public static final Item craftPattern2_2 = make(prefix(LibItemNames.CRAFT_PATTERN_PREFIX + "2_2"), new CraftingPatternItem(CraftyCratePattern.CRAFTY_2_2, unstackable()));
	public static final Item craftPattern1_2 = make(prefix(LibItemNames.CRAFT_PATTERN_PREFIX + "1_2"), new CraftingPatternItem(CraftyCratePattern.CRAFTY_1_2, unstackable()));
	public static final Item craftPattern2_1 = make(prefix(LibItemNames.CRAFT_PATTERN_PREFIX + "2_1"), new CraftingPatternItem(CraftyCratePattern.CRAFTY_2_1, unstackable()));
	public static final Item craftPattern1_3 = make(prefix(LibItemNames.CRAFT_PATTERN_PREFIX + "1_3"), new CraftingPatternItem(CraftyCratePattern.CRAFTY_1_3, unstackable()));
	public static final Item craftPattern3_1 = make(prefix(LibItemNames.CRAFT_PATTERN_PREFIX + "3_1"), new CraftingPatternItem(CraftyCratePattern.CRAFTY_3_1, unstackable()));
	public static final Item craftPattern2_3 = make(prefix(LibItemNames.CRAFT_PATTERN_PREFIX + "2_3"), new CraftingPatternItem(CraftyCratePattern.CRAFTY_2_3, unstackable()));
	public static final Item craftPattern3_2 = make(prefix(LibItemNames.CRAFT_PATTERN_PREFIX + "3_2"), new CraftingPatternItem(CraftyCratePattern.CRAFTY_3_2, unstackable()));
	public static final Item craftPatternDonut = make(prefix(LibItemNames.CRAFT_PATTERN_PREFIX + "donut"), new CraftingPatternItem(CraftyCratePattern.CRAFTY_DONUT, unstackable()));

	// Guardian of Gaia drops
	public static final Item dice = make(prefix(LibItemNames.DICE), new DiceOfFateItem(unstackable().fireResistant().rarity(Rarity.EPIC)));
	public static final Item infiniteFruit = make(prefix(LibItemNames.INFINITE_FRUIT), new FruitOfGrisaiaItem(unstackable().fireResistant().rarity(Rarity.EPIC)));
	public static final Item kingKey = make(prefix(LibItemNames.KING_KEY), new KeyOfTheKingsLawItem(unstackable().fireResistant().rarity(Rarity.EPIC)));
	public static final Item flugelEye = make(prefix(LibItemNames.FLUGEL_EYE), new EyeOfTheFlugelItem(unstackable().fireResistant().rarity(Rarity.EPIC)));
	public static final Item thorRing = make(prefix(LibItemNames.THOR_RING), new RingOfThorItem(unstackable().fireResistant().rarity(Rarity.EPIC)));
	public static final Item odinRing = make(prefix(LibItemNames.ODIN_RING), new RingOfOdinItem(unstackable().fireResistant().rarity(Rarity.EPIC)));
	public static final Item lokiRing = make(prefix(LibItemNames.LOKI_RING), new RingOfLokiItem(unstackable().fireResistant().rarity(Rarity.EPIC)));
	public static final Item recordGaia1 = make(prefix(LibItemNames.RECORD_GAIA1), new BotaniaRecordItem(1, BotaniaSounds.gaiaMusic1, unstackable().rarity(Rarity.RARE), 202));
	public static final Item recordGaia2 = make(prefix(LibItemNames.RECORD_GAIA2), new BotaniaRecordItem(1, BotaniaSounds.gaiaMusic2, unstackable().rarity(Rarity.RARE), 227));
	public static final Item ancientWillAhrim = make(prefix(LibItemNames.ANCIENT_WILL_PREFIX + "ahrim"), new AncientWillItem(AncientWillContainer.AncientWillType.AHRIM, unstackable().rarity(Rarity.UNCOMMON)));
	public static final Item ancientWillDharok = make(prefix(LibItemNames.ANCIENT_WILL_PREFIX + "dharok"), new AncientWillItem(AncientWillContainer.AncientWillType.DHAROK, unstackable().rarity(Rarity.UNCOMMON)));
	public static final Item ancientWillGuthan = make(prefix(LibItemNames.ANCIENT_WILL_PREFIX + "guthan"), new AncientWillItem(AncientWillContainer.AncientWillType.GUTHAN, unstackable().rarity(Rarity.UNCOMMON)));
	public static final Item ancientWillTorag = make(prefix(LibItemNames.ANCIENT_WILL_PREFIX + "torag"), new AncientWillItem(AncientWillContainer.AncientWillType.TORAG, unstackable().rarity(Rarity.UNCOMMON)));
	public static final Item ancientWillVerac = make(prefix(LibItemNames.ANCIENT_WILL_PREFIX + "verac"), new AncientWillItem(AncientWillContainer.AncientWillType.VERAC, unstackable().rarity(Rarity.UNCOMMON)));
	public static final Item ancientWillKaril = make(prefix(LibItemNames.ANCIENT_WILL_PREFIX + "karil"), new AncientWillItem(AncientWillContainer.AncientWillType.KARIL, unstackable().rarity(Rarity.UNCOMMON)));
	public static final Item pinkinator = make(prefix(LibItemNames.PINKINATOR), new PinkinatorItem(unstackable().rarity(Rarity.UNCOMMON)));

	// Brewing
	public static final Item vial = make(prefix(LibItemNames.VIAL), new VialItem(defaultBuilder()));
	public static final Item flask = make(prefix(LibItemNames.FLASK), new VialItem(defaultBuilder()));
	public static final Item brewVial = make(prefix(LibItemNames.BREW_VIAL), new BaseBrewItem(unstackable(), 4, 32, () -> vial));
	public static final Item brewFlask = make(prefix(LibItemNames.BREW_FLASK), new BaseBrewItem(unstackable(), 6, 24, () -> flask));
	public static final Item bloodPendant = make(prefix(LibItemNames.BLOOD_PENDANT), new TaintedBloodPendantItem(unstackable()));
	public static final Item incenseStick = make(prefix(LibItemNames.INCENSE_STICK), new IncenseStickItem(unstackable()));

	// Cosmetics
	public static final Item blackBowtie = make(prefix(LibItemNames.COSMETIC_PREFIX + "black_bowtie"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.BLACK_BOWTIE, unstackable()));
	public static final Item blackTie = make(prefix(LibItemNames.COSMETIC_PREFIX + "black_tie"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.BLACK_TIE, unstackable()));
	public static final Item redGlasses = make(prefix(LibItemNames.COSMETIC_PREFIX + "red_glasses"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.RED_GLASSES, unstackable()));
	public static final Item puffyScarf = make(prefix(LibItemNames.COSMETIC_PREFIX + "puffy_scarf"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.PUFFY_SCARF, unstackable()));
	public static final Item engineerGoggles = make(prefix(LibItemNames.COSMETIC_PREFIX + "engineer_goggles"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.ENGINEER_GOGGLES, unstackable()));
	public static final Item eyepatch = make(prefix(LibItemNames.COSMETIC_PREFIX + "eyepatch"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.EYEPATCH, unstackable()));
	public static final Item wickedEyepatch = make(prefix(LibItemNames.COSMETIC_PREFIX + "wicked_eyepatch"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.WICKED_EYEPATCH, unstackable()));
	public static final Item redRibbons = make(prefix(LibItemNames.COSMETIC_PREFIX + "red_ribbons"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.RED_RIBBONS, unstackable()));
	public static final Item pinkFlowerBud = make(prefix(LibItemNames.COSMETIC_PREFIX + "pink_flower_bud"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.PINK_FLOWER_BUD, unstackable()));
	public static final Item polkaDottedBows = make(prefix(LibItemNames.COSMETIC_PREFIX + "polka_dotted_bows"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.POLKA_DOTTED_BOWS, unstackable()));
	public static final Item blueButterfly = make(prefix(LibItemNames.COSMETIC_PREFIX + "blue_butterfly"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.BLUE_BUTTERFLY, unstackable()));
	public static final Item catEars = make(prefix(LibItemNames.COSMETIC_PREFIX + "cat_ears"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.CAT_EARS, unstackable()));
	public static final Item witchPin = make(prefix(LibItemNames.COSMETIC_PREFIX + "witch_pin"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.WITCH_PIN, unstackable()));
	public static final Item devilTail = make(prefix(LibItemNames.COSMETIC_PREFIX + "devil_tail"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.DEVIL_TAIL, unstackable()));
	public static final Item kamuiEye = make(prefix(LibItemNames.COSMETIC_PREFIX + "kamui_eye"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.KAMUI_EYE, unstackable()));
	public static final Item googlyEyes = make(prefix(LibItemNames.COSMETIC_PREFIX + "googly_eyes"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.GOOGLY_EYES, unstackable()));
	public static final Item fourLeafClover = make(prefix(LibItemNames.COSMETIC_PREFIX + "four_leaf_clover"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.FOUR_LEAF_CLOVER, unstackable()));
	public static final Item clockEye = make(prefix(LibItemNames.COSMETIC_PREFIX + "clock_eye"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.CLOCK_EYE, unstackable()));
	public static final Item unicornHorn = make(prefix(LibItemNames.COSMETIC_PREFIX + "unicorn_horn"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.UNICORN_HORN, unstackable()));
	public static final Item devilHorns = make(prefix(LibItemNames.COSMETIC_PREFIX + "devil_horns"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.DEVIL_HORNS, unstackable()));
	public static final Item hyperPlus = make(prefix(LibItemNames.COSMETIC_PREFIX + "hyper_plus"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.HYPER_PLUS, unstackable()));
	public static final Item botanistEmblem = make(prefix(LibItemNames.COSMETIC_PREFIX + "botanist_emblem"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.BOTANIST_EMBLEM, unstackable()));
	public static final Item ancientMask = make(prefix(LibItemNames.COSMETIC_PREFIX + "ancient_mask"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.ANCIENT_MASK, unstackable()));
	public static final Item eerieMask = make(prefix(LibItemNames.COSMETIC_PREFIX + "eerie_mask"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.EERIE_MASK, unstackable()));
	public static final Item alienAntenna = make(prefix(LibItemNames.COSMETIC_PREFIX + "alien_antenna"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.ALIEN_ANTENNA, unstackable()));
	public static final Item anaglyphGlasses = make(prefix(LibItemNames.COSMETIC_PREFIX + "anaglyph_glasses"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.ANAGLYPH_GLASSES, unstackable()));
	public static final Item orangeShades = make(prefix(LibItemNames.COSMETIC_PREFIX + "orange_shades"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.ORANGE_SHADES, unstackable()));
	public static final Item grouchoGlasses = make(prefix(LibItemNames.COSMETIC_PREFIX + "groucho_glasses"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.GROUCHO_GLASSES, unstackable()));
	public static final Item thickEyebrows = make(prefix(LibItemNames.COSMETIC_PREFIX + "thick_eyebrows"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.THICK_EYEBROWS, unstackable()));
	public static final Item lusitanicShield = make(prefix(LibItemNames.COSMETIC_PREFIX + "lusitanic_shield"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.LUSITANIC_SHIELD, unstackable()));
	public static final Item tinyPotatoMask = make(prefix(LibItemNames.COSMETIC_PREFIX + "tiny_potato_mask"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.TINY_POTATO_MASK, unstackable()));
	public static final Item questgiverMark = make(prefix(LibItemNames.COSMETIC_PREFIX + "questgiver_mark"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.QUESTGIVER_MARK, unstackable()));
	public static final Item thinkingHand = make(prefix(LibItemNames.COSMETIC_PREFIX + "thinking_hand"), new CosmeticBaubleItem(CosmeticBaubleItem.Variant.THINKING_HAND, unstackable()));

	public static final MenuType<BaubleBoxContainer> BAUBLE_BOX_CONTAINER = XplatAbstractions.INSTANCE.createMenuType(BaubleBoxContainer::fromNetwork);
	public static final MenuType<FlowerPouchContainer> FLOWER_BAG_CONTAINER = XplatAbstractions.INSTANCE.createMenuType(FlowerPouchContainer::fromNetwork);

	private static <T extends Item> T make(ResourceLocation id, T item) {
		var old = ALL.put(id, item);
		if (old != null) {
			throw new IllegalArgumentException("Typo? Duplicate id " + id);
		}
		return item;
	}

	@NotNull
	private static MysticalPetalItem makePetal(DyeColor dyeColor) {
		return make(prefix(dyeColor.getName() + LibItemNames.PETAL_SUFFIX),
				new MysticalPetalItem(BotaniaBlocks.getBuriedPetal(dyeColor), dyeColor, defaultBuilder()));
	}

	public static Item.Properties defaultBuilder() {
		return XplatAbstractions.INSTANCE.defaultItemBuilder();
	}

	// Forge does custom damage by just implementing a method on Item,
	// Fabric does it by an extra lambda to the Properties object
	public static Item.Properties defaultBuilderCustomDamage() {
		return XplatAbstractions.INSTANCE.defaultItemBuilderWithCustomDamageOnFabric();
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
		r.accept(ResoluteIvyRecipe.SERIALIZER, prefix("keep_ivy"));
		r.accept(LaputaShardUpgradeRecipe.SERIALIZER, prefix("laputa_shard_upgrade"));
		r.accept(LensDyeingRecipe.SERIALIZER, prefix("lens_dye"));
		r.accept(ManaBlasterClipRecipe.SERIALIZER, prefix("mana_gun_add_clip"));
		r.accept(ManaBlasterLensRecipe.SERIALIZER, prefix("mana_gun_add_lens"));
		r.accept(ManaBlasterRemoveLensRecipe.SERIALIZER, prefix("mana_gun_remove_lens"));
		r.accept(ManaUpgradeRecipe.SERIALIZER, prefix("mana_upgrade"));
		r.accept(ShapelessManaUpgradeRecipe.SERIALIZER, prefix("mana_upgrade_shapeless"));
		r.accept(MergeVialRecipe.SERIALIZER, prefix("merge_vial"));
		r.accept(NbtOutputRecipe.SERIALIZER, prefix("nbt_output_wrapper"));
		r.accept(PhantomInkRecipe.SERIALIZER, prefix("phantom_ink_apply"));
		r.accept(SpellbindingClothRecipe.SERIALIZER, prefix("spell_cloth_apply"));
		r.accept(SplitLensRecipe.SERIALIZER, prefix("split_lens"));
		r.accept(TerraShattererTippingRecipe.SERIALIZER, prefix("terra_pick_tipping"));
		r.accept(WandOfTheForestRecipe.SERIALIZER, prefix("twig_wand"));
		r.accept(WaterBottleMatchingRecipe.SERIALIZER, prefix("water_bottle_matching_shaped"));
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
		return item instanceof ManaTabletItem || item instanceof BandOfManaItem || item instanceof TerraShattererItem
				|| item instanceof RelicItem || item instanceof RelicBaubleItem;
	}

}
