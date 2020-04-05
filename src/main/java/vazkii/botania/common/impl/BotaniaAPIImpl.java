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
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IRegistryDelegate;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.internal.IInternalMethodHandler;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.core.handler.InternalMethodHandler;
import vazkii.botania.common.core.handler.PixieHandler;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class BotaniaAPIImpl implements BotaniaAPI {
	private static final LazyValue<Rarity> RELIC_RARITY = new LazyValue<>(() -> Rarity.create("RELIC", TextFormatting.GOLD));

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
		return 1;
	}

	@Override
	public IForgeRegistry<Brew> getBrewRegistry() {
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

	private final InternalMethodHandler tmp = new InternalMethodHandler();

	@Override
	public IInternalMethodHandler internalHandler() {
		return tmp;
	}

	@Override
	public IAttribute getPixieSpawnChanceAttribute() {
		return PixieHandler.PIXIE_SPAWN_CHANCE;
	}

	public Map<ResourceLocation, Integer> oreWeights = Collections.emptyMap();
	public Map<ResourceLocation, Integer> netherOreWeights = Collections.emptyMap();
	private Map<IRegistryDelegate<Block>, Function<DyeColor, Block>> paintableBlocks = new ConcurrentHashMap<>();

	@Override
	public Map<ResourceLocation, Integer> getOreWeights() {
		return Collections.unmodifiableMap(oreWeights);
	}

	@Override
	public Map<ResourceLocation, Integer> getNetherOreWeights() {
		return Collections.unmodifiableMap(netherOreWeights);
	}

	@Override
	public Map<IRegistryDelegate<Block>, Function<DyeColor, Block>> getPaintableBlocks() {
		return Collections.unmodifiableMap(paintableBlocks);
	}

	@Override
	public void registerPaintableBlock(IRegistryDelegate<Block> block, Function<DyeColor, Block> transformer) {
		paintableBlocks.put(block, transformer);
	}
}
