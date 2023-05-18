package vazkii.botania.common.helper;

import org.apache.commons.lang3.mutable.MutableInt;

public class ForcePushHelper implements AutoCloseable {
	private static final ThreadLocal<MutableInt> forcePushCounter = ThreadLocal.withInitial(MutableInt::new);

	public static boolean isForcePush() {
		return forcePushCounter.get().intValue() > 0;
	}

	public ForcePushHelper() {
		forcePushCounter.get().increment();
	}

	@Override
	public void close() {
		forcePushCounter.get().decrement();
	}
}
