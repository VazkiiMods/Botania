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
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.ForceRelayBlock;
import vazkii.botania.test.TestingUtil;

public class ForceRelayTest {
	@GameTest(template = "botania:block/piston_relay_basic")
	public void testBasic(GameTestHelper helper) {
		var initialRelay = new BlockPos(4, 2, 2);
		var initialAndesite = new BlockPos(4, 4, 5);
		var initialSlimeUnderAndesite = initialAndesite.below();
		var initialDiorite = new BlockPos(3, 4, 5);
		var initialSlimeUnderDiorite = initialDiorite.below();

		var data = ForceRelayBlock.WorldData.get(helper.getLevel());
		data.mapping.put(helper.absolutePos(initialRelay), helper.absolutePos(initialSlimeUnderDiorite));

		helper.startSequence().thenExecute(() -> {
			var activateFirstPiston = new BlockPos(4, 2, 0);
			helper.setBlock(activateFirstPiston, Blocks.REDSTONE_BLOCK);
		}).thenExecuteAfter(4, () -> {
			helper.assertBlockPresent(BotaniaBlocks.pistonRelay, initialRelay.south());
			helper.assertBlockPresent(Blocks.ANDESITE, initialAndesite.south());
			helper.assertBlockPresent(Blocks.SLIME_BLOCK, initialSlimeUnderAndesite.south());
			helper.assertBlockPresent(Blocks.DIORITE, initialDiorite.south());
			helper.assertBlockPresent(Blocks.SLIME_BLOCK, initialSlimeUnderDiorite.south());
		}).thenExecute(() -> {
			var activateSecondPiston = new BlockPos(6, 2, 3);
			helper.setBlock(activateSecondPiston, Blocks.REDSTONE_BLOCK);
		}).thenExecuteAfter(4, () -> {
			var relayPos = initialRelay.south().west();
			helper.assertBlockPresent(BotaniaBlocks.pistonRelay, relayPos);
			helper.assertBlockPresent(Blocks.ANDESITE, initialAndesite.south().west());
			helper.assertBlockPresent(Blocks.SLIME_BLOCK, initialSlimeUnderAndesite.south().west());
			helper.assertBlockPresent(Blocks.DIORITE, initialDiorite.south().west());
			helper.assertBlockPresent(Blocks.SLIME_BLOCK, initialSlimeUnderDiorite.south().west());

			helper.destroyBlock(relayPos);
			TestingUtil.assertThat(!data.mapping.containsKey(relayPos), () -> "Removing relay should remove internal mapping");
		}).thenSucceed();
	}

	@GameTest(template = "botania:block/piston_relay_immovable")
	public void testImmovable(GameTestHelper helper) {
		var initialRelay = new BlockPos(1, 2, 1);
		var initialCobble = new BlockPos(1, 2, 0);

		var data = ForceRelayBlock.WorldData.get(helper.getLevel());
		data.mapping.put(helper.absolutePos(initialRelay), helper.absolutePos(initialCobble));

		helper.startSequence().thenExecute(() -> {
			var lever = new BlockPos(2, 2, 2);
			helper.pullLever(lever);
		}).thenExecuteAfter(4, () -> {
			var relayAfter = new BlockPos(0, 2, 1);
			helper.assertBlockPresent(BotaniaBlocks.pistonRelay, relayAfter);
			helper.assertBlockPresent(Blocks.COBBLESTONE, initialCobble);
			TestingUtil.assertEquals(data.mapping.get(helper.absolutePos(relayAfter)),
					helper.absolutePos(initialCobble),
					() -> "If destination block cannot move, the relay should move but retain " +
							"binding to the destination block's original position");
		}).thenSucceed();
	}

	@GameTest(template = "botania:block/piston_relay_sticky_move_no_pull")
	public void testStickyPistonMoveNotPropagated(GameTestHelper helper) {
		final var initialRelay = new BlockPos(1, 2, 2);
		final var initialGranite = new BlockPos(3, 2, 3);
		final var stickyPistonButton = new BlockPos(1, 2, 0);
		final var nonstickyPistonButton = new BlockPos(1, 2, 5);

		final var data = ForceRelayBlock.WorldData.get(helper.getLevel());
		data.mapping.put(helper.absolutePos(initialRelay), helper.absolutePos(initialGranite));

		helper.startSequence()
				.thenExecute(() -> helper.pressButton(stickyPistonButton))
				.thenExecuteAfter(4, () -> {
					// relay should have moved, but not the bound block
					final var relayAfter = initialRelay.south();
					helper.assertBlockPresent(BotaniaBlocks.pistonRelay, relayAfter);
					helper.assertBlockPresent(Blocks.POLISHED_GRANITE, initialGranite);
					TestingUtil.assertEquals(data.mapping.get(helper.absolutePos(relayAfter)),
							helper.absolutePos(initialGranite),
							() -> "If relay is moved directly via sticky piston, it should update its source " +
									"location, but retain the binding to the destination block's original position");
					TestingUtil.assertEquals(data.mapping.get(helper.absolutePos(initialRelay)), null,
							() -> "If relay is moved for whatever reason, its location data should be updated");
				})
				.thenExecuteAfter(20, () -> {
					// piston should have retracted by now, nothing else should have changed
					final var relayAfter = initialRelay.south();
					helper.assertBlockPresent(BotaniaBlocks.pistonRelay, relayAfter);
					helper.assertBlockPresent(Blocks.POLISHED_GRANITE, initialGranite);
					TestingUtil.assertEquals(data.mapping.get(helper.absolutePos(relayAfter)),
							helper.absolutePos(initialGranite),
							() -> "If a sticky piston retracts from the relay, neither the relay nor its bound " +
									"location should change");
					TestingUtil.assertEquals(data.mapping.get(helper.absolutePos(initialRelay)), null,
							() -> "If relay is not moved, its location data should not change");
				})
				.thenExecute(() -> helper.pressButton(nonstickyPistonButton))
				.thenExecuteAfter(4, () -> {
					// relay should have moved back and also moved the bound block
					final var graniteAfter = initialGranite.north();
					helper.assertBlockPresent(BotaniaBlocks.pistonRelay, initialRelay);
					helper.assertBlockPresent(Blocks.POLISHED_GRANITE, graniteAfter);
					TestingUtil.assertEquals(data.mapping.get(helper.absolutePos(initialRelay)),
							helper.absolutePos(graniteAfter),
							() -> "If relay is moved directly via non-sticky piston, " +
									"it should update its source and bound locations");
					TestingUtil.assertEquals(data.mapping.get(helper.absolutePos(initialRelay.south())), null,
							() -> "If relay is moved for whatever reason, its location data should be updated");
				})
				.thenSucceed();
	}

