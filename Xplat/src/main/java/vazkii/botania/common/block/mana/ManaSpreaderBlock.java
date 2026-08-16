/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import com.google.common.base.Suppliers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.RedstoneSensitiveBlock;
import vazkii.botania.api.internal.OptionallyColored;
import vazkii.botania.api.mana.BasicLensItem;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaWaterloggedBlock;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.mana.ManaSpreaderBlockEntity;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.common.item.CustomCreativeTabContents;
import vazkii.botania.common.item.WandOfTheForestItem;
import vazkii.botania.common.lib.LibBlockNames;

import java.util.Optional;
import java.util.function.Supplier;

public class ManaSpreaderBlock extends BotaniaWaterloggedBlock implements EntityBlock, OptionallyColored, CustomCreativeTabContents {
	private static final VoxelShape SHAPE = box(2, 2, 2, 14, 14, 14);
	private static final VoxelShape SHAPE_PADDING = box(1, 1, 1, 15, 15, 15);
	private static final VoxelShape SHAPE_SCAFFOLDING = box(0, 0, 0, 16, 16, 16);
	public static final BooleanProperty HAS_SCAFFOLDING = BotaniaStateProperties.HAS_SCAFFOLDING;

	public record SpreaderParameters(int capacity, boolean redstoneTriggered,
			Supplier<BurstProperties> burstPropertiesSupplier, boolean rainbowRendered, int hudColor) {
	}

	public static final int BURST_SIZE_DEFAULT = 160;
	public static final int BURST_SIZE_ELVEN = (int) (1.5 * BURST_SIZE_DEFAULT);
	public static final int BURST_SIZE_GAIA = 4 * BURST_SIZE_DEFAULT;
	public static final int SPREADER_CAPACITY_DEFAULT = 1000;
	public static final int SPREADER_CAPACITY_GAIA = 10 * BURST_SIZE_GAIA;

	public static final SpreaderParameters DEFAULT_SPREADER_PARAMETERS = new SpreaderParameters(SPREADER_CAPACITY_DEFAULT, false,
			() -> new BurstProperties(BURST_SIZE_DEFAULT, 60, 4f, 0f, 1f, 0x20FF20),
			false, 0x00FF00);
	public static final SpreaderParameters PULSE_SPREADER_PARAMETERS = new SpreaderParameters(SPREADER_CAPACITY_DEFAULT, true,
			() -> new BurstProperties(BURST_SIZE_DEFAULT, 60, 4f, 0f, 1f, 0xFF2020),
			false, 0xFF0000);
	public static final SpreaderParameters ELVEN_SPREADER_PARAMETERS = new SpreaderParameters(SPREADER_CAPACITY_DEFAULT, false,
			() -> new BurstProperties(BURST_SIZE_ELVEN, 80, 4f, 0f, 1.25f, 0xFF45C4),
			false, 0xFF00AE);
	public static final SpreaderParameters GAIA_SPREADER_PARAMETERS = new SpreaderParameters(SPREADER_CAPACITY_GAIA, false,
			() -> new BurstProperties(BURST_SIZE_GAIA, 120, 20f, 0f, 2f, 0x20FF20),
			true, 0x00FF00);

	private final SpreaderParameters spreaderParameters;
	@Nullable
	private final DyeColor coverColor;
	private final Supplier<ResourceLocation> spreaderModelIdSupplier = Suppliers.memoize(
			() -> ModelLocationUtils.getModelLocation(getBaseBlock(this)));
	private final Supplier<ResourceLocation> coreModelIdSupplier = Suppliers.memoize(
			() -> ModelLocationUtils.getModelLocation(getBaseBlock(this), "_core"));
	private final Supplier<ResourceLocation> scaffoldingModelIdSupplier = Suppliers.memoize(
			() -> ModelLocationUtils.getModelLocation(getBaseBlock(this), "_scaffolding"));

	public ManaSpreaderBlock(SpreaderParameters spreaderParameters, @Nullable DyeColor coverColor, Properties builder) {
		super(builder);
		this.spreaderParameters = spreaderParameters;
		this.coverColor = coverColor;
		registerDefaultState(defaultBlockState()
				.setValue(HAS_SCAFFOLDING, false)
				.setValue(BlockStateProperties.POWERED, false));
	}

