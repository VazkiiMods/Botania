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
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertPosException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

import vazkii.botania.test.TestingUtil;

public class HopperhockTest {

	private static final String TEMPLATE = "botania:block/hopperhock_furnace";
	private static final BlockPos FLOWER_POS = new BlockPos(2, 3, 2);
	// Above the light gray glass which is at 2, 2, 1
	private static final BlockPos SPAWN_ITEM_POS = new BlockPos(2, 3, 1);
	private static final float SPAWN_ITEM_X = SPAWN_ITEM_POS.getX();
	private static final float SPAWN_ITEM_Y = SPAWN_ITEM_POS.getY();
	private static final float SPAWN_ITEM_Z = SPAWN_ITEM_POS.getZ();

	@GameTest(template = TEMPLATE)
	public void testInsertTop(GameTestHelper helper) {
		var furnacePos = FLOWER_POS.below();
		helper.setBlock(furnacePos, Blocks.FURNACE);
		spawnItem(helper, Items.COBBLESTONE);

		helper.succeedWhen(() -> {
			assertItemInSlot(helper, furnacePos, 0, Items.COBBLESTONE, 1);
			helper.assertEntityNotPresent(EntityType.ITEM);
		});
	}

	@GameTest(template = TEMPLATE)
	public void testInsertSide(GameTestHelper helper) {
		var furnacePos = FLOWER_POS.south();
		helper.setBlock(furnacePos, Blocks.FURNACE);
		spawnItem(helper, Items.CHARCOAL);

		helper.succeedWhen(() -> {
			assertItemInSlot(helper, furnacePos, 1, Items.CHARCOAL, 1);
			helper.assertEntityNotPresent(EntityType.ITEM);
		});
	}

	@GameTest(template = TEMPLATE)
	public void testInsertBottom(GameTestHelper helper) {
		var furnacePos = FLOWER_POS.above();
		helper.setBlock(furnacePos, Blocks.FURNACE);
		spawnItem(helper, Items.CHARCOAL);

		helper.succeedWhen(() -> {
			// Both the fuel and output slots are accessible from the bottom face, but inserting into the
			// output slot isn't possible.
			assertItemInSlot(helper, furnacePos, 1, Items.CHARCOAL, 1);
			helper.assertEntityNotPresent(EntityType.ITEM);
		});
	}

	@GameTest(template = TEMPLATE)
	public void testInventoryFull(GameTestHelper helper) {
		var furnacePos = FLOWER_POS.below();
		helper.setBlock(furnacePos, Blocks.FURNACE);
		getFurnaceBlockEntity(helper, furnacePos).setItem(0, new ItemStack(Items.OAK_LOG, 64));
		spawnItem(helper, Items.OAK_LOG);

		helper.startSequence().thenExecuteAfter(61, () -> {
			assertItemInSlot(helper, furnacePos, 0, Items.OAK_LOG, 64);
			helper.assertItemEntityPresent(Items.OAK_LOG, SPAWN_ITEM_POS, 0);
		}).thenSucceed();
	}

	@GameTest(template = TEMPLATE)
	public void testAddToStack(GameTestHelper helper) {
		var furnacePos = FLOWER_POS.below();
		helper.setBlock(furnacePos, Blocks.FURNACE);
		getFurnaceBlockEntity(helper, furnacePos).setItem(0, new ItemStack(Items.OAK_LOG, 60));
		ItemEntity itemEntity = spawnItem(helper, Items.OAK_LOG);
		itemEntity.getItem().setCount(10);

		helper.succeedWhen(() -> {
			assertItemInSlot(helper, furnacePos, 0, Items.OAK_LOG, 64);
			helper.assertItemEntityCountIs(Items.OAK_LOG, SPAWN_ITEM_POS, 0, 6);
		});
	}

	@GameTest(template = TEMPLATE)
	public void testWrongItemForSlot(GameTestHelper helper) {
		var furnacePos = FLOWER_POS.above();
		helper.setBlock(furnacePos, Blocks.FURNACE);
		spawnItem(helper, Items.BRICKS);

		helper.startSequence().thenExecuteAfter(61, () -> {
			helper.assertContainerEmpty(furnacePos);
			helper.assertItemEntityPresent(Items.BRICKS, SPAWN_ITEM_POS, 0);
		}).thenSucceed();
	}

	@GameTest(template = TEMPLATE)
	public void testFilterMatch(GameTestHelper helper) {
		var furnacePos = FLOWER_POS.below();
		helper.setBlock(furnacePos, Blocks.FURNACE);
		spawnItemFrame(helper, furnacePos.west(), Direction.WEST, Items.COBBLESTONE);
		spawnItem(helper, Items.COBBLESTONE);

		helper.succeedWhen(() -> {
			assertItemInSlot(helper, furnacePos, 0, Items.COBBLESTONE, 1);
			helper.assertEntityNotPresent(EntityType.ITEM);
		});
	}

	@GameTest(template = TEMPLATE)
	public void testFilterNoMatch(GameTestHelper helper) {
		var furnacePos = FLOWER_POS.below();
		helper.setBlock(furnacePos, Blocks.FURNACE);
		spawnItemFrame(helper, furnacePos.west(), Direction.WEST, Items.OAK_LOG);
		spawnItem(helper, Items.COBBLESTONE);

		helper.startSequence().thenExecuteAfter(61, () -> {
			helper.assertContainerEmpty(furnacePos);
			helper.assertItemEntityPresent(Items.COBBLESTONE, SPAWN_ITEM_POS, 0);
		}).thenSucceed();
	}

	private static void spawnItemFrame(GameTestHelper helper, BlockPos pos, Direction direction, Item item) {
		ItemFrame itemFrame = new ItemFrame(helper.getLevel(), helper.absolutePos(pos), direction);
		itemFrame.setItem(new ItemStack(item));
		helper.getLevel().addFreshEntity(itemFrame);
	}

	private ItemEntity spawnItem(GameTestHelper helper, Item item) {
		return helper.spawnItem(item, SPAWN_ITEM_X, SPAWN_ITEM_Y, SPAWN_ITEM_Z);
	}

	private static void assertItemInSlot(GameTestHelper helper, BlockPos furnacePos, int slot, Item expectedItem, int expectedCount) {
		ItemStack stack = getFurnaceBlockEntity(helper, furnacePos).getItem(slot);
		if (stack.getItem() != expectedItem) {
			TestingUtil.throwPositionedAssertion(
					helper,
					furnacePos,
					() -> "Expected " + expectedItem + " in slot " + slot + ", but got " + stack.getItem());
		} else if (stack.getCount() != expectedCount) {
			TestingUtil.throwPositionedAssertion(
					helper,
					furnacePos,
					() -> "Expected " + expectedCount + " items in slot " + slot + ", but got " + stack.getCount());
		}
	}

	private static AbstractFurnaceBlockEntity getFurnaceBlockEntity(GameTestHelper helper, BlockPos furnacePos) {
		if (helper.getBlockEntity(furnacePos) instanceof AbstractFurnaceBlockEntity furnace) {
			return furnace;
		} else {
			throw new GameTestAssertPosException(
					"Expected furnace here", helper.absolutePos(furnacePos), furnacePos, helper.getTick());
		}
	}
}
