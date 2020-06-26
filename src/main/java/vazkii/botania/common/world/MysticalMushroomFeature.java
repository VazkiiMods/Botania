/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.world;

import com.mojang.serialization.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import net.minecraft.world.gen.feature.structure.StructureManager;
import vazkii.botania.api.item.IFlowerlessBiome;
import vazkii.botania.api.item.IFlowerlessWorld;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;

import java.util.Random;
import java.util.function.Function;

public class MysticalMushroomFeature extends Feature<MysticalFlowerConfig> {
	public MysticalMushroomFeature(Function<Dynamic<?>, ? extends MysticalFlowerConfig> configFactory) {
		super(configFactory);
	}

	@Override
	public boolean func_230362_a_(ISeedReader world, StructureManager structureManager, ChunkGenerator generator, Random rand, BlockPos pos, MysticalFlowerConfig config) {
		boolean spawn = true;
		if (world.getDimension() instanceof IFlowerlessWorld) {
			spawn = ((IFlowerlessWorld) world.getDimension()).generateFlowers(world);
		} else if (world.getBiome(pos) instanceof IFlowerlessBiome) {
			spawn = ((IFlowerlessBiome) world.getBiome(pos)).canGenerateFlowers(world, pos.getX(), pos.getZ());
		}

		if (!spawn) {
			return false;
		}

		boolean any = false;
		for (int i = 0; i < config.getMushroomPatchSize(); i++) {
			int x = pos.getX() + rand.nextInt(16) + 8;
			int z = pos.getZ() + rand.nextInt(16) + 8;
			int y = rand.nextInt(26) + 4;
			BlockPos pos3 = new BlockPos(x, y, z);
			DyeColor color = DyeColor.byId(rand.nextInt(16));
			BlockState mushroom = ModBlocks.getMushroom(color).getDefaultState();
			if (world.isAirBlock(pos3) && mushroom.isValidPosition(world, pos3)) {
				world.setBlockState(pos3, mushroom, 2);
				any = true;
			}
		}
		return any;
	}
}
