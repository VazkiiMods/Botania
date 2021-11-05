/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.test;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

import vazkii.botania.common.block.ModBlocks;

public class TestTest {
	@GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
	public void testTest1(GameTestHelper helper) {
		helper.setBlock(BlockPos.ZERO, ModBlocks.whiteFlower.defaultBlockState());
		helper.succeedIf(() -> helper.assertBlockPresent(ModBlocks.whiteFlower, BlockPos.ZERO));
	}
}
