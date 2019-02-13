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
import net.minecraft.block.BlockVine;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.common.block.BlockSolidVines;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import java.util.Map;

public class EntityVineBall extends EntityThrowable {

	private static final DataParameter<Float> GRAVITY = EntityDataManager.createKey(EntityVineBall.class, DataSerializers.FLOAT);
	private static final Map<EnumFacing, PropertyBool> propMap = ImmutableMap.of(EnumFacing.NORTH, BlockVine.NORTH, EnumFacing.SOUTH, BlockVine.SOUTH,
			EnumFacing.WEST, BlockVine.WEST, EnumFacing.EAST, BlockVine.EAST);

	public EntityVineBall(World world) {
		super(world);
	}

	public EntityVineBall(EntityLivingBase thrower, boolean gravity) {
		super(thrower.world, thrower);
		dataManager.set(GRAVITY, gravity ? 0.03F : 0F);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(GRAVITY, 0F);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void handleStatusUpdate(byte id) {
		if(id == 3) {
			for(int j = 0; j < 16; j++) {
				world.spawnParticle(EnumParticleTypes.ITEM_CRACK, posX, posY, posZ, Math.random() * 0.2 - 0.1, Math.random() * 0.25, Math.random() * 0.2 - 0.1, Item.getIdFromItem(ModItems.vineBall));
			}
		}
	}

	@Override
	protected void onImpact(@Nonnull RayTraceResult var1) {
		if(!world.isRemote) {
			if(var1 != null) {
				EnumFacing dir = var1.sideHit;

				if(dir != null && dir.getAxis() != EnumFacing.Axis.Y) {
					BlockPos pos = var1.getBlockPos().offset(dir);
					boolean first = true;
					while(pos.getY() > 0) {
						IBlockState state = world.getBlockState(pos);
						Block block = state.getBlock();
						if(block.isAir(state, world, pos)) {
							IBlockState stateSet = ModBlocks.solidVines.getDefaultState().withProperty(propMap.get(dir.getOpposite()), true);
							
							if(first && !((BlockSolidVines)ModBlocks.solidVines).canAttachTo(world, pos, dir)) break;
							first = false;
							
							world.setBlockState(pos, stateSet, 1 | 2);
							world.playEvent(2001, pos, Block.getStateId(stateSet));
							pos = pos.down();
						} else break;
					}
				}

			}

			this.world.setEntityState(this, (byte)3);
			setDead();
		}
	}

	@Override
	protected float getGravityVelocity() {
		return dataManager.get(GRAVITY);
	}

}
