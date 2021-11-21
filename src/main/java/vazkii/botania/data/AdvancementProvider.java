/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import vazkii.botania.common.advancements.*;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.entity.ModEntities;
import vazkii.botania.common.item.ItemLexicon;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.lib.ModTags;
import vazkii.botania.mixin.AccessorAdvancementProvider;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class AdvancementProvider extends net.minecraft.data.advancements.AdvancementProvider {
	public AdvancementProvider(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	public void run(HashCache hashCache) {
		((AccessorAdvancementProvider) this).setTabs(getAdvancements());
		super.run(hashCache);
	}

	protected List<Consumer<Consumer<Advancement>>> getAdvancements() {
		return List.of(AdvancementProvider::mainAdvancements, AdvancementProvider::challengeAdvancements);
	}

	private static void mainAdvancements(Consumer<Advancement> consumer) {
		// Main progression line
		Advancement root = Advancement.Builder.advancement()
				.display(rootDisplay(ModItems.lexicon, "itemGroup.botania",
						"botania.desc", prefix("textures/block/livingwood_log.png")))
				.addCriterion("flower", onPickup(ModTags.Items.MYSTICAL_FLOWERS))
				.save(consumer, mainId("root"));

		Advancement flowerPickup = Advancement.Builder.advancement()
				.display(simple(ModBlocks.pinkFlower, "flowerPickup", FrameType.TASK))
				.parent(root)
				.addCriterion("flower", onPickup(ModTags.Items.MYSTICAL_FLOWERS))
				.save(consumer, mainId("flower_pickup"));

		Advancement manaPoolPickup = Advancement.Builder.advancement()
				.display(simple(ModBlocks.manaPool, "manaPoolPickup", FrameType.TASK))
				.parent(flowerPickup)
				.addCriterion("pickup", onPickup(ModBlocks.manaPool, ModBlocks.creativePool, ModBlocks.dilutedPool, ModBlocks.fabulousPool))
				.save(consumer, mainId("mana_pool_pickup"));

		Advancement runePickup = Advancement.Builder.advancement()
				.display(simple(ModBlocks.runeAltar, "runePickup", FrameType.TASK))
				.parent(manaPoolPickup)
				.addCriterion("rune", onPickup(ModTags.Items.RUNES))
				.save(consumer, mainId("rune_pickup"));

		Advancement terrasteelPickup = Advancement.Builder.advancement()
				.display(simple(ModItems.terrasteel, "terrasteelPickup", FrameType.TASK))
				.parent(runePickup)
				.addCriterion("terrasteel", onPickup(ModItems.terrasteel))
				.save(consumer, mainId("terrasteel_pickup"));

		Advancement elfPortalOpen = Advancement.Builder.advancement()
				.display(simple(ModBlocks.alfPortal, "elfPortalOpen", FrameType.TASK))
				.parent(terrasteelPickup)
				.addCriterion("portal", new AlfPortalTrigger.Instance(EntityPredicate.Composite.ANY, ItemPredicate.ANY, LocationPredicate.ANY))
				.save(consumer, mainId("elf_portal_open"));

		Advancement gaiaGuardianKill = Advancement.Builder.advancement()
				.display(simple(ModBlocks.gaiaHead, "gaiaGuardianKill", FrameType.TASK))
				.parent(elfPortalOpen)
				.addCriterion("guardian", KilledTrigger.TriggerInstance
						.playerKilledEntity(EntityPredicate.Builder.entity().of(ModEntities.DOPPLEGANGER)))
				.save(consumer, mainId("gaia_guardian_kill"));

		Advancement enderAirMake = Advancement.Builder.advancement()
				.display(simple(ModItems.enderAirBottle, "enderAirMake", FrameType.TASK))
				.parent(elfPortalOpen)
				.addCriterion("air", onPickup(ModItems.enderAirBottle))
				.save(consumer, mainId("ender_air_make"));

		// Parent: root
		Advancement.Builder.advancement()
				.display(simple(ModItems.lexicon, "lexiconUse", FrameType.TASK))
				.parent(root)
				.addCriterion("use_lexicon", new UseItemSuccessTrigger.Instance(EntityPredicate.Composite.ANY,
						ItemPredicate.Builder.item().of(ModItems.lexicon).build(), LocationPredicate.ANY))
				.save(consumer, mainId("lexicon_use"));
		Advancement.Builder.advancement()
				.display(simple(ModItems.cacophonium, "cacophoniumCraft", FrameType.TASK))
				.parent(root)
				.addCriterion("cacophonium", onPickup(ModItems.cacophonium))
				.save(consumer, mainId("cacophonium_craft"));

		// Parent: mystical flowers
		Advancement.Builder.advancement()
				.display(simple(ModSubtiles.endoflame, "generatingFlower", FrameType.TASK))
				.parent(flowerPickup)
				.addCriterion("flower", onPickup(ModTags.Items.GENERATING_SPECIAL_FLOWERS))
				.save(consumer, mainId("generating_flower"));

		// Parent: mana pool
		Advancement.Builder.advancement()
				.display(simple(ModBlocks.enchanter, "enchanterMake", FrameType.TASK))
				.parent(manaPoolPickup)
				.addCriterion("code_triggered", new ImpossibleTrigger.TriggerInstance())
				.save(consumer, mainId("enchanter_make"));
		Advancement.Builder.advancement()
				.display(simple(ModSubtiles.bellethorn, "functionalFlower", FrameType.TASK))
				.parent(manaPoolPickup)
				.addCriterion("flower", onPickup(ModTags.Items.FUNCTIONAL_SPECIAL_FLOWERS))
				.save(consumer, mainId("functional_flower"));
		Advancement.Builder.advancement()
				.display(simple(ModItems.poolMinecart, "manaCartCraft", FrameType.TASK))
				.parent(manaPoolPickup)
				.addCriterion("poolcart", onPickup(ModItems.poolMinecart))
				.save(consumer, mainId("mana_cart_craft"));
		Advancement.Builder.advancement()
				.display(simple(ModItems.manaweaveCloth, "manaweaveArmorCraft", FrameType.TASK))
				.parent(manaPoolPickup)
				.addCriterion("head", onPickup(ModItems.manaweaveHelm, ModItems.manaweaveChest, ModItems.manaweaveLegs, ModItems.manaweaveBoots))
				.save(consumer, mainId("manaweave_armor_craft"));
		Advancement.Builder.advancement()
				.display(simple(ModItems.spark, "sparkCraft", FrameType.TASK))
				.parent(manaPoolPickup)
				.addCriterion("spark", onPickup(ModItems.spark))
				.save(consumer, mainId("spark_craft"));
		Advancement.Builder.advancement()
				.display(simple(ModItems.manaCookie, "manaCookieEat", FrameType.TASK))
				.parent(manaPoolPickup)
				.addCriterion("cookie", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.manaCookie))
				.save(consumer, mainId("mana_cookie_eat"));
		Advancement.Builder.advancement()
				.display(simple(ModItems.craftingHalo, "craftingHaloCraft", FrameType.TASK))
				.parent(manaPoolPickup)
				.addCriterion("pool", onPickup(ModItems.craftingHalo))
				.save(consumer, mainId("crafting_halo_craft"));
		Advancement.Builder.advancement()
				.display(simple(ModItems.manaRing, "baubleWear", FrameType.TASK))
				.parent(manaPoolPickup)
				.addCriterion("code_triggered", new ImpossibleTrigger.TriggerInstance())
				.save(consumer, mainId("bauble_wear"));
		Advancement.Builder.advancement()
				.display(simple(ModBlocks.tinyPotato, "tinyPotatoPet", FrameType.TASK))
				.parent(manaPoolPickup)
				.addCriterion("code_triggered", new ImpossibleTrigger.TriggerInstance())
				.save(consumer, mainId("tiny_potato_pet"));

		// Parent: runes
		Advancement.Builder.advancement()
				.display(simple(ModItems.manaGun, "manaBlasterShoot", FrameType.TASK))
				.parent(runePickup)
				.addCriterion("shoot", new ManaGunTrigger.Instance(EntityPredicate.Composite.ANY, ItemPredicate.ANY, EntityPredicate.ANY, null))
				.save(consumer, mainId("mana_blaster_shoot"));
		Advancement.Builder.advancement()
				.display(simple(ModSubtiles.pollidisiac, "pollidisiacPickup", FrameType.TASK))
				.parent(runePickup)
				.addCriterion("pollidisiac", onPickup(ModSubtiles.pollidisiac))
				.save(consumer, mainId("pollidisiac_pickup"));
		Advancement.Builder.advancement()
				.display(simple(ModItems.dirtRod, "dirtRodCraft", FrameType.TASK))
				.parent(runePickup)
				.addCriterion("dirtrod", onPickup(ModItems.dirtRod))
				.save(consumer, mainId("dirt_rod_craft"));
		Advancement.Builder.advancement()
				.display(simple(ModBlocks.brewery, "brewPickup", FrameType.TASK))
				.parent(runePickup)
				.addCriterion("pickup", onPickup(ModItems.brewFlask, ModItems.brewVial))
				.save(consumer, mainId("brew_pickup"));

		// Parent: terrasteel
		Advancement.Builder.advancement()
				.display(simple(ModItems.terraSword, "terrasteelWeaponCraft", FrameType.TASK))
				.parent(terrasteelPickup)
				.addCriterion("terrablade", onPickup(ModItems.terraSword, ModItems.thornChakram))
				.save(consumer, mainId("terrasteel_weapon_craft"));

		// Parent: elven portal
		Advancement.Builder.advancement()
				.display(simple(ModSubtiles.heiseiDream, "heiseiDreamPickup", FrameType.TASK))
				.parent(elfPortalOpen)
				.addCriterion("heisei_dream", onPickup(ModSubtiles.heiseiDream))
				.save(consumer, mainId("heisei_dream_pickup"));
		Advancement.Builder.advancement()
				.display(simple(ModSubtiles.kekimurus, "kekimurusPickup", FrameType.TASK))
				.parent(elfPortalOpen)
				.addCriterion("kekimurus", onPickup(ModSubtiles.kekimurus))
				.save(consumer, mainId("kekimurus_pickup"));
		Advancement.Builder.advancement()
				.display(simple(ModSubtiles.bubbell, "bubbellPickup", FrameType.TASK))
				.parent(elfPortalOpen)
				.addCriterion("bubbell", onPickup(ModSubtiles.bubbell))
				.save(consumer, mainId("bubbell_pickup"));

		// Parent: gaia guardian
		Advancement.Builder.advancement()
				.display(simple(ModItems.spawnerMover, "spawnerMoverUse", FrameType.TASK))
				.parent(gaiaGuardianKill)
				.addCriterion("use_spawner_mover", new UseItemSuccessTrigger.Instance(EntityPredicate.Composite.ANY,
						ItemPredicate.Builder.item().of(ModItems.spawnerMover).build(), LocationPredicate.ANY))
				.save(consumer, mainId("spawner_mover_use"));
		DisplayInfo tiaraWings = simple(ModItems.flightTiara, "tiaraWings", FrameType.TASK);
		tiaraWings.getIcon().getOrCreateTag().putInt("variant", 1);
		InventoryChangeTrigger.TriggerInstance[] variants = IntStream.range(1, ItemFlightTiara.WING_TYPES)
				.mapToObj(i -> {
					CompoundTag tag = new CompoundTag();
					tag.putInt("variant", i);
					return tag;
				})
				.map(nbt -> ItemPredicate.Builder.item().of(ModItems.flightTiara).hasNbt(nbt).build())
				.map(InventoryChangeTrigger.TriggerInstance::hasItems)
				.toArray(InventoryChangeTrigger.TriggerInstance[]::new);
		var builder = Advancement.Builder.advancement()
				.display(tiaraWings)
				.parent(gaiaGuardianKill)
				.requirements(RequirementsStrategy.OR);
		for (int i = 0; i < variants.length; i++) {
			var variant = variants[i];
			builder.addCriterion("tiara_" + (i + 1), variant);
		}
		builder.save(consumer, mainId("tiara_wings"));

		Advancement.Builder.advancement()
				.display(simple(ModSubtiles.dandelifeon, "dandelifeonPickup", FrameType.TASK))
				.parent(gaiaGuardianKill)
				.addCriterion("dandelifeon", onPickup(ModSubtiles.dandelifeon))
				.save(consumer, mainId("dandelifeon_pickup"));
		Advancement.Builder.advancement()
				.display(simple(ModBlocks.manaBomb, "manaBombIgnite", FrameType.TASK))
				.parent(gaiaGuardianKill)
				.addCriterion("bomb", onPickup(ModBlocks.manaBomb))
				.save(consumer, mainId("mana_bomb_ignite"));

		// Parent: ender air
		Advancement.Builder.advancement()
				.display(simple(ModBlocks.lightRelayDefault, "luminizerRide", FrameType.TASK))
				.parent(enderAirMake)
				.addCriterion("code_triggered", new ImpossibleTrigger.TriggerInstance())
				.save(consumer, mainId("luminizer_ride"));
		Advancement.Builder.advancement()
				.display(simple(ModBlocks.corporeaCrystalCube, "corporeaCraft", FrameType.TASK))
				.parent(enderAirMake)
				.addCriterion("pickup", onPickup(ModBlocks.corporeaCrystalCube, ModBlocks.corporeaFunnel,
						ModBlocks.corporeaIndex, ModBlocks.corporeaInterceptor, ModBlocks.corporeaRetainer))
				.save(consumer, mainId("corporea_craft"));

		// Lexicon locks
		Advancement.Builder.advancement()
				.parent(flowerPickup)
				.addCriterion("lexicon", onPickup(ModBlocks.defaultAltar))
				.save(consumer, mainId("apothecary_pickup"));
		Advancement.Builder.advancement()
				.parent(flowerPickup)
				.addCriterion("lexicon", onPickup(ModSubtiles.pureDaisy))
				.save(consumer, mainId("pure_daisy_pickup"));
		Advancement.Builder.advancement()
				.parent(flowerPickup)
				.addCriterion("lexicon", onPickup(ModBlocks.runeAltar))
				.save(consumer, mainId("runic_altar_pickup"));

		var elvenLexiconUnlock = new CompoundTag();
		elvenLexiconUnlock.putBoolean(ItemLexicon.TAG_ELVEN_UNLOCK, true);

		Advancement.Builder.advancement()
				.parent(elfPortalOpen)
				.addCriterion("lexicon", InventoryChangeTrigger.TriggerInstance.hasItems(
						ItemPredicate.Builder.item().of(ModItems.lexicon).hasNbt(elvenLexiconUnlock).build()
				))
				.save(consumer, mainId("elf_lexicon_pickup"));
	}

	private static void challengeAdvancements(Consumer<Advancement> consumer) {
		Advancement root = Advancement.Builder.advancement()
				.display(rootDisplay(ModItems.dice, "advancement.botania_challenge",
						"advancement.botania_challenge.desc", prefix("textures/block/livingrock_bricks.png")))
				.addCriterion("flower", onPickup(ModTags.Items.MYSTICAL_FLOWERS))
				.save(consumer, challengeId("root"));

		// hardmode Gaia Guardian related
		CompoundTag hardmodeNbt = new CompoundTag();
		hardmodeNbt.putBoolean("hardMode", true);
		Advancement hardMode = Advancement.Builder.advancement()
				.display(simple(ModItems.lifeEssence, "gaiaGuardianHardmode", FrameType.CHALLENGE))
				.parent(root)
				.addCriterion("guardian", KilledTrigger.TriggerInstance.playerKilledEntity(
						EntityPredicate.Builder.entity()
								.of(ModEntities.DOPPLEGANGER)
								.nbt(new NbtPredicate(hardmodeNbt)).build()))
				.save(consumer, challengeId("gaia_guardian_hardmode"));

		relicBindAdvancement(consumer, hardMode, ModItems.infiniteFruit, "infiniteFruit", "fruit");
		relicBindAdvancement(consumer, hardMode, ModItems.kingKey, "kingKey", "key");
		relicBindAdvancement(consumer, hardMode, ModItems.flugelEye, "flugelEye", "eye");
		relicBindAdvancement(consumer, hardMode, ModItems.thorRing, "thorRing", "ring");
		relicBindAdvancement(consumer, hardMode, ModItems.odinRing, "odinRing", "ring");
		Advancement lokiRing = relicBindAdvancement(consumer, hardMode, ModItems.lokiRing, "lokiRing", "ring");

		Advancement.Builder.advancement()
				.display(simple(ModItems.lokiRing, "lokiRingMany", FrameType.CHALLENGE))
				.parent(lokiRing)
				.addCriterion("place_blocks", new LokiPlaceTrigger.Instance(
						EntityPredicate.Composite.ANY, EntityPredicate.ANY, ItemPredicate.ANY, MinMaxBounds.Ints.atLeast(255)
				))
				.save(consumer, challengeId("loki_ring_many"));
		Advancement.Builder.advancement()
				.display(simple(ModItems.pinkinator, "pinkinator", FrameType.CHALLENGE))
				.parent(hardMode)
				.addCriterion("use_pinkinator", new UseItemSuccessTrigger.Instance(
						EntityPredicate.Composite.ANY, matchItems(ModItems.pinkinator), LocationPredicate.ANY))
				.save(consumer, challengeId("pinkinator"));

		// Misc challenges
		Advancement.Builder.advancement()
				.display(simple(Blocks.PLAYER_HEAD, "gaiaGuardianNoArmor", FrameType.CHALLENGE))
				.parent(root)
				.addCriterion("no_armor", new DopplegangerNoArmorTrigger.Instance(
						EntityPredicate.Composite.ANY, EntityPredicate.ANY, DamageSourcePredicate.ANY))
				.save(consumer, challengeId("gaia_guardian_no_armor"));
		Advancement.Builder.advancement()
				.display(hidden(ModBlocks.motifDaybloom, "old_flower_pickup", FrameType.CHALLENGE))
				.parent(root)
				.addCriterion("flower", onPickup(ModBlocks.motifDaybloom, ModBlocks.motifNightshade))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, challengeId("old_flower_pickup"));
		DisplayInfo desuGun = simple(ModItems.manaGun, "desuGun", FrameType.CHALLENGE);
		desuGun.getIcon().setHoverName(new TextComponent("desu gun"));
		Advancement.Builder.advancement()
				.display(desuGun)
				.parent(root)
				.addCriterion("use_gun", new ManaGunTrigger.Instance(
						EntityPredicate.Composite.ANY, ItemPredicate.ANY, EntityPredicate.ANY, true))
				.save(consumer, challengeId("desu_gun"));
		Advancement.Builder.advancement()
				.display(simple(ModBlocks.corporeaIndex, "superCorporeaRequest", FrameType.CHALLENGE))
				.parent(root)
				.addCriterion("over_fifty_thousand", new CorporeaRequestTrigger.Instance(
						EntityPredicate.Composite.ANY, MinMaxBounds.Ints.atLeast(50000), LocationPredicate.ANY))
				.save(consumer, challengeId("super_corporea_request"));
		Advancement.Builder.advancement()
				.display(simple(ModItems.terraPick, "rankSSPick", FrameType.CHALLENGE))
				.parent(root)
				.addCriterion("code_triggered", new ImpossibleTrigger.TriggerInstance())
				.save(consumer, challengeId("rank_ss_pick"));
		CompoundTag level20Shard = new CompoundTag();
		level20Shard.putInt("level", 19);
		Advancement.Builder.advancement()
				.display(simple(ModItems.laputaShard, "l20ShardUse", FrameType.CHALLENGE))
				.parent(root)
				.addCriterion("use_l20_shard", InventoryChangeTrigger.TriggerInstance.hasItems(
						ItemPredicate.Builder.item().of(ModItems.laputaShard).hasNbt(level20Shard).build()))
				.save(consumer, challengeId("l20_shard_use"));
		Advancement.Builder.advancement()
				.display(hidden(Items.BREAD, "alfPortalBread", FrameType.CHALLENGE))
				.parent(root)
				.addCriterion("bread", new AlfPortalBreadTrigger.Instance(EntityPredicate.Composite.ANY, LocationPredicate.ANY))
				.save(consumer, challengeId("alf_portal_bread"));

	}

	private static Advancement relicBindAdvancement(Consumer<Advancement> consumer, Advancement parent, Item relicItem,
			String titleKey, String criterionName) {
		String id = challengeId(Registry.ITEM.getKey(relicItem).getPath());
		return Advancement.Builder.advancement()
				.display(simple(relicItem, titleKey, FrameType.CHALLENGE))
				.parent(parent)
				.addCriterion(criterionName, new RelicBindTrigger.Instance(EntityPredicate.Composite.ANY,
						ItemPredicate.Builder.item().of(relicItem).build()))
				.save(consumer, id);
	}

	protected static InventoryChangeTrigger.TriggerInstance onPickup(Tag<Item> tag) {
		return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build());
	}

	protected static InventoryChangeTrigger.TriggerInstance onPickup(ItemLike... items) {
		return InventoryChangeTrigger.TriggerInstance.hasItems(matchItems(items));
	}

	protected static ItemPredicate matchItems(ItemLike... items) {
		return ItemPredicate.Builder.item().of(items).build();
	}

	protected static DisplayInfo simple(ItemLike icon, String name, FrameType frameType) {
		String expandedName = "advancement.botania:" + name;
		return new DisplayInfo(new ItemStack(icon.asItem()),
				new TranslatableComponent(expandedName),
				new TranslatableComponent(expandedName + ".desc"),
				null, frameType, true, true, false);
	}

	protected static DisplayInfo hidden(ItemLike icon, String name, FrameType frameType) {
		String expandedName = "advancement.botania:" + name;
		return new DisplayInfo(new ItemStack(icon.asItem()),
				new TranslatableComponent(expandedName),
				new TranslatableComponent(expandedName + ".desc"),
				null, frameType, true, true, true);
	}

	protected static DisplayInfo rootDisplay(ItemLike icon, String titleKey, String descKey, ResourceLocation background) {
		return new DisplayInfo(new ItemStack(icon.asItem()),
				new TranslatableComponent(titleKey),
				new TranslatableComponent(descKey),
				background, FrameType.TASK, false, false, false);
	}

	private static String mainId(String name) {
		return prefix("main/" + name).toString();
	}

	private static String challengeId(String name) {
		return prefix("challenge/" + name).toString();
	}

	@Override
	public String getName() {
		return "Botania Advancements";
	}
}
