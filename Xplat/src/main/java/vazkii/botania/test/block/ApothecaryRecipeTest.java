package vazkii.botania.test.block;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.block.PetalApothecary;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.block.PetalApothecaryBlock;
import vazkii.botania.common.item.BotaniaItems;

public class ApothecaryRecipeTest {

	private static final String TEMPLATE = "botania:block/apothecary_recipe";

	private static final BlockPos APOTHECARY = new BlockPos(1, 2, 1);

	private void fillApothecary(GameTestHelper helper) {
		BlockState apothecary = helper.getBlockState(APOTHECARY);
		helper.setBlock(APOTHECARY, apothecary.setValue(PetalApothecaryBlock.FLUID, PetalApothecary.State.WATER));
	}

	private void spawnItem(GameTestHelper helper, Item item) {
		BlockPos pos = APOTHECARY.above();
		helper.spawnItem(item, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F);
	}

	@GameTest(template = TEMPLATE)
	public void testItemEnterPrevention(GameTestHelper helper) {
		helper.startSequence().thenExecute(() -> {
			spawnItem(helper, BotaniaItems.whitePetal);
			spawnItem(helper, BotaniaItems.whitePetal);
			spawnItem(helper, BotaniaItems.whitePetal);
			spawnItem(helper, BotaniaItems.whitePetal);
		}).thenExecuteAfter(1, () -> {
			spawnItem(helper, Items.MELON_SEEDS);
		}).thenExecuteAfter(10, () -> {
			helper.assertItemEntityPresent(BotaniaFlowerBlocks.pureDaisy.asItem(), APOTHECARY.above(), 1.5);
			fillApothecary(helper);
			// Remove the pure daisy craft result
			helper.killAllEntities();

			// Drop new seeds which should not enter the apothecary because of the stored recipe
			spawnItem(helper, Items.MELON_SEEDS);
			spawnItem(helper, Items.PUMPKIN_SEEDS);
		}).thenExecuteAfter(10, () -> {
			helper.assertItemEntityPresent(Items.PUMPKIN_SEEDS, APOTHECARY.above(), 1.5);
			helper.assertItemEntityPresent(Items.MELON_SEEDS, APOTHECARY.above(), 1.5);

			// Drop an item which will enter the apothecary, which will clear the recipe and allow the seeds to enter
			spawnItem(helper, Items.DIAMOND);
		}).thenExecuteAfter(10, () -> {
			// Kill all items (which should be none) and destroy apothecary to have only its contents as dropped items
			helper.killAllEntities();
			helper.destroyBlock(APOTHECARY);

			helper.assertItemEntityPresent(Items.PUMPKIN_SEEDS, APOTHECARY, 1.5);
			helper.assertItemEntityPresent(Items.MELON_SEEDS, APOTHECARY, 1.5);
			helper.assertItemEntityPresent(Items.DIAMOND, APOTHECARY, 1.5);

			helper.killAllEntities();
		}).thenSucceed();
	}

}
