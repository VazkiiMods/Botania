/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.OrechidOutput;
import vazkii.botania.common.lib.ResourceLocationHelper;

import java.util.Collections;
import java.util.List;

import me.shedaniel.rei.api.EntryStack;

public class OrechidIgnemREIDisplay extends OrechidBaseREIDisplay {
	public OrechidIgnemREIDisplay(OrechidRecipeWrapper recipe) {
		super(recipe);
		this.stone = Collections.singletonList(Collections.singletonList(EntryStack.create(new ItemStack(Blocks.NETHERRACK, 64))));
	}

	@Override
	protected List<OrechidOutput> getOreWeights() {
		return BotaniaAPI.instance().getNetherOrechidWeights();
	}

	@Override
	public @NotNull Identifier getRecipeCategory() {
		return ResourceLocationHelper.prefix("orechid_ignem");
	}
}
