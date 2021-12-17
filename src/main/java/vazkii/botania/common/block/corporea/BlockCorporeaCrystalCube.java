/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.corporea;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.corporea.TileCorporeaBase;
import vazkii.botania.common.block.tile.corporea.TileCorporeaCrystalCube;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class BlockCorporeaCrystalCube extends BlockModWaterloggable implements ITileEntityProvider, IWandable, IWandHUD {

	private static final VoxelShape SHAPE = makeCuboidShape(3.0, 0, 3.0, 13.0, 16, 13.0);

	public BlockCorporeaCrystalCube(Properties builder) {
		super(builder);
	}

	@Override
	public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		if (!world.isRemote) {
			TileCorporeaCrystalCube cube = (TileCorporeaCrystalCube) world.getTileEntity(pos);
			cube.doRequest(player.isSneaking());
		}
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return SHAPE;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		ItemStack stack = player.getHeldItem(hand);
		if (!stack.isEmpty()) {
			if (stack.getItem() == ModItems.twigWand && player.isSneaking()) {
				return ActionResultType.PASS;
			}
			TileCorporeaCrystalCube cube = (TileCorporeaCrystalCube) world.getTileEntity(pos);
			if (cube.locked) {
				if (!world.isRemote) {
					player.sendStatusMessage(new TranslationTextComponent("botaniamisc.crystalCubeLocked"), false);
				}
			} else {
				cube.setRequestTarget(stack);
			}
			return ActionResultType.SUCCESS;
		} else {
			if (player.isSneaking()) {
				TileCorporeaCrystalCube cube = (TileCorporeaCrystalCube) world.getTileEntity(pos);
				cube.locked = !cube.locked;
				if (!world.isRemote) {
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(cube);
				}
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(MatrixStack ms, Minecraft mc, World world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileCorporeaCrystalCube) {
			((TileCorporeaCrystalCube) te).renderHUD(ms, mc);
		}
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		TileEntity te = world.getTileEntity(pos);
		return te instanceof TileCorporeaCrystalCube && ((TileCorporeaCrystalCube) te).onUsedByWand(player, stack, world, pos, side);
	}

	@Nonnull
	@Override
	public TileCorporeaBase createNewTileEntity(@Nonnull IBlockReader world) {
		return new TileCorporeaCrystalCube();
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
		return ((TileCorporeaCrystalCube) world.getTileEntity(pos)).getComparatorValue();
	}
}
