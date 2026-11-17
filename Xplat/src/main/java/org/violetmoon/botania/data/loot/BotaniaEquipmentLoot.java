/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package vazkii.botania.data.loot;

import net.minecraft.advancements.critereon.EntityEquipmentPredicate;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.item.armortrim.TrimPatterns;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.loot.BotaniaLootTables;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;

public class BotaniaEquipmentLoot implements LootTableSubProvider {
	public static final int COLOR_ENDERMAN_BODY = 0x1d1d21; // (black)
	public static final int COLOR_TIDE_LEATHER = 0x169c9c; // (cyan)
	public static final int COLOR_EVOKER_COAT = 0x323639; // (black + gray)
	public static final int COLOR_VINDICATOR_BOOTS = 0x323639; // (black + gray(
	public static final int COLOR_VINDICATOR_JACKET = 0x474f52; // (gray)
	public static final int COLOR_VINDICATOR_LEGWEAR = 0x168c8c; // (black + 7 cyan)
	public static final int COLOR_ILLUSIONER_COAT = 0x3b7bc2; // (blue + light blue)

	private final HolderLookup.Provider registries;
	private final HolderLookup.RegistryLookup<TrimPattern> patternRegistry;
	private final HolderLookup.RegistryLookup<TrimMaterial> materialRegistry;
	private final Map<Holder<ArmorMaterial>, List<Item>> armorItems = Map.of(
			ArmorMaterials.LEATHER, List.of(Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS),
			ArmorMaterials.CHAIN, List.of(Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS),
			ArmorMaterials.IRON, List.of(Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS),
			ArmorMaterials.GOLD, List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS),
			ArmorMaterials.DIAMOND, List.of(Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS),
			ArmorMaterials.NETHERITE, List.of(Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS)
	);

	public BotaniaEquipmentLoot(HolderLookup.Provider registries) {
		this.registries = registries;
		this.patternRegistry = registries.lookupOrThrow(Registries.TRIM_PATTERN);
		this.materialRegistry = registries.lookupOrThrow(Registries.TRIM_MATERIAL);
	}

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
		defineGaiaHardWitherSkeletonEquipment(output);

		defineDefaultEquipmentTables(output);
		defineAncientCityEquipmentTables(output);
		defineBastionRemnantEquipmentTables(output);
		defineDesertPyramidEquipmentTables(output);
		defineEndCityEquipmentTables(output);
		defineJungleTempleEquipmentTables(output);
		defineFortressEquipmentTables(output);
		defineOceanMonumentEquipmentTables(output);
		definePillagerOutpostEquipmentTables(output);
		defineRuinedPortalEquipmentTables(output);
		defineShipwreckEquipmentTables(output);
		defineStrongholdEquipmentTables(output);
		defineTrailRuinsEquipmentTables(output);
		defineTrialChamberEquipmentTables(output);
		defineWoodlandMansionEquipmentTables(output);
	}

	private void defineGaiaHardWitherSkeletonEquipment(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
		output.accept(BotaniaLootTables.GAIA_HARD_WITHER_SKELETON,
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(BotaniaItems.ELEMENTIUM_SWORD))));
	}

	private LootPoolEntryContainer.Builder<?> buildWeaponAxe() {
		return NestedLootTable.inlineLootTable(LootTable.lootTable().withPool(LootPool.lootPool()
				.apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)
						.when(LootItemRandomChanceCondition.randomChance(0.3f)))
				.add(LootItem.lootTableItem(Items.IRON_AXE).setWeight(4))
				.add(LootItem.lootTableItem(Items.DIAMOND_AXE))
		).build());
	}

	private LootPoolEntryContainer.Builder<?> buildWeaponAxeGold() {
		return LootItem.lootTableItem(Items.GOLDEN_AXE)
				.apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)
						.when(LootItemRandomChanceCondition.randomChance(0.3f)));
	}

	private LootPoolEntryContainer.Builder<?> buildWeaponAxeIron() {
		return LootItem.lootTableItem(Items.IRON_AXE)
				.apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)
						.when(LootItemRandomChanceCondition.randomChance(0.3f)));
	}

	private LootPoolEntryContainer.Builder<?> buildWeaponBow() {
		return LootItem.lootTableItem(Items.BOW)
				.apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)
						.when(LootItemRandomChanceCondition.randomChance(0.3f)));
	}

	private LootPoolEntryContainer.Builder<?> buildWeaponCrossbow() {
		return LootItem.lootTableItem(Items.CROSSBOW)
				.apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries));
	}

	private LootPoolEntryContainer.Builder<?> buildWeaponSword() {
		return NestedLootTable.inlineLootTable(LootTable.lootTable().withPool(LootPool.lootPool()
				.apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)
						.when(LootItemRandomChanceCondition.randomChance(0.3f)))
				.add(LootItem.lootTableItem(Items.IRON_SWORD).setWeight(4))
				.add(LootItem.lootTableItem(Items.DIAMOND_SWORD))
		).build());
	}

	private LootPoolEntryContainer.Builder<?> buildWeaponSwordGold() {
		return LootItem.lootTableItem(Items.GOLDEN_SWORD)
				.apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)
						.when(LootItemRandomChanceCondition.randomChance(0.3f)));
	}

	private LootPoolEntryContainer.Builder<?> buildWeaponTrident() {
		// no useful enchantments for mob usage
		return LootItem.lootTableItem(Items.TRIDENT);
	}

	private LootPoolEntryContainer.Builder<?> buildZombieVillagerEquipmentByProfession() {
		return NestedLootTable.inlineLootTable(LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(NestedLootTable.inlineLootTable(
								createArmorSet(UnaryOperator.identity(), true, armorItems.get(ArmorMaterials.CHAIN)).build()
						).setWeight(4))
						.add(NestedLootTable.inlineLootTable(
								createArmorSet(UnaryOperator.identity(), true, armorItems.get(ArmorMaterials.IRON)).build()
						).setWeight(10))
						.add(NestedLootTable.inlineLootTable(
								createArmorSet(UnaryOperator.identity(), true, armorItems.get(ArmorMaterials.DIAMOND)).build()
						).setWeight(1))
						.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
								EntityPredicate.Builder.entity().nbt(
										new NbtPredicate(getProfessionNbt(VillagerProfession.ARMORER)))))
				)
				.withPool(LootPool.lootPool()
						.add(NestedLootTable.inlineLootTable(
								// TODO: is there any good way to randomly dye the armor items?
								createArmorSet(UnaryOperator.identity(), true, armorItems.get(ArmorMaterials.LEATHER)).build()
						))
						.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
								EntityPredicate.Builder.entity().nbt(
										new NbtPredicate(getProfessionNbt(VillagerProfession.LEATHERWORKER)))))
				)
				.withPool(LootPool.lootPool()
						.add(buildWeaponAxe()
								.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
										EntityPredicate.Builder.entity().nbt(
												new NbtPredicate(getProfessionNbt(VillagerProfession.BUTCHER))))))
						.add(LootItem.lootTableItem(Items.IRON_HOE)
								.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
										EntityPredicate.Builder.entity().nbt(
												new NbtPredicate(getProfessionNbt(VillagerProfession.FARMER))))))
						.add(LootItem.lootTableItem(Items.BOW)
								.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
										EntityPredicate.Builder.entity().nbt(
												new NbtPredicate(getProfessionNbt(VillagerProfession.FLETCHER))))))
						.add(LootItem.lootTableItem(Items.FISHING_ROD)
								.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
										EntityPredicate.Builder.entity().nbt(
												new NbtPredicate(getProfessionNbt(VillagerProfession.FISHERMAN))))))
						.add(LootItem.lootTableItem(Items.IRON_PICKAXE)
								.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
										EntityPredicate.Builder.entity().nbt(
												new NbtPredicate(getProfessionNbt(VillagerProfession.TOOLSMITH))))))
						.add(buildWeaponSword()
								.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
										EntityPredicate.Builder.entity().nbt(
												new NbtPredicate(getProfessionNbt(VillagerProfession.WEAPONSMITH))))))
				).build());
	}

	private LootPoolEntryContainer.Builder<?> buildWeaponForPiglin() {
		return NestedLootTable.inlineLootTable(LootTable.lootTable().withPool(LootPool.lootPool()
				.apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)
						.when(LootItemRandomChanceCondition.randomChance(0.3f)))
				.add(LootItem.lootTableItem(Items.GOLDEN_SWORD))
				.add(LootItem.lootTableItem(Items.CROSSBOW))
		).build());
	}

	private LootPoolEntryContainer.Builder<?> buildWeaponForWitherSkeleton() {
		return NestedLootTable.inlineLootTable(LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(-1, 1))
				.apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries))
				.add(LootItem.lootTableItem(Items.STONE_SWORD))
				.add(LootItem.lootTableItem(Items.BOW))
		).build());
	}

	// [VanillaCopy] Tag names are synchronized with ZombieVillager::addAdditionalSaveData and VillagerData::CODEC
	private CompoundTag getProfessionNbt(VillagerProfession profession) {
		var villagerDataTag = new CompoundTag();
		BuiltInRegistries.VILLAGER_PROFESSION.byNameCodec().encodeStart(NbtOps.INSTANCE, profession).resultOrPartial(
				BotaniaAPI.LOGGER::error).ifPresent(data -> villagerDataTag.put("profession", data));
		var tag = new CompoundTag();
		tag.put("VillagerData", villagerDataTag);
		return tag;
	}

	private void defineDefaultEquipmentTables(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
		output.accept(BotaniaLootTables.LOONIUM_DROWNED_DEFAULT,
				LootTable.lootTable().withPool(LootPool.lootPool().add(buildWeaponTrident())));
		output.accept(BotaniaLootTables.LOONIUM_PIGLIN_BRUTE_DEFAULT,
				LootTable.lootTable().withPool(LootPool.lootPool().add(buildWeaponAxeGold())));
		output.accept(BotaniaLootTables.LOONIUM_PILLAGER_DEFAULT,
				LootTable.lootTable().withPool(LootPool.lootPool().add(buildWeaponCrossbow())));
		output.accept(BotaniaLootTables.LOONIUM_SKELETON_DEFAULT,
				LootTable.lootTable().withPool(LootPool.lootPool().add(buildWeaponBow())));
		output.accept(BotaniaLootTables.LOONIUM_VINDICATOR_DEFAULT,
				LootTable.lootTable().withPool(LootPool.lootPool().add(buildWeaponAxeIron())));
		output.accept(BotaniaLootTables.LOONIUM_ZOMBIE_DEFAULT,
				LootTable.lootTable().withPool(LootPool.lootPool().add(buildWeaponSword())));
		output.accept(BotaniaLootTables.LOONIUM_ZOMBIE_VILLAGER,
				LootTable.lootTable().withPool(LootPool.lootPool().add(buildZombieVillagerEquipmentByProfession())));
	}

	private void defineAncientCityEquipmentTables(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {

		ArmorTrim trimWardQuartz = getTrim(TrimPatterns.WARD, TrimMaterials.QUARTZ);
		ArmorTrim trimSilenceCopper = getTrim(TrimPatterns.SILENCE, TrimMaterials.COPPER);

		output.accept(BotaniaLootTables.LOONIUM_ARMOR_ANCIENT_CITY,
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimWardQuartz,
								armorItems.get(ArmorMaterials.IRON)).build()).setWeight(11))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimWardQuartz,
								armorItems.get(ArmorMaterials.DIAMOND)).build()).setWeight(5))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimSilenceCopper,
								armorItems.get(ArmorMaterials.GOLD)).build()).setWeight(3))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimSilenceCopper,
								armorItems.get(ArmorMaterials.DIAMOND)).build()).setWeight(1))
				).withPool(LootPool.lootPool()
						// Note: Slowness from Strays stacks with tipped arrow effects, so just checking for bow here
						.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
								EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment()
										.mainhand(ItemPredicate.Builder.item().of(Items.BOW)).build())))
						.when(LootItemRandomChanceCondition.randomChance(0.9f))
						.add(LootItem.lootTableItem(Items.TIPPED_ARROW).apply(SetComponentsFunction.setComponent(
								DataComponents.POTION_CONTENTS, PotionContents.EMPTY.withEffectAdded(
										new MobEffectInstance(MobEffects.DARKNESS, 200)))))
				)
		);
		output.accept(BotaniaLootTables.LOONIUM_DROWNED_ANCIENT_CITY,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponTrident()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_ANCIENT_CITY)))
		);
		output.accept(BotaniaLootTables.LOONIUM_SKELETON_ANCIENT_CITY,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponBow()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_ANCIENT_CITY)))
		);
		output.accept(BotaniaLootTables.LOONIUM_ZOMBIE_ANCIENT_CITY,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponSword()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_ANCIENT_CITY)))
		);
	}

	private void defineBastionRemnantEquipmentTables(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {

		ArmorTrim trimSnoutGold = getTrim(TrimPatterns.SNOUT, TrimMaterials.GOLD);
		ArmorTrim trimSnoutNetherite = getTrim(TrimPatterns.SNOUT, TrimMaterials.NETHERITE);

		output.accept(BotaniaLootTables.LOONIUM_ARMOR_BASTION_REMNANT,
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimSnoutNetherite,
								armorItems.get(ArmorMaterials.GOLD)).build()).setWeight(4))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimSnoutGold,
								armorItems.get(ArmorMaterials.NETHERITE)).build()).setWeight(1))
				)
		);
		output.accept(BotaniaLootTables.LOONIUM_PIGLIN_BASTION_REMNANT,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponForPiglin()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_BASTION_REMNANT)))
		);
	}

	private void defineDesertPyramidEquipmentTables(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {

		ArmorTrim trimDuneRedstone = getTrim(TrimPatterns.DUNE, TrimMaterials.REDSTONE);

		output.accept(BotaniaLootTables.LOONIUM_ARMOR_DESERT_PYRAMID,
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimDuneRedstone,
								armorItems.get(ArmorMaterials.IRON)).build()).setWeight(5))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimDuneRedstone,
								armorItems.get(ArmorMaterials.GOLD)).build()).setWeight(2))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimDuneRedstone,
								armorItems.get(ArmorMaterials.DIAMOND)).build()).setWeight(1))
				)
		);
		output.accept(BotaniaLootTables.LOONIUM_SKELETON_DESERT_PYRAMID,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponBow()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_DESERT_PYRAMID)))
		);
		output.accept(BotaniaLootTables.LOONIUM_ZOMBIE_DESERT_PYRAMID,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponSword()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_DESERT_PYRAMID)))
		);
	}

	private void defineEndCityEquipmentTables(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {

		ArmorTrim trimSpireAmethyst = getTrim(TrimPatterns.SPIRE, TrimMaterials.AMETHYST);

		output.accept(BotaniaLootTables.LOONIUM_ARMOR_END_CITY,
				LootTable.lootTable().withPool(LootPool.lootPool()
						.apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)
								.when(LootItemRandomChanceCondition.randomChance(0.3f)))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimSpireAmethyst,
								armorItems.get(ArmorMaterials.IRON)).build()).setWeight(3))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimSpireAmethyst,
								armorItems.get(ArmorMaterials.GOLD)).build()).setWeight(2))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimSpireAmethyst,
								armorItems.get(ArmorMaterials.DIAMOND)).build()).setWeight(2))
				).withPool(LootPool.lootPool()
						.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
								EntityPredicate.Builder.entity()
										.entityType(EntityTypePredicate.of(EntityType.SKELETON))))
						.when(LootItemRandomChanceCondition.randomChance(0.9f))
						.add(LootItem.lootTableItem(Items.TIPPED_ARROW).apply(SetComponentsFunction.setComponent(
								DataComponents.POTION_CONTENTS, PotionContents.EMPTY.withEffectAdded(
										new MobEffectInstance(MobEffects.LEVITATION, 200)))))
				)
		);
		output.accept(BotaniaLootTables.LOONIUM_SKELETON_END_CITY,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponBow()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_END_CITY)))
		);
		output.accept(BotaniaLootTables.LOONIUM_ZOMBIE_END_CITY,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponSword()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_END_CITY)))
		);
	}

	private void defineFortressEquipmentTables(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {

		ArmorTrim trimRibIron = getTrim(TrimPatterns.RIB, TrimMaterials.IRON);

		output.accept(BotaniaLootTables.LOONIUM_ARMOR_FORTRESS,
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimRibIron,
								armorItems.get(ArmorMaterials.IRON)).build()).setWeight(7))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimRibIron,
								armorItems.get(ArmorMaterials.GOLD)).build()).setWeight(3))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimRibIron,
								armorItems.get(ArmorMaterials.DIAMOND)).build()).setWeight(2))
				)
		);
		output.accept(BotaniaLootTables.LOONIUM_SKELETON_FORTRESS,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponForWitherSkeleton()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_FORTRESS)))
		);
		output.accept(BotaniaLootTables.LOONIUM_ZOMBIE_FORTRESS,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponSwordGold()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_FORTRESS)))
		);
	}

	private void defineJungleTempleEquipmentTables(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {

		ArmorTrim trimWildEmerald = getTrim(TrimPatterns.WILD, TrimMaterials.EMERALD);

		output.accept(BotaniaLootTables.LOONIUM_ARMOR_JUNGLE_TEMPLE,
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimWildEmerald,
								armorItems.get(ArmorMaterials.CHAIN)).build()).setWeight(4))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimWildEmerald,
								armorItems.get(ArmorMaterials.GOLD)).build()).setWeight(2))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimWildEmerald,
								armorItems.get(ArmorMaterials.DIAMOND)).build()).setWeight(1))
				)
		);
		output.accept(BotaniaLootTables.LOONIUM_DROWNED_JUNGLE_TEMPLE,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponTrident()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_JUNGLE_TEMPLE)))
		);
		output.accept(BotaniaLootTables.LOONIUM_SKELETON_JUNGLE_TEMPLE,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponBow()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_JUNGLE_TEMPLE)))
		);
		output.accept(BotaniaLootTables.LOONIUM_ZOMBIE_JUNGLE_TEMPLE,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponSword()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_JUNGLE_TEMPLE)))
		);
	}

	private void defineOceanMonumentEquipmentTables(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {

		output.accept(BotaniaLootTables.LOONIUM_ARMOR_MONUMENT,
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.inlineLootTable(createRandomizedDyedArmorSet(
								getTrim(TrimPatterns.TIDE, TrimMaterials.COPPER), COLOR_TIDE_LEATHER,
								armorItems.get(ArmorMaterials.LEATHER)).build()).setWeight(2))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(
								getTrim(TrimPatterns.TIDE, TrimMaterials.DIAMOND),
								armorItems.get(ArmorMaterials.GOLD)).build()).setWeight(3))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(
								getTrim(TrimPatterns.TIDE, TrimMaterials.GOLD),
								armorItems.get(ArmorMaterials.DIAMOND)).build()).setWeight(1))
				)
		);
		output.accept(BotaniaLootTables.LOONIUM_DROWNED_MONUMENT,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponTrident()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_MONUMENT)))
		);
		output.accept(BotaniaLootTables.LOONIUM_SKELETON_MONUMENT,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponBow()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_MONUMENT)))
		);
		output.accept(BotaniaLootTables.LOONIUM_ZOMBIE_MONUMENT,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponSword()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_MONUMENT)))
		);
	}

	private void definePillagerOutpostEquipmentTables(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {

		ArmorTrim trimSentryEmerald = getTrim(TrimPatterns.SENTRY, TrimMaterials.EMERALD);

		output.accept(BotaniaLootTables.LOONIUM_ARMOR_OUTPOST,
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimSentryEmerald,
								armorItems.get(ArmorMaterials.CHAIN)).build()).setWeight(5))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimSentryEmerald,
								armorItems.get(ArmorMaterials.IRON)).build()).setWeight(3))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimSentryEmerald,
								armorItems.get(ArmorMaterials.DIAMOND)).build()).setWeight(1))
				)
		);
		output.accept(BotaniaLootTables.LOONIUM_SKELETON_OUTPOST,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponBow()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_OUTPOST)))
		);
		output.accept(BotaniaLootTables.LOONIUM_ZOMBIE_OUTPOST,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponSword()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_OUTPOST)))
		);
	}

	private void defineRuinedPortalEquipmentTables(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {

		output.accept(BotaniaLootTables.LOONIUM_ARMOR_PORTAL,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(
								LootItem.lootTableItem(Items.GOLDEN_HELMET)).setRolls(UniformGenerator.between(0, 1)))
						.withPool(LootPool.lootPool().add(
								LootItem.lootTableItem(Items.GOLDEN_CHESTPLATE)).setRolls(UniformGenerator.between(0, 1)))
						.withPool(LootPool.lootPool().add(
								LootItem.lootTableItem(Items.GOLDEN_LEGGINGS)).setRolls(UniformGenerator.between(0, 1)))
						.withPool(LootPool.lootPool().add(
								LootItem.lootTableItem(Items.GOLDEN_BOOTS)).setRolls(UniformGenerator.between(0, 1)))
		);

		output.accept(BotaniaLootTables.LOONIUM_DROWNED_PORTAL,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(
								NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_PORTAL)))
						.withPool(LootPool.lootPool().add(buildWeaponTrident()))
		);
		output.accept(BotaniaLootTables.LOONIUM_PIGLIN_PORTAL,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(
								NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_PORTAL)))
						.withPool(LootPool.lootPool().add(buildWeaponForPiglin()))
		);
		output.accept(BotaniaLootTables.LOONIUM_SKELETON_PORTAL,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(
								NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_PORTAL)))
						.withPool(LootPool.lootPool().add(buildWeaponBow()))
		);
		output.accept(BotaniaLootTables.LOONIUM_ZOMBIE_PORTAL,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(
								NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_PORTAL)))
						.withPool(LootPool.lootPool().add(buildWeaponSwordGold()))
		);
	}

	private void defineShipwreckEquipmentTables(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {

		ArmorTrim trimCoastEmerald = getTrim(TrimPatterns.COAST, TrimMaterials.EMERALD);

		output.accept(BotaniaLootTables.LOONIUM_ARMOR_SHIPWRECK,
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimCoastEmerald,
								armorItems.get(ArmorMaterials.CHAIN)).build()).setWeight(4))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimCoastEmerald,
								armorItems.get(ArmorMaterials.IRON)).build()).setWeight(4))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimCoastEmerald,
								armorItems.get(ArmorMaterials.DIAMOND)).build()).setWeight(1))
				)
		);
		output.accept(BotaniaLootTables.LOONIUM_DROWNED_SHIPWRECK,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponTrident()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_SHIPWRECK)))
		);
		output.accept(BotaniaLootTables.LOONIUM_SKELETON_SHIPWRECK,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponBow()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_SHIPWRECK)))
		);
		output.accept(BotaniaLootTables.LOONIUM_ZOMBIE_SHIPWRECK,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponSword()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_SHIPWRECK)))
		);
	}

	private void defineStrongholdEquipmentTables(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {

		ArmorTrim trimEyeRedstone = getTrim(TrimPatterns.EYE, TrimMaterials.REDSTONE);
		ArmorTrim trimEyeLapis = getTrim(TrimPatterns.EYE, TrimMaterials.LAPIS);

		output.accept(BotaniaLootTables.LOONIUM_ARMOR_STRONGHOLD,
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimEyeLapis,
								armorItems.get(ArmorMaterials.IRON)).build()).setWeight(5))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimEyeRedstone,
								armorItems.get(ArmorMaterials.GOLD)).build()).setWeight(3))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimEyeLapis,
								armorItems.get(ArmorMaterials.DIAMOND)).build()).setWeight(2))
						// Enderman cosplay
						.add(NestedLootTable.inlineLootTable(LootTable.lootTable()
								.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.LEATHER_HELMET)
										.apply(setTrim(getTrim(TrimPatterns.EYE, TrimMaterials.AMETHYST)))
										.apply(setDyedColor(COLOR_ENDERMAN_BODY))))
								.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.LEATHER_CHESTPLATE)
										.apply(setDyedColor(COLOR_ENDERMAN_BODY))))
								.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.LEATHER_LEGGINGS)
										.apply(setDyedColor(COLOR_ENDERMAN_BODY))))
								.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.LEATHER_BOOTS)
										.apply(setDyedColor(COLOR_ENDERMAN_BODY))))
								.build()).setWeight(1))
				)
		);
		output.accept(BotaniaLootTables.LOONIUM_DROWNED_STRONGHOLD,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponTrident()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_STRONGHOLD)))
		);
		output.accept(BotaniaLootTables.LOONIUM_SKELETON_STRONGHOLD,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponBow()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_STRONGHOLD)))
		);
		output.accept(BotaniaLootTables.LOONIUM_ZOMBIE_STRONGHOLD,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponSword()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_STRONGHOLD)))
		);
	}

	private void defineTrailRuinsEquipmentTables(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {

		ArmorTrim trimHostEmerald = getTrim(TrimPatterns.HOST, TrimMaterials.EMERALD);
		ArmorTrim trimRaiserAmethyst = getTrim(TrimPatterns.RAISER, TrimMaterials.AMETHYST);
		ArmorTrim trimShaperLapis = getTrim(TrimPatterns.SHAPER, TrimMaterials.LAPIS);
		ArmorTrim trimWayfinderRedstone = getTrim(TrimPatterns.WAYFINDER, TrimMaterials.REDSTONE);

		output.accept(BotaniaLootTables.LOONIUM_ARMOR_TRAIL_RUINS,
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimHostEmerald,
								armorItems.get(ArmorMaterials.CHAIN)).build()).setWeight(7))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimWayfinderRedstone,
								armorItems.get(ArmorMaterials.CHAIN)).build()).setWeight(7))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimRaiserAmethyst,
								armorItems.get(ArmorMaterials.IRON)).build()).setWeight(8))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimHostEmerald,
								armorItems.get(ArmorMaterials.IRON)).build()).setWeight(8))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimRaiserAmethyst,
								armorItems.get(ArmorMaterials.GOLD)).build()).setWeight(3))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimShaperLapis,
								armorItems.get(ArmorMaterials.GOLD)).build()).setWeight(3))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimShaperLapis,
								armorItems.get(ArmorMaterials.DIAMOND)).build()).setWeight(2))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimWayfinderRedstone,
								armorItems.get(ArmorMaterials.DIAMOND)).build()).setWeight(2))
				)
		);
		output.accept(BotaniaLootTables.LOONIUM_DROWNED_TRAIL_RUINS,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponTrident()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_TRAIL_RUINS)))
		);
		output.accept(BotaniaLootTables.LOONIUM_SKELETON_TRAIL_RUINS,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponBow()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_TRAIL_RUINS)))
		);
		output.accept(BotaniaLootTables.LOONIUM_ZOMBIE_TRAIL_RUINS,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponSword()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_TRAIL_RUINS)))
		);
	}

	private void defineTrialChamberEquipmentTables(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {

		ArmorTrim trimBoltCopper = getTrim(TrimPatterns.BOLT, TrimMaterials.COPPER);
		ArmorTrim trimFlowDiamond = getTrim(TrimPatterns.FLOW, TrimMaterials.DIAMOND);

		output.accept(BotaniaLootTables.LOONIUM_ARMOR_TRIAL_CHAMBER,
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimBoltCopper,
								armorItems.get(ArmorMaterials.CHAIN)).build()).setWeight(4))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimFlowDiamond,
								armorItems.get(ArmorMaterials.IRON)).build()).setWeight(4))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimBoltCopper,
								armorItems.get(ArmorMaterials.IRON)).build()).setWeight(1))
						.add(NestedLootTable.inlineLootTable(createRandomizedArmorSet(trimFlowDiamond,
								armorItems.get(ArmorMaterials.DIAMOND)).build()).setWeight(1))
				)
		);
		output.accept(BotaniaLootTables.LOONIUM_DROWNED_TRIAL_CHAMBER,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponTrident()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_TRIAL_CHAMBER)))
		);
		output.accept(BotaniaLootTables.LOONIUM_SKELETON_TRIAL_CHAMBER,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponBow()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_TRIAL_CHAMBER)))
		);
		output.accept(BotaniaLootTables.LOONIUM_ZOMBIE_TRIAL_CHAMBER,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().add(buildWeaponSword()))
						.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.LOONIUM_ARMOR_TRIAL_CHAMBER)))
		);
	}

	private void defineWoodlandMansionEquipmentTables(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {

		// it's cosplays all the way down
		output.accept(BotaniaLootTables.LOONIUM_ARMOR_MANSION, LootTable.lootTable().withPool(LootPool.lootPool()
				// Evoker cosplay, with higher likelihood of holding a totem
				.add(NestedLootTable.inlineLootTable(createDyedArmorSet(
						getTrim(TrimPatterns.VEX, TrimMaterials.GOLD), COLOR_EVOKER_COAT,
						List.of(Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS))
						.withPool(LootPool.lootPool()
								.when(LootItemRandomChanceCondition.randomChance(0.2f))
								.add(LootItem.lootTableItem(Items.TOTEM_OF_UNDYING)))
						.build())
						.setWeight(2)
				)
				// Vindicator cosplay, usually including axe (even for ranged mobs)
				.add(NestedLootTable.inlineLootTable(LootTable.lootTable()
						.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.LEATHER_CHESTPLATE)
								.apply(setTrim(getTrim(TrimPatterns.VEX, TrimMaterials.NETHERITE)))
								.apply(setDyedColor(COLOR_VINDICATOR_JACKET))))
						.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.LEATHER_LEGGINGS)
								.apply(setDyedColor(COLOR_VINDICATOR_LEGWEAR))))
						.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.LEATHER_BOOTS)
								.apply(setDyedColor(COLOR_VINDICATOR_BOOTS))))
						.withPool(LootPool.lootPool()
								.when(LootItemRandomChanceCondition.randomChance(0.9f))
								.add(LootItem.lootTableItem(Items.IRON_AXE)
										.apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)
												.when(LootItemRandomChanceCondition.randomChance(0.3f)))))
						.build()).setWeight(2)
				)
				// Illusioner cosplay, including bow and blindness arrows, even for mobs that don't know how to use bows
				.add(NestedLootTable.inlineLootTable(
						createDyedArmorSet(
								getTrim(TrimPatterns.VEX, TrimMaterials.LAPIS), COLOR_ILLUSIONER_COAT,
								List.of(Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS))
								.withPool(LootPool.lootPool()
										.when(LootItemRandomChanceCondition.randomChance(0.9f))
										.add(LootItem.lootTableItem(Items.BOW)
												.apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)
														.when(LootItemRandomChanceCondition.randomChance(0.3f)))))
								.withPool(LootPool.lootPool()
										.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
												EntityPredicate.Builder.entity()
														.entityType(EntityTypePredicate.of(EntityType.SKELETON))))
										.when(LootItemRandomChanceCondition.randomChance(0.9f))
										.add(LootItem.lootTableItem(Items.TIPPED_ARROW).apply(
												SetComponentsFunction.setComponent(DataComponents.POTION_CONTENTS,
														PotionContents.EMPTY.withEffectAdded(new MobEffectInstance(MobEffects.BLINDNESS, 100)))))
								)
								.build())
						.setWeight(1)
				)
				// Vex cosplay, including sword (even for ranged mobs)
				.add(NestedLootTable.inlineLootTable(
						LootTable.lootTable()
								.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.DIAMOND_HELMET)
										.apply(setTrim(getTrim(TrimPatterns.VEX, TrimMaterials.AMETHYST)))))
								.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.DIAMOND_CHESTPLATE)))
								.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.DIAMOND_LEGGINGS)))
								.withPool(LootPool.lootPool()
										.when(LootItemRandomChanceCondition.randomChance(0.9f))
										.add(LootItem.lootTableItem(Items.IRON_SWORD)
												.apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries)
														.when(LootItemRandomChanceCondition.randomChance(0.3f)))))
								.build())
						.setWeight(45)
						.when(AnyOfCondition.anyOf(
								// focus Vex cosplay on baby mobs, reduce chance for everyone else
								LootItemRandomChanceCondition.randomChance(0.005f),
								LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
										EntityPredicate.Builder.entity()
												.flags(EntityFlagsPredicate.Builder.flags().setIsBaby(true)))
						))
				))
				// also everyone gets a chance to hold a Totem of Undying
				.withPool(LootPool.lootPool()
						.when(LootItemRandomChanceCondition.randomChance(0.05f))
						.add(LootItem.lootTableItem(Items.TOTEM_OF_UNDYING))
				)
		);
	}

	private ArmorTrim getTrim(ResourceKey<TrimPattern> pattern, ResourceKey<TrimMaterial> material) {
		Holder.Reference<TrimPattern> tidePattern = patternRegistry.get(pattern).orElseThrow();
		Holder.Reference<TrimMaterial> goldMaterial = materialRegistry.get(material).orElseThrow();
		return new ArmorTrim(goldMaterial, tidePattern);
	}

	private static UnaryOperator<LootPoolSingletonContainer.Builder<?>> addTrim(ArmorTrim trim) {
		return builder -> builder.apply(setTrim(trim));
	}

	private static UnaryOperator<LootPoolSingletonContainer.Builder<?>> addTrimAndDye(ArmorTrim trim, int color) {
		return builder -> builder.apply(setTrim(trim)).apply(setDyedColor(color));
	}

	private static LootItemConditionalFunction.Builder<?> setTrim(ArmorTrim trim) {
		return SetComponentsFunction.setComponent(DataComponents.TRIM, trim);
	}

	private static LootItemConditionalFunction.Builder<?> setDyedColor(int color) {
		return SetComponentsFunction.setComponent(DataComponents.DYED_COLOR, new DyedItemColor(color, true));
	}

	private LootTable.Builder createRandomizedArmorSet(ArmorTrim trim, List<Item> armorItems) {
		return createArmorSet(addTrim(trim), true, armorItems);
	}

	private LootTable.Builder createRandomizedDyedArmorSet(ArmorTrim trim, Integer color, List<Item> armorItems) {
		return createArmorSet(addTrimAndDye(trim, color), true, armorItems);
	}

	private LootTable.Builder createDyedArmorSet(ArmorTrim trim, Integer color, List<Item> armorItems) {
		return createArmorSet(addTrimAndDye(trim, color), false, armorItems);
	}

	private LootTable.Builder createArmorSet(UnaryOperator<LootPoolSingletonContainer.Builder<?>> armorModifier, boolean randomized, List<Item> armorItems) {
		LootTable.Builder lootTable = LootTable.lootTable();
		for (Item armorItem : armorItems) {
			lootTable.withPool(LootPool.lootPool()
					.setRolls(randomized ? UniformGenerator.between(0, 1) : ConstantValue.exactly(1))
					.add(armorModifier.apply(LootItem.lootTableItem(armorItem))));
		}
		return lootTable;
	}
}
