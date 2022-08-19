/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Nov 17, 2014, 6:05:18 PM (GMT)]
 */
package vazkii.botania.common.block.decor;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.lib.LibResources;

import java.util.HashMap;

public interface IFloatingFlower {

	public ItemStack getDisplayStack();

	public IslandType getIslandType();

	public void setIslandType(IslandType type);

	public class IslandType {
		private static HashMap<String, IslandType> registry = new HashMap<String, IslandType>();

		public static final IslandType GRASS = new IslandType("GRASS", LibResources.MODEL_MINI_ISLAND);
		public static final IslandType PODZOL = new IslandType("PODZOL", LibResources.MODEL_MINI_ISLAND_PODZOL);
		public static final IslandType MYCEL = new IslandType("MYCEL", LibResources.MODEL_MINI_ISLAND_MYCEL);
		public static final IslandType SNOW = new IslandType("SNOW", LibResources.MODEL_MINI_ISLAND_SNOW);
		public static final IslandType DRY = new IslandType("DRY", LibResources.MODEL_MINI_ISLAND_DRY);
		public static final IslandType GOLDEN = new IslandType("GOLDEN", LibResources.MODEL_MINI_ISLAND_GOLDEN);
		public static final IslandType VIVID = new IslandType("VIVID", LibResources.MODEL_MINI_ISLAND_VIVID);
		public static final IslandType SCORCHED = new IslandType("SCORCHED", LibResources.MODEL_MINI_ISLAND_SCORCHED);
		public static final IslandType INFUSED = new IslandType("INFUSED", LibResources.MODEL_MINI_ISLAND_INFUSED);
		public static final IslandType MUTATED = new IslandType("MUTATED", LibResources.MODEL_MINI_ISLAND_MUTATED);

		public IslandType(String name, String s) {
			this(name, new ResourceLocation(s));
		}

		public IslandType(String name, ResourceLocation s) {
			if (registry.containsKey(name)) throw new IllegalArgumentException(name+" already registered!");
			this.typeName = name;
			res = s;
			registry.put(name, this);
		}

		private final ResourceLocation res;
		public final String typeName;

		public static IslandType ofType(String typeStr) {
			IslandType type = registry.get(typeStr);
			return type == null ? GRASS : type;
		}

		public ResourceLocation getResource() {
			return res;
		}

		public int getColor() {
			return 0xFFFFFF;
		}

		public String toString() {
			return this.typeName;
		}

	}

}
