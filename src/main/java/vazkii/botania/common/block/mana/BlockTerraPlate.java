/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 8, 2014, 5:25:12 PM (GMT)]
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.TileTerraPlate;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;

public class BlockTerraPlate extends BlockMod implements ILexiconable {

	private static final VoxelShape SHAPE = makeCuboidShape(0, 0, 0, 16, 3, 16);

	public BlockTerraPlate(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return SHAPE;
	}

	@Override
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, Direction s, float xs, float ys, float zs) {
		ItemStack stack = player.getHeldItem(hand);
		if(!stack.isEmpty()
				&& (stack.getItem() == ModItems.manaSteel || stack.getItem() == ModItems.manaPearl || stack.getItem() == ModItems.manaDiamond)) {
			if(!world.isRemote) {
				ItemStack target = stack.split(1);
				ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, target);
				item.setPickupDelay(40);
				item.motionX = item.motionY = item.motionZ = 0;
				world.spawnEntity(item);
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean isFullCube(BlockState state) {
		return false;
	}

	@Override
	public boolean allowsMovement(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, PathType type) {
		return false;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
		return new TileTerraPlate();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, PlayerEntity player, ItemStack lexicon) {
		return LexiconData.terrasteel;
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
		TileTerraPlate plate = (TileTerraPlate) world.getTileEntity(pos);
		int val = (int) ((double) plate.getCurrentMana() / (double) TileTerraPlate.MAX_MANA * 15.0);
		if(plate.getCurrentMana() > 0)
			val = Math.max(val, 1);

		return val;
	}

}
