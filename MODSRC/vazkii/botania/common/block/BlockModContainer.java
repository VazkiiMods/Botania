/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 5:32:55 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.core.BotaniaCreativeTab;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BlockModContainer<T extends TileEntity> extends BlockContainer {

	protected BlockModContainer(Material par2Material) {
		super(par2Material);
		if(registerInCreative())
			setCreativeTab(BotaniaCreativeTab.INSTANCE);
	}

	@Override
	public Block setBlockName(String par1Str) {
		if(shouldRegisterInNameSet())
			GameRegistry.registerBlock(this, par1Str);
		return super.setBlockName(par1Str);
	}

	protected boolean shouldRegisterInNameSet() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = IconHelper.forBlock(par1IconRegister, this);
	}

	boolean registerInCreative() {
		return true;
	}

	@Override
	public abstract T createNewTileEntity(World world, int meta);

}