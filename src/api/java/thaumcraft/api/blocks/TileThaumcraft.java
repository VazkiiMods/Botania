package thaumcraft.api.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * 
 * @author azanor
 *
 * Custom tile entity class I use for most of my tile entities. Setup in such a way that only 
 * the nbt data within readCustomNBT / writeCustomNBT will be sent to the client when the tile
 * updates. Apart from all the normal TE data that gets sent that is.
 * 
 */
public class TileThaumcraft extends TileEntity {
		
	//NBT stuff
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        readCustomNBT(nbt);
    }
	
	public void readCustomNBT(NBTTagCompound nbt)
    {
        //TODO
    }

	@Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        writeCustomNBT(nbt);
    }
	
	public void writeCustomNBT(NBTTagCompound nbt)
    {
		//TODO
    }
	
	//Client Packet stuff
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
        this.writeCustomNBT(nbt);
        return new S35PacketUpdateTileEntity(this.getPos(), -999, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);		
		this.readCustomNBT(pkt.getNbtCompound());
	}
	
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
	
	public EnumFacing getFacing() {
		try {
			return EnumFacing.getFront(getBlockMetadata() & 7);
		} catch (Exception e) {	}
		return EnumFacing.UP;
	}
	
	public boolean gettingPower() {
		return worldObj.isBlockPowered(getPos());
	}

}
