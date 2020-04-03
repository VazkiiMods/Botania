package vazkii.botania.test;

import org.junit.jupiter.api.Test;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.impl.ManaItemHandlerImpl;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Basic smoke test to ensure the class name strings in the API are still accurate
 */
public class APITest {
	@Test
	public void testManaItemHandler() {
		assertEquals(ManaItemHandler.instance().getClass(), ManaItemHandlerImpl.class);
	}
}
