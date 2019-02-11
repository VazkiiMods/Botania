/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 8, 2015, 4:29:01 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.Particles;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.tile.TileCocoon;
import vazkii.botania.common.lexicon.LexiconData;

import javax.annotation.Nonnull;

public class BlockCocoon extends BlockMod implements ILexiconable {

	private static final VoxelShape SHAPE = makeCuboidShape(3, 0, 3, 13, 14, 13);;

	protected BlockCocoon(Builder builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader world, BlockPos pos) {
		return SHAPE;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Nonnull
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	public void onEntityCollision(IBlockState state, World world, BlockPos pos, Entity e) {
		if(!world.isRemote && e instanceof EntityItem) {
			EntityItem item = (EntityItem) e;
			ItemStack stack = item.getItem();
			addStack(world, pos, stack, false);
			
			if(stack.isEmpty())
				item.remove();
		}
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing s, float xs, float ys, float zs) {
		ItemStack stack = player.getHeldItem(hand);
		return addStack(world, pos, stack, player.abilities.isCreativeMode);
	}
	
	private boolean addStack(World world, BlockPos pos, ItemStack stack, boolean creative) {
		TileCocoon cocoon = (TileCocoon) world.getTileEntity(pos);
		Item item = stack.getItem();
		
		if(cocoon != null && (item == Items.EMERALD || item == Items.CHORUS_FRUIT)) {
			if(!world.isRemote) {
				if(item == Items.EMERALD && cocoon.emeraldsGiven < TileCocoon.MAX_EMERALDS) {
					if(!creative)
						stack.shrink(1);
					cocoon.emeraldsGiven++;
					((WorldServer) world).spawnParticle(Particles.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 1, 0, 0, 0, 0.5);
				} else if(item == Items.CHORUS_FRUIT && cocoon.chorusFruitGiven < TileCocoon.MAX_CHORUS_FRUITS) {
					if(!creative)
						stack.shrink(1);
					cocoon.chorusFruitGiven++;
					((WorldServer) world).spawnParticle(Particles.PORTAL, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 32, 0, 0, 0, 0.5);
				}
			}

			return true;
		}
		
		return false;
	}

	@Nonnull
	@Override
	public Item getItemDropped(IBlockState state, World world, BlockPos pos, int fortune) {
		return Items.AIR;
	}

	@Override
	public boolean canSilkHarvest(@Nonnull IBlockState state, IWorldReader world, BlockPos pos, EntityPlayer player) {
		return false;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull IBlockState state, @Nonnull IBlockReader world) {
		return new TileCocoon();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.cocoon;
	}

	@Nonnull
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader world, IBlockState state, BlockPos pos, EnumFacing side) {
		return side.getAxis() == EnumFacing.Axis.Y ? BlockFaceShape.CENTER_BIG : BlockFaceShape.MIDDLE_POLE_THICK;
	}

}
