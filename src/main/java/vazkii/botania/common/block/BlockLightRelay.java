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

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.block.tile.TileLightRelay;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockLightRelay extends BlockModContainer implements IWandable, ILexiconable {

	public static IIcon invIcon, worldIcon, invIconRed, worldIconRed;

	protected BlockLightRelay() {
		super(Material.glass);
		float f = 5F / 16F;
		setBlockBounds(f, f, f, 1F - f, 1F - f, 1F - f);
		setBlockName(LibBlockNames.LIGHT_RELAY);
	}

	@Override
	public Block setBlockName(String par1Str) {
		register(par1Str);
		return super.setBlockName(par1Str);
	}

	void register(String name) {
		GameRegistry.registerBlock(this, ItemBlockWithMetadataAndName.class, name);
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for(int i = 0; i < 2; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	public int damageDropped(int meta) {
		return meta == 0 ? 0 : 1;
	}

	@Override
	protected boolean shouldRegisterInNameSet() {
		return false;
	}

	@Override
	public boolean getBlocksMovement(IBlockAccess p_149655_1_, int p_149655_2_, int p_149655_3_, int p_149655_4_) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int s, float xs, float ys, float zs) {
		((TileLightRelay) world.getTileEntity(x, y, z)).mountEntity(player);
		return true;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
		return null;
	}

	@Override
	public int tickRate(World p_149738_1_) {
		return 2;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) & -9, 1 | 2);
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int s) {
		int meta = world.getBlockMetadata(x, y, z);
		return (meta & 8) != 0 ? 15 : 0;
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		invIcon = IconHelper.forBlock(par1IconRegister, this, 0);
		worldIcon = IconHelper.forBlock(par1IconRegister, this, 1);
		invIconRed = IconHelper.forBlock(par1IconRegister, this, 2);
		worldIconRed = IconHelper.forBlock(par1IconRegister, this, 3);
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return meta > 0 ? invIconRed : invIcon;
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

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.luminizerTransport;
	}

}
