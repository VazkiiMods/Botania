package vazkii.botania.test.block;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.phys.AABB;

import vazkii.botania.common.block.flower.functional.RannuncarpusBlockEntity;
import vazkii.botania.common.helper.DelayHelper;
import vazkii.botania.test.TestingUtil;

public class RannuncarpusTest {
	private static final String TEMPLATE = "botania:block/rannuncarpus";
	private static final String TEMPLATE_CANDLES = "botania:block/rannuncarpus_candles";

	private static final BlockPos FLOWER_POS = new BlockPos(2, 3, 2);
	private static final BlockPos FILTER_POS = FLOWER_POS.below();

	private static final BlockPos EXPECTED_PLACE_POS = new BlockPos(3, 2, 2);
	private static final BlockPos FILTERED_POS = EXPECTED_PLACE_POS.below();

	private static final BlockPos MAGENTA_CANDLE_POS = new BlockPos(3, 3, 3);
	private static final BlockPos EXPECTED_PLACE_POS1 = new BlockPos(1, 3, 3);
	private static final BlockPos EXPECTED_PLACE_POS2 = new BlockPos(3, 3, 1);
	private static final BlockPos SEA_PICKLE_POS = new BlockPos(1, 2, 1);
	private static final BlockPos FLOATING_YELLOW_CANDLE_POS = new BlockPos(1, 5, 2);

	@GameTest(template = TEMPLATE, batch = "rannuncarpus1")
	public void testDestinationFilterPositive(GameTestHelper helper) {
		helper.startSequence().thenExecute(() -> {
			helper.killAllEntities();
			helper.spawnItem(Blocks.STONE.asItem(), FLOWER_POS.getX(), FLOWER_POS.getY() + 1, FLOWER_POS.getZ());
		}).thenWaitUntil(() -> {
			helper.assertBlockPresent(Blocks.STONE, EXPECTED_PLACE_POS);
		}).thenSucceed();
	}

	@GameTest(template = TEMPLATE, batch = "rannuncarpus2")
	public void testDestinationFilterNegative(GameTestHelper helper) {
		helper.startSequence().thenExecute(() -> {
			helper.killAllEntities();
			helper.setBlock(FILTERED_POS, Blocks.POLISHED_ANDESITE);
			helper.spawnItem(Blocks.STONE.asItem(), FLOWER_POS.getX(), FLOWER_POS.getY() + 1, FLOWER_POS.getZ());
		}).thenExecuteFor(DelayHelper.FUNCTIONAL_INHERENT_DELAY + 1, () -> {
			helper.assertBlockNotPresent(Blocks.STONE, EXPECTED_PLACE_POS);
		}).thenSucceed();
	}

	@GameTest(template = TEMPLATE)
	public void testPickupFilterPositive(GameTestHelper helper) {
		helper.startSequence().thenExecute(() -> {
			var frame = getItemFrame(helper);
			frame.setItem(new ItemStack(Blocks.STONE));
			helper.spawnItem(Blocks.STONE.asItem(), FLOWER_POS.getX(), FLOWER_POS.getY() + 1, FLOWER_POS.getZ());
		}).thenWaitUntil(() -> {
			helper.assertBlockPresent(Blocks.STONE, EXPECTED_PLACE_POS);
		}).thenSucceed();
	}

	@GameTest(template = TEMPLATE)
	public void testPickupFilterNegative(GameTestHelper helper) {
		helper.startSequence().thenExecute(() -> {
			var frame = getItemFrame(helper);
			frame.setItem(new ItemStack(Blocks.COBBLESTONE));
			helper.spawnItem(Blocks.STONE.asItem(), FLOWER_POS.getX(), FLOWER_POS.getY() + 1, FLOWER_POS.getZ());
		}).thenExecuteFor(DelayHelper.FUNCTIONAL_INHERENT_DELAY + 1, () -> {
			helper.assertBlockNotPresent(Blocks.STONE, EXPECTED_PLACE_POS);
		}).thenSucceed();
	}

	private static final int MULTI_PLACEMENT_TIMEOUT_TICKS =
			// 10 blocks to place, 1 interval where nothing should be placed, 1 interval leeway
			(10 + 1 + 1) * RannuncarpusBlockEntity.PLACE_INTERVAL_TICKS
					// new items are supplied twice during the test
					+ 2 * DelayHelper.FUNCTIONAL_INHERENT_DELAY;

