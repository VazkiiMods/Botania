package vazkii.botania.forge.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.ModTags;

import static vazkii.botania.common.item.ModItems.*;

public class ForgeItemTagProvider extends ItemTagsProvider {
	public ForgeItemTagProvider(DataGenerator generator, BlockTagsProvider blockTags, ExistingFileHelper helper) {
		super(generator, blockTags, LibMisc.MOD_ID, helper);
	}

	@Override
	public String getName() {
		return "Botania item tags (Forge-specific)";
	}

	@Override
	protected void addTags() {
		this.tag(Tags.Items.SHEARS).add(manasteelShears, elementiumShears);

		this.tag(forge("dusts/mana")).addTag(ModTags.Items.DUSTS_MANA);
		this.tag(forge("dusts")).addTag(forge("dusts/mana"));

		this.tag(forge("gems/dragonstone")).addTag(ModTags.Items.GEMS_DRAGONSTONE);
		this.tag(forge("gems/mana_diamond")).addTag(ModTags.Items.GEMS_MANA_DIAMOND);
		this.tag(forge("gems")).addTag(forge("gems/dragonstone")).addTag(forge("gems/mana_diamond"));

		this.tag(forge("ingots/elementium")).addTag(ModTags.Items.INGOTS_ELEMENTIUM);
		this.tag(forge("ingots/manasteel")).addTag(ModTags.Items.INGOTS_MANASTEEL);
		this.tag(forge("ingots/terrasteel")).addTag(ModTags.Items.INGOTS_TERRASTEEL);
		this.tag(forge("ingots")).addTag(forge("ingots/elementium"))
				.addTag(forge("ingots/manasteel"))
				.addTag(forge("ingots/terrasteel"));

		this.tag(forge("nuggets/elementium")).addTag(ModTags.Items.NUGGETS_ELEMENTIUM);
		this.tag(forge("nuggets/manasteel")).addTag(ModTags.Items.NUGGETS_MANASTEEL);
		this.tag(forge("nuggets/terrasteel")).addTag(ModTags.Items.NUGGETS_TERRASTEEL);
		this.tag(forge("nuggets")).addTag(forge("nuggets/elementium"))
				.addTag(forge("nuggets/manasteel"))
				.addTag(forge("nuggets/terrasteel"));

		this.copyToSameName(ForgeBlockTagProvider.ELEMENTIUM);
		this.copyToSameName(ForgeBlockTagProvider.MANASTEEL);
		this.copyToSameName(ForgeBlockTagProvider.TERRASTEEL);
		this.copy(ForgeBlockTagProvider.MUSHROOMS, Tags.Items.MUSHROOMS);
		this.copy(Tags.Blocks.STORAGE_BLOCKS_QUARTZ, Tags.Items.STORAGE_BLOCKS_QUARTZ);
		this.copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
		this.copy(Tags.Blocks.GLASS, Tags.Items.GLASS);
		this.copy(Tags.Blocks.GLASS_PANES, Tags.Items.GLASS_PANES);

		generateAccessoryTags();
	}

	private void generateAccessoryTags() {
		tag(accessory("belt")).add(
				knockbackBelt, speedUpBelt, superTravelBelt, travelBelt
		);
		tag(accessory("body")).add(
				balanceCloak, holyCloak, invisibilityCloak, thirdEye, unholyCloak
		);
		tag(accessory("charm")).add(
				divaCharm, goddessCharm, monocle, tinyPlanet
		);
		tag(accessory("head")).add(flightTiara, itemFinder);
		tag(accessory("necklace")).add(
				bloodPendant, cloudPendant, icePendant, lavaPendant,
				superCloudPendant, superLavaPendant
		);
		tag(accessory("ring")).add(
				auraRing, auraRingGreater, dodgeRing, lokiRing, magnetRing, magnetRingGreater,
				manaRing, manaRingGreater, miningRing, odinRing, pixieRing, reachRing,
				swapRing, thorRing, waterRing
		);
		tag(accessory("curio")).add(
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

	private static Tag.Named<Item> accessory(String name) {
		return ItemTags.createOptional(new ResourceLocation("curios", name));
	}

	private static Tag.Named<Item> forge(String name) {
		return ItemTags.createOptional(new ResourceLocation("forge", name));
	}

	private void copyToSameName(Tag.Named<Block> source) {
		this.copy(source, ItemTags.createOptional(source.getName()));
	}
}
