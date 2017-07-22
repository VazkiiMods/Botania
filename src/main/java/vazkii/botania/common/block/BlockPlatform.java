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
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.PlatformVariant;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.block.tile.TileCamo;
import vazkii.botania.common.block.tile.TilePlatform;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockPlatform extends BlockCamo implements ILexiconable, IWandable, IManaCollisionGhost {

	public BlockPlatform() {
		super(Material.WOOD, LibBlockNames.PLATFORM);
		setHardness(2.0F);
		setResistance(5.0F);
		setSoundType(SoundType.WOOD);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.PLATFORM_VARIANT, PlatformVariant.ABSTRUSE));
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[] { BotaniaStateProps.PLATFORM_VARIANT, },
				new IUnlistedProperty[] { BotaniaStateProps.HELD_STATE, BotaniaStateProps.HELD_WORLD, BotaniaStateProps.HELD_POS });
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.PLATFORM_VARIANT).ordinal();
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta > PlatformVariant.values().length) {
			meta = 0;
		}
		return getDefaultState().withProperty(BotaniaStateProps.PLATFORM_VARIANT, PlatformVariant.values()[meta]);
	}

	@Nonnull
	@Override
	public IBlockState getExtendedState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
		state = ((IExtendedBlockState) state).withProperty(BotaniaStateProps.HELD_WORLD, world)
				.withProperty(BotaniaStateProps.HELD_POS, pos);

		TileEntity te = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
		if (te instanceof TileCamo) {
			TileCamo tile = (TileCamo) te;
			return ((IExtendedBlockState) state).withProperty(BotaniaStateProps.HELD_STATE, tile.camoState);
		} else {
			return state;
		}
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, @Nonnull BlockRenderLayer layer) {
		return true;
	}


	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> stacks) {
		for(int i = 0; i < PlatformVariant.values().length; i++)
			stacks.add(new ItemStack(this, 1, i));
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB par5AxisAlignedBB, @Nonnull List<AxisAlignedBB> stacks, Entity par7Entity, boolean isActualState) {
		PlatformVariant variant = state.getValue(BotaniaStateProps.PLATFORM_VARIANT);
		if(variant == PlatformVariant.INFRANGIBLE || variant == PlatformVariant.ABSTRUSE && par7Entity != null && par7Entity.posY > pos.getY() + 0.9 && (!(par7Entity instanceof EntityPlayer) || !par7Entity.isSneaking()))
			super.addCollisionBoxToList(state, world, pos, par5AxisAlignedBB, stacks, par7Entity, isActualState);
	}

	@Override
	public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
		PlatformVariant variant = world.getBlockState(pos).getValue(BotaniaStateProps.PLATFORM_VARIANT);
		return variant == PlatformVariant.INFRANGIBLE ? -1F : super.getBlockHardness(state, world, pos);
	}
	
	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
		return state.getValue(BotaniaStateProps.PLATFORM_VARIANT) != PlatformVariant.INFRANGIBLE;
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, @Nonnull Entity exploder, Explosion explosion) {
		return world.getBlockState(pos).getValue(BotaniaStateProps.PLATFORM_VARIANT) != PlatformVariant.INFRANGIBLE ? super.getExplosionResistance(world, pos, exploder, explosion) : Float.MAX_VALUE;
	}
	
	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
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

	@Override
	public boolean isGhost(IBlockState state, World world, BlockPos pos) {
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerBlockToState(this, 3);
		// Statemapper after item registration, because the items should be registered to the actual variant names
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(BotaniaStateProps.PLATFORM_VARIANT).build());

	}
}