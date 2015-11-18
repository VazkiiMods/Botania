package thaumcraft.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.aspects.IEssentiaTransport;

public class ThaumcraftApiHelper {
	
	public static boolean areItemsEqual(ItemStack s1,ItemStack s2)
    {
		if (s1.isItemStackDamageable() && s2.isItemStackDamageable())
		{
			return s1.getItem() == s2.getItem();
		} else
			return s1.getItem() == s2.getItem() && s1.getItemDamage() == s2.getItemDamage();
    }
	
	/**
	 * Notifies thaumcraft that something with regards to runic shielding has changed and that it should
	 * rescan worn items to determine what the max shielding value is. 
	 * It automatically rescans once every 5 seconds, but this makes it happen sooner.
	 * @param entity
	 */
	public static void markRunicDirty(Entity entity) {
		ThaumcraftApi.internalMethods.markRunicDirty(entity);
	}
	
	public static boolean containsMatch(boolean strict, ItemStack[] inputs, List<ItemStack> targets)
    {
        for (ItemStack input : inputs)
        {
            for (ItemStack target : targets)
            {
                if (OreDictionary.itemMatches(target, input, strict))
                {
                    return true;
                }
            }
        }
        return false;
    }
	
	public static boolean areItemStacksEqualForCrafting(ItemStack stack0, Object in)
    {
		if (stack0==null && in!=null) return false;
		if (stack0!=null && in==null) return false;
		if (stack0==null && in==null) return true;
		
		if (in instanceof String) {
			List<ItemStack> l = OreDictionary.getOres((String) in);
			return containsMatch(false, new ItemStack[]{stack0}, l);
		}
		
		if (in instanceof ItemStack) {
			//nbt
			boolean t1=areItemStackTagsEqualForCrafting(stack0, (ItemStack) in);		
			if (!t1) return false;	
	        return OreDictionary.itemMatches((ItemStack) in, stack0, false);
		}
		
		return false;
    }
	
	public static boolean areItemStackTagsEqualForCrafting(ItemStack slotItem,ItemStack recipeItem)
    {
    	if (recipeItem == null || slotItem == null) return false;
    	if (recipeItem.getTagCompound()!=null && slotItem.getTagCompound()==null ) return false;
    	if (recipeItem.getTagCompound()==null ) return true;
    	
    	Iterator iterator = recipeItem.getTagCompound().getKeySet().iterator();
        while (iterator.hasNext())
        {
            String s = (String)iterator.next();
            if (slotItem.getTagCompound().hasKey(s)) {
            	if (!slotItem.getTagCompound().getTag(s).toString().equals(
            			recipeItem.getTagCompound().getTag(s).toString())) {
            		return false;
            	}
            } else {
        		return false;
            }
            
        }
        return true;
    }
   
    
    public static TileEntity getConnectableTile(World world, BlockPos pos, EnumFacing face) {
		TileEntity te = world.getTileEntity(pos.offset(face));
		if (te instanceof IEssentiaTransport && ((IEssentiaTransport)te).isConnectable(face.getOpposite())) 
			return te;
		else
			return null;
	}
    
