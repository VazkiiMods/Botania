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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * A capability interface marking a TE as a floating flower
 */
public interface IFloatingFlower {

	/**
	 * @return The itemstack to display on top of the island
	 */
	ItemStack getDisplayStack();

	IslandType getIslandType();

	void setIslandType(IslandType type);

	class Storage implements Capability.IStorage<IFloatingFlower> {
		@Nullable
		@Override
		public INBT writeNBT(Capability<IFloatingFlower> capability, IFloatingFlower instance, Direction side) {
			CompoundNBT ret = new CompoundNBT();
			ret.putString("islandType", instance.getIslandType().typeName);
			return ret;
		}

		@Override
		public void readNBT(Capability<IFloatingFlower> capability, IFloatingFlower instance, Direction side, INBT nbt) {
			if(nbt instanceof CompoundNBT) {
				IslandType t = IslandType.ofType(((CompoundNBT) nbt).getString("islandType"));
				if(t != null) {
					instance.setIslandType(t);
				}
			}
		}
	}

	class IslandType {
		private static final Map<String, IslandType> registry = new HashMap<>();

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
			typeName = name;
			synchronized (registry) {
				if (registry.containsKey(name)) throw new IllegalArgumentException(name+" already registered!");
				registry.put(name, this);
			}
		}

		public static IslandType ofType(String typeStr) {
			synchronized (registry) {
				IslandType type = registry.get(typeStr);
				return type == null ? GRASS : type;
			}
		}

		@Override
		public String toString() {
			return typeName;
		}

	}

}