	@GameTest(template = TEMPLATE_CANDLES, timeoutTicks = MULTI_PLACEMENT_TIMEOUT_TICKS, batch = "rannuncarpus3")
	public void testMultiplePlacements(GameTestHelper helper) {
		final var yellowCandles = helper.spawnItem(Blocks.YELLOW_CANDLE.asItem(), FLOWER_POS.getX(), FLOWER_POS.getY() + 1, FLOWER_POS.getZ());
		helper.startSequence().thenExecute(() -> {
			// phase 1: fill all free spots with yellow candles
			final var stack = yellowCandles.getItem();
			stack.setCount(10); // should have candles left over when done
			yellowCandles.setItem(stack);
		}).thenWaitUntil(() -> {
			// should place 2x 4 candles in the free spaces, even though one of them is occupied by grass
			helper.assertBlockPresent(Blocks.YELLOW_CANDLE, EXPECTED_PLACE_POS1);
			helper.assertBlockPresent(Blocks.YELLOW_CANDLE, EXPECTED_PLACE_POS2);
			helper.assertBlockProperty(EXPECTED_PLACE_POS1, CandleBlock.CANDLES, 4);
			helper.assertBlockProperty(EXPECTED_PLACE_POS2, CandleBlock.CANDLES, 4);
		}).thenExecuteAfter(RannuncarpusBlockEntity.PLACE_INTERVAL_TICKS + 1, () -> {
			// once both spots are filled, no other yellow candles should have been place anywhere else
			helper.assertBlockProperty(FLOATING_YELLOW_CANDLE_POS, CandleBlock.CANDLES, 1);
			if (yellowCandles.getItem().getCount() < 2) {
				helper.fail("Too few yellow candles left over", yellowCandles);
			}
			helper.killAllEntities();
		}).thenExecute(() -> {
			// phase 2: ensure various other properties are kept when adding
			helper.spawnItem(Blocks.MAGENTA_CANDLE.asItem(), FLOWER_POS.getX(), FLOWER_POS.getY() + 1, FLOWER_POS.getZ());
			helper.spawnItem(Blocks.SEA_PICKLE.asItem(), FLOWER_POS.getX(), FLOWER_POS.getY() + 1, FLOWER_POS.getZ());
		}).thenWaitUntil(() -> {
			helper.assertBlockProperty(MAGENTA_CANDLE_POS, CandleBlock.CANDLES, 2);
			helper.assertBlockProperty(SEA_PICKLE_POS, SeaPickleBlock.PICKLES, 2);
		}).thenExecute(() -> {
			helper.assertBlockProperty(MAGENTA_CANDLE_POS, CandleBlock.LIT, true);
			helper.assertBlockProperty(SEA_PICKLE_POS, SeaPickleBlock.WATERLOGGED, true);
		}).thenSucceed();
	}

	@GameTest(template = TEMPLATE, timeoutTicks = DelayHelper.FUNCTIONAL_INHERENT_DELAY + 13 * RannuncarpusBlockEntity.PLACE_INTERVAL_TICKS)
	public void testMultiplePlacementsPrefersEmptySpots(GameTestHelper helper) {
		// have the flower place across the entire platform
		helper.setBlock(FILTER_POS, Blocks.POLISHED_ANDESITE);
		helper.setBlock(FILTERED_POS, Blocks.POLISHED_ANDESITE);

		// prefill half the spaces
		for (BlockPos pos : BlockPos.betweenClosed(0, 2, 0, 4, 2, 4)) {
			if (((pos.getX() + pos.getZ()) % 2) == 1) {
				helper.setBlock(pos, Blocks.SEA_PICKLE);
			}
		}

		// provide exactly enough items to fill the other half
		final var seaPickles = helper.spawnItem(Blocks.SEA_PICKLE.asItem(), FLOWER_POS.getX(), FLOWER_POS.getY() + 1, FLOWER_POS.getZ());
		final var stack = seaPickles.getItem();
		stack.setCount(12);
		seaPickles.setItem(stack);

		helper.startSequence().thenExecuteAfter(DelayHelper.FUNCTIONAL_INHERENT_DELAY + 12 * RannuncarpusBlockEntity.PLACE_INTERVAL_TICKS + 1, () -> {
			// ensure every block has exactly one sea pickle
			for (BlockPos pos : BlockPos.betweenClosed(1, 2, 1, 4, 2, 4)) {
				if (pos.equals(FILTER_POS)) {
					continue;
				}
				helper.assertBlockPresent(Blocks.SEA_PICKLE, pos);
				helper.assertBlockProperty(pos, SeaPickleBlock.PICKLES, 1);
			}
			if (!stack.isEmpty()) {
				helper.fail("Not all items used", seaPickles);
			}
		}).thenSucceed();
	}

	private static ItemFrame getItemFrame(GameTestHelper helper) {
		var bounds = new AABB(helper.absolutePos(FLOWER_POS));
		var list = helper.getLevel().getEntitiesOfClass(ItemFrame.class, bounds);
		TestingUtil.assertThat(list.size() == 1, () -> "Number of item frames wasn't 1");
		return list.get(0);
	}
}