	@GameTest(template = "botania:block/piston_relay_structure_pull")
	public void testMovingInPistonStructure(GameTestHelper helper) {
		final var button = new BlockPos(0, 2, 1);
		final var initialRelay1 = new BlockPos(3, 2, 2);
		final var initialRelay2 = new BlockPos(3, 2, 5);
		final var initialBoundSlime = new BlockPos(2, 2, 4);
		final var initialAndesite = new BlockPos(3, 4, 3);

		final var data = ForceRelayBlock.WorldData.get(helper.getLevel());
		data.mapping.put(helper.absolutePos(initialRelay1), helper.absolutePos(initialBoundSlime));
		data.mapping.put(helper.absolutePos(initialRelay2), helper.absolutePos(initialAndesite));

		helper.startSequence()
				.thenExecute(() -> helper.pressButton(button))
				.thenExecuteAfter(4, () -> {
					// sticky piston should have moved the block structure with the first relay,
					// which should have moved the other block structure with the second relay,
					// which should have moved the andesite block
					helper.assertBlockPresent(BotaniaBlocks.pistonRelay, initialRelay1.east());
					helper.assertBlockPresent(BotaniaBlocks.pistonRelay, initialRelay2.east());
					helper.assertBlockState(initialBoundSlime, BlockState::isAir,
							() -> "Second slime block structure should have moved, " +
									"leaving behind an air block at the originally bound location");
					helper.assertBlockPresent(Blocks.POLISHED_ANDESITE, initialAndesite.east());
					TestingUtil.assertEquals(data.mapping.get(helper.absolutePos(initialRelay1.east())),
							helper.absolutePos(initialBoundSlime.east()),
							() -> "Even if the structure was pushed by a sticky piston, the relay and bound " +
									"locations should have been updated, since the relay was not pushed directly.");
					TestingUtil.assertEquals(data.mapping.get(helper.absolutePos(initialRelay2.east())),
							helper.absolutePos(initialAndesite.east()),
							() -> "If a relay is moved indirectly as part of a block structure, its location and the " +
									"bound block's location data should have been updated.");
					TestingUtil.assertEquals(data.mapping.get(helper.absolutePos(initialRelay1)), null,
							() -> "If relay is moved for whatever reason, its location data should be updated");
					TestingUtil.assertEquals(data.mapping.get(helper.absolutePos(initialRelay2)), null,
							() -> "If relay is moved for whatever reason, its location data should be updated");
				})
				.thenExecuteAfter(24, () -> {
					// the sticky piston should have retracted by now and everything should have moved back
					helper.assertBlockPresent(BotaniaBlocks.pistonRelay, initialRelay1);
					helper.assertBlockPresent(BotaniaBlocks.pistonRelay, initialRelay2);
					helper.assertBlockPresent(Blocks.SLIME_BLOCK, initialBoundSlime);
					helper.assertBlockPresent(Blocks.POLISHED_ANDESITE, initialAndesite);
					TestingUtil.assertEquals(data.mapping.get(helper.absolutePos(initialRelay1)),
							helper.absolutePos(initialBoundSlime),
							() -> "Even if the structure was pushed by a sticky piston, the relay and bound " +
									"locations should have been updated, since the relay was not pushed directly.");
					TestingUtil.assertEquals(data.mapping.get(helper.absolutePos(initialRelay2)),
							helper.absolutePos(initialAndesite),
							() -> "If a relay is moved indirectly as part of a block structure, its location and the " +
									"bound block's location data should have been updated.");
					TestingUtil.assertEquals(data.mapping.get(helper.absolutePos(initialRelay1.east())), null,
							() -> "If relay is moved for whatever reason, its location data should be updated");
					TestingUtil.assertEquals(data.mapping.get(helper.absolutePos(initialRelay2.east())), null,
							() -> "If relay is moved for whatever reason, its location data should be updated");
				})
				.thenSucceed();
	}
}
