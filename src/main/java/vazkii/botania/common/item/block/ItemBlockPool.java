/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.common.block.mana.BlockPool;

import javax.annotation.Nonnull;

import java.util.List;

public class ItemBlockPool extends BlockItem {

	public ItemBlockPool(Block block, Settings props) {
		super(block, props);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(@Nonnull ItemStack stack, World world, @Nonnull List<Text> stacks, @Nonnull TooltipContext flag) {
		if (((BlockPool) getBlock()).variant == BlockPool.Variant.CREATIVE) {
			for (int i = 0; i < 2; i++) {
				stacks.add(new TranslatableText("botaniamisc.creativePool" + i).formatted(Formatting.GRAY));
			}
		}
	}

}
