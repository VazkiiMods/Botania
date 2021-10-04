/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.rod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.core.handler.ModSounds;
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
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public void onUseTick(@Nonnull Level world, @Nonnull LivingEntity living, @Nonnull ItemStack stack, int count) {
		if (count != getUseDuration(stack) && count % 10 == 0 && living instanceof Player) {
			terraform(stack, world, (Player) living);
		}
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		return ItemUtils.startUsingInstantly(world, player, hand);
	}

	private void terraform(ItemStack stack, Level world, Player player) {
		int range = IManaProficiencyArmor.hasProficiency(player, stack) ? 22 : 16;

		BlockPos startCenter = player.blockPosition().below();

		if (startCenter.getY() < world.getSeaLevel()) // Not below sea level
		{
			return;
		}

		List<CoordsWithBlock> blocks = new ArrayList<>();

		for (BlockPos pos : BlockPos.betweenClosed(startCenter.offset(-range, -range, -range), startCenter.offset(range, range, range))) {
			BlockState state = world.getBlockState(pos);
			if (state.isAir()) {
				continue;
			}

			if (ModTags.Blocks.TERRAFORMABLE.contains(state.getBlock())) {
				List<BlockPos> airBlocks = new ArrayList<>();

				for (Direction dir : Direction.Plane.HORIZONTAL) {
					BlockPos pos_ = pos.relative(dir);
					BlockState state_ = world.getBlockState(pos_);
					Block block_ = state_.getBlock();
					if (state_.isAir() || state_.getMaterial().isReplaceable()
							|| block_ instanceof FlowerBlock && !state_.is(ModTags.Blocks.SPECIAL_FLOWERS)
							|| block_ instanceof DoublePlantBlock) {
						airBlocks.add(pos_);
					}
				}

				if (!airBlocks.isEmpty()) {
					if (pos.getY() > startCenter.getY()) {
						blocks.add(new CoordsWithBlock(pos, Blocks.AIR));
					} else {
						for (BlockPos coords : airBlocks) {
							if (!world.isEmptyBlock(coords.below())) {
								blocks.add(new CoordsWithBlock(coords, Blocks.DIRT));
							}
						}
					}
				}
			}
		}

		int cost = COST_PER * blocks.size();

		if (world.isClientSide || ManaItemHandler.instance().requestManaExactForTool(stack, player, cost, true)) {
			if (!world.isClientSide) {
				for (CoordsWithBlock block : blocks) {
					world.setBlockAndUpdate(block, block.block.defaultBlockState());
				}
			}

			if (!blocks.isEmpty()) {
				world.playSound(player, player.getX(), player.getY(), player.getZ(), ModSounds.terraformRod, SoundSource.BLOCKS, 1F, 0.4F);
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
	public boolean provideBlock(Player player, ItemStack requestor, ItemStack stack, Block block, boolean doit) {
		if (block == Blocks.DIRT) {
			return (doit && ManaItemHandler.instance().requestManaExactForTool(requestor, player, ItemDirtRod.COST, true)) ||
					(!doit && ManaItemHandler.instance().requestManaExactForTool(requestor, player, ItemDirtRod.COST, false));
		}
		return false;
	}

	@Override
	public int getBlockCount(Player player, ItemStack requestor, ItemStack stack, Block block) {
		if (block == Blocks.DIRT) {
			return ManaItemHandler.instance().getInvocationCountForTool(requestor, player, ItemDirtRod.COST);
		}
		return 0;
	}
}
