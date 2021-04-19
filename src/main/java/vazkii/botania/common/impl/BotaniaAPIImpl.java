/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.impl;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.EmptyHandler;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.corporea.ICorporeaNodeDetector;
import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.api.internal.OrechidOutput;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.subtile.functional.SubTileSolegnolia;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.handler.ManaNetworkHandler;
import vazkii.botania.common.integration.corporea.CorporeaNodeDetectors;
import vazkii.botania.common.item.CapWrapper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.relic.ItemLokiRing;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class BotaniaAPIImpl implements BotaniaAPI {
	private static final LazyValue<Rarity> RELIC_RARITY = new LazyValue<>(() -> Rarity.create("RELIC", TextFormatting.GOLD));

	public static List<OrechidOutput> weights = new ArrayList<>();
	public static List<OrechidOutput> netherWeights = new ArrayList<>();

	private enum ArmorMaterial implements IArmorMaterial {
		MANASTEEL("manasteel", 16, new int[] { 2, 5, 6, 2 }, 18, () -> SoundEvents.ITEM_ARMOR_EQUIP_IRON, () -> ModItems.manaSteel, 0),
		MANAWEAVE("manaweave", 5, new int[] { 1, 2, 2, 1 }, 18, () -> SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, () -> ModItems.manaweaveCloth, 0),
		ELEMENTIUM("elementium", 18, new int[] { 2, 5, 6, 2 }, 18, () -> SoundEvents.ITEM_ARMOR_EQUIP_IRON, () -> ModItems.elementium, 0),
		TERRASTEEL("terrasteel", 34, new int[] { 3, 6, 8, 3 }, 26, () -> SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, () -> ModItems.terrasteel, 3);

		private final String name;
		private final int durabilityMultiplier;
		private final int[] damageReduction;
		private final int enchantability;
		private final Supplier<SoundEvent> equipSound;
		private final Supplier<Item> repairItem;
		private final float toughness;
		private static final int[] MAX_DAMAGE_ARRAY = new int[] { 13, 15, 16, 11 };

		ArmorMaterial(String name, int durabilityMultiplier, int[] damageReduction, int enchantability, Supplier<SoundEvent> equipSound, Supplier<Item> repairItem, float toughness) {
			this.name = name;
			this.durabilityMultiplier = durabilityMultiplier;
			this.damageReduction = damageReduction;
			this.enchantability = enchantability;
			this.equipSound = equipSound;
			this.repairItem = repairItem;
			this.toughness = toughness;
		}

		@Override
		public int getDurability(EquipmentSlotType slot) {
			return durabilityMultiplier * MAX_DAMAGE_ARRAY[slot.getIndex()];
		}

		@Override
		public int getDamageReductionAmount(EquipmentSlotType slot) {
			return damageReduction[slot.getIndex()];
		}

		@Override
		public int getEnchantability() {
			return enchantability;
		}

		@Nonnull
		@Override
		public SoundEvent getSoundEvent() {
			return equipSound.get();
		}

		@Nonnull
		@Override
		public Ingredient getRepairMaterial() {
			return Ingredient.fromItems(repairItem.get());
		}

		@Nonnull
		@Override
		public String getName() {
			return name;
		}

		@Override
		public float getToughness() {
			return toughness;
		}

		@Override
		public float getKnockbackResistance() {
			return 0;
		}
	}

	private enum ItemTier implements IItemTier {
		MANASTEEL(300, 6.2F, 2, 3, 20, () -> ModItems.manaSteel),
		ELEMENTIUM(720, 6.2F, 2, 3, 20, () -> ModItems.elementium),
		TERRASTEEL(2300, 9, 3, 4, 26, () -> ModItems.terrasteel);

		private final int maxUses;
		private final float efficiency;
		private final float attackDamage;
		private final int harvestLevel;
		private final int enchantability;
		private final Supplier<Item> repairItem;

		ItemTier(int maxUses, float efficiency, float attackDamage, int harvestLevel, int enchantability, Supplier<Item> repairItem) {
			this.maxUses = maxUses;
			this.efficiency = efficiency;
			this.attackDamage = attackDamage;
			this.harvestLevel = harvestLevel;
			this.enchantability = enchantability;
			this.repairItem = repairItem;
		}

		@Override
		public int getMaxUses() {
			return maxUses;
		}

		@Override
		public float getEfficiency() {
			return efficiency;
		}

		@Override
		public float getAttackDamage() {
			return attackDamage;
		}

		@Override
		public int getHarvestLevel() {
			return harvestLevel;
		}

		@Override
		public int getEnchantability() {
			return enchantability;
		}

		@Override
		public Ingredient getRepairMaterial() {
			return Ingredient.fromItems(repairItem.get());
		}
	}

	@Override
	public int apiVersion() {
		return 2;
	}

	@Override
	public Registry<Brew> getBrewRegistry() {
		return ModBrews.registry;
	}

	@Override
	public IArmorMaterial getManasteelArmorMaterial() {
		return ArmorMaterial.MANASTEEL;
	}

	@Override
	public IArmorMaterial getElementiumArmorMaterial() {
		return ArmorMaterial.ELEMENTIUM;
	}

	@Override
	public IArmorMaterial getManaweaveArmorMaterial() {
		return ArmorMaterial.MANAWEAVE;
	}

	@Override
	public IArmorMaterial getTerrasteelArmorMaterial() {
		return ArmorMaterial.TERRASTEEL;
	}

	@Override
	public IItemTier getManasteelItemTier() {
		return ItemTier.MANASTEEL;
	}

	@Override
	public IItemTier getElementiumItemTier() {
		return ItemTier.ELEMENTIUM;
	}

	@Override
	public IItemTier getTerrasteelItemTier() {
		return ItemTier.TERRASTEEL;
	}

	@Override
	public Rarity getRelicRarity() {
		return RELIC_RARITY.getValue();
	}

	@Override
	public IManaNetwork getManaNetworkInstance() {
		return ManaNetworkHandler.instance;
	}

	@Override
	public int getPassiveFlowerDecay() {
		return LibMisc.PASSIVE_FLOWER_DECAY;
	}

	@Override
	public IInventory getAccessoriesInventory(PlayerEntity player) {
		return new CapWrapper(EquipmentHandler.getAllWorn(player).orElseGet(EmptyHandler::new));
	}

	@Override
	public void breakOnAllCursors(PlayerEntity player, ItemStack stack, BlockPos pos, Direction side) {
		ItemLokiRing.breakOnAllCursors(player, stack, pos, side);
	}

	@Override
	public boolean hasSolegnoliaAround(Entity e) {
		return SubTileSolegnolia.hasSolegnoliaAround(e);
	}

	@Override
	public void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float size, int m) {
		SparkleParticleData data = SparkleParticleData.sparkle(size, r, g, b, m);
		world.addParticle(data, x, y, z, 0, 0, 0);
	}

	@Override
	public boolean shouldForceCheck() {
		return ConfigHandler.COMMON.flowerForceCheck.get();
	}

	private final Map<ResourceLocation, Integer> legacyOreWeights = new ConcurrentHashMap<>();
	private final Map<ResourceLocation, Integer> legacyNetherOreWeights = new ConcurrentHashMap<>();
	private final Map<ResourceLocation, Function<DyeColor, Block>> paintableBlocks = new ConcurrentHashMap<>();

	@Override
	public List<OrechidOutput> getOrechidWeights() {
		return Collections.unmodifiableList(weights);
	}

	@Override
	public List<OrechidOutput> getNetherOrechidWeights() {
		return Collections.unmodifiableList(netherWeights);
	}

	@Override
	public Map<ResourceLocation, Integer> getOreWeights() {
		return Collections.unmodifiableMap(legacyOreWeights);
	}

	@Override
	public Map<ResourceLocation, Integer> getNetherOreWeights() {
		return Collections.unmodifiableMap(legacyNetherOreWeights);
	}

	@Override
	public void registerOreWeight(ResourceLocation tag, int weight) {
		legacyOreWeights.put(tag, weight);
	}

	@Override
	public void registerNetherOreWeight(ResourceLocation tag, int weight) {
		legacyNetherOreWeights.put(tag, weight);
	}

	@Override
	public Map<ResourceLocation, Function<DyeColor, Block>> getPaintableBlocks() {
		return Collections.unmodifiableMap(paintableBlocks);
	}

	@Override
	public void registerPaintableBlock(ResourceLocation block, Function<DyeColor, Block> transformer) {
		paintableBlocks.put(block, transformer);
	}

	@Override
	public void registerCorporeaNodeDetector(ICorporeaNodeDetector detector) {
		CorporeaNodeDetectors.register(detector);
	}
}
