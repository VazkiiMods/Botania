/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

import vazkii.botania.common.entity.EnderEssenceCloudEntity;
import vazkii.botania.common.handler.BotaniaSounds;

import java.util.List;

public class EnderEssenceBottlingBehavior extends OptionalDispenseItemBehavior {

	@Override
	protected ItemStack execute(BlockSource source, ItemStack stack) {
		BlockPos blockPos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
		List<EnderEssenceCloudEntity> entities = source.level().getEntitiesOfClass(
				EnderEssenceCloudEntity.class,
				new AABB(blockPos), EntitySelector.ENTITY_STILL_ALIVE);
		if (!entities.isEmpty()) {
			EnderEssenceCloudEntity cloud = entities.getFirst();
			ItemStack bottledStack = cloud.getBottledItem();
			if (!bottledStack.isEmpty()) {
				source.level().playSound(null, blockPos, BotaniaSounds.ENDER_ESSENCE_FILL, SoundSource.BLOCKS, 1, 1);
				source.level().gameEvent(null, GameEvent.FLUID_PICKUP, source.pos());
				cloud.discard();
				setSuccess(true);
				return consumeWithRemainder(source, stack, bottledStack);
			}
		}
		this.setSuccess(false);
		return super.execute(source, stack);
	}
}
