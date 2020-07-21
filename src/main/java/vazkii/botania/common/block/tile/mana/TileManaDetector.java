/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.mana;

import com.google.common.base.Predicates;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Box;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileMod;

public class TileManaDetector extends TileMod implements Tickable {
	public TileManaDetector() {
		super(ModTiles.MANA_DETECTOR);
	}

	@Override
	public void tick() {
		boolean state = getCachedState().get(Properties.POWERED);
		boolean expectedState = world.getEntities(ThrownEntity.class, new Box(pos, pos.add(1, 1, 1)), Predicates.instanceOf(IManaBurst.class)).size() != 0;
		if (state != expectedState && !world.isClient) {
			world.setBlockState(getPos(), getCachedState().with(Properties.POWERED, expectedState));
		}

		if (expectedState) {
			for (int i = 0; i < 4; i++) {
				SparkleParticleData data = SparkleParticleData.sparkle(0.7F + 0.5F * (float) Math.random(), 1F, 0.2F, 0.2F, 5);
				world.addParticle(data, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0, 0, 0);
			}
		}
	}

}
