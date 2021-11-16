/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.test.item;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileCacophonium;
import vazkii.botania.common.item.ItemCacophonium;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.test.TestingUtil;

// https://github.com/williewillus/botania-fabric-issues/issues/92
public class CacophoniumTest {
	@GameTest(template = "botania:block/cacophonium")
	public void testCacophonium(GameTestHelper helper) {
		BlockPos cowPos = new BlockPos(1, 2, 1);
		BlockPos noteBlockPos = new BlockPos(1, 2, 3);
		helper.assertBlockPresent(Blocks.NOTE_BLOCK, noteBlockPos);

		Player player = helper.makeMockPlayer();
		Cow cow = helper.spawnWithNoFreeWill(EntityType.COW, cowPos);

		//Give a cacophonium to the player.
		player.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.cacophonium));

		//Use the cacophonium on the cow.
		player.interactOn(cow, InteractionHand.MAIN_HAND);
		SoundEvent sound = ItemCacophonium.getSound(player.getMainHandItem());
		TestingUtil.assertEquals(sound, SoundEvents.COW_AMBIENT);

		//Use the cacophonium on the note block.
		TestingUtil.useItemOn(helper, player, InteractionHand.MAIN_HAND, noteBlockPos);
		helper.assertBlockPresent(ModBlocks.cacophonium, noteBlockPos);

		BlockEntity be = helper.getBlockEntity(noteBlockPos);
		TestingUtil.assertThat(be instanceof TileCacophonium, "No cacophonium block entity?");
		TestingUtil.assertEquals(ItemCacophonium.getSound(((TileCacophonium) be).stack), SoundEvents.COW_AMBIENT);

		//Moo.
		helper.setBlock(noteBlockPos.south(), Blocks.REDSTONE_BLOCK);
		helper.assertBlockProperty(noteBlockPos, BlockStateProperties.POWERED, true);

		helper.succeed();
	}
}
