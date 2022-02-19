package vazkii.botania.fabric.data;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.relic.ItemDice;
import vazkii.botania.xplat.IXplatAbstractions;

import static vazkii.botania.common.item.ModItems.*;

public class FabricItemTagProvider extends ItemTagsProvider {
	public static final Tag.Named<Item> QUARTZ_BLOCKS = IXplatAbstractions.INSTANCE.itemTag(new ResourceLocation("c", "quartz_blocks"));
	private static final Tag.Named<Item> MUSHROOMS = IXplatAbstractions.INSTANCE.itemTag(new ResourceLocation("c", "mushrooms"));
	private static final Tag.Named<Item> GLASS = IXplatAbstractions.INSTANCE.itemTag(new ResourceLocation("c", "glass"));
	private static final Tag.Named<Item> GLASS_ALT = IXplatAbstractions.INSTANCE.itemTag(new ResourceLocation("c", "glass_blocks"));
	private static final Tag.Named<Item> GLASS_PANE = IXplatAbstractions.INSTANCE.itemTag(new ResourceLocation("c", "glass_pane"));
	private static final Tag.Named<Item> GLASS_PANE_ALT = IXplatAbstractions.INSTANCE.itemTag(new ResourceLocation("c", "glass_panes"));

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
		generateToolTags();
		generateAccessoryTags();
		generateCompatTags();
	}

	private void generateToolTags() {
		this.tag((Tag.Named<Item>) FabricToolTags.AXES).add(manasteelAxe, elementiumAxe, terraAxe);
		this.tag((Tag.Named<Item>) FabricToolTags.HOES).add(manasteelHoe, elementiumHoe);
		this.tag((Tag.Named<Item>) FabricToolTags.PICKAXES).add(manasteelPick, elementiumPick, terraPick, glassPick);
		this.tag((Tag.Named<Item>) FabricToolTags.SHOVELS).add(manasteelShovel, elementiumShovel);
		this.tag((Tag.Named<Item>) FabricToolTags.SWORDS).add(manasteelSword, elementiumSword, terraSword, thunderSword, starSword);
		this.tag((Tag.Named<Item>) FabricToolTags.SHEARS).add(manasteelShears, elementiumShears);
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
		this.tag(accessory("hand/ring")).add(
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
		);
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
		this.tag(TagFactory.ITEM.create(new ResourceLocation("modern_industrialization", "replicator_blacklist")))
				.add(ItemDice.getRelics())
				.add(dice, manaTablet, manaRing, manaRingGreater, blackerLotus, blackHoleTalisman, flowerBag,
						spawnerMover, terraPick, ModBlocks.terrasteelBlock.asItem());
	}

	private static Tag.Named<Item> accessory(String name) {
		return TagFactory.ITEM.create(new ResourceLocation("trinkets", name));
	}
}
