package thaumcraft.api.research;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class ResearchItem 
{
	/**
	 * A short string used as a key for this research. Must be unique
	 */
	public final String key;
	
	/**
	 * A short string used as a reference to the research category to which this must be added.
	 */
	public final String category;

	/**
	 * The aspect tags and their values required to complete this research	 * 
	 */
	public final AspectList tags;
	
	/**
     * This links to any research that needs to be completed before this research can be discovered or learnt.
     */
    public String[] parents = null;
    
    /**
     * Like parent above, but a line will not be displayed in the thaumonomicon linking them. Just used to prevent clutter.
     */
    public String[] parentsHidden = null;
    
    /**
     * any research linked to this that will be unlocked automatically when this research is complete
     */
    public String[] siblings = null;
	
    /**
     * the horizontal position of the research icon
     */
    public final int displayColumn;

    /**
     * the vertical position of the research icon
     */
    public final int displayRow;
    
    /**
     * the icon to be used for this research 
     */
    public final ItemStack[] icon_item;
    
    /**
     * the icon to be used for this research 
     */
    public final ResourceLocation[] icon_resource;
    
    /**
     * How large the research grid is. Valid values are 1 to 3.
     */
    private int complexity;

    /**
     * Special research has a spiky border. Used for important research milestones.
     */
    private boolean isSpecial;
    
    /**
     * Research that can be directly purchased with knowledge fragments
     */
    private boolean isSecondary;
    
	/**
     * This indicates if the research should use a circular icon border. Usually used for "passive" research 
     * that doesn't have recipes and grants passive effects, or that unlock automatically.
     */
    private boolean isRound;
    
    /**
     * Stub research cannot be discovered by normal means, but can be unlocked via the sibling system.
     */
    private boolean isStub;
    
    /**
     * This indicated that the research is completely hidden and cannot be discovered by any 
     * player-controlled means. The recipes will never show up in the thaumonomicon.
     * Usually used to unlock "hidden" recipes via sibling unlocking, like 
     * the various cap and rod combos for wands.
     */
    private boolean isVirtual;      
    
    /**
     * Concealed research does not display in the thaumonomicon until parent researches are discovered.
     * Often times some of the parent researches is linked to scanned objects.
     */
    private boolean isHidden;
        
    /**
     * These research items will automatically unlock for all players on game start
     */
    private boolean isAutoUnlock;
    
    /**
     * Flip the way the connecting lines are drawn in the research browser.
     */
    private boolean isFlipped;

	private ResearchPage[] pages = null;
	
	public ResearchItem(String key, String category)
    {
    	this.key = key;
    	this.category = category;
    	this.tags = new AspectList();    	
        this.icon_resource = null;
        this.icon_item = null;
        this.displayColumn = 0;
        this.displayRow = 0;
        this.isVirtual = true;      
    }
    
    public ResearchItem(String key, String category, AspectList tags, int col, int row, int complex, Object ... icon)
    {
    	this.key = key;
    	this.category = category;
    	this.tags = tags;    	
    	if (icon[0] instanceof ResourceLocation) {
    		ResourceLocation[] t = new ResourceLocation[icon.length];
    		System.arraycopy(icon, 0, t, 0, icon.length);
    		this.icon_resource = t;
    	} else this.icon_resource = null;
    	if (icon[0] instanceof ItemStack) {
    		ItemStack[] t = new ItemStack[icon.length];
    		System.arraycopy(icon, 0, t, 0, icon.length);
    		this.icon_item = t;
    	} else this.icon_item = null;
        this.displayColumn = col;
        this.displayRow = row;
        this.complexity = complex;
        if (complexity < 1) this.complexity = 1;
        if (complexity > 3) this.complexity = 3;
    }
    
    
    public ResearchItem setSpecial()
    {
        this.isSpecial = true;
        return this;
    }
    
    public ResearchItem setStub()
    {
        this.isStub = true;
        return this;
    }    
    
        
    public ResearchItem setHidden()
    {
        this.isHidden = true;
        return this;
    }
        
    public ResearchItem setParents(String... par)
    {
        this.parents = par;
        return this;
    }    
    
    

	public ResearchItem setParentsHidden(String... par)
    {
        this.parentsHidden = par;
        return this;
    }
    
    public ResearchItem setSiblings(String... sib)
    {
        this.siblings = sib;
        return this;
    }
    
    public ResearchItem setPages(ResearchPage... par)
    {
        this.pages = par;
        return this;
    }
    
    public ResearchPage[] getPages() {
		return pages;
	}

	public ResearchItem registerResearchItem()
    {
        ResearchCategories.addResearch(this);
        return this;
    }

    public String getName()
    {
    	return StatCollector.translateToLocal("tc.research_name."+key);
    }
    
    public String getText()
    {
    	return StatCollector.translateToLocal("tc.research_text."+key);
    }

    public boolean isSpecial()
    {
        return this.isSpecial;
    }
    
    public boolean isStub()
    {
        return this.isStub;
    }
                
    public boolean isHidden()
    {
        return this.isHidden;
    }
    
    public boolean isVirtual()
    {
        return this.isVirtual;
    }
    
    public boolean isAutoUnlock() {
		return isAutoUnlock;
	}
	
	public ResearchItem setAutoUnlock()
    {
        this.isAutoUnlock = true;
        return this;
    }
	
	public boolean isRound() {
		return isRound;
	}

	public ResearchItem setRound() {
		this.isRound = true;
		return this;
	}
	
	public ResearchItem setSecondary()
    {
        this.isSecondary = true;
        return this;
    }
	
	public boolean isSecondary() {
		return isSecondary;
	}

	public int getComplexity() {
		return complexity;
	}
	
	public ResearchItem setFlipped() {
    	this.isFlipped = true;
    	return this;
    }
	
	public boolean isFlipped() {
		return this.isFlipped;
	}

	public ResearchItem setComplexity(int complexity) {
		this.complexity = complexity;
		return this;
	}
	
	public int getExperience() {
		if (this.tags!=null && this.tags.visSize()>0) {
			return Math.max(1, (int) Math.sqrt(this.tags.visSize()));
		} else
		return 0;
	}

	/**
	 * @return the aspect aspects ordinal with the highest value. Used to determine scroll color and similar things
	 */
	public Aspect getResearchPrimaryTag() {
		Aspect aspect=null;
		int highest=0;
		if (tags!=null)
		for (Aspect tag:tags.getAspects()) {
			if (tags.getAmount(tag)>highest) {
				aspect=tag;
				highest=tags.getAmount(tag);
			};
		}
		return aspect;
	}
	
}
