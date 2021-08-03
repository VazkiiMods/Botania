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
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import vazkii.botania.common.block.mana.BlockPool;

import javax.annotation.Nonnull;

import java.util.List;

public class ItemBlockPool extends BlockItem {

	public ItemBlockPool(Block block, Properties props) {
		super(block, props);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendHoverText(@Nonnull ItemStack stack, Level world, @Nonnull List<Component> stacks, @Nonnull TooltipFlag flag) {
		if (((BlockPool) getBlock()).variant == BlockPool.Variant.CREATIVE) {
			for (int i = 0; i < 2; i++) {
				stacks.add(new TranslatableComponent("botaniamisc.creativePool" + i).withStyle(ChatFormatting.GRAY));
			}
		}
	}

}
