/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 7, 2014, 2:25:22 PM (GMT)]
 */
package vazkii.botania.common.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.PlatformVariant;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.tile.TileCamo;
import vazkii.botania.common.block.tile.TilePlatform;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockPlatform extends BlockCamo implements ILexiconable, IWandable {

	public BlockPlatform() {
		super(Material.wood);
		setHardness(2.0F);
		setResistance(5.0F);
		setStepSound(Block.soundTypeWood);
		setUnlocalizedName(LibBlockNames.PLATFORM);
		setDefaultState(((IExtendedBlockState) blockState.getBaseState())
				.withProperty(BotaniaStateProps.HELD_STATE, null)
				.withProperty(BotaniaStateProps.PLATFORM_VARIANT, PlatformVariant.ABSTRUSE));
	}

	@Override
	public BlockState createBlockState() {
		return new ExtendedBlockState(this, new IProperty[] { BotaniaStateProps.PLATFORM_VARIANT, }, new IUnlistedProperty[] { BotaniaStateProps.HELD_STATE });
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.PLATFORM_VARIANT).ordinal();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta > PlatformVariant.values().length) {
			meta = 0;
		}
		return getDefaultState().withProperty(BotaniaStateProps.PLATFORM_VARIANT, PlatformVariant.values()[meta]);
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (world.getTileEntity(pos) instanceof TileCamo) {
			TileCamo tile = ((TileCamo) world.getTileEntity(pos));
			return ((IExtendedBlockState) state).withProperty(BotaniaStateProps.HELD_STATE, tile.camoState);
		} else {
			return ((IExtendedBlockState) state);
		}
	}

	@Override
	public boolean canRenderInLayer(EnumWorldBlockLayer layer) {
		return true;
	}

	@Override
	protected boolean shouldRegisterInNameSet() {
		return false;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public Block setUnlocalizedName(String par1Str) {
		GameRegistry.registerBlock(this, ItemBlockWithMetadataAndName.class, par1Str);
		return super.setUnlocalizedName(par1Str);
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		for(int i = 0; i < PlatformVariant.values().length; i++)
			par3List.add(new ItemStack(par1, 1, i));
	}

	@Override
	public void addCollisionBoxesToList(World par1World, BlockPos pos, IBlockState state, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
		PlatformVariant variant = state.getValue(BotaniaStateProps.PLATFORM_VARIANT);
		if(variant == PlatformVariant.INFRANGIBLE || variant == PlatformVariant.ABSTRUSE && par7Entity != null && par7Entity.posY > pos.getY() + 0.9 && (!(par7Entity instanceof EntityPlayer) || !par7Entity.isSneaking()))
			super.addCollisionBoxesToList(par1World, pos, state, par5AxisAlignedBB, par6List, par7Entity);
	}

	@Override
	public float getBlockHardness(World par1World, BlockPos pos) {
		PlatformVariant variant = par1World.getBlockState(pos).getValue(BotaniaStateProps.PLATFORM_VARIANT);
		return variant == PlatformVariant.INFRANGIBLE ? -1F : super.getBlockHardness(par1World, pos);
	}

	@Override
	public TileCamo createNewTileEntity(World world, int meta) {
		return new TilePlatform();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		PlatformVariant variant = world.getBlockState(pos).getValue(BotaniaStateProps.PLATFORM_VARIANT);
		return variant == PlatformVariant.ABSTRUSE ? LexiconData.platform : variant == PlatformVariant.INFRANGIBLE ? null : LexiconData.spectralPlatform;
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
		TilePlatform tile = (TilePlatform) world.getTileEntity(pos);
		return tile.onWanded(player);
	}

}