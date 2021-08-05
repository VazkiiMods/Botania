/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import vazkii.botania.common.block.tile.TileCocoon;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class BlockCocoon extends BlockModWaterloggable implements EntityBlock {

	private static final VoxelShape SHAPE = box(3, 0, 3, 13, 14, 13);;

	protected BlockCocoon(Properties builder) {
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
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity e) {
		if (!world.isClientSide && e instanceof ItemEntity) {
			ItemEntity item = (ItemEntity) e;
			ItemStack stack = item.getItem();
			addStack(world, pos, stack, false);

			if (stack.isEmpty()) {
				item.discard();
			}
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);
		return addStack(world, pos, stack, player.getAbilities().instabuild);
	}

	private InteractionResult addStack(Level world, BlockPos pos, ItemStack stack, boolean creative) {
		TileCocoon cocoon = (TileCocoon) world.getBlockEntity(pos);
		Item item = stack.getItem();

		if (cocoon != null && (item == Items.EMERALD || item == Items.CHORUS_FRUIT || item == ModItems.lifeEssence)) {
			if (!world.isClientSide) {
				if (item == Items.EMERALD && cocoon.emeraldsGiven < TileCocoon.MAX_EMERALDS) {
					if (!creative) {
						stack.shrink(1);
					}
					cocoon.emeraldsGiven++;
					((ServerLevel) world).sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 1, 0.1, 0.05, 0.1, 0.5);
				} else if (item == Items.CHORUS_FRUIT && cocoon.chorusFruitGiven < TileCocoon.MAX_CHORUS_FRUITS) {
					if (!creative) {
						stack.shrink(1);
					}
					cocoon.chorusFruitGiven++;
					((ServerLevel) world).sendParticles(ParticleTypes.PORTAL, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 32, 0, 0, 0, 0.5);
				} else if (item == ModItems.lifeEssence && !cocoon.gaiaSpiritGiven) {
					if (!creative) {
						stack.shrink(1);
					}
					cocoon.forceRare();
					WispParticleData data = WispParticleData.wisp(0.6F, 0F, 1F, 0F);
					((ServerLevel) world).sendParticles(data, pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5, 8, 0.1, 0.1, 0.1, 0.04);
				}
			}

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileCocoon(pos, state);
	}

}
