/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.internal.Colored;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.block_entity.PylonBlockEntity;
import vazkii.botania.common.item.material.MysticalPetalItem;

public class ManaPylonBlock extends PylonBlock {

	public ManaPylonBlock(Properties builder) {
		super(builder);
	}

	@Override
	protected void clientTick(Level level, BlockPos pos, BlockState state, PylonBlockEntity self) {

		super.clientTick(level, pos, state, self);

		if (self.activated && self.centerPos != null) {
			if (!level.getBlockState(self.centerPos).is(BotaniaBlocks.enchanter)) {
				self.activated = false;
				return;
			}

			BlockPos flowerPos = pos.below();
			BlockState flowerState = level.getBlockState(flowerPos);
			if (flowerState.getBlock() instanceof Colored flower) {
				int hex = MysticalPetalItem.getPetalLikeColor(flower.getColor());
				float r = FastColor.ARGB32.red(hex) / 255f;
				float g = FastColor.ARGB32.green(hex) / 255f;
				float b = FastColor.ARGB32.blue(hex) / 255f;

				RandomSource rng = level.random;
				Vec3 centerBlock = new Vec3(
						self.centerPos.getX() + 0.5,
						self.centerPos.getY() + 0.75 + (rng.nextDouble() - 0.5) * 0.25,
						self.centerPos.getZ() + 0.5);

				if (rng.nextInt(4) == 0) {
					level.addParticle(SparkleParticleData.sparkle(rng.nextFloat(), r, g, b, 8),
							centerBlock.x + (rng.nextDouble() - 0.5) * 0.25,
							centerBlock.y + 0.5,
							centerBlock.z + (rng.nextDouble() - 0.5) * 0.25,
							0, 0, 0);
				}

				Vec3 flowerOffset = flowerState.getOffset(level, flowerPos);
				Vec3 ourCoords = Vec3.atCenterOf(pos).add(0, 1 + (rng.nextDouble() - 0.5) * 0.25, 0);
				Vec3 movementVector = centerBlock.subtract(ourCoords).scale(0.04);

				level.addParticle(WispParticleData.wisp(rng.nextFloat() / 3, r, g, b),
						pos.getX() + 0.5 + flowerOffset.x + (rng.nextDouble() - 0.5) * 0.5,
						pos.getY() - 0.5,
						pos.getZ() + 0.5 + flowerOffset.z + (rng.nextDouble() - 0.5) * 0.5,
						0, 0.04, 0);
				level.addParticle(WispParticleData.wisp(rng.nextFloat() / 5, r, g, b),
						pos.getX() + 0.5 + (rng.nextDouble() - 0.5) * 0.125,
						pos.getY() + 1.5,
						pos.getZ() + 0.5 + (rng.nextDouble() - 0.5) * 0.125,
						0, 0.001, 0);
				level.addParticle(WispParticleData.wispNoClip(rng.nextFloat() / 8, r, g, b),
						pos.getX() + 0.5 + (rng.nextDouble() - 0.5) * 0.25,
						pos.getY() + 1.5,
						pos.getZ() + 0.5 + (rng.nextDouble() - 0.5) * 0.25,
						movementVector.x, movementVector.y, movementVector.z);
			}
		}
	}

	@Override
	public void addRandomParticle(Level level, BlockPos pos) {
		RandomSource rng = level.random;
		level.addParticle(SparkleParticleData.sparkle(rng.nextFloat(), 0.5f, 0.5f, 1.0f, 2),
				pos.getX() + rng.nextDouble(),
				pos.getY() + rng.nextDouble() * 1.3,
				pos.getZ() + rng.nextDouble(),
				0, 0, 0);
	}

	@Override
	public float getEnchantPowerBonus(BlockState state, LevelReader world, BlockPos pos) {
		return 8f;
	}
}
