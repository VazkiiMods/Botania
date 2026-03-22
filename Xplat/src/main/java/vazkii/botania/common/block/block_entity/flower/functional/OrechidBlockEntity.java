/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity.flower.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.api.recipe.OrechidRecipe;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.crafting.BlockStateRecipe;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.handler.OrechidManager;
import vazkii.botania.common.helper.MathHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class OrechidBlockEntity extends FunctionalFlowerBlockEntity {
	public static final int DEFAULT_COST = 17500;
	public static final int DEFAULT_COST_GOG = 700;
	public static final int DEFAULT_DELAY = 100;
	public static final int DELAY_GOG = 2;
	private static final int RANGE = 5;
	private static final int RANGE_Y = 3;
	public static final String TAG_COOLDOWN = "cooldown";

	private int cooldown = 0;

	protected OrechidBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public OrechidBlockEntity(BlockPos pos, BlockState state) {
		this(BotaniaBlockEntities.ORECHID, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide || !canOperate()) {
			return;
		}

		if (cooldown > 0) {
			cooldown -= getOvergrowthFactor();
			setChanged();
			return;
		}

		if (isPowered() || !shouldUpdateThisTick()
				|| getMana() < OrechidManager.getMinManaCost(level, getRecipeType())) {
			return;
		}

		BlockPos coords = getCoordsToPut();
		if (coords == null) {
			return;
		}
		OrechidRecipe recipe = findMatchingRecipe(coords);
		if (recipe == null) {
			return;
		}
		if (recipe.getManaCost() <= getMana()) {
			trySetRecipe(coords, recipe);
		}
		cooldown = recipe.getCooldown();
	}

	protected void playSound(BlockPos coords) {
		getLevel().playSound(null, coords, BotaniaSounds.orechid, SoundSource.BLOCKS, 1F, 1F);
	}

	@Nullable
	private OrechidRecipe findMatchingRecipe(BlockPos coords) {
		BlockState input = getLevel().getBlockState(coords);
		List<WeightedEntry.Wrapper<OrechidRecipe>> values = new ArrayList<>();
		for (OrechidRecipe recipe : OrechidManager.getMatchingRecipes(getLevel().getRecipeManager(), getRecipeType(), input)) {
			values.add(WeightedEntry.wrap(recipe, recipe.getWeight(getLevel(), coords)));
		}
		return WeightedRandom.getRandomItem(getLevel().random, values)
				.map(WeightedEntry.Wrapper::data)
				.orElse(null);
	}

	private void trySetRecipe(BlockPos coords, OrechidRecipe recipe) {
		BlockState stateToPlace = recipe.getOutput(level, coords).pick(level.random);

		BlockStateRecipe.replaceBlock(coords, recipe, stateToPlace, (ServerLevel) level, () -> {
			playSound(coords);
			addMana(-recipe.getManaCost());
			sync();
		});
	}

	@Nullable
	private BlockPos getCoordsToPut() {
		List<BlockPos> possibleCoords = new ArrayList<>();
		var matcher = getReplaceMatcher();
		for (BlockPos pos : MathHelper.aroundPosClosed(getEffectivePos(), getRange(), getRangeY())) {
			BlockState state = getLevel().getBlockState(pos);
			if (matcher.test(state)) {
				possibleCoords.add(pos.immutable());
			}
		}

		if (possibleCoords.isEmpty()) {
			return null;
		}
		return possibleCoords.get(getLevel().random.nextInt(possibleCoords.size()));
	}

	public boolean canOperate() {
		return true;
	}

	public RecipeType<? extends OrechidRecipe> getRecipeType() {
		return BotaniaRecipeTypes.ORECHID_TYPE;
	}

	public Predicate<BlockState> getReplaceMatcher() {
		return state -> !OrechidManager.getMatchingRecipes(
				this.getLevel().getRecipeManager(),
				this.getRecipeType(),
				state
		).isEmpty();
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		super.writeToPacketNBT(cmp, registries);

		cmp.putInt(TAG_COOLDOWN, cooldown);
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		super.readFromPacketNBT(cmp, registries);

		cooldown = cmp.getInt(TAG_COOLDOWN);
	}

	@Override
	public int getUpdateInterval() {
		return 2;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), getRange());
	}

	public int getRange() {
		return RANGE;
	}

	public int getRangeY() {
		return RANGE_Y;
	}

	@Override
	public int getColor() {
		return 0x818181;
	}

	@Override
	public int getMaxMana() {
		return Math.max(getMana(), OrechidManager.getMaxManaCost(getLevel(), getRecipeType()));
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder components) {
		if (cooldown > 0) {
			components.set(BotaniaDataComponents.COOLDOWN, cooldown);
		}
	}

	@Override
	protected void applyImplicitComponents(DataComponentInput componentInput) {
		cooldown = componentInput.getOrDefault(BotaniaDataComponents.COOLDOWN, 0);
	}
}
