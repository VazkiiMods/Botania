/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.test;

import org.junit.jupiter.api.Test;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.impl.BotaniaAPIClientImpl;
import vazkii.botania.common.impl.BotaniaAPIImpl;
import vazkii.botania.common.impl.corporea.CorporeaHelperImpl;
import vazkii.botania.common.impl.mana.ManaItemHandlerImpl;

/**
 * Basic smoke test to ensure the class name strings in the API are still accurate
 */
public class APITest {
	@Test
	public void testManaItemHandler() {
		assertEquals(ManaItemHandler.instance().getClass(), ManaItemHandlerImpl.class);
	}

	@Test
	public void testCorporeaHelper() {
		assertEquals(CorporeaHelper.instance().getClass(), CorporeaHelperImpl.class);
	}

	@Test
	public void testMainAPI() {
		assertEquals(BotaniaAPI.instance().getClass(), BotaniaAPIImpl.class);
	}

	@Test
	public void testClientAPI() {
		assertEquals(BotaniaAPIClient.instance().getClass(), BotaniaAPIClientImpl.class);
	}
}
