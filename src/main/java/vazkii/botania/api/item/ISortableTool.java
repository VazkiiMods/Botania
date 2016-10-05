/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 23, 2015, 7:03:48 PM (GMT)]
 */
package vazkii.botania.api.item;

import net.minecraft.item.ItemStack;

/**
 * This interface describes a tool that can be sorted by the Ring of
 * Correction.
 */
public interface ISortableTool {

	/**
	 * Gets the type of tool that this is. A pick, axe or shovel.
	 */
	public ToolType getSortingType(ItemStack stack);

	/**
	 * Gets the priority that this tool should have when being sorted. The
	 * tool with the highest priority will be picked. The way this is specified
	 * should be (tool-level) * 100 + (tool-modifier) * 10 + (efficiency-level).
	 * <br><br>
	 * For example, a <b>Manasteel Pickaxe</b> is tool-level 10 and it doesn't have
	 * modifiers. Assuming Efficiency 4, the priority should be 10 * 100 + 4 = <b>1004</b>.
	 * This will rate higher than a similar pickaxe with Efficiency 3.<br>
	 * A <b>Terra Shatterer</b> has a modifier, its rank and is tool-level 20. With Efficiency
	 * 5 and rank B (2) the priority should be 20 * 100 + 2 * 10 + 5 = <b>2025</b>.
	 * <br><br>
	 * All intermediate tool levels are there for other mod tools that wish to occupy the spots inbetween.
	 * Of course, you don't have to always adhere to this. Tools like the <b>Vitreous Pickaxe</b> don't,
	 * that one in particular is priority 0 so it's never picked.
	 */
	public int getSortingPriority(ItemStack stack);

	public static enum ToolType {
		PICK, AXE, SHOVEL
	}

}