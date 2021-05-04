/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.VineBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

import java.util.Map;

@OnlyIn(
	value = Dist.CLIENT,
	_interface = IRendersAsItem.class
)
public class EntityVineBall extends ThrowableEntity implements IRendersAsItem {
	private static final DataParameter<Float> GRAVITY = EntityDataManager.createKey(EntityVineBall.class, DataSerializers.FLOAT);
	private static final Map<Direction, BooleanProperty> propMap = ImmutableMap.of(Direction.NORTH, VineBlock.NORTH, Direction.SOUTH, VineBlock.SOUTH,
			Direction.WEST, VineBlock.WEST, Direction.EAST, VineBlock.EAST);

	public EntityVineBall(EntityType<EntityVineBall> type, World world) {
		super(type, world);
	}

	public EntityVineBall(LivingEntity thrower, boolean gravity) {
		super(ModEntities.VINE_BALL, thrower, thrower.world);
		dataManager.set(GRAVITY, gravity ? 0.03F : 0F);
	}

	public EntityVineBall(double x, double y, double z, World worldIn) {
		super(ModEntities.VINE_BALL, x, y, z, worldIn);
		dataManager.set(GRAVITY, 0.03F);
	}

	@Override
	protected void registerData() {
		dataManager.register(GRAVITY, 0F);
	}

	@Nonnull
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleStatusUpdate(byte id) {
		if (id == 3) {
			for (int j = 0; j < 16; j++) {
				world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(ModItems.vineBall)), getPosX(), getPosY(), getPosZ(), Math.random() * 0.2 - 0.1, Math.random() * 0.25, Math.random() * 0.2 - 0.1);
			}
		}
	}

	@Override
	protected void onImpact(@Nonnull RayTraceResult rtr) {
		if (!world.isRemote) {
			if (rtr.getType() == RayTraceResult.Type.BLOCK) {
				Direction dir = ((BlockRayTraceResult) rtr).getFace();

				if (dir.getAxis() != Direction.Axis.Y) {
					BlockPos pos = ((BlockRayTraceResult) rtr).getPos().offset(dir);
					boolean first = true;
					while (pos.getY() > 0) {
						BlockState state = world.getBlockState(pos);
						Block block = state.getBlock();
						if (block.isAir(state, world, pos)) {
							BlockState stateSet = ModBlocks.solidVines.getDefaultState().with(propMap.get(dir.getOpposite()), true);

							if (first && !stateSet.isValidPosition(world, pos)) {
								break;
							}
							first = false;

							world.setBlockState(pos, stateSet);
							world.playEvent(2001, pos, Block.getStateId(stateSet));
							pos = pos.down();
						} else {
							break;
						}
					}
				}

			}

			this.world.setEntityState(this, (byte) 3);
			remove();
		}
	}

	@Override
	protected float getGravityVelocity() {
		return dataManager.get(GRAVITY);
	}

	@Nonnull
	@Override
	public ItemStack getItem() {
		return new ItemStack(ModItems.vineBall);
	}
}
