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

import net.minecraft.block.BlockFurnace;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.subtile.functional.SubTileExoflame;
import vazkii.botania.common.block.tile.TileMod;
import vazkii.botania.common.core.handler.ModSounds;

public class TileBellows extends TileMod implements ITickable {

	private static final String TAG_ACTIVE = "active";

	public float movePos;
	public boolean active = false;
	public float moving = 0F;

	public void interact() {
		if(moving == 0F)
			setActive(true);
	}

	@Override
	public void update() {
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
				world.playSound(null, pos, ModSounds.bellows, SoundCategory.BLOCKS, 0.1F, 3F);

			if(tile instanceof TileEntityFurnace) {
				TileEntityFurnace furnace = (TileEntityFurnace) tile;
				if(SubTileExoflame.canFurnaceSmelt(furnace)) {
					furnace.setField(2, Math.min(199, furnace.getField(2) + 20)); // cookTime
					furnace.setField(0, Math.max(0, furnace.getField(0) - 10)); // burnTime
				}

				if(furnace.hasWorld() && furnace.getBlockType() == Blocks.LIT_FURNACE) {
					// Copypasta from BlockFurnace
					EnumFacing enumfacing = world.getBlockState(furnace.getPos()).getValue(BlockFurnace.FACING);
					double d0 = pos.getX() + 0.5D;
					double d1 = pos.getY() + world.rand.nextDouble() * 6.0D / 16.0D;
					double d2 = pos.getZ() + 0.5D;
					double d3 = 0.52D;
					double d4 = world.rand.nextDouble() * 0.6D - 0.3D;

					switch (enumfacing)
					{
					case WEST:
						world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
						world.spawnParticle(EnumParticleTypes.FLAME, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
						break;
					case EAST:
						world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
						world.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
						break;
					case NORTH:
						world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D);
						world.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D);
						break;
					case SOUTH:
						world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
						world.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
					default: break;
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

	}

	public TileEntity getLinkedTile() {
		EnumFacing side = world.getBlockState(getPos()).getValue(BotaniaStateProps.CARDINALS);
		return world.getTileEntity(getPos().offset(side));
	}

	@Override
	public void writePacketNBT(NBTTagCompound cmp) {
		cmp.setBoolean(TAG_ACTIVE, active);
	}

	@Override
	public void readPacketNBT(NBTTagCompound cmp) {
		active = cmp.getBoolean(TAG_ACTIVE);
	}

	public void setActive(boolean active) {
		if(!world.isRemote) {
			boolean diff = this.active != active;
			this.active = active;
			if(diff)
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);
		}
	}


}
