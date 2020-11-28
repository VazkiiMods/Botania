/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.components.EntityComponents;

import java.util.List;

public class SubTileEntropinnyum extends TileEntityGeneratingFlower {
	private static final int RANGE = 12;
	private static final int EXPLODE_EFFECT_EVENT = 0;
	private static final int ANGRY_EFFECT_EVENT = 1;

	public SubTileEntropinnyum() {
		super(ModSubtiles.ENTROPINNYUM);
	}

	public static boolean isUnethical(Entity e) {
		if (!e.world.getChunkManager().shouldTickEntity(e)) {
			return false;
		}

		BlockPos center = e.getBlockPos();
		int x = center.getX();
		int y = center.getY();
		int z = center.getZ();
		int range = 3;

		// Should actually check for corals too, but it gets broken when the piston extends
		int movingPistons = 0;
		int rails = 0;
		int slimes = 0;
		for (BlockPos pos : BlockPos.iterate(x - range, y - range, z - range, x + range + 1, y + range + 1, z + range + 1)) {
			BlockState state = e.world.getBlockState(pos);
			if (state.getBlock() == Blocks.MOVING_PISTON) {
				movingPistons++;
				BlockEntity te = e.world.getBlockEntity(pos);
				if (te instanceof PistonBlockEntity) {
					state = ((PistonBlockEntity) te).getPushedBlock();
				}
			}

			if (state.getBlock() instanceof DetectorRailBlock) {
				rails++;
			} else if (state.getBlock() instanceof SlimeBlock || state.getBlock() instanceof HoneyBlock) {
				slimes++;
			}

			if (movingPistons > 0 && rails > 0 && slimes > 0) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!getWorld().isClient && getMana() == 0) {
			List<TntEntity> tnts = getWorld().getNonSpectatingEntities(TntEntity.class, new Box(getEffectivePos().add(-RANGE, -RANGE, -RANGE), getEffectivePos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
			for (TntEntity tnt : tnts) {
				FluidState fluid = getWorld().getFluidState(tnt.getBlockPos());
				if (tnt.getFuseTimer() == 1 && tnt.isAlive() && fluid.isEmpty()) {
					boolean unethical = EntityComponents.TNT_ETHICAL.get(tnt).unethical;
					tnt.playSound(unethical ? SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE : SoundEvents.ENTITY_GENERIC_EXPLODE, 0.2F, (1F + (getWorld().random.nextFloat() - getWorld().random.nextFloat()) * 0.2F) * 0.7F);
					tnt.remove();
					addMana(unethical ? 3 : getMaxMana());
					sync();

					getWorld().addSyncedBlockEvent(getPos(), getCachedState().getBlock(), unethical ? ANGRY_EFFECT_EVENT : EXPLODE_EFFECT_EVENT, tnt.getEntityId());
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
		} else if (event == ANGRY_EFFECT_EVENT) {
			if (getWorld().isClient && getWorld().getEntityById(param) instanceof TntEntity) {
				Entity e = getWorld().getEntityById(param);

				for (int i = 0; i < 50; i++) {
					world.addParticle(ParticleTypes.ANGRY_VILLAGER, e.getX() + Math.random() * 4 - 2, e.getY() + Math.random() * 4 - 2, e.getZ() + Math.random() * 4 - 2, 0, 0, 0);
				}
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
