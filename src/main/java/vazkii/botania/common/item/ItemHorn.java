/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Unit;
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
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.block.IHornHarvestable;
import vazkii.botania.api.block.IHornHarvestable.EnumHornType;
import vazkii.botania.common.block.subtile.functional.SubTileBergamute;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemHorn extends Item {
	public ItemHorn(Properties props) {
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

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		return ItemUtils.startUsingInstantly(world, player, hand);
	}

	@Override
	public void onUseTick(Level world, @Nonnull LivingEntity player, @Nonnull ItemStack stack, int time) {
		if (!world.isClientSide) {
			if (time != getUseDuration(stack) && time % 5 == 0) {
				breakGrass(world, stack, player.blockPosition());
			}
			world.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.hornDoot, SoundSource.BLOCKS, 1F, 1F);
		}
	}

	public static void breakGrass(Level world, ItemStack stack, BlockPos srcPos) {
		EnumHornType type = null;
		if (stack.is(ModItems.grassHorn)) {
			type = EnumHornType.WILD;
		} else if (stack.is(ModItems.leavesHorn)) {
			type = EnumHornType.CANOPY;
		} else if (stack.is(ModItems.snowHorn)) {
			type = EnumHornType.COVERING;
		}

		int range = 12 - type.ordinal() * 3;
		int rangeY = 3 + type.ordinal() * 4;
		List<BlockPos> coords = new ArrayList<>();

		for (BlockPos pos : BlockPos.betweenClosed(srcPos.offset(-range, -rangeY, -range),
				srcPos.offset(range, rangeY, range))) {
			BlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			BlockEntity be = world.getBlockEntity(pos);
			IHornHarvestable harvestable = IHornHarvestable.API.find(world, pos, state, be, Unit.INSTANCE);

			if (SubTileBergamute.isBergamuteNearby(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)) {
				continue;
			}
			if (harvestable != null
					? harvestable.canHornHarvest(world, pos, stack, type)
					: type == EnumHornType.WILD && block instanceof BushBlock && !state.is(ModTags.Blocks.SPECIAL_FLOWERS)
							|| type == EnumHornType.CANOPY && state.is(BlockTags.LEAVES)
							|| type == EnumHornType.COVERING && state.is(Blocks.SNOW)) {
				coords.add(pos.immutable());
			}
		}

		Collections.shuffle(coords, world.random);

		int count = Math.min(coords.size(), 32 + type.ordinal() * 16);
		for (int i = 0; i < count; i++) {
			BlockPos currCoords = coords.get(i);
			BlockState state = world.getBlockState(currCoords);
			BlockEntity be = world.getBlockEntity(currCoords);
			IHornHarvestable harvestable = IHornHarvestable.API.find(world, currCoords, state, be, Unit.INSTANCE);

			if (harvestable != null && harvestable.hasSpecialHornHarvest(world, currCoords, stack, type)) {
				harvestable.harvestByHorn(world, currCoords, stack, type);
			} else {
				world.destroyBlock(currCoords, true);
			}
		}
	}

}
