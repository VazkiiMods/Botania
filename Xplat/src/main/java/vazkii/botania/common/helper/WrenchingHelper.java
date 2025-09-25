package vazkii.botania.common.helper;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.SortedPair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.mixin.BaseRailBlockAccessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Implements block state manipulation operations for the {@link vazkii.botania.common.item.WandOfTheForestItem}
 */
public class WrenchingHelper {
	private static final double RAIL_INTERACTION_SIDE_OFFSET = 0.55;
	/**
	 * Block manipulation strategies for when neither binding behavior nor explicitly defined wand interactions exist.
	 */
	private static final List<Pair<BlockStateSidePredicate, BlockStateManipulator>> BLOCK_STATE_MANIPULATION_STRATEGIES = List.of(
			// skip any blocks tagged for wand manipulation opt-out
			Pair.of(WrenchingHelper::skipBlockStateManipulation, BlockStateManipulator.NO_OP),

			// log-like pillar blocks
			Pair.of((state, side) -> state.getBlock() instanceof RotatedPillarBlock,
					WrenchingHelper::applyPillarBlockRotation),

			// blocks with fine granular horizontal rotation options (e.g. signs, heads, banners)
			Pair.of((state, side) -> state.hasProperty(BlockStateProperties.ROTATION_16),
					WrenchingHelper::applySignOrBannerRotation),

			// blocks with a boolean property for each side (e.g. huge mushroom blocks)
			Pair.of(WrenchingHelper::hasSixSidedToggleProperties, WrenchingHelper::applySixSidedPropertyToggle),

			// toggle top/bottom slab when clicking side (ignore for double slabs)
			Pair.of(WrenchingHelper::isSideOfSlabBlock, WrenchingHelper::applyToggleSlabType),

			// toggle top/bottom half when clicking side (e.g. for stairs or trapdoors)
			Pair.of(WrenchingHelper::isSideOfHalfBlock, WrenchingHelper::applyToggleHalf),

			// blocks that can define their orientation with an attach-face and a horizontal facing (e.g. button)
			Pair.of(WrenchingHelper::isAttachFaceAndHorizontalFacing,
					WrenchingHelper::applyHorizontalFacingAndAttachFaceRotation),

			// blocks with a "facing" property of value type Direction
			Pair.of(WrenchingHelper::isOtherFacingPropertyBlockState, WrenchingHelper::applyFacingRotationChange),

			// blocks with a FrontAndTop property (crafter, jigsaw)
			Pair.of((state, side) -> state.hasProperty(BlockStateProperties.ORIENTATION),
					WrenchingHelper::applyOrientationRotationChange),

			// Fallback to vanilla block rotation
			Pair.of(BlockStateSidePredicate.ALWAYS, WrenchingHelper::applyVanillaRotation)
	);
	private static final Map<SortedPair<Direction>, List<RailShape>> DIRECTION_RAIL_SHAPES = Map.of(
			SortedPair.of(Direction.NORTH, Direction.SOUTH), List
					.of(RailShape.NORTH_SOUTH, RailShape.ASCENDING_NORTH, RailShape.ASCENDING_SOUTH),
			SortedPair.of(Direction.EAST, Direction.WEST), List
					.of(RailShape.EAST_WEST, RailShape.ASCENDING_EAST, RailShape.ASCENDING_WEST),
			SortedPair.of(Direction.SOUTH, Direction.EAST), List.of(RailShape.SOUTH_EAST),
			SortedPair.of(Direction.SOUTH, Direction.WEST), List.of(RailShape.SOUTH_WEST),
			SortedPair.of(Direction.NORTH, Direction.WEST), List.of(RailShape.NORTH_WEST),
			SortedPair.of(Direction.NORTH, Direction.EAST), List.of(RailShape.NORTH_EAST)
	);
	private static final Map<RailShape, SortedPair<Direction>> RAIL_SHAPE_DIRECTIONS = Map.of(
			RailShape.NORTH_SOUTH, SortedPair.of(Direction.NORTH, Direction.SOUTH),
			RailShape.EAST_WEST, SortedPair.of(Direction.EAST, Direction.WEST),
			RailShape.ASCENDING_EAST, SortedPair.of(Direction.EAST, Direction.WEST),
			RailShape.ASCENDING_WEST, SortedPair.of(Direction.EAST, Direction.WEST),
			RailShape.ASCENDING_NORTH, SortedPair.of(Direction.NORTH, Direction.SOUTH),
			RailShape.ASCENDING_SOUTH, SortedPair.of(Direction.NORTH, Direction.SOUTH),
			RailShape.SOUTH_EAST, SortedPair.of(Direction.SOUTH, Direction.EAST),
			RailShape.SOUTH_WEST, SortedPair.of(Direction.SOUTH, Direction.WEST),
			RailShape.NORTH_WEST, SortedPair.of(Direction.NORTH, Direction.WEST),
			RailShape.NORTH_EAST, SortedPair.of(Direction.NORTH, Direction.EAST)
	);
	private static final Map<RailShape, Direction> RAIL_RAISED_DIRECTION = Map.of(
			RailShape.ASCENDING_EAST, Direction.EAST,
			RailShape.ASCENDING_WEST, Direction.WEST,
			RailShape.ASCENDING_NORTH, Direction.NORTH,
			RailShape.ASCENDING_SOUTH, Direction.SOUTH
	);
	private static final Map<Direction, RailShape> DIRECTION_RAISED_RAIL = RAIL_RAISED_DIRECTION
			.entrySet().stream().collect(Collectors.toUnmodifiableMap(Map.Entry::getValue, Map.Entry::getKey));
	// flat, straight shapes should come first
	private static final Map<Direction, RailShape> DIRECTION_FLAT_STRAIGHT_RAIL = Map.of(
			Direction.NORTH, RailShape.NORTH_SOUTH,
			Direction.SOUTH, RailShape.NORTH_SOUTH,
			Direction.EAST, RailShape.EAST_WEST,
			Direction.WEST, RailShape.EAST_WEST
	);
	// Helper lists for rotating rail shapes without other modifications
	private static final List<RailShape> ASCENDING_SHAPES_CLOCKWISE = List.of(
			RailShape.ASCENDING_NORTH, RailShape.ASCENDING_EAST, RailShape.ASCENDING_SOUTH, RailShape.ASCENDING_WEST
	);
	private static final List<RailShape> STRAIGHT_SHAPES_CLOCKWISE = List.of(
			RailShape.NORTH_SOUTH, RailShape.EAST_WEST
	);
	private static final List<RailShape> BENT_SHAPES_CLOCKWISE = List.of(
			RailShape.NORTH_EAST, RailShape.SOUTH_EAST, RailShape.SOUTH_WEST, RailShape.NORTH_WEST
	);

