/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [May 4, 2014, 12:29:56 PM (GMT)]
 */
package vazkii.botania.common.block;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.block.tile.TileOpenCrate;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockOpenCrate extends BlockModContainer implements ILexiconable {

	IIcon iconSide;
	IIcon iconBottom;
	IIcon iconSideCraft;
	IIcon iconBottomCraft;
	
	private static final int SUBTYPES = 2;
	
	public BlockOpenCrate() {
		super(Material.wood);
		setHardness(2.0F);
		setStepSound(soundTypeWood);
		setBlockName(LibBlockNames.OPEN_CRATE);
	}

	@Override
	protected boolean shouldRegisterInNameSet() {
		return false;
	}
	
	@Override
	public Block setBlockName(String par1Str) {
		GameRegistry.registerBlock(this, ItemBlockWithMetadataAndName.class, par1Str);
		return super.setBlockName(par1Str);
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		for(int i = 0; i < SUBTYPES; i++)
			par3List.add(new ItemStack(par1, 1, i));
	}
	
	@Override
	public int damageDropped(int meta) {
		return meta;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		iconSide = IconHelper.forBlock(par1IconRegister, this, 0);
		iconBottom = IconHelper.forBlock(par1IconRegister, this, 1);
		iconSideCraft = IconHelper.forBlock(par1IconRegister, this, 2);
		iconBottomCraft = IconHelper.forBlock(par1IconRegister, this, 3);
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return meta == 0 ? side == 0 ? iconBottom : iconSide : side == 0 ? iconBottomCraft : iconSideCraft;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileOpenCrate();
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.openCrate;
	}

}
