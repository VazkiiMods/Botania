/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.decor;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.TileManaFlame;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockManaFlame extends BlockMod implements BlockEntityProvider {

	private static final VoxelShape SHAPE = createCuboidShape(4, 4, 4, 12, 12, 12);

	public BlockManaFlame(Settings builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
		return SHAPE;
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (Botania.gardenOfGlassLoaded) {
			ItemStack stack = player.getStackInHand(hand);
			if (!stack.isEmpty() && ItemTags.SAPLINGS.contains(stack.getItem()) && !player.inventory.contains(new ItemStack(ModItems.lexicon))) {
				if (!world.isClient) {
					stack.decrement(1);
					player.inventory.offerOrDrop(player.world, new ItemStack(ModItems.lexicon));
				}
				return ActionResult.SUCCESS;
			}

		}
		return ActionResult.PASS;
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rand) {
		BlockEntity te = world.getBlockEntity(pos);
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
			world.addParticle(data, x, y, z, 0, m, 0);
		}
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rand) {
		BlockEntity te = world.getBlockEntity(pos);
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
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileManaFlame();
	}
}
