/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.common.block.mana.BlockPool;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.List;

public class ItemBlockPool extends BlockItem {

	public ItemBlockPool(Block block, Properties props) {
		super(block, props);
		addPropertyOverride(new ResourceLocation(LibMisc.MOD_ID, "full"), (stack, worldIn, entityIn) -> {
			boolean renderFull = ((BlockPool) block).variant == BlockPool.Variant.CREATIVE || stack.hasTag() && stack.getTag().getBoolean("RenderFull");
			return renderFull ? 1F : 0F;
		});
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(@Nonnull ItemStack stack, World world, @Nonnull List<ITextComponent> stacks, @Nonnull ITooltipFlag flag) {
		if (((BlockPool) getBlock()).variant == BlockPool.Variant.CREATIVE) {
			for (int i = 0; i < 2; i++) {
				stacks.add(new TranslationTextComponent("botaniamisc.creativePool" + i).applyTextStyle(TextFormatting.GRAY));
			}
		}
	}

}
