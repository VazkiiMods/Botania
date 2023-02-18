package vazkii.botania.test.block;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.flower.generating.DandelifeonBlockEntity;

public class DandelifeonTest {
	private static final String TEMPLATE = "botania:block/dandelifeon_transfer";

	private static final BlockPos LEVER_POS = new BlockPos(29, 2, 12);
	private static final BlockPos TARGET_POS = new BlockPos(10, 3, 11);
	private static final BlockPos LOWER_SQUARE = new BlockPos(32, 3, 11);
	private static final BlockPos UPPER_SQUARE = new BlockPos(32, 3, 12);
	private static final int EXPECTED_MANA = 2460;

	@GameTest(template = TEMPLATE, timeoutTicks = 250)
	public void testDandelifeonTransferring(GameTestHelper helper) {
		helper.startSequence().thenExecute(() -> {
			helper.pullLever(LEVER_POS);
		}).thenExecuteAfter(24 * DandelifeonBlockEntity.SPEED, () -> {
			// test the still life is present
			for (int x = LOWER_SQUARE.getX(); x <= UPPER_SQUARE.getX(); x++) {
				for (int y = LOWER_SQUARE.getY(); y <= UPPER_SQUARE.getY(); y++) {
					for (int z = LOWER_SQUARE.getZ(); z <= UPPER_SQUARE.getZ(); z++) {
						helper.assertBlockPresent(BotaniaBlocks.cellBlock, new BlockPos(x, y, z));
					}
				}
			}
			var be = helper.getBlockEntity(TARGET_POS);
			if (!(be instanceof DandelifeonBlockEntity dandie)) {
				throw new GameTestAssertException("Missing Dandelifeon at: " + TARGET_POS);
			}
			// test the region was nuked
			int radius = dandie.getRange();
			for (int i = -radius; i <= radius; i++) {
				for (int j = -radius; j <= radius; j++) {
					helper.assertBlockNotPresent(BotaniaBlocks.cellBlock, new BlockPos(i + TARGET_POS.getX(), TARGET_POS.getY(), j + TARGET_POS.getZ()));
				}
			}
			// test mana value is correct
			if (dandie.getMana() != EXPECTED_MANA) {
				throw new GameTestAssertException("Wrong amount of mana: expected " + EXPECTED_MANA + " but was " + dandie.getMana());
			}
		}).thenSucceed();
	}
}
