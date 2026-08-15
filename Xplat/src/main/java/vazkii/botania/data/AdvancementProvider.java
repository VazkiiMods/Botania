/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import vazkii.botania.common.advancements.*;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.corporea.CorporeaIndexBlockEntity;
import vazkii.botania.common.block.block_entity.flower.functional.LooniumBlockEntity;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.entity.BotaniaEntities;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.LaputaShardItem;
import vazkii.botania.common.item.equipment.bauble.FlugelTiaraItem;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class AdvancementProvider {
	public static net.minecraft.data.advancements.AdvancementProvider create(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		return new net.minecraft.data.advancements.AdvancementProvider(packOutput, lookupProvider, List.of(new BotaniaStoryAdvancements(), new BotaniaChallengeAdvancements()));
	}

	public static class BotaniaStoryAdvancements implements AdvancementSubProvider {

		@Override
		public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> writer) {
			Criterion<InventoryChangeTrigger.TriggerInstance> elvenLexicon = InventoryChangeTrigger.TriggerInstance.hasItems(
					ItemPredicate.Builder.item().of(BotaniaItems.LEXICA_BOTANIA)
							.hasComponents(DataComponentPredicate.builder()
									.expect(BotaniaDataComponents.ELVEN_UNLOCK, Unit.INSTANCE).build())
							.build()
			);

			// Main progression line
			AdvancementHolder root = Advancement.Builder.advancement()
					.display(rootDisplay(BotaniaItems.LEXICA_BOTANIA, "itemGroup.botania",
							"botania.desc", botaniaRL("textures/block/livingwood_log.png")))
					.addCriterion("flower", onPickup(BotaniaTags.Items.SMALL_MYSTICAL_FLOWERS))
					.save(writer, mainId("root"));

			AdvancementHolder flowerPickup = Advancement.Builder.advancement()
					.display(simple(BotaniaBlocks.PINK_MYSTICAL_FLOWER, "flowerPickup", AdvancementType.TASK))
					.parent(root)
					.addCriterion("flower", onPickup(BotaniaTags.Items.SMALL_MYSTICAL_FLOWERS))
					.addCriterion("double_flower", onPickup(BotaniaTags.Items.TALL_MYSTICAL_FLOWERS))
					.requirements(AdvancementRequirements.Strategy.OR)
					.save(writer, mainId("flower_pickup"));

			AdvancementHolder manaPoolPickup = Advancement.Builder.advancement()
					.display(simple(BotaniaBlocks.MANA_POOL, "manaPoolPickup", AdvancementType.TASK))
					.parent(flowerPickup)
					.addCriterion("pickup", onPickup(BotaniaTags.Items.ALL_MANA_POOLS))
					.save(writer, mainId("mana_pool_pickup"));

			AdvancementHolder runePickup = Advancement.Builder.advancement()
					.display(simple(BotaniaBlocks.RUNIC_ALTAR, "runePickup", AdvancementType.TASK))
					.parent(manaPoolPickup)
					.addCriterion("rune", onPickup(BotaniaTags.Items.RUNES))
					.save(writer, mainId("rune_pickup"));

			AdvancementHolder terrasteelPickup = Advancement.Builder.advancement()
					.display(simple(BotaniaItems.TERRASTEEL_INGOT, "terrasteelPickup", AdvancementType.TASK))
					.parent(runePickup)
					.addCriterion("terrasteel", onPickup(BotaniaItems.TERRASTEEL_INGOT))
					.save(writer, mainId("terrasteel_pickup"));

			AdvancementHolder elfPortalOpen = Advancement.Builder.advancement()
					.display(simple(BotaniaBlocks.ELVEN_GATEWAY_CORE, "elfPortalOpen", AdvancementType.TASK))
					.parent(terrasteelPickup)
					.addCriterion("portal", AlfheimPortalTrigger.Instance.activatedPortal())
					.save(writer, mainId("elf_portal_open"));

			AdvancementHolder gaiaGuardianKill = Advancement.Builder.advancement()
					.display(simple(BotaniaBlocks.GAIA_HEAD, "gaiaGuardianKill", AdvancementType.TASK))
					.parent(elfPortalOpen)
					.addCriterion("guardian", KilledTrigger.TriggerInstance
							.playerKilledEntity(EntityPredicate.Builder.entity().of(BotaniaEntities.GAIA_GUARDIAN)))
					.save(writer, mainId("gaia_guardian_kill"));

			AdvancementHolder enderEssenceMake = Advancement.Builder.advancement()
					.display(simple(BotaniaItems.PURE_ENDER_ESSENCE, "enderEssenceMake", AdvancementType.TASK))
					.parent(elfPortalOpen)
					.addCriterion("essence", onPickup(BotaniaTags.Items.ENDER_ESSENCES))
					.save(writer, mainId("ender_essence_make"));

			// Parent: root
			Advancement.Builder.advancement()
					.display(simple(BotaniaItems.LEXICA_BOTANIA, "lexiconUse", AdvancementType.TASK))
					.parent(root)
					.addCriterion("use_lexicon", UseItemSuccessTrigger.Instance.used(BotaniaItems.LEXICA_BOTANIA))
					.save(writer, mainId("lexicon_use"));
			Advancement.Builder.advancement()
					.display(simple(BotaniaItems.CACOPHONIUM, "cacophoniumCraft", AdvancementType.TASK))
					.parent(root)
					.addCriterion("cacophonium", onPickup(BotaniaItems.CACOPHONIUM))
					.save(writer, mainId("cacophonium_craft"));

			// Parent: mystical flowers
			Advancement.Builder.advancement()
					.display(simple(BotaniaBlocks.ENDOFLAME, "generatingFlower", AdvancementType.TASK))
					.parent(flowerPickup)
					.addCriterion("flower", onPickup(BotaniaTags.Items.GENERATING_SPECIAL_FLOWERS))
					.save(writer, mainId("generating_flower"));

			// Parent: mana pool
			Advancement.Builder.advancement()
					.display(simple(BotaniaBlocks.MANA_ENCHANTER, "enchanterMake", AdvancementType.TASK))
					.parent(manaPoolPickup)
					.addCriterion("code_triggered",
							CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
					.save(writer, mainId("enchanter_make"));
			Advancement.Builder.advancement()
					.display(simple(BotaniaBlocks.BELLETHORNE, "functionalFlower", AdvancementType.TASK))
					.parent(manaPoolPickup)
					.addCriterion("flower", onPickup(BotaniaTags.Items.FUNCTIONAL_SPECIAL_FLOWERS))
					.save(writer, mainId("functional_flower"));
			Advancement.Builder.advancement()
					.display(simple(BotaniaItems.MANA_POOL_MINECART, "manaCartCraft", AdvancementType.TASK))
					.parent(manaPoolPickup)
					.addCriterion("poolcart", onPickup(BotaniaItems.MANA_POOL_MINECART))
					.save(writer, mainId("mana_cart_craft"));
			Advancement.Builder.advancement()
					.display(simple(BotaniaItems.MANAWEAVE_CLOTH, "manaweaveArmorCraft", AdvancementType.TASK))
					.parent(manaPoolPickup)
					.addCriterion("head", onPickup(
							BotaniaItems.MANAWEAVE_HELMET, BotaniaItems.MANAWEAVE_CHESTPLATE,
							BotaniaItems.MANAWEAVE_LEGGINGS, BotaniaItems.MANAWEAVE_BOOTS))
					.save(writer, mainId("manaweave_armor_craft"));
			Advancement.Builder.advancement()
					.display(simple(BotaniaItems.SPARK, "sparkCraft", AdvancementType.TASK))
					.parent(manaPoolPickup)
					.addCriterion("spark", onPickup(BotaniaItems.SPARK))
					.save(writer, mainId("spark_craft"));
			Advancement.Builder.advancement()
					.display(simple(BotaniaItems.BISCUIT_OF_TOTALITY, "manaCookieEat", AdvancementType.TASK))
					.parent(manaPoolPickup)
					.addCriterion("cookie",
							ConsumeItemTrigger.TriggerInstance.usedItem(BotaniaItems.BISCUIT_OF_TOTALITY))
					.save(writer, mainId("biscuit_of_totality_eat"));
			Advancement.Builder.advancement()
					.display(simple(BotaniaItems.ASSEMBLY_HALO, "craftingHaloCraft", AdvancementType.TASK))
					.parent(manaPoolPickup)
					.addCriterion("pool", onPickup(BotaniaItems.ASSEMBLY_HALO))
					.save(writer, mainId("assembly_halo_craft"));
			Advancement.Builder.advancement()
					.display(simple(BotaniaItems.BAND_OF_MANA, "baubleWear", AdvancementType.TASK))
					.parent(manaPoolPickup)
					.addCriterion("code_triggered",
							CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
					.save(writer, mainId("bauble_wear"));
			Advancement.Builder.advancement()
					.display(simple(BotaniaBlocks.TINY_POTATO, "tinyPotatoPet", AdvancementType.TASK))
					.parent(manaPoolPickup)
					.addCriterion("code_triggered",
							CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
					.save(writer, mainId("tiny_potato_pet"));

			// Parent: runes
			Advancement.Builder.advancement()
					.display(simple(BotaniaItems.MANA_BLASTER, "manaBlasterShoot", AdvancementType.TASK))
					.parent(runePickup)
					.addCriterion("shoot", ManaBlasterTrigger.Instance.shoot())
					.save(writer, mainId("mana_blaster_shoot"));
			Advancement.Builder.advancement()
					.display(simple(BotaniaBlocks.POLLIDISIAC, "pollidisiacPickup", AdvancementType.TASK))
					.parent(runePickup)
					.addCriterion("pollidisiac", onPickup(BotaniaBlocks.POLLIDISIAC))
					.save(writer, mainId("pollidisiac_pickup"));
			Advancement.Builder.advancement()
					.display(simple(BotaniaItems.ROD_OF_THE_LANDS, "dirtRodCraft", AdvancementType.TASK))
					.parent(runePickup)
					.addCriterion("dirtrod", onPickup(BotaniaItems.ROD_OF_THE_LANDS))
					.save(writer, mainId("rod_of_the_lands_craft"));
			Advancement.Builder.advancement()
					.display(simple(BotaniaBlocks.BOTANICAL_BREWERY, "brewPickup", AdvancementType.TASK))
					.parent(runePickup)
					.addCriterion("pickup", onPickup(BotaniaItems.BREW_FLASK, BotaniaItems.BREW_VIAL))
					.save(writer, mainId("brew_pickup"));

			// Parent: terrasteel
			Advancement.Builder.advancement()
					.display(simple(BotaniaItems.TERRA_BLADE, "terrasteelWeaponCraft", AdvancementType.TASK))
					.parent(terrasteelPickup)
					.addCriterion("terrablade", onPickup(BotaniaItems.TERRA_BLADE, BotaniaItems.THORN_CHAKRAM))
					.save(writer, mainId("terrasteel_weapon_craft"));

			// Parent: elven portal
			Advancement.Builder.advancement()
					.display(simple(BotaniaBlocks.HEISEI_DREAM, "heiseiDreamPickup", AdvancementType.TASK))
					.parent(elfPortalOpen)
					.addCriterion("heisei_dream", onPickup(BotaniaBlocks.HEISEI_DREAM))
					.save(writer, mainId("heisei_dream_pickup"));
			Advancement.Builder.advancement()
					.display(simple(BotaniaBlocks.KEKIMURUS, "kekimurusPickup", AdvancementType.TASK))
					.parent(elfPortalOpen)
					.addCriterion("kekimurus", onPickup(BotaniaBlocks.KEKIMURUS))
					.save(writer, mainId("kekimurus_pickup"));
			Advancement.Builder.advancement()
					.display(simple(BotaniaBlocks.BUBBELL, "bubbellPickup", AdvancementType.TASK))
					.parent(elfPortalOpen)
					.addCriterion("bubbell", onPickup(BotaniaBlocks.BUBBELL))
					.save(writer, mainId("bubbell_pickup"));

			// Parent: gaia guardian
			Advancement.Builder.advancement()
					.display(simple(BotaniaItems.LIFE_AGGREGATOR, "spawnerMoverUse", AdvancementType.TASK))
					.parent(gaiaGuardianKill)
					.addCriterion("use_life_aggregator",
							UseItemSuccessTrigger.Instance.used(BotaniaItems.LIFE_AGGREGATOR))
					.save(writer, mainId("life_aggregator_use"));
			DisplayInfo tiaraWings = simple(BotaniaItems.FLUGEL_TIARA, "tiaraWings", AdvancementType.TASK);
			tiaraWings.getIcon().set(BotaniaDataComponents.TIARA_VARIANT, 1);
			Criterion<?>[] variants = IntStream.range(1, FlugelTiaraItem.WING_TYPES)
					.mapToObj(i -> ItemPredicate.Builder.item().of(BotaniaItems.FLUGEL_TIARA)
							.hasComponents(DataComponentPredicate.builder()
									.expect(BotaniaDataComponents.TIARA_VARIANT, i).build())
							.build())
					.map(InventoryChangeTrigger.TriggerInstance::hasItems)
					.toArray(Criterion<?>[]::new);
			var builder = Advancement.Builder.advancement()
					.display(tiaraWings)
					.parent(gaiaGuardianKill)
					.requirements(AdvancementRequirements.Strategy.OR);
			for (int i = 0; i < variants.length; i++) {
				var variant = variants[i];
				builder.addCriterion("tiara_" + (i + 1), variant);
			}
			builder.save(writer, mainId("tiara_wings"));

			Advancement.Builder.advancement()
					.display(simple(BotaniaBlocks.DANDELIFEON, "dandelifeonPickup", AdvancementType.TASK))
					.parent(gaiaGuardianKill)
					.addCriterion("dandelifeon", onPickup(BotaniaBlocks.DANDELIFEON))
					.save(writer, mainId("dandelifeon_pickup"));
			Advancement.Builder.advancement()
					.display(simple(BotaniaBlocks.MANASTORM_CHARGE, "manaBombIgnite", AdvancementType.TASK))
					.parent(gaiaGuardianKill)
					.addCriterion("bomb", onPickup(BotaniaBlocks.MANASTORM_CHARGE))
					.save(writer, mainId("manastorm_charge_ignite"));

			// Parent: ender air
			Advancement.Builder.advancement()
					.display(simple(BotaniaBlocks.LUMINIZER, "luminizerRide", AdvancementType.TASK))
					.parent(enderEssenceMake)
					.addCriterion("code_triggered",
							CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
					.save(writer, mainId("luminizer_ride"));
			Advancement.Builder.advancement()
					.display(simple(BotaniaBlocks.CORPOREA_CRYSTAL_CUBE, "corporeaCraft", AdvancementType.TASK))
					.parent(enderEssenceMake)
					.addCriterion("pickup", onPickup(
							BotaniaBlocks.CORPOREA_CRYSTAL_CUBE, BotaniaBlocks.CORPOREA_FUNNEL,
							BotaniaBlocks.CORPOREA_INDEX, BotaniaBlocks.CORPOREA_INTERCEPTOR,
							BotaniaBlocks.CORPOREA_RETAINER
					))
					.save(writer, mainId("corporea_craft"));

			// Lexicon locks
			Advancement.Builder.advancement()
					.parent(root)
					.addCriterion("flower", onPickup(BotaniaTags.Items.SMALL_MYSTICAL_FLOWERS))
					.addCriterion("double_flower", onPickup(BotaniaTags.Items.TALL_MYSTICAL_FLOWERS))
					.addCriterion("elven_lexicon", elvenLexicon)
					.requirements(AdvancementRequirements.Strategy.OR)
					.save(writer, mainId("flower_pickup_lexicon"));
			Advancement.Builder.advancement()
					.parent(flowerPickup)
					.addCriterion("apothecary", onPickup(BotaniaTags.Items.PETAL_APOTHECARIES))
					.addCriterion("elven_lexicon", elvenLexicon)
					.requirements(AdvancementRequirements.Strategy.OR)
					.save(writer, mainId("apothecary_pickup"));
			Advancement.Builder.advancement()
					.parent(flowerPickup)
					.addCriterion("daisy", onPickup(BotaniaBlocks.PURE_DAISY))
					.addCriterion("elven_lexicon", elvenLexicon)
					.requirements(AdvancementRequirements.Strategy.OR)
					.save(writer, mainId("pure_daisy_pickup"));
			Advancement.Builder.advancement()
					.parent(root)
					.addCriterion("pickup", onPickup(BotaniaTags.Items.ALL_MANA_POOLS))
					.addCriterion("elven_lexicon", elvenLexicon)
					.requirements(AdvancementRequirements.Strategy.OR)
					.save(writer, mainId("mana_pool_pickup_lexicon"));
			Advancement.Builder.advancement()
					.parent(flowerPickup)
					.addCriterion("altar", onPickup(BotaniaBlocks.RUNIC_ALTAR))
					.addCriterion("rune", onPickup(BotaniaTags.Items.RUNES))
					.addCriterion("elven_lexicon", elvenLexicon)
					.requirements(AdvancementRequirements.Strategy.OR)
					.save(writer, mainId("runic_altar_pickup"));
			Advancement.Builder.advancement()
					.parent(flowerPickup)
					.addCriterion("terrasteel", onPickup(BotaniaItems.TERRASTEEL_INGOT))
					.addCriterion("elven_lexicon", elvenLexicon)
					.requirements(AdvancementRequirements.Strategy.OR)
					.save(writer, mainId("terrasteel_pickup_lexicon"));
			Advancement.Builder.advancement()
					.parent(elfPortalOpen)
					.addCriterion("lexicon", elvenLexicon)
					.save(writer, mainId("elf_lexicon_pickup"));
			Advancement.Builder.advancement()
					.parent(root)
					.addCriterion("code_triggered",
							CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
					.save(writer, mainId("dog"));
		}
	}

	public static class BotaniaChallengeAdvancements implements AdvancementSubProvider {

		private static final EntityType<?>[] LOONIUM_MOBS_TO_KILL = {
				EntityType.BLAZE,
				EntityType.CAVE_SPIDER,
				EntityType.CREEPER,
				EntityType.DROWNED,
				EntityType.ENDERMAN,
				EntityType.EVOKER,
				EntityType.GUARDIAN,
				EntityType.HOGLIN,
				EntityType.HUSK,
				EntityType.PIGLIN,
				EntityType.PIGLIN_BRUTE,
				EntityType.PILLAGER,
				EntityType.SHULKER,
				EntityType.SILVERFISH,
				EntityType.SKELETON,
				EntityType.STRAY,
				EntityType.VINDICATOR,
				EntityType.WITHER_SKELETON,
				EntityType.ZOGLIN,
				EntityType.ZOMBIE_VILLAGER,
				EntityType.ZOMBIE,
				EntityType.ZOMBIFIED_PIGLIN
		};

		@Override
		public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> writer) {
			AdvancementHolder root = Advancement.Builder.advancement()
					.display(rootDisplay(BotaniaItems.DICE_OF_FATE,
							"advancement.botania_challenge",
							"advancement.botania_challenge.desc",
							botaniaRL("textures/block/livingrock_bricks.png")))
					.addCriterion("flower", onPickup(BotaniaTags.Items.SMALL_MYSTICAL_FLOWERS))
					.save(writer, challengeId("root"));

			// hardmode Gaia Guardian related
			CompoundTag hardmodeNbt = new CompoundTag();
			hardmodeNbt.putBoolean("hardMode", true);
			AdvancementHolder hardMode = Advancement.Builder.advancement()
					.display(simple(BotaniaItems.GAIA_SPIRIT, "gaiaGuardianHardmode", AdvancementType.CHALLENGE))
					.parent(root)
					.rewards(AdvancementRewards.Builder.experience(100))
					.addCriterion("guardian", KilledTrigger.TriggerInstance.playerKilledEntity(
							EntityPredicate.Builder.entity()
									.of(BotaniaEntities.GAIA_GUARDIAN)
									.nbt(new NbtPredicate(hardmodeNbt))))
					.save(writer, challengeId("gaia_guardian_hardmode"));

			relicBindAdvancement(writer, hardMode, BotaniaItems.FRUIT_OF_GRISAIA, "infiniteFruit", "fruit");
			relicBindAdvancement(writer, hardMode, BotaniaItems.KEY_OF_THE_KINGS_LAW, "kingKey", "key");
			relicBindAdvancement(writer, hardMode, BotaniaItems.EYE_OF_THE_FLUGEL, "flugelEye", "eye");
			relicBindAdvancement(writer, hardMode, BotaniaItems.RING_OF_THOR, "thorRing", "ring");
			relicBindAdvancement(writer, hardMode, BotaniaItems.RING_OF_ODIN, "odinRing", "ring");
			AdvancementHolder lokiRing = relicBindAdvancement(writer, hardMode, BotaniaItems.RING_OF_LOKI, "lokiRing", "ring");

			Advancement.Builder.advancement()
					.display(simple(BotaniaItems.RING_OF_LOKI, "lokiRingMany", AdvancementType.CHALLENGE))
					.parent(lokiRing)
					.rewards(AdvancementRewards.Builder.experience(85))
					.addCriterion("place_blocks",
							LokiPlaceTrigger.Instance.blocksPlaced(MinMaxBounds.Ints.atLeast(255)))
					.save(writer, challengeId("ring_of_loki_many"));
			Advancement.Builder.advancement()
					.display(simple(BotaniaItems.THE_PINKINATOR, "the_pinkinator", AdvancementType.CHALLENGE))
					.parent(hardMode)
					.rewards(AdvancementRewards.Builder.experience(40))
					.addCriterion("use_pinkinator",
							UseItemSuccessTrigger.Instance.used(BotaniaItems.THE_PINKINATOR))
					.save(writer, challengeId("the_pinkinator"));

			// Misc challenges
			Advancement.Builder.advancement()
					.display(simple(Blocks.PLAYER_HEAD, "gaiaGuardianNoArmor", AdvancementType.CHALLENGE))
					.parent(root)
					.rewards(AdvancementRewards.Builder.experience(1000))
					.addCriterion("no_armor", GaiaGuardianNoArmorTrigger.Instance.unarmoredKill())
					.save(writer, challengeId("gaia_guardian_no_armor"));
			Advancement.Builder.advancement()
					.display(hidden(BotaniaBlocks.DAYBLOOM_MOTIF, "old_flower_pickup", AdvancementType.CHALLENGE))
					.parent(root)
					.rewards(AdvancementRewards.Builder.experience(40))
					.addCriterion("flower", onPickup(BotaniaBlocks.DAYBLOOM_MOTIF, BotaniaBlocks.NIGHTSHADE_MOTIF))
					.requirements(AdvancementRequirements.Strategy.OR)
					.save(writer, challengeId("old_flower_pickup"));
			Advancement.Builder.advancement()
					.display(simple(BotaniaBlocks.CORPOREA_INDEX, "superCorporeaRequest", AdvancementType.CHALLENGE))
					.parent(root)
					.rewards(AdvancementRewards.Builder.experience(85))
					.addCriterion("big_request", CorporeaRequestTrigger.Instance.numExtracted(
							MinMaxBounds.Ints.atLeast(CorporeaIndexBlockEntity.MAX_REQUEST)))
					.save(writer, challengeId("super_corporea_request"));
			Advancement.Builder.advancement()
					.display(simple(BotaniaItems.TERRA_SHATTERER, "rankSSPick", AdvancementType.CHALLENGE))
					.parent(root)
					.rewards(AdvancementRewards.Builder.experience(500))
					.addCriterion("code_triggered",
							CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
					.save(writer, challengeId("rank_ss_pick"));
			CompoundTag level20Shard = new CompoundTag();
			level20Shard.putInt("level", 19);
			Advancement.Builder.advancement()
					.display(simple(BotaniaItems.SHARD_OF_LAPUTA, "l20ShardUse", AdvancementType.CHALLENGE))
					.parent(root)
					.rewards(AdvancementRewards.Builder.experience(65))
					.addCriterion("use_l20_shard", InventoryChangeTrigger.TriggerInstance.hasItems(
							ItemPredicate.Builder.item().of(BotaniaItems.SHARD_OF_LAPUTA).hasComponents(
									DataComponentPredicate.builder()
											.expect(BotaniaDataComponents.SHARD_LEVEL, LaputaShardItem.MAX_LEVEL).build())
									.build()))
					.save(writer, challengeId("l20_shard_use"));
			Advancement.Builder.advancement()
					.display(hidden(Items.BREAD, "alfPortalBread", AdvancementType.CHALLENGE))
					.parent(root)
					.rewards(AdvancementRewards.Builder.experience(40))
					.addCriterion("bread", AlfheimPortalBreadTrigger.Instance.sentBread())
					.save(writer, challengeId("alf_portal_bread"));
			Advancement.Builder.advancement()
					.display(simple(BotaniaBlocks.TINY_POTATO, "tinyPotatoBirthday", AdvancementType.CHALLENGE))
					.parent(root)
					.rewards(AdvancementRewards.Builder.experience(40))
					.addCriterion("code_triggered",
							CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
					.save(writer, challengeId("tiny_potato_birthday"));
			addLooniumMobsToKill(Advancement.Builder.advancement())
					.display(simple(BotaniaBlocks.LOONIUM, "allLooniumMobs", AdvancementType.CHALLENGE))
					.parent(root)
					.requirements(AdvancementRequirements.Strategy.AND)
					.save(writer, challengeId("all_loonium_mobs"));
		}

		private static Advancement.Builder addLooniumMobsToKill(Advancement.Builder builder) {
			for (EntityType<?> entityType : LOONIUM_MOBS_TO_KILL) {
				builder.addCriterion(
						BuiltInRegistries.ENTITY_TYPE.getKey(entityType).toString(),
						KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity()
								.of(entityType).team(LooniumBlockEntity.LOONIUM_TEAM_NAME))
				);
			}

			return builder;
		}
	}

	private static AdvancementHolder relicBindAdvancement(Consumer<AdvancementHolder> consumer, AdvancementHolder parent, Item relicItem,
			String titleKey, String criterionName) {
		String id = challengeId(BuiltInRegistries.ITEM.getKey(relicItem).getPath());
		return Advancement.Builder.advancement()
				.display(simple(relicItem, titleKey, AdvancementType.CHALLENGE))
				.parent(parent)
				.rewards(AdvancementRewards.Builder.experience(50))
				.addCriterion(criterionName, RelicBindTrigger.Instance.bound(relicItem))
				.save(consumer, id);
	}

	protected static Criterion<InventoryChangeTrigger.TriggerInstance> onPickup(TagKey<Item> tag) {
		return InventoryChangeTrigger.TriggerInstance.hasItems(
				ItemPredicate.Builder.item().of(tag).build());
	}

	protected static Criterion<InventoryChangeTrigger.TriggerInstance> onPickup(ItemLike... items) {
		return InventoryChangeTrigger.TriggerInstance.hasItems(matchItems(items));
	}

	protected static ItemPredicate matchItems(ItemLike... items) {
		return ItemPredicate.Builder.item().of(items).build();
	}

	protected static DisplayInfo simple(ItemLike icon, String name, AdvancementType advancementType) {
		String expandedName = "advancement.botania:" + name;
		return new DisplayInfo(new ItemStack(icon.asItem()),
				Component.translatable(expandedName),
				Component.translatable(expandedName + ".desc"),
				Optional.empty(), advancementType, true, true, false);
	}

	protected static DisplayInfo hidden(ItemLike icon, String name, AdvancementType advancementType) {
		String expandedName = "advancement.botania:" + name;
		return new DisplayInfo(new ItemStack(icon.asItem()),
				Component.translatable(expandedName),
				Component.translatable(expandedName + ".desc"),
				Optional.empty(), advancementType, true, true, true);
	}

	protected static DisplayInfo rootDisplay(ItemLike icon, String titleKey, String descKey, ResourceLocation background) {
		return new DisplayInfo(new ItemStack(icon.asItem()),
				Component.translatable(titleKey),
				Component.translatable(descKey),
				Optional.of(background), AdvancementType.TASK, false, false, false);
	}

	private static String mainId(String name) {
		return botaniaRL("main/" + name).toString();
	}

	private static String challengeId(String name) {
		return botaniaRL("challenge/" + name).toString();
	}
}
