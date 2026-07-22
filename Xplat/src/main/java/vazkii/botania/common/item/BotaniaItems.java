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
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.item.AncientWillContainer;
import vazkii.botania.api.item.PhantomInkable;
import vazkii.botania.api.mana.spark.SparkUpgradeType;
import vazkii.botania.api.state.enums.CraftyCratePattern;
import vazkii.botania.client.gui.bag.ColoredContentsPouchMenu;
import vazkii.botania.client.gui.box.TrinketCaseMenu;
import vazkii.botania.client.gui.enderhand.HandOfEnderMenu;
import vazkii.botania.common.BotaniaStats;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.mana.ManaPoolBlock;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.common.item.brew.BaseBrewItem;
import vazkii.botania.common.item.brew.IncenseStickItem;
import vazkii.botania.common.item.brew.VialItem;
import vazkii.botania.common.item.equipment.armor.elementium.*;
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
import vazkii.botania.common.item.record.BotaniaJukeboxSongs;
import vazkii.botania.common.item.relic.*;
import vazkii.botania.common.item.rod.*;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public final class BotaniaItems {
	private static final Map<String, Item> ALL = new LinkedHashMap<>(); // Preserve insertion order

	public static final LexicaBotaniaItem LEXICA_BOTANIA = make(LibItemNames.LEXICA_BOTANIA,
			new LexicaBotaniaItem(unstackable()));
	public static final Item WAND_OF_THE_FOREST = make(LibItemNames.WAND_OF_THE_FOREST,
			new WandOfTheForestItem(ChatFormatting.DARK_GREEN, unstackable()
					.component(BotaniaDataComponents.WAND_BIND_MODE, Unit.INSTANCE)));
	public static final Item WAND_OF_THE_ELVEN_FOREST = make(LibItemNames.WAND_OF_THE_ELVEN_FOREST,
			new WandOfTheForestItem(ChatFormatting.LIGHT_PURPLE, unstackable()
					.component(BotaniaDataComponents.WAND_BIND_MODE, Unit.INSTANCE)));
	public static final Item FLORAL_OBEDIENCE_STICK = make(LibItemNames.FLORAL_OBEDIENCE_STICK,
			new FloralObedienceStickItem(unstackable()));
	public static final Item FLORAL_FERTILIZER = make(LibItemNames.FLORAL_FERTILIZER,
			new FloralFertilizerItem(defaultBuilder()));

	// mystical petals
	public static final Item WHITE_MYSTICAL_PETAL = makePetal(DyeColor.WHITE);
	public static final Item LIGHT_GRAY_MYSTICAL_PETAL = makePetal(DyeColor.LIGHT_GRAY);
	public static final Item GRAY_MYSTICAL_PETAL = makePetal(DyeColor.GRAY);
	public static final Item BLACK_MYSTICAL_PETAL = makePetal(DyeColor.BLACK);
	public static final Item BROWN_MYSTICAL_PETAL = makePetal(DyeColor.BROWN);
	public static final Item RED_MYSTICAL_PETAL = makePetal(DyeColor.RED);
	public static final Item ORANGE_MYSTICAL_PETAL = makePetal(DyeColor.ORANGE);
	public static final Item YELLOW_MYSTICAL_PETAL = makePetal(DyeColor.YELLOW);
	public static final Item LIME_MYSTICAL_PETAL = makePetal(DyeColor.LIME);
	public static final Item GREEN_MYSTICAL_PETAL = makePetal(DyeColor.GREEN);
	public static final Item CYAN_MYSTICAL_PETAL = makePetal(DyeColor.CYAN);
	public static final Item LIGHT_BLUE_MYSTICAL_PETAL = makePetal(DyeColor.LIGHT_BLUE);
	public static final Item BLUE_MYSTICAL_PETAL = makePetal(DyeColor.BLUE);
	public static final Item PURPLE_MYSTICAL_PETAL = makePetal(DyeColor.PURPLE);
	public static final Item MAGENTA_MYSTICAL_PETAL = makePetal(DyeColor.MAGENTA);
	public static final Item PINK_MYSTICAL_PETAL = makePetal(DyeColor.PINK);

	public static final Item MANASTEEL_INGOT = make(LibItemNames.MANASTEEL_INGOT, new Item(defaultBuilder()));
	public static final Item MANA_PEARL = make(LibItemNames.MANA_PEARL, new Item(defaultBuilder()));
	public static final Item MANA_DIAMOND = make(LibItemNames.MANA_DIAMOND, new Item(defaultBuilder()));
	public static final Item LIVINGWOOD_TWIG = make(LibItemNames.LIVINGWOOD_TWIG, new Item(defaultBuilder()));
	public static final Item TERRASTEEL_INGOT = make(LibItemNames.TERRASTEEL_INGOT,
			new GaiaRitualSacrificeItem(defaultBuilder()
					.rarity(Rarity.UNCOMMON), false));
	public static final Item GAIA_SPIRIT = make(LibItemNames.GAIA_SPIRIT,
			new Item(defaultBuilder()
					.rarity(Rarity.RARE)));
	public static final Item REDSTONE_ROOT = make(LibItemNames.REDSTONE_ROOT, new Item(defaultBuilder()));
	public static final Item ELEMENTIUM_INGOT = make(LibItemNames.ELEMENTIUM_INGOT, new Item(defaultBuilder()));
	public static final Item PIXIE_DUST = make(LibItemNames.PIXIE_DUST, new Item(defaultBuilder()));
	public static final Item DRAGONSTONE = make(LibItemNames.DRAGONSTONE, new Item(defaultBuilder()));
	public static final Item RED_STRING = make(LibItemNames.RED_STRING, new Item(defaultBuilder()));
	public static final Item DREAMWOOD_TWIG = make(LibItemNames.DREAMWOOD_TWIG, new Item(defaultBuilder()));
	public static final Item GAIA_INGOT = make(LibItemNames.GAIA_INGOT,
			new GaiaRitualSacrificeItem(defaultBuilder().rarity(Rarity.RARE), true));
	public static final Item ENDER_AIR_BOTTLE = make(LibItemNames.ENDER_AIR_BOTTLE, new EnderAirItem(defaultBuilder()));
	public static final Item MANA_INFUSED_STRING = make(LibItemNames.MANA_STRING, new Item(defaultBuilder()));
	public static final Item MANASTEEL_NUGGET = make(LibItemNames.MANASTEEL_NUGGET, new Item(defaultBuilder()));
	public static final Item TERRASTEEL_NUGGET = make(LibItemNames.TERRASTEEL_NUGGET,
			new Item(defaultBuilder().rarity(Rarity.UNCOMMON)));
	public static final Item ELEMENTIUM_NUGGET = make(LibItemNames.ELEMENTIUM_NUGGET, new Item(defaultBuilder()));
	public static final Item LIVING_ROOT = make(LibItemNames.LIVING_ROOT, new BoneMealItem(defaultBuilder()));
	public static final Item PEBBLE = make(LibItemNames.PEBBLE, new Item(defaultBuilder()));
	public static final Item MANAWEAVE_CLOTH = make(LibItemNames.MANAWEAVE_CLOTH, new Item(defaultBuilder()));
	public static final Item MANA_POWDER = make(LibItemNames.MANA_POWDER, new Item(defaultBuilder()));

	// quartz variants
	public static final Item DARK_QUARTZ = make(LibItemNames.SMOKEY_QUARTZ, new Item(defaultBuilder()));
	public static final Item MANA_QUARTZ = make(LibItemNames.MANA_QUARTZ, new Item(defaultBuilder()));
	public static final Item BLAZE_QUARTZ = make(LibItemNames.BLAZE_QUARTZ, new Item(defaultBuilder()));
	public static final Item LAVENDER_QUARTZ = make(LibItemNames.LAVENDER_QUARTZ, new Item(defaultBuilder()));
	public static final Item RED_QUARTZ = make(LibItemNames.RED_QUARTZ, new Item(defaultBuilder()));
	public static final Item ELVEN_QUARTZ = make(LibItemNames.ELVEN_QUARTZ, new Item(defaultBuilder()));
	public static final Item SUNNY_QUARTZ = make(LibItemNames.SUNNY_QUARTZ, new Item(defaultBuilder()));

	// lenses
	public static final Item MANA_LENS = make(LibItemNames.MANA_LENS,
			new LensItem(stackTo16(), new Lens(), LensItem.PROP_NONE));
	public static final Item VELOCITY_LENS = make(LibItemNames.VELOCITY_LENS,
			new LensItem(stackTo16(), new VelocityLens(), LensItem.PROP_NONE));
	public static final Item POTENCY_LENS = make(LibItemNames.POTENCY_LENS,
			new LensItem(stackTo16(), new PotencyLens(), LensItem.PROP_POWER));
	public static final Item RESISTANCE_LENS = make(LibItemNames.RESISTANCE_LENS,
			new LensItem(stackTo16(), new ResistanceLens(), LensItem.PROP_NONE));
	public static final Item EFFICIENCY_LENS = make(LibItemNames.EFFICIENCY_LENS,
			new LensItem(stackTo16(), new EfficiencyLens(), LensItem.PROP_NONE));
	public static final Item BOUNCE_LENS = make(LibItemNames.BOUNCE_LENS,
			new LensItem(stackTo16(), new BounceLens(), LensItem.PROP_TOUCH));
	public static final Item GRAVITY_LENS = make(LibItemNames.GRAVITY_LENS,
			new LensItem(stackTo16(), new GravityLens(), LensItem.PROP_ORIENTATION));
	public static final Item BORE_LENS = make(LibItemNames.BORE_LENS,
			new LensItem(stackTo16(), new BoreLens(), LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));
	public static final Item DAMAGING_LENS = make(LibItemNames.DAMAGING_LENS,
			new LensItem(stackTo16(), new DamagingLens(), LensItem.PROP_DAMAGE));
	public static final Item PHANTOM_LENS = make(LibItemNames.PHANTOM_LENS,
			new LensItem(stackTo16(), new PhantomLens(), LensItem.PROP_TOUCH));
	public static final Item MAGNETIZING_LENS = make(LibItemNames.MAGNETIZING_LENS,
			new LensItem(stackTo16(), new MagnetizingLens(), LensItem.PROP_ORIENTATION));
	public static final Item ENTROPIC_LENS = make(LibItemNames.ENTROPIC_LENS,
			new LensItem(stackTo16(), new EntropicLens(),
					LensItem.PROP_DAMAGE | LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));
	public static final Item INFLUENCE_LENS = make(LibItemNames.INFLUENCE_LENS,
			new LensItem(stackTo16(), new InfluenceLens(), LensItem.PROP_NONE));
	public static final Item WEIGHT_LENS = make(LibItemNames.WEIGHT_LENS,
			new LensItem(stackTo16(), new WeightLens(), LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));
	public static final Item PAINTSLINGER_LENS = make(LibItemNames.PAINTSLINGER_LENS,
			new LensItem(stackTo16(), new PaintslingerLens(), LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));
	public static final Item KINDLE_LENS = make(LibItemNames.KINDLE_LENS,
			new LensItem(stackTo16(), new KindleLens(),
					LensItem.PROP_DAMAGE | LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));
	public static final Item FORCE_LENS = make(LibItemNames.FORCE_LENS,
			new LensItem(stackTo16(), new ForceLens(), LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));
	public static final Item FLASH_LENS = make(LibItemNames.FLASH_LENS,
			new LensItem(stackTo16(), new FlashLens(), LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));
	public static final Item WARP_LENS = make(LibItemNames.WARP_LENS,
			new LensItem(stackTo16(), new WarpLens(), LensItem.PROP_NONE));
	public static final Item REDIRECTIVE_LENS = make(LibItemNames.REDIRECTIVE_LENS,
			new LensItem(stackTo16(), new RedirectiveLens(), LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));
	public static final Item CELEBRATORY_LENS = make(LibItemNames.CELEBRATORY_LENS,
			new LensItem(stackTo16(), new CelebratoryLens(), LensItem.PROP_TOUCH));
	public static final Item FLARE_LENS = make(LibItemNames.FLARE_LENS,
			new LensItem(stackTo16(), new FlareLens(), LensItem.PROP_CONTROL));
	public static final Item MESSENGER_LENS = make(LibItemNames.MESSENGER_LENS,
			new LensItem(stackTo16(), new MessengerLens(), LensItem.PROP_POWER));
	public static final Item TRIPWIRE_LENS = make(LibItemNames.TRIPWIRE_LENS,
			new LensItem(stackTo16(), new TripwireLens(), LensItem.PROP_CONTROL));
	public static final Item STORM_LENS = make(LibItemNames.LENS_STORM,
			new LensItem(stackTo16().rarity(Rarity.EPIC), new StormLens(), LensItem.PROP_NONE));

	// runes
	public static final Item RUNE_OF_WATER = make(LibItemNames.RUNE_OF_WATER, new RuneItem(defaultBuilder()));
	public static final Item RUNE_OF_FIRE = make(LibItemNames.RUNE_OF_FIRE, new RuneItem(defaultBuilder()));
	public static final Item RUNE_OF_EARTH = make(LibItemNames.RUNE_OF_EARTH, new RuneItem(defaultBuilder()));
	public static final Item RUNE_OF_AIR = make(LibItemNames.RUNE_OF_AIR, new RuneItem(defaultBuilder()));
	public static final Item RUNE_OF_SPRING = make(LibItemNames.RUNE_OF_SPRING, new RuneItem(defaultBuilder()));
	public static final Item RUNE_OF_SUMMER = make(LibItemNames.RUNE_OF_SUMMER, new RuneItem(defaultBuilder()));
	public static final Item RUNE_OF_AUTUMN = make(LibItemNames.RUNE_OF_AUTUMN, new RuneItem(defaultBuilder()));
	public static final Item RUNE_OF_WINTER = make(LibItemNames.RUNE_OF_WINTER, new RuneItem(defaultBuilder()));
	public static final Item RUNE_OF_MANA = make(LibItemNames.RUNE_OF_MANA, new RuneItem(defaultBuilder()));
	public static final Item RUNE_OF_LUST = make(LibItemNames.RUNE_OF_LUST, new RuneItem(defaultBuilder()));
	public static final Item RUNE_OF_GLUTTONY = make(LibItemNames.RUNE_OF_GLUTTONY, new RuneItem(defaultBuilder()));
	public static final Item RUNE_OF_GREED = make(LibItemNames.RUNE_OF_GREED, new RuneItem(defaultBuilder()));
	public static final Item RUNE_OF_SLOTH = make(LibItemNames.RUNE_OF_SLOTH, new RuneItem(defaultBuilder()));
	public static final Item RUNE_OF_WRATH = make(LibItemNames.RUNE_OF_WRATH, new RuneItem(defaultBuilder()));
	public static final Item RUNE_OF_ENVY = make(LibItemNames.RUNE_OF_ENVY, new RuneItem(defaultBuilder()));
	public static final Item RUNE_OF_PRIDE = make(LibItemNames.RUNE_OF_PRIDE, new RuneItem(defaultBuilder()));

	// seeds
	public static final Item PASTURE_SEEDS = make(LibItemNames.PASTURE_SEEDS,
			new GrassSeedsItem(Blocks.GRASS_BLOCK, 0x006600, defaultBuilder()));
	public static final Item BOREAL_SEEDS = make(LibItemNames.BOREAL_SEEDS,
			new GrassSeedsItem(Blocks.PODZOL, 0x805E00, defaultBuilder()));
	public static final Item INFESTATION_SPORES = make(LibItemNames.INFESTATION_SPORES,
			new GrassSeedsItem(Blocks.MYCELIUM, 0x5E0054, defaultBuilder()));
	public static final Item DRY_SEEDS = make(LibItemNames.DRY_PASTURE_SEEDS,
			new GrassSeedsItem(BotaniaBlocks.DRY_GRASS_BLOCK, 0x66800D, defaultBuilder()));
	public static final Item GOLDEN_SEEDS = make(LibItemNames.GOLDEN_PASTURE_SEEDS,
			new GrassSeedsItem(BotaniaBlocks.GOLDEN_GRASS_BLOCK, 0xBFB300, defaultBuilder()));
	public static final Item VIVID_SEEDS = make(LibItemNames.VIVID_PASTURE_SEEDS,
			new GrassSeedsItem(BotaniaBlocks.VIVID_GRASS_BLOCK, 0x00801A, defaultBuilder()));
	public static final Item SCORCHED_SEEDS = make(LibItemNames.SCORCHED_PASTURE_SEEDS,
			new GrassSeedsItem(BotaniaBlocks.SCORCHED_GRASS_BLOCK, 0xBF0000, defaultBuilder()));
	public static final Item INFUSED_SEEDS = make(LibItemNames.INFUSED_PASTURE_SEEDS,
			new GrassSeedsItem(BotaniaBlocks.INFUSED_GRASS_BLOCK, 0x008C8C, defaultBuilder()));
	public static final Item MUTATED_SEEDS = make(LibItemNames.MUTATED_PASTURE_SEEDS,
			new GrassSeedsItem(BotaniaBlocks.MUTATED_GRASS_BLOCK, 0x661A66, defaultBuilder()));

	// Rods
	public static final Item ROD_OF_THE_LANDS = make(LibItemNames.ROD_OF_THE_LANDS,
			new LandsRodItem(unstackable()));
	public static final Item ROD_OF_THE_HIGHLANDS = make(LibItemNames.ROD_OF_THE_HIGHLANDS,
			new HighlandsRodItem(unstackable()));
	public static final Item ROD_OF_THE_TERRA_FIRMA = make(LibItemNames.ROD_OF_THE_TERRA_FIRMA,
			new TerraFirmaRodItem(unstackable()
					.rarity(Rarity.UNCOMMON)));
	public static final Item ROD_OF_THE_DEPTHS = make(LibItemNames.ROD_OF_THE_DEPTHS,
			new DepthsRodItem(unstackable()));
	public static final Item ROD_OF_THE_SEAS = make(LibItemNames.ROD_OF_THE_SEAS,
			new SeasRodItem(unstackable()));
	public static final Item ROD_OF_THE_SKIES = make(LibItemNames.ROD_OF_THE_SKIES,
			new SkiesRodItem(unstackable()));
	public static final Item ROD_OF_THE_HELLS = make(LibItemNames.ROD_OF_THE_HELLS,
			new HellsRodItem(unstackable()));
	public static final Item ROD_OF_THE_PLENTIFUL_MANTLE = make(LibItemNames.ROD_OF_THE_PLENTIFUL_MANTLE,
			new PlentifulMantleRodItem(unstackable()));
	public static final Item ROD_OF_THE_MOLTEN_CORE = make(LibItemNames.ROD_OF_THE_MOLTEN_CORE,
			new MoltenCoreRodItem(unstackable()));
	public static final Item ROD_OF_THE_SHIFTING_CRUST = make(LibItemNames.ROD_OF_THE_SHIFTING_CRUST,
			new ShiftingCrustRodItem(unstackable()));
	public static final Item ROD_OF_THE_BIFROST = make(LibItemNames.ROD_OF_THE_BIFROST,
			new BifrostRodItem(unstackable()));
	public static final Item ROD_OF_THE_SHADED_MESA = make(LibItemNames.ROD_OF_THE_SHADED_MESA,
			new ShadedMesaRodItem(unstackable()));
	public static final Item ROD_OF_THE_UNSTABLE_RESERVOIR = make(LibItemNames.ROD_OF_THE_UNSTABLE_RESERVOIR,
			new UnstableReservoirRodItem(unstackable()
					.rarity(Rarity.RARE)));

	// Equipment
	public static final Item MANASTEEL_HELMET = make(LibItemNames.MANASTEEL_HELMET,
			new ManasteelHelmItem(unstackableCustomDamage()
					.durability(ArmorItem.Type.HELMET.getDurability(16))));
	public static final Item MANASTEEL_CHESTPLATE = make(LibItemNames.MANASTEEL_CHESTPLATE,
			new ManasteelArmorItem(ArmorItem.Type.CHESTPLATE, unstackableCustomDamage()
					.durability(ArmorItem.Type.CHESTPLATE.getDurability(16))));
	public static final Item MANASTEEL_LEGGINGS = make(LibItemNames.MANASTEEL_LEGGINGS,
			new ManasteelArmorItem(ArmorItem.Type.LEGGINGS, unstackableCustomDamage()
					.durability(ArmorItem.Type.LEGGINGS.getDurability(16))));
	public static final Item MANASTEEL_BOOTS = make(LibItemNames.MANASTEEL_BOOTS,
			new ManasteelArmorItem(ArmorItem.Type.BOOTS, unstackableCustomDamage()
					.durability(ArmorItem.Type.BOOTS.getDurability(16))));
	public static final Item MANASTEEL_PICKAXE = make(LibItemNames.MANASTEEL_PICKAXE,
			new ManasteelPickaxeItem(unstackableCustomDamage()));
	public static final Item MANASTEEL_SHOVEL = make(LibItemNames.MANASTEEL_SHOVEL,
			new ManasteelShovelItem(unstackableCustomDamage()));
	public static final Item MANASTEEL_AXE = make(LibItemNames.MANASTEEL_AXE,
			new ManasteelAxeItem(unstackableCustomDamage()));
	public static final Item MANASTEEL_HOE = make(LibItemNames.MANASTEEL_HOE,
			new ManasteelHoeItem(unstackableCustomDamage()));
	public static final Item MANASTEEL_SWORD = make(LibItemNames.MANASTEEL_SWORD,
			new ManasteelSwordItem(unstackableCustomDamage()));
	public static final Item MANASTEEL_SHEARS = make(LibItemNames.MANASTEEL_SHEARS,
			new ManasteelShearsItem(unstackableCustomDamage()
					.durability(238)));
	public static final Item ELEMENTIUM_HELMET = make(LibItemNames.ELEMENTIUM_HELMET,
			new ElementiumHelmItem(0.11, unstackableCustomDamage()
					.durability(ArmorItem.Type.HELMET.getDurability(18))));
	public static final Item ELEMENTIUM_CHESTPLATE = make(LibItemNames.ELEMENTIUM_CHESTPLATE,
			new ElementiumArmorItem(ArmorItem.Type.CHESTPLATE, 0.17, unstackableCustomDamage()
					.durability(ArmorItem.Type.CHESTPLATE.getDurability(18))));
	public static final Item ELEMENTIUM_LEGGINGS = make(LibItemNames.ELEMENTIUM_LEGGINGS,
			new ElementiumArmorItem(ArmorItem.Type.LEGGINGS, 0.15, unstackableCustomDamage()
					.durability(ArmorItem.Type.LEGGINGS.getDurability(18))));
	public static final Item ELEMENTIUM_BOOTS = make(LibItemNames.ELEMENTIUM_BOOTS,
			new ElementiumArmorItem(ArmorItem.Type.BOOTS, 0.09, unstackableCustomDamage()
					.durability(ArmorItem.Type.BOOTS.getDurability(18))));
	public static final Item ELEMENTIUM_PICKAXE = make(LibItemNames.ELEMENTIUM_PICKAXE,
			new ElementiumPickaxeItem(unstackableCustomDamage()));
	public static final Item ELEMENTIUM_SHOVEL = make(LibItemNames.ELEMENTIUM_SHOVEL,
			new ElementiumShovelItem(unstackableCustomDamage()));
	public static final Item ELEMENTIUM_AXE = make(LibItemNames.ELEMENTIUM_AXE,
			new ElementiumAxeItem(unstackableCustomDamage()));
	public static final Item ELEMENTIUM_HOE = make(LibItemNames.ELEMENTIUM_HOE,
			new ElementiumHoeItem(unstackableCustomDamage()));
	public static final Item ELEMENTIUM_SWORD = make(LibItemNames.ELEMENTIUM_SWORD,
			new ElementiumSwordItem(unstackableCustomDamage()));
	public static final Item ELEMENTIUM_SHEARS = make(LibItemNames.ELEMENTIUM_SHEARS,
			new ElementiumShearsItem(unstackableCustomDamage()
					.durability(238)));
	public static final Item TERRASTEEL_HELMET = make(LibItemNames.TERRASTEEL_HELMET,
			new TerrasteelHelmItem(unstackableCustomDamage()
					.durability(ArmorItem.Type.HELMET.getDurability(34))
					.fireResistant()
					.rarity(Rarity.UNCOMMON)));
	public static final Item TERRASTEEL_CHESTPLATE = make(LibItemNames.TERRASTEEL_CHESTPLATE,
			new TerrasteelArmorItem(ArmorItem.Type.CHESTPLATE, unstackableCustomDamage()
					.durability(ArmorItem.Type.CHESTPLATE.getDurability(34))
					.fireResistant()
					.rarity(Rarity.UNCOMMON)));
	public static final Item TERRASTEEL_LEGGINGS = make(LibItemNames.TERRASTEEL_LEGGINGS,
			new TerrasteelArmorItem(ArmorItem.Type.LEGGINGS, unstackableCustomDamage()
					.durability(ArmorItem.Type.LEGGINGS.getDurability(34))
					.fireResistant()
					.rarity(Rarity.UNCOMMON)));
	public static final Item TERRASTEEL_BOOTS = make(LibItemNames.TERRASTEEL_BOOTS,
			new TerrasteelArmorItem(ArmorItem.Type.BOOTS, unstackableCustomDamage()
					.durability(ArmorItem.Type.BOOTS.getDurability(34))
					.fireResistant()
					.rarity(Rarity.UNCOMMON)));
	public static final Item TERRA_SHATTERER = make(LibItemNames.TERRA_SHATTERER,
			new TerraShattererItem(unstackableCustomDamage()
					.fireResistant()
					.rarity(Rarity.UNCOMMON)
					.component(BotaniaDataComponents.MAX_MANA, TerraShattererItem.MAX_MANA)
					.component(BotaniaDataComponents.CAN_RECEIVE_MANA_FROM_POOL, Unit.INSTANCE)));
	public static final Item TERRA_TRUNCATOR = make(LibItemNames.TERRA_TRUNCATOR,
			new TerraTruncatorItem(unstackableCustomDamage()
					.fireResistant()
					.rarity(Rarity.UNCOMMON)));
	public static final Item TERRA_BLADE = make(LibItemNames.TERRA_BLADE,
			new TerraBladeItem(unstackableCustomDamage()
					.fireResistant()
					.rarity(Rarity.UNCOMMON)));
	public static final Item STARCALLER = make(LibItemNames.STARCALLER,
			new StarcallerItem(unstackableCustomDamage().rarity(Rarity.UNCOMMON)));
	public static final Item THUNDERCALLER = make(LibItemNames.THUNDERCALLER,
			new ThundercallerItem(unstackableCustomDamage().rarity(Rarity.UNCOMMON)));
	public static final Item MANAWEAVE_HELMET = make(LibItemNames.MANAWEAVE_HELMET,
			new ManaweaveHelmItem(unstackableCustomDamage()
					.durability(ArmorItem.Type.HELMET.getDurability(5))));
	public static final Item MANAWEAVE_CHESTPLATE = make(LibItemNames.MANAWEAVE_CHESTPLATE,
			new ManaweaveArmorItem(ArmorItem.Type.CHESTPLATE, unstackableCustomDamage()
					.durability(ArmorItem.Type.CHESTPLATE.getDurability(5))));
	public static final Item MANAWEAVE_LEGGINGS = make(LibItemNames.MANAWEAVE_LEGGINGS,
			new ManaweaveArmorItem(ArmorItem.Type.LEGGINGS, unstackableCustomDamage()
					.durability(ArmorItem.Type.LEGGINGS.getDurability(5))));
	public static final Item MANAWEAVE_BOOTS = make(LibItemNames.MANAWEAVE_BOOTS,
			new ManaweaveArmorItem(ArmorItem.Type.BOOTS, unstackableCustomDamage()
					.durability(ArmorItem.Type.BOOTS.getDurability(5))));
	public static final Item SOULSCRIBE = make(LibItemNames.SOULSCRIBE,
			new SoulscribeItem(unstackable()
					.durability(69))); // What you looking at?
	public static final Item VITREOUS_PICKAXE = make(LibItemNames.VITREOUS_PICKAXE,
			new VitreousPickaxeItem(unstackableCustomDamage()));
	public static final Item LIVINGWOOD_BOW = make(LibItemNames.LIVINGWOOD_BOW,
			new LivingwoodBowItem(defaultBuilderCustomDamage()
					.durability(500)));
	public static final Item CRYSTAL_BOW = make(LibItemNames.CRYSTAL_BOW,
			new CrystalBowItem(defaultBuilderCustomDamage()
					.durability(500)));
	public static final Item THORN_CHAKRAM = make(LibItemNames.THORN_CHAKRAM,
			new ThornChakramItem(defaultBuilder()
					.stacksTo(6)
					.rarity(Rarity.UNCOMMON)));
	public static final Item FLARE_CHAKRAM = make(LibItemNames.FLARE_CHAKRAM,
			new ThornChakramItem(defaultBuilder()
					.stacksTo(6)
					.rarity(Rarity.UNCOMMON)));

	// Misc tools
	public static final Item MANA_TABLET = make(LibItemNames.MANA_TABLET,
			new ManaTabletItem(unstackable()
					.component(BotaniaDataComponents.MAX_MANA, ManaTabletItem.DEFAULT_MAX_MANA)
					.component(BotaniaDataComponents.CAN_PROVIDE_MANA_TO_ITEMS, Unit.INSTANCE)
					.component(BotaniaDataComponents.CAN_DRAIN_MANA_TO_POOL, Unit.INSTANCE)
					.component(BotaniaDataComponents.CAN_ACCEPT_MANA_FROM_ITEMS, Unit.INSTANCE)
					.component(BotaniaDataComponents.CAN_RECEIVE_MANA_FROM_POOL, Unit.INSTANCE)));
	public static final Item MANA_MIRROR = make(LibItemNames.MANA_MIRROR,
			new ManaMirrorItem(unstackable()
					.rarity(Rarity.UNCOMMON)
					.component(BotaniaDataComponents.MANA_BACKLOG, 0)
					.component(BotaniaDataComponents.CAN_PROVIDE_MANA_TO_ITEMS, Unit.INSTANCE)));
	public static final Item MANA_BLASTER = make(LibItemNames.MANA_BLASTER,
			new ManaBlasterItem(unstackable()));
	public static final Item LENS_CLIP = make(LibItemNames.LENS_CLIP,
			new Item(unstackable()));
	public static final HornItem HORN_OF_THE_WILD = make(LibItemNames.HORN_OF_THE_WILD,
			new HornOfTheWildItem(unstackable()));
	public static final HornItem HORN_OF_THE_CANOPY = make(LibItemNames.HORN_OF_THE_CANOPY,
			new HornOfTheCanopyItem(unstackable()));
	public static final HornItem HORN_OF_THE_COVERING = make(LibItemNames.HORN_OF_THE_COVERING,
			new HornOfTheCoveringItem(unstackable()));
	public static final Item VINE_BALL = make(LibItemNames.VINE_BALL,
			new VineBallItem(defaultBuilder()));
	public static final Item LIVINGWOOD_SLINGSHOT = make(LibItemNames.LIVINGWOOD_SLINGSHOT,
			new LivingwoodSlingshotItem(unstackable()));
	public static final Item EXTRAPOLATED_BUCKET = make(LibItemNames.EXTRAPOLATED_BUCKET,
			new ExtrapolatedBucketItem(unstackable()));
	public static final Item LIFE_AGGREGATOR = make(LibItemNames.LIFE_AGGREGATOR,
			new LifeAggregatorItem(unstackable().rarity(Rarity.RARE)));
	public static final Item HAND_OF_ENDER = make(LibItemNames.HAND_OF_ENDER,
			new EnderHandItem(unstackable()));
	public static final Item ASSEMBLY_HALO = make(LibItemNames.ASSEMBLY_HALO,
			new AssemblyHaloItem(unstackable()));
	public static final Item MANUFACTORY_HALO = make(LibItemNames.MANUFACTORY_HALO,
			new ManufactoryHaloItem(unstackable()
					.component(BotaniaDataComponents.ACTIVE, Unit.INSTANCE)));
	public static final Item SPELLBINDING_CLOTH = make(LibItemNames.SPELLBINDING_CLOTH,
			new SpellbindingClothItem(XplatAbstractions.INSTANCE.noRepairOnNeoForge(unstackable()
					.durability(35))));
	public static final FlowerPouchItem FLOWER_POUCH = make(LibItemNames.FLOWER_POUCH,
			new FlowerPouchItem(unstackable()
					.component(BotaniaDataComponents.ITEM_TAGS,
							List.of(BotaniaTags.Items.SMALL_MYSTICAL_FLOWERS, BotaniaTags.Items.TALL_MYSTICAL_FLOWERS))));
	public static final PetalPouchItem PETAL_POUCH = make(LibItemNames.PETAL_POUCH,
			new PetalPouchItem(unstackable()
					.component(BotaniaDataComponents.ACTIVE, Unit.INSTANCE)
					.component(BotaniaDataComponents.ITEM_TAGS,
							List.of(BotaniaTags.Items.PETALS, BotaniaTags.Items.SHIMMERING_MUSHROOMS))
					.component(BotaniaDataComponents.CRAFTABLE_ITEM_TAGS,
							List.of(BotaniaTags.Items.SMALL_MYSTICAL_FLOWERS, BotaniaTags.Items.TALL_MYSTICAL_FLOWERS))));
	public static final Item BLACK_HOLE_TALISMAN = make(LibItemNames.BLACK_HOLE_TALISMAN,
			new BlackHoleTalismanItem(unstackable()
					.rarity(Rarity.RARE)));
	public static final Item STONE_OF_TEMPERANCE = make(LibItemNames.STONE_OF_TEMPERANCE,
			new StoneOfTemperanceItem(unstackable()));
	public static final Item WATER_BOWL = make(LibItemNames.WATER_BOWL,
			new WaterBowlItem(unstackable().craftRemainder(Items.BOWL)));
	public static final Item CACOPHONIUM = make(LibItemNames.CACOPHONIUM,
			new CacophoniumItem(unstackable()));
	public static final Item SLIME_IN_A_BOTTLE = make(LibItemNames.SLIME_IN_A_BOTTLE,
			new SlimeInABottleItem(unstackable()));
	public static final Item WORLDSHAPERS_SEXTANT = make(LibItemNames.WORLDSHAPERS_SEXTANT,
			new SextantItem(unstackable()));
	public static final Item WORLDSHAPERS_ASTROLABE = make(LibItemNames.WORLDSHAPERS_ASTROLABE,
			new AstrolabeItem(unstackable().rarity(Rarity.RARE)));
	public static final Item TRINKET_CASE = make(LibItemNames.TRINKET_CASE,
			new BaubleBoxItem(unstackable()));

	// Baubles / trinkets / curios / etc.
	public static final Item BAND_OF_MANA = make(LibItemNames.BAND_OF_MANA,
			new BandOfManaItem(unstackable()
					.component(BotaniaDataComponents.MAX_MANA, BandOfManaItem.DEFAULT_MAX_MANA)
					.component(BotaniaDataComponents.CAN_PROVIDE_MANA_TO_ITEMS, Unit.INSTANCE)
					.component(BotaniaDataComponents.CAN_DRAIN_MANA_TO_POOL, Unit.INSTANCE)
					.component(BotaniaDataComponents.CAN_ACCEPT_MANA_FROM_ITEMS, Unit.INSTANCE)
					.component(BotaniaDataComponents.CAN_RECEIVE_MANA_FROM_POOL, Unit.INSTANCE)));
	public static final Item GREATER_BAND_OF_MANA = make(LibItemNames.GREATER_BAND_OF_MANA,
			new BandOfManaItem(unstackable()
					.rarity(Rarity.UNCOMMON)
					.fireResistant()
					.component(BotaniaDataComponents.MAX_MANA, BandOfManaItem.DEFAULT_GREATER_MAX_MANA)
					.component(BotaniaDataComponents.CAN_PROVIDE_MANA_TO_ITEMS, Unit.INSTANCE)
					.component(BotaniaDataComponents.CAN_DRAIN_MANA_TO_POOL, Unit.INSTANCE)
					.component(BotaniaDataComponents.CAN_ACCEPT_MANA_FROM_ITEMS, Unit.INSTANCE)
					.component(BotaniaDataComponents.CAN_RECEIVE_MANA_FROM_POOL, Unit.INSTANCE)));
	public static final Item BAND_OF_AURA = make(LibItemNames.BAND_OF_AURA,
			new BandOfAuraItem(10, unstackable()));
	public static final Item GREATER_BAND_OF_AURA = make(LibItemNames.GREATER_BAND_OF_AURA,
			new BandOfAuraItem(2, unstackable()
					.rarity(Rarity.UNCOMMON)
					.fireResistant()));
	public static final Item RING_OF_MAGNETIZATION = make(LibItemNames.RING_OF_MAGNETIZATION,
			new RingOfMagnetizationItem(unstackable()
					.component(BotaniaDataComponents.RANGE, RingOfMagnetizationItem.DEFAULT_RANGE)));
	public static final Item GREATER_RING_OF_MAGNETIZATION = make(LibItemNames.GREATER_RING_OF_MAGNETIZATION,
			new RingOfMagnetizationItem(unstackable()
					.rarity(Rarity.UNCOMMON)
					.fireResistant()
					.component(BotaniaDataComponents.RANGE, RingOfMagnetizationItem.DEFAULT_GREATER_RANGE)));
	public static final Item RING_OF_CHORDATA = make(LibItemNames.RING_OF_CHORDATA,
			new RingOfChordataItem(unstackable()
					.rarity(Rarity.UNCOMMON)));
	public static final Item RING_OF_CORRECTION = make(LibItemNames.RING_OF_CORRECTION,
			new RingOfCorrectionItem(unstackable()));
	public static final Item RING_OF_DEXTEROUS_MOTION = make(LibItemNames.RING_OF_DEXTEROUS_MOTION,
			new RingOfDexterousMotionItem(unstackable()));
	public static final Item RING_OF_THE_MANTLE = make(LibItemNames.RING_OF_THE_MANTLE,
			new RingOfTheMantleItem(unstackable()));
	public static final Item GREAT_FAIRY_RING = make(LibItemNames.GREAT_FAIRY_RING,
			new GreatFairyRingItem(unstackable()));
	public static final Item RING_OF_FAR_REACH = make(LibItemNames.RING_OF_FAR_REACH,
			new RingOfFarReachItem(unstackable()));
	public static final Item SOJOURNERS_SASH = make(LibItemNames.SOJOURNORS_SASH,
			new SojournersSashItem(unstackable()));
	public static final Item GLOBETROTTERS_SASH = make(LibItemNames.GLOBETROTTERS_SASH,
			new GlobetrottersSashItem(unstackable()
					.rarity(Rarity.RARE)));
	public static final Item PLANESTRIDERS_SASH = make(LibItemNames.PLANESTRIDERS_SASH,
			new PlanestridersSashItem(unstackable()));
	public static final Item TECTONIC_GIRDLE = make(LibItemNames.TECTONIC_GIRDLE,
			new TectonicGirdleItem(unstackable()));
	public static final Item SNOWFLAKE_PENDANT = make(LibItemNames.SNOWFLAKE_PENDANT,
			new SnowflakePendantItem(unstackable()));
	public static final Item PYROCLAST_PENDANT = make(LibItemNames.PYROCLAST_PENDANT,
			new PyroclastPendantItem(unstackable()));
	public static final Item CRIMSON_PENDANT = make(LibItemNames.CRIMSON_PENDANT,
			new CrimsonPendantItem(unstackable()
					.rarity(Rarity.RARE)));
	public static final Item CIRRUS_AMULET = make(LibItemNames.CIRRUS_AMULET,
			new CirrusAmuletItem(unstackable()));
	public static final Item NIMBUS_AMULET = make(LibItemNames.NIMBUS_AMULET,
			new NimbusAmuletItem(unstackable()
					.rarity(Rarity.RARE)));
	public static final Item CLOAK_OF_VIRTUE = make(LibItemNames.CLOAK_OF_VIRTUE,
			new CloakOfVirtueItem(unstackable()
					.rarity(Rarity.RARE)));
	public static final Item CLOAK_OF_SIN = make(LibItemNames.CLOAK_OF_SIN,
			new CloakOfSinItem(unstackable()
					.rarity(Rarity.RARE)));
	public static final Item CLOAK_OF_BALANCE = make(LibItemNames.CLOAK_OF_BALANCE,
			new CloakOfBalanceItem(unstackable()
					.rarity(Rarity.RARE)));
	public static final Item INVISIBILITY_CLOAK = make(LibItemNames.INVISIBILITY_CLOAK,
			new InvisibilityCloakItem(unstackable()));
	public static final Item THIRD_EYE = make(LibItemNames.THIRD_EYE,
			new ThirdEyeItem(unstackable()));
	public static final Item MANASEER_MONOCLE = make(LibItemNames.MANASEER_MONOCLE,
			new ManaseerMonocleItem(unstackable()));
	public static final Item TINY_PLANET = make(LibItemNames.TINY_PLANET,
			new TinyPlanetItem(unstackable()));
	public static final Item BENEVOLENT_GODDESS_CHARM = make(LibItemNames.BENEVOLENT_GODDESS_CHARM,
			new BenevolentGoddessCharmItem(unstackable()));
	public static final Item CHARM_OF_THE_DIVA = make(LibItemNames.CHARM_OF_THE_DIVA,
			new CharmOfTheDivaItem(unstackable()
					.rarity(Rarity.RARE)));
	public static final Item THE_SPECTATOR = make(LibItemNames.THE_SPECTATOR,
			new SpectatorItem(unstackable()));
	public static final Item FLUGEL_TIARA = make(LibItemNames.FLUGEL_TIARA,
			new FlugelTiaraItem(unstackable()
					.rarity(Rarity.RARE)));

	// Misc
	public static final Item BISCUIT_OF_TOTALITY = make(LibItemNames.BISCUIT_OF_TOTALITY,
			new Item(defaultBuilder()
					.food(new FoodProperties.Builder()
							.nutrition(0)
							.saturationModifier(0.1F)
							.effect(new MobEffectInstance(MobEffects.SATURATION, 20, 0), 1)
							.build())));
	public static final Item MANA_IN_A_BOTTLE = make(LibItemNames.MANA_IN_A_BOTTLE,
			new BottledManaItem(unstackable()
					// Mark as food just to fool foxes into using it
					.food(new FoodProperties.Builder().alwaysEdible().build())
					.component(BotaniaDataComponents.REMAINING_USES, BottledManaItem.SWIGS)));
	public static final Item SHARD_OF_LAPUTA = make(LibItemNames.SHARD_OF_LAPUTA,
			new LaputaShardItem(unstackable()
					.rarity(Rarity.RARE)));
	public static final Item NECRODERMAL_VIRUS = make(LibItemNames.NECRODERMAL_VIRUS,
			new EquestrianVirusItem(defaultBuilder()
					.rarity(Rarity.UNCOMMON)));
	public static final Item NULLODERMAL_VIRUS = make(LibItemNames.NULLODERMAL_VIRUS,
			new EquestrianVirusItem(defaultBuilder()
					.rarity(Rarity.UNCOMMON)));
	public static final Item SPARK = make(LibItemNames.SPARK, new ManaSparkItem(defaultBuilder()));
	public static final Item SPARK_AUGMENT_DISPERSIVE = make(LibItemNames.SPARK_AUGMENT + "_" + SparkUpgradeType.DISPERSIVE.name().toLowerCase(Locale.ROOT),
			new SparkAugmentItem(SparkUpgradeType.DISPERSIVE, defaultBuilder()));
	public static final Item SPARK_AUGMENT_DOMINANT = make(LibItemNames.SPARK_AUGMENT + "_" + SparkUpgradeType.DOMINANT.name().toLowerCase(Locale.ROOT),
			new SparkAugmentItem(SparkUpgradeType.DOMINANT, defaultBuilder()));
	public static final Item SPARK_AUGMENT_RECESSIVE = make(LibItemNames.SPARK_AUGMENT + "_" + SparkUpgradeType.RECESSIVE.name().toLowerCase(Locale.ROOT),
			new SparkAugmentItem(SparkUpgradeType.RECESSIVE, defaultBuilder()));
	public static final Item SPARK_AUGMENT_ISOLATED = make(LibItemNames.SPARK_AUGMENT + "_" + SparkUpgradeType.ISOLATED.name().toLowerCase(Locale.ROOT),
			new SparkAugmentItem(SparkUpgradeType.ISOLATED, defaultBuilder()));
	public static final Item CORPOREA_SPARK = make(LibItemNames.CORPOREA_SPARK,
			new CorporeaSparkItem(defaultBuilder()));
	public static final Item MASTER_CORPOREA_SPARK = make(LibItemNames.MASTER_CORPOREA_SPARK,
			new CorporeaSparkItem(defaultBuilder()));
	public static final Item CREATIVE_CORPOREA_SPARK = make(LibItemNames.CREATIVE_CORPOREA_SPARK,
			new CorporeaSparkItem(defaultBuilder()
					.rarity(Rarity.EPIC)));
	public static final Item BLACK_LOTUS = make(LibItemNames.BLACK_LOTUS,
			new BlackLotusItem(defaultBuilder()
					.rarity(Rarity.UNCOMMON)));
	public static final Item BLACKER_LOTUS = make(LibItemNames.BLACKER_LOTUS,
			new BlackLotusItem(defaultBuilder()
					.rarity(Rarity.RARE)));
	public static final Item WORLD_SEED = make(LibItemNames.WORLD_SEED,
			new WorldSeedItem(defaultBuilder()));
	public static final Item PHANTOM_INK = make(LibItemNames.PHANTOM_INK,
			new PhantomInkItem(defaultBuilder()));
	public static final Item POOL_MINECART = make(LibItemNames.POOL_MINECART,
			new ManaPoolMinecartItem(unstackable()));
	public static final Item RESOLUTE_IVY = make(LibItemNames.RESOLUTE_IVY,
			new ResoluteIvyItem(defaultBuilder()));
	public static final Item CRAFTING_PLACEHOLDER = make(LibItemNames.CRAFTING_PLACEHOLDER,
			new SelfReturningItem(defaultBuilder()));
	public static final Item CRAFTING_PATTERN_1_1 = make(LibItemNames.CRAFTING_PATTERN_PREFIX + "1_1",
			new CraftingPatternItem(CraftyCratePattern.CRAFTY_1_1, unstackable()));
	public static final Item CRAFTING_PATTERN_2_2 = make(LibItemNames.CRAFTING_PATTERN_PREFIX + "2_2",
			new CraftingPatternItem(CraftyCratePattern.CRAFTY_2_2, unstackable()));
	public static final Item CRAFTING_PATTERN_1_2 = make(LibItemNames.CRAFTING_PATTERN_PREFIX + "1_2",
			new CraftingPatternItem(CraftyCratePattern.CRAFTY_1_2, unstackable()));
	public static final Item CRAFTING_PATTERN_2_1 = make(LibItemNames.CRAFTING_PATTERN_PREFIX + "2_1",
			new CraftingPatternItem(CraftyCratePattern.CRAFTY_2_1, unstackable()));
	public static final Item CRAFTING_PATTERN_1_3 = make(LibItemNames.CRAFTING_PATTERN_PREFIX + "1_3",
			new CraftingPatternItem(CraftyCratePattern.CRAFTY_1_3, unstackable()));
	public static final Item CRAFTING_PATTERN_3_1 = make(LibItemNames.CRAFTING_PATTERN_PREFIX + "3_1",
			new CraftingPatternItem(CraftyCratePattern.CRAFTY_3_1, unstackable()));
	public static final Item CRAFTING_PATTERN_2_3 = make(LibItemNames.CRAFTING_PATTERN_PREFIX + "2_3",
			new CraftingPatternItem(CraftyCratePattern.CRAFTY_2_3, unstackable()));
	public static final Item CRAFTING_PATTERN_3_2 = make(LibItemNames.CRAFTING_PATTERN_PREFIX + "3_2",
			new CraftingPatternItem(CraftyCratePattern.CRAFTY_3_2, unstackable()));
	public static final Item CRAFTING_PATTERN_DONUT = make(LibItemNames.CRAFTING_PATTERN_PREFIX + "donut",
			new CraftingPatternItem(CraftyCratePattern.CRAFTY_DONUT, unstackable()));

	// Guardian of Gaia drops
	public static final Item DICE_OF_FATE = make(LibItemNames.DICE_OF_FATE,
			new DiceOfFateItem(unstackable()
					.fireResistant()
					.rarity(Rarity.EPIC)));
	public static final Item FRUIT_OF_GRISAIA = make(LibItemNames.THE_FRUIT_OF_GRISAIA,
			new FruitOfGrisaiaItem(unstackable()
					.fireResistant()
					.rarity(Rarity.EPIC)));
	public static final Item KEY_OF_THE_KINGS_LAW = make(LibItemNames.KEY_OF_THE_KINGS_LAW,
			new KeyOfTheKingsLawItem(unstackable()
					.fireResistant()
					.rarity(Rarity.EPIC)));
	public static final Item EYE_OF_THE_FLUGEL = make(LibItemNames.EYE_OF_THE_FLUGEL,
			new EyeOfTheFlugelItem(unstackable()
					.fireResistant()
					.rarity(Rarity.EPIC)));
	public static final Item RING_OF_THOR = make(LibItemNames.RING_OF_THOR,
			new RingOfThorItem(unstackable()
					.fireResistant()
					.rarity(Rarity.EPIC)));
	public static final Item RING_OF_ODIN = make(LibItemNames.RING_OF_ODIN,
			new RingOfOdinItem(unstackable()
					.fireResistant()
					.rarity(Rarity.EPIC)));
	public static final Item RING_OF_LOKI = make(LibItemNames.RING_OF_LOKI,
			new RingOfLokiItem(unstackable()
					.fireResistant()
					.rarity(Rarity.EPIC)));
	public static final Item SCATHED_MUSIC_DISC_1 = make(LibItemNames.SCATHED_MUSIC_DISC_1,
			new Item(unstackable()
					.rarity(Rarity.RARE)
					.jukeboxPlayable(BotaniaJukeboxSongs.GAIA_MUSIC_1)));
	public static final Item SCATHED_MUSIC_DISC_2 = make(LibItemNames.SCATHED_MUSIC_DISC_2,
			new Item(unstackable()
					.rarity(Rarity.RARE)
					.jukeboxPlayable(BotaniaJukeboxSongs.GAIA_MUSIC_2)));
	public static final Item WILL_OF_AHRIM = make(LibItemNames.ANCIENT_WILL_PREFIX + "ahrim",
			new AncientWillItem(AncientWillContainer.AncientWillType.AHRIM, unstackable()
					.rarity(Rarity.RARE)));
	public static final Item WILL_OF_DHAROK = make(LibItemNames.ANCIENT_WILL_PREFIX + "dharok",
			new AncientWillItem(AncientWillContainer.AncientWillType.DHAROK, unstackable()
					.rarity(Rarity.RARE)));
	public static final Item WILL_OF_GUTHAN = make(LibItemNames.ANCIENT_WILL_PREFIX + "guthan",
			new AncientWillItem(AncientWillContainer.AncientWillType.GUTHAN, unstackable()
					.rarity(Rarity.RARE)));
	public static final Item WILL_OF_TORAG = make(LibItemNames.ANCIENT_WILL_PREFIX + "torag",
			new AncientWillItem(AncientWillContainer.AncientWillType.TORAG, unstackable()
					.rarity(Rarity.RARE)));
	public static final Item WILL_OF_VERAC = make(LibItemNames.ANCIENT_WILL_PREFIX + "verac",
			new AncientWillItem(AncientWillContainer.AncientWillType.VERAC, unstackable()
					.rarity(Rarity.RARE)));
	public static final Item WILL_OF_KARIL = make(LibItemNames.ANCIENT_WILL_PREFIX + "karil",
			new AncientWillItem(AncientWillContainer.AncientWillType.KARIL, unstackable()
					.rarity(Rarity.RARE)));
	public static final Item THE_PINKINATOR = make(LibItemNames.THE_PINKINATOR,
			new PinkinatorItem(unstackable()
					.rarity(Rarity.RARE)));

	// Brewing
	public static final Item MANAGLASS_VIAL = make(LibItemNames.MANAGLASS_VIAL,
			new VialItem(defaultBuilder()));
	public static final Item ALFGLASS_FLASK = make(LibItemNames.ALFGLASS_FLASK,
			new VialItem(defaultBuilder()));
	public static final BaseBrewItem BREW_VIAL = make(LibItemNames.BREW_VIAL,
			new BaseBrewItem(32, () -> MANAGLASS_VIAL, unstackable()
					.component(BotaniaDataComponents.MAX_USES, BaseBrewItem.DEFAULT_USES_VIAL)));
	public static final BaseBrewItem BREW_FLASK = make(LibItemNames.BREW_FLASK,
			new BaseBrewItem(24, () -> ALFGLASS_FLASK, unstackable()
					.component(BotaniaDataComponents.MAX_USES, BaseBrewItem.DEFAULT_USES_FLASK)));
	public static final Item TAINTED_BLOOD_PENDANT = make(LibItemNames.TAINTED_BLOOD_PENDANT,
			new TaintedBloodPendantItem(unstackable()));
	public static final Item INCENSE_STICK = make(LibItemNames.INCENSE_STICK,
			new IncenseStickItem(unstackable()));

	// Cosmetics
	public static final Item COSMETIC_BLACK_BOWTIE = make(LibItemNames.COSMETIC_PREFIX + "black_bowtie",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.BLACK_BOWTIE, unstackable()));
	public static final Item COSMETIC_BLACK_TIE = make(LibItemNames.COSMETIC_PREFIX + "black_tie",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.BLACK_TIE, unstackable()));
	public static final Item COSMETIC_RED_GLASSES = make(LibItemNames.COSMETIC_PREFIX + "red_glasses",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.RED_GLASSES, unstackable()));
	public static final Item COSMETIC_PUFFY_SCARF = make(LibItemNames.COSMETIC_PREFIX + "puffy_scarf",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.PUFFY_SCARF, unstackable()));
	public static final Item COSMETIC_ENGINEER_GOGGLES = make(LibItemNames.COSMETIC_PREFIX + "engineer_goggles",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.ENGINEER_GOGGLES, unstackable()));
	public static final Item COSMETIC_EYEPATCH = make(LibItemNames.COSMETIC_PREFIX + "eyepatch",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.EYEPATCH, unstackable()));
	public static final Item COSMETIC_WICKED_EYEPATCH = make(LibItemNames.COSMETIC_PREFIX + "wicked_eyepatch",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.WICKED_EYEPATCH, unstackable()));
	public static final Item COSMETIC_RED_RIBBONS = make(LibItemNames.COSMETIC_PREFIX + "red_ribbons",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.RED_RIBBONS, unstackable()));
	public static final Item COSMETIC_PINK_FLOWER_BUD = make(LibItemNames.COSMETIC_PREFIX + "pink_flower_bud",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.PINK_FLOWER_BUD, unstackable()));
	public static final Item COSMETIC_POLKA_DOTTED_BOWS = make(LibItemNames.COSMETIC_PREFIX + "polka_dotted_bows",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.POLKA_DOTTED_BOWS, unstackable()));
	public static final Item COSMETIC_BLUE_BUTTERFLY = make(LibItemNames.COSMETIC_PREFIX + "blue_butterfly",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.BLUE_BUTTERFLY, unstackable()));
	public static final Item COSMETIC_CAT_EARS = make(LibItemNames.COSMETIC_PREFIX + "cat_ears",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.CAT_EARS, unstackable()));
	public static final Item COSMETIC_WITCH_PIN = make(LibItemNames.COSMETIC_PREFIX + "witch_pin",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.WITCH_PIN, unstackable()));
	public static final Item COSMETIC_DEVIL_TAIL = make(LibItemNames.COSMETIC_PREFIX + "devil_tail",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.DEVIL_TAIL, unstackable()));
	public static final Item COSMETIC_KAMUI_EYE = make(LibItemNames.COSMETIC_PREFIX + "kamui_eye",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.KAMUI_EYE, unstackable()));
	public static final Item COSMETIC_GOOGLY_EYES = make(LibItemNames.COSMETIC_PREFIX + "googly_eyes",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.GOOGLY_EYES, unstackable()));
	public static final Item COSMETIC_FOUR_LEAFED_CLOVER = make(LibItemNames.COSMETIC_PREFIX + "four_leaf_clover",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.FOUR_LEAF_CLOVER, unstackable()));
	public static final Item COSMETIC_CLOCK_EYE = make(LibItemNames.COSMETIC_PREFIX + "clock_eye",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.CLOCK_EYE, unstackable()));
	public static final Item COSMETIC_UNICORN_HORN = make(LibItemNames.COSMETIC_PREFIX + "unicorn_horn",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.UNICORN_HORN, unstackable()));
	public static final Item COSMETIC_DEVIL_HORNS = make(LibItemNames.COSMETIC_PREFIX + "devil_horns",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.DEVIL_HORNS, unstackable()));
	public static final Item COSMETIC_HYPER_PLUS = make(LibItemNames.COSMETIC_PREFIX + "hyper_plus",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.HYPER_PLUS, unstackable()));
	public static final Item COSMETIC_BOTANIST_EMBLEM = make(LibItemNames.COSMETIC_PREFIX + "botanist_emblem",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.BOTANIST_EMBLEM, unstackable()));
	public static final Item COSMETIC_ANCIENT_MASK = make(LibItemNames.COSMETIC_PREFIX + "ancient_mask",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.ANCIENT_MASK, unstackable()));
	public static final Item COSMETIC_EERIE_MASK = make(LibItemNames.COSMETIC_PREFIX + "eerie_mask",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.EERIE_MASK, unstackable()));
	public static final Item COSMETIC_ALIEN_ANTENNA = make(LibItemNames.COSMETIC_PREFIX + "alien_antenna",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.ALIEN_ANTENNA, unstackable()));
	public static final Item COSMETIC_ANAGLYPH_GLASSES = make(LibItemNames.COSMETIC_PREFIX + "anaglyph_glasses",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.ANAGLYPH_GLASSES, unstackable()));
	public static final Item COSMETIC_ORANGE_SHADES = make(LibItemNames.COSMETIC_PREFIX + "orange_shades",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.ORANGE_SHADES, unstackable()));
	public static final Item COSMETIC_GROUCHO_GLASSES = make(LibItemNames.COSMETIC_PREFIX + "groucho_glasses",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.GROUCHO_GLASSES, unstackable()));
	public static final Item COSMETIC_THICK_EYEBROWS = make(LibItemNames.COSMETIC_PREFIX + "thick_eyebrows",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.THICK_EYEBROWS, unstackable()));
	public static final Item COSMETIC_LUSITANIC_SHIELD = make(LibItemNames.COSMETIC_PREFIX + "lusitanic_shield",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.LUSITANIC_SHIELD, unstackable()));
	public static final Item COSMETIC_TINY_POTATO_MASK = make(LibItemNames.COSMETIC_PREFIX + "tiny_potato_mask",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.TINY_POTATO_MASK, unstackable()));
	public static final Item COSMETIC_QUESTGIVER_MARK = make(LibItemNames.COSMETIC_PREFIX + "questgiver_mark",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.QUESTGIVER_MARK, unstackable()));
	public static final Item COSMETIC_THINKING_HAND = make(LibItemNames.COSMETIC_PREFIX + "thinking_hand",
			new CosmeticBaubleItem(CosmeticBaubleItem.Variant.THINKING_HAND, unstackable()));

	// Banner patterns
	public static final Item BANNER_PATTERN_BOTANIA = make("botania_banner_pattern",
			new BannerPatternItem(BotaniaTags.BannerPatterns.PATTERN_ITEM_BOTANIA, unstackable()
					.rarity(Rarity.UNCOMMON)));
	public static final Item BANNER_PATTERN_MATERIALS = make("materials_banner_pattern",
			new BannerPatternItem(BotaniaTags.BannerPatterns.PATTERN_ITEM_MATERIALS, unstackable()));
	public static final Item BANNER_PATTERN_SPARK_AUGMENTS = make("spark_augments_banner_pattern",
			new BannerPatternItem(BotaniaTags.BannerPatterns.PATTERN_ITEM_SPARK_AUGMENTS, unstackable()));
	public static final Item BANNER_PATTERN_TOOLS = make("tools_banner_pattern",
			new BannerPatternItem(BotaniaTags.BannerPatterns.PATTERN_ITEM_TOOLS, unstackable()));

	public static final MenuType<TrinketCaseMenu> TRINKET_CASE_MENU_TYPE =
			XplatAbstractions.INSTANCE.createMenuType(TrinketCaseMenu::new, ByteBufCodecs.BOOL);
	public static final MenuType<ColoredContentsPouchMenu> COLORED_CONTENTS_POUCH_MENU_TYPE =
			XplatAbstractions.INSTANCE.createMenuType(ColoredContentsPouchMenu::new, ByteBufCodecs.BOOL);
	public static final MenuType<HandOfEnderMenu> HAND_OF_ENDER_MENU_TYPE =
			XplatAbstractions.INSTANCE.createMenuType(HandOfEnderMenu::new, ByteBufCodecs.BOOL);

	public static final CauldronInteraction MANA_POOL_INTERACTION = BotaniaItems::cauldronUndyeManaPool;
	public static final CauldronInteraction MANA_LENS_INTERACTION = BotaniaItems::cauldronUndyeManaLens;
	public static final CauldronInteraction PHANTOM_INK_INTERACTION = BotaniaItems::cauldronRemovePhantomInk;

	private static ItemInteractionResult cauldronUndyeManaPool(BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, ItemStack stack) {
		Block block = Block.byItem(stack.getItem());
		if (!(block instanceof ManaPoolBlock poolBlock)) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}
		if (!level.isClientSide()) {
			ManaPoolBlock undyedPool = ManaPoolBlock.getUndyedBlock(poolBlock);
			ItemStack itemstack = stack.transmuteCopy(undyedPool, 1);
			player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, itemstack, false));
			player.awardStat(BotaniaStats.MANA_POOLS_CLEANED);
			LayeredCauldronBlock.lowerFillLevel(state, level, pos);
		}
		return ItemInteractionResult.sidedSuccess(level.isClientSide());
	}

	private static ItemInteractionResult cauldronUndyeManaLens(BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, ItemStack stack) {
		if (!LensItem.isLensTinted(stack)) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}
		if (!level.isClientSide()) {
			stack.remove(BotaniaDataComponents.LENS_RAINBOW_TINT);
			stack.remove(BotaniaDataComponents.LENS_TINT);
			player.awardStat(BotaniaStats.MANA_LENSES_CLEANED);
			LayeredCauldronBlock.lowerFillLevel(state, level, pos);
		}
		return ItemInteractionResult.sidedSuccess(level.isClientSide());
	}

	private static ItemInteractionResult cauldronRemovePhantomInk(BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, ItemStack stack) {
		if (!(stack.getItem() instanceof PhantomInkable inkable) || !inkable.hasPhantomInk(stack)) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}
		if (!level.isClientSide()) {
			inkable.setPhantomInk(stack, false);
			player.awardStat(BotaniaStats.PHANTOM_INK_CLEANED);
			LayeredCauldronBlock.lowerFillLevel(state, level, pos);
		}
		return ItemInteractionResult.sidedSuccess(level.isClientSide());
	}

	private static <T extends Item> T make(String name, T item) {
		var old = ALL.put(name, item);
		if (old != null) {
			throw new IllegalArgumentException("Typo? Duplicate name: " + name);
		}
		return item;
	}

	private static MysticalPetalItem makePetal(DyeColor dyeColor) {
		return make(dyeColor.getName() + LibItemNames.MYSTICAL_PETAL_SUFFIX,
				new MysticalPetalItem(BotaniaBlocks.getBuriedPetal(dyeColor), dyeColor, defaultBuilder()));
	}

	public static Item.Properties defaultBuilder() {
		return XplatAbstractions.INSTANCE.defaultItemBuilder();
	}

	// NeoForge does custom damage by just implementing a method on Item,
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
			r.accept(e.getValue(), botaniaRL(e.getKey()));
		}
	}

	public static void registerMenuTypes(BiConsumer<MenuType<?>, ResourceLocation> consumer) {
		consumer.accept(TRINKET_CASE_MENU_TYPE, botaniaRL(LibItemNames.TRINKET_CASE));
		consumer.accept(COLORED_CONTENTS_POUCH_MENU_TYPE, botaniaRL(LibItemNames.FLOWER_POUCH));
		consumer.accept(HAND_OF_ENDER_MENU_TYPE, botaniaRL(LibItemNames.HAND_OF_ENDER));
	}

	public static void registerCauldronInteractions() {
		ColorHelper.supportedColors().forEach(color -> {
			CauldronInteraction.WATER.map()
					.put(BotaniaBlocks.findOptionallyDyedBlock(BotaniaBlocks.MANA_POOL, color).asItem(),
							MANA_POOL_INTERACTION);
			CauldronInteraction.WATER.map()
					.put(BotaniaBlocks.findOptionallyDyedBlock(BotaniaBlocks.CREATIVE_MANA_POOL, color).asItem(),
							MANA_POOL_INTERACTION);
			CauldronInteraction.WATER.map()
					.put(BotaniaBlocks.findOptionallyDyedBlock(BotaniaBlocks.DILUTED_MANA_POOL, color).asItem(),
							MANA_POOL_INTERACTION);
			CauldronInteraction.WATER.map()
					.put(BotaniaBlocks.findOptionallyDyedBlock(BotaniaBlocks.FABULOUS_MANA_POOL, color).asItem(),
							MANA_POOL_INTERACTION);
		});

		ALL.values().stream().filter(item -> item instanceof LensItem)
				.forEach(item -> CauldronInteraction.WATER.map().put(item, MANA_LENS_INTERACTION));
		ALL.values().stream().filter(item -> item instanceof PhantomInkable)
				.forEach(item -> CauldronInteraction.WATER.map().put(item, PHANTOM_INK_INTERACTION));
	}

	public static Item getPetal(DyeColor color) {
		return switch (color) {
			case WHITE -> WHITE_MYSTICAL_PETAL;
			case ORANGE -> ORANGE_MYSTICAL_PETAL;
			case MAGENTA -> MAGENTA_MYSTICAL_PETAL;
			case LIGHT_BLUE -> LIGHT_BLUE_MYSTICAL_PETAL;
			case YELLOW -> YELLOW_MYSTICAL_PETAL;
			case LIME -> LIME_MYSTICAL_PETAL;
			case PINK -> PINK_MYSTICAL_PETAL;
			case GRAY -> GRAY_MYSTICAL_PETAL;
			case LIGHT_GRAY -> LIGHT_GRAY_MYSTICAL_PETAL;
			case CYAN -> CYAN_MYSTICAL_PETAL;
			case PURPLE -> PURPLE_MYSTICAL_PETAL;
			case BLUE -> BLUE_MYSTICAL_PETAL;
			case BROWN -> BROWN_MYSTICAL_PETAL;
			case GREEN -> GREEN_MYSTICAL_PETAL;
			case RED -> RED_MYSTICAL_PETAL;
			case BLACK -> BLACK_MYSTICAL_PETAL;
		};
	}

	public static boolean isNoDespawn(Item item) {
		return item instanceof ManaTabletItem || item instanceof BandOfManaItem || item instanceof TerraShattererItem
				|| item instanceof RelicItem || item instanceof RelicBaubleItem;
	}
}
