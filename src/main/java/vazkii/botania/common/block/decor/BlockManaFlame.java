/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.decor;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.TileManaFlame;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockManaFlame extends BlockMod implements EntityBlock {

	private static final VoxelShape SHAPE = box(4, 4, 4, 12, 12, 12);

	public BlockManaFlame(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Nonnull
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (Botania.gardenOfGlassLoaded) {
			ItemStack stack = player.getItemInHand(hand);
			if (!stack.isEmpty() && ItemTags.SAPLINGS.contains(stack.getItem()) && !player.inventory.contains(new ItemStack(ModItems.lexicon))) {
				if (!world.isClientSide) {
					stack.shrink(1);
					player.inventory.placeItemBackInInventory(player.level, new ItemStack(ModItems.lexicon));
				}
				return InteractionResult.SUCCESS;
			}

		}
		return InteractionResult.PASS;
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
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

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockGetter world) {
		return new TileManaFlame();
	}
}
