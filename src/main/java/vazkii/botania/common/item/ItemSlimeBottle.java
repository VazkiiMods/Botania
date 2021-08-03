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

import vazkii.botania.common.block.subtile.generating.SubTileNarslimmus;
import vazkii.botania.common.core.helper.ItemNBTHelper;

public class ItemSlimeBottle extends Item {
	public static final String TAG_ACTIVE = "active";

	public ItemSlimeBottle(Properties builder) {
		super(builder);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int something, boolean somethingelse) {
		if (!world.isClientSide) {
			boolean slime = SubTileNarslimmus.isSlimeChunk(world, entity.blockPosition());
			ItemNBTHelper.setBoolean(stack, TAG_ACTIVE, slime);
		}
	}
}
