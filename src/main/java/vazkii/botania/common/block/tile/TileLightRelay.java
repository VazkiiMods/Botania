/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.TransportationHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.wand.IWandBindable;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.BlockLightRelay;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.ModEntities;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class TileLightRelay extends TileMod implements ITickableTileEntity, IWandBindable {
	private static final int MAX_DIST = 20;

	private static final String TAG_BIND_X = "bindX";
	private static final String TAG_BIND_Y = "bindY";
	private static final String TAG_BIND_Z = "bindZ";

	private BlockPos bindPos = new BlockPos(0, -1, 0);
	private int ticksElapsed = 0;

	public TileLightRelay() {
		super(ModTiles.LIGHT_RELAY);
	}

	public void mountEntity(Entity e) {
		BlockPos nextDest = getNextDestination();
		if (e.isPassenger() || world.isRemote || nextDest == null || !isValidBinding()) {
			return;
		}

		EntityPlayerMover mover = new EntityPlayerMover(world, pos, nextDest);
		world.addEntity(mover);
		e.startRiding(mover);
		if (!(e instanceof ItemEntity)) {
			mover.playSound(ModSounds.lightRelay, 0.2F, (float) Math.random() * 0.3F + 0.7F);
		}
		if (e instanceof ServerPlayerEntity) {
			PlayerHelper.grantCriterion((ServerPlayerEntity) e, prefix("main/luminizer_ride"), "code_triggered");
		}
	}

	@Override
	public void tick() {
		ticksElapsed++;

		BlockPos nextDest = getNextDestination();
		if (nextDest != null && nextDest.getY() > -1 && isValidBinding()) {
			if (world.isRemote) {
				Vector3 vec = getMovementVector();
				if (vec != null) {
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
					WispParticleData data = WispParticleData.wisp(0.1F, 0.4F, 0.4F, 1F, 1);
					for (int i = start; i < start + count; i++) {
						mul = Math.min(maxMul, mul + mulPer);
						double rad = radPer * (i + ticksElapsed * 0.4);
						Vector3 vecRot = vecMag.crossProduct(Vector3.ONE).multiply(mul).rotate(rad, vecMag).add(vecTip);
						world.addParticle(data, vecRot.x, vecRot.y, vecRot.z, (float) -vecMag.x, (float) -vecMag.y, (float) -vecMag.z);
						vecTip = vecTip.add(vecMag);
					}
				}
			} else {
				BlockPos endpoint = getEndpoint();

				if (endpoint != null) {
					AxisAlignedBB aabb = getBlockState().getShape(world, pos).getBoundingBox().offset(pos);
					float range = 0.6F;
					List<EnderPearlEntity> enderPearls = world.getEntitiesWithinAABB(EnderPearlEntity.class, aabb.grow(range));
					for (EnderPearlEntity pearl : enderPearls) {
						pearl.setPositionAndUpdate(
								endpoint.getX() + (pearl.getPosX() - pos.getX()),
								endpoint.getY() + (pearl.getPosY() - pos.getY()),
								endpoint.getZ() + (pearl.getPosZ() - pos.getZ())
						);
					}
				}
			}
		}
	}

	private boolean isValidBinding() {
		BlockPos nextDest = getNextDestination();
		if (nextDest == null) {
			return false;
		}

		Block block = world.getBlockState(nextDest).getBlock();
		return block instanceof BlockLightRelay;
	}

	private BlockPos getEndpoint() {
		List<TileLightRelay> pointsPassed = new ArrayList<>();
		TileLightRelay relay = this;
		BlockPos lastCoords = null;

		// Doing while(true) gives an unreachable code error
		boolean run = true;
		while (run) {
			if (pointsPassed.contains(relay)) {
				return null; // Circular path
			}
			pointsPassed.add(relay);

			BlockPos coords = relay.getNextDestination();
			if (coords == null) {
				return lastCoords;
			}

			TileEntity tile = world.getTileEntity(coords);
			if (tile != null && tile instanceof TileLightRelay) {
				relay = (TileLightRelay) tile;
			} else {
				return lastCoords;
			}

			lastCoords = coords;
		}

		return null;
	}

	public Vector3 getMovementVector() {
		BlockPos dest = getNextDestination();
		if (dest == null) {
			return null;
		}

		return new Vector3(dest.getX() - pos.getX(), dest.getY() - pos.getY(), dest.getZ() - pos.getZ());
	}

	@Override
	public BlockPos getBinding() {
		return bindPos;
	}

	public BlockPos getNextDestination() {
		BlockState state = getBlockState();
		if (state.getBlock() == ModBlocks.lightRelayToggle && state.get(BlockStateProperties.POWERED)) {
			return null;
		} else if (state.getBlock() == ModBlocks.lightRelayFork) {
			BlockPos torchPos = null;
			for (int i = -2; i < 3; i++) {
				BlockPos testPos = pos.add(0, i, 0);

				BlockState testState = world.getBlockState(testPos);
				if (testState.getBlock() == ModBlocks.animatedTorch) {
					torchPos = testPos;
					break;
				}
			}

			if (torchPos != null) {
				TileAnimatedTorch torch = (TileAnimatedTorch) world.getTileEntity(torchPos);
				Direction side = TileAnimatedTorch.SIDES[torch.side].getOpposite();
				for (int i = 1; i < MAX_DIST; i++) {
					BlockPos testPos = pos.offset(side, i);
					BlockState testState = world.getBlockState(testPos);
					if (testState.getBlock() instanceof BlockLightRelay) {
						return testPos;
					}
				}
			}
		}

		return getBinding();
	}

	@Override
	public boolean canSelect(PlayerEntity player, ItemStack wand, BlockPos pos, Direction side) {
		return true;
	}

	@Override
	public boolean bindTo(PlayerEntity player, ItemStack wand, BlockPos pos, Direction side) {
		if (!(player.world.getBlockState(pos).getBlock() instanceof BlockLightRelay)
				|| pos.distanceSq(getPos()) > MAX_DIST * MAX_DIST) {
			return false;
		}

		bindPos = pos;
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		return true;
	}

	@Override
	public void readPacketNBT(CompoundNBT cmp) {
		bindPos = new BlockPos(
				cmp.getInt(TAG_BIND_X),
				cmp.getInt(TAG_BIND_Y),
				cmp.getInt(TAG_BIND_Z)
		);
	}

	@Override
	public void writePacketNBT(CompoundNBT cmp) {
		cmp.putInt(TAG_BIND_X, bindPos.getX());
		cmp.putInt(TAG_BIND_Y, bindPos.getY());
		cmp.putInt(TAG_BIND_Z, bindPos.getZ());
	}

	public static class EntityPlayerMover extends Entity {
		private static final String TAG_EXIT_X = "exitX";
		private static final String TAG_EXIT_Y = "exitY";
		private static final String TAG_EXIT_Z = "exitZ";
		private static final DataParameter<BlockPos> EXIT_POS = EntityDataManager.createKey(EntityPlayerMover.class, DataSerializers.BLOCK_POS);

		public EntityPlayerMover(EntityType<EntityPlayerMover> type, World world) {
			super(type, world);
			noClip = true;
		}

		public EntityPlayerMover(World world, BlockPos pos, BlockPos exitPos) {
			this(ModEntities.PLAYER_MOVER, world);
			setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
			setExit(exitPos);
		}

		@Override
		protected void registerData() {
			dataManager.register(EXIT_POS, BlockPos.ZERO);
		}

		@Override
		public void tick() {
			super.tick();

			if (getPassengers().isEmpty() && !world.isRemote) {
				remove();
				return;
			}

			boolean isItem = getRidingEntity() instanceof ItemEntity;
			if (!isItem && ticksExisted % 30 == 0) {
				playSound(ModSounds.lightRelay, 0.05F, (float) Math.random() * 0.3F + 0.7F);
			}

			BlockPos pos = getPosition();
			BlockPos exitPos = getExitPos();

			if (pos.equals(exitPos)) {
				TileEntity tile = world.getTileEntity(pos);
				if (tile instanceof TileLightRelay) {
					BlockState state = world.getBlockState(pos);
					if (state.getBlock() == ModBlocks.lightRelayDetector) {
						world.setBlockState(pos, state.with(BlockStateProperties.POWERED, true));
						world.getPendingBlockTicks().scheduleTick(pos, state.getBlock(), 2);
					}

					TileLightRelay relay = (TileLightRelay) tile;
					BlockPos bind = relay.getNextDestination();
					if (bind != null && relay.isValidBinding()) {
						setExit(bind);
						return;
					}
				}

				for (Entity e : getPassengers()) {
					e.stopRiding();
				}
				remove();
			} else {
				Vector3d thisVec = getPositionVec();
				Vector3d motVec = thisVec.inverse().add(exitPos.getX() + 0.5, exitPos.getY() + 0.5, exitPos.getZ() + 0.5).normalize().scale(0.5);

				int color;

				int count = 4;
				for (int i = 0; i < count; i++) {
					color = MathHelper.hsvToRGB(ticksExisted / 36F + 1F / count * i, 1F, 1F);
					double rad = Math.PI * 2.0 / count * i + ticksExisted / Math.PI;
					double cos = Math.cos(rad);
					double sin = Math.sin(rad);
					double s = 0.4;

					int r = (color >> 16) & 0xFF;
					int g = (color >> 8) & 0xFF;
					int b = color & 0xFF;
					SparkleParticleData data = SparkleParticleData.sparkle(1.2F, r / 255F, g / 255F, b / 255F, 10);
					world.addParticle(data, getPosX() + cos * s, getPosY() - 0.5, getPosZ() + sin * s, 0, 0, 0);
				}

				setPosition(getPosX() + motVec.x, getPosY() + motVec.y, getPosZ() + motVec.z);
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
		protected void readAdditional(@Nonnull CompoundNBT cmp) {
			setExit(new BlockPos(cmp.getInt(TAG_EXIT_X), cmp.getInt(TAG_EXIT_Y), cmp.getInt(TAG_EXIT_Z)));
		}

		@Override
		protected void writeAdditional(@Nonnull CompoundNBT cmp) {
			BlockPos exit = getExitPos();
			cmp.putInt(TAG_EXIT_X, exit.getX());
			cmp.putInt(TAG_EXIT_Y, exit.getY());
			cmp.putInt(TAG_EXIT_Z, exit.getZ());
		}

		// [VanillaCopy] PigEntity logic to select a dismount location
		@Nonnull
		@Override
		public Vector3d func_230268_c_(@Nonnull LivingEntity living) {
			Direction direction = living.getHorizontalFacing();
			int[][] aint = TransportationHelper.func_234632_a_(direction);
			BlockPos blockpos = this.getPosition();
			BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

			for (Pose pose : living.getAvailablePoses()) {
				AxisAlignedBB axisalignedbb = living.getPoseAABB(pose);

				for (int[] aint1 : aint) {
					blockpos$mutable.setPos(blockpos.getX() + aint1[0], blockpos.getY(), blockpos.getZ() + aint1[1]);
					double d0 = this.world.func_234936_m_(blockpos$mutable);
					if (TransportationHelper.func_234630_a_(d0)) {
						Vector3d vector3d = Vector3d.copyCenteredWithVerticalOffset(blockpos$mutable, d0);
						if (TransportationHelper.func_234631_a_(this.world, living, axisalignedbb.offset(vector3d))) {
							living.setPose(pose);
							return vector3d;
						}
					}
				}
			}

			return super.func_230268_c_(living);
		}

		@Nonnull
		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		public BlockPos getExitPos() {
			return dataManager.get(EXIT_POS);
		}

		public void setExit(BlockPos pos) {
			dataManager.set(EXIT_POS, pos);
		}

	}

}
