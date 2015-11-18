package thaumcraft.api.internal;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

public class WorldCoordinates implements Comparable
{
    public BlockPos pos;
    
    public int dim;

    public WorldCoordinates() {}

    public WorldCoordinates(BlockPos pos, int d)
    {
        this.pos = pos;
        this.dim = d;
    }
    
    public WorldCoordinates(TileEntity tile)
    {
        this.pos = tile.getPos();
        this.dim = tile.getWorld().provider.getDimensionId();
    }

    public WorldCoordinates(WorldCoordinates par1ChunkCoordinates)
    {
        this.pos = par1ChunkCoordinates.pos;
        this.dim = par1ChunkCoordinates.dim;
    }

    public boolean equals(Object par1Obj)
    {
        if (!(par1Obj instanceof WorldCoordinates))
        {
            return false;
        }
        else
        {
        	WorldCoordinates coordinates = (WorldCoordinates)par1Obj;
            return this.pos.equals(coordinates.pos) && this.dim == coordinates.dim ;
        }
    }

    public int hashCode()
    {
        return this.pos.getX() + this.pos.getY() << 8 + this.pos.getZ() << 16 + this.dim << 24;
    }

    /**
     * Compare the coordinate with another coordinate
     */
    public int compareWorldCoordinate(WorldCoordinates par1)
    {
        return this.dim == par1.dim ? this.pos.compareTo(par1.pos) : -1;
    }

    public void set(BlockPos pos, int d)
    {
        this.pos = pos;
        this.dim = d;
    }

    /**
     * Returns the squared distance between this coordinates and the coordinates given as argument.
     */
    public double getDistanceSquared(BlockPos pos)
    {
        return this.pos.distanceSq(pos);
    }

    /**
     * Return the squared distance between this coordinates and the ChunkCoordinates given as argument.
     */
    public double getDistanceSquaredToWorldCoordinates(WorldCoordinates par1ChunkCoordinates)
    {
        return this.getDistanceSquared(par1ChunkCoordinates.pos);
    }

    public int compareTo(Object par1Obj)
    {
        return this.compareWorldCoordinate((WorldCoordinates)par1Obj);
    }
    
    public void readNBT(NBTTagCompound nbt) {
    	int x = nbt.getInteger("w_x");
    	int y = nbt.getInteger("w_y");
    	int z = nbt.getInteger("w_z");
    	this.pos = new BlockPos(x,y,z);
    	this.dim = nbt.getInteger("w_d");
    }
    
    public void writeNBT(NBTTagCompound nbt) {
    	nbt.setInteger("w_x",pos.getX());
    	nbt.setInteger("w_y",pos.getY());
    	nbt.setInteger("w_z",pos.getZ());
    	nbt.setInteger("w_d",dim);
    }
    
}
