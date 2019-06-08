/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 17, 2015, 1:32:58 AM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.block.subtile.generating.SubTileNarslimmus;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nullable;

public class ItemSlimeBottle extends ItemMod {
	private static final String TAG_ACTIVE = "active";

	public ItemSlimeBottle(Properties builder) {
		super(builder);
		addPropertyOverride(new ResourceLocation(LibMisc.MOD_ID, "active"), (stack, worldIn, entityIn) -> stack.hasTag() && stack.getTag().getBoolean(TAG_ACTIVE) ? 1.0F : 0.0F);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int something, boolean somethingelse) {
		if(!world.isRemote) {
			boolean slime = SubTileNarslimmus.isSlimeChunk(world, new BlockPos(entity));
			ItemNBTHelper.setBoolean(stack, TAG_ACTIVE, slime);
		}
	}
}
