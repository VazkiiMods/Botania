/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 30, 2015, 3:56:19 PM (GMT)]
 */
package vazkii.botania.common.block.corporea;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.StorageVariant;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.corporea.TileCorporeaBase;
import vazkii.botania.common.block.tile.corporea.TileCorporeaCrystalCube;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockCorporeaCrystalCube extends BlockCorporeaBase implements ILexiconable {

	public BlockCorporeaCrystalCube() {
		super(Material.iron, LibBlockNames.CORPOREA_CRYSTAL_CUBE);
		setHardness(5.5F);
		setStepSound(soundTypeMetal);
		float f = (1F - 10F / 16F) / 2F;
		setBlockBounds(f, 0F, f, 1F - f, 1F, 1F - f);
		setDefaultState(((IExtendedBlockState) blockState.getBaseState()).withProperty(Properties.AnimationProperty, null).withProperty(Properties.StaticProperty, true));
	}

	@Override
	public BlockState createBlockState() {
		return new ExtendedBlockState(this, new IProperty[] { Properties.StaticProperty }, new IUnlistedProperty[] { Properties.AnimationProperty } );
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState();
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.withProperty(Properties.StaticProperty, true);
	}

	@Override
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
		if(!world.isRemote) {
			TileCorporeaCrystalCube cube = (TileCorporeaCrystalCube) world.getTileEntity(pos);
			cube.doRequest(player.isSneaking());
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing s, float xs, float ys, float zs) {
		ItemStack stack = player.getCurrentEquippedItem();

		if(stack != null) {
			TileCorporeaCrystalCube cube = (TileCorporeaCrystalCube) world.getTileEntity(pos);
			cube.setRequestTarget(stack);
			return true;
		}
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	public TileCorporeaBase createNewTileEntity(World world, int meta) {
		return new TileCorporeaCrystalCube();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.corporeaCrystalCube;
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, BlockPos pos) {
		return ((TileCorporeaCrystalCube) world.getTileEntity(pos)).compValue;
	}

}
