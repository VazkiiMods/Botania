/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.test.item;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.ColoredContentsPouchItem;
import vazkii.botania.test.TestingUtil;

public class FlowerPouchTest {
	@GameTest(template = TestingUtil.EMPTY_STRUCTURE)
	public void testNoShinyFlowers(GameTestHelper helper) {
		var player = helper.makeMockPlayer(GameType.CREATIVE);
		var bag = new ItemStack(BotaniaItems.FLOWER_POUCH);
		player.getInventory().setItem(1, bag);

		var flower = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), new ItemStack(BotaniaBlocks.BLACK_GLIMMERING_FLOWER, 64));
		TestingUtil.assertThat(!ColoredContentsPouchItem.onPickupItem(flower, player), () -> "Should not pick up glimmering flowers");

		TestingUtil.assertEquals(flower.getItem().getItem(), BotaniaBlocks.BLACK_GLIMMERING_FLOWER.asItem());
		TestingUtil.assertEquals(flower.getItem().getCount(), 64);

		var inv = BotaniaItems.FLOWER_POUCH.getInventory(bag);
		for (int i = 0; i < inv.getContainerSize(); i++) {
			TestingUtil.assertThat(inv.getItem(i).isEmpty(), () -> "Bag should be empty");
		}
		helper.succeed();
	}

	@GameTest(template = TestingUtil.EMPTY_STRUCTURE)
	public void testPickupBasic(GameTestHelper helper) {
		var player = helper.makeMockPlayer(GameType.CREATIVE);
		var bag = new ItemStack(BotaniaItems.FLOWER_POUCH);
		player.getInventory().setItem(1, bag);

		var flower = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), new ItemStack(BotaniaBlocks.BLACK_MYSTICAL_FLOWER, 60));
		TestingUtil.assertThat(ColoredContentsPouchItem.onPickupItem(flower, player), () -> "Pickup should succeed since the bag has room");
		TestingUtil.assertThat(flower.getItem().isEmpty(), () -> "Should have consumed everything");

		var slot = ColorHelper.supportedColors().toList().indexOf(DyeColor.BLACK);
		var flowerInBag = BotaniaItems.FLOWER_POUCH.getInventory(bag).getItem(slot);
		TestingUtil.assertThat(!flowerInBag.isEmpty(), () -> "Bag should have an item in black slot");
		TestingUtil.assertEquals(flowerInBag.getItem(), BotaniaBlocks.BLACK_MYSTICAL_FLOWER.asItem());
		TestingUtil.assertEquals(flowerInBag.getCount(), 60);

		flower.getItem().setCount(5);
		TestingUtil.assertThat(!ColoredContentsPouchItem.onPickupItem(flower, player), () -> "Pickup should have remainder since slot is full");
		TestingUtil.assertEquals(flower.getItem().getCount(), 1, () -> "Entity should have one left that didn't fit");
		var updatedFlowerInBag = BotaniaItems.FLOWER_POUCH.getInventory(bag).getItem(slot);
		TestingUtil.assertEquals(updatedFlowerInBag.getCount(), 64, () -> "Stack should be full now");

		helper.succeed();
	}
}
