package vazkii.botania.test.item;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ItemFlowerBag;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.test.TestingUtil;

public class FlowerBagTest {
	@GameTest(template = TestingUtil.EMPTY_STRUCTURE)
	public void testNoShinyFlowers(GameTestHelper helper) {
		var player = helper.makeMockPlayer();
		var bag = new ItemStack(ModItems.flowerBag);
		player.getInventory().setItem(1, bag);

		var flower = new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), new ItemStack(ModBlocks.blackShinyFlower, 64));
		TestingUtil.assertThat(!ItemFlowerBag.onPickupItem(flower, player), () -> "Should not pick up glimmering flowers");

		TestingUtil.assertEquals(flower.getItem().getItem(), ModBlocks.blackShinyFlower.asItem());
		TestingUtil.assertEquals(flower.getItem().getCount(), 64);

		var inv = ItemFlowerBag.getInventory(bag);
		for (int i = 0; i < inv.getContainerSize(); i++) {
			TestingUtil.assertThat(inv.getItem(i).isEmpty(), () -> "Bag should be empty");
		}
		helper.succeed();
	}

	@GameTest(template = TestingUtil.EMPTY_STRUCTURE)
	public void testPickupBasic(GameTestHelper helper) {
		var player = helper.makeMockPlayer();
		var bag = new ItemStack(ModItems.flowerBag);
		player.getInventory().setItem(1, bag);

		var flower = new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), new ItemStack(ModBlocks.blackFlower, 64));
		TestingUtil.assertThat(ItemFlowerBag.onPickupItem(flower, player), () -> "Pickup should succeed since the bag has room");
		TestingUtil.assertThat(flower.getItem().isEmpty(), () -> "Should have consumed everything");

		var flowerInBag = ItemFlowerBag.getInventory(bag).getItem(DyeColor.BLACK.getId());
		TestingUtil.assertThat(!flowerInBag.isEmpty(), () -> "Bag should have an item in black slot");
		TestingUtil.assertEquals(flowerInBag.getItem(), ModBlocks.blackFlower.asItem());
		TestingUtil.assertEquals(flowerInBag.getCount(), 64);
		helper.succeed();
	}
}
