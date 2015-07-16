/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 15, 2015, 8:31:13 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.wand.IWandBindable;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.block.tile.TileLightRelay;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockLightRelay extends BlockModContainer implements IWandable {

	public static IIcon invIcon, worldIcon;
	
	protected BlockLightRelay() {
		super(Material.glass);
		float f = 5F / 16F;
		setBlockBounds(f, f, f, 1F - f, 1F - f, 1F - f);
		setBlockName(LibBlockNames.LIGHT_RELAY);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		invIcon = IconHelper.forBlock(par1IconRegister, this, 0);
		worldIcon = IconHelper.forBlock(par1IconRegister, this, 1);	
	}

	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return invIcon;
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
	public int getRenderType() {
		return LibRenderIDs.idLightRelay;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileLightRelay();
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
		return false;
	}
	
}
