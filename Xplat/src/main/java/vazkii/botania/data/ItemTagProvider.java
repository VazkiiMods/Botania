/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import vazkii.botania.api.internal.OptionallyColored;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.mana.ManaSpreaderBlock;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.common.item.AncientWillItem;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.lens.LensItem;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.data.util.DummyTagLookup;

import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class ItemTagProvider extends ItemTagsProvider {
	private static final Set<TagKey<Item>> REQUIRED_TAGS = Set.of(
			ItemTags.SAND,
			ItemTags.ARROWS
	);

	public ItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
			CompletableFuture<TagsProvider.TagLookup<Block>> blockTags) {
		super(output, lookupProvider, DummyTagLookup.completedFuture(REQUIRED_TAGS), blockTags);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(ItemTags.AXES)
				.add(BotaniaItems.MANASTEEL_AXE, BotaniaItems.ELEMENTIUM_AXE, BotaniaItems.TERRA_TRUNCATOR);
		this.tag(ItemTags.HOES).add(BotaniaItems.MANASTEEL_HOE, BotaniaItems.ELEMENTIUM_HOE);
		this.tag(ItemTags.PICKAXES).add(
				BotaniaItems.MANASTEEL_PICKAXE, BotaniaItems.ELEMENTIUM_PICKAXE, BotaniaItems.TERRA_SHATTERER,
				BotaniaItems.VITREOUS_PICKAXE);
		this.tag(ItemTags.SHOVELS).add(BotaniaItems.MANASTEEL_SHOVEL, BotaniaItems.ELEMENTIUM_SHOVEL);
		this.tag(ItemTags.SWORDS).add(
				BotaniaItems.MANASTEEL_SWORD, BotaniaItems.ELEMENTIUM_SWORD, BotaniaItems.TERRA_BLADE,
				BotaniaItems.THUNDERCALLER, BotaniaItems.STARCALLER);
		this.tag(ItemTags.BOW_ENCHANTABLE).add(BotaniaItems.LIVINGWOOD_BOW, BotaniaItems.CRYSTAL_BOW);
		this.tag(ItemTags.DURABILITY_ENCHANTABLE).add(
				BotaniaItems.LIVINGWOOD_BOW, BotaniaItems.CRYSTAL_BOW, BotaniaItems.MANASTEEL_SHEARS,
				BotaniaItems.ELEMENTIUM_SHEARS);
		this.tag(ItemTags.MINING_ENCHANTABLE).add(BotaniaItems.MANASTEEL_SHEARS, BotaniaItems.ELEMENTIUM_SHEARS);

		this.tag(ItemTags.HEAD_ARMOR).add(
				BotaniaItems.MANASTEEL_HELMET, BotaniaItems.MANAWEAVE_HELMET,
				BotaniaItems.ELEMENTIUM_HELMET, BotaniaItems.TERRASTEEL_HELMET);
		this.tag(ItemTags.CHEST_ARMOR).add(
				BotaniaItems.MANASTEEL_CHESTPLATE, BotaniaItems.MANAWEAVE_CHESTPLATE,
				BotaniaItems.ELEMENTIUM_CHESTPLATE, BotaniaItems.TERRASTEEL_CHESTPLATE);
		this.tag(ItemTags.LEG_ARMOR).add(
				BotaniaItems.MANASTEEL_LEGGINGS, BotaniaItems.MANAWEAVE_LEGGINGS,
				BotaniaItems.ELEMENTIUM_LEGGINGS, BotaniaItems.TERRASTEEL_LEGGINGS);
		this.tag(ItemTags.FOOT_ARMOR).add(
				BotaniaItems.MANASTEEL_BOOTS, BotaniaItems.MANAWEAVE_BOOTS,
				BotaniaItems.ELEMENTIUM_BOOTS, BotaniaItems.TERRASTEEL_BOOTS);

		this.copy(BlockTags.RAILS, ItemTags.RAILS);
		this.copy(BlockTags.SLABS, ItemTags.SLABS);
		this.copy(BlockTags.PLANKS, ItemTags.PLANKS);
		this.copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
		this.copy(BlockTags.STAIRS, ItemTags.STAIRS);
		this.copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
		this.copy(BlockTags.WALLS, ItemTags.WALLS);
		this.copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
		this.copy(BlockTags.FENCE_GATES, ItemTags.FENCE_GATES);
		this.copy(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
		this.copy(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);
		this.copy(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
		this.copy(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
		this.copy(BlockTags.STANDING_SIGNS, ItemTags.SIGNS);
		this.copy(BlockTags.CEILING_HANGING_SIGNS, ItemTags.HANGING_SIGNS);
		this.copy(BlockTags.STONE_BUTTONS, ItemTags.STONE_BUTTONS);

		this.copy(BotaniaTags.Blocks.MUNDANE_FLOATING_FLOWERS, BotaniaTags.Items.MUNDANE_FLOATING_FLOWERS);
		this.copy(BotaniaTags.Blocks.MISC_FLOATING_FLOWERS, BotaniaTags.Items.MISC_FLOATING_FLOWERS);
		this.copy(BotaniaTags.Blocks.GENERATING_FLOATING_FLOWERS, BotaniaTags.Items.GENERATING_FLOATING_FLOWERS);
		this.copy(BotaniaTags.Blocks.FUNCTIONAL_FLOATING_FLOWERS, BotaniaTags.Items.FUNCTIONAL_FLOATING_FLOWERS);
		this.copy(BotaniaTags.Blocks.SPECIAL_FLOATING_FLOWERS, BotaniaTags.Items.SPECIAL_FLOATING_FLOWERS);
		this.copy(BotaniaTags.Blocks.FLOATING_FLOWERS, BotaniaTags.Items.FLOATING_FLOWERS);
		this.copy(BotaniaTags.Blocks.TALL_MYSTICAL_FLOWERS, BotaniaTags.Items.TALL_MYSTICAL_FLOWERS);
		this.copy(BotaniaTags.Blocks.SMALL_MYSTICAL_FLOWERS, BotaniaTags.Items.SMALL_MYSTICAL_FLOWERS);
		this.copy(BotaniaTags.Blocks.GLIMMERING_FLOWERS, BotaniaTags.Items.GLIMMERING_FLOWERS);
		this.copy(BotaniaTags.Blocks.SHIMMERING_MUSHROOMS, BotaniaTags.Items.SHIMMERING_MUSHROOMS);

		this.copy(BotaniaTags.Blocks.MISC_SPECIAL_FLOWERS, BotaniaTags.Items.MISC_SPECIAL_FLOWERS);
		this.copy(BotaniaTags.Blocks.GENERATING_SPECIAL_FLOWERS, BotaniaTags.Items.GENERATING_SPECIAL_FLOWERS);
		this.copy(BotaniaTags.Blocks.FUNCTIONAL_SPECIAL_FLOWERS, BotaniaTags.Items.FUNCTIONAL_SPECIAL_FLOWERS);
		this.copy(BotaniaTags.Blocks.SPECIAL_FLOWERS, BotaniaTags.Items.SPECIAL_FLOWERS);
		this.copy(BotaniaTags.Blocks.MINI_FLOWERS, BotaniaTags.Items.MINI_FLOWERS);
		this.tag(BotaniaTags.Items.CONTRIBUTOR_HEADFLOWERS)
				.addTag(BotaniaTags.Items.SPECIAL_FLOWERS)
				.add(BotaniaBlocks.DAYBLOOM_MOTIF.asItem(), BotaniaBlocks.NIGHTSHADE_MOTIF.asItem());

		this.tag(ItemTags.TALL_FLOWERS).addTag(BotaniaTags.Items.TALL_MYSTICAL_FLOWERS);
		this.tag(ItemTags.SMALL_FLOWERS)
				.addTag(BotaniaTags.Items.SMALL_MYSTICAL_FLOWERS)
				.addTag(BotaniaTags.Items.GLIMMERING_FLOWERS)
				.addTag(BotaniaTags.Items.SPECIAL_FLOWERS)
				.add(
						BotaniaBlocks.DAYBLOOM_MOTIF.asItem(),
						BotaniaBlocks.NIGHTSHADE_MOTIF.asItem(),
						BotaniaBlocks.HYDROANGEAS_MOTIF.asItem());

		this.tag(BotaniaTags.Items.BURST_VIEWERS).add(BotaniaItems.MANASEER_MONOCLE);
		this.tag(BotaniaTags.Items.LENS).add(getItems(LensItem.class::isInstance));

		this.tag(BotaniaTags.Items.LENS_GLUE).add(Items.SLIME_BALL).add(Items.HONEY_BOTTLE);

		this.tag(ItemTags.PIGLIN_LOVED).add(
				BotaniaBlocks.ALCHEMY_CATALYST.asItem(), BotaniaItems.CHARM_OF_THE_DIVA,
				BotaniaBlocks.HOVERING_HOURGLASS.asItem(), BotaniaBlocks.MANA_PYLON.asItem(),
				BotaniaItems.MANASEER_MONOCLE);
		this.tag(ItemTags.CLUSTER_MAX_HARVESTABLES).add(
				BotaniaItems.MANASTEEL_PICKAXE, BotaniaItems.ELEMENTIUM_PICKAXE, BotaniaItems.TERRA_SHATTERER,
				BotaniaItems.VITREOUS_PICKAXE);
		this.tag(ItemTags.LECTERN_BOOKS).add(BotaniaItems.LEXICA_BOTANIA);
		this.tag(ItemTags.BOOKSHELF_BOOKS).add(BotaniaItems.LEXICA_BOTANIA);
		this.tag(ItemTags.BEACON_PAYMENT_ITEMS).add(
				BotaniaItems.MANASTEEL_INGOT, BotaniaItems.TERRASTEEL_INGOT, BotaniaItems.ELEMENTIUM_INGOT,
				BotaniaItems.MANA_DIAMOND, BotaniaItems.DRAGONSTONE);

		this.copy(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
		this.copy(BotaniaTags.Blocks.LIVINGWOOD_LOGS, BotaniaTags.Items.LIVINGWOOD_LOGS);
		this.copy(BotaniaTags.Blocks.LIVINGWOOD_LOGS_GLIMMERING, BotaniaTags.Items.LIVINGWOOD_LOGS_GLIMMERING);
		this.copy(BotaniaTags.Blocks.DREAMWOOD_LOGS, BotaniaTags.Items.DREAMWOOD_LOGS);
		this.copy(BotaniaTags.Blocks.DREAMWOOD_LOGS_GLIMMERING, BotaniaTags.Items.DREAMWOOD_LOGS_GLIMMERING);

		this.tag(BotaniaTags.Items.DISPOSABLE)
				.add(
						Items.DIRT, Items.GRAVEL, Items.COBBLESTONE, Items.NETHERRACK, Items.COBBLED_DEEPSLATE,
						Items.END_STONE)
				.addTag(ItemTags.SAND);
		this.tag(BotaniaTags.Items.SEMI_DISPOSABLE)
				.add(
						Items.ANDESITE, Items.DIORITE, Items.GRANITE, Items.TUFF, Items.CALCITE, Items.STONE,
						Items.BASALT, Items.BLACKSTONE, Items.DEEPSLATE, Items.DRIPSTONE_BLOCK, Items.POINTED_DRIPSTONE,
						Items.MOSS_BLOCK, Items.SANDSTONE, Items.RED_SANDSTONE)
				.addOptional(ResourceLocation.fromNamespaceAndPath("quark", "jasper"))
				.addOptional(ResourceLocation.fromNamespaceAndPath("quark", "limestone"))
				.addOptional(ResourceLocation.fromNamespaceAndPath("quark", "marble"))
				.addOptional(ResourceLocation.fromNamespaceAndPath("quark", "slate"));

		this.copy(BotaniaTags.Blocks.BLOCKS_QUARTZ, BotaniaTags.Items.BLOCKS_QUARTZ);

		this.tag(BotaniaTags.Items.RUNES).add(
				BotaniaItems.RUNE_OF_WATER, BotaniaItems.RUNE_OF_FIRE, BotaniaItems.RUNE_OF_EARTH,
				BotaniaItems.RUNE_OF_AIR, BotaniaItems.RUNE_OF_SPRING, BotaniaItems.RUNE_OF_SUMMER,
				BotaniaItems.RUNE_OF_AUTUMN, BotaniaItems.RUNE_OF_WINTER, BotaniaItems.RUNE_OF_MANA,
				BotaniaItems.RUNE_OF_LUST, BotaniaItems.RUNE_OF_GLUTTONY, BotaniaItems.RUNE_OF_GREED,
				BotaniaItems.RUNE_OF_SLOTH, BotaniaItems.RUNE_OF_WRATH, BotaniaItems.RUNE_OF_ENVY,
				BotaniaItems.RUNE_OF_PRIDE
		);

		this.tag(BotaniaTags.Items.ANCIENT_WILLS).add(getItems(AncientWillItem.class::isInstance));

		TagAppender<Item> allPetals = this.tag(BotaniaTags.Items.PETALS);
		ColorHelper.supportedColors().forEach(color -> {
			var petalTag = BotaniaTags.Items.getPetalTag(color);
			this.tag(petalTag).add(BotaniaItems.getPetal(color), BotaniaBlocks.getShimmeringMushroom(color).asItem());
			allPetals.addTag(petalTag);
		});

		IntrinsicTagAppender<Item> petalApothecaries = this.tag(BotaniaTags.Items.PETAL_APOTHECARIES);
		for (Block apothecary : BotaniaBlocks.ALL_APOTHECARIES) {
			petalApothecaries.add(apothecary.asItem());
		}

		this.tag(BotaniaTags.Items.LOONIUM_OFFHAND_EQUIPMENT)
				.add(Items.FIREWORK_ROCKET, Items.TOTEM_OF_UNDYING)
				.addTag(ItemTags.ARROWS);
		this.tag(BotaniaTags.Items.MAGNET_RING_IGNORED);
		this.tag(BotaniaTags.Items.RODS).add(
				BotaniaItems.ROD_OF_THE_LANDS,
				BotaniaItems.ROD_OF_THE_HIGHLANDS,
				BotaniaItems.ROD_OF_THE_TERRA_FIRMA,
				BotaniaItems.ROD_OF_THE_DEPTHS,
				BotaniaItems.ROD_OF_THE_SEAS,
				BotaniaItems.ROD_OF_THE_SKIES,
				BotaniaItems.ROD_OF_THE_HELLS,
				BotaniaItems.ROD_OF_THE_PLENTIFUL_MANTLE,
				BotaniaItems.ROD_OF_THE_MOLTEN_CORE,
				BotaniaItems.ROD_OF_THE_SHIFTING_CRUST,
				BotaniaItems.ROD_OF_THE_BIFROST,
				BotaniaItems.ROD_OF_THE_SHADED_MESA,
				BotaniaItems.ROD_OF_THE_UNSTABLE_RESERVOIR
		);
		this.tag(BotaniaTags.Items.MANA_USING_ITEMS).add(
				BotaniaItems.TAINTED_BLOOD_PENDANT,
				BotaniaItems.ROD_OF_THE_DEPTHS,
				BotaniaItems.CRYSTAL_BOW,
				BotaniaItems.ROD_OF_THE_LANDS,
				BotaniaItems.CHARM_OF_THE_DIVA,
				BotaniaItems.ROD_OF_THE_PLENTIFUL_MANTLE,
				BotaniaItems.ELEMENTIUM_AXE,
				BotaniaItems.ELEMENTIUM_BOOTS,
				BotaniaItems.ELEMENTIUM_CHESTPLATE,
				BotaniaItems.ELEMENTIUM_HELMET,
				BotaniaItems.ELEMENTIUM_HOE,
				BotaniaItems.ELEMENTIUM_LEGGINGS,
				BotaniaItems.ELEMENTIUM_PICKAXE,
				BotaniaItems.ELEMENTIUM_SHEARS,
				BotaniaItems.ELEMENTIUM_SHOVEL,
				BotaniaItems.ELEMENTIUM_SWORD,
				BotaniaItems.HAND_OF_ENDER,
				BotaniaItems.ROD_OF_THE_SHIFTING_CRUST,
				BotaniaItems.ROD_OF_THE_HELLS,
				BotaniaItems.FLUGEL_TIARA,
				BotaniaItems.EYE_OF_THE_FLUGEL,
				BotaniaItems.VITREOUS_PICKAXE,
				BotaniaItems.BENEVOLENT_GODDESS_CHARM,
				BotaniaItems.ROD_OF_THE_SHADED_MESA,
				BotaniaItems.FRUIT_OF_GRISAIA,
				BotaniaItems.INVISIBILITY_CLOAK,
				BotaniaItems.KEY_OF_THE_KINGS_LAW,
				BotaniaItems.LIVINGWOOD_BOW,
				BotaniaItems.RING_OF_LOKI,
				BotaniaItems.MANASTEEL_AXE,
				BotaniaItems.MANASTEEL_BOOTS,
				BotaniaItems.MANASTEEL_CHESTPLATE,
				BotaniaItems.MANASTEEL_HELMET,
				BotaniaItems.MANASTEEL_HOE,
				BotaniaItems.MANASTEEL_LEGGINGS,
				BotaniaItems.MANASTEEL_PICKAXE,
				BotaniaItems.MANASTEEL_SHEARS,
				BotaniaItems.MANASTEEL_SHOVEL,
				BotaniaItems.MANASTEEL_SWORD,
				BotaniaItems.MANAWEAVE_BOOTS,
				BotaniaItems.MANAWEAVE_CHESTPLATE,
				BotaniaItems.MANAWEAVE_HELMET,
				BotaniaItems.MANAWEAVE_LEGGINGS,
				BotaniaItems.RING_OF_THE_MANTLE,
				BotaniaItems.ROD_OF_THE_UNSTABLE_RESERVOIR,
				BotaniaItems.ROD_OF_THE_BIFROST,
				BotaniaItems.ROD_OF_THE_HIGHLANDS,
				BotaniaItems.ROD_OF_THE_MOLTEN_CORE,
				BotaniaItems.STARCALLER,
				BotaniaItems.GLOBETROTTERS_SASH,
				BotaniaItems.TERRA_TRUNCATOR,
				BotaniaItems.TERRA_SHATTERER,
				BotaniaItems.TERRA_BLADE,
				BotaniaItems.ROD_OF_THE_TERRA_FIRMA,
				BotaniaItems.TERRASTEEL_BOOTS,
				BotaniaItems.TERRASTEEL_CHESTPLATE,
				BotaniaItems.TERRASTEEL_HELMET,
				BotaniaItems.TERRASTEEL_LEGGINGS,
				BotaniaItems.THIRD_EYE,
				BotaniaItems.THUNDERCALLER,
				BotaniaItems.ROD_OF_THE_SKIES,
				BotaniaItems.SOJOURNERS_SASH,
				BotaniaItems.RING_OF_CHORDATA,
				BotaniaItems.ROD_OF_THE_SEAS
		);
		this.tag(ItemTags.FREEZE_IMMUNE_WEARABLES).add(
				BotaniaItems.SNOWFLAKE_PENDANT, BotaniaItems.MANAWEAVE_HELMET, BotaniaItems.MANAWEAVE_CHESTPLATE,
				BotaniaItems.MANAWEAVE_LEGGINGS, BotaniaItems.MANAWEAVE_BOOTS);

		this.tag(BotaniaTags.Items.MANA_POWDER_SOURCE_DUSTS).add(
				Items.GUNPOWDER, Items.REDSTONE, Items.GLOWSTONE_DUST, Items.SUGAR);

		this.tag(BotaniaTags.Items.PICKABLE_BLOCK_PROVIDER).add(
				BotaniaItems.ROD_OF_THE_LANDS, BotaniaItems.ROD_OF_THE_HIGHLANDS, BotaniaItems.ROD_OF_THE_DEPTHS,
				BotaniaItems.BLACK_HOLE_TALISMAN);

		copy(BotaniaTags.Blocks.DYED_MANA_POOLS, BotaniaTags.Items.DYED_MANA_POOLS);
		copy(BotaniaTags.Blocks.DYED_CREATIVE_MANA_POOLS, BotaniaTags.Items.DYED_CREATIVE_MANA_POOLS);
		copy(BotaniaTags.Blocks.DYED_DILUTED_MANA_POOLS, BotaniaTags.Items.DYED_DILUTED_MANA_POOLS);
		copy(BotaniaTags.Blocks.DYED_FABULOUS_MANA_POOLS, BotaniaTags.Items.DYED_FABULOUS_MANA_POOLS);
		copy(BotaniaTags.Blocks.MANA_POOLS, BotaniaTags.Items.MANA_POOLS);
		copy(BotaniaTags.Blocks.CREATIVE_MANA_POOLS, BotaniaTags.Items.CREATIVE_MANA_POOLS);
		copy(BotaniaTags.Blocks.DILUTED_MANA_POOLS, BotaniaTags.Items.DILUTED_MANA_POOLS);
		copy(BotaniaTags.Blocks.FABULOUS_MANA_POOLS, BotaniaTags.Items.FABULOUS_MANA_POOLS);
		copy(BotaniaTags.Blocks.ALL_MANA_POOLS, BotaniaTags.Items.ALL_MANA_POOLS);

		tag(BotaniaTags.Items.MANA_POOL_DYE_REMOVER).add(Items.CLAY_BALL);

		tag(BotaniaTags.Items.DIMS_FLOATING_FLOWERS).add(Items.INK_SAC);
		tag(BotaniaTags.Items.UNDIMS_FLOATING_FLOWERS).add(Items.GLOW_INK_SAC);

		tag(BotaniaTags.Items.IGNORED_BY_ENDOFLAME).add(getItems(
				item -> item instanceof BlockItem blockItem && blockItem.getBlock() instanceof ManaSpreaderBlock));
	}

	private static <B extends Block & OptionallyColored> Item[] getColoredBlockItems(B baseBlock) {
		return ColorHelper.supportedColors().map(color -> BotaniaBlocks.findOptionallyDyedBlock(baseBlock, color))
				.map(Block::asItem).toArray(Item[]::new);
	}

	private static Item[] getItems(Predicate<Item> predicate) {
		Comparator<Item> itemComparator = Comparator.comparing(BuiltInRegistries.ITEM::getKey);
		return BuiltInRegistries.ITEM.stream().filter(predicate).sorted(itemComparator).toArray(Item[]::new);
	}
}
