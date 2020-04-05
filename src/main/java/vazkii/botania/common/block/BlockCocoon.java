/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.tile.TileCocoon;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class BlockCocoon extends BlockMod {

	private static final VoxelShape SHAPE = makeCuboidShape(3, 0, 3, 13, 14, 13);;

	protected BlockCocoon(Properties builder) {
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
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity e) {
		if (!world.isRemote && e instanceof ItemEntity) {
			ItemEntity item = (ItemEntity) e;
			ItemStack stack = item.getItem();
			addStack(world, pos, stack, false);

			if (stack.isEmpty()) {
				item.remove();
			}
		}
	}

	@Override
	public ActionResultType onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		ItemStack stack = player.getHeldItem(hand);
		return addStack(world, pos, stack, player.abilities.isCreativeMode);
	}

	private ActionResultType addStack(World world, BlockPos pos, ItemStack stack, boolean creative) {
		TileCocoon cocoon = (TileCocoon) world.getTileEntity(pos);
		Item item = stack.getItem();

		if(cocoon != null && (item == Items.EMERALD || item == Items.CHORUS_FRUIT || item == ModItems.lifeEssence)) {
			if(!world.isRemote) {
				if(item == Items.EMERALD && cocoon.emeraldsGiven < TileCocoon.MAX_EMERALDS) {
					if(!creative) {
						stack.shrink(1);
					}
					cocoon.emeraldsGiven++;
					((ServerWorld) world).spawnParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 1, 0.1, 0.05, 0.1, 0.5);
				} else if(item == Items.CHORUS_FRUIT && cocoon.chorusFruitGiven < TileCocoon.MAX_CHORUS_FRUITS) {
					if(!creative) {
						stack.shrink(1);
					}
					cocoon.chorusFruitGiven++;
					((ServerWorld) world).spawnParticle(ParticleTypes.PORTAL, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 32, 0, 0, 0, 0.5);
				} else if (item == ModItems.lifeEssence && !cocoon.gaiaSpiritGiven) {
					if(!creative) {
						stack.shrink(1);
					}
					cocoon.forceRare();
					WispParticleData data = WispParticleData.wisp(0.6F, 0F, 1F, 0F);
					((ServerWorld) world).spawnParticle(data, pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5, 8, 0.1, 0.1, 0.1, 0.04);
				}
			}

			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
		return new TileCocoon();
	}

}
