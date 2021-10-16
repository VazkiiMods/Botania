/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.impl;

import com.google.common.base.Suppliers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.block.IHornHarvestable;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.corporea.ICorporeaNodeDetector;
import vazkii.botania.api.internal.IManaNetwork;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.subtile.functional.SubTileSolegnolia;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.handler.ManaNetworkHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.integration.corporea.CorporeaNodeDetectors;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.relic.ItemLokiRing;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class BotaniaAPIImpl implements BotaniaAPI {

	private static final Supplier<Rarity> RELIC_RARITY = Suppliers.memoize(() -> Rarity.EPIC);

	private enum ArmorMaterial implements net.minecraft.world.item.ArmorMaterial {
		MANASTEEL("manasteel", 16, new int[] { 2, 5, 6, 2 }, 18, () -> ModSounds.equipManasteel, () -> ModItems.manaSteel, 0),
		MANAWEAVE("manaweave", 5, new int[] { 1, 2, 2, 1 }, 18, () -> ModSounds.equipManaweave, () -> ModItems.manaweaveCloth, 0),
		ELEMENTIUM("elementium", 18, new int[] { 2, 5, 6, 2 }, 18, () -> ModSounds.equipElementium, () -> ModItems.elementium, 0),
		TERRASTEEL("terrasteel", 34, new int[] { 3, 6, 8, 3 }, 26, () -> ModSounds.equipTerrasteel, () -> ModItems.terrasteel, 3);

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
		public int getDurabilityForSlot(EquipmentSlot slot) {
			return durabilityMultiplier * MAX_DAMAGE_ARRAY[slot.getIndex()];
		}

		@Override
		public int getDefenseForSlot(EquipmentSlot slot) {
			return damageReduction[slot.getIndex()];
		}

		@Override
		public int getEnchantmentValue() {
			return enchantability;
		}

		@Nonnull
		@Override
		public SoundEvent getEquipSound() {
			return equipSound.get();
		}

		@Nonnull
		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.of(repairItem.get());
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

	private enum ItemTier implements Tier {
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
		public int getUses() {
			return maxUses;
		}

		@Override
		public float getSpeed() {
			return efficiency;
		}

		@Override
		public float getAttackDamageBonus() {
			return attackDamage;
		}

		@Override
		public int getLevel() {
			return harvestLevel;
		}

		@Override
		public int getEnchantmentValue() {
			return enchantability;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.of(repairItem.get());
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
	public net.minecraft.world.item.ArmorMaterial getManasteelArmorMaterial() {
		return ArmorMaterial.MANASTEEL;
	}

	@Override
	public net.minecraft.world.item.ArmorMaterial getElementiumArmorMaterial() {
		return ArmorMaterial.ELEMENTIUM;
	}

	@Override
	public net.minecraft.world.item.ArmorMaterial getManaweaveArmorMaterial() {
		return ArmorMaterial.MANAWEAVE;
	}

	@Override
	public net.minecraft.world.item.ArmorMaterial getTerrasteelArmorMaterial() {
		return ArmorMaterial.TERRASTEEL;
	}

	@Override
	public Tier getManasteelItemTier() {
		return ItemTier.MANASTEEL;
	}

	@Override
	public Tier getElementiumItemTier() {
		return ItemTier.ELEMENTIUM;
	}

	@Override
	public Tier getTerrasteelItemTier() {
		return ItemTier.TERRASTEEL;
	}

	@Override
	public Rarity getRelicRarity() {
		return RELIC_RARITY.get();
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
	public Container getAccessoriesInventory(Player player) {
		return EquipmentHandler.getAllWorn(player);
	}

	@Override
	public void breakOnAllCursors(Player player, ItemStack stack, BlockPos pos, Direction side) {
		ItemLokiRing.breakOnAllCursors(player, stack, pos, side);
	}

	@Override
	public boolean hasSolegnoliaAround(Entity e) {
		return SubTileSolegnolia.hasSolegnoliaAround(e);
	}

	@Override
	public void sparkleFX(Level world, double x, double y, double z, float r, float g, float b, float size, int m) {
		SparkleParticleData data = SparkleParticleData.sparkle(size, r, g, b, m);
		world.addParticle(data, x, y, z, 0, 0, 0);
	}

	@Override
	public boolean shouldForceCheck() {
		return ConfigHandler.COMMON.flowerForceCheck.getValue();
	}

	private final Map<ResourceLocation, Function<DyeColor, Block>> paintableBlocks = new ConcurrentHashMap<>();
	private final Map<ResourceLocation, IHornHarvestable> hornHarvestableBlocks = new ConcurrentHashMap<>();

	@Override
	public Map<ResourceLocation, Function<DyeColor, Block>> getPaintableBlocks() {
		return Collections.unmodifiableMap(paintableBlocks);
	}

	@Override
	public void registerPaintableBlock(ResourceLocation block, Function<DyeColor, Block> transformer) {
		paintableBlocks.put(block, transformer);
	}

	@Override
	public Optional<IHornHarvestable> getHornHarvestable(Block block) {
		if (block instanceof IHornHarvestable) {
			// callers of this method on blocks which still implement the interface should still receive the instance
			return Optional.of((IHornHarvestable) block);
		}
		return Optional.ofNullable(hornHarvestableBlocks.getOrDefault(Registry.BLOCK.getKey(block), null));
	}

	@Override
	public void registerHornHarvestableBlock(ResourceLocation block, IHornHarvestable harvestable) {
		hornHarvestableBlocks.put(block, harvestable);
	}

	@Override
	public void registerCorporeaNodeDetector(ICorporeaNodeDetector detector) {
		CorporeaNodeDetectors.register(detector);
	}
}
