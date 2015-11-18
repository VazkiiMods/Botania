package thaumcraft.api.research;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.aspects.AspectList;

public class ScanAspect implements IScanThing {
	
	String research;	
	Aspect aspect;
	
	/**
	 * NOTE: You should not have to add your own entry for aspects since a trigger research is added automatically for each aspect in the format "![tagname]"
	 * for example: "!vitium"	  
	 */
	

	public ScanAspect(String research, Aspect aspect) {
		this.research = research;
		this.aspect = aspect;
	}

	@Override
	public boolean checkThing(EntityPlayer player, Object obj) {	
		if (obj == null) return false;
		
		AspectList al = null;		
		
		if (obj instanceof Entity && !(obj instanceof EntityItem)) {
			al = AspectHelper.getEntityAspects((Entity) obj);
		} else {
			ItemStack is = null;		
			if (obj instanceof ItemStack) 
				is = (ItemStack) obj;
			if (obj instanceof EntityItem && ((EntityItem)obj).getEntityItem()!=null) 
				is = ((EntityItem)obj).getEntityItem();
			if (obj instanceof BlockPos) {
				Block b = player.worldObj.getBlockState((BlockPos) obj).getBlock();
				is = new ItemStack(b,1,b.getMetaFromState(player.worldObj.getBlockState((BlockPos) obj)));
			}
			
			if (is!=null) {
				al = AspectHelper.getObjectAspects(is);			
			} 
		}
		
		return al!=null && al.getAmount(aspect)>0;
	}
	
	@Override
	public String getResearchKey() {
		return research;
	}
	
}
