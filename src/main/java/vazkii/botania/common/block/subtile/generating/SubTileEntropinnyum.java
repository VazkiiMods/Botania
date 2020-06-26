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
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.lib.LibMisc;

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

		if (!getWorld().isRemote && getMana() == 0) {
			List<TNTEntity> tnts = getWorld().getEntitiesWithinAABB(TNTEntity.class, new AxisAlignedBB(getEffectivePos().add(-RANGE, -RANGE, -RANGE), getEffectivePos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
			for (TNTEntity tnt : tnts) {
				FluidState fluid = getWorld().getFluidState(tnt.func_233580_cy_());
				if (tnt.getFuse() == 1 && tnt.isAlive() && fluid.isEmpty()) {
					tnt.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.2F, (1F + (getWorld().rand.nextFloat() - getWorld().rand.nextFloat()) * 0.2F) * 0.7F);
					tnt.remove();
					addMana(getMaxMana());
					sync();

					getWorld().addBlockEvent(getPos(), getBlockState().getBlock(), EXPLODE_EFFECT_EVENT, tnt.getEntityId());
					break;
				}
			}
		}
	}

	@Override
	public boolean receiveClientEvent(int event, int param) {
		if (event == EXPLODE_EFFECT_EVENT) {
			if (getWorld().isRemote && getWorld().getEntityByID(param) instanceof TNTEntity) {
				Entity e = getWorld().getEntityByID(param);

				for (int i = 0; i < 50; i++) {
					SparkleParticleData data = SparkleParticleData.sparkle((float) (Math.random() * 0.65F + 1.25F), 1F, (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 12);
					world.addParticle(data, e.getPosX() + Math.random() * 4 - 2, e.getPosY() + Math.random() * 4 - 2, e.getPosZ() + Math.random() * 4 - 2, 0, 0, 0);
				}

				getWorld().addParticle(ParticleTypes.EXPLOSION_EMITTER, e.getPosX(), e.getPosY(), e.getPosZ(), 1D, 0D, 0D);
			}
			return true;
		} else {
			return super.receiveClientEvent(event, param);
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
