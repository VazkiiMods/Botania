/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 18, 2014, 10:13:02 PM (GMT)]
 */
package vazkii.botania.common.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import thaumcraft.api.crafting.IInfusionStabiliser;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.block.tile.TilePylon;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;

@Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.crafting.IInfusionStabiliser", striprefs = true)
public class BlockPylon extends BlockModContainer implements ILexiconable, IInfusionStabiliser {

	public BlockPylon() {
		super(Material.iron);
		setHardness(5.5F);
		setStepSound(soundTypeMetal);
		setBlockName(LibBlockNames.PYLON);
		setLightLevel(0.5F);

		float f = 1F / 16F * 2F;
		setBlockBounds(f, 0F, f, 1F - f, 1F / 16F * 21F, 1F - f);
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
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		// NO-OP
	}

	@Override
	public int damageDropped(int par1) {
		return par1;
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2, List par3) {
		for(int i = 0; i < 3; i++)
			par3.add(new ItemStack(par1, 1, i));
	}

	@Override
	public IIcon getIcon(int par1, int par2) {
		return par2 == 0 ? Blocks.diamond_block.getIcon(0, 0) : ModBlocks.storage.getIcon(0, par2);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return LibRenderIDs.idPylon;
	}

	@Override
	public float getEnchantPowerBonus(World world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z) == 0 ? 8 : 15;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TilePylon();
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		int meta = world.getBlockMetadata(x, y, z);
		return meta == 0 ? LexiconData.pylon : meta == 1 ? LexiconData.alfhomancyIntro : LexiconData.gaiaRitual;
	}
	@Override
	public boolean canStabaliseInfusion(World world, int x, int y, int z) {
		return ConfigHandler.enableThaumcraftStablizers;
	}
}
