/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 26, 2014, 7:32:16 PM (GMT)]
 */
package vazkii.botania.common.entity;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.VineBlock;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.Map;

@OnlyIn(
	value = Dist.CLIENT,
	_interface = IRendersAsItem.class
)
public class EntityVineBall extends ThrowableEntity implements IRendersAsItem {
	@ObjectHolder(LibMisc.MOD_ID + ":vine_ball")
	public static EntityType<EntityVineBall> TYPE;

	private static final DataParameter<Float> GRAVITY = EntityDataManager.createKey(EntityVineBall.class, DataSerializers.FLOAT);
	private static final Map<Direction, BooleanProperty> propMap = ImmutableMap.of(Direction.NORTH, VineBlock.NORTH, Direction.SOUTH, VineBlock.SOUTH,
			Direction.WEST, VineBlock.WEST, Direction.EAST, VineBlock.EAST);

	public EntityVineBall(World world) {
		this(TYPE, world);
	}

	public EntityVineBall(EntityType<EntityVineBall> type, World world) {
		super(type, world);
	}

	public EntityVineBall(LivingEntity thrower, boolean gravity) {
		super(TYPE, thrower, thrower.world);
		dataManager.set(GRAVITY, gravity ? 0.03F : 0F);
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
		if(id == 3) {
			for(int j = 0; j < 16; j++) {
				world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(ModItems.vineBall)), posX, posY, posZ, Math.random() * 0.2 - 0.1, Math.random() * 0.25, Math.random() * 0.2 - 0.1);
			}
		}
	}

	@Override
	protected void onImpact(@Nonnull RayTraceResult rtr) {
		if(!world.isRemote) {
			if(rtr.getType() == RayTraceResult.Type.BLOCK) {
				Direction dir = ((BlockRayTraceResult) rtr).getFace();

				if(dir.getAxis() != Direction.Axis.Y) {
					BlockPos pos = ((BlockRayTraceResult) rtr).getPos().offset(dir);
					boolean first = true;
					while(pos.getY() > 0) {
						BlockState state = world.getBlockState(pos);
						Block block = state.getBlock();
						if(block.isAir(state, world, pos)) {
							BlockState stateSet = ModBlocks.solidVines.getDefaultState().with(propMap.get(dir.getOpposite()), true);
							
							if(first && !stateSet.isValidPosition(world, pos)) break;
							first = false;
							
							world.setBlockState(pos, stateSet, 1 | 2);
							world.playEvent(2001, pos, Block.getStateId(stateSet));
							pos = pos.down();
						} else break;
					}
				}

			}

			this.world.setEntityState(this, (byte)3);
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
