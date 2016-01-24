/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 21, 2014, 9:18:28 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;

public class TileMod extends TileEntity implements ITickable {

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeToNBT(par1nbtTagCompound);

		writeCustomNBT(par1nbtTagCompound);
	}

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
		super.readFromNBT(par1nbtTagCompound);

		readCustomNBT(par1nbtTagCompound);
	}

	public void writeCustomNBT(NBTTagCompound cmp) {
		// NO-OP
	}

	public void readCustomNBT(NBTTagCompound cmp) {
		// NO-OP
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		writeCustomNBT(nbttagcompound);
		return new S35PacketUpdateTileEntity(pos, -999, nbttagcompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		readCustomNBT(packet.getNbtCompound());
	}

	@Override
	public final void update() {
		if (!isInvalid() && worldObj.isBlockLoaded(getPos(), false)) {
			if (!worldObj.isRemote)
				updateEntity();
			else {
				try {
					updateEntity();
				} catch (Exception e) { // todo 1.8 remove this abhorration after I figure out what's going on.
					e.printStackTrace();
					FMLLog.severe("[Botania]: CLIENT TICK FAILED");
					FMLLog.severe("[Botania]: World: %s, Pos: %s, TE: %s", worldObj, pos, this);
					if (this instanceof TileSpecialFlower) {
						TileSpecialFlower spec = ((TileSpecialFlower) this);
						FMLLog.severe("[Botania]: SUBTILE: %s", spec.getSubTile().getUnlocalizedName());
					}
				}
			}
		}
	}

	protected void updateEntity() {
		// NO-OP
	}

}
