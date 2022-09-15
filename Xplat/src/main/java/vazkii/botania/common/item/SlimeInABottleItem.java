/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import vazkii.botania.common.block.flower.generating.NarslimmusBlockEntity;
import vazkii.botania.common.helper.ItemNBTHelper;

public class SlimeInABottleItem extends Item {
	public static final String TAG_ACTIVE = "active";

	public SlimeInABottleItem(Properties builder) {
		super(builder);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int something, boolean somethingelse) {
		if (!world.isClientSide) {
			boolean slime = NarslimmusBlockEntity.isSlimeChunk(world, entity.blockPosition());
			ItemNBTHelper.setBoolean(stack, TAG_ACTIVE, slime);
		}
	}
}
