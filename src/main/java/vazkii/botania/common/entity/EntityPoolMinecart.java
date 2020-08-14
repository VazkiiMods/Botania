/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.fabricmc.fabric.api.entity.EntityPickInteractionAware;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.block.tile.mana.TilePump;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.network.PacketSpawnEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityPoolMinecart extends AbstractMinecartEntity implements EntityPickInteractionAware {
	private static final int TRANSFER_RATE = 10000;
	private static final String TAG_MANA = "mana";
	private static final TrackedData<Integer> MANA = DataTracker.registerData(EntityPoolMinecart.class, TrackedDataHandlerRegistry.INTEGER);

	public EntityPoolMinecart(EntityType<EntityPoolMinecart> type, World world) {
		super(type, world);
	}

	public EntityPoolMinecart(World world, double x, double y, double z) {
		super(ModEntities.POOL_MINECART, world, x, y, z);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		dataTracker.startTracking(MANA, 0);
	}

	@Nonnull
	@Override
	public Packet<?> createSpawnPacket() {
		return PacketSpawnEntity.make(this);
	}

	@Nonnull
	@Override
	public BlockState getContainedBlock() {
		return ModBlocks.manaPool.getDefaultState();
	}

	@Nonnull
	@Override
	public AbstractMinecartEntity.Type getMinecartType() {
		return Type.RIDEABLE;
	}

	@Override
	protected boolean canAddPassenger(Entity passenger) {
		return false;
	}

	@Override
	protected void applySlowdown() {
		float f = 0.98F;
		this.setVelocity(getVelocity().multiply(f, 0, f));
	}

	@Nonnull
	@Override
	public ItemStack getPickedStack(@Nullable PlayerEntity player, HitResult target) {
		return new ItemStack(ModItems.poolMinecart);
	}

	@Override
	public void dropItems(DamageSource source) {
		super.dropItems(source);
		dropItem(ModBlocks.manaPool, 0);
	}

	@Override
	public int getDefaultBlockOffset() {
		return 8;
	}

	@Override
	public void tick() {
		super.tick();

		if (world.isClient) {
			double particleChance = 1F - (double) getMana() / (double) TilePool.MAX_MANA * 0.1;
			int color = TilePool.PARTICLE_COLOR;
			float red = (color >> 16 & 0xFF) / 255F;
			float green = (color >> 8 & 0xFF) / 255F;
			float blue = (color & 0xFF) / 255F;
			double x = MathHelper.floor(getX());
			double y = MathHelper.floor(getY());
			double z = MathHelper.floor(getZ());
			if (Math.random() > particleChance) {
				WispParticleData data = WispParticleData.wisp((float) Math.random() / 3F, red, green, blue, 2F);
				world.addParticle(data, x + 0.3 + Math.random() * 0.5, y + 0.85 + Math.random() * 0.25, z + Math.random(), 0, (float) Math.random() / 25F, 0);
			}
		}
	}

	@Override
	public void moveOnRail(BlockPos pos, BlockState state) {
		super.moveOnRail(pos, state);

		for (Direction dir : Direction.Type.HORIZONTAL) {
			BlockPos posP = pos.offset(dir);
			Block block = world.getBlockState(posP).getBlock();
			if (block == ModBlocks.pump) {
				BlockPos posP_ = posP.offset(dir);
				BlockEntity tile = world.getBlockEntity(posP_);
				BlockEntity tile_ = world.getBlockEntity(posP);
				TilePump pump = (TilePump) tile_;

				if (tile instanceof IManaPool) {
					IManaPool pool = (IManaPool) tile;
					Direction pumpDir = world.getBlockState(posP).get(Properties.HORIZONTAL_FACING);
					boolean did = false;
					boolean can = false;

					if (pumpDir == dir) { // Pool -> Cart
						can = true;

						if (!pump.hasRedstone) {
							int cartMana = getMana();
							int poolMana = pool.getCurrentMana();
							int transfer = Math.min(TRANSFER_RATE, poolMana);
							int actualTransfer = Math.min(TilePool.MAX_MANA - cartMana, transfer);
							if (actualTransfer > 0) {
								pool.receiveMana(-transfer);
								setMana(cartMana + actualTransfer);
								did = true;
							}
						}
					} else if (pumpDir == dir.getOpposite()) { // Cart -> Pool
						can = true;

						if (!pump.hasRedstone && !pool.isFull()) {
							int cartMana = getMana();
							int transfer = Math.min(TRANSFER_RATE, cartMana);
							if (transfer > 0) {
								pool.receiveMana(transfer);
								setMana(cartMana - transfer);
								did = true;
							}
						}
					}

					if (did) {
						pump.hasCart = true;
						pump.setActive(true);
					}

					if (can) {
						pump.hasCartOnTop = true;
						pump.comparator = (int) ((double) getMana() / (double) TilePool.MAX_MANA * 15); // different from TilePool.calculateComparatorLevel, kept for compatibility
					}

				}
			}
		}
	}

	@Override
	protected void writeCustomDataToTag(@Nonnull CompoundTag cmp) {
		super.writeCustomDataToTag(cmp);
		cmp.putInt(TAG_MANA, getMana());
	}

	@Override
	protected void readCustomDataFromTag(CompoundTag cmp) {
		super.readCustomDataFromTag(cmp);
		setMana(cmp.getInt(TAG_MANA));
	}

	@Override
	public int getComparatorLevel() {
		return TilePool.calculateComparatorLevel(getMana(), TilePool.MAX_MANA);
	}

	public int getMana() {
		return dataTracker.get(MANA);
	}

	public void setMana(int mana) {
		dataTracker.set(MANA, mana);
	}

}
