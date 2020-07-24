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
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.PistonTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.ModSubtiles;

import java.util.List;

public class SubTileEntropinnyum extends TileEntityGeneratingFlower {
	private static final String TAG_UNETHICAL = "botania:unethical";
	private static final int RANGE = 12;
	private static final int EXPLODE_EFFECT_EVENT = 0;
	public static final int MAX_MANA = 6500;
	private static final int ANGRY_EFFECT_EVENT = 1;

	public SubTileEntropinnyum() {
		super(ModSubtiles.ENTROPINNYUM);
		MinecraftForge.EVENT_BUS.addListener(this::onEntityJoin);
	}

	private void onEntityJoin(EntityJoinWorldEvent evt) {
		if (!evt.getWorld().isRemote && evt.getEntity() instanceof TNTEntity && isUnethical(evt.getEntity())) {
			evt.getEntity().getPersistentData().putBoolean(TAG_UNETHICAL, true);
		}
	}

	private static boolean isUnethical(Entity e) {
		BlockPos center = e.func_233580_cy_();
		int x = center.getX();
		int y = center.getY();
		int z = center.getZ();
		int range = 3;

		// Should actually check for corals too, but it gets broken when the piston extends
		int movingPistons = 0;
		int rails = 0;
		int slimes = 0;
		for (BlockPos pos : BlockPos.getAllInBoxMutable(x - range, y - range, z - range, x + range + 1, y + range + 1, z + range + 1)) {
			BlockState state = e.world.getBlockState(pos);
			if (state.getBlock() == Blocks.MOVING_PISTON) {
				movingPistons++;
				TileEntity te = e.world.getTileEntity(pos);
				if (te instanceof PistonTileEntity) {
					state = ((PistonTileEntity) te).getPistonState();
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

		if (!getWorld().isRemote && getMana() == 0) {
			List<TNTEntity> tnts = getWorld().getEntitiesWithinAABB(TNTEntity.class, new AxisAlignedBB(getEffectivePos().add(-RANGE, -RANGE, -RANGE), getEffectivePos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
			for (TNTEntity tnt : tnts) {
				FluidState fluid = getWorld().getFluidState(tnt.func_233580_cy_());
				if (tnt.getFuse() == 1 && tnt.isAlive() && fluid.isEmpty()) {
					boolean unethical = tnt.getPersistentData().getBoolean(TAG_UNETHICAL);
					tnt.playSound(unethical ? SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE : SoundEvents.ENTITY_GENERIC_EXPLODE, 0.2F, (1F + (getWorld().rand.nextFloat() - getWorld().rand.nextFloat()) * 0.2F) * 0.7F);
					tnt.remove();
					addMana(unethical ? 3 : getMaxMana());
					sync();

					getWorld().addBlockEvent(getPos(), getBlockState().getBlock(), unethical ? ANGRY_EFFECT_EVENT : EXPLODE_EFFECT_EVENT, tnt.getEntityId());
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
		} else if (event == ANGRY_EFFECT_EVENT) {
			if (getWorld().isRemote && getWorld().getEntityByID(param) instanceof TNTEntity) {
				Entity e = getWorld().getEntityByID(param);

				for (int i = 0; i < 50; i++) {
					world.addParticle(ParticleTypes.ANGRY_VILLAGER, e.getPosX() + Math.random() * 4 - 2, e.getPosY() + Math.random() * 4 - 2, e.getPosZ() + Math.random() * 4 - 2, 0, 0, 0);
				}
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
		return MAX_MANA;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
	}

}
