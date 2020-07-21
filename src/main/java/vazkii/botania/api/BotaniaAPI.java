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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.EmptyHandler;

import org.apache.logging.log4j.LogManager;

import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.corporea.IWrappedInventory;
import vazkii.botania.api.corporea.InvWithLocation;
import vazkii.botania.api.internal.DummyManaNetwork;
import vazkii.botania.api.internal.IManaNetwork;

import javax.annotation.Nonnull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface BotaniaAPI {
	String MODID = "botania";

	Lazy<BotaniaAPI> INSTANCE = new Lazy<>(() -> {
		try {
			return (BotaniaAPI) Class.forName("vazkii.botania.common.impl.BotaniaAPIImpl").newInstance();
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

	default Map<Identifier, Integer> getOreWeights() {
		return Collections.emptyMap();
	}

	default Map<Identifier, Integer> getNetherOreWeights() {
		return Collections.emptyMap();
	}

	/**
	 * Register ore to be produced by the Orechid
	 * 
	 * @param tag    Block tag ID containing the ores to register
	 * @param weight Relative weight of tis entry
	 */
	default void registerOreWeight(Identifier tag, int weight) {

	}

	/**
	 * Register ore to be produced by the Orechid Ignem
	 * 
	 * @see #registerOreWeight
	 */
	default void registerNetherOreWeight(Identifier tag, int weight) {

	}

	default Map<Identifier, Function<DyeColor, Block>> getPaintableBlocks() {
		return Collections.emptyMap();
	}

	default void registerPaintableBlock(Block block, Function<DyeColor, Block> transformer) {
		registerPaintableBlock(Registry.BLOCK.getId(block), transformer);
	}

	/**
	 * Make Botania aware of how to transform between different colors of a block, for use in the paint lens.
	 * This method can be safely called during parallel mod initialization
	 * 
	 * @param blockId     The block ID
	 * @param transformer Function from color to a new block
	 */
	default void registerPaintableBlock(Identifier blockId, Function<DyeColor, Block> transformer) {

	}

	ArmorMaterial DUMMY_ARMOR_MATERIAL = new ArmorMaterial() {
		@Override
		public int getDurability(@Nonnull EquipmentSlot slot) {
			return 0;
		}

		@Override
		public int getProtectionAmount(@Nonnull EquipmentSlot slot) {
			return 0;
		}

		@Override
		public int getEnchantability() {
			return 0;
		}

		@Nonnull
		@Override
		public SoundEvent getEquipSound() {
			return SoundEvents.ITEM_ARMOR_EQUIP_LEATHER;
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

	ToolMaterial DUMMY_ITEM_TIER = new ToolMaterial() {
		@Override
		public int getDurability() {
			return 0;
		}

		@Override
		public float getMiningSpeedMultiplier() {
			return 0;
		}

		@Override
		public float getAttackDamage() {
			return 0;
		}

		@Override
		public int getMiningLevel() {
			return 0;
		}

		@Override
		public int getEnchantability() {
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

	default ToolMaterial getManasteelItemTier() {
		return DUMMY_ITEM_TIER;
	}

	default ToolMaterial getElementiumItemTier() {
		return DUMMY_ITEM_TIER;
	}

	default ToolMaterial getTerrasteelItemTier() {
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

	default Inventory getAccessoriesInventory(PlayerEntity player) {
		return new SimpleInventory(0);
	}

	/**
	 * Break all the blocks the given player has selected with the loki ring.
	 * The item passed must implement {@link vazkii.botania.api.item.ISequentialBreaker}.
	 */
	default void breakOnAllCursors(PlayerEntity player, ItemStack stack, BlockPos pos, Direction side) {}

	default boolean hasSolegnoliaAround(Entity e) {
		return false;
	}

	default void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float size, int m) {}

	/**
	 * See config value "flower.forceCheck" for more information
	 */
	default boolean shouldForceCheck() {
		return false;
	}

	// todo 1.16 reevaluate this
	default List<IWrappedInventory> wrapInventory(List<InvWithLocation> inventories) {
		return Collections.emptyList();
	}
}
