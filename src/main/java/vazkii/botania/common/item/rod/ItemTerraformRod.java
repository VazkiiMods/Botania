/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.rod;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

public class ItemTerraformRod extends Item implements IManaUsingItem, IBlockProvider {
	private static final int COST_PER = 55;

	public ItemTerraformRod(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public void onUse(@Nonnull World world, @Nonnull LivingEntity living, @Nonnull ItemStack stack, int count) {
		if (count != getUseDuration(stack) && count % 10 == 0 && living instanceof PlayerEntity) {
			terraform(stack, world, (PlayerEntity) living);
		}
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		player.setActiveHand(hand);
		return ActionResult.resultSuccess(player.getHeldItem(hand));
	}

	private void terraform(ItemStack stack, World world, PlayerEntity player) {
		int range = IManaProficiencyArmor.hasProficiency(player, stack) ? 22 : 16;

		BlockPos startCenter = player.func_233580_cy_().down();

		if (startCenter.getY() < world.getSeaLevel()) // Not below sea level
		{
			return;
		}

		List<CoordsWithBlock> blocks = new ArrayList<>();

		for (BlockPos pos : BlockPos.getAllInBoxMutable(startCenter.add(-range, -range, -range), startCenter.add(range, range, range))) {
			BlockState state = world.getBlockState(pos);
			if (state.isAir(world, pos)) {
				continue;
			}

			if (ModTags.Blocks.TERRAFORMABLE.contains(state.getBlock())) {
				List<BlockPos> airBlocks = new ArrayList<>();

				for (Direction dir : Direction.Plane.HORIZONTAL) {
					BlockPos pos_ = pos.offset(dir);
					BlockState state_ = world.getBlockState(pos_);
					Block block_ = state_.getBlock();
					if (state_.isAir(world, pos_) || state_.getMaterial().isReplaceable()
							|| block_ instanceof FlowerBlock && !block_.isIn(ModTags.Blocks.SPECIAL_FLOWERS)
							|| block_ instanceof DoublePlantBlock) {
						airBlocks.add(pos_);
					}
				}

				if (!airBlocks.isEmpty()) {
					if (pos.getY() > startCenter.getY()) {
						blocks.add(new CoordsWithBlock(pos, Blocks.AIR));
					} else {
						for (BlockPos coords : airBlocks) {
							if (!world.isAirBlock(coords.down())) {
								blocks.add(new CoordsWithBlock(coords, Blocks.DIRT));
							}
						}
					}
				}
			}
		}

		int cost = COST_PER * blocks.size();

		if (world.isRemote || ManaItemHandler.instance().requestManaExactForTool(stack, player, cost, true)) {
			if (!world.isRemote) {
				for (CoordsWithBlock block : blocks) {
					world.setBlockState(block, block.block.getDefaultState());
				}
			}

			if (!blocks.isEmpty()) {
				for (int i = 0; i < 10; i++) {
					world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.BLOCK_SAND_STEP, SoundCategory.BLOCKS, 1F, 0.4F);
				}
				SparkleParticleData data = SparkleParticleData.sparkle(2F, 0.35F, 0.2F, 0.05F, 5);
				for (int i = 0; i < 120; i++) {
					world.addParticle(data, startCenter.getX() - range + range * 2 * Math.random(), startCenter.getY() + 2 + (Math.random() - 0.5) * 2, startCenter.getZ() - range + range * 2 * Math.random(), 0, 0, 0);
				}
			}
		}
	}

	private static class CoordsWithBlock extends BlockPos {

		private final Block block;

		private CoordsWithBlock(BlockPos pos, Block block) {
			super(pos);
			this.block = block;
		}

	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public boolean provideBlock(PlayerEntity player, ItemStack requestor, ItemStack stack, Block block, boolean doit) {
		if (block == Blocks.DIRT) {
			return !doit || ManaItemHandler.instance().requestManaExactForTool(requestor, player, ItemDirtRod.COST, true);
		}
		return false;
	}

	@Override
	public int getBlockCount(PlayerEntity player, ItemStack requestor, ItemStack stack, Block block) {
		if (block == Blocks.DIRT) {
			return -1;
		}
		return 0;
	}
}
