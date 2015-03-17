/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 17, 2015, 9:46:53 PM (GMT)]
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockPump extends BlockMod {

	private static final int[][] ICON_ORIENTATIONS = {
		{ 4, 3, 1, 2 },
		{ 3, 4, 2, 1 },
		{ 2, 1, 4, 3 },
		{ 1, 2, 3, 4 }
	};
	
	IIcon[] icons = new IIcon[5];

	public BlockPump() {
		super(Material.rock);
		setHardness(2.0F);
		setResistance(10.0F);
		setStepSound(soundTypeStone);
		setBlockName(LibBlockNames.PUMP);
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
		int orientation = Math.max(2, BlockPistonBase.determineOrientation(par1World, par2, par3, par4, par5EntityLivingBase));
		par1World.setBlockMetadataWithNotify(par2, par3, par4, orientation, 1 | 2);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		for(int i = 0; i < icons.length; i++)
			icons[i] = IconHelper.forBlock(par1IconRegister, this, i);
	}

	@Override
	public IIcon getIcon(int par1, int par2) {
		return par1 < 2 ? icons[0] : icons[ICON_ORIENTATIONS[Math.max(0, par2 - 2)][par1 - 2]];
	}

	public int getOrientation(World w, int x, int y, int z) {
		return w.getBlockMetadata(x, y, z);
	}
}
