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

import java.awt.*;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.state.enums.PoolVariant;
import vazkii.botania.client.core.handler.ClientTickHandler;

public class ItemBlockPool extends ItemBlockWithMetadataAndName {

	public ItemBlockPool(Block par2Block) {
		super(par2Block);
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		if(par1ItemStack.getItemDamage() == 1)
			for(int i = 0; i < 2; i++)
				par3List.add(StatCollector.translateToLocal("botaniamisc.creativePool" + i));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int renderPass) {
		if (stack.getItemDamage() == PoolVariant.FABULOUS.ordinal() && renderPass == 0) {
			float time = ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks;
			return Color.getHSBColor(time * 0.005F, 0.6F, 1F).hashCode();
		} else {
			return 16777215;
		}
	}

}
