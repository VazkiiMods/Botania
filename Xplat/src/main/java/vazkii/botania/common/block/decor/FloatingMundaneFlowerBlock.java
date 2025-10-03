/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.decor;

import net.minecraft.core.BlockPos;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.internal.Colored;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.FloatingFlowerBaseBlock;
import vazkii.botania.common.item.material.MysticalPetalItem;
import vazkii.botania.xplat.BotaniaConfig;

public class FloatingMundaneFlowerBlock extends FloatingFlowerBaseBlock implements Colored {

	public final DyeColor color;

	public FloatingMundaneFlowerBlock(DyeColor color, Properties props) {
		super(props);
		this.color = color;
	}

	@Override
	public DyeColor getColor() {
		return color;
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
		int color = MysticalPetalItem.getPetalLikeColor(this.color);
		float r = FastColor.ARGB32.red(color) / 255f;
		float g = FastColor.ARGB32.green(color) / 255f;
		float b = FastColor.ARGB32.blue(color) / 255f;

		if (rand.nextDouble() < BotaniaConfig.client().flowerParticleFrequency()) {
			SparkleParticleData data = SparkleParticleData.sparkle(rand.nextFloat(), r, g, b, 5);
			world.addParticle(data, pos.getX() + 0.3 + rand.nextFloat() * 0.5, pos.getY() + 0.5 + rand.nextFloat() * 0.5, pos.getZ() + 0.3 + rand.nextFloat() * 0.5, 0, 0, 0);
		}
	}

}
