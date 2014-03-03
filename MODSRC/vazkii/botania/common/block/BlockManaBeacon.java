/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 3, 2014, 4:53:59 PM (GMT)]
 */
package vazkii.botania.common.block;

import java.awt.Color;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.block.tile.TileManaBeacon;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockIDs;
import vazkii.botania.common.lib.LibBlockNames;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockManaBeacon extends BlockModContainer implements ILexiconable {

	Icon[] icons = new Icon[2];
	
	public BlockManaBeacon() {
		super(LibBlockIDs.idManaBeacon, Material.iron);
		setHardness(5.0F);
		setResistance(10.0F);
		setStepSound(soundMetalFootstep);
		float size = 3F / 16F;
		setBlockBounds(size, size, size, 1F - size, 1F - size, 1F - size);
		setUnlocalizedName(LibBlockNames.MANA_BEACON);
	}
	
	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		for(int i = 0; i < 2; i++)
			icons[i] = IconHelper.forBlock(par1IconRegister, this, i);
	}
	
	@Override
	public Icon getIcon(int par1, int par2) {
		return icons[par1 == 1 ? 1 : 0];
	}

	@Override
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		for(int i = 0; i < 16; i++)
			par3List.add(new ItemStack(par1, 1, i));
	}

	@Override
	public Block setUnlocalizedName(String par1Str) {
		GameRegistry.registerBlock(this, ItemBlockWithMetadataAndName.class, par1Str);
		return super.setUnlocalizedName(par1Str);
	}

	@Override
	protected boolean shouldRegisterInNameSet() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int damageDropped(int par1) {
		return par1;
	}

	@Override
	public int getRenderColor(int par1) {
		float[] color = EntitySheep.fleeceColorTable[par1];
		return new Color(color[0], color[1], color[2]).getRGB();
	}

	@Override
	public int colorMultiplier(IBlockAccess par1iBlockAccess, int par2, int par3, int par4) {
		return getRenderColor(par1iBlockAccess.getBlockMetadata(par2, par3, par4));
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.unstableBlocks;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileManaBeacon();
	}
}
