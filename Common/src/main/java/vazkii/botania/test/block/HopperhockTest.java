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
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.test.TestingUtil;

public class HopperhockTest {

	private static final String TEMPLATE = "botania:block/hopperhock_furnace";
	private static final BlockPos FURNACE_POS = new BlockPos(6, 3, 6);

	@GameTest(template = TEMPLATE)
	public void testInsertTop(GameTestHelper helper) {
		helper.setBlock(FURNACE_POS.above(), ModSubtiles.hopperhockFloating);
		helper.spawnItem(Items.COBBLESTONE, 1, 2, 1);

		helper.succeedWhen(() -> {
			assertItemInSlot(helper, 0, Items.COBBLESTONE, 1);
			helper.assertEntityNotPresent(EntityType.ITEM);
		});
	}

	@GameTest(template = TEMPLATE)
	public void testInsertSide(GameTestHelper helper) {
		helper.setBlock(FURNACE_POS.east(), ModSubtiles.hopperhockFloating);
		helper.spawnItem(Items.CHARCOAL, 1, 2, 1);

		helper.succeedWhen(() -> {
			assertItemInSlot(helper, 1, Items.CHARCOAL, 1);
			helper.assertEntityNotPresent(EntityType.ITEM);
		});
	}

	@GameTest(template = TEMPLATE)
	public void testInsertBottom(GameTestHelper helper) {
		helper.setBlock(FURNACE_POS.below(), ModSubtiles.hopperhock);
		helper.spawnItem(Items.CHARCOAL, 1, 2, 1);

		helper.succeedWhen(() -> {
			// Both the fuel and output slots are accessible from the bottom face, but inserting into the
			// output slot isn't possible.
			assertItemInSlot(helper, 1, Items.CHARCOAL, 1);
			helper.assertEntityNotPresent(EntityType.ITEM);
		});
	}

	@GameTest(template = TEMPLATE)
	public void testInventoryFull(GameTestHelper helper) {
		helper.setBlock(FURNACE_POS.above(), ModSubtiles.hopperhockFloating);
		getFurnaceBlockEntity(helper).setItem(0, new ItemStack(Items.OAK_LOG, 64));
		helper.spawnItem(Items.OAK_LOG, 1, 2, 1);

		helper.startSequence().thenExecuteAfter(61, () -> {
			assertItemInSlot(helper, 0, Items.OAK_LOG, 64);
			helper.assertItemEntityPresent(Items.OAK_LOG, new BlockPos(1, 2, 1), 0);
		}).thenSucceed();
	}

	@GameTest(template = TEMPLATE)
	public void testAddToStack(GameTestHelper helper) {
		helper.setBlock(FURNACE_POS.above(), ModSubtiles.hopperhockFloating);
		getFurnaceBlockEntity(helper).setItem(0, new ItemStack(Items.OAK_LOG, 60));
		ItemEntity itemEntity = helper.spawnItem(Items.OAK_LOG, 1, 2, 1);
		itemEntity.getItem().setCount(10);

		helper.succeedWhen(() -> {
			assertItemInSlot(helper, 0, Items.OAK_LOG, 64);
			helper.assertItemEntityCountIs(Items.OAK_LOG, new BlockPos(1, 2, 1), 0, 6);
		});
	}

	@GameTest(template = TEMPLATE)
	public void testWrongItemForSlot(GameTestHelper helper) {
		helper.setBlock(FURNACE_POS.below(), ModSubtiles.hopperhock);
		helper.spawnItem(Items.BRICKS, 1, 2, 1);

		helper.startSequence().thenExecuteAfter(61, () -> {
			helper.assertContainerEmpty(FURNACE_POS);
			helper.assertItemEntityPresent(Items.BRICKS, new BlockPos(1, 2, 1), 0);
		}).thenSucceed();
	}

	@GameTest(template = TEMPLATE)
	public void testFilterMatch(GameTestHelper helper) {
		helper.setBlock(FURNACE_POS.above(), ModSubtiles.hopperhockFloating);
		spawnItemFrame(helper, FURNACE_POS.west(), Direction.WEST, Items.COBBLESTONE);
		helper.spawnItem(Items.COBBLESTONE, 1, 2, 1);

		helper.succeedWhen(() -> {
			assertItemInSlot(helper, 0, Items.COBBLESTONE, 1);
			helper.assertEntityNotPresent(EntityType.ITEM);
		});
	}

	@GameTest(template = TEMPLATE)
	public void testFilterNoMatch(GameTestHelper helper) {
		helper.setBlock(FURNACE_POS.above(), ModSubtiles.hopperhockFloating);
		spawnItemFrame(helper, FURNACE_POS.west(), Direction.WEST, Items.OAK_LOG);
		helper.spawnItem(Items.COBBLESTONE, 1, 2, 1);

		helper.startSequence().thenExecuteAfter(61, () -> {
			helper.assertContainerEmpty(FURNACE_POS);
			helper.assertItemEntityPresent(Items.COBBLESTONE, new BlockPos(1, 2, 1), 0);
		}).thenSucceed();
	}

	private static void spawnItemFrame(GameTestHelper helper, BlockPos pos, Direction direction, Item item) {
		ItemFrame itemFrame = new ItemFrame(helper.getLevel(), helper.absolutePos(pos), direction);
		itemFrame.setItem(new ItemStack(item));
		helper.getLevel().addFreshEntity(itemFrame);
	}

	private static void assertItemInSlot(GameTestHelper helper, int slot, Item expectedItem, int expectedCount) {
		ItemStack stack = getFurnaceBlockEntity(helper).getItem(slot);
		if (stack.getItem() != expectedItem) {
			TestingUtil.throwPositionedAssertion(
					helper,
					FURNACE_POS,
					() -> "Expected " + expectedItem + " in slot " + slot + ", but got " + stack.getItem());
		} else if (stack.getCount() != expectedCount) {
			TestingUtil.throwPositionedAssertion(
					helper,
					FURNACE_POS,
					() -> "Expected " + expectedCount + " items in slot " + slot + ", but got " + stack.getCount());
		}
	}

	private static AbstractFurnaceBlockEntity getFurnaceBlockEntity(GameTestHelper helper) {
		if (helper.getBlockEntity(FURNACE_POS) instanceof AbstractFurnaceBlockEntity furnace) {
			return furnace;
		} else {
			throw new GameTestAssertPosException(
					"Expected furnace here", helper.absolutePos(FURNACE_POS), FURNACE_POS, helper.getTick());
		}
	}
}
