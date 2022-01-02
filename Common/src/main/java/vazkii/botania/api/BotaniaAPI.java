/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api;

import com.google.common.base.Suppliers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.corporea.ICorporeaNodeDetector;
import vazkii.botania.api.internal.DummyManaNetwork;
import vazkii.botania.api.internal.IManaNetwork;

import javax.annotation.Nonnull;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public interface BotaniaAPI {
	String MODID = "botania";
	String GOG_MODID = "gardenofglass";
	Logger LOGGER = LogManager.getLogger(MODID);

	Supplier<BotaniaAPI> INSTANCE = Suppliers.memoize(() -> {
		try {
			return (BotaniaAPI) Class.forName("vazkii.botania.common.impl.BotaniaAPIImpl")
					.getDeclaredConstructor().newInstance();
		} catch (ReflectiveOperationException e) {
			LogManager.getLogger().warn("Unable to find BotaniaAPIImpl, using a dummy");
			return new BotaniaAPI() {};
		}
	});

	static BotaniaAPI instance() {
		return INSTANCE.get();
	}

	/**
	 * @return A unique version number for this version of the API. When anything is added, this number will be
	 *         incremented
	 */
	default int apiVersion() {
		return 0;
	}

	/**
	 * Get the registry for brews.
	 * Forge documentation: This is purely a read-only wrapper. Register brews using the registry events.
	 */
	default Registry<Brew> getBrewRegistry() {
		return null;
	}

	default Map<ResourceLocation, Function<DyeColor, Block>> getPaintableBlocks() {
		return Collections.emptyMap();
	}

	default void registerPaintableBlock(Block block, Function<DyeColor, Block> transformer) {
		registerPaintableBlock(Registry.BLOCK.getKey(block), transformer);
	}

	/**
	 * Make Botania aware of how to transform between different colors of a block, for use in the paint lens.
	 * This method can be safely called during parallel mod initialization
	 * 
	 * @param blockId     The block ID
	 * @param transformer Function from color to a new block
	 */
	default void registerPaintableBlock(ResourceLocation blockId, Function<DyeColor, Block> transformer) {

	}

	ArmorMaterial DUMMY_ARMOR_MATERIAL = new ArmorMaterial() {
		@Override
		public int getDurabilityForSlot(@Nonnull EquipmentSlot slot) {
			return 0;
		}

		@Override
		public int getDefenseForSlot(@Nonnull EquipmentSlot slot) {
			return 0;
		}

		@Override
		public int getEnchantmentValue() {
			return 0;
		}

		@Nonnull
		@Override
		public SoundEvent getEquipSound() {
			return SoundEvents.ARMOR_EQUIP_LEATHER;
		}

		@Nonnull
		@Override
		public Ingredient getRepairIngredient() {
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

		@Override
		public float getKnockbackResistance() {
			return 0;
		}
	};

	Tier DUMMY_ITEM_TIER = new Tier() {
		@Override
		public int getUses() {
			return 0;
		}

		@Override
		public float getSpeed() {
			return 0;
		}

		@Override
		public float getAttackDamageBonus() {
			return 0;
		}

		@Override
		public int getLevel() {
			return 0;
		}

		@Override
		public int getEnchantmentValue() {
			return 0;
		}

		@Nonnull
		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.EMPTY;
		}
	};

	default ArmorMaterial getManasteelArmorMaterial() {
		return DUMMY_ARMOR_MATERIAL;
	}

	default ArmorMaterial getElementiumArmorMaterial() {
		return DUMMY_ARMOR_MATERIAL;
	}

	default ArmorMaterial getManaweaveArmorMaterial() {
		return DUMMY_ARMOR_MATERIAL;
	}

	default ArmorMaterial getTerrasteelArmorMaterial() {
		return DUMMY_ARMOR_MATERIAL;
	}

	default Tier getManasteelItemTier() {
		return DUMMY_ITEM_TIER;
	}

	default Tier getElementiumItemTier() {
		return DUMMY_ITEM_TIER;
	}

	default Tier getTerrasteelItemTier() {
		return DUMMY_ITEM_TIER;
	}

	default Rarity getRelicRarity() {
		return Rarity.EPIC;
	}

	default IManaNetwork getManaNetworkInstance() {
		return DummyManaNetwork.instance;
	}

	/**
	 * @return How many ticks a passive flower can have before it decays
	 */
	default int getPassiveFlowerDecay() {
		return 0;
	}

	default Container getAccessoriesInventory(Player player) {
		return new SimpleContainer(0);
	}

	/**
	 * Break all the blocks the given player has selected with the loki ring.
	 * The item passed must implement {@link vazkii.botania.api.item.ISequentialBreaker}.
	 */
	default void breakOnAllCursors(Player player, ItemStack stack, BlockPos pos, Direction side) {}

	default boolean hasSolegnoliaAround(Entity e) {
		return false;
	}

	default void sparkleFX(Level world, double x, double y, double z, float r, float g, float b, float size, int m) {}

	default void registerCorporeaNodeDetector(ICorporeaNodeDetector detector) {

	}
}
