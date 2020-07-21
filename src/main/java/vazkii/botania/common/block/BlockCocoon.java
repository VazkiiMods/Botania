/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.tile.TileCocoon;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class BlockCocoon extends BlockModWaterloggable implements BlockEntityProvider {

	private static final VoxelShape SHAPE = createCuboidShape(3, 0, 3, 13, 14, 13);;

	protected BlockCocoon(Settings builder) {
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
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity e) {
		if (!world.isClient && e instanceof ItemEntity) {
			ItemEntity item = (ItemEntity) e;
			ItemStack stack = item.getStack();
			addStack(world, pos, stack, false);

			if (stack.isEmpty()) {
				item.remove();
			}
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);
		return addStack(world, pos, stack, player.abilities.creativeMode);
	}

	private ActionResult addStack(World world, BlockPos pos, ItemStack stack, boolean creative) {
		TileCocoon cocoon = (TileCocoon) world.getBlockEntity(pos);
		Item item = stack.getItem();

		if (cocoon != null && (item == Items.EMERALD || item == Items.CHORUS_FRUIT || item == ModItems.lifeEssence)) {
			if (!world.isClient) {
				if (item == Items.EMERALD && cocoon.emeraldsGiven < TileCocoon.MAX_EMERALDS) {
					if (!creative) {
						stack.decrement(1);
					}
					cocoon.emeraldsGiven++;
					((ServerWorld) world).spawnParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 1, 0.1, 0.05, 0.1, 0.5);
				} else if (item == Items.CHORUS_FRUIT && cocoon.chorusFruitGiven < TileCocoon.MAX_CHORUS_FRUITS) {
					if (!creative) {
						stack.decrement(1);
					}
					cocoon.chorusFruitGiven++;
					((ServerWorld) world).spawnParticles(ParticleTypes.PORTAL, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 32, 0, 0, 0, 0.5);
				} else if (item == ModItems.lifeEssence && !cocoon.gaiaSpiritGiven) {
					if (!creative) {
						stack.decrement(1);
					}
					cocoon.forceRare();
					WispParticleData data = WispParticleData.wisp(0.6F, 0F, 1F, 0F);
					((ServerWorld) world).spawnParticles(data, pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5, 8, 0.1, 0.1, 0.1, 0.04);
				}
			}

			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileCocoon();
	}

}
