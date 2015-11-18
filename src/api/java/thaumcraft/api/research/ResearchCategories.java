package thaumcraft.api.research;

import java.util.Collection;
import java.util.LinkedHashMap;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.common.FMLLog;

import org.apache.logging.log4j.Level;

public class ResearchCategories {
	
	//Research
	public static LinkedHashMap <String, ResearchCategoryList> researchCategories = new LinkedHashMap <String,ResearchCategoryList>();
	
	/**
	 * @param key
	 * @return the research item linked to this key
	 */
	public static ResearchCategoryList getResearchList(String key) {
		return researchCategories.get(key);
	}
	
	/**
	 * @param key
	 * @return the name of the research category linked to this key. 
	 * Must be stored as localization information in the LanguageRegistry.
	 */
	public static String getCategoryName(String key) {
		return StatCollector.translateToLocal("tc.research_category."+key);
	}
	
	/**
	 * @param key the research key
	 * @return the ResearchItem object. 
	 */
	public static ResearchItem getResearch(String key) {
		Collection rc = researchCategories.values();
		for (Object cat:rc) {
			Collection rl = ((ResearchCategoryList)cat).research.values();
			for (Object ri:rl) {
				if ((((ResearchItem)ri).key).equals(key)) return (ResearchItem)ri;
			}
		}
		return null;
	}
	
	/**
	 * This should only be done at the PostInit stage
	 * @param key the key used for this category
	 * @param researchkey the research that the player needs to have completed before this category becomes visible. Set as null to always show.
	 * @param icon the icon to be used for the research category tab
	 * @param background the resource location of the background image to use for this category
	 * @return the name of the research linked to this key
	 */
	public static void registerCategory(String key, String researchkey, ResourceLocation icon, ResourceLocation background) {
		if (getResearchList(key)==null) {
			ResearchCategoryList rl = new ResearchCategoryList(researchkey, icon, background);
			researchCategories.put(key, rl);
		}
	}
	
	/**
	 * This should only be done at the PostInit stage
	 * @param key the key used for this category
	 * @param researchkey the research that the player needs to have completed before this category becomes visible. Set as null to always show.
	 * @param icon the icon to be used for the research category tab
	 * @param background the resource location of the background image to use for this category
	 * @param background2 the resource location of the foreground image that lies between the background and icons
	 * @return the name of the research linked to this key
	 */
	public static void registerCategory(String key, String researchkey, ResourceLocation icon, ResourceLocation background, ResourceLocation background2) {
		if (getResearchList(key)==null) {
			ResearchCategoryList rl = new ResearchCategoryList(researchkey, icon, background, background2);
			researchCategories.put(key, rl);
		}
	}
	
	public static void addResearch(ResearchItem ri) {
		ResearchCategoryList rl = getResearchList(ri.category);
		if (rl!=null && !rl.research.containsKey(ri.key)) {
			
			if (!ri.isVirtual()) {
				for (ResearchItem rr:rl.research.values()) {
					if (rr.displayColumn == ri.displayColumn && rr.displayRow == ri.displayRow) {
						FMLLog.log(Level.FATAL, "[Thaumcraft] Research ["+ri.getName()+"] not added as it overlaps with existing research ["+rr.getName()+"]");
						return;
					}
				}
			}
			
			
			rl.research.put(ri.key, ri);
			
			if (ri.displayColumn < rl.minDisplayColumn) 
	        {
	            rl.minDisplayColumn = ri.displayColumn;
	        }

	        if (ri.displayRow < rl.minDisplayRow)
	        {
	            rl.minDisplayRow = ri.displayRow;
	        }

	        if (ri.displayColumn > rl.maxDisplayColumn)
	        {
	            rl.maxDisplayColumn = ri.displayColumn;
	        }

	        if (ri.displayRow > rl.maxDisplayRow)
	        {
	            rl.maxDisplayRow = ri.displayRow;
	        }
	        		}
	}
}
