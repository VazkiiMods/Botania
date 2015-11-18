package thaumcraft.api.wands;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public interface IWand {

	public abstract int getMaxVis(ItemStack stack);

	public abstract int getVis(ItemStack is, Aspect aspect);

	public abstract AspectList getAllVis(ItemStack is);

	public abstract AspectList getAspectsWithRoom(ItemStack wandstack);
	
	public abstract float getConsumptionModifier(ItemStack is, EntityPlayer player, Aspect aspect, boolean crafting);

	public abstract boolean consumeVis(ItemStack is, EntityPlayer player, Aspect aspect, int amount, boolean crafting);

	public abstract boolean consumeAllVis(ItemStack is, EntityPlayer player, AspectList aspects, boolean doit, boolean crafting);

	public abstract int addVis(ItemStack is, Aspect aspect, int amount, boolean doit);

	public abstract ItemFocusBasic getFocus(ItemStack stack);

	public abstract ItemStack getFocusStack(ItemStack stack);

	public abstract void setFocus(ItemStack stack, ItemStack focus);

	public abstract WandRod getRod(ItemStack stack);

	public abstract boolean isStaff(ItemStack stack);

	public abstract boolean isSceptre(ItemStack stack);

	public abstract void setRod(ItemStack stack, WandRod rod);

	public abstract WandCap getCap(ItemStack stack);

	public abstract void setCap(ItemStack stack, WandCap cap);
	
	public abstract int getFocusPotency(ItemStack itemstack);
	
	public abstract int getFocusTreasure(ItemStack itemstack);
	
	public abstract int getFocusFrugal(ItemStack itemstack);
	
	public abstract int getFocusEnlarge(ItemStack itemstack);

	public abstract int getFocusExtend(ItemStack itemstack);

}