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

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.registry.GameRegistry;
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

import java.util.List;

public class BlockPlatform extends BlockCamo implements ILexiconable, IWandable {

	public BlockPlatform() {
		super(Material.wood, LibBlockNames.PLATFORM);
		setHardness(2.0F);
		setResistance(5.0F);
		setSoundType(SoundType.WOOD);
		setDefaultState(((IExtendedBlockState) blockState.getBaseState())
				.withProperty(BotaniaStateProps.HELD_STATE, null)
				.withProperty(BotaniaStateProps.HELD_WORLD, null)
				.withProperty(BotaniaStateProps.HELD_POS, null)
				.withProperty(BotaniaStateProps.PLATFORM_VARIANT, PlatformVariant.ABSTRUSE));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[] { BotaniaStateProps.PLATFORM_VARIANT, },
				new IUnlistedProperty[] { BotaniaStateProps.HELD_STATE, BotaniaStateProps.HELD_WORLD, BotaniaStateProps.HELD_POS });
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
		state = ((IExtendedBlockState) state).withProperty(BotaniaStateProps.HELD_WORLD, world)
				.withProperty(BotaniaStateProps.HELD_POS, pos);

		if (world.getTileEntity(pos) instanceof TileCamo) {
			TileCamo tile = ((TileCamo) world.getTileEntity(pos));
			return ((IExtendedBlockState) state).withProperty(BotaniaStateProps.HELD_STATE, tile.camoState);
		} else {
			return state;
		}
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return true;
	}


	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public void registerItemForm() {
		GameRegistry.register(new ItemBlockWithMetadataAndName(this), getRegistryName());
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		for(int i = 0; i < PlatformVariant.values().length; i++)
			par3List.add(new ItemStack(par1, 1, i));
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World par1World, BlockPos pos, AxisAlignedBB par5AxisAlignedBB, List<AxisAlignedBB> par6List, Entity par7Entity) {
		PlatformVariant variant = state.getValue(BotaniaStateProps.PLATFORM_VARIANT);
		if(variant == PlatformVariant.INFRANGIBLE || variant == PlatformVariant.ABSTRUSE && par7Entity != null && par7Entity.posY > pos.getY() + 0.9 && (!(par7Entity instanceof EntityPlayer) || !par7Entity.isSneaking()))
			super.addCollisionBoxToList(state, par1World, pos, par5AxisAlignedBB, par6List, par7Entity);
	}

	@Override
	public float getBlockHardness(IBlockState state, World par1World, BlockPos pos) {
		PlatformVariant variant = par1World.getBlockState(pos).getValue(BotaniaStateProps.PLATFORM_VARIANT);
		return variant == PlatformVariant.INFRANGIBLE ? -1F : super.getBlockHardness(state, par1World, pos);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
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