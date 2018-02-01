/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 17, 2015, 6:36:43 PM (GMT)]
 */
package vazkii.botania.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.block.tile.mana.TilePump;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import java.awt.Color;

public class EntityPoolMinecart extends EntityMinecart {

	private static final int TRANSFER_RATE = 10000;
	private static final String TAG_MANA = "mana";
	private static final DataParameter<Integer> MANA = EntityDataManager.createKey(EntityPoolMinecart.class, DataSerializers.VARINT);

	public EntityPoolMinecart(World world) {
		super(world);
	}

	public EntityPoolMinecart(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(MANA, 0);
	}

	@Nonnull
	@Override
	public IBlockState getDisplayTile() {
		return ModBlocks.pool.getDefaultState();
	}

	@Nonnull
	@Override
	public ItemStack getCartItem() {
		return new ItemStack(ModItems.poolMinecart);
	}

	@Nonnull
	@Override
	public EntityMinecart.Type getType() {
		return Type.RIDEABLE;
	}

	@Override
	public boolean canBeRidden() {
		return false;
	}

	@Override
	protected void applyDrag() {
		float f = 0.98F;

		this.motionX *= (double)f;
		this.motionY *= 0.0D;
		this.motionZ *= (double)f;
	}


	@Nonnull
	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(ModItems.poolMinecart);
	}

	@Override
	public void killMinecart(DamageSource source) {
		super.killMinecart(source);
		dropItemWithOffset(Item.getItemFromBlock(ModBlocks.pool), 1, 0.0F);
	}

	@Override
	public int getDefaultDisplayTileOffset() {
		return 8;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(world.isRemote) {
			double particleChance = 1F - (double) getMana() / (double) TilePool.MAX_MANA * 0.1;
			Color color = TilePool.PARTICLE_COLOR;
			double x = MathHelper.floor(posX);
			double y = MathHelper.floor(posY);
			double z = MathHelper.floor(posZ);
			if(Math.random() > particleChance)
				Botania.proxy.wispFX(x + 0.3 + Math.random() * 0.5, y + 0.85 + Math.random() * 0.25, z + Math.random(), color.getRed(), color.getGreen() / 255F, color.getBlue() / 255F, (float) Math.random() / 3F, (float) -Math.random() / 25F, 2F);
		}
	}

	@Override
	public void moveMinecartOnRail(BlockPos pos) {
		super.moveMinecartOnRail(pos);

		for(EnumFacing dir : EnumFacing.HORIZONTALS) {
			BlockPos posP = pos.offset(dir);
			Block block = world.getBlockState(posP).getBlock();
			if(block == ModBlocks.pump) {
				BlockPos posP_ = posP.offset(dir);
				TileEntity tile = world.getTileEntity(posP_);
				TileEntity tile_ = world.getTileEntity(posP);
				TilePump pump = (TilePump) tile_;

				if(tile != null && tile instanceof IManaPool) {
					IManaPool pool = (IManaPool) tile;
					EnumFacing pumpDir = world.getBlockState(posP).getValue(BotaniaStateProps.CARDINALS);
					boolean did = false;
					boolean can = false;

					if(pumpDir == dir) { // Pool -> Cart
						can = true;

						if(!pump.hasRedstone) {
							int cartMana = getMana();
							int poolMana = pool.getCurrentMana();
							int transfer = Math.min(TRANSFER_RATE, poolMana);
							int actualTransfer = Math.min(TilePool.MAX_MANA - cartMana, transfer);
							if(actualTransfer > 0) {
								pool.recieveMana(-transfer);
								setMana(cartMana + actualTransfer);
								did = true;
							}
						}
					} else if(pumpDir == dir.getOpposite()) { // Cart -> Pool
						can = true;

						if(!pump.hasRedstone && !pool.isFull()) {
							int cartMana = getMana();
							int transfer = Math.min(TRANSFER_RATE, cartMana);
							if(transfer > 0) {
								pool.recieveMana(transfer);
								setMana(cartMana - transfer);
								did = true;
							}
						}
					}

					if(did) {
						VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, posP_);
						pump.hasCart = true;
						if(!pump.active)
							pump.setActive(true);
					}

					if(can) {
						pump.hasCartOnTop = true;
						pump.comparator = (int) ((double) getMana() / (double) TilePool.MAX_MANA * 15); // different from TilePool.calculateComparatorLevel, kept for compatibility
					}

				}
			}
		}
	}

	@Override
	protected void writeEntityToNBT(@Nonnull NBTTagCompound cmp) {
		super.writeEntityToNBT(cmp);
		cmp.setInteger(TAG_MANA, getMana());
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound cmp) {
		super.readEntityFromNBT(cmp);
		setMana(cmp.getInteger(TAG_MANA));
	}

	@Override
	public int getComparatorLevel() {
		return TilePool.calculateComparatorLevel(getMana(), TilePool.MAX_MANA);
	}

	public int getMana() {
		return dataManager.get(MANA);
	}

	public void setMana(int mana) {
		dataManager.set(MANA, mana);
	}

}
