package vazkii.botania.test.item.lens;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.test.TestingUtil;

public class BoreWarpLensTest {
	private static final String TEMPLATE = "botania:item/lens/bore_warp_relay_interaction";

	private static final BlockPos SPREADER_POS = new BlockPos(2, 2, 1);
	private static final BlockPos SPREADER_TARGET_POS = new BlockPos(5, 2, 1);
	private static final BlockPos BUTTON_POS = new BlockPos(2, 3, 2);
	private static final BlockPos RELAY_POS = new BlockPos(4, 2, 1);
	private static final BlockPos BOUND_POS = new BlockPos(1, 2, 4);
	private static final BlockPos TARGET_BLOCK_POS = new BlockPos(3, 2, 4);
	private static final BlockPos UNWARPED_HOPPER_POS = new BlockPos(3, 1, 4);
	private static final BlockPos WARPED_HOPPER_POS = new BlockPos(1, 1, 1);

	@GameTest(template = TEMPLATE, timeoutTicks = 25)
	public void testWarpBoreLens(GameTestHelper helper) {
		setUpLensesAndBindings(helper, BotaniaItems.lensWarp, BotaniaItems.lensMine);

		helper.startSequence()
				.thenExecuteAfter(1, () -> helper.pressButton(BUTTON_POS))
				.thenWaitUntil(() -> helper.assertBlockProperty(BUTTON_POS, ButtonBlock.POWERED, false))
				.thenExecute(() -> {
					helper.assertBlock(RELAY_POS, block -> block == BotaniaBlocks.pistonRelay, "Force relay was broken");
					helper.assertBlockState(TARGET_BLOCK_POS, BlockState::isAir, () -> "Target block was not broken");
					helper.assertContainerContains(UNWARPED_HOPPER_POS, Items.POLISHED_ANDESITE);
					helper.assertContainerEmpty(WARPED_HOPPER_POS);
				})
				.thenSucceed();
	}

	@GameTest(template = TEMPLATE, timeoutTicks = 25)
	public void testBoreWarpLens(GameTestHelper helper) {
		setUpLensesAndBindings(helper, BotaniaItems.lensMine, BotaniaItems.lensWarp);

		helper.startSequence()
				.thenExecuteAfter(1, () -> helper.pressButton(BUTTON_POS))
				.thenWaitUntil(() -> helper.assertBlockProperty(BUTTON_POS, ButtonBlock.POWERED, false))
				.thenExecute(() -> {
					helper.assertBlock(RELAY_POS, block -> block == BotaniaBlocks.pistonRelay, "Force relay was broken");
					helper.assertBlockState(TARGET_BLOCK_POS, BlockState::isAir, () -> "Target block was not broken");
					helper.assertContainerEmpty(UNWARPED_HOPPER_POS);
					helper.assertContainerContains(WARPED_HOPPER_POS, Items.POLISHED_ANDESITE);
				})
				.thenSucceed();
	}

	private static void setUpLensesAndBindings(GameTestHelper helper, Item firstLensType, Item secondLensType) {
		TestingUtil.setUpSpreaderAndCompositeLens(helper, firstLensType, secondLensType, SPREADER_POS, SPREADER_TARGET_POS);
		TestingUtil.bindForceRelayTarget(helper, RELAY_POS, BOUND_POS);
	}

}
