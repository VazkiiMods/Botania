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

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lexicon.page.PageShedding;
import vazkii.botania.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class SheddingHandler {

	private SheddingHandler() {}

	private static final List<ShedPattern> patterns = new ArrayList<>();
	private static final List<ShedPattern> defaultPatterns = new ArrayList<>();

	@SubscribeEvent
	public static void onLivingUpdate(LivingUpdateEvent event) {
		if(event.getEntityLiving().world.isRemote)
			return;

		ShedPattern pattern = getShedPattern(event.getEntityLiving());

		if(pattern != null) {
			if(event.getEntityLiving().world.rand.nextInt(pattern.getRate()) == 0)
				event.getEntityLiving().entityDropItem(pattern.getItemStack(), 0.0F);
		}
	}

	private static ShedPattern getShedPattern(Entity entity) {
		for(ShedPattern pattern : patterns)
			if(pattern.type.getEntityClass().isInstance(entity))
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
			PageShedding page = new PageShedding(String.valueOf(i), pattern.getEntityString(), pattern.lexiconSize, pattern.getItemStack());
			LexiconData.shedding.addPage(page);
		}
	}

	public static void loadFromConfig(Configuration config) {
		//		defaultPatterns.add(new ShedPattern(EntityChicken.class, new ItemStack(Items.FEATHER), 26000, 20));
		//		defaultPatterns.add(new ShedPattern(EntitySquid.class, new ItemStack(Items.DYE), 18000, 20));
		//		defaultPatterns.add(new ShedPattern(EntityVillager.class, new ItemStack(Items.EMERALD), 226000, 40));
		//		defaultPatterns.add(new ShedPattern(EntitySpider.class, new ItemStack(Items.STRING), 12000, 40));
		//		defaultPatterns.add(new ShedPattern(EntityBlaze.class, new ItemStack(Items.BLAZE_POWDER), 8000, 40));
		//		defaultPatterns.add(new ShedPattern(EntityGhast.class, new ItemStack(Items.GHAST_TEAR), 9001, 30));
		//		defaultPatterns.add(new ShedPattern(EntitySkeleton.class, new ItemStack(Items.BONE), 36000, 40));
		//		defaultPatterns.add(new ShedPattern(EntitySlime.class, new ItemStack(Items.SLIME_BALL), 21000, 40));

		ArrayList<String> defaultNames = new ArrayList<>();

		for(ShedPattern pattern : defaultPatterns) {
			loadFromConfig(config, pattern.getEntityString(), pattern);
			defaultNames.add(pattern.getEntityString());
		}

		for(Entry<ResourceLocation, EntityType<?>> entry : ForgeRegistries.ENTITIES.getEntries()) {
			if(EntityLiving.class.isAssignableFrom(entry.getValue().getEntityClass())) {
				String name = entry.getKey().toString();
				if(!defaultNames.contains(name))
					loadFromConfig(config, name, null);
			}
		}
	}

	private static void loadFromConfig(Configuration config, String key, ShedPattern defaultPattern) {
		String itemName = "";
		int rate = -1;
		int lexiconSize = 40;

		if(defaultPattern != null) {
			itemName = defaultPattern.getItemStack().getItem().getRegistryName().toString();
			rate = defaultPattern.rate;
			lexiconSize = defaultPattern.lexiconSize;
		}

		Property prop = config.get("Shedding", key + ".item", itemName);
		prop.setComment("Configuration of Shedding for "+key);
		itemName = prop.getString();
		rate = config.get("Shedding", key + ".rate", rate).getInt();
		lexiconSize = config.get("Shedding", key + ".lexiconDisplaySize", lexiconSize).getInt();

		if(itemName != null && Item.REGISTRY.getObject(new ResourceLocation(itemName)) != null && rate != -1)
			patterns.add(new ShedPattern(EntityList.getClass(new ResourceLocation(key)), new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(itemName))), rate, lexiconSize));
	}

	private static class ShedPattern {

		private final EntityType<?> type;
		private final ItemStack itemStack;
		private final int rate;
		private final int lexiconSize;

		public ShedPattern(EntityType<?> type, ItemStack itemStack, int rate, int lexiconSize) {
			this.type = type;
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
			return type.getRegistryName().toString();
		}
	}

}
