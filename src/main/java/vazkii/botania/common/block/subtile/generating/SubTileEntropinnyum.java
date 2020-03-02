/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 7, 2014, 10:59:44 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;

public class SubTileEntropinnyum extends TileEntityGeneratingFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":entropinnyum")
	public static TileEntityType<SubTileEntropinnyum> TYPE;

	private static final int RANGE = 12;
	private static final int EXPLODE_EFFECT_EVENT = 0;

	public SubTileEntropinnyum() {
		super(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if(!getWorld().isRemote && getMana() == 0) {
			List<TNTEntity> tnts = getWorld().getEntitiesWithinAABB(TNTEntity.class, new AxisAlignedBB(getEffectivePos().add(-RANGE, -RANGE, -RANGE), getEffectivePos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
			for(TNTEntity tnt : tnts) {
				IFluidState fluid = getWorld().getFluidState(new BlockPos(tnt));
				if(tnt.getFuse() == 1 && tnt.isAlive() && fluid.isEmpty()) {
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
		if(event == EXPLODE_EFFECT_EVENT) {
			if(getWorld().isRemote && getWorld().getEntityByID(param) instanceof TNTEntity) {
				Entity e = getWorld().getEntityByID(param);

				for(int i = 0; i < 50; i++) {
                    SparkleParticleData data = SparkleParticleData.sparkle((float) (Math.random() * 0.65F + 1.25F), 1F, (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 12);
                    world.addParticle(data, e.posX + Math.random() * 4 - 2, e.posY + Math.random() * 4 - 2, e.posZ + Math.random() * 4 - 2, 0, 0, 0);
                }

				getWorld().addParticle(ParticleTypes.EXPLOSION_EMITTER, e.posX, e.posY, e.posZ, 1D, 0D, 0D);
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
