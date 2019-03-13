/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 17, 2014, 5:31:53 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.subtile.ISpecialFlower;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.decor.BlockFloatingFlower;
import vazkii.botania.common.block.tile.TileFloatingSpecialFlower;
import vazkii.botania.common.block.tile.TileSpecialFlower;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockFloatingSpecialFlower extends BlockFloatingFlower implements ISpecialFlower, IWandable, ILexiconable, IWandHUD {

	public BlockFloatingSpecialFlower(Properties props) {
		super(EnumDyeColor.WHITE, props);
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[] { BotaniaStateProps.COLOR }, new IUnlistedProperty[] { BotaniaStateProps.SUBTILE_ID, BotaniaStateProps.ISLAND_TYPE });
	}

	@Nonnull
	@Override
	public IBlockState getExtendedState(@Nonnull IBlockState state, IBlockReader world, BlockPos pos) {
		state = super.getExtendedState(state, world, pos);
		TileEntity te = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
		if (te instanceof TileFloatingSpecialFlower) {
			state = ((IExtendedBlockState) state).with(BotaniaStateProps.SUBTILE_ID, ((TileFloatingSpecialFlower) te).subTileName);
		}
		return state;
	}

	@Override
	public int getLightValue(@Nonnull IBlockState state, IWorldReader world, @Nonnull BlockPos pos) {
		if(world.getBlockState(pos).getBlock() != this)
			return world.getBlockState(pos).getLightValue(world, pos);

		TileEntity tile = world.getTileEntity(pos);
		return tile instanceof TileSpecialFlower ? ((TileSpecialFlower) tile).getLightValue() : lightValue;
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		return ((TileSpecialFlower) world.getTileEntity(pos)).getComparatorInputOverride();
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockReader world, BlockPos pos, EnumFacing side) {
		return ((TileSpecialFlower) world.getTileEntity(pos)).getPowerLevel(side);
	}

	@Override
	public int getStrongPower(IBlockState state, IBlockReader world, BlockPos pos, EnumFacing side) {
		return getWeakPower(state, world, pos, side);
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(IBlockState state, World world, BlockPos pos, Random rand) {}

	@Override
	public void fillItemGroup(ItemGroup tab, NonNullList<ItemStack> stacks) {
		for(ResourceLocation s : BotaniaAPI.subtilesForCreativeMenu) {
			stacks.add(ItemBlockSpecialFlower.ofType(new ItemStack(this), s));
			if(BotaniaAPI.miniFlowers.containsKey(s))
				stacks.add(ItemBlockSpecialFlower.ofType(new ItemStack(this), BotaniaAPI.miniFlowers.get(s)));
		}
	}

	@Nonnull
	@Override
	public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull IBlockReader world, @Nonnull BlockPos pos, EntityPlayer player) {
		ResourceLocation name = ((TileSpecialFlower) world.getTileEntity(pos)).subTileName;
		return ItemBlockSpecialFlower.ofType(new ItemStack(state.getBlock()), name);
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		((TileSpecialFlower) world.getTileEntity(pos)).onBlockHarvested(world, pos, state, player);
	}

	@Override
	public boolean removedByPlayer(@Nonnull IBlockState state, World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player, boolean willHarvest, IFluidState fluid) {
		if (willHarvest) {
			// Copy of super.removedByPlayer but don't set to air yet
			// This is so getDrops below will have a TE to work with
			onBlockHarvested(world, pos, state, player);
			return true;
		} else {
			return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
		}
	}

	@Override
	public void harvestBlock(@Nonnull World world, EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, TileEntity te, ItemStack stack) {
		super.harvestBlock(world, player, pos, state, te, stack);
		// Now delete the block and TE
		world.removeBlock(pos);
	}

	@Override
	public void getDrops(@Nonnull IBlockState state, NonNullList<ItemStack> list, World world, BlockPos pos, int fortune) {
		TileEntity tile = world.getTileEntity(pos);

		if(tile != null) {
			ResourceLocation name = ((TileSpecialFlower) tile).subTileName;
			list.add(ItemBlockSpecialFlower.ofType(new ItemStack(state.getBlock()), name));
			((TileSpecialFlower) tile).getDrops(list);
		}
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
		return ((TileSpecialFlower) world.getTileEntity(pos)).onWanded(stack, player);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
		((TileSpecialFlower) world.getTileEntity(pos)).onBlockPlacedBy(world, pos, state, entity, stack);
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		return ((TileSpecialFlower) world.getTileEntity(pos)).onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ) || super.onBlockActivated(state, world, pos, player, hand, side, hitX, hitY, hitZ);
	}

	@Override
	public void onBlockAdded(IBlockState state, World world, BlockPos pos, IBlockState oldState) {
		((TileSpecialFlower) world.getTileEntity(pos)).onBlockAdded(world, pos, state);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(Minecraft mc, World world, BlockPos pos) {
		((TileSpecialFlower) world.getTileEntity(pos)).renderHUD(mc);
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull IBlockState state, @Nonnull IBlockReader world) {
		return new TileFloatingSpecialFlower();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return ((TileSpecialFlower) world.getTileEntity(pos)).getEntry();
	}
}
