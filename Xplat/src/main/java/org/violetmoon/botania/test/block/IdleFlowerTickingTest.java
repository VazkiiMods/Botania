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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;

import vazkii.botania.api.block.WandBindable;
import vazkii.botania.api.block_entity.BindableSpecialFlowerBlockEntity;
import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.common.block.flower.FloatingSpecialFlowerBlock;
import vazkii.botania.common.block.flower.SpecialFlowerBlock;

import java.util.Collection;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * This test checks simply places each defined special flower and lets it sit for a while.
 * Functional flowers start with a full buffer.
 */
public class IdleFlowerTickingTest {
	private static final BlockPos FLOWER_POS = new BlockPos(16, 16, 16);
	private static final String BATCH_NAME = "IdleFlowerTickingTest";
	private static final String TEMPLATE = "botania:block/flower_binding_arena";

	@GameTestGenerator
	public Collection<TestFunction> generateTestForEachSpecialFlowerBlockEntity() {
		return BuiltInRegistries.BLOCK.stream()
				.filter(block -> block instanceof SpecialFlowerBlock || block instanceof FloatingSpecialFlowerBlock)
				.map(block -> {
					String testName = "%s.test_%s_idle".formatted(
							IdleFlowerTickingTest.class.getSimpleName(),
							BuiltInRegistries.BLOCK.getKey(block).getPath().toLowerCase(Locale.ROOT));
					return new TestFunction(
							BATCH_NAME,
							testName,
							TEMPLATE,
							102,
							0L,
							true,
							testSpecialFlowerIdle(block)
					);
				})
				.toList();
	}

	private Consumer<GameTestHelper> testSpecialFlowerIdle(Block block) {
		return helper -> {
			if (block instanceof SpecialFlowerBlock) {
				helper.setBlock(FLOWER_POS.below(), Blocks.GRASS_BLOCK);
			}
			helper.setBlock(FLOWER_POS, block);
			BlockEntity be = helper.getBlockEntity(FLOWER_POS);
			if (!(be instanceof SpecialFlowerBlockEntity specialFlower)) {
				helper.fail("Missing special flower block entity");
				return;
			}
			if (be instanceof BindableSpecialFlowerBlockEntity<?> && WandBindable.LOOKUP.find(be, null) == null) {
				helper.fail("Bindable special flower block entity is not registered as WandBindable");
				return;
			}
			if (specialFlower instanceof FunctionalFlowerBlockEntity functionalFlower) {
				functionalFlower.addMana(functionalFlower.getMaxMana());
			}
			helper.startSequence()
					.thenExecuteAfter(101, helper::killAllEntities)
					.thenSucceed();
		};
	}
}
