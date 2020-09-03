/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.decor;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.TileManaFlame;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockManaFlame extends BlockMod implements ITileEntityProvider {

	private static final VoxelShape SHAPE = makeCuboidShape(4, 4, 4, 12, 12, 12);

	public BlockManaFlame(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return SHAPE;
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (Botania.gardenOfGlassLoaded) {
			ItemStack stack = player.getHeldItem(hand);
			if (!stack.isEmpty() && ItemTags.SAPLINGS.contains(stack.getItem()) && !player.inventory.hasItemStack(new ItemStack(ModItems.lexicon))) {
				if (!world.isRemote) {
					stack.shrink(1);
					player.inventory.placeItemBackInInventory(player.world, new ItemStack(ModItems.lexicon));
				}
				return ActionResultType.SUCCESS;
			}

		}
		return ActionResultType.PASS;
	}

	@Override
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileManaFlame) {
			int color = ((TileManaFlame) te).getColor();
			float v = 0.1F;

			float r = (float) (color >> 16 & 0xFF) / 0xFF;
			float g = (float) (color >> 8 & 0xFF) / 0xFF;
			float b = (float) (color & 0xFF) / 0xFF;

			double luminance = 0.2126 * r + 0.7152 * g + 0.0722 * b; // Standard relative luminance calculation

			if (luminance < v) {
				r += (float) Math.random() * 0.125F;
				g += (float) Math.random() * 0.125F;
				b += (float) Math.random() * 0.125F;
			}

			float w = 0.15F;
			float h = 0.05F;
			double x = pos.getX() + 0.5 + (Math.random() - 0.5) * w;
			double y = pos.getY() + 0.25 + (Math.random() - 0.5) * h;
			double z = pos.getZ() + 0.5 + (Math.random() - 0.5) * w;

			float s = 0.2F + (float) Math.random() * 0.1F;
			float m = 0.03F + (float) Math.random() * 0.015F;

			WispParticleData data = WispParticleData.wisp(s, r, g, b, 1);
			Botania.proxy.addParticleForceNear(world, data, x, y, z, 0, m, 0);
		}
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
		return new TileManaFlame();
	}
}
