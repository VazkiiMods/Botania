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

import vazkii.botania.common.block.BlockPistonRelay;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.test.TestingUtil;

public class PistonRelayTest {
	@GameTest(template = "botania:block/piston_relay_basic")
	public void testBasic(GameTestHelper helper) {
		var initialRelay = new BlockPos(4, 2, 2);
		var initialAndesite = new BlockPos(4, 4, 5);
		var initialSlimeUnderAndesite = initialAndesite.below();
		var initialDiorite = new BlockPos(3, 4, 5);
		var initialSlimeUnderDiorite = initialDiorite.below();

		var data = BlockPistonRelay.WorldData.get(helper.getLevel());
		data.mapping.put(helper.absolutePos(initialRelay), helper.absolutePos(initialSlimeUnderDiorite));

		helper.startSequence().thenExecute(() -> {
			var activateFirstPiston = new BlockPos(4, 2, 0);
			helper.setBlock(activateFirstPiston, Blocks.REDSTONE_BLOCK);
		}).thenExecuteAfter(4, () -> {
			helper.assertBlockPresent(ModBlocks.pistonRelay, initialRelay.south());
			helper.assertBlockPresent(Blocks.ANDESITE, initialAndesite.south());
			helper.assertBlockPresent(Blocks.SLIME_BLOCK, initialSlimeUnderAndesite.south());
			helper.assertBlockPresent(Blocks.DIORITE, initialDiorite.south());
			helper.assertBlockPresent(Blocks.SLIME_BLOCK, initialSlimeUnderDiorite.south());
		}).thenExecute(() -> {
			var activateSecondPiston = new BlockPos(6, 2, 3);
			helper.setBlock(activateSecondPiston, Blocks.REDSTONE_BLOCK);
		}).thenExecuteAfter(4, () -> {
			var relayPos = initialRelay.south().west();
			helper.assertBlockPresent(ModBlocks.pistonRelay, relayPos);
			// Andesite should not have moved
			helper.assertBlockPresent(Blocks.ANDESITE, initialAndesite.south());
			helper.assertBlockPresent(Blocks.SLIME_BLOCK, initialSlimeUnderAndesite.south());
			helper.assertBlockPresent(Blocks.DIORITE, initialDiorite.south().west());
			helper.assertBlockPresent(Blocks.SLIME_BLOCK, initialSlimeUnderDiorite.south().west());

			helper.destroyBlock(relayPos);
			TestingUtil.assertThat(!data.mapping.containsKey(relayPos), () -> "Removing relay should remove internal mapping");
		}).thenSucceed();
	}
}
