/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity.flower.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.api.recipe.PureDaisyRecipe;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.crafting.BlockStateRecipe;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;

import java.util.Arrays;

public class PureDaisyBlockEntity extends SpecialFlowerBlockEntity {
	private static final String TAG_POSITION = "position";
	private static final String TAG_TICKS_REMAINING = "ticksRemaining";
	private static final String TAG_TICKED_POSITIONS = "tickedPositions";
	private static final int RECIPE_COMPLETE_EVENT = 0;

	private static final BlockPos[] POSITIONS = {
			new BlockPos(-1, 0, -1),
			new BlockPos(-1, 0, 0),
			new BlockPos(-1, 0, 1),
			new BlockPos(0, 0, 1),
			new BlockPos(1, 0, 1),
			new BlockPos(1, 0, 0),
			new BlockPos(1, 0, -1),
			new BlockPos(0, 0, -1),
	};

	private int positionAt = 0;
	private final int[] prevTicksRemaining = new int[POSITIONS.length];
	private final int[] ticksRemaining = new int[POSITIONS.length];
	private byte prevTickedPositions = 0;

	public PureDaisyBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.PURE_DAISY, pos, state);
		Arrays.fill(prevTicksRemaining, -1);
		Arrays.fill(ticksRemaining, -1);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (level.isClientSide()) {
			BlockPos.MutableBlockPos coords = new BlockPos.MutableBlockPos();
			RandomSource rng = level.getRandom();
			for (int i = 0; i < POSITIONS.length; i++) {
				if (ticksRemaining[i] > 0) {
					coords.setWithOffset(getEffectivePos(), POSITIONS[i]);
					SparkleParticleData data = SparkleParticleData.sparkle(rng.nextFloat(), 1F, 1F, 1F, 5);
					level.addAlwaysVisibleParticle(data,
							coords.getX() + rng.nextDouble(),
							coords.getY() + rng.nextDouble(),
							coords.getZ() + rng.nextDouble(),
							0, 0, 0);
				}
			}

			return;
		}

		positionAt++;
		if (positionAt == POSITIONS.length) {
			positionAt = 0;
		}

		BlockPos coords = getEffectivePos().offset(POSITIONS[positionAt]);
		if (!level.isEmptyBlock(coords)) {
			level.getProfiler().push("findRecipe");
			PureDaisyRecipe recipe = findRecipe(coords);
			level.getProfiler().pop();

			if (recipe != null) {
				if (ticksRemaining[positionAt] == -1) {
					ticksRemaining[positionAt] = recipe.getTime();
				}

				ticksRemaining[positionAt]--;

				if (ticksRemaining[positionAt] <= 0) {
					ticksRemaining[positionAt] = -1;

					BlockState recipeOutputState = recipe.getOutput().pick(level.getRandom());
					BlockState stateToPlace;
					if (recipe.isCopyInputProperties()) {
						BlockState stateToReplace = level.getBlockState(coords);
						stateToPlace = recipeOutputState.getBlock().withPropertiesOf(stateToReplace);
					} else {
						stateToPlace = recipeOutputState;
					}
					BlockStateRecipe.replaceBlock(coords, recipe, stateToPlace, (ServerLevel) level,
							() -> level.blockEvent(getBlockPos(), getBlockState().getBlock(), RECIPE_COMPLETE_EVENT, positionAt));
				}

			} else {
				ticksRemaining[positionAt] = -1;
			}
		} else {
			ticksRemaining[positionAt] = -1;
		}

		if (!Arrays.equals(ticksRemaining, prevTicksRemaining)) {
			markForPersisting();
			System.arraycopy(ticksRemaining, 0, prevTicksRemaining, 0, POSITIONS.length);
		}
		byte tickedPositions = getTickedPositionBits();
		if (prevTickedPositions != tickedPositions) {
			markForImmediateSync();
			prevTickedPositions = tickedPositions;
		}
	}

	@Nullable
	private PureDaisyRecipe findRecipe(BlockPos coords) {
		BlockState state = getLevel().getBlockState(coords);

		for (RecipeHolder<PureDaisyRecipe> recipe : level.getRecipeManager().getAllRecipesFor(
				BotaniaRecipeTypes.PURE_DAISY_TYPE)) {
			if (recipe.value() instanceof PureDaisyRecipe daisyRecipe && daisyRecipe.matches(getLevel(), coords, state)) {
				return daisyRecipe;
			}
		}

		return null;
	}

	@Override
	public boolean triggerEvent(int type, int param) {
		if (type == RECIPE_COMPLETE_EVENT) {
			if (getLevel().isClientSide()) {
				BlockPos coords = getEffectivePos().offset(POSITIONS[param]);
				for (int i = 0; i < 25; i++) {
					double x = coords.getX() + Math.random();
					double y = coords.getY() + Math.random() + 0.5;
					double z = coords.getZ() + Math.random();

					WispParticleData data = WispParticleData.wisp((float) Math.random() / 2F, 1, 1, 1);
					getLevel().addParticle(data, x, y, z, 0, 0, 0);
				}
			}

			return true;
		}
		return super.triggerEvent(type, param);
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), 1);
	}

	@Override
	protected void loadAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		super.loadAdditional(cmp, registries);
		positionAt = cmp.getInt(TAG_POSITION);

		if (level != null && level.isClientSide() && cmp.contains(TAG_TICKED_POSITIONS)) {
			byte tickedPositionBits = cmp.getByte(TAG_TICKED_POSITIONS);
			for (int i = 0; i < 8; i++) {
				ticksRemaining[i] = (tickedPositionBits >>> i) & 1;
			}
		} else {
			for (int i = 0; i < ticksRemaining.length; i++) {
				ticksRemaining[i] = cmp.getInt(TAG_TICKS_REMAINING + i);
			}
		}
	}

	@Override
	protected void saveAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		super.saveAdditional(cmp, registries);
		cmp.putInt(TAG_POSITION, positionAt);
		for (int i = 0; i < ticksRemaining.length; i++) {
			cmp.putInt(TAG_TICKS_REMAINING + i, ticksRemaining[i]);
		}
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		var tag = super.getUpdateTag(registries);
		byte tickedPositionBits = getTickedPositionBits();
		tag.putByte(TAG_TICKED_POSITIONS, tickedPositionBits);
		return tag;
	}

	private byte getTickedPositionBits() {
		byte tickedPositionBits = 0;
		for (int i = 0; i < 8; i++) {
			if (ticksRemaining[i] > 0) {
				tickedPositionBits |= (byte) (1 << i);
			}
		}
		return tickedPositionBits;
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
}
