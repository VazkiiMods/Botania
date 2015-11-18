package thaumcraft.api.crafting;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchHelper;

public class InfusionRecipe
{
	protected AspectList aspects;
	protected String[] research;
	private Object[] components;
	private Object recipeInput;
	protected Object recipeOutput;
	protected int instability;
	
	public InfusionRecipe(String research, Object output, int inst, AspectList aspects2, Object input, Object[] recipe) {
		this(new String[]{research},output,inst,aspects2,input,recipe);
	}
	
	public InfusionRecipe(String[] research, Object output, int inst, AspectList aspects2, Object input, Object[] recipe) {
		this.research = research;
		this.recipeOutput = output;
		this.recipeInput = input;
		this.aspects = aspects2;
		this.components = recipe;
		this.instability = inst;
	}

	/**
     * Used to check if a recipe matches current crafting inventory
     * @param player 
     */
	public boolean matches(ArrayList<ItemStack> input, ItemStack central, World world, EntityPlayer player) {
		if (getRecipeInput()==null) return false;
			
		if (research!=null && research[0].length()>0 && !ResearchHelper.isResearchComplete(player.getCommandSenderName(), research)) {
    		return false;
    	}
		
		ItemStack i2 = central.copy();
		if (getRecipeInput() instanceof ItemStack &&
				((ItemStack)getRecipeInput()).getItemDamage()==OreDictionary.WILDCARD_VALUE) {
			i2.setItemDamage(OreDictionary.WILDCARD_VALUE);
		}
		
		if (!ThaumcraftApiHelper.areItemStacksEqualForCrafting(i2, getRecipeInput())) return false;
		
		ArrayList<ItemStack> ii = new ArrayList<ItemStack>();
		for (ItemStack is:input) {
			ii.add(is.copy());
		}
		
		for (Object comp:getComponents()) {
			boolean b=false;
			for (int a=0;a<ii.size();a++) {
				 i2 = ii.get(a).copy();
				if (ThaumcraftApiHelper.areItemStacksEqualForCrafting(i2, comp)) {
					ii.remove(a);
					b=true;
					break;
				}
			}
			if (!b) return false;
		}
		return ii.size()==0?true:false;
    }
    
    public String[] getResearch() {
		return research;
    }
    
	public Object getRecipeInput() {
		return recipeInput;
	}

	public Object[] getComponents() {
		return components;
	}
	
	public Object getRecipeOutput() {
		return recipeOutput;
	}
	
	public AspectList getAspects() {
		return aspects;
	}
			
	
	public Object getRecipeOutput(EntityPlayer player, ItemStack input, ArrayList<ItemStack> comps ) {
		return recipeOutput;
    }
    
    public AspectList getAspects(EntityPlayer player, ItemStack input, ArrayList<ItemStack> comps) {
		return aspects;
    }
    
    public int getInstability(EntityPlayer player, ItemStack input, ArrayList<ItemStack> comps) {
		return instability;
    }
}
