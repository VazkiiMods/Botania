package thaumcraft.api.crafting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchHelper;

@Deprecated
public class InfusionEnchantmentRecipe
{
	
	public AspectList aspects;
	public String[] research;
	public Object[] components;
	public Enchantment enchantment;
	public int recipeXP;
	public int instability;
	
	public InfusionEnchantmentRecipe(String research, Enchantment input, int inst, AspectList aspects2, Object[] recipe) {
		this(new String[]{research},input,inst,aspects2,recipe);
	}
	
	public InfusionEnchantmentRecipe(String[] research, Enchantment input, int inst, AspectList aspects2, Object[] recipe) {
		this.research = research;
		this.enchantment = input;
		this.aspects = aspects2;
		this.components = recipe;
		this.instability = inst;
		this.recipeXP = Math.max(1, input.getMinEnchantability(1)/3);
	}

	/**
     * Used to check if a recipe matches current crafting inventory
     * @param player 
     */
	public boolean matches(ArrayList<ItemStack> input, ItemStack central, World world, EntityPlayer player) {
		if (research!=null && research[0].length()>0 && !ResearchHelper.isResearchComplete(player.getCommandSenderName(), research)) {
    		return false;
    	}
		
		if (!enchantment.canApply(central) || !central.getItem().isItemTool(central)) {
			return false;
		}
				
		Map map1 = EnchantmentHelper.getEnchantments(central);
		Iterator iterator = map1.keySet().iterator();
        while (iterator.hasNext())
        {
        	int j1 = ((Integer)iterator.next()).intValue();
            Enchantment ench = Enchantment.enchantmentsBookList[j1];
            if (j1 == enchantment.effectId &&
            		EnchantmentHelper.getEnchantmentLevel(j1, central)>=ench.getMaxLevel())
            	return false;
            if (enchantment.effectId != ench.effectId && 
            	(!enchantment.canApplyTogether(ench) ||
            	!ench.canApplyTogether(enchantment))) {
            	return false;
            }
        }
		
		ItemStack i2 = null;
		
		ArrayList<ItemStack> ii = new ArrayList<ItemStack>();
		for (ItemStack is:input) {
			ii.add(is.copy());
		}
		
		for (Object comp:components) {
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
//		System.out.println(ii.size());
		return ii.size()==0?true:false;
    }
	   
    public Enchantment getEnchantment() {
		return enchantment;
    	
    }
    
    public AspectList getAspects() {
		return aspects;
    	
    }
    
    public String[] getResearch() {
		return research;
    	
    }

	public int calcInstability(ItemStack recipeInput) {
		int i = 0;
		Map map1 = EnchantmentHelper.getEnchantments(recipeInput);
		Iterator iterator = map1.keySet().iterator();
        while (iterator.hasNext())
        {        	int j1 = ((Integer)iterator.next()).intValue();
        	
        	i += EnchantmentHelper.getEnchantmentLevel(j1, recipeInput);
        }
		return (i/2) + instability;
	}

	public int calcXP(ItemStack recipeInput) {
		return recipeXP * (1+EnchantmentHelper.getEnchantmentLevel(enchantment.effectId, recipeInput));
	}

	public float getEssentiaMod(ItemStack recipeInput) {
		float mod = EnchantmentHelper.getEnchantmentLevel(enchantment.effectId, recipeInput);
		Map map1 = EnchantmentHelper.getEnchantments(recipeInput);
		Iterator iterator = map1.keySet().iterator();
        while (iterator.hasNext())
        {
        	int j1 = ((Integer)iterator.next()).intValue();
        	if (j1 != enchantment.effectId)
        		mod += EnchantmentHelper.getEnchantmentLevel(j1, recipeInput) * .1f;
        }
		return mod;
	}

}
