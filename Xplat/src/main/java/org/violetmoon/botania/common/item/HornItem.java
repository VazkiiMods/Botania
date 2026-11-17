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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import vazkii.botania.common.block.block_entity.flower.misc.BergamuteBlockEntity;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.MathHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class HornItem extends Item {
	public HornItem(Properties props) {
		super(props);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.TOOT_HORN;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 72000;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		return ItemUtils.startUsingInstantly(world, player, hand);
	}

	@Override
	public void onUseTick(Level world, LivingEntity living, ItemStack stack, int time) {
		if (!world.isClientSide) {
			if (time != getUseDuration(stack, living) && time % 5 == 0) {
				living.gameEvent(GameEvent.INSTRUMENT_PLAY);
				breakBlocks(world, this, living.blockPosition());
			}
			world.playSound(null, living.getX(), living.getY(), living.getZ(), BotaniaSounds.HORN_DOOT, SoundSource.BLOCKS, 1F, 1F);
		}
	}

	protected abstract boolean canHarvest(Level level, BlockPos pos);

	protected abstract int getRange();

	protected abstract int getRangeY();

	protected abstract int getNumBlocksToBreak();

	public static void breakBlocks(Level world, HornItem horn, BlockPos srcPos) {
		List<BlockPos> coords = new ArrayList<>();

		for (BlockPos pos : MathHelper.aroundPosClosed(srcPos, horn.getRange(), horn.getRangeY())) {
			if (horn.canHarvest(world, pos) && !BergamuteBlockEntity.isBergamuteNearby(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)) {
				coords.add(pos.immutable());
			}
		}

		Collections.shuffle(coords);

		int count = Math.min(coords.size(), horn.getNumBlocksToBreak());
		for (int i = 0; i < count; i++) {
			BlockPos currCoords = coords.get(i);
			world.destroyBlock(currCoords, true);
		}
	}

}