    public static TileEntity getConnectableTile(IBlockAccess world, BlockPos pos, EnumFacing face) {
		TileEntity te = world.getTileEntity(pos.offset(face));
		if (te instanceof IEssentiaTransport && ((IEssentiaTransport)te).isConnectable(face.getOpposite())) 
			return te;
		else
			return null;
	}
    
//    private static HashMap<Integer, AspectList> allAspects= new HashMap<Integer, AspectList>();
//    private static HashMap<Integer, AspectList> allCompoundAspects= new HashMap<Integer, AspectList>();
//    
//    public static AspectList getAllAspects(int amount) {
//    	if (allAspects.get(amount)==null) {
//    		AspectList al = new AspectList();
//    		for (Aspect aspect:Aspect.aspects.values()) {
//    			al.add(aspect, amount);
//    		}
//    		allAspects.put(amount, al);
//    	} 
//    	return allAspects.get(amount);
//    }
//    
//    public static AspectList getAllCompoundAspects(int amount) {
//    	if (allCompoundAspects.get(amount)==null) {
//    		AspectList al = new AspectList();
//    		for (Aspect aspect:Aspect.getCompoundAspects()) {
//    			al.add(aspect, amount);
//    		}
//    		allCompoundAspects.put(amount, al);
//    	} 
//    	return allCompoundAspects.get(amount);
//    }
    
    
	public static MovingObjectPosition rayTraceIgnoringSource(World world, Vec3 v1, Vec3 v2, 
			boolean bool1, boolean bool2, boolean bool3)
	{
	    if (!Double.isNaN(v1.xCoord) && !Double.isNaN(v1.yCoord) && !Double.isNaN(v1.zCoord))
	    {
	        if (!Double.isNaN(v2.xCoord) && !Double.isNaN(v2.yCoord) && !Double.isNaN(v2.zCoord))
	        {
	            int i = MathHelper.floor_double(v2.xCoord);
	            int j = MathHelper.floor_double(v2.yCoord);
	            int k = MathHelper.floor_double(v2.zCoord);
	            int l = MathHelper.floor_double(v1.xCoord);
	            int i1 = MathHelper.floor_double(v1.yCoord);
	            int j1 = MathHelper.floor_double(v1.zCoord);
	            IBlockState block = world.getBlockState(new BlockPos(l, i1, j1));
	
	            MovingObjectPosition movingobjectposition2 = null;
	            int k1 = 200;
	
	            while (k1-- >= 0)
	            {
	                if (Double.isNaN(v1.xCoord) || Double.isNaN(v1.yCoord) || Double.isNaN(v1.zCoord))
	                {
	                    return null;
	                }
	
	                if (l == i && i1 == j && j1 == k)
	                {
	                    continue;
	                }
	
	                boolean flag6 = true;
	                boolean flag3 = true;
	                boolean flag4 = true;
	                double d0 = 999.0D;
	                double d1 = 999.0D;
	                double d2 = 999.0D;
	
	                if (i > l)
	                {
	                    d0 = (double)l + 1.0D;
	                }
	                else if (i < l)
	                {
	                    d0 = (double)l + 0.0D;
	                }
	                else
	                {
	                    flag6 = false;
	                }
	
	                if (j > i1)
	                {
	                    d1 = (double)i1 + 1.0D;
	                }
	                else if (j < i1)
	                {
	                    d1 = (double)i1 + 0.0D;
	                }
	                else
	                {
	                    flag3 = false;
	                }
	
	                if (k > j1)
	                {
	                    d2 = (double)j1 + 1.0D;
	                }
	                else if (k < j1)
	                {
	                    d2 = (double)j1 + 0.0D;
	                }
	                else
	                {
	                    flag4 = false;
	                }
	
	                double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = v2.xCoord - v1.xCoord;
                    double d7 = v2.yCoord - v1.yCoord;
                    double d8 = v2.zCoord - v1.zCoord;

                    if (flag6)
                    {
                        d3 = (d0 - v1.xCoord) / d6;
                    }

                    if (flag3)
                    {
                        d4 = (d1 - v1.yCoord) / d7;
                    }

                    if (flag4)
                    {
                        d5 = (d2 - v1.zCoord) / d8;
                    }

                    if (d3 == -0.0D)
                    {
                        d3 = -1.0E-4D;
                    }

                    if (d4 == -0.0D)
                    {
                        d4 = -1.0E-4D;
                    }

                    if (d5 == -0.0D)
                    {
                        d5 = -1.0E-4D;
                    }
	
	                EnumFacing enumfacing;

                    if (d3 < d4 && d3 < d5)
                    {
                        enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
                        v1 = new Vec3(d0, v1.yCoord + d7 * d3, v1.zCoord + d8 * d3);
                    }
                    else if (d4 < d5)
                    {
                        enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                        v1 = new Vec3(v1.xCoord + d6 * d4, d1, v1.zCoord + d8 * d4);
                    }
                    else
                    {
                        enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        v1 = new Vec3(v1.xCoord + d6 * d5, v1.yCoord + d7 * d5, d2);
                    }

                    l = MathHelper.floor_double(v1.xCoord) - (enumfacing == EnumFacing.EAST ? 1 : 0);
                    i1 = MathHelper.floor_double(v1.yCoord) - (enumfacing == EnumFacing.UP ? 1 : 0);
                    j1 = MathHelper.floor_double(v1.zCoord) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
	
	                IBlockState block1 = world.getBlockState(new BlockPos(l, i1, j1));
	
	                if (!bool2 || block1.getBlock().getCollisionBoundingBox(world, new BlockPos(l, i1, j1), block1) != null)
	                {
	                    if (block1.getBlock().canCollideCheck(block1, bool1))
	                    {
	                        MovingObjectPosition movingobjectposition1 = block1.getBlock().collisionRayTrace(world, new BlockPos(l, i1, j1), v1, v2);
	
	                        if (movingobjectposition1 != null)
	                        {
	                            return movingobjectposition1;
	                        }
	                    }
	                    else
	                    {
	                        movingobjectposition2 = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, v1, enumfacing, new BlockPos(l, i1, j1));
	                    }
	                }
	            }
	
	            return bool3 ? movingobjectposition2 : null;
	        }
	        else
	        {
	            return null;
	        }
	    }
	    else
	    {
	        return null;
	    }
	}
	
	public static Object getNBTDataFromId(NBTTagCompound nbt, byte id, String key) {
		switch (id) {
		case 1: return nbt.getByte(key);
		case 2: return nbt.getShort(key);
		case 3: return nbt.getInteger(key);
		case 4: return nbt.getLong(key);
		case 5: return nbt.getFloat(key);
		case 6: return nbt.getDouble(key);
		case 7: return nbt.getByteArray(key);
		case 8: return nbt.getString(key);
		case 9: return nbt.getTagList(key, (byte) 10);
		case 10: return nbt.getTag(key);
		case 11: return nbt.getIntArray(key);
		default: return null;
		}
	}
}
