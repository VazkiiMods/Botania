package vazkii.botania.test.item.lens;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.lens.LensItem;
import vazkii.botania.test.TestingUtil;

public class WarpForceTest {
	private static final String TEMPLATE = "botania:item/lens/force_warp_relay_interaction";

	private static final BlockPos SPREADER_POS = new BlockPos(1, 2, 1);
	private static final BlockPos SPREADER_TARGET_POS = new BlockPos(6, 2, 1);
	private static final BlockPos BUTTON_POS = new BlockPos(1, 2, 3);
	private static final BlockPos RELAY_POS = new BlockPos(3, 2, 1);
	private static final BlockPos BOUND_POS = new BlockPos(3, 2, 3);

	@GameTest(template = TEMPLATE, timeoutTicks = 50)
	public void testWarpForceLens(GameTestHelper helper) {
		setUpLensesAndBindings(helper, BotaniaItems.lensWarp, BotaniaItems.lensPiston);

		helper.startSequence()
				// short delay to ensure spreader picks up mana from pool
				.thenExecuteAfter(1, () -> helper.pressButton(BUTTON_POS))
				.thenWaitUntil(() -> helper.assertBlockProperty(BUTTON_POS, ButtonBlock.POWERED, false))
				.thenExecute(() -> {
					helper.assertBlock(RELAY_POS, BotaniaBlocks.pistonRelay::equals, () -> "Force relay moved");
					helper.assertBlockState(BOUND_POS, BlockState::isAir, () -> "Bound block did not move");
					helper.assertBlock(BOUND_POS.east(), Blocks.POLISHED_ANDESITE::equals,
							() -> "Bound block did not move to expected position");
					TestingUtil.assertEquals(TestingUtil.getBoundForceRelayTarget(helper, RELAY_POS), helper.absolutePos(BOUND_POS),
							() -> "Relay binding has changed");
				})
				// second shot to confirm binding still works
				.thenExecute(() -> helper.setBlock(BOUND_POS, Blocks.POLISHED_DIORITE))
				.thenExecute(() -> helper.pressButton(BUTTON_POS))
				.thenWaitUntil(() -> helper.assertBlockProperty(BUTTON_POS, ButtonBlock.POWERED, false))
				.thenExecute(() -> {
					helper.assertBlockState(RELAY_POS, blockState -> blockState.is(BotaniaBlocks.pistonRelay),
							() -> "Force relay moved after second burst");
					helper.assertBlockState(BOUND_POS, BlockState::isAir, () -> "Bound block did not move");
					helper.assertBlock(BOUND_POS.east(), Blocks.POLISHED_DIORITE::equals,
							() -> "New block did not move to expected position after second burst");
					helper.assertBlock(BOUND_POS.east(2), Blocks.POLISHED_ANDESITE::equals,
							() -> "Original block did not move to expected position after second burst");
					TestingUtil.assertEquals(TestingUtil.getBoundForceRelayTarget(helper, RELAY_POS), helper.absolutePos(BOUND_POS),
							() -> "Relay binding has changed after second burst");
				})
				.thenSucceed();
	}

	// TODO: Regression test for https://github.com/VazkiiMods/Botania/issues/4593
	@GameTest(template = TEMPLATE, timeoutTicks = 50, required = false)
	public void testForceWarpLens(GameTestHelper helper) {
		setUpLensesAndBindings(helper, BotaniaItems.lensPiston, BotaniaItems.lensWarp);

		helper.startSequence()
				// short delay to ensure spreader picks up mana from pool
				.thenExecuteAfter(1, () -> helper.pressButton(BUTTON_POS))
				.thenWaitUntil(() -> helper.assertBlockProperty(BUTTON_POS, ButtonBlock.POWERED, false))
				.thenExecute(() -> {
					helper.assertBlockState(RELAY_POS, BlockState::isAir, () -> "Force relay did not move");
					helper.assertBlock(RELAY_POS.east(), BotaniaBlocks.pistonRelay::equals,
							() -> "Force relay did not move to expected position");
					helper.assertBlockState(BOUND_POS, BlockState::isAir, () -> "Bound block did not move");
					helper.assertBlock(BOUND_POS.east(), Blocks.POLISHED_ANDESITE::equals,
							() -> "Bound block did not move to expected position");
					TestingUtil.assertEquals(TestingUtil.getBoundForceRelayTarget(helper, RELAY_POS.east()), BOUND_POS.east(),
							() -> "Relay binding was not updated");
				})
				// second shot to confirm binding still works
				.thenExecute(() -> helper.setBlock(BOUND_POS, Blocks.POLISHED_DIORITE))
				.thenExecute(() -> helper.pressButton(BUTTON_POS))
				.thenWaitUntil(() -> helper.assertBlockProperty(BUTTON_POS, ButtonBlock.POWERED, false))
				.thenExecute(() -> {
					helper.assertBlock(BOUND_POS, Blocks.POLISHED_DIORITE::equals, () -> "New block at original bound position moved");
					helper.assertBlockState(RELAY_POS.east(), BlockState::isAir, () -> "Force relay did not move after second burst");
					helper.assertBlock(RELAY_POS.east(2), BotaniaBlocks.pistonRelay::equals,
							() -> "Force relay did not move to expected position after second burst");
					helper.assertBlockState(BOUND_POS.east(), BlockState::isAir, () -> "Bound block did not move a second time");
					helper.assertBlock(BOUND_POS.east(2), Blocks.POLISHED_ANDESITE::equals,
							() -> "Bound block did not move to expected position after second burst");
					TestingUtil.assertEquals(TestingUtil.getBoundForceRelayTarget(helper, RELAY_POS.east(2)), BOUND_POS.east(2),
							() -> "Relay binding was not updated after second burst");
				})
				.thenSucceed();
	}

	private static void setUpLensesAndBindings(GameTestHelper helper, Item firstLensType, Item secondLensType) {
		final var warpLensStack = new ItemStack(firstLensType);
		final var forceLensStack = new ItemStack(secondLensType);
		final var compositeLens = ((LensItem) warpLensStack.getItem()).setCompositeLens(warpLensStack, forceLensStack);
		final var spreaderEntity = TestingUtil.assertBlockEntity(helper, SPREADER_POS, BotaniaBlockEntities.SPREADER);
		spreaderEntity.getItemHandler().setItem(0, compositeLens);

		TestingUtil.bindWithWandOfTheForest(helper, SPREADER_POS, SPREADER_TARGET_POS);
		TestingUtil.bindForceRelayTarget(helper, RELAY_POS, BOUND_POS);
	}
}
