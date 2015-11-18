package thaumcraft.api.wands;

import java.util.LinkedHashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * @author Azanor
 * 
 * This class is used to keep the material information for the various rods. 
 * It is also used to generate the wand recipes ingame.
 *
 */
public class WandRod {

	
	private String tag;
	
	/**
	 * Cost to craft this wand. Combined with the rod cost.
	 */
	private int craftCost;
	
	/** 
	 * The amount of vis that can be stored
	 */
	int capacity;   

	/**
	 * The texture that will be used for the ingame wand rod
	 */
	protected ResourceLocation texture;
	
	/**
	 * the actual item that makes up this rod and will be used to generate the wand recipes
	 */
	ItemStack item;
	
	/**
	 * A class that will be called whenever the wand onUpdate tick is run
	 */
	IWandRodOnUpdate onUpdate;
	
	/**
	 * Is this a staff rod?
	 */
	private boolean isStaff=false;
	
	/**
	 * Does this rod give a free level of potency? 
	 */
	private boolean potencyBonus=false;	

	public static LinkedHashMap<String,WandRod> rods = new LinkedHashMap<String,WandRod>();
	
	public static WandRod getRod(String tag) {
		return rods.get(tag);
	}
	
	public WandRod (String tag, int capacity, ItemStack item, int craftCost, ResourceLocation texture) {
		this.setTag(tag);
		this.capacity = capacity;
		this.texture = texture;
		this.item=item;
		this.setCraftCost(craftCost);
		rods.put(tag, this);
	}
	
	public WandRod (String tag, int capacity, ItemStack item, int craftCost, IWandRodOnUpdate onUpdate, ResourceLocation texture) {
		this.setTag(tag);
		this.capacity = capacity;
		this.texture = texture;
		this.item=item;
		this.setCraftCost(craftCost);
		rods.put(tag, this);
		this.onUpdate = onUpdate;
	}
	
	public String getTag() {
		return tag;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public ResourceLocation getTexture() {
		return texture;
	}

	public void setTexture(ResourceLocation texture) {
		this.texture = texture;
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

	public IWandRodOnUpdate getOnUpdate() {
		return onUpdate;
	}

	public void setOnUpdate(IWandRodOnUpdate onUpdate) {
		this.onUpdate = onUpdate;
	}
	
	/**
	 * The research a player needs to have finished to be able to craft a wand with this rod. 
	 */
	public String getResearch() {
		return "ROD_"+getTag();
	}
	
	public boolean isStaff() {
		return isStaff;
	}

	public void setStaff(boolean isStaff) {
		this.isStaff = isStaff;
	}
	
	public boolean hasPotencyBonus() {
		return potencyBonus;
	}

	public void setPotencyBonus(boolean potencyBonus) {
		this.potencyBonus = potencyBonus;
	}

	//  Some examples:
	//	WandRod WAND_ROD_WOOD = new WandRod("wood",250,new ItemStack(Item.stick),1,new ResourceLocation("thaumcraft","items/wand/cap_iron_mat"));
	//	WandRod WAND_ROD_BLAZE = new WandRod("blaze",750,new ItemStack(Item.blazeRod),7,new ResourceLocation("thaumcraft","items/wand/rod_blaze_mat"),new WandRodBlazeOnUpdate());
}
