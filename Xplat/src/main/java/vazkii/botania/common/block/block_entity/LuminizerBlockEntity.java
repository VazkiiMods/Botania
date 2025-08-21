/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.Bound;
import vazkii.botania.api.block.PhantomInkableBlock;
import vazkii.botania.api.block.WandBindable;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.LuminizerBlock;
import vazkii.botania.common.entity.LuminizerMoverEntity;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.helper.VecHelper;

import java.util.ArrayList;
import java.util.List;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class LuminizerBlockEntity extends BotaniaBlockEntity implements WandBindable, PhantomInkableBlock {
	public static final int MAX_DIST = 20;

	private static final String TAG_BIND_X = "bindX";
	private static final String TAG_BIND_Y = "bindY";
	private static final String TAG_BIND_Z = "bindZ";
	private static final String TAG_NO_PARTICLE = "noParticle";

	private BlockPos bindPos = Bound.UNBOUND_POS;
	private int ticksElapsed = 0;
	private boolean noParticle = false;

	public LuminizerBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.LIGHT_RELAY, pos, state);
	}

	public void mountEntity(Entity e) {
		BlockPos nextDest = getNextDestination();
		if (e.isPassenger() || level.isClientSide || nextDest == null || !isValidBinding()) {
			return;
		}

		LuminizerMoverEntity mover = new LuminizerMoverEntity(level, worldPosition, nextDest);
		level.addFreshEntity(mover);
		e.startRiding(mover);
		if (!(e instanceof ItemEntity)) {
			mover.playSound(BotaniaSounds.lightRelay, 1F, (float) Math.random() * 0.3F + 0.7F);
		}
		if (e instanceof ServerPlayer serverPlayer) {
			PlayerHelper.grantCriterion(serverPlayer, botaniaRL("main/luminizer_ride"), "code_triggered");
		}
	}

	public static void clientTick(Level level, BlockPos worldPosition, BlockState state, LuminizerBlockEntity self) {
		self.ticksElapsed++;

		BlockPos nextDest = self.getNextDestination();
		if (!self.isNoParticle() && nextDest != null && nextDest.getY() != Integer.MIN_VALUE && self.isValidBinding()) {
			Vec3 vec = self.getMovementVector();
			if (vec != null) {
				double dist = 0.1;
				int size = (int) (vec.length() / dist);
				int count = 10;
				int start = self.ticksElapsed % size;

				Vec3 vecMag = vec.normalize().scale(dist);
				Vec3 vecTip = vecMag.scale(start).add(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5);

				double radPer = Math.PI / 16.0;
				float mul = 0.5F;
				float mulPer = 0.4F;
				float maxMul = 2;
				WispParticleData data = WispParticleData.wisp(0.1F, 0.4F, 0.4F, 1F, 1);
				for (int i = start; i < start + count; i++) {
					mul = Math.min(maxMul, mul + mulPer);
					double rad = radPer * (i + self.ticksElapsed * 0.4);
					Vec3 intermediate = vecMag.cross(VecHelper.ONE).scale(mul);
					Vec3 vecRot = VecHelper.rotate(intermediate, rad, vecMag).add(vecTip);
					level.addParticle(data, vecRot.x, vecRot.y, vecRot.z, (float) -vecMag.x, (float) -vecMag.y, (float) -vecMag.z);
					vecTip = vecTip.add(vecMag);
				}
			}
		}
	}

	public static void serverTick(Level level, BlockPos worldPosition, BlockState state, LuminizerBlockEntity self) {
		self.ticksElapsed++;

		BlockPos nextDest = self.getNextDestination();
		if (nextDest != null && nextDest.getY() != Integer.MIN_VALUE && self.isValidBinding()) {
			BlockPos endpoint = self.getEndpoint();

			if (endpoint != null) {
				AABB aabb = state.getShape(level, worldPosition).bounds().move(worldPosition);
				float range = 0.6F;
				List<ThrownEnderpearl> enderPearls = level.getEntitiesOfClass(ThrownEnderpearl.class, aabb.inflate(range));
				for (ThrownEnderpearl pearl : enderPearls) {
					pearl.teleportTo(
							endpoint.getX() + pearl.getX() - worldPosition.getX(),
							endpoint.getY() + pearl.getY() - worldPosition.getY(),
							endpoint.getZ() + pearl.getZ() - worldPosition.getZ()
					);
				}
			}
		}
	}

	public boolean isValidBinding() {
		BlockPos nextDest = getNextDestination();
		if (nextDest == null) {
			return false;
		}

		Block block = level.getBlockState(nextDest).getBlock();
		return block instanceof LuminizerBlock;
	}

	@Nullable
	private BlockPos getEndpoint() {
		List<LuminizerBlockEntity> pointsPassed = new ArrayList<>();
		LuminizerBlockEntity relay = this;
		BlockPos lastCoords = null;

		while (true) {
			if (pointsPassed.contains(relay)) {
				return null; // Circular path
			}
			pointsPassed.add(relay);

			BlockPos coords = relay.getNextDestination();
			if (coords == null) {
				return lastCoords;
			}

			BlockEntity tile = level.getBlockEntity(coords);
			if (tile instanceof LuminizerBlockEntity tileRelay) {
				relay = tileRelay;
			} else {
				return lastCoords;
			}

			lastCoords = coords;
		}
	}

	public void setNoParticle() {
		noParticle = true;
		setChanged();
	}

	public boolean isNoParticle() {
		return noParticle;
	}

	@Nullable
	public Vec3 getMovementVector() {
		BlockPos dest = getNextDestination();
		if (dest == null) {
			return null;
		}

		return new Vec3(dest.getX() - worldPosition.getX(), dest.getY() - worldPosition.getY(), dest.getZ() - worldPosition.getZ());
	}

	@Override
	public BlockPos getBinding() {
		return bindPos;
	}

	@Nullable
	public BlockPos getNextDestination() {
		BlockState state = getBlockState();
		return state.getBlock() instanceof LuminizerBlock luminizer ? luminizer.getNextDestination(level, state, getBlockPos(), this) : null;
	}

	@Override
	public boolean canSelect(Player player, ItemStack wand, BlockPos pos, Direction side) {
		return true;
	}

	@Override
	public boolean bindTo(Player player, ItemStack wand, BlockPos pos, Direction side) {
		if (!(player.level().getBlockState(pos).getBlock() instanceof LuminizerBlock)
				|| pos.distSqr(getBlockPos()) > MAX_DIST * MAX_DIST) {
			return false;
		}

		bindPos = pos;
		setChanged();
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		return true;
	}

	@Override
	public boolean onPhantomInked(@Nullable Player player, ItemStack stack, Direction side) {
		if (isNoParticle()) {
			return false;
		}
		if (!level.isClientSide) {
			if (player == null || !player.getAbilities().instabuild) {
				stack.shrink(1);
			}
			setNoParticle();
			level.gameEvent(null, GameEvent.BLOCK_CHANGE, getBlockPos());
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
		return true;
	}

	@Override
	public void readPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		bindPos = new BlockPos(
				cmp.getInt(TAG_BIND_X),
				cmp.getInt(TAG_BIND_Y),
				cmp.getInt(TAG_BIND_Z)
		);
		noParticle = cmp.getBoolean(TAG_NO_PARTICLE);
	}

	@Override
	public void writePacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		cmp.putInt(TAG_BIND_X, bindPos.getX());
		cmp.putInt(TAG_BIND_Y, bindPos.getY());
		cmp.putInt(TAG_BIND_Z, bindPos.getZ());
		cmp.putBoolean(TAG_NO_PARTICLE, noParticle);
	}

}
