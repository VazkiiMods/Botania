/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 27, 2014, 8:32:52 PM (GMT)]
 */
package vazkii.botania.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.state.enums.PoolVariant;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemBlockPool extends ItemBlockWithMetadataAndName {

	public ItemBlockPool(Block par2Block) {
		super(par2Block);
		addPropertyOverride(new ResourceLocation(LibMisc.MOD_ID, "full"), (stack, worldIn, entityIn) -> {
			boolean renderFull = stack.getItemDamage() == PoolVariant.CREATIVE.ordinal() || stack.hasTagCompound() && stack.getTagCompound().getBoolean("RenderFull");
			return renderFull ? 1F : 0F;
		});
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(@Nonnull ItemStack par1ItemStack, World world, @Nonnull List<String> stacks, @Nonnull ITooltipFlag flag) {
		if(par1ItemStack.getItemDamage() == 1)
			for(int i = 0; i < 2; i++)
				stacks.add(I18n.format("botaniamisc.creativePool" + i));
	}

}
