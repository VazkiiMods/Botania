/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.capability.BlockApiNoContext;

import java.util.Collection;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

/**
 * A block entity, usually some sort of spawner, that can be picked up and placed down again with a Life Aggregator.
 */
public interface LifeAggregatorCarryable {

	ResourceLocation ID = botaniaRL("life_aggregator_carryable");
	BlockApiNoContext<LifeAggregatorCarryable> LOOKUP = new BlockApiNoContext<>(ID, LifeAggregatorCarryable.class);

	/**
	 * Get the block type to store
	 */
	Block getBlockType();

	/**
	 * Gather all relevant data of the block entity into a {@link CustomData} instance.
	 * This data will later be used to restore the block entity when placing it down.
	 *
	 * @implNote Return <code>null</code> if the block doesn't have a block entity to restore data to.
	 */
	@Nullable
	CustomData gatherBlockEntityData(HolderLookup.Provider providers);

	/**
	 * Get the entity type to display.
	 * 
	 * @implNote Return <code>null</code> if there is no good way to represent the spawner's intended output with a
	 *           single entity type.
	 */
	@Nullable
	EntityType<?> getEntityType();

	/**
	 * Optional block state information in the form of {@link BlockItemStateProperties} for restoring the block state.
	 * 
	 * @implNote Prefer returning <code>null</code> over returning an empty instance if the block has no state that
	 *           needs restoring. Also don't return any placement-specific state data unless it explicitly needs to be
	 *           set to a non-standard value.
	 */
	@Nullable
	default BlockItemStateProperties gatherBlockStateData() {
		return null;
	}

	/**
	 * Called on the newly placed block after transferring block state and block entity data, and before the Life
	 * Aggregator item is discarded. Only called server-side.
	 * 
	 * @param context The context used to place the block. The item stack will refer to the Life Aggregator item.
	 */
	default void updateAfterPlacement(UseOnContext context) {}

	/**
	 * Specialized version of the {@link LifeAggregatorCarryable} interface for implementations that just dump block
	 * entity data. This preset defaults to storing all NBT data of the block's entity and all property values of the
	 * block's state.
	 * 
	 * @param <T> The BlockEntity type.
	 */
	interface LifeAggregatorCarryableBlockEntity<T extends BlockEntity> extends LifeAggregatorCarryable {
		T blockEntity();

		@Override
		default Block getBlockType() {
			return blockEntity().getBlockState().getBlock();
		}

		@Override
		default CustomData gatherBlockEntityData(HolderLookup.Provider providers) {
			return CustomData.of(blockEntity().saveWithId(providers));
		}

		@Nullable
		@Override
		default BlockItemStateProperties gatherBlockStateData() {
			BlockState state = blockEntity().getBlockState();
			Collection<Property<?>> properties = state.getProperties();
			if (properties.isEmpty()) {
				return null;
			}

			BlockItemStateProperties propertyValues = BlockItemStateProperties.EMPTY;
			for (Property<?> property : properties) {
				propertyValues = propertyValues.with(property, state);
			}

			return propertyValues;
		}
	}
}
