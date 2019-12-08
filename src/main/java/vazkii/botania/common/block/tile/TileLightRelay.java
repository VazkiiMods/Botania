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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import elucent.albedo.lighting.ILightProvider;
import elucent.albedo.lighting.Light;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.LuminizerVariant;
import vazkii.botania.api.wand.IWandBindable;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.LibMisc;

public class TileLightRelay extends TileMod implements ITickable, IWandBindable {

	private static final int MAX_DIST = 20;

	private static final String TAG_BIND_X = "bindX";
	private static final String TAG_BIND_Y = "bindY";
	private static final String TAG_BIND_Z = "bindZ";

	private BlockPos bindPos = new BlockPos(0, -1, 0);
	private int ticksElapsed = 0;

	public void mountEntity(Entity e) {
		BlockPos nextDest = getNextDestination();
		if(e.isRiding() || world.isRemote || nextDest == null || !isValidBinding())
			return;

		EntityPlayerMover mover = new EntityPlayerMover(world, pos, nextDest);
		world.spawnEntity(mover);
		e.startRiding(mover);
		if(!(e instanceof EntityItem)) {
			mover.playSound(ModSounds.lightRelay, 0.2F, (float) Math.random() * 0.3F + 0.7F);
		}
		if(e instanceof EntityPlayerMP) {
			PlayerHelper.grantCriterion((EntityPlayerMP) e, new ResourceLocation(LibMisc.MOD_ID, "main/luminizer_ride"), "code_triggered");
		}
	}

