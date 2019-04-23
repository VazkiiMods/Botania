/**
 * This class was created by <codewarrior0>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.orechid;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Map;

// This only exists to hold the map entry
public class OrechidRecipeWrapper implements Comparable<OrechidRecipeWrapper> {
	public final Map.Entry<ResourceLocation, Integer> entry;

	public OrechidRecipeWrapper(Map.Entry<ResourceLocation, Integer> entry) {
		this.entry = entry;
	}

	@Override
	public int compareTo(@Nonnull OrechidRecipeWrapper o) {
		return Integer.compare(o.entry.getValue(), entry.getValue());
	}
}
