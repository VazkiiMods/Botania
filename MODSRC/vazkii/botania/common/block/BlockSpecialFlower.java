/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 22, 2014, 7:06:38 PM (GMT)]
 */
package vazkii.botania.common.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.ISpecialFlower;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.block.tile.TileSpecialFlower;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockSpecialFlower extends BlockFlower implements ITileEntityProvider, ISpecialFlower, IWandable, ILexiconable, IWandHUD {

	public static Map<String, IIcon> icons = new HashMap();

	static {
		BotaniaAPI.subtilesForCreativeMenu.addAll(Arrays.asList(new String[] {
				// Misc
				LibBlockNames.SUBTILE_PUREDAISY,
				LibBlockNames.SUBTILE_MANASTAR,

				// Generating
				LibBlockNames.SUBTILE_DAYBLOOM,
				LibBlockNames.SUBTILE_NIGHTSHADE,
				LibBlockNames.SUBTILE_ENDOFLAME,
				LibBlockNames.SUBTILE_HYDROANGEAS,
				LibBlockNames.SUBTILE_THERMALILY,
				LibBlockNames.SUBTILE_ARCANE_ROSE,
				LibBlockNames.SUBTILE_MUNCHDEW,
				LibBlockNames.SUBTILE_ENTROPINNYUM,
				LibBlockNames.SUBTILE_KEKIMURUS,

				// Functional
				LibBlockNames.SUBTILE_JADED_AMARANTHUS,
				LibBlockNames.SUBTILE_BELLETHORN,
				LibBlockNames.SUBTILE_DREADTHORN,
				LibBlockNames.SUBTILE_HEISEI_DREAM,
				LibBlockNames.SUBTILE_TIGERSEYE,
				LibBlockNames.SUBTILE_ORECHID,
				LibBlockNames.SUBTILE_FALLEN_KANADE,
				LibBlockNames.SUBTILE_EXOFLAME,
				LibBlockNames.SUBTILE_AGRICARNATION,
				LibBlockNames.SUBTILE_HOPPERHOCK,
				LibBlockNames.SUBTILE_RANNUNCARPUS,
				LibBlockNames.SUBTILE_TANGLEBERRIE,
				LibBlockNames.SUBTILE_JIYUULIA,
				LibBlockNames.SUBTILE_HYACIDUS,
				LibBlockNames.SUBTILE_POLLIDISIAC,
				LibBlockNames.SUBTILE_CLAYCONIA,
				LibBlockNames.SUBTILE_LOONIUM
		}));
	}

	protected BlockSpecialFlower() {
		super(0);
		setBlockName(LibBlockNames.SPECIAL_FLOWER);
		setHardness(0.1F);
		setStepSound(soundTypeGrass);
		setTickRandomly(true);
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
		setBlockBounds(0.3F, 0.0F, 0.3F, 0.8F, 1, 0.8F);
	}

	@Override
	public int getRenderType() {
		return LibRenderIDs.idSpecialFlower;
	}

	@Override
	public Block setBlockName(String par1Str) {
		GameRegistry.registerBlock(this, ItemBlockSpecialFlower.class, par1Str);
		return super.setBlockName(par1Str);
	}

	@Override
	public int quantityDropped(Random par1Random) {
		return 0;
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		for(String s : BotaniaAPI.subtilesForCreativeMenu)
			par3List.add(ItemBlockSpecialFlower.ofType(s));
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		for(String s : BotaniaAPI.subtilesForCreativeMenu)
			icons.put(s, IconHelper.forName(par1IconRegister, s));
	}

	@Override
	public IIcon getIcon(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5) {
		return ((TileSpecialFlower) par1iBlockAccess.getTileEntity(par2, par3, par4)).getIcon();
	}

	@Override
	public IIcon getIcon(int par1, int par2) {
		return BlockModFlower.icons[16];
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		String name = ((TileSpecialFlower) world.getTileEntity(x, y, z)).subTileName;
		return ItemBlockSpecialFlower.ofType(name);
	}

	@Override
	public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer) {
		if(!par6EntityPlayer.capabilities.isCreativeMode)
			dropBlockAsItem(par1World, par2, par3, par4, par5, 0);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> list = new ArrayList();
		TileEntity tile = world.getTileEntity(x, y, z);

		if(tile != null) {
			String name = ((TileSpecialFlower) tile).subTileName;
			list.add(ItemBlockSpecialFlower.ofType(name));
		}

		return list;
	}

	@Override
	public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6) {
		super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
		TileEntity tileentity = par1World.getTileEntity(par2, par3, par4);
		return tileentity != null ? tileentity.receiveClientEvent(par5, par6) : false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileSpecialFlower();
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return ((TileSpecialFlower) world.getTileEntity(x, y, z)).getEntry();
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
		return ((TileSpecialFlower) world.getTileEntity(x, y, z)).onWanded(stack, player);
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, int x, int y, int z) {
		((TileSpecialFlower) world.getTileEntity(x, y, z)).renderHUD(mc, res);
	}

}
