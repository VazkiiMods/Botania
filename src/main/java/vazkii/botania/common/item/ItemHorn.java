/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IHornHarvestable;
import vazkii.botania.api.item.IHornHarvestable.EnumHornType;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemHorn extends Item {
	public ItemHorn(Settings props) {
		super(props);
	}

	@Nonnull
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 72000;
	}

	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, @Nonnull Hand hand) {
		player.setCurrentHand(hand);
		return TypedActionResult.consume(player.getStackInHand(hand));
	}

	@Override
	public void usageTick(World world, @Nonnull LivingEntity player, @Nonnull ItemStack stack, int time) {
		if (!world.isClient) {
			if (time != getMaxUseTime(stack) && time % 5 == 0) {
				breakGrass(world, stack, player.getBlockPos());
			}
			world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_NOTE_BLOCK_BASS, SoundCategory.BLOCKS, 1F, 0.001F);
		}
	}

	public static void breakGrass(World world, ItemStack stack, BlockPos srcPos) {
		EnumHornType type = null;
		if (stack.getItem() == ModItems.grassHorn) {
			type = EnumHornType.WILD;
		} else if (stack.getItem() == ModItems.leavesHorn) {
			type = EnumHornType.CANOPY;
		} else if (stack.getItem() == ModItems.snowHorn) {
			type = EnumHornType.COVERING;
		}

		int range = 12 - type.ordinal() * 3;
		int rangeY = 3 + type.ordinal() * 4;
		List<BlockPos> coords = new ArrayList<>();

		for (BlockPos pos : BlockPos.iterate(srcPos.add(-range, -rangeY, -range),
				srcPos.add(range, rangeY, range))) {
			Block block = world.getBlockState(pos).getBlock();
			IHornHarvestable harvestable = BotaniaAPI.instance().getHornHarvestable(block).orElse(null);

			if (harvestable != null
					? harvestable.canHornHarvest(world, pos, stack, type)
					: type == EnumHornType.WILD && block instanceof PlantBlock && !block.isIn(ModTags.Blocks.SPECIAL_FLOWERS)
							|| type == EnumHornType.CANOPY && BlockTags.LEAVES.contains(block)
							|| type == EnumHornType.COVERING && block == Blocks.SNOW) {
				coords.add(pos.toImmutable());
			}
		}

		Collections.shuffle(coords, world.random);

		int count = Math.min(coords.size(), 32 + type.ordinal() * 16);
		for (int i = 0; i < count; i++) {
			BlockPos currCoords = coords.get(i);
			BlockState state = world.getBlockState(currCoords);
			Block block = state.getBlock();
			IHornHarvestable harvestable = BotaniaAPI.instance().getHornHarvestable(block).orElse(null);

			if (harvestable != null && harvestable.hasSpecialHornHarvest(world, currCoords, stack, type)) {
				harvestable.harvestByHorn(world, currCoords, stack, type);
			} else {
				world.breakBlock(currCoords, true);
			}
		}
	}

}
