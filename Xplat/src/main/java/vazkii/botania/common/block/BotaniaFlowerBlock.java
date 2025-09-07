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
import net.minecraft.core.Holder;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.internal.Colored;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.item.material.MysticalPetalItem;
import vazkii.botania.xplat.BotaniaConfig;

public abstract class BotaniaFlowerBlock extends FlowerBlock implements Colored {
	public final DyeColor color;

	protected BotaniaFlowerBlock(DyeColor color, Holder<MobEffect> effect, int seconds, Properties builder) {
		super(effect, seconds, builder);
		this.color = color;
	}

	@Override
	public DyeColor getColor() {
		return color;
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
		if (rand.nextDouble() >= BotaniaConfig.client().flowerParticleFrequency()) {
			return;
		}
		Vec3 offset = state.getOffset(world, pos);
		double x = pos.getX() + offset.x;
		double y = pos.getY() + offset.y;
		double z = pos.getZ() + offset.z;
		int color = MysticalPetalItem.getPetalLikeColor(this.color);
		float r = FastColor.ARGB32.red(color) / 255f;
		float g = FastColor.ARGB32.green(color) / 255f;
		float b = FastColor.ARGB32.blue(color) / 255f;

		SparkleParticleData data = SparkleParticleData.sparkle(rand.nextFloat(), r, g, b, 5);
		world.addParticle(data, x + 0.3 + rand.nextFloat() * 0.5, y + 0.5 + rand.nextFloat() * 0.5, z + 0.3 + rand.nextFloat() * 0.5, 0, 0, 0);
	}
}
