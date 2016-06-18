/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 15, 2015, 8:32:04 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.sound.BotaniaSoundEvents;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.LuminizerVariant;
import vazkii.botania.api.wand.IWandBindable;
import vazkii.botania.common.Botania;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.Vector3;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TileLightRelay extends TileMod implements IWandBindable {

	private static final int MAX_DIST = 20;

	private static final String TAG_BIND_X = "bindX";
	private static final String TAG_BIND_Y = "bindY";
	private static final String TAG_BIND_Z = "bindZ";

	BlockPos bindPos = new BlockPos(0, -1, 0);
	int ticksElapsed = 0;

	public void mountEntity(Entity e) {
		if(e.isRiding() || worldObj.isRemote || bindPos.getY() == -1 || !isValidBinding())
			return;

		EntityPlayerMover mover = new EntityPlayerMover(worldObj, pos, bindPos);
		worldObj.spawnEntityInWorld(mover);
		e.startRiding(mover);
		if(!(e instanceof EntityItem)) {
			mover.playSound(BotaniaSoundEvents.lightRelay, 0.2F, (float) Math.random() * 0.3F + 0.7F);
			if(e instanceof EntityPlayer)
				((EntityPlayer) e).addStat(ModAchievements.luminizerRide, 1);
		}
	}

	@Override
	public void update() {
		ticksElapsed++;

		if(bindPos.getY() > -1 && isValidBinding()) {
			Vector3 vec = getMovementVector();

			double dist = 0.1;
			int size = (int) (vec.mag() / dist);
			int count = 10;
			int start = ticksElapsed % size;

			Vector3 vecMag = vec.copy().normalize().multiply(dist);
			Vector3 vecTip = vecMag.copy().multiply(start).add(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

			double radPer = Math.PI / 16.0;
			float mul = 0.5F;
			float mulPer = 0.4F;
			float maxMul = 2;
			for(int i = start; i < start + count; i++) {
				mul = Math.min(maxMul, mul + mulPer);
				double rad = radPer * (i + ticksElapsed * 0.4);
				Vector3 vecRot = vecMag.copy().crossProduct(Vector3.one).multiply(mul).rotate(rad, vecMag).add(vecTip);
				Botania.proxy.wispFX(worldObj, vecRot.x, vecRot.y, vecRot.z, 0.4F, 0.4F, 1F, 0.1F, (float) -vecMag.x, (float) -vecMag.y, (float) -vecMag.z, 1F);
				vecTip.add(vecMag);
			}
			
			BlockPos endpoint = getEndpoint();

			if(endpoint != null && !worldObj.isRemote) {
				float range = 0.5F;
				List<EntityEnderPearl> enderPearls = worldObj.getEntitiesWithinAABB(EntityEnderPearl.class, new AxisAlignedBB(pos.add(-range, -range, -range), pos.add(1 + range, 1 + range, 1 + range)));
				for(EntityEnderPearl pearl : enderPearls) {
					pearl.posX = endpoint.getX() + pearl.posX - pos.getX();
					pearl.posY = endpoint.getY() + pearl.posY - pos.getY();
					pearl.posZ = endpoint.getZ() + pearl.posZ - pos.getZ();
				}
			}
		}
	}

	public boolean isValidBinding() {
		Block block = worldObj.getBlockState(bindPos).getBlock();
		return block == ModBlocks.lightRelay;
	}
	
	public BlockPos getEndpoint() {
		List<TileLightRelay> pointsPassed = new ArrayList<>();
		TileLightRelay relay = this;
		BlockPos lastCoords = null;

		// Doing while(true) gives an unreachable code error
		boolean run = true;
		while(run) {
			if(pointsPassed.contains(relay))
				return null; // Circular path
			pointsPassed.add(relay);
			
			BlockPos coords = relay.getBinding();
			if(coords == null)
				return lastCoords;
			
			TileEntity tile = worldObj.getTileEntity(coords);
			if(tile != null && tile instanceof TileLightRelay)
				relay = (TileLightRelay) tile;
			else return lastCoords;

			lastCoords = coords;
		}

		return null;
	}

	public Vector3 getMovementVector() {
		return new Vector3(bindPos.getX() - pos.getX(), bindPos.getY() - pos.getY(), bindPos.getZ() - pos.getZ());
	}

	@Override
	public BlockPos getBinding() {
		return bindPos.getY() == -1 ? null : bindPos;
	}

	@Override
	public boolean canSelect(EntityPlayer player, ItemStack wand, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Override
	public boolean bindTo(EntityPlayer player, ItemStack wand, BlockPos pos, EnumFacing side) {
		if(player.worldObj.getBlockState(pos).getBlock() != ModBlocks.lightRelay || MathHelper.pointDistanceSpace(pos, getPos()) > MAX_DIST)
			return false;

		bindPos = pos;
		return true;
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		bindPos = new BlockPos(
			cmp.getInteger(TAG_BIND_X),
			cmp.getInteger(TAG_BIND_Y),
			cmp.getInteger(TAG_BIND_Z)
		);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_BIND_X, bindPos.getX());
		cmp.setInteger(TAG_BIND_Y, bindPos.getY());
		cmp.setInteger(TAG_BIND_Z, bindPos.getZ());
	}

	public static class EntityPlayerMover extends Entity {

		private static final String TAG_EXIT_X = "exitX";
		private static final String TAG_EXIT_Y = "exitY";
		private static final String TAG_EXIT_Z = "exitZ";
		private static final DataParameter<BlockPos> EXIT_POS = EntityDataManager.createKey(EntityPlayerMover.class, DataSerializers.BLOCK_POS);


		public EntityPlayerMover(World world) {
			super(world);
		}

		public EntityPlayerMover(World world, BlockPos pos, BlockPos exitPos) {
			this(world);
			setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
			setExit(exitPos);
		}

		@Override
		protected void entityInit() {
			setSize(0F, 0F);
			noClip = true;
			dataManager.register(EXIT_POS, BlockPos.ORIGIN);
		}

		@Override
		public void onUpdate() {
			super.onUpdate();

			if(getPassengers().isEmpty() && !worldObj.isRemote) {
				setDead();
				return;
			}

			boolean isItem = getRidingEntity() instanceof EntityItem;
			if(!isItem && ticksExisted % 30 == 0)
				playSound(BotaniaSoundEvents.lightRelay, 0.05F, (float) Math.random() * 0.3F + 0.7F);

			BlockPos pos = new BlockPos(this);
			BlockPos exitPos = getExitPos();

			if(pos.equals(exitPos)) {
				TileEntity tile = worldObj.getTileEntity(pos);
				if(tile != null && tile instanceof TileLightRelay) {
					if(worldObj.getBlockState(pos).getValue(BotaniaStateProps.LUMINIZER_VARIANT) == LuminizerVariant.DETECTOR) {
						worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(BotaniaStateProps.POWERED, true), 1 | 2);
						worldObj.scheduleUpdate(pos, tile.getBlockType(), tile.getBlockType().tickRate(worldObj));
					}

					TileLightRelay relay = (TileLightRelay) tile;
					BlockPos bind = relay.getBinding();
					if(bind != null && relay.isValidBinding()) {
						setExit(bind);
						return;
					}
				}

				posY += 1.5;
				setDead();
			} else {
				Vector3 thisVec = Vector3.fromEntity(this);
				Vector3 motVec = thisVec.negate().add(exitPos.getX() + 0.5, exitPos.getY() + 0.5, exitPos.getZ() + 0.5).normalize().multiply(0.5);

				Color color;

				int count = 4;
				for(int i = 0; i < count; i++) {
					color = Color.getHSBColor(ticksExisted / 36F + 1F / count * i, 1F, 1F);
					double rad = Math.PI * 2.0 / count * i + ticksExisted / Math.PI;
					double cos = Math.cos(rad);
					double sin = Math.sin(rad);
					double s = 0.4;

					Botania.proxy.sparkleFX(worldObj, posX + cos * s, posY - 0.5, posZ + sin * s, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1.2F, 10);
				}

				posX += motVec.x;
				posY += motVec.y;
				posZ += motVec.z;
			}
		}

		@Override
		public boolean shouldRiderSit() {
			return false;
		}

		@Override
		public boolean attackEntityFrom(@Nonnull DamageSource source, float damage) {
			return false;
		}

		@Override
		protected void readEntityFromNBT(@Nonnull NBTTagCompound cmp) {
			setExit(new BlockPos(cmp.getInteger(TAG_EXIT_X), cmp.getInteger(TAG_EXIT_Y), cmp.getInteger(TAG_EXIT_Z)));
		}

		@Override
		protected void writeEntityToNBT(@Nonnull NBTTagCompound cmp) {
			BlockPos exit = getExitPos();
			cmp.setInteger(TAG_EXIT_X, exit.getX());
			cmp.setInteger(TAG_EXIT_Y, exit.getY());
			cmp.setInteger(TAG_EXIT_Z, exit.getZ());
		}

		public BlockPos getExitPos() {
			return dataManager.get(EXIT_POS);
		}

		public void setExit(BlockPos pos) {
			dataManager.set(EXIT_POS, pos);
		}

	}

}
