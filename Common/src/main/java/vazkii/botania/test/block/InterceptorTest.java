/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.test.block;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertPosException;
import net.minecraft.gametest.framework.GameTestHelper;

import vazkii.botania.common.block.tile.corporea.TileCorporeaRetainer;

public class InterceptorTest {

	private static final String TEMPLATE = "botania:block/interceptor";

	private static final BlockPos APPLE_BUTTON_JEANS = new BlockPos(6, 6, 4);
	private static final BlockPos BOOTS_WITH_THE_FUR = new BlockPos(4, 6, 4);
	private static final BlockPos WILDCARD_RETAINER = new BlockPos(6, 5, 7);
	private static final BlockPos BOOTS_RETAINER = new BlockPos(4, 5, 7);

	@GameTest(template = TEMPLATE)
	public void testAppleRequest(GameTestHelper helper) {
		helper.pressButton(APPLE_BUTTON_JEANS);
		helper.startSequence().thenExecuteAfter(1, () -> {
			assertHasPendingRequest(helper, WILDCARD_RETAINER);
			assertHasNoPendingRequest(helper, BOOTS_RETAINER);
		}).thenSucceed();
	}

	@GameTest(template = TEMPLATE)
	public void testBootsRequest(GameTestHelper helper) {
		helper.pressButton(BOOTS_WITH_THE_FUR);
		helper.startSequence().thenExecuteAfter(1, () -> {
			assertHasPendingRequest(helper, WILDCARD_RETAINER);
			assertHasPendingRequest(helper, BOOTS_RETAINER);
		}).thenSucceed();
	}

	private static void assertHasPendingRequest(GameTestHelper helper, BlockPos retainerPos) {
		assertHasPendingRequest(helper, retainerPos, false);
	}

	private static void assertHasPendingRequest(GameTestHelper helper, BlockPos retainerPos, boolean invert) {
		TileCorporeaRetainer retainer = (TileCorporeaRetainer) helper.getBlockEntity(retainerPos);
		if (retainer == null) {
			throw new GameTestAssertPosException("Expected corporea retainer", helper.absolutePos(retainerPos), retainerPos, helper.getTick());
		}
		if (!retainer.hasPendingRequest() ^ invert) {
			throw new GameTestAssertPosException("Expected corporea retainer to " + (invert ? "not " : "") + "have a pending request",
					helper.absolutePos(retainerPos), retainerPos, helper.getTick());
		}
	}

	private static void assertHasNoPendingRequest(GameTestHelper helper, BlockPos retainerPos) {
		assertHasPendingRequest(helper, retainerPos, true);
	}

}
