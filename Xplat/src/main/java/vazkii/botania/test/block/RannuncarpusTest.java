package vazkii.botania.test.block;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

import vazkii.botania.common.helper.DelayHelper;
import vazkii.botania.test.TestingUtil;

public class RannuncarpusTest {
	private static final String TEMPLATE = "botania:block/rannuncarpus";

	private static final BlockPos FLOWER_POS = new BlockPos(2, 3, 2);
	private static final BlockPos FILTER_POS = FLOWER_POS.below();

	private static final BlockPos EXPECTED_PLACE_POS = new BlockPos(3, 2, 2);
	private static final BlockPos FILTERED_POS = EXPECTED_PLACE_POS.below();

	@GameTest(template = TEMPLATE)
	public void testDestinationFilterPositive(GameTestHelper helper) {
		helper.startSequence().thenExecute(() -> {
			helper.killAllEntities();
			helper.spawnItem(Blocks.STONE.asItem(), FLOWER_POS.getX(), FLOWER_POS.getY() + 1, FLOWER_POS.getZ());
		}).thenWaitUntil(() -> {
			helper.assertBlockPresent(Blocks.STONE, EXPECTED_PLACE_POS);
		}).thenSucceed();
	}

	@GameTest(template = TEMPLATE)
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

	private static ItemFrame getItemFrame(GameTestHelper helper) {
		var bounds = new AABB(helper.absolutePos(FLOWER_POS));
		var list = helper.getLevel().getEntitiesOfClass(ItemFrame.class, bounds);
		TestingUtil.assertThat(list.size() == 1, () -> "Number of item frames wasn't 1");
		return list.get(0);
	}
}
