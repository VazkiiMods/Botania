package vazkii.botania.forge.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import vazkii.botania.common.lib.LibMisc;

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
		this.copy(ForgeBlockTagProvider.MUSHROOMS, Tags.Items.MUSHROOMS);
		this.copy(Tags.Blocks.STORAGE_BLOCKS_QUARTZ, Tags.Items.STORAGE_BLOCKS_QUARTZ);
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
}
