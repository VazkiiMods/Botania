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
import net.minecraft.world.World;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.block.tile.mana.TilePump;
import vazkii.botania.common.item.ModItems;

public class EntityPoolMinecart extends EntityMinecart {

	private static final int TRANSFER_RATE = 10000;
	private static final String TAG_MANA = "mana";
	private static final DataParameter<Integer> MANA = EntityDataManager.createKey(EntityPoolMinecart.class, DataSerializers.VARINT);

	public EntityPoolMinecart(World p_i1712_1_) {
		super(p_i1712_1_);
	}

	public EntityPoolMinecart(World p_i1715_1_, double p_i1715_2_, double p_i1715_4_, double p_i1715_6_) {
		super(p_i1715_1_, p_i1715_2_, p_i1715_4_, p_i1715_6_);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(MANA, 0);
	}

	@Override
	public IBlockState getDisplayTile() {
		return ModBlocks.pool.getDefaultState();
	}

	@Override
	public ItemStack getCartItem() {
		return new ItemStack(ModItems.poolMinecart);
	}

	@Override
	public EntityMinecart.Type getType() {
		return Type.RIDEABLE;
	}

	@Override
	public boolean canBeRidden() {
		return false;
	}

	@Override
	public void killMinecart(DamageSource p_94095_1_) {
		super.killMinecart(p_94095_1_);
		dropItemWithOffset(Item.getItemFromBlock(ModBlocks.pool), 1, 0.0F);
	}

	@Override
	public int getDefaultDisplayTileOffset() {
		return 8;
	}

	@Override
	public void moveMinecartOnRail(BlockPos pos) {
		super.moveMinecartOnRail(pos);

		for(EnumFacing dir : EnumFacing.HORIZONTALS) {
			BlockPos posP = pos.offset(dir);
			Block block = worldObj.getBlockState(posP).getBlock();
			if(block == ModBlocks.pump) {
				BlockPos posP_ = posP.offset(dir);
				TileEntity tile = worldObj.getTileEntity(posP_);
				TileEntity tile_ = worldObj.getTileEntity(posP);
				TilePump pump = (TilePump) tile_;

				if(tile != null && tile instanceof IManaPool && !pump.hasRedstone) {
					IManaPool pool = (IManaPool) tile;
					EnumFacing pumpDir = worldObj.getBlockState(posP).getValue(BotaniaStateProps.CARDINALS);
					boolean did = false;
					boolean can = false;

					if(pumpDir == dir) { // Pool -> Cart
						can = true;
						int cartMana = getMana();
						int poolMana = pool.getCurrentMana();
						int transfer = Math.min(TRANSFER_RATE, poolMana);
						int actualTransfer = Math.min(TilePool.MAX_MANA - cartMana, transfer);
						if(actualTransfer > 0) {
							pool.recieveMana(-transfer);
							setMana(cartMana + actualTransfer);
							did = true;
						}
					} else if(pumpDir == dir.getOpposite()) { // Cart -> Pool
						can = true;
						if(!pool.isFull()) {
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
						VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, posP_);
						pump.hasCart = true;
						if(!pump.active)
							pump.setActive(true);
					}

					if(can) {
						pump.hasCartOnTop = true;
						pump.comparator = (int) ((double) getMana() / (double) TilePool.MAX_MANA * 15);
					}

				}
			}
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setInteger(TAG_MANA, getMana());
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		setMana(p_70037_1_.getInteger(TAG_MANA));
	}

	public int getMana() {
		return dataManager.get(MANA);
	}

	public void setMana(int mana) {
		dataManager.set(MANA, mana);
	}

}
