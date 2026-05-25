package vazkii.botania.api.state.enums;

import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Rotation;

import vazkii.botania.common.block.AnimatedTorchBlock;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.UnaryOperator;

public enum TorchMode implements StringRepresentable {
	TOGGLE(Direction::getOpposite),
	ROTATE(Rotation.CLOCKWISE_90::rotate),
	RANDOM(direction -> random());

	private static final Random rng = new Random();
	private final UnaryOperator<Direction> facingUpdater;

	private static Direction random() {
		List<Direction> possibleValues = List.copyOf(AnimatedTorchBlock.FACING.getPossibleValues());
		return possibleValues.get(rng.nextInt(possibleValues.size()));
	}

	TorchMode(UnaryOperator<Direction> facingUpdater) {
		this.facingUpdater = facingUpdater;
	}

	public Direction getNewFacing(Direction direction) {
		return facingUpdater.apply(direction);
	}

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
