/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.ModSubtiles;

import java.util.List;

public class SubTileEntropinnyum extends TileEntityGeneratingFlower {
	private static final int RANGE = 12;
	private static final int EXPLODE_EFFECT_EVENT = 0;

	public SubTileEntropinnyum() {
		super(ModSubtiles.ENTROPINNYUM);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!getWorld().isClient && getMana() == 0) {
			List<TntEntity> tnts = getWorld().getNonSpectatingEntities(TntEntity.class, new Box(getEffectivePos().add(-RANGE, -RANGE, -RANGE), getEffectivePos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
			for (TntEntity tnt : tnts) {
				FluidState fluid = getWorld().getFluidState(tnt.getBlockPos());
				if (tnt.getFuseTimer() == 1 && tnt.isAlive() && fluid.isEmpty()) {
					tnt.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.2F, (1F + (getWorld().random.nextFloat() - getWorld().random.nextFloat()) * 0.2F) * 0.7F);
					tnt.remove();
					addMana(getMaxMana());
					sync();

					getWorld().addSyncedBlockEvent(getPos(), getCachedState().getBlock(), EXPLODE_EFFECT_EVENT, tnt.getEntityId());
					break;
				}
			}
		}
	}

	@Override
	public boolean onSyncedBlockEvent(int event, int param) {
		if (event == EXPLODE_EFFECT_EVENT) {
			if (getWorld().isClient && getWorld().getEntityById(param) instanceof TntEntity) {
				Entity e = getWorld().getEntityById(param);

				for (int i = 0; i < 50; i++) {
					SparkleParticleData data = SparkleParticleData.sparkle((float) (Math.random() * 0.65F + 1.25F), 1F, (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 12);
					world.addParticle(data, e.getX() + Math.random() * 4 - 2, e.getY() + Math.random() * 4 - 2, e.getZ() + Math.random() * 4 - 2, 0, 0, 0);
				}

				getWorld().addParticle(ParticleTypes.EXPLOSION_EMITTER, e.getX(), e.getY(), e.getZ(), 1D, 0D, 0D);
			}
			return true;
		} else {
			return super.onSyncedBlockEvent(event, param);
		}
	}

	@Override
	public int getColor() {
		return 0xcb0000;
	}

	@Override
	public int getMaxMana() {
		return 6500;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
	}

}
