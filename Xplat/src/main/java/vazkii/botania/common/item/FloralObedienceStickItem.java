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
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import vazkii.botania.api.block_entity.BindableSpecialFlowerBlockEntity;
import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.mana.ManaCollector;
import vazkii.botania.api.mana.ManaPool;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.common.helper.MathHelper;

public class FloralObedienceStickItem extends Item {

	public FloralObedienceStickItem(Properties props) {
		super(props);
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		return applyStick(world, pos)
				? InteractionResult.sidedSuccess(world.isClientSide())
				: InteractionResult.PASS;
	}

	public static boolean applyStick(Level world, BlockPos pos) {
		var receiver = ManaReceiver.LOOKUP.find(world, pos, null);
		if (receiver instanceof ManaPool || receiver instanceof ManaCollector) {
			int range = receiver instanceof ManaPool ? FunctionalFlowerBlockEntity.LINK_RANGE : GeneratingFlowerBlockEntity.LINK_RANGE;

			int rangeSqr = range * range;
			for (BlockPos iterPos : MathHelper.aroundPosClosed(pos, range)) {
				if (MathHelper.distSqr(iterPos, pos) > rangeSqr) {
					continue;
				}

				if (world.getBlockEntity(iterPos) instanceof BindableSpecialFlowerBlockEntity<?> bindable
						&& bindable.wouldBeValidBinding(pos)) {
					bindable.setBindingPos(pos);
					WandOfTheForestItem.doParticleBeamWithOffset(world, iterPos, pos);
				}
			}

			return true;
		}
		if (world.getBlockEntity(pos) instanceof BindableSpecialFlowerBlockEntity<?> bindableFlower) {
			if (bindableFlower.getBindingPos() == null) {
				bindableFlower.attemptAutoBinding();
			} else {
				bindableFlower.setBindingPos(null);
			}
			return true;
		}

		return false;
	}
}
