/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 28, 2015, 5:27:43 PM (GMT)]
 */
package vazkii.botania.common.block.tile.mana;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.subtile.functional.SubTileExoflame;
import vazkii.botania.common.block.tile.TileMod;

public class TileBellows extends TileMod {

	private static final String TAG_ACTIVE = "active";

	public float movePos;
	public boolean active = false;
	public float moving = 0F;

	public void interact() {
		if(moving == 0F)
			setActive(true);
	}

	@Override
	public void updateEntity() {
		boolean disable = true;
		TileEntity tile = getLinkedTile();
		if(!active && tile instanceof TilePool) {
			TilePool pool = (TilePool) tile;
			boolean transfer = pool.isDoingTransfer;
			if(transfer) {
				if(!active && pool.ticksDoingTransfer >= getBlockMetadata() * 2 - 2)
					setActive(true);
				disable = false;
			}
		}

		float max = 0.9F;
		float min = 0F;

		float incr = max / 20F;

		if(movePos < max && active && moving >= 0F) {
			if(moving == 0F)
				worldObj.playSoundEffect(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, "botania:bellows", 0.1F, 3F);

			if(tile instanceof TileEntityFurnace) {
				TileEntityFurnace furnace = (TileEntityFurnace) tile;
				if(SubTileExoflame.canFurnaceSmelt(furnace)) {
					furnace.furnaceCookTime = Math.min(199, furnace.furnaceCookTime + 20);
					furnace.furnaceBurnTime = Math.max(0, furnace.furnaceBurnTime - 10);
				}

				if(furnace.getBlockType() == Blocks.lit_furnace) {
					// Copypasta from BlockFurnace
					int x = furnace.xCoord;
					int y = furnace.yCoord;
					int z = furnace.zCoord;
					int l = worldObj.getBlockMetadata(x, y, z);
					float f = x + 0.5F;
					float f1 = y + 0.0F + worldObj.rand.nextFloat() * 6.0F / 16.0F;
					float f2 = z + 0.5F;
					float f3 = 0.52F;
					float f4 = worldObj.rand.nextFloat() * 0.6F - 0.3F;

					if(l == 4) {
						worldObj.spawnParticle("smoke", f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
						worldObj.spawnParticle("flame", f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
					} else if(l == 5) {
						worldObj.spawnParticle("smoke", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
						worldObj.spawnParticle("flame", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
					} else if(l == 2) {
						worldObj.spawnParticle("smoke", f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
						worldObj.spawnParticle("flame", f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
					} else if(l == 3) {
						worldObj.spawnParticle("smoke", f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
						worldObj.spawnParticle("flame", f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
					}
				}
			}

			movePos += incr * 3;
			moving = incr * 3;
			if(movePos >= max) {
				movePos = Math.min(max, movePos);
				moving = 0F;
				if(disable)
					setActive(false);
			}
		} else if(movePos > min) {
			movePos -= incr;
			moving = -incr;
			if(movePos <= min) {
				movePos = Math.max(min, movePos);
				moving = 0F;
			}
		}

		super.updateEntity();
	}

	public TileEntity getLinkedTile() {
		int meta = getBlockMetadata();
		ForgeDirection dir = ForgeDirection.getOrientation(meta);
		return worldObj.getTileEntity(xCoord + dir.offsetX, yCoord, zCoord + dir.offsetZ);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setBoolean(TAG_ACTIVE, active);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		active = cmp.getBoolean(TAG_ACTIVE);
	}

	public void setActive(boolean active) {
		if(!worldObj.isRemote) {
			boolean diff = this.active != active;
			this.active = active;
			if(diff)
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, xCoord, yCoord, zCoord);
		}
	}


}
