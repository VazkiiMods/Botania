package thaumcraft.api.aspects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApi.EntityTagsNBT;
import thaumcraft.api.ThaumcraftApiHelper;

public class AspectHelper {

	public static AspectList cullTags(AspectList temp) {
		AspectList temp2 = new AspectList();
		for (Aspect tag:temp.getAspects()) {
			if (tag!=null)
				temp2.add(tag, temp.getAmount(tag));
		}
		while (temp2!=null && temp2.size()>6) {
			Aspect lowest = null;
			float low = Short.MAX_VALUE;
			for (Aspect tag:temp2.getAspects()) {
				if (tag==null) continue;
				float ta=temp2.getAmount(tag);
				if (tag.isPrimal()) {
					ta *= .9f;
				} else {
					if (!tag.getComponents()[0].isPrimal()) {
						ta *= 1.1f;
						if (!tag.getComponents()[0].getComponents()[0].isPrimal()) {
							ta *= 1.05f;
						}
						if (!tag.getComponents()[0].getComponents()[1].isPrimal()) {
							ta *= 1.05f;
						}
					}
					if (!tag.getComponents()[1].isPrimal()) {
						ta *= 1.1f;
						if (!tag.getComponents()[1].getComponents()[0].isPrimal()) {
							ta *= 1.05f;
						}
						if (!tag.getComponents()[1].getComponents()[1].isPrimal()) {
							ta *= 1.05f;
						}
					}
				}
				
				if (ta<low) {
					low = ta;					 
					lowest = tag;
				}
			}
			temp2.aspects.remove(lowest);
		}
		return temp2; 
	}

	public static AspectList getObjectAspects(ItemStack is) {
		return ThaumcraftApi.internalMethods.getObjectAspects(is);
	}

	public static AspectList generateTags(Item item, int meta) {
		return ThaumcraftApi.internalMethods.generateTags(item, meta);
	}

	public static AspectList getEntityAspects(Entity entity) { 		
		AspectList tags = null;               
	    if (entity instanceof EntityPlayer) {
	    	tags = new AspectList();
	    	tags.add(Aspect.MAN, 4);        	
			Random rand = new Random(((EntityPlayer)entity).getCommandSenderName().hashCode());
	    	Aspect[] posa = Aspect.aspects.values().toArray(new Aspect[]{});
			tags.add(posa[rand.nextInt(posa.length)], 4);
	    	tags.add(posa[rand.nextInt(posa.length)], 4);
	    	tags.add(posa[rand.nextInt(posa.length)], 4);
	    } else {
	        f1:
			for (ThaumcraftApi.EntityTags et:ThaumcraftApi.scanEntities) {
				if (!et.entityName.equals(EntityList.getEntityString(entity))) continue;
				if (et.nbts==null || et.nbts.length==0) {
					tags = et.aspects;
				} else {
					NBTTagCompound tc = new NBTTagCompound();
					entity.writeToNBT(tc);
					for (EntityTagsNBT nbt:et.nbts) {
						if (tc.hasKey(nbt.name)) {
							if (!ThaumcraftApiHelper.getNBTDataFromId(tc, tc.getTagId(nbt.name), nbt.name).equals(nbt.value)) continue f1;
						} else {
							continue f1;
						}
					}
					tags = et.aspects;
				}
			}
	    }           		
		return tags;
	}

	public static Aspect getCombinationResult(Aspect aspect1, Aspect aspect2) {
		Collection<Aspect> aspects = Aspect.aspects.values();
		for (Aspect aspect:aspects) {
			if (aspect.getComponents()!=null && (
				(aspect.getComponents()[0]==aspect1 && aspect.getComponents()[1]==aspect2) ||
				(aspect.getComponents()[0]==aspect2 && aspect.getComponents()[1]==aspect1))) {
				
				return aspect;
			}
		}
		return null;
	}

	public static Aspect getRandomPrimal(Random rand, Aspect aspect) {
		ArrayList<Aspect> list = new ArrayList<Aspect>();
		if (aspect!=null) {			
			AspectList temp = new AspectList();
			temp.add(aspect, 1);
			AspectList temp2 = AspectHelper.reduceToPrimals(temp);
			for (Aspect a:temp2.getAspects()) {
				for (int b=0;b<temp2.getAmount(a);b++) {
					list.add(a);
				}
			}
		}
		
		return list.size()>0?list.get(rand.nextInt(list.size())):null;
	}

	public static AspectList reduceToPrimals(AspectList in) {
		AspectList out = new AspectList();
		for (Aspect aspect:in.getAspects()) {
			if (aspect!=null) {
				if (aspect.isPrimal()) {
					out.add(aspect, in.getAmount(aspect));
				} else {
					AspectList temp = new AspectList();
					temp.add(aspect.getComponents()[0],in.getAmount(aspect));
					temp.add(aspect.getComponents()[1],in.getAmount(aspect));
					AspectList temp2 = reduceToPrimals(temp);
					
					for (Aspect a:temp2.getAspects()) {
						out.add(a, temp2.getAmount(a));
					}
				}
			}
		}
		return out;
	}

	public static AspectList getPrimalAspects(AspectList in) {
		AspectList t = new AspectList();
		t.add(Aspect.AIR, in.getAmount(Aspect.AIR));
		t.add(Aspect.FIRE, in.getAmount(Aspect.FIRE));
		t.add(Aspect.WATER, in.getAmount(Aspect.WATER));
		t.add(Aspect.EARTH, in.getAmount(Aspect.EARTH));
		t.add(Aspect.ORDER, in.getAmount(Aspect.ORDER));
		t.add(Aspect.ENTROPY, in.getAmount(Aspect.ENTROPY));
		return t;
	}
	
	public static AspectList getAuraAspects(AspectList in) {
		AspectList t = new AspectList();
		t.add(Aspect.AIR, in.getAmount(Aspect.AIR));
		t.add(Aspect.FIRE, in.getAmount(Aspect.FIRE));
		t.add(Aspect.WATER, in.getAmount(Aspect.WATER));
		t.add(Aspect.EARTH, in.getAmount(Aspect.EARTH));
		t.add(Aspect.ORDER, in.getAmount(Aspect.ORDER));
		t.add(Aspect.ENTROPY, in.getAmount(Aspect.ENTROPY));
		t.add(Aspect.FLUX, in.getAmount(Aspect.FLUX));
		return t;
	}

}
