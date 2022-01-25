/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockModelGenerators.class)
public interface AccessorBlockModelGenerators {
	@Invoker("createSlab")
	static BlockStateGenerator makeSlabState(Block block, ResourceLocation bottomModel, ResourceLocation topModel, ResourceLocation doubleModel) {
		throw new IllegalStateException();
	}

	@Invoker("createStairs")
	static BlockStateGenerator makeStairState(Block block, ResourceLocation innerModel, ResourceLocation straightModel, ResourceLocation outerModel) {
		throw new IllegalStateException();
	}

	@Invoker("createWall")
	static BlockStateGenerator makeWallState(Block block, ResourceLocation postModel, ResourceLocation lowModel, ResourceLocation tallModel) {
		throw new IllegalStateException();
	}

	@Invoker("createFenceGate")
	static BlockStateGenerator makeFenceGateState(Block block, ResourceLocation openModel, ResourceLocation closedModel, ResourceLocation openWallModel, ResourceLocation closedWallModel) {
		throw new IllegalStateException();
	}

	@Invoker("createFence")
	static BlockStateGenerator makeFenceState(Block block, ResourceLocation postModel, ResourceLocation sideModel) {
		throw new IllegalStateException();
	}

	@Invoker("createAxisAlignedPillarBlock")
	static BlockStateGenerator createAxisAlignedPillarBlock(Block block, ResourceLocation model) {
		throw new IllegalStateException();
	}

	@Invoker("createHorizontalFacingDispatch")
	static PropertyDispatch horizontalDispatch() {
		throw new IllegalStateException();
	}

	@Invoker("createFacingDispatch")
	static PropertyDispatch facingDispatch() {
		throw new IllegalStateException();
	}

	@Invoker("createRotatedVariants")
	static Variant[] createRotatedVariants(ResourceLocation model) {
		throw new IllegalStateException();
	}
}
