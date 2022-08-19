/**
 * This class was created by <SoundLogic>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 4, 2014, 10:38:50 PM (GMT)]
 */
package vazkii.botania.common.core.handler;

import java.util.ArrayList;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lexicon.page.PageShedding;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class SheddingHandler {

	public static ArrayList<ShedPattern> patterns = new ArrayList<ShedPattern>();
	public static ArrayList<ShedPattern> defaultPatterns = new ArrayList<ShedPattern>();

	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		if(event.entity.worldObj.isRemote)
			return;

		ShedPattern pattern = getShedPattern(event.entity);

		if(pattern != null) {
			if(event.entity.worldObj.rand.nextInt(pattern.getRate()) == 0)
				event.entity.entityDropItem(pattern.getItemStack(), 0.0F);
		}
	}

	public static ShedPattern getShedPattern(Entity entity) {
		for(ShedPattern pattern : patterns)
			if(pattern.EntityClass.isInstance(entity))
				return pattern;

		return null;
	}

	public static boolean hasShedding() {
		return patterns.size() > 0;
	}

	public static void addToLexicon() {
		if(!hasShedding())
			return;

		int i = 1;
		for(ShedPattern pattern : patterns) {
			PageShedding page = new PageShedding(String.valueOf(i), (String)EntityList.classToStringMapping.get(pattern.EntityClass), pattern.lexiconSize, pattern.getItemStack());
			LexiconData.shedding.addPage(page);
		}
	}

	public static void loadFromConfig(Configuration config) {
		defaultPatterns.add(new ShedPattern(EntityChicken.class, new ItemStack(Items.feather), 26000, 20));
		defaultPatterns.add(new ShedPattern(EntitySquid.class, new ItemStack(Items.dye), 18000, 20));
		defaultPatterns.add(new ShedPattern(EntityVillager.class, new ItemStack(Items.emerald), 226000, 40));
		defaultPatterns.add(new ShedPattern(EntitySpider.class, new ItemStack(Items.string), 12000, 40));
		defaultPatterns.add(new ShedPattern(EntityBlaze.class, new ItemStack(Items.blaze_powder), 8000, 40));
		defaultPatterns.add(new ShedPattern(EntityGhast.class, new ItemStack(Items.ghast_tear), 9001, 30));
		defaultPatterns.add(new ShedPattern(EntitySkeleton.class, new ItemStack(Items.bone), 36000, 40));
		defaultPatterns.add(new ShedPattern(EntitySlime.class, new ItemStack(Items.slime_ball), 21000, 40));

		ArrayList<String> defaultNames = new ArrayList<String>();

		for(ShedPattern pattern : defaultPatterns) {
			loadFromConfig(config, pattern.getEntityString(), pattern);
			defaultNames.add(pattern.getEntityString());
		}

		for(Object o : EntityList.stringToClassMapping.entrySet()) {
			Entry<String, Class> entry = (Entry<String, Class>) o;

			if(EntityLiving.class.isAssignableFrom(entry.getValue())) {
				String name = entry.getKey();
				if(!defaultNames.contains(name))
					loadFromConfig(config, name, null);
			}
		}
	}

	public static void loadFromConfig(Configuration config, String key, ShedPattern defaultPattern) {
		String itemName = "";
		int metadata = 0;
		int rate = -1;
		int lexiconSize = 40;

		if(defaultPattern != null) {
			itemName = Item.itemRegistry.getNameForObject(defaultPattern.getItemStack().getItem());
			metadata = defaultPattern.getItemStack().getItemDamage();
			rate = defaultPattern.rate;
			lexiconSize = defaultPattern.lexiconSize;
		}

		Property prop = config.get("Shedding", key + ".item", itemName);
		prop.comment = "Configuration of Shedding for "+key;
		itemName = prop.getString();
		rate = config.get("Shedding", key + ".rate", rate).getInt();
		metadata = config.get("Shedding", key + ".metadata", metadata).getInt();
		lexiconSize = config.get("Shedding", key + ".lexiconDisplaySize", lexiconSize).getInt();

		if(itemName != null && !itemName.isEmpty() && rate != -1)
			patterns.add(new ShedPattern((Class<?>) EntityList.stringToClassMapping.get(key), new ItemStack((Item) Item.itemRegistry.getObject(itemName), 1, metadata), rate, lexiconSize));
	}

	public static class ShedPattern {

		Class EntityClass;
		ItemStack itemStack;
		int rate;
		int lexiconSize;

		public ShedPattern(Class EntityClass, ItemStack itemStack, int rate, int lexiconSize) {
			this.EntityClass = EntityClass;
			this.itemStack = itemStack;
			this.rate = rate;
			this.lexiconSize = lexiconSize;
		}
		public ItemStack getItemStack() {
			return itemStack.copy();
		}

		public int getRate() {
			return rate;
		}

		public String getEntityString() {
			return (String) EntityList.classToStringMapping.get(EntityClass);
		}
	}

}
