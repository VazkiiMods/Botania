/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 9, 2014, 7:17:46 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.block.tile.TileAlfPortal;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockAlfPortal extends BlockModContainer implements IWandable, ILexiconable {

	IIcon iconOff, iconOn;
	public static IIcon portalTex;

	public BlockAlfPortal() {
		super(Material.wood);
		setHardness(10F);
		setStepSound(soundTypeWood);
		setBlockName(LibBlockNames.ALF_PORTAL);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		iconOff = IconHelper.forBlock(par1IconRegister, this, 0);
		iconOn = IconHelper.forBlock(par1IconRegister, this, 1);
		portalTex = IconHelper.forBlock(par1IconRegister, this, "Inside");
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return meta == 0 ? iconOff : iconOn;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileAlfPortal();
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.alfhomancyIntro;
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
		boolean did = ((TileAlfPortal) world.getTileEntity(x, y, z)).onWanded();
		if(did && player != null)
			player.addStat(ModAchievements.elfPortalOpen, 1);
		return did;
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z) == 0 ? 0 : 15;
	}

}
