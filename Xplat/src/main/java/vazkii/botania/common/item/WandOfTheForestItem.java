/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import com.mojang.datafixers.util.Pair;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.Bound;
import vazkii.botania.api.block.WandBindable;
import vazkii.botania.api.item.CoordBoundItem;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.DataComponentHelper;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.proxy.Proxy;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class WandOfTheForestItem extends Item implements CustomCreativeTabContents {

	/**
	 * Block manipulation strategies for when neither binding behavior nor explicitly defined wand interactions exist.
	 * The final entry's {@link BlockStateManipulator} must not return {@code null}.
	 */
	public static final List<Pair<BlockStateSidePredicate, BlockStateManipulator>> BLOCK_STATE_MANIPULATION_STRATEGIES = List.of(
			// skip any blocks tagged for wand manipulation opt-out
			Pair.of((state, dir) -> state.is(BotaniaTags.Blocks.UNWANDABLE),
					BlockStateManipulator.NO_OP),

			// log-like pillar blocks
			Pair.of((state, side) -> state.getBlock() instanceof RotatedPillarBlock,
					WandOfTheForestItem::applyPillarBlockRotation),

			// blocks with fine granular horizontal rotation options (e.g. signs, heads, banners)
			Pair.of((state, side) -> state.hasProperty(BlockStateProperties.ROTATION_16),
					WandOfTheForestItem::applySignOrBannerRotation),

			// blocks with a boolean property for each side (e.g. huge mushroom blocks)
			Pair.of(WandOfTheForestItem::hasSixSidedToggleProperties, WandOfTheForestItem::applySixSidedPropertyToggle),

			// toggle top/bottom slab when clicking side (ignore for double slabs)
			Pair.of(WandOfTheForestItem::isSideOfSlabBlock, WandOfTheForestItem::applyToggleSlabType),

			// toggle top/bottom half when clicking side (e.g. for stairs or trapdoors)
			Pair.of(WandOfTheForestItem::isSideOfHalfBlock, WandOfTheForestItem::applyToggleHalf),

			// blocks with a "facing" property of value type Direction
			Pair.of((state, side) -> getFacingPropOptional(state).isPresent(),
					WandOfTheForestItem::applyFacingRotationChange),

			// Fallback to vanilla block rotation
			Pair.of(BlockStateSidePredicate.ALWAYS, WandOfTheForestItem::applyVanillaRotation)
	);

	public final ChatFormatting modeChatFormatting;

	public WandOfTheForestItem(ChatFormatting formatting, Item.Properties builder) {
		super(builder);
		this.modeChatFormatting = formatting;
	}

	private static boolean tryCompleteBinding(Level level, Player player, ItemStack wand, GlobalPos src, BlockPos target, Direction targetSide) {
		if (!target.equals(src.pos()) && src.dimension().equals(level.dimension())) {
			Optional<Direction> srcSide = getBindingSide(wand);
			setBindingAttempt(wand, null, null);

			WandBindable bindable = XplatAbstractions.instance()
					.findWandBindable(level, src.pos(), null, null, srcSide.orElse(null));
			if (bindable != null) {
				if (bindable.bindTo(player, wand, target, targetSide)) {
					doParticleBeamWithOffset(level, src.pos(), target);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		ItemStack stack = ctx.getItemInHand();
		Level world = ctx.getLevel();
		Player player = ctx.getPlayer();
		BlockPos pos = ctx.getClickedPos();
		GlobalPos globalPos = GlobalPos.of(world.dimension(), pos);
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		Direction side = ctx.getClickedFace();
		BlockEntity tile = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;

		if (player == null) {
			return InteractionResult.PASS;
		}

		if (player.isSecondaryUseActive()) {
			Optional<GlobalPos> boundPos = getBindingAttempt(stack);
			if (boundPos.filter(loc -> tryCompleteBinding(world, player, stack, loc, pos, side)).isPresent()) {
				return InteractionResult.SUCCESS;
			}

			if (getBindMode(stack)) {
				WandBindable bindable = XplatAbstractions.instance().findWandBindable(world, pos, state, tile, side);
				if (bindable != null && bindable.canSelect(player, stack, side)) {
					if (boundPos.filter(globalPos::equals).isPresent()) {
						setBindingAttempt(stack, null, null);
					} else {
						setBindingAttempt(stack, globalPos, side);
					}

					if (world.isClientSide) {
						player.playSound(BotaniaSounds.ding, 0.11F, 1F);
					}

					return InteractionResult.SUCCESS;
				}
			}
		}

		var wandable = XplatAbstractions.INSTANCE.findWandable(world, pos, state, tile, side);
		if (player.isSecondaryUseActive() && (wandable == null || getBindMode(stack))
				&& (!(block instanceof GameMasterBlock) || player.canUseGameMasterBlocks())) {
			BlockState newState = manipulateBlockstate(state, side, blockState -> blockState.canSurvive(world, pos));
			if (newState != state) {
				world.setBlockAndUpdate(pos, newState);
				world.playSound(player, pos, newState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1F, 1F);
				return InteractionResult.SUCCESS;
			}
		}

		if (wandable != null && wandable.onUsedByWand(player, stack, side)) {
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	private static BlockState manipulateBlockstate(BlockState oldState, Direction side, Predicate<BlockState> canSurvive) {
		for (var strategy : BLOCK_STATE_MANIPULATION_STRATEGIES) {
			var predicate = strategy.getFirst();
			var manipulator = strategy.getSecond();

			if (predicate.test(oldState, side)) {
				BlockState newState = manipulator.apply(oldState, side, canSurvive);
				if (newState != null) {
					return newState;
				}
			}
		}
		return oldState;
	}

	private static BlockState applyPillarBlockRotation(BlockState state, Direction side, Predicate<BlockState> canSurvive) {
		return iterateToNextValidPropertyValue(
				state, BlockStateProperties.AXIS, BlockStateProperties.AXIS.getPossibleValues(),
				state.getValue(BlockStateProperties.AXIS), canSurvive
		);
	}

	private static BlockState applySignOrBannerRotation(BlockState state, Direction side, Predicate<BlockState> canSurvive) {
		return iterateToNextValidPropertyValue(
				state, BlockStateProperties.ROTATION_16,
				BlockStateProperties.ROTATION_16.getPossibleValues(),
				state.getValue(BlockStateProperties.ROTATION_16), canSurvive
		);
	}

	/**
	 * Returns a "facing" property of type {@link Direction}. There are various properties that match this definition,
	 * such as horizontal facing direction, hopper facing direction, or true six-sided facing, possible accompanied
	 * by an {@link AttachFace} property.
	 */
	@SuppressWarnings("unchecked")
	private static Optional<Property<Direction>> getFacingPropOptional(BlockState state) {
		return state.getProperties().stream()
				.filter(prop -> prop.getName().equals("facing") && prop.getValueClass() == Direction.class)
				// we just verified the property value type, so it's safe to convert here
				.map(prop -> (Property<Direction>) prop)
				.findFirst();
	}

	private static boolean hasSixSidedToggleProperties(BlockState oldState, Direction side) {
		return oldState.hasProperty(PipeBlock.PROPERTY_BY_DIRECTION.get(side)) &&
				oldState.getProperties().containsAll(PipeBlock.PROPERTY_BY_DIRECTION.values());
	}

	private static BlockState applySixSidedPropertyToggle(BlockState oldState, Direction side, Predicate<BlockState> canSurvive) {
		BooleanProperty directionPropertyFromSide = PipeBlock.PROPERTY_BY_DIRECTION.get(side);
		boolean oldValue = oldState.getValue(directionPropertyFromSide);
		BlockState newState = oldState.setValue(directionPropertyFromSide, !oldValue);
		return canSurvive.test(newState) ? newState : oldState;
	}

	private static boolean isSideOfSlabBlock(BlockState state, Direction side) {
		return !side.getAxis().isVertical() && state.getBlock() instanceof SlabBlock
				&& state.getValue(SlabBlock.TYPE) != SlabType.DOUBLE;
	}

	private static BlockState applyToggleSlabType(BlockState oldState, Direction side, Predicate<BlockState> canSurvive) {
		BlockState newState = switch (oldState.getValue(SlabBlock.TYPE)) {
			case TOP -> oldState.setValue(SlabBlock.TYPE, SlabType.BOTTOM);
			case BOTTOM -> oldState.setValue(SlabBlock.TYPE, SlabType.TOP);
			default -> oldState;
		};
		return canSurvive.test(newState) ? newState : oldState;
	}

	private static boolean isSideOfHalfBlock(BlockState state, Direction side) {
		return !side.getAxis().isVertical() && state.hasProperty(BlockStateProperties.HALF);
	}

	private static BlockState applyToggleHalf(BlockState oldState, Direction side, Predicate<BlockState> canSurvive) {
		BlockState newState = oldState.cycle(BlockStateProperties.HALF);
		return canSurvive.test(newState) ? newState : oldState;
	}

	private static BlockState applyFacingRotationChange(BlockState state, Direction side, Predicate<BlockState> canSurvive) {
		Property<Direction> facingProp = getFacingPropOptional(state).orElseThrow();
		return rotateFacingDirection(state, side, canSurvive, facingProp);
	}

	private static BlockState applyVanillaRotation(BlockState oldState, Direction side, Predicate<BlockState> canSurvive) {
		for (Rotation rot : new Rotation[] { Rotation.CLOCKWISE_90, Rotation.CLOCKWISE_180, Rotation.COUNTERCLOCKWISE_90 }) {
			BlockState newState = oldState.rotate(rot);
			if (canSurvive.test(newState)) {
				return newState;
			}
		}
		return oldState;
	}

	private static BlockState rotateFacingDirection(BlockState oldState, Direction side, Predicate<BlockState> canSurvive, Property<Direction> facingProp) {
		if (oldState.hasProperty(BlockStateProperties.CHEST_TYPE) && !oldState.getValue(BlockStateProperties.CHEST_TYPE).equals(ChestType.SINGLE)
				|| oldState.hasProperty(BlockStateProperties.EXTENDED) && oldState.getValue(BlockStateProperties.EXTENDED).equals(Boolean.TRUE)
				|| oldState.hasProperty(BlockStateProperties.BED_PART)) {
			// rotating double chests would be nice, but seems beyond the scope of this feature; same goes for beds and extended pistons
			return oldState;
		}

		Direction oldDir = oldState.getValue(facingProp);
		if (oldState.hasProperty(BlockStateProperties.ATTACH_FACE) && oldState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
			// FaceAttachedHorizontalDirectionalBlock or equivalent block, rotate around clicked side
			if (side.getAxis() == Direction.Axis.Y) {
				// clicked vertically attached block from top or bottom, just rotate on that face
				return rotateClockwiseAroundSideDirect(oldState, side, canSurvive, facingProp, oldDir);
			}

			AttachFace attachFace = oldState.getValue(BlockStateProperties.ATTACH_FACE);
			if (attachFace == AttachFace.WALL && oldDir.getAxis() == side.getAxis()) {
				// clicked wall-attached block on attachment axis, just flip to other side, if possible
				BlockState newState = oldState.setValue(facingProp, oldDir.getOpposite());
				return canSurvive.test(newState) ? newState : oldState;
			}

			// operate on an implied direction, rotate that, and eventually translate it back at the end
			Direction impliedDir = switch (attachFace) {
				case FLOOR -> Direction.DOWN;
				case CEILING -> Direction.UP;
				case WALL -> oldDir;
			};

			Function<Direction, BlockState> newStateFunction = dir -> switch (dir) {
				case UP -> oldState.setValue(BlockStateProperties.ATTACH_FACE, AttachFace.CEILING);
				case DOWN -> oldState.setValue(BlockStateProperties.ATTACH_FACE, AttachFace.FLOOR);
				default -> oldState.setValue(BlockStateProperties.ATTACH_FACE, AttachFace.WALL).setValue(facingProp, dir);
			};

			return rotateClockwiseAroundSide(side, impliedDir, newStateFunction, canSurvive);
		}

		List<Direction> possibleFacingValues = new ArrayList<>(BlockStateProperties.FACING.getPossibleValues());
		if (possibleFacingValues.retainAll(facingProp.getPossibleValues())) {
			// doesn't support all possible directions
			if (possibleFacingValues.isEmpty()) {
				// How did we get here?
				return oldState;
			}

			// iterate over values in the order defined by BlockStateProperties.FACING,
			// because it makes more sense than the native order of the Direction enum values
			return iterateToNextValidPropertyValue(oldState, facingProp, possibleFacingValues, oldDir, canSurvive);
		}

		if (oldDir.getAxis() != side.getAxis()) {
			// rotate clockwise around clicked side
			return rotateClockwiseAroundSideDirect(oldState, side, canSurvive, facingProp, oldDir);
		}

		// facing towards or away from clicked side, flip around
		BlockState newState = oldState.setValue(facingProp, oldDir.getOpposite());
		return canSurvive.test(newState) ? newState : oldState;
	}

	private static BlockState rotateClockwiseAroundSideDirect(BlockState oldState, Direction side, Predicate<BlockState> canSurvive, Property<Direction> facingProp, Direction oldDir) {
		return rotateClockwiseAroundSide(side, oldDir, dir -> oldState.setValue(facingProp, dir), canSurvive);
	}

	private static BlockState rotateClockwiseAroundSide(Direction side, Direction oldDir, Function<Direction, BlockState> newStateFunction, Predicate<BlockState> canSurvive) {
		BlockState newState;
		Direction newDir = oldDir;
		do {
			newDir = getClockwiseDirectionForSide(side, newDir);
			newState = newStateFunction.apply(newDir);
		} while (newDir != oldDir && !canSurvive.test(newState));

		return newState;
	}

	private static Direction getClockwiseDirectionForSide(Direction side, Direction oldDir) {
		return side.getAxisDirection() == Direction.AxisDirection.NEGATIVE
				? oldDir.getCounterClockWise(side.getAxis())
				: oldDir.getClockWise(side.getAxis());
	}

	private static <T extends Comparable<T>> BlockState iterateToNextValidPropertyValue(BlockState oldState, Property<T> property, Collection<T> orderedValues, T oldValue, Predicate<BlockState> canSurvive) {
		Iterator<T> it = orderedValues.iterator();
		while (it.hasNext() && !it.next().equals(oldValue)) {
			// look for current value
		}
		// now find next value that results in a valid block state in the given context
		while (it.hasNext()) {
			BlockState newState = oldState.setValue(property, it.next());
			if (canSurvive.test(newState)) {
				return newState;
			}
		}
		// failed to find valid state after the current state, look before
		it = orderedValues.iterator();
		while (it.hasNext()) {
			T newValue = it.next();
			if (newValue.equals(oldValue)) {
				// no valid values
				return oldState;
			}
			BlockState newState = oldState.setValue(property, newValue);
			if (canSurvive.test(newState)) {
				return newState;
			}
		}
		// nothing worked, leave it as is
		return oldState;
	}

	public static void doParticleBeamWithOffset(Level world, BlockPos orig, BlockPos end) {
		Vec3 origOffset = world.getBlockState(orig).getOffset(world, orig);
		Vec3 vorig = new Vec3(orig.getX() + origOffset.x() + 0.5, orig.getY() + origOffset.y() + 0.5, orig.getZ() + origOffset.z() + 0.5);
		Vec3 endOffset = world.getBlockState(end).getOffset(world, end);
		Vec3 vend = new Vec3(end.getX() + endOffset.x() + 0.5, end.getY() + endOffset.y() + 0.5, end.getZ() + endOffset.z() + 0.5);
		doParticleBeam(world, vorig, vend);
	}

	public static void doParticleBeam(Level world, Vec3 orig, Vec3 end) {
		if (!world.isClientSide) {
			return;
		}

		Vec3 diff = end.subtract(orig);
		Vec3 movement = diff.normalize().scale(0.05);
		int iters = (int) (diff.length() / movement.length());
		float huePer = 1F / iters;
		float hueSum = (float) Math.random();

		Vec3 currentPos = orig;
		for (int i = 0; i < iters; i++) {
			float hue = i * huePer + hueSum;
			int color = Mth.hsvToRgb(Mth.frac(hue), 1F, 1F);
			float r = (color >> 16 & 0xFF) / 255F;
			float g = (color >> 8 & 0xFF) / 255F;
			float b = (color & 0xFF) / 255F;

			SparkleParticleData data = SparkleParticleData.noClip(0.5F, r, g, b, 4);
			Proxy.INSTANCE.addParticleForceNear(world, data, currentPos.x, currentPos.y, currentPos.z, 0, 0, 0);
			currentPos = currentPos.add(movement);
		}
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		getBindingAttempt(stack).ifPresent(pos -> {
			if (!pos.dimension().equals(world.dimension())
					|| XplatAbstractions.instance().findWandBindable(world, pos.pos(), null, null,
							getBindingSide(stack).orElse(null)) == null) {
				setBindingAttempt(stack, null, null);
			}
		});
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player.isSecondaryUseActive()) {
			if (!world.isClientSide) {
				setBindMode(stack, !getBindMode(stack));
			} else {
				player.playSound(BotaniaSounds.ding, 0.1F, 1F);
			}
		}

		return InteractionResultHolder.success(stack);
	}

	@Override
	public void addToCreativeTab(Item me, CreativeModeTab.Output output) {
		output.accept(me);
		List<Pair<DyeColor, DyeColor>> colorPairs = Arrays.asList(
				new Pair<>(DyeColor.WHITE, DyeColor.LIGHT_BLUE),
				new Pair<>(DyeColor.WHITE, DyeColor.PINK),
				new Pair<>(DyeColor.LIGHT_BLUE, DyeColor.PINK),
				new Pair<>(DyeColor.PURPLE, DyeColor.BLUE),
				new Pair<>(DyeColor.RED, DyeColor.RED),
				new Pair<>(DyeColor.BLUE, DyeColor.BLUE),
				new Pair<>(DyeColor.ORANGE, DyeColor.ORANGE),
				new Pair<>(DyeColor.BLACK, DyeColor.BLACK),
				new Pair<>(DyeColor.GRAY, DyeColor.LIGHT_GRAY),
				new Pair<>(DyeColor.PINK, DyeColor.PINK),
				new Pair<>(DyeColor.YELLOW, DyeColor.LIME),
				new Pair<>(DyeColor.WHITE, DyeColor.BLACK)
		);
		Collections.shuffle(colorPairs);
		for (int i = 0; i < 7; i++) {
			Pair<DyeColor, DyeColor> pair = colorPairs.get(i);
			if (Math.random() < 0.5) {
				pair = new Pair<>(pair.getSecond(), pair.getFirst());
			}
			output.accept(setColors(new ItemStack(me), pair.getFirst(), pair.getSecond()));
		}
	}

	@Override
	public Component getName(ItemStack stack) {
		Component mode = Component.literal(" (")
				.append(Component.translatable(getModeString(stack)).withStyle(modeChatFormatting))
				.append(")");
		return super.getName(stack).plainCopy().append(mode);
	}

	public static ItemStack setColors(ItemStack wand, DyeColor color1, DyeColor color2) {
		wand.set(BotaniaDataComponents.WAND_COLOR1, color1);
		wand.set(BotaniaDataComponents.WAND_COLOR2, color2);

		return wand;
	}

	public static DyeColor getColor1(ItemStack stack) {
		return stack.getOrDefault(BotaniaDataComponents.WAND_COLOR1, DyeColor.WHITE);
	}

	public static DyeColor getColor2(ItemStack stack) {
		return stack.getOrDefault(BotaniaDataComponents.WAND_COLOR2, DyeColor.WHITE);
	}

	public static void setBindingAttempt(ItemStack stack, @Nullable GlobalPos pos, @Nullable Direction side) {
		DataComponentHelper.setOptional(stack, BotaniaDataComponents.BINDING_POS, pos);
		DataComponentHelper.setOptional(stack, BotaniaDataComponents.BINDING_SIDE, side);
	}

	public static Optional<GlobalPos> getBindingAttempt(ItemStack stack) {
		return Optional.ofNullable(stack.get(BotaniaDataComponents.BINDING_POS));
	}

	public static Optional<Direction> getBindingSide(ItemStack stack) {
		return Optional.ofNullable(stack.get(BotaniaDataComponents.BINDING_SIDE));
	}

	public static boolean getBindMode(ItemStack stack) {
		return stack.has(BotaniaDataComponents.WAND_BIND_MODE);
	}

	public static void setBindMode(ItemStack stack, boolean bindMode) {
		DataComponentHelper.setFlag(stack, BotaniaDataComponents.WAND_BIND_MODE, bindMode);
	}

	public static String getModeString(ItemStack stack) {
		return "botaniamisc.wandMode." + (getBindMode(stack) ? "bind" : "function");
	}

	public static class CoordBoundItemImpl implements CoordBoundItem {
		private final ItemStack stack;

		public CoordBoundItemImpl(ItemStack stack) {
			this.stack = stack;
		}

		@Nullable
		@Override
		public BlockPos getBinding(Level world) {
			Optional<GlobalPos> bound = getBindingAttempt(stack);
			if (bound.isPresent() && bound.get().dimension().equals(world.dimension())) {
				return bound.get().pos();
			}

			var pos = ClientProxy.INSTANCE.getClientHit();
			if (pos instanceof BlockHitResult bHit && pos.getType() == HitResult.Type.BLOCK) {
				BlockEntity tile = world.getBlockEntity(bHit.getBlockPos());
				if (tile instanceof Bound boundTile) {
					return boundTile.getBinding();
				}
			}

			return null;
		}
	}

	@FunctionalInterface
	public interface BlockStateSidePredicate {
		BlockStateSidePredicate ALWAYS = (state, side) -> true;

		boolean test(BlockState state, Direction side);
	}

	@FunctionalInterface
	public interface BlockStateManipulator {
		BlockStateManipulator NO_OP = (state, side, canSurvive) -> state;

		/**
		 * Manipulates a block state based on the clicked side and a test function for potential new block states.
		 * Returns {@code null} to indicate other strategies should be attempted.
		 */
		@Nullable
		BlockState apply(BlockState state, Direction side, Predicate<BlockState> canSurvive);
	}
}
