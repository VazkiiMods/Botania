package thaumcraft.api.crafting;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class CrucibleRecipe {

	private ItemStack recipeOutput;
	
	public Object catalyst;
	public AspectList aspects;
	public String research;
	
	public int hash;
	
	public CrucibleRecipe(String researchKey, ItemStack result, Object cat, AspectList tags) {
		recipeOutput = result;
		this.aspects = tags;
		this.research = researchKey;
		this.catalyst = cat;
		if (cat instanceof String) {
			this.catalyst = OreDictionary.getOres((String) cat);
		}
		String hc = researchKey;
		hc += result.toString();
		for (Aspect tag:tags.getAspects()) {
			hc += tag.getTag()+tags.getAmount(tag);
		}
		if (cat instanceof ItemStack) {
			hc += ((ItemStack)cat).toString();
		} else
		if (cat instanceof List && ((List<ItemStack>)catalyst).size()>0) {
			for (ItemStack is :(List<ItemStack>)catalyst) {
				hc += is.toString();
			}
		}
		
		hash = hc.hashCode();
	}
	
		

	public boolean matches(AspectList itags, ItemStack cat) {
		if (catalyst instanceof ItemStack && !OreDictionary.itemMatches((ItemStack) catalyst,cat,false)) {
			return false;
		} else 
		if (catalyst instanceof List && ((List<ItemStack>)catalyst).size()>0) {
			if (!ThaumcraftApiHelper.containsMatch(false, new ItemStack[]{cat},
					(List<ItemStack>)catalyst)) return false;
		}
		if (itags==null) return false;
		for (Aspect tag:aspects.getAspects()) {
			if (itags.getAmount(tag)<aspects.getAmount(tag)) return false;
		}
		return true;
	}
	
	public boolean catalystMatches(ItemStack cat) {
		if (catalyst instanceof ItemStack && OreDictionary.itemMatches((ItemStack) catalyst,cat,false)) {
			return true;
		} else 
		if (catalyst instanceof List && ((List<ItemStack>)catalyst).size()>0) {
			if (ThaumcraftApiHelper.containsMatch(false, new ItemStack[]{cat}, (List<ItemStack>)catalyst)) return true;
		}
		return false;
	}
	
	public AspectList removeMatching(AspectList itags) {
		AspectList temptags = new AspectList();
		temptags.aspects.putAll(itags.aspects);
		
		for (Aspect tag:aspects.getAspects()) {
			temptags.remove(tag, aspects.getAmount(tag));
		}
		
		itags = temptags;
		return itags;
	}
	
	public ItemStack getRecipeOutput() {
		return recipeOutput;
	}
	

}
