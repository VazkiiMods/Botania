/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 14, 2014, 5:31:15 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import vazkii.botania.api.recipe.IElvenItem;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.block.ItemBlockElven;
import vazkii.botania.common.item.block.ItemBlockMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMod extends Block {

	public int originalLight;

	public BlockMod(Material par2Material) {
		super(par2Material);
		if(registerInCreative())
			setCreativeTab(BotaniaCreativeTab.INSTANCE);
	}

	@Override
	public Block setBlockName(String par1Str) {
		if(shouldRegisterInNameSet())
			GameRegistry.registerBlock(this, this instanceof IElvenItem ? ItemBlockElven.class : ItemBlockMod.class, par1Str);
		return super.setBlockName(par1Str);
	}

	protected boolean shouldRegisterInNameSet() {
		return true;
	}

	@Override
	public Block setLightLevel(float p_149715_1_) {
		originalLight = (int) (p_149715_1_ * 15);
		return super.setLightLevel(p_149715_1_);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = IconHelper.forBlock(par1IconRegister, this);
	}

	boolean registerInCreative() {
		return true;
	}


}