	@Nullable
	public static BlockState manipulateBlockState(Level world, BlockPos pos, BlockState state, Direction side, Vec3 relativePos) {
		if (state.is(BotaniaTags.Blocks.UNWANDABLE)) {
			return null;
		}
		Optional<Property<RailShape>> railShapePropOptional = getRailShapePropOptional(state);
		if (railShapePropOptional.isPresent()) {
			BlockState newState = applyRailShapeChange(state, side, relativePos,
					blockState -> !BaseRailBlockAccessor.botania_shouldBeRemoved(pos, world,
							blockState.getValue(railShapePropOptional.get())));
			if (newState != null) {
				return newState;
			}
		}
		return applyStandardBlockStateManipulationStrategies(state, side,
				blockState -> blockState.canSurvive(world, pos));
	}

	@Nullable
	private static BlockState applyStandardBlockStateManipulationStrategies(BlockState oldState, Direction side, Predicate<BlockState> canSurvive) {
		for (var strategy : BLOCK_STATE_MANIPULATION_STRATEGIES) {
			var predicate = strategy.first();
			var manipulator = strategy.second();

			if (predicate.test(oldState, side)) {
				BlockState newState = manipulator.apply(oldState, side, canSurvive);
				if (newState != null) {
					return newState;
				}
			}
		}
		return null;
	}

