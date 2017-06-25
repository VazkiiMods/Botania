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
package vazkii.botania.api.item;

import net.minecraft.item.ItemStack;

import java.util.HashMap;

public interface IFloatingFlower {

	public ItemStack getDisplayStack();

	public IslandType getIslandType();

	public void setIslandType(IslandType type);

	public class IslandType {
		private static final HashMap<String, IslandType> registry = new HashMap<>();

		public static final IslandType GRASS = new IslandType("GRASS");
		public static final IslandType PODZOL = new IslandType("PODZOL");
		public static final IslandType MYCEL = new IslandType("MYCEL");
		public static final IslandType SNOW = new IslandType("SNOW");
		public static final IslandType DRY = new IslandType("DRY");
		public static final IslandType GOLDEN = new IslandType("GOLDEN");
		public static final IslandType VIVID = new IslandType("VIVID");
		public static final IslandType SCORCHED = new IslandType("SCORCHED");
		public static final IslandType INFUSED = new IslandType("INFUSED");
		public static final IslandType MUTATED = new IslandType("MUTATED");

		private final String typeName;

		/**
		 * Instantiates and registers a new floating flower island type
		 * Note that you need to register the model for this island type, see BotaniaAPIClient
		 * @param name The name of this floating flower island type
		 */
		public IslandType(String name) {
			if (registry.containsKey(name)) throw new IllegalArgumentException(name+" already registered!");
			typeName = name;
			registry.put(name, this);
		}

		public static IslandType ofType(String typeStr) {
			IslandType type = registry.get(typeStr);
			return type == null ? GRASS : type;
		}

		@Override
		public String toString() {
			return typeName;
		}

	}

}