	public static ManaSpreaderBlock getBaseBlock(ManaSpreaderBlock potentiallyCoveredBlock) {
		return BotaniaBlocks.findOptionallyDyedBlock(potentiallyCoveredBlock, null, LibBlockNames.COVERED_INFIX);
	}

	@Override
	public void addToCreativeTab(Item me, CreativeModeTab.Output output) {
		if (me instanceof BlockItem blockItem && blockItem.getBlock() instanceof ManaSpreaderBlock block
				&& block.getCoverColor() != null) {
			// don't add covered spreaders to creative inventory or recipe viewer lists
			return;
		}
		output.accept(me);
	}

	@Override
	public Optional<DyeColor> getOptionalColor() {
		return Optional.ofNullable(coverColor);
	}

	public boolean isCovered() {
		return coverColor != null;
	}

	@Nullable
	public DyeColor getCoverColor() {
		return coverColor;
	}

	// variant value definitions
	public boolean isRedstoneTriggered() {
		return spreaderParameters.redstoneTriggered;
	}

	public boolean isRainbowRendered() {
		return spreaderParameters.rainbowRendered;
	}

	public BurstProperties getDefaultBurstProperties() {
		return spreaderParameters.burstPropertiesSupplier.get();
	}

	public int getManaCapacity() {
		return spreaderParameters.capacity;
	}

	public int getHudColor() {
		return spreaderParameters.hudColor;
	}

	public ResourceLocation getSpreaderModelId() {
		return spreaderModelIdSupplier.get();
	}

	public ResourceLocation getCoreModelId() {
		return coreModelIdSupplier.get();
	}

	public ResourceLocation getScaffoldingModelId() {
		return scaffoldingModelIdSupplier.get();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(HAS_SCAFFOLDING, BlockStateProperties.POWERED);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return RedstoneSensitiveBlock.getPoweredStateForPlacement(super.getStateForPlacement(context), context);
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		RedstoneSensitiveBlock.updateRedstonePower(state, world, pos);
	}

