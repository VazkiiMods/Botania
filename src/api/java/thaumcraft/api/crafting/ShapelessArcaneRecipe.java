package thaumcraft.api.crafting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchHelper;

public class ShapelessArcaneRecipe implements IArcaneRecipe
{
    private ItemStack output = null;
    private ArrayList input = new ArrayList();
    
    public AspectList aspects = null;
    public String[] research; 
    
    public ShapelessArcaneRecipe(String research, ItemStack result, AspectList aspects, Object... recipe){ this(new String[] {research}, result,aspects, recipe); }

    public ShapelessArcaneRecipe(String[] research, ItemStack result, AspectList aspects, Object... recipe)
    {
        output = result.copy();
        this.research = research;
        this.aspects = aspects;
        for (Object in : recipe)
        {
            if (in instanceof ItemStack)
            {
                input.add(((ItemStack)in).copy());
            }
            else if (in instanceof Item)
            {
                input.add(new ItemStack((Item)in));
            }
            else if (in instanceof Block)
            {
                input.add(new ItemStack((Block)in));
            }
            else if (in instanceof String)
            {
                input.add(OreDictionary.getOres((String)in));
            }
            else
            {
                String ret = "Invalid shapeless ore recipe: ";
                for (Object tmp :  recipe)
                {
                    ret += tmp + ", ";
                }
                ret += output;
                throw new RuntimeException(ret);
            }
        }
    }

    @Override
    public int getRecipeSize(){ return input.size(); }

    @Override
    public ItemStack getRecipeOutput(){ return output; }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting var1){ return output.copy(); }
    
    @Override
    public boolean matches(InventoryCrafting inv, World world)
    {
    	return inv instanceof IArcaneWorkbench && matches(inv,world,null);
    }

    @Override
    public boolean matches(InventoryCrafting var1, World world, EntityPlayer player)
    {
    	if (player!=null && ( research != null && research[0].length()>0 && !ResearchHelper.isResearchComplete(player.getCommandSenderName(), research))) {
    		return false;
    	}
    	
        ArrayList required = new ArrayList(input);
        
        for (int x = 0; x < 9; x++)
        {
            ItemStack slot = var1.getStackInSlot(x);

            if (slot != null)
            {
                boolean inRecipe = false;
                Iterator req = required.iterator();

                while (req.hasNext())
                {
                    boolean match = false;

                    Object next = req.next();

                    if (next instanceof ItemStack)
                    {
                        match = checkItemEquals((ItemStack)next, slot);
                    }
                    else if (next instanceof List)
                    {
                        for (ItemStack item : (List<ItemStack>)next)
                        {
                            match = match || checkItemEquals(item, slot);
                        }
                    }

                    if (match)
                    {
                        inRecipe = true;
                        required.remove(next);
                        break;
                    }
                }

                if (!inRecipe)
                {
                    return false;
                }
            }
        }
        
        return required.isEmpty();
    }

    private boolean checkItemEquals(ItemStack target, ItemStack input)
    {
        if (input == null && target != null || input != null && target == null)
        {
            return false;
        }
        return (target.getItem() == input.getItem() && 
        		(!target.hasTagCompound() || ThaumcraftApiHelper.areItemStackTagsEqualForCrafting(input,target)) &&
        		(target.getItemDamage() == OreDictionary.WILDCARD_VALUE|| target.getItemDamage() == input.getItemDamage()));
    }

    /**
     * Returns the input for this recipe, any mod accessing this value should never
     * manipulate the values in this array as it will effect the recipe itself.
     * @return The recipes input vales.
     */
    public ArrayList getInput()
    {
        return this.input;
    }
    
    @Override		
	public AspectList getAspects() {
		return aspects;
	}
    
    @Override		
	public AspectList getAspects(InventoryCrafting inv) {
		return aspects;
	}
	
	@Override
	public String[] getResearch() {
		return research;
	}
	
	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting p_179532_1_)
    {
        ItemStack[] aitemstack = new ItemStack[p_179532_1_.getSizeInventory()];

        for (int i = 0; i < Math.min(9, aitemstack.length); ++i)
        {
            ItemStack itemstack = p_179532_1_.getStackInSlot(i);
            aitemstack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
        }

        return aitemstack;
    }
}