	@Override
	public void update() {
		ticksElapsed++;

		BlockPos nextDest = getNextDestination();
		if(nextDest != null && nextDest.getY() > -1 && isValidBinding()) {
			if(world.isRemote) {
				Vector3 vec = getMovementVector();
				if(vec != null) {
					double dist = 0.1;
					int size = (int) (vec.mag() / dist);
					int count = 10;
					int start = ticksElapsed % size;

					Vector3 vecMag = vec.normalize().multiply(dist);
					Vector3 vecTip = vecMag.multiply(start).add(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

					double radPer = Math.PI / 16.0;
					float mul = 0.5F;
					float mulPer = 0.4F;
					float maxMul = 2;
					for(int i = start; i < start + count; i++) {
						mul = Math.min(maxMul, mul + mulPer);
						double rad = radPer * (i + ticksElapsed * 0.4);
						Vector3 vecRot = vecMag.crossProduct(Vector3.ONE).multiply(mul).rotate(rad, vecMag).add(vecTip);
						Botania.proxy.wispFX(vecRot.x, vecRot.y, vecRot.z, 0.4F, 0.4F, 1F, 0.1F, (float) -vecMag.x, (float) -vecMag.y, (float) -vecMag.z, 1F);
						vecTip = vecTip.add(vecMag);
					}
				}
			} else {
				BlockPos endpoint = getEndpoint();

				if(endpoint != null) {
					AxisAlignedBB aabb = ModBlocks.lightRelay.getBoundingBox(world.getBlockState(pos), world, pos).offset(pos);
					float range = 0.5F;
					List<EntityEnderPearl> enderPearls = world.getEntitiesWithinAABB(EntityEnderPearl.class, aabb.grow(range));
					for(EntityEnderPearl pearl : enderPearls) {
						pearl.setPositionAndUpdate(
								endpoint.getX() + pearl.posX - pos.getX(),
								endpoint.getY() + pearl.posY - pos.getY(),
								endpoint.getZ() + pearl.posZ - pos.getZ()
								);
					}
				}
			}
		}
	}

	private boolean isValidBinding() {
		BlockPos nextDest = getNextDestination();
		if(nextDest == null)
			return false;

		Block block = world.getBlockState(nextDest).getBlock();
		return block == ModBlocks.lightRelay;
	}

	private BlockPos getEndpoint() {
		List<TileLightRelay> pointsPassed = new ArrayList<>();
		TileLightRelay relay = this;
		BlockPos lastCoords = null;

		// Doing while(true) gives an unreachable code error
		boolean run = true;
		while(run) {
			if(pointsPassed.contains(relay))
				return null; // Circular path
			pointsPassed.add(relay);

			BlockPos coords = relay.getNextDestination();
			if(coords == null)
				return lastCoords;

			TileEntity tile = world.getTileEntity(coords);
			if(tile != null && tile instanceof TileLightRelay)
				relay = (TileLightRelay) tile;
			else return lastCoords;

			lastCoords = coords;
		}

		return null;
	}

	public Vector3 getMovementVector() {
		BlockPos dest = getNextDestination();
		if(dest == null)
			return null;

		return new Vector3(dest.getX() - pos.getX(), dest.getY() - pos.getY(), dest.getZ() - pos.getZ());
	}

	@Override
	public BlockPos getBinding() {
		return bindPos;
	}

	public BlockPos getNextDestination() {
		IBlockState state = world.getBlockState(pos);
		if(state.getValue(BotaniaStateProps.LUMINIZER_VARIANT) == LuminizerVariant.TOGGLE && state.getValue(BotaniaStateProps.POWERED))
			return null;
		else if(state.getValue(BotaniaStateProps.LUMINIZER_VARIANT) == LuminizerVariant.FORK) {
			BlockPos torchPos = null;
			for(int i = -2; i < 3; i++) {
				BlockPos testPos = pos.add(0, i, 0);

				IBlockState testState = world.getBlockState(testPos);
				if(testState.getBlock() == ModBlocks.animatedTorch) {
					torchPos = testPos;
					break;
				}
			}

			if(torchPos != null) {
				TileAnimatedTorch torch = (TileAnimatedTorch) world.getTileEntity(torchPos);
				EnumFacing side = TileAnimatedTorch.SIDES[torch.side].getOpposite();
				for(int i = 1; i < MAX_DIST; i++) {
					BlockPos testPos = pos.offset(side, i);
					IBlockState testState = world.getBlockState(testPos);
					if(testState.getBlock() == ModBlocks.lightRelay)
						return testPos;
				}
			}
		}

		return getBinding();
	}

	@Override
	public boolean canSelect(EntityPlayer player, ItemStack wand, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Override
	public boolean bindTo(EntityPlayer player, ItemStack wand, BlockPos pos, EnumFacing side) {
		if(player.world.getBlockState(pos).getBlock() != ModBlocks.lightRelay || pos.distanceSq(getPos()) > MAX_DIST * MAX_DIST)
			return false;

		bindPos = pos;
		return true;
	}

	@Override
	public void readPacketNBT(NBTTagCompound cmp) {
		bindPos = new BlockPos(
				cmp.getInteger(TAG_BIND_X),
				cmp.getInteger(TAG_BIND_Y),
				cmp.getInteger(TAG_BIND_Z)
				);
	}

	@Override
	public void writePacketNBT(NBTTagCompound cmp) {
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

			if(getPassengers().isEmpty() && !world.isRemote) {
				setDead();
				return;
			}

			boolean isItem = getRidingEntity() instanceof EntityItem;
			if(!isItem && ticksExisted % 30 == 0)
				playSound(ModSounds.lightRelay, 0.05F, (float) Math.random() * 0.3F + 0.7F);

			BlockPos pos = new BlockPos(this);
			BlockPos exitPos = getExitPos();

			if(pos.equals(exitPos)) {
				TileEntity tile = world.getTileEntity(pos);
				if(tile != null && tile instanceof TileLightRelay) {
					if(world.getBlockState(pos).getValue(BotaniaStateProps.LUMINIZER_VARIANT) == LuminizerVariant.DETECTOR) {
						world.setBlockState(pos, world.getBlockState(pos).withProperty(BotaniaStateProps.POWERED, true), 1 | 2);
						world.scheduleUpdate(pos, tile.getBlockType(), tile.getBlockType().tickRate(world));
					}

					TileLightRelay relay = (TileLightRelay) tile;
					BlockPos bind = relay.getNextDestination();
					if(bind != null && relay.isValidBinding()) {
						setExit(bind);
						return;
					}
				}

				for(Entity e : getPassengers()) {
					e.dismountRidingEntity();
					if(e instanceof EntityPlayerMP)
						((EntityPlayerMP) e).connection.setPlayerLocation(posX, posY, posZ, e.rotationYaw, e.rotationPitch);
					else e.setPosition(posX, posY, posZ);
				}
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

					Botania.proxy.sparkleFX(posX + cos * s, posY - 0.5, posZ + sin * s, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1.2F, 10);
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
