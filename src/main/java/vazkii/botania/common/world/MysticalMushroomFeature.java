/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.world;

import net.minecraft.block.BlockState;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import vazkii.botania.common.block.ModBlocks;

import java.util.Random;

public class MysticalMushroomFeature extends Feature<MysticalFlowerConfig> {
	public MysticalMushroomFeature() {
		super(MysticalFlowerConfig.CODEC);
	}

	@Override
	public boolean generate(ServerWorldAccess world, StructureAccessor structureManager, ChunkGenerator generator, Random rand, BlockPos pos, MysticalFlowerConfig config) {
		boolean any = false;
		for (int i = 0; i < config.getMushroomPatchSize(); i++) {
			int x = pos.getX() + rand.nextInt(16) + 8;
			int z = pos.getZ() + rand.nextInt(16) + 8;
			int y = rand.nextInt(26) + 4;
			BlockPos pos3 = new BlockPos(x, y, z);
			DyeColor color = DyeColor.byId(rand.nextInt(16));
			BlockState mushroom = ModBlocks.getMushroom(color).getDefaultState();
			if (world.isAir(pos3) && mushroom.canPlaceAt(world, pos3)) {
				world.setBlockState(pos3, mushroom, 2);
				any = true;
			}
		}
		return any;
	}
}
