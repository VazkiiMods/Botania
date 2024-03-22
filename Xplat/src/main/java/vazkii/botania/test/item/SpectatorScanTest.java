package vazkii.botania.test.item;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.equipment.bauble.SpectatorItem;
import vazkii.botania.mixin.RandomizableContainerBlockEntityAccessor;
import vazkii.botania.test.TestingUtil;

import java.util.Arrays;

public class SpectatorScanTest {
	private static final BlockPos POSITION_CHEST_NORMAL = new BlockPos(17, 16, 16);
	private static final BlockPos POSITION_CHEST_LOOT = new BlockPos(15, 16, 16);
	private static final BlockPos POSITION_CART_NORMAL = new BlockPos(11, 2, 6);
	private static final BlockPos POSITION_CART_LOOT = new BlockPos(9, 2, 6);
	private static final BlockPos POSITION_ITEM = new BlockPos(7, 3, 3);
	private static final BlockPos POSITION_VILLAGER = new BlockPos(4, 2, 3);

	private static final String LOOT_TABLE_CHEST = "minecraft:chests/simple_dungeon";
	private static final String LOOT_TABLE_CART = "minecraft:chests/abandoned_mineshaft";

	@GameTest(template = "botania:item/spectator_scan")
	public void testSpectatorScanBehavior(GameTestHelper helper) {
		// set up test entities
		var itemEntity = helper.spawnItem(Items.IRON_INGOT, POSITION_ITEM);
		var villager = helper.spawn(EntityType.VILLAGER, POSITION_VILLAGER);
		villager.setVillagerData(new VillagerData(VillagerType.PLAINS, VillagerProfession.TOOLSMITH, 2));

		// set inventory content
		var regularChestCart = helper.spawn(EntityType.CHEST_MINECART, POSITION_CART_NORMAL);
		regularChestCart.setItem(2, new ItemStack(Items.COAL));
		regularChestCart.setItem(5, new ItemStack(Items.IRON_INGOT));

		var regularChest = TestingUtil.assertBlockEntity(helper, POSITION_CHEST_NORMAL, BlockEntityType.CHEST);
		regularChest.setItem(3, new ItemStack(Items.FLINT));
		regularChest.setItem(7, new ItemStack(Items.IRON_INGOT));

		// set loot tables
		var lootChestCart = helper.spawn(EntityType.CHEST_MINECART, POSITION_CART_LOOT);
		lootChestCart.setLootTable(new ResourceLocation(LOOT_TABLE_CART), 1L);

		var lootChest = TestingUtil.assertBlockEntity(helper, POSITION_CHEST_LOOT, BlockEntityType.CHEST);
		lootChest.setLootTable(new ResourceLocation(LOOT_TABLE_CHEST), 1L);

		// set up player
		var player = helper.makeMockSurvivalPlayer();
		player.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_INGOT));
		player.moveTo(helper.absoluteVec(new Vec3(24, 24, 24)));
		var spectatorStack = new ItemStack(BotaniaItems.itemFinder);
		((SpectatorItem) BotaniaItems.itemFinder).onWornTick(spectatorStack, player);
		BotaniaAPI.LOGGER.info("Player position: {}, chest position: {}", player.blockPosition(), helper.absolutePos(POSITION_CHEST_NORMAL));

		// ensure loot content has not been rolled
		TestingUtil.assertThat(lootChestCart.getLootTable() != null, () -> "Chest loot was rolled");
		TestingUtil.assertThat(((RandomizableContainerBlockEntityAccessor) lootChest).getLootTable() != null,
				() -> "Chest loot was rolled");

		// check that exactly the relevant positions have been found
		ListTag blocks = ItemNBTHelper.getList(spectatorStack, SpectatorItem.TAG_BLOCK_POSITIONS, Tag.TAG_LONG, false);
		TestingUtil.assertEquals(blocks.size(), 1, () -> "Expected 1 block hit, was " + blocks.size());
		BlockPos chestPos = BlockPos.of(((LongTag) blocks.get(0)).getAsLong());
		TestingUtil.assertEquals(helper.absolutePos(POSITION_CHEST_NORMAL), chestPos,
				() -> "Chest position " + helper.absolutePos(POSITION_CHEST_NORMAL) + " not in result, but found " + chestPos);

		int[] entities = ItemNBTHelper.getIntArray(spectatorStack, SpectatorItem.TAG_ENTITY_POSITIONS);
		TestingUtil.assertEquals(entities.length, 3, () -> "Expected 3 entity hits, but got " + entities.length);
		TestingUtil.assertThat(Arrays.stream(entities).anyMatch(id -> villager.getId() == id), () -> "Villager not in result");
		TestingUtil.assertThat(Arrays.stream(entities).anyMatch(id -> itemEntity.getId() == id), () -> "Item entity not in result");
		TestingUtil.assertThat(Arrays.stream(entities).anyMatch(id -> regularChestCart.getId() == id), () -> "Minecart not in result");

		helper.succeed();
	}
}
