package vazkii.botania.common.helper;

import net.minecraft.core.BlockPos;

import java.util.ArrayDeque;
import java.util.Deque;

public class ForcePushHelper implements AutoCloseable {
	/**
	 * Keeps track of push origins for nested force relay moves.
	 */
	private static final ThreadLocal<Deque<BlockPos>> forcePushOriginStack = ThreadLocal.withInitial(ArrayDeque::new);

	/**
	 * Keeps track of nested block push movement types (extending=true or retracting=false).
	 */
	private static final ThreadLocal<Deque<Boolean>> movementTypeContextStack = ThreadLocal.withInitial(ArrayDeque::new);

	public static boolean isForcePush() {
		return !forcePushOriginStack.get().isEmpty();
	}

	public static BlockPos getForcePushOrigin() {
		if (!isForcePush()) {
			throw new IllegalStateException("Not currently performing a Force Relay or Force Lens push");
		}
		return forcePushOriginStack.get().peek();
	}

	public static void pushMovementTypeContext(boolean extending) {
		movementTypeContextStack.get().push(extending);
	}

	public static void popMovementTypeContext() {
		movementTypeContextStack.get().pop();
	}

	public static boolean isExtendingMovementContext() {
		return movementTypeContextStack.get().peek() == Boolean.TRUE;
	}

	public ForcePushHelper(BlockPos pushLocation) {
		forcePushOriginStack.get().push(pushLocation.immutable());
	}

	@Override
	public void close() {
		forcePushOriginStack.get().pop();
	}
}