	@Override
	public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		if (blockState.getValue(HAS_SCAFFOLDING)) {
			return SHAPE_SCAFFOLDING;
		}
		return coverColor != null ? SHAPE_PADDING : SHAPE;
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter world, BlockPos pos) {
		return SHAPE;
	}

	@Override
	public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		Direction orientation = placer == null ? Direction.WEST : Direction.orderedByNearest(placer)[0].getOpposite();
		if (!(world.getBlockEntity(pos) instanceof ManaSpreaderBlockEntity spreader)) {
			return;
		}
		switch (orientation) {
			case DOWN:
				spreader.rotationY = -90F;
				break;
			case UP:
				spreader.rotationY = 90F;
				break;
			case NORTH:
				spreader.rotationX = 270F;
				break;
			case SOUTH:
				spreader.rotationX = 90F;
				break;
			case WEST:
				break;
			case EAST:
				spreader.rotationX = 180F;
				break;
		}
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack heldItem, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (!(world.getBlockEntity(pos) instanceof ManaSpreaderBlockEntity spreader)) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}

		if (heldItem.getItem() instanceof WandOfTheForestItem) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}
		boolean mainHandEmpty = player.getMainHandItem().isEmpty();

		ItemStack lens = spreader.getItemHandler().getItem(0);
		boolean playerHasLens = heldItem.getItem() instanceof BasicLensItem;
		boolean lensIsSame = playerHasLens && ItemStack.isSameItemSameComponents(heldItem, lens);
		DyeColor playerWoolColor = ColorHelper.getWoolColor(heldItem.getItem());
		boolean woolIsSame = playerWoolColor == coverColor;
		boolean playerHasScaffolding = heldItem.is(Items.SCAFFOLDING);
		boolean shouldInsert = (playerHasLens && !lensIsSame)
				|| (playerWoolColor != null && !woolIsSame)
				|| (playerHasScaffolding && !state.getValue(HAS_SCAFFOLDING));

		if (shouldInsert) {
			if (playerHasLens) {
				ItemStack toInsert = heldItem.copyWithCount(1);
				if (!lens.isEmpty()) {
					player.setItemInHand(hand, ItemUtils.createFilledResult(heldItem, player, lens));
				} else if (!player.hasInfiniteMaterials()) {
					heldItem.shrink(1);
				}
				spreader.getItemHandler().setItem(0, toInsert);
				world.playSound(player, pos, BotaniaSounds.SPREADER_ADD_LENS, SoundSource.BLOCKS, 1F, 1F);
			} else if (playerWoolColor != null) {
				if (coverColor != null) {
					player.setItemInHand(hand, ItemUtils.createFilledResult(heldItem, player, new ItemStack(ColorHelper.WOOL_MAP.apply(coverColor))));
				} else if (!player.hasInfiniteMaterials()) {
					heldItem.shrink(1);
				}
				world.setBlockAndUpdate(pos,
						BotaniaBlocks.findOptionallyDyedBlock(this, playerWoolColor, "_covered_")
								.withPropertiesOf(state));
				// need to do this as the client unfortunately deletes its copy of the block entity on block type change
				spreader.setChanged();
				world.playSound(player, pos, BotaniaSounds.SPREADER_COVER, SoundSource.BLOCKS, 1F, 1F);
			} else { // playerHasScaffolding
				world.setBlockAndUpdate(pos, state.setValue(HAS_SCAFFOLDING, true));
				world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));

				if (!player.hasInfiniteMaterials()) {
					heldItem.shrink(1);
				}

				world.playSound(player, pos, BotaniaSounds.SPREADER_SCAFFOLD, SoundSource.BLOCKS, 1F, 1F);
			}
			return ItemInteractionResult.sidedSuccess(world.isClientSide());
		}

		if (state.getValue(HAS_SCAFFOLDING) && player.isSecondaryUseActive()) {
			ItemStack scaffolding = new ItemStack(Items.SCAFFOLDING);
			if (!player.hasInfiniteMaterials() || !player.getInventory().contains(scaffolding)) {
				player.getInventory().placeItemBackInInventory(scaffolding);
			}
			world.setBlockAndUpdate(pos, state.setValue(HAS_SCAFFOLDING, false));
			world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));

			world.playSound(player, pos, BotaniaSounds.SPREADER_UN_SCAFFOLD, SoundSource.BLOCKS, 1F, 1F);

			return ItemInteractionResult.sidedSuccess(world.isClientSide());
		}
		if (!lens.isEmpty() && (mainHandEmpty || lensIsSame)) {
			if (!player.hasInfiniteMaterials() || !player.getInventory().contains(lens)) {
				player.getInventory().placeItemBackInInventory(lens);
			}
			spreader.getItemHandler().setItem(0, ItemStack.EMPTY);

			world.playSound(player, pos, BotaniaSounds.SPREADER_REMOVE_LENS, SoundSource.BLOCKS, 1F, 1F);

			return ItemInteractionResult.sidedSuccess(world.isClientSide());
		}
		if (coverColor != null && (mainHandEmpty || woolIsSame)) {
			ItemStack wool = new ItemStack(ColorHelper.WOOL_MAP.apply(coverColor));
			player.getInventory().placeItemBackInInventory(wool);
			world.setBlockAndUpdate(pos,
					BotaniaBlocks.findOptionallyDyedBlock(this, null, "_covered_")
							.withPropertiesOf(state));
			spreader.setChanged();
			world.playSound(player, pos, BotaniaSounds.SPREADER_UNCOVER, SoundSource.BLOCKS, 1F, 1F);

			return ItemInteractionResult.sidedSuccess(world.isClientSide());
		}

		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() instanceof ManaSpreaderBlock oldBlock
				&& newState.getBlock() instanceof ManaSpreaderBlock newBlock
				&& ManaSpreaderBlock.getBaseBlock(oldBlock) == ManaSpreaderBlock.getBaseBlock(newBlock)) {
			// don't delete block entity if it's the same spreader type
			return;
		}
		Containers.dropContentsOnDestroy(state, newState, level, pos);
		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ManaSpreaderBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, BotaniaBlockEntities.MANA_SPREADER, ManaSpreaderBlockEntity::commonTick);
	}
}
