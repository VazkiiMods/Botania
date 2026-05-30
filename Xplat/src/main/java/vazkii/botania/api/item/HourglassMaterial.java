package vazkii.botania.api.item;

import net.minecraft.resources.ResourceLocation;

import vazkii.botania.api.capability.ItemApiNoContext;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

/**
 * An item with this capability can be used in a Hovering Hourglass.
 */
public interface HourglassMaterial {
	ResourceLocation ID = botaniaRL("hourglass_material");
	ItemApiNoContext<HourglassMaterial> LOOKUP = new ItemApiNoContext<>(ID, HourglassMaterial.class);

	HourglassMaterial SAND = new Material(20, 0xFFEC49, false);
	HourglassMaterial RED_SAND = new Material(200, 0xE95800, false);
	HourglassMaterial SOUL_SAND = new Material(1200, 0x5A412F, false);
	HourglassMaterial MANA_POWDER = new Material(1, 0x03abFF, true);

	/**
	 * Defines the number of game ticks each unit of this material corresponds to in the hourglass.
	 * For {@link #isCounter() counter materials} this instead specifies the number opf burst hits per unit.
	 * Must return a value > 0.
	 */
	int numberOfTicks();

	/**
	 * Defines the color of this material inside a hovering hourglass as packed RGB value.
	 */
	int color();

	/**
	 * Whether this material counts the number of mana bursts the hourglass was hit by instead of counting the time.
	 */
	boolean isCounter();

	/**
	 * A default implementation of {@link HourglassMaterial} with constant properties.
	 */
	record Material(int numberOfTicks, int color, boolean isCounter) implements HourglassMaterial {
	}
}
