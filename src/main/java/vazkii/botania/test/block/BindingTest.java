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
import net.minecraft.gametest.framework.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.test.TestingUtil;

import javax.annotation.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BindingTest {
	private static final String BATCH = LibMisc.MOD_ID + ":" + "binding";

	@GameTest(batch = BATCH, template = "botania:block/flower_binding_arena") //Empty 33x33x33 structure
	public void functionalFlowerAutoBindTest(GameTestHelper helper) {
		autobindTest(helper, ModBlocks.creativePool, ModSubtiles.clayconiaFloating, 10);
	}

	@GameTest(batch = BATCH, template = "botania:block/flower_binding_arena")
	public void generatingFlowerAutoBindTest(GameTestHelper helper) {
		autobindTest(helper, ModBlocks.manaSpreader, ModSubtiles.endoflameFloating, 6);
	}

	private void autobindTest(GameTestHelper helper, Block bindTargetBlock, Block flower, int maxDistance) {
		BlockPos middle = new BlockPos(16, 16, 16);
		helper.setBlock(middle, bindTargetBlock);
		BlockPos absoluteBindTarget = TestingUtil.assertAnyBlockEntity(helper, middle).getBlockPos();

		List<BlockPos> justCloseEnough = placeAxialFlowers(helper, middle, flower, maxDistance);
		List<BlockPos> tooFarAway = placeAxialFlowers(helper, middle, flower, maxDistance + 1);

		helper.runAfterDelay(1L, () -> {
			justCloseEnough.forEach(pos -> assertFlowerBoundTo(helper, pos, absoluteBindTarget));
			tooFarAway.forEach(pos -> assertFlowerBoundTo(helper, pos, null));
			helper.succeed();
		});
	}

	@GameTest(batch = BATCH, template = "botania:block/flower_binding_arena")
	public void functionalFlowerManualBindTest(GameTestHelper helper) {
		manualBindTest(helper, ModBlocks.creativePool, ModSubtiles.clayconiaFloating, 10);
	}

	@GameTest(batch = BATCH, template = "botania:block/flower_binding_arena")
	public void generatingFlowerManualBindTest(GameTestHelper helper) {
		manualBindTest(helper, ModBlocks.manaSpreader, ModSubtiles.endoflameFloating, 6);
	}

	private void manualBindTest(GameTestHelper helper, Block bindTargetBlock, Block flower, int maxDistance) {
		BlockPos middle = new BlockPos(16, 16, 16);

		//Place flowers first so they can't automatically bind
		List<BlockPos> justCloseEnough = placeAxialFlowers(helper, middle, flower, maxDistance);
		List<BlockPos> tooFarAway = placeAxialFlowers(helper, middle, flower, maxDistance + 1);

		helper.setBlock(middle, bindTargetBlock);
		BlockPos absoluteBindTarget = TestingUtil.assertAnyBlockEntity(helper, middle).getBlockPos();

		//Bind each flower with the Wand of the Forest. (before doing any assertions, so you can examine the result in-game)
		justCloseEnough.forEach(pos -> TestingUtil.bindWithWandOfTheForest(helper, pos, middle));
		tooFarAway.forEach(pos -> TestingUtil.bindWithWandOfTheForest(helper, pos, middle));

		//Close-enough ones should bind, far away ones should not.
		justCloseEnough.forEach(pos -> assertFlowerBoundTo(helper, pos, absoluteBindTarget));
		tooFarAway.forEach(pos -> assertFlowerBoundTo(helper, pos, null));

		helper.succeed();
	}

	@GameTest(batch = BATCH, template = "botania:block/flower_binding_arena")
	public void functionalFlowerObedienceStick(GameTestHelper helper) {
		obedienceStickTest(helper, ModBlocks.creativePool, ModSubtiles.clayconiaFloating, 10);
	}

	@GameTest(batch = BATCH, template = "botania:block/flower_binding_arena")
	public void generatingFlowerObedienceStick(GameTestHelper helper) {
		obedienceStickTest(helper, ModBlocks.manaSpreader, ModSubtiles.endoflameFloating, 6);
	}

	private void obedienceStickTest(GameTestHelper helper, Block bindTargetBlock, Block flower, int maxDistance) {
		BlockPos middle = new BlockPos(16, 16, 16);

		//Place flowers first so they can't automatically bind
		List<BlockPos> justCloseEnough = placeAxialFlowers(helper, middle, flower, maxDistance);
		List<BlockPos> tooFarAway = placeAxialFlowers(helper, middle, flower, maxDistance + 1);

		helper.setBlock(middle, bindTargetBlock);
		BlockPos absoluteBindTarget = TestingUtil.assertAnyBlockEntity(helper, middle).getBlockPos();

		//Use a Floral Obedience Stick on the pool
		Player player = helper.makeMockPlayer();
		player.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.obedienceStick));
		TestingUtil.useItemOn(helper, player, InteractionHand.MAIN_HAND, middle);

		//All close-enough flowers should bind, all far-away ones should not
		justCloseEnough.forEach(pos -> assertFlowerBoundTo(helper, pos, absoluteBindTarget));
		tooFarAway.forEach(pos -> assertFlowerBoundTo(helper, pos, null));

		helper.succeed();
	}

	private static List<BlockPos> placeAxialFlowers(GameTestHelper helper, BlockPos center, Block flower, int distance) {
		return Arrays.stream(Direction.values())
				.map(dir -> center.relative(dir, distance))
				.peek(pos -> helper.setBlock(pos, flower))
				.collect(Collectors.toList());
	}

	private static void assertFlowerBoundTo(GameTestHelper helper, BlockPos relativePos, @Nullable BlockPos absoluteBindTarget) {
		String message = absoluteBindTarget == null ? "Flower should not have bound" : "Flower should have bound to " + absoluteBindTarget;

		BlockEntity be = TestingUtil.assertAnyBlockEntity(helper, relativePos);
		if (be instanceof TileEntityGeneratingFlower tege) {
			TestingUtil.assertEqualsAt(helper, relativePos, tege.getBinding(), absoluteBindTarget, () -> message);
		} else if (be instanceof TileEntityFunctionalFlower tefe) {
			TestingUtil.assertEqualsAt(helper, relativePos, tefe.getBinding(), absoluteBindTarget, () -> message);
		} else {
			TestingUtil.throwPositionedAssertion(helper, relativePos, () -> "Expected a flower here");
		}
	}
}
