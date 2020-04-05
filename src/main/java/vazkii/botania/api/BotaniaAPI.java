/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IRegistryDelegate;

import org.apache.logging.log4j.LogManager;

import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.internal.DummyMethodHandler;
import vazkii.botania.api.internal.IInternalMethodHandler;

import javax.annotation.Nonnull;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

public interface BotaniaAPI {
	LazyValue<BotaniaAPI> INSTANCE = new LazyValue<>(() -> {
		try {
			return (BotaniaAPI) Class.forName("vazkii.botania.common.impl.BotaniaAPIImpl").newInstance();
		} catch (ReflectiveOperationException e) {
			LogManager.getLogger().warn("Unable to find BotaniaAPIImpl, using a dummy");
			return new BotaniaAPI() {};
		}
	});

	static BotaniaAPI instance() {
		return INSTANCE.getValue();
	}

	/**
	 * @return A unique version number for this version of the API. When anything is added, this number will be
	 *         incremented
	 */
	default int apiVersion() {
		return 0;
	}

	default IForgeRegistry<Brew> getBrewRegistry() {
		return null;
	}

	default Map<ResourceLocation, Integer> getOreWeights() {
		return Collections.emptyMap();
	}

	default Map<ResourceLocation, Integer> getNetherOreWeights() {
		return Collections.emptyMap();
	}

	default Map<IRegistryDelegate<Block>, Function<DyeColor, Block>> getPaintableBlocks() {
		return Collections.emptyMap();
	}

	default void registerPaintableBlock(Block block, Function<DyeColor, Block> transformer) {
		registerPaintableBlock(block.delegate, transformer);
	}

	/**
	 * Make Botania aware of how to transform between different colors of a block, for use in the paint lens.
	 * This method can be safely called during parallel mod initialization
	 * 
	 * @param block       The block
	 * @param transformer Function from color to a new block
	 */
	default void registerPaintableBlock(IRegistryDelegate<Block> block, Function<DyeColor, Block> transformer) {

	}

	IArmorMaterial DUMMY_ARMOR_MATERIAL = new IArmorMaterial() {
		@Override
		public int getDurability(@Nonnull EquipmentSlotType slot) {
			return 0;
		}

		@Override
		public int getDamageReductionAmount(@Nonnull EquipmentSlotType slot) {
			return 0;
		}

		@Override
		public int getEnchantability() {
			return 0;
		}

		@Nonnull
		@Override
		public SoundEvent getSoundEvent() {
			return SoundEvents.ITEM_ARMOR_EQUIP_LEATHER;
		}

		@Nonnull
		@Override
		public Ingredient getRepairMaterial() {
			return Ingredient.EMPTY;
		}

		@Override
		public String getName() {
			return "missingno";
		}

		@Override
		public float getToughness() {
			return 0;
		}
	};

	IItemTier DUMMY_ITEM_TIER = new IItemTier() {
		@Override
		public int getMaxUses() {
			return 0;
		}

		@Override
		public float getEfficiency() {
			return 0;
		}

		@Override
		public float getAttackDamage() {
			return 0;
		}

		@Override
		public int getHarvestLevel() {
			return 0;
		}

		@Override
		public int getEnchantability() {
			return 0;
		}

		@Nonnull
		@Override
		public Ingredient getRepairMaterial() {
			return Ingredient.EMPTY;
		}
	};

	default IArmorMaterial getManasteelArmorMaterial() {
		return DUMMY_ARMOR_MATERIAL;
	}

	default IArmorMaterial getElementiumArmorMaterial() {
		return DUMMY_ARMOR_MATERIAL;
	}

	default IArmorMaterial getManaweaveArmorMaterial() {
		return DUMMY_ARMOR_MATERIAL;
	}

	default IArmorMaterial getTerrasteelArmorMaterial() {
		return DUMMY_ARMOR_MATERIAL;
	}

	default IItemTier getManasteelItemTier() {
		return DUMMY_ITEM_TIER;
	}

	default IItemTier getElementiumItemTier() {
		return DUMMY_ITEM_TIER;
	}

	default IItemTier getTerrasteelItemTier() {
		return DUMMY_ITEM_TIER;
	}

	default Rarity getRelicRarity() {
		return Rarity.EPIC;
	}

	// todo 1.15 combine with this and BotaniaAPIClient
	default IInternalMethodHandler internalHandler() {
		return new DummyMethodHandler();
	}

	default IAttribute getPixieSpawnChanceAttribute() {
		return new RangedAttribute(null, "missingno", 0, 0, 0);
	}
}
