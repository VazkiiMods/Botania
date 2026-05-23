/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity;

import com.google.common.base.Suppliers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.api.mana.spark.SparkAttachable;
import vazkii.botania.api.mana.spark.SparkHelper;
import vazkii.botania.api.recipe.ProcessingRecipeInput;
import vazkii.botania.api.recipe.TerrestrialAgglomerationRecipe;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.TerraPlateState;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.crafting.recipe.RecipeUtils;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.proxy.Proxy;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class TerrestrialAgglomerationPlateBlockEntity extends BlockEntity implements SparkAttachable, ManaReceiver {
	public static final Supplier<IMultiblock> MULTIBLOCK = Suppliers.memoize(() -> PatchouliAPI.get().makeMultiblock(
			new String[][] {
					{
							"___",
							"_P_",
							"___"
					},
					{
							"RLR",
							"L0L",
							"RLR"
					}
			},
			'P', BotaniaBlocks.terraPlate,
			'R', PatchouliAPI.get().tagMatcher(BotaniaTags.Blocks.TERRA_PLATE_BASE),
			'0', PatchouliAPI.get().tagMatcher(BotaniaTags.Blocks.TERRA_PLATE_BASE),
			'L', PatchouliAPI.get().tagMatcher(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/lapis")))
	));
	public static final int BLOCK_EVENT_PROGRESS_UPDATE = 0;
	public static final int BLOCK_EVENT_CRAFTING_EFFECT = 1;
	public static final int BLOCK_EVENT_CRAFTING_ABORTED = 2;

	private static final String TAG_MANA = "mana";
	private static final String TAG_MANA_TO_GET = "mana_to_get";

	private int mana;
	private int manaToGet;
	private int currentProgress = -1;
	private long lastProgressTick;
	private final RecipeManager.CachedCheck<ProcessingRecipeInput, TerrestrialAgglomerationRecipe> cachedCheck = RecipeManager.createCheck(BotaniaRecipeTypes.TERRA_PLATE_TYPE);

	public TerrestrialAgglomerationPlateBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.TERRA_PLATE, pos, state);
	}

	public static void serverCollectingTick(Level level, BlockPos pos, BlockState state,
			TerrestrialAgglomerationPlateBlockEntity self) {
		if (!self.hasValidPlatform()) {
			self.stopCollecting();
			return;
		}
		List<ItemEntity> itemEntities = self.getItemEntities();
		ProcessingRecipeInput recipeInput = RecipeUtils.getInputFromEntities(itemEntities);
		Optional<RecipeHolder<TerrestrialAgglomerationRecipe>> recipeHolderOptional = self.cachedCheck.getRecipeFor(recipeInput, level);
		if (recipeHolderOptional.isEmpty()) {
			self.stopCollecting();
			return;
		}
		RecipeHolder<TerrestrialAgglomerationRecipe> recipeHolder = recipeHolderOptional.get();
		TerrestrialAgglomerationRecipe recipe = recipeHolder.value();
		if (self.manaToGet != recipe.getMana()) {
			// recipe changed on-the-fly somehow?
			self.manaToGet = recipe.getMana();
			self.setChanged();
		}
		ManaSpark spark = SparkAttachable.getAttachedSpark(level, pos);
		SparkHelper.registerTransferFromSparksAround(spark, level, pos);
		if (self.mana > 0) {
			int newProgress = 100 * self.mana / self.manaToGet;
			if (newProgress != self.currentProgress || level.getGameTime() - self.lastProgressTick > 10) {
				self.currentProgress = newProgress;
				level.blockEvent(pos, state.getBlock(), BLOCK_EVENT_PROGRESS_UPDATE, newProgress);
			}
		}
		if (self.mana < self.manaToGet) {
			return;
		}
		self.finishCrafting(level, pos, state, itemEntities, recipeInput, recipeHolder);
	}

	private void finishCrafting(Level level, BlockPos pos, BlockState state, List<ItemEntity> itemEntities,
			ProcessingRecipeInput recipeInput, RecipeHolder<TerrestrialAgglomerationRecipe> recipeHolder) {
		Player player = getCraftingPlayer(itemEntities);
		ItemStack result = recipeHolder.value().assemble(recipeInput, level.registryAccess());
		if (player != null) {
			player.triggerRecipeCrafted(recipeHolder, List.of(result));
			result.onCraftedBy(level, player, result.getCount());
		} else {
			result.onCraftedBySystem(level);
		}
		for (ItemEntity item : itemEntities) {
			item.discard();
		}
		ItemEntity item = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, result, 0, 0, 0);
		level.addFreshEntity(item);
		level.playSound(null, pos, BotaniaSounds.terrasteelCraft, SoundSource.BLOCKS, 1F, 1F);
		mana = 0;
		manaToGet = 0;
		level.blockEvent(pos, state.getBlock(), BLOCK_EVENT_CRAFTING_EFFECT, 0);
		level.setBlock(pos,
				state.setValue(BotaniaStateProperties.TERRA_PLATE_STATE, TerraPlateState.DONE),
				Block.UPDATE_ALL);
		level.scheduleTick(pos, state.getBlock(), 4);
	}

	private void stopCollecting() {
		manaToGet = 0;
		level.setBlock(getBlockPos(),
				getBlockState().setValue(BotaniaStateProperties.TERRA_PLATE_STATE,
						mana > 0 ? TerraPlateState.DISSIPATING : TerraPlateState.IDLE),
				Block.UPDATE_ALL);
		level.blockEvent(getBlockPos(), getBlockState().getBlock(), BLOCK_EVENT_PROGRESS_UPDATE, -1);
	}

	public static void serverDissipatingTick(Level level, BlockPos pos, BlockState state,
			TerrestrialAgglomerationPlateBlockEntity self) {
		self.receiveMana(-1000);
		if (self.mana == 0) {
			level.setBlock(pos,
					state.setValue(BotaniaStateProperties.TERRA_PLATE_STATE, TerraPlateState.IDLE),
					Block.UPDATE_ALL);
		}
	}

	public static void clientCollectingTick(Level level, BlockPos blockPos, BlockState state,
			TerrestrialAgglomerationPlateBlockEntity self) {
		if (self.currentProgress < 0 || level.getGameTime() - self.lastProgressTick > 30) {
			return;
		}

		int ticks = self.currentProgress;

		int totalSpiritCount = 3;
		double tickIncrement = 360.0 / totalSpiritCount;

		int speed = 5;
		double wticks = ticks * speed - tickIncrement;

		double radius = Math.sin((ticks - 100) / 10.0) * 2;
		double vY = Math.sin(wticks * Math.PI / 180 * 0.55) * -0.05;

		float r = 0F;
		float g = ticks / 100f;
		float b = 1f - ticks / 100f;
		Vec3 pos = blockPos.getCenter();
		RandomSource rng = level.getRandom();

		for (int i = 0; i < totalSpiritCount; i++) {
			double angle = wticks * (Math.PI / 180);
			double wx = pos.x + Math.sin(angle) * radius;
			double wy = pos.y - 0.25 + Math.abs(radius) * 0.7;
			double wz = pos.z + Math.cos(angle) * radius;

			wticks += tickIncrement;
			WispParticleData primaryData = WispParticleData.wisp(0.85f, r, g, b, 0.25f);
			Proxy.INSTANCE.addParticleForceNear(level, primaryData, wx, wy, wz, 0, vY, 0);
			WispParticleData data = WispParticleData.wisp(rng.nextFloat() * 0.1f + 0.1f, r, g, b, 0.9f);
			level.addParticle(data, wx, wy, wz,
					(rng.nextDouble() - 0.5) * 0.05,
					(rng.nextDouble() - 0.5) * 0.05,
					(rng.nextDouble() - 0.5) * 0.05);
		}
	}

	@Override
	public boolean triggerEvent(int id, int param) {
		switch (id) {
			case BLOCK_EVENT_CRAFTING_EFFECT -> {
				this.currentProgress = -1;
				if (level.isClientSide()) {
					float r = 0;
					float g = 1;
					float b = 0;
					Vec3 pos = getBlockPos().getCenter();
					RandomSource rng = level.getRandom();

					for (int j = 0; j < 15; j++) {
						WispParticleData data = WispParticleData.wisp(rng.nextFloat() * 0.15f + 0.15f, r, g, b);
						level.addParticle(data, pos.x, pos.y, pos.z,
								(rng.nextDouble() - 0.5) * 0.125,
								(rng.nextDouble() - 0.5) * 0.125,
								(rng.nextDouble() - 0.5) * 0.125);
					}
				}
				return true;
			}
			case BLOCK_EVENT_CRAFTING_ABORTED -> {
				this.currentProgress = -1;
				return true;
			}
			case BLOCK_EVENT_PROGRESS_UPDATE -> {
				this.currentProgress = param;
				this.lastProgressTick = level.getGameTime();
				// no particles (handled by clientCollectingTick)
				return true;
			}
		}
		return false;
	}

	public void tryStartProcessing() {
		if (!hasValidPlatform()) {
			return;
		}
		List<ItemEntity> itemEntities = getItemEntities();
		ProcessingRecipeInput recipeInput = RecipeUtils.getInputFromEntities(itemEntities);
		Optional<RecipeHolder<TerrestrialAgglomerationRecipe>> recipe = cachedCheck.getRecipeFor(recipeInput, level);
		if (recipe.isPresent()) {
			manaToGet = recipe.get().value().getMana();
			level.setBlock(getBlockPos(),
					getBlockState().setValue(BotaniaStateProperties.TERRA_PLATE_STATE, TerraPlateState.COLLECTING),
					Block.UPDATE_ALL);
		}
	}

	@Nullable
	private static Player getCraftingPlayer(List<ItemEntity> itemEntities) {
		Player player = null;
		int minAge = Integer.MAX_VALUE;
		for (ItemEntity entity : itemEntities) {
			if (entity.getOwner() instanceof Player owner && entity.getAge() < minAge) {
				player = owner;
				minAge = entity.getAge();
			}
		}
		return player;
	}

	private List<ItemEntity> getItemEntities() {
		return level.getEntitiesOfClass(ItemEntity.class, new AABB(worldPosition), EntitySelector.ENTITY_STILL_ALIVE);
	}

	private boolean hasValidPlatform() {
		return MULTIBLOCK.get().validate(level, getBlockPos().below()) != null;
	}

	@Override
	protected void saveAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		cmp.putInt(TAG_MANA, mana);
		cmp.putInt(TAG_MANA_TO_GET, manaToGet);
	}

	@Override
	protected void loadAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		mana = cmp.getInt(TAG_MANA);
		manaToGet = cmp.getInt(TAG_MANA_TO_GET);
	}

	@Override
	@UnknownNullability
	public Level getManaReceiverLevel() {
		return getLevel();
	}

	@Override
	public BlockPos getManaReceiverPos() {
		return getBlockPos();
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public boolean isFull() {
		return mana >= manaToGet;
	}

	@Override
	public void receiveMana(int mana) {
		this.mana = Math.max(0, this.mana + mana);
		level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
	}

	@Override
	public boolean canReceiveManaFromBursts() {
		return manaToGet > 0;
	}

	@Override
	public boolean canAttachSpark(ItemStack stack) {
		return true;
	}

	@Override
	public boolean areIncomingTransfersDone() {
		return mana >= manaToGet;
	}

	@Override
	public int getAvailableSpaceForMana() {
		return Math.max(0, manaToGet - mana);
	}

	public float getCompletion() {
		return manaToGet > 0 ? (float) mana / manaToGet : 0;
	}

	public int getComparatorLevel() {
		return manaToGet > 0
				? Math.clamp(15L * mana / manaToGet, 1, 15)
				: mana > 0 ? 1 : 0;
	}
}
