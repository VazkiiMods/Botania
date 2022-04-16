package vazkii.botania.fabric.data;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.relic.ItemDice;

import static vazkii.botania.common.item.ModItems.*;

public class FabricItemTagProvider extends ItemTagsProvider {
	public static final TagKey<Item> QUARTZ_BLOCKS = itemTag(new ResourceLocation("c", "quartz_blocks"));
	private static final TagKey<Item> MUSHROOMS = itemTag(new ResourceLocation("c", "mushrooms"));
	private static final TagKey<Item> GLASS = itemTag(new ResourceLocation("c", "glass"));
	private static final TagKey<Item> GLASS_ALT = itemTag(new ResourceLocation("c", "glass_blocks"));
	private static final TagKey<Item> GLASS_PANE = itemTag(new ResourceLocation("c", "glass_pane"));
	private static final TagKey<Item> GLASS_PANE_ALT = itemTag(new ResourceLocation("c", "glass_panes"));
	public static final TagKey<Item> WOODEN_CHESTS = itemTag(new ResourceLocation("c", "wooden_chests"));

	private static TagKey<Item> itemTag(ResourceLocation location) {
		return TagKey.create(Registry.ITEM_REGISTRY, location);
	}

	public FabricItemTagProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider) {
		super(dataGenerator, blockTagsProvider);
	}

	@Override
	public String getName() {
		return "Botania item tags (Fabric-specific)";
	}

	@Override
	protected void addTags() {
		for (var color : DyeColor.values()) {
			this.tag(MUSHROOMS).add(ModBlocks.getMushroom(color).asItem());
		}
		this.copy(FabricBlockTagProvider.MUSHROOMS, MUSHROOMS);
		this.copy(FabricBlockTagProvider.QUARTZ_BLOCKS, QUARTZ_BLOCKS);
		this.copy(FabricBlockTagProvider.GLASS, GLASS);
		this.copy(FabricBlockTagProvider.GLASS_ALT, GLASS_ALT);
		this.copy(FabricBlockTagProvider.GLASS_PANE, GLASS_PANE);
		this.copy(FabricBlockTagProvider.GLASS_PANE_ALT, GLASS_PANE_ALT);
		this.tag(WOODEN_CHESTS).add(Items.CHEST, Items.TRAPPED_CHEST);
		generateToolTags();
		generateAccessoryTags();
		generateCompatTags();
	}

	private void generateToolTags() {
		// TODO unhardcode once Fabric API has TagKey constants
		this.tag(fabric("axes")).add(manasteelAxe, elementiumAxe, terraAxe);
		this.tag(fabric("hoes")).add(manasteelHoe, elementiumHoe);
		this.tag(fabric("pickaxes")).add(manasteelPick, elementiumPick, terraPick, glassPick);
		this.tag(fabric("shovels")).add(manasteelShovel, elementiumShovel);
		this.tag(fabric("swords")).add(manasteelSword, elementiumSword, terraSword, thunderSword, starSword);
		this.tag(fabric("shears")).add(manasteelShears, elementiumShears);
	}

	private TagKey<Item> fabric(String name) {
		return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("fabric", name));
	}

	private void generateAccessoryTags() {
		this.tag(accessory("chest/cape")).add(
				balanceCloak,
				holyCloak,
				invisibilityCloak,
				unholyCloak
		);
		this.tag(accessory("chest/necklace")).add(
				bloodPendant,
				cloudPendant,
				divaCharm,
				goddessCharm,
				icePendant,
				lavaPendant,
				superCloudPendant,
				superLavaPendant,
				thirdEye
		);
		Item[] rings = {
				auraRing,
				auraRingGreater,
				dodgeRing,
				lokiRing,
				magnetRing,
				magnetRingGreater,
				manaRing,
				manaRingGreater,
				miningRing,
				odinRing,
				pixieRing,
				reachRing,
				swapRing,
				thorRing,
				waterRing
		};
		this.tag(accessory("hand/ring")).add(rings);
		this.tag(accessory("offhand/ring")).add(rings);
		this.tag(accessory("head/face")).add(
				itemFinder,
				monocle,
				tinyPlanet
		);
		this.tag(accessory("head/hat")).add(
				flightTiara
		);
		this.tag(accessory("legs/belt")).add(
				knockbackBelt,
				speedUpBelt,
				superTravelBelt,
				travelBelt
		);
		this.tag(accessory("all")).add(
				blackBowtie, blackTie,
				redGlasses, puffyScarf,
				engineerGoggles, eyepatch,
				wickedEyepatch, redRibbons,
				pinkFlowerBud, polkaDottedBows,
				blueButterfly, catEars,
				witchPin, devilTail,
				kamuiEye, googlyEyes,
				fourLeafClover, clockEye,
				unicornHorn, devilHorns,
				hyperPlus, botanistEmblem,
				ancientMask, eerieMask,
				alienAntenna, anaglyphGlasses,
				orangeShades, grouchoGlasses,
				thickEyebrows, lusitanicShield,
				tinyPotatoMask, questgiverMark,
				thinkingHand
		);
	}

	private void generateCompatTags() {
		this.tag(itemTag(new ResourceLocation("modern_industrialization", "replicator_blacklist")))
				.add(ItemDice.RELIC_STACKS.get().stream().map(ItemStack::getItem).toArray(Item[]::new))
				.add(dice, manaTablet, manaRing, manaRingGreater, blackerLotus, blackHoleTalisman, flowerBag,
						spawnerMover, terraPick, ModBlocks.terrasteelBlock.asItem());
	}

	private static TagKey<Item> accessory(String name) {
		return itemTag(new ResourceLocation("trinkets", name));
	}
}