	private static boolean skipBlockStateManipulation(BlockState state, Direction dir) {
		// the following are out of scope, as they would involve moving another block
		return state.hasProperty(BlockStateProperties.BED_PART)
				|| state.hasProperty(BlockStateProperties.EXTENDED) && state.getValue(BlockStateProperties.EXTENDED)
				|| (state.hasProperty(BlockStateProperties.CHEST_TYPE)
						&& state.getValue(BlockStateProperties.CHEST_TYPE) != ChestType.SINGLE);
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

	@SuppressWarnings("unchecked")
	public static Optional<Property<RailShape>> getRailShapePropOptional(BlockState state) {
		return state.getProperties().stream()
				.filter(prop -> prop.getName().equals("shape") && prop.getValueClass() == RailShape.class)
				// we just verified the property value type, so it's safe to convert here
				.map(prop -> (Property<RailShape>) prop)
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

	private static boolean isAttachFaceAndHorizontalFacing(BlockState state, Direction side) {
		return state.hasProperty(BlockStateProperties.ATTACH_FACE)
				&& state.hasProperty(BlockStateProperties.HORIZONTAL_FACING);
	}

	private static BlockState applyHorizontalFacingAndAttachFaceRotation(BlockState oldState, Direction side, Predicate<BlockState> canSurvive) {
		Property<Direction> facingProp = getFacingPropOptional(oldState).orElseThrow();
		Direction oldDir = oldState.getValue(facingProp);

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

	private static boolean isOtherFacingPropertyBlockState(BlockState state, Direction side) {
		return getFacingPropOptional(state).isPresent()
				&& state.getProperties().stream().noneMatch(p -> p.getValueClass().equals(RailShape.class));
	}

	private static BlockState applyFacingRotationChange(BlockState state, Direction side, Predicate<BlockState> canSurvive) {
		Property<Direction> facingProp = getFacingPropOptional(state).orElseThrow();
		Direction oldDir = state.getValue(facingProp);
		List<Direction> possibleFacingValues = new ArrayList<>(BlockStateProperties.FACING.getPossibleValues());
		if (possibleFacingValues.retainAll(facingProp.getPossibleValues())) {
			// doesn't support all possible directions
			if (possibleFacingValues.isEmpty()) {
				// How did we get here?
				return state;
			}

			// iterate over values in the order defined by BlockStateProperties.FACING,
			// because it makes more sense than the native order of the Direction enum values
			return iterateToNextValidPropertyValue(state, facingProp, possibleFacingValues, oldDir, canSurvive);
		}

		if (oldDir.getAxis() != side.getAxis()) {
			// rotate clockwise around clicked side
			return rotateClockwiseAroundSideDirect(state, side, canSurvive, facingProp, oldDir);
		}

		// facing towards or away from clicked side, flip around
		BlockState newState = state.setValue(facingProp, oldDir.getOpposite());
		return canSurvive.test(newState) ? newState : state;
	}

	private static BlockState applyOrientationRotationChange(BlockState oldState, Direction side, Predicate<BlockState> canSurvive) {
		FrontAndTop orientation = oldState.getValue(BlockStateProperties.ORIENTATION);
		Direction oldFront = orientation.front();
		Direction oldTop = orientation.top();

		if (side.getAxis() == Direction.Axis.Y) {
			// rotate horizontally
			Function<Direction, BlockState> newStateFunction;
			Direction oldDir;
			if (oldTop == Direction.UP) {
				// rotate front
				newStateFunction = newFront -> oldState.setValue(BlockStateProperties.ORIENTATION,
						FrontAndTop.fromFrontAndTop(newFront, oldTop));
				oldDir = oldFront;
			} else {
				// front is up or down, rotate top
				newStateFunction = newTop -> oldState.setValue(BlockStateProperties.ORIENTATION,
						FrontAndTop.fromFrontAndTop(oldFront, newTop));
				oldDir = oldTop;
			}
			return rotateClockwiseAroundSide(side, oldDir, newStateFunction, canSurvive);
		}

		if (oldFront.getAxis() == side.getAxis()) {
			// clicked front or back of horizontally-facing block, flip front to back (top can only be up)
			BlockState newState = oldState.setValue(BlockStateProperties.ORIENTATION,
					FrontAndTop.fromFrontAndTop(oldFront.getOpposite(), oldTop));
			return canSurvive.test(newState) ? newState : oldState;
		}

		// rotate around the horizontal axis perpendicular to front and top; top must be up if front is horizontal
		Direction newFront = getClockwiseDirectionForSide(oldFront, side);
		boolean isTopClockwise = newFront == oldTop;
		Function<Direction, BlockState> newStateFunction = newDir -> {
			Direction newTop = newDir.getAxis() == Direction.Axis.Y
					? getClockwiseDirectionForSide(newDir, isTopClockwise ? side : side.getOpposite())
					: Direction.UP;
			return oldState.setValue(BlockStateProperties.ORIENTATION, FrontAndTop.fromFrontAndTop(newDir, newTop));
		};

		return rotateClockwiseAroundSide(side, oldFront, newStateFunction, canSurvive);
	}

	private static Optional<RailShape> bendRailEnd(Collection<RailShape> possibleShapes, RailShape oldShape, Direction from, Direction to) {
		SortedPair<Direction> endDirections = RAIL_SHAPE_DIRECTIONS.get(oldShape);
		//region parameter checks: from must be an end of the existing shape, to must be perpendicular to from
		if (!endDirections.contains(from)) {
			throw new IllegalArgumentException("Rail shape %s has no end towards %s".formatted(oldShape, from));
		}
		if (from.getAxis() == to.getAxis()) {
			throw new IllegalArgumentException(
					"Cannot bend %s to opposite direction %s->%s".formatted(oldShape, from, to));
		}
		//endregion

		if (to == Direction.UP) {
			RailShape raisedShape = DIRECTION_RAISED_RAIL.get(from);
			return raisedShape != null && possibleShapes.contains(raisedShape)
					? Optional.of(raisedShape)
					: Optional.empty();
		}

		if (to == Direction.DOWN) {
			//region sanity check: throw if from is not the raised side
			if (RAIL_RAISED_DIRECTION.get(oldShape) != from) {
				throw new IllegalArgumentException(
						"Trying to lower %s side of a rail that is not raised: %s".formatted(from, oldShape));
			}
			//endregion

			RailShape loweredShape = DIRECTION_FLAT_STRAIGHT_RAIL.get(from);
			return possibleShapes.contains(loweredShape) ? Optional.of(loweredShape) : Optional.empty();
		}

		SortedPair<Direction> newDirections;
		if (endDirections.contains(to.getOpposite())) {
			// straightening the rail
			newDirections = SortedPair.of(to, to.getOpposite());
		} else {
			// bending the rail, or rotating already bent rail
			newDirections = SortedPair.of(to, from.getOpposite());
		}
		RailShape newShape = DIRECTION_RAIL_SHAPES.get(newDirections).getFirst();
		return possibleShapes.contains(newShape) ? Optional.of(newShape) : Optional.empty();
	}

	private static void defineRailEndBendingInteractions(Collection<RailShape> possibleShapes,
			List<Pair<Vec3, List<RailShape>>> newShapes, Direction clickedSide, RailShape oldShape,
			Direction thisEnd, Direction otherEnd, @Nullable Direction raisedEnd) {
		Direction cwDir = thisEnd.getClockWise();
		Direction ccwDir = thisEnd.getCounterClockWise();
		if (clickedSide == thisEnd || clickedSide.getAxis() == Direction.Axis.Y) {
			// raise or lower this end, potentially straightening the rail
			bendRailEnd(possibleShapes, oldShape, thisEnd, thisEnd == raisedEnd ? Direction.DOWN : Direction.UP)
					.ifPresent(newShape -> newShapes.add(Pair.of(
							new Vec3(thisEnd.step()).scale(RAIL_INTERACTION_SIDE_OFFSET),
							List.of(oldShape, newShape))));

			if (clickedSide == thisEnd || cwDir != otherEnd) {
				// bend this end of the rail clockwise
				bendRailEnd(possibleShapes, oldShape, thisEnd, cwDir).ifPresent(
						newShape -> newShapes.add(Pair.of(
								new Vec3(thisEnd.step().add(cwDir.step())).scale(RAIL_INTERACTION_SIDE_OFFSET),
								List.of(oldShape, newShape))));
			}

			if (clickedSide == thisEnd || ccwDir != otherEnd) {
				// bend this end of the rail counter-clockwise
				bendRailEnd(possibleShapes, oldShape, thisEnd, ccwDir).ifPresent(
						newShape -> newShapes.add(Pair.of(
								new Vec3(thisEnd.step().add(ccwDir.step())).scale(RAIL_INTERACTION_SIDE_OFFSET),
								List.of(oldShape, newShape))));
			}
		}
		if (thisEnd.getAxis() != otherEnd.getAxis() && clickedSide == otherEnd.getOpposite()) {
			// straighten rail
			bendRailEnd(possibleShapes, oldShape, otherEnd, thisEnd.getOpposite()).ifPresent(
					newShape -> newShapes.add(Pair.of(
							new Vec3(clickedSide.step().sub(thisEnd.step())).scale(RAIL_INTERACTION_SIDE_OFFSET),
							List.of(oldShape, DIRECTION_FLAT_STRAIGHT_RAIL.get(thisEnd)))));
		}
	}

	@Nullable
	public static BlockState applyRailShapeChange(BlockState state, Direction side, Vec3 relativePos,
			Predicate<BlockState> canSurvive) {
		Property<RailShape> shapeProp = getRailShapePropOptional(state).orElseThrow();
		Collection<RailShape> possibleShapes = shapeProp.getPossibleValues();
		//noinspection SlowListContainsAll
		if (STRAIGHT_SHAPES_CLOCKWISE.containsAll(possibleShapes)
				|| state.getProperties().stream().anyMatch(p -> p.getName().equals("facing"))) {
			// there's probably something special about these rails, don't touch them here
			return null;
		}

		RailShape oldShape = state.getValue(shapeProp);
		SortedPair<Direction> endDirections = RAIL_SHAPE_DIRECTIONS.get(oldShape);
		@Nullable
		Direction raisedEnd = RAIL_RAISED_DIRECTION.get(oldShape);

		List<Pair<Vec3, List<RailShape>>> shapeInteractions = new ArrayList<>();

		Direction endA = endDirections.first();
		Direction endB = endDirections.second();
		if (side.getAxis() == Direction.Axis.Y) {
			// center interaction point for clicks on top or bottom: rotate the rail as-is (CCW if clicked bottom side)
			List<RailShape> shapeList = raisedEnd != null
					? ASCENDING_SHAPES_CLOCKWISE
					: endA.getAxis() == endB.getAxis() ? STRAIGHT_SHAPES_CLOCKWISE : BENT_SHAPES_CLOCKWISE;
			shapeInteractions.add(Pair.of(Vec3.ZERO, side == Direction.UP ? shapeList : shapeList.reversed()));

		} else if (!endDirections.contains(side)) {
			// center interaction point for clicks on any side that is not a connecting end of the rail
			Direction left = side.getClockWise();
			Direction right = side.getCounterClockWise();
			List<RailShape> newShapes = new ArrayList<>(DIRECTION_RAIL_SHAPES.get(SortedPair.of(left, right)));
			newShapes.retainAll(possibleShapes);
			shapeInteractions.add(Pair.of(new Vec3(side.step()).scale(RAIL_INTERACTION_SIDE_OFFSET),
					side.getAxisDirection() == Direction.AxisDirection.POSITIVE ? newShapes.reversed() : newShapes));
		}

		defineRailEndBendingInteractions(possibleShapes, shapeInteractions, side, oldShape, endA, endB, raisedEnd);
		defineRailEndBendingInteractions(possibleShapes, shapeInteractions, side, oldShape, endB, endA, raisedEnd);

		double bestDist = Double.POSITIVE_INFINITY;
		List<RailShape> bestShapes = List.of(oldShape);
		for (var entry : shapeInteractions) {
			Vec3 interactionPoint = entry.first();
			double dist = interactionPoint.distanceToSqr(relativePos);
			if (dist >= bestDist) {
				continue;
			}
			bestShapes = entry.second();
			bestDist = dist;
		}
		boolean found = bestShapes.getLast() == oldShape;
		for (RailShape shape : bestShapes) {
			if (shape == oldShape) {
				found = true;
			} else if (found) {
				BlockState newState = state.setValue(shapeProp, shape);
				if (canSurvive.test(newState)) {
					return newState;
				}
			}
		}
		BlockState newState = state.setValue(shapeProp, bestShapes.getFirst());
		if (canSurvive.test(newState)) {
			return newState;
		}

		return state;
	}

	@Nullable
	private static BlockState applyVanillaRotation(BlockState oldState, Direction side, Predicate<BlockState> canSurvive) {
		for (Rotation rot : new Rotation[] { Rotation.CLOCKWISE_90, Rotation.CLOCKWISE_180, Rotation.COUNTERCLOCKWISE_90 }) {
			BlockState newState = oldState.rotate(rot);
			if (newState != oldState && canSurvive.test(newState)) {
				return newState;
			}
		}
		return null;
	}

	private static BlockState rotateClockwiseAroundSideDirect(BlockState oldState, Direction side,
			Predicate<BlockState> canSurvive, Property<Direction> facingProp, Direction oldDir) {
		return rotateClockwiseAroundSide(side, oldDir, dir -> oldState.setValue(facingProp, dir), canSurvive);
	}

	private static BlockState rotateClockwiseAroundSide(Direction side, Direction oldDir,
			Function<Direction, BlockState> newStateFunction, Predicate<BlockState> canSurvive) {
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

	private static <T extends Comparable<T>> BlockState iterateToNextValidPropertyValue(BlockState oldState,
			Property<T> property, Collection<T> orderedValues, T oldValue, Predicate<BlockState> canSurvive) {
		Iterator<T> it = orderedValues.iterator();
		//noinspection StatementWithEmptyBody
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
