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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.block_entity.PylonBlockEntity;
import vazkii.botania.common.block.flower.BotaniaFlowerBlock;
import vazkii.botania.common.item.material.MysticalPetalItem;

public class ManaPylonBlock extends PylonBlock {

	public ManaPylonBlock(Properties builder) {
		super(builder);
	}

	@Override
	protected void clientTick(Level level, BlockPos worldPosition, BlockState state, PylonBlockEntity self) {

		super.clientTick(level, worldPosition, state, self);

		if (self.activated && self.centerPos != null) {
			if (!level.getBlockState(self.centerPos).is(BotaniaBlocks.enchanter)) {
				self.activated = false;
				return;
			}

			Vec3 centerBlock = new Vec3(self.centerPos.getX() + 0.5, self.centerPos.getY() + 0.75 + (Math.random() - 0.5 * 0.25), self.centerPos.getZ() + 0.5);

			Vec3 ourCoords = Vec3.atCenterOf(worldPosition).add(0, 1 + (Math.random() - 0.5 * 0.25), 0);
			Vec3 movementVector = centerBlock.subtract(ourCoords).normalize().scale(0.2);

			Block block = level.getBlockState(worldPosition.below()).getBlock();
			if (block instanceof BotaniaFlowerBlock flower) {
				int hex = MysticalPetalItem.getPetalLikeColor(flower.color);
				float r = FastColor.ARGB32.red(hex) / 255f;
				float g = FastColor.ARGB32.green(hex) / 255f;
				float b = FastColor.ARGB32.blue(hex) / 255f;

				if (level.random.nextInt(4) == 0) {
					SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), r, g, b, 8);
					level.addParticle(data, centerBlock.x + (Math.random() - 0.5) * 0.5, centerBlock.y, centerBlock.z + (Math.random() - 0.5) * 0.5, 0, 0, 0);
				}

				WispParticleData data1 = WispParticleData.wisp((float) Math.random() / 3F, r, g, b, 1);
				level.addParticle(data1, worldPosition.getX() + 0.5 + (Math.random() - 0.5) * 0.25, worldPosition.getY() - 0.5, worldPosition.getZ() + 0.5 + (Math.random() - 0.5) * 0.25, 0, - -0.04F, 0);
				WispParticleData data = WispParticleData.wisp((float) Math.random() / 5F, r, g, b, 1);
				level.addParticle(data, worldPosition.getX() + 0.5 + (Math.random() - 0.5) * 0.125, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5 + (Math.random() - 0.5) * 0.125, 0, - -0.001F, 0);
				WispParticleData data2 = WispParticleData.wisp((float) Math.random() / 8F, r, g, b);
				level.addParticle(data2, worldPosition.getX() + 0.5 + (Math.random() - 0.5) * 0.25, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5 + (Math.random() - 0.5) * 0.25, (float) movementVector.x, (float) movementVector.y, (float) movementVector.z);
			}
		}
	}

	@Override
	public void addRandomParticle(Level level, BlockPos pos) {
		SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), 0.5f, 0.5f, 1.0f, 2);
		level.addParticle(data, pos.getX() + Math.random(), pos.getY() + Math.random() * 1.3, pos.getZ() + Math.random(), 0, 0, 0);
	}

	@Override
	public float getEnchantPowerBonus(BlockState state, LevelReader world, BlockPos pos) {
		return 8f;
	}
}
