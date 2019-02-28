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
import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.subtile.functional.SubTileExoflame;
import vazkii.botania.common.block.tile.TileMod;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

public class TileBellows extends TileMod implements ITickable {
	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.BELLOWS)
	public static TileEntityType<TileBellows> TYPE;
	private static final String TAG_ACTIVE = "active";

	public float movePos;
	public boolean active = false;
	public float moving = 0F;

	public TileBellows() {
		super(TYPE);
	}

	public void interact() {
		if(moving == 0F)
			setActive(true);
	}

	@Override
	public void tick() {
		boolean disable = true;
		TileEntity tile = getLinkedTile();
		if(!active && tile instanceof TilePool) {
			TilePool pool = (TilePool) tile;
			boolean transfer = pool.isDoingTransfer;
			if(transfer) {
				if(pool.ticksDoingTransfer >= getBlockMetadata() * 2 - 2)
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

				if(furnace.hasWorld() && furnace.getBlockState().get(BlockFurnace.LIT)) {
					// [VanillaCopy] BlockFurnace
					double d0 = (double)pos.getX() + 0.5D;
					double d1 = (double)pos.getY();
					double d2 = (double)pos.getZ() + 0.5D;

					EnumFacing enumfacing = furnace.getBlockState().get(BlockFurnace.FACING);
					EnumFacing.Axis enumfacing$axis = enumfacing.getAxis();
					double d3 = 0.52D;
					double d4 = world.rand.nextDouble() * 0.6D - 0.3D;
					double d5 = enumfacing$axis == EnumFacing.Axis.X ? (double)enumfacing.getXOffset() * 0.52D : d4;
					double d6 = world.rand.nextDouble() * 6.0D / 16.0D;
					double d7 = enumfacing$axis == EnumFacing.Axis.Z ? (double)enumfacing.getZOffset() * 0.52D : d4;
					world.addParticle(Particles.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
					world.addParticle(Particles.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
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
		EnumFacing side = world.getBlockState(getPos()).get(BotaniaStateProps.CARDINALS);
		return world.getTileEntity(getPos().offset(side));
	}

	@Override
	public void writePacketNBT(NBTTagCompound cmp) {
		cmp.putBoolean(TAG_ACTIVE, active);
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
