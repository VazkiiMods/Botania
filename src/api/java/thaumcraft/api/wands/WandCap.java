package thaumcraft.api.wands;

import java.util.LinkedHashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.Aspect;

/**
 * This class is used to keep the material information for the various caps. 
 * It is also used to generate the wand recipes ingame.
 * @author Azanor
 *
 */
public class WandCap {

	private String tag;
	
	/**
	 * Cost to craft this wand. Combined with the rod cost.
	 */
	private int craftCost;
	
	/**
	 * the amount by which all aspect costs are multiplied
	 */
	float baseCostModifier; 
	
	/**
	 * how many additional vis is gained per charge 'tick' (one charge tick occurs every 5 game ticks)
	 * examples: brass caps have a bonus of 1 and void caps have a bonus of 3 
	 */
	int chargeBonus; 
	
	
	/**
	 * specifies a list of primal aspects that use the special discount figure instead of the normal discount.
	 */
	List<Aspect> specialCostModifierAspects;
	
	/**
	 * the amount by which the specified aspect costs are multiplied
	 */
	float specialCostModifier;
	
	/**
	 * The texture that will be used for the ingame wand cap
	 */
	ResourceLocation texture;
	
	/**
	 * the actual item that makes up this cap and will be used to generate the wand recipes
	 */
	ItemStack item;
	
	public static LinkedHashMap<String,WandCap> caps = new LinkedHashMap<String,WandCap>();

	public WandCap (String tag, float discount, int charge, ItemStack item, int craftCost, ResourceLocation texture) {
		this.setTag(tag);
		this.baseCostModifier = discount;
		this.specialCostModifierAspects = null;
		this.texture = texture;
		this.item=item;
		this.chargeBonus = charge;
		this.setCraftCost(craftCost);
		caps.put(tag, this);
	}
	
	public WandCap (String tag, float discount, int charge, List<Aspect> specialAspects, float discountSpecial, ItemStack item, int craftCost, ResourceLocation texture) {
		this.setTag(tag);
		this.baseCostModifier = discount;
		this.specialCostModifierAspects = specialAspects;
		this.specialCostModifier = discountSpecial;
		this.texture = texture;
		this.item=item;
		this.chargeBonus = charge;
		this.setCraftCost(craftCost);
		caps.put(tag, this);
	}
	
	public float getBaseCostModifier() {
		return baseCostModifier;
	}

	public List<Aspect> getSpecialCostModifierAspects() {
		return specialCostModifierAspects;
	}

	public float getSpecialCostModifier() {
		return specialCostModifier;
	}
	
	public int getChargeBonus() {
		return chargeBonus;
	}

	public ResourceLocation getTexture() {
		return texture;
	}

	public void setTexture(ResourceLocation texture) {
		this.texture = texture;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public int getCraftCost() {
		return craftCost;
	}

	public void setCraftCost(int craftCost) {
		this.craftCost = craftCost;
	}
	
	/**
	 * The research a player needs to have finished to be able to craft a wand with this cap. 
	 */
	public String getResearch() {
		return "CAP_"+getTag();
	}
	
	//  Some examples:
	//  WandCap WAND_CAP_IRON = new WandCap("iron", 1.1f, Arrays.asList(Aspect.ORDER),1, new ItemStack(ConfigItems.itemWandCap,1,0),1);
	//  WandCap WAND_CAP_GOLD = new WandCap("gold", 1f, new ItemStack(ConfigItems.itemWandCap,1,1),3);
	
}
