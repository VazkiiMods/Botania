/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [24/11/2015, 22:50:29 (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibItemNames;

public class ItemSpeedUpBelt extends ItemTravelBelt {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_SPEED_UP_BELT);

	private static final String TAG_SPEED = "speed";
	private static final String TAG_OLD_X = "oldX";
	private static final String TAG_OLD_Y = "oldY";
	private static final String TAG_OLD_Z = "oldZ";

	public ItemSpeedUpBelt() {
		super(LibItemNames.SPEED_UP_BELT, 0F, 0.2F, 2F);
	}

	@Override
	public ResourceLocation getRenderTexture() {
		return texture;
	}

	@Override
	public float getSpeed(ItemStack stack) {
		return ItemNBTHelper.getFloat(stack, TAG_SPEED, 0F);
	}

	@Override
	public void onMovedTick(ItemStack stack, EntityPlayer player) {
		float speed = getSpeed(stack);
		float newspeed = Math.min(0.25F, speed + 0.00035F);
		ItemNBTHelper.setFloat(stack, TAG_SPEED, newspeed);
		commitPositionAndCompare(stack, player);
	}

	@Override
	public void onNotMovingTick(ItemStack stack, EntityPlayer player) {
		if(!commitPositionAndCompare(stack, player))
			ItemNBTHelper.setFloat(stack, TAG_SPEED, 0F);
	}

	public boolean commitPositionAndCompare(ItemStack stack, EntityPlayer player) {
		double oldX = ItemNBTHelper.getDouble(stack, TAG_OLD_X, 0);
		double oldY = ItemNBTHelper.getDouble(stack, TAG_OLD_Y, 0);
		double oldZ = ItemNBTHelper.getDouble(stack, TAG_OLD_Z, 0);

		ItemNBTHelper.setDouble(stack, TAG_OLD_X, player.posX);
		ItemNBTHelper.setDouble(stack, TAG_OLD_Y, player.posY);
		ItemNBTHelper.setDouble(stack, TAG_OLD_Z, player.posZ);

		return Math.abs(oldX - player.posX) > 0.001 || Math.abs(oldY - player.posY) > 0.001 || Math.abs(oldZ - player.posZ) > 0.001;
	}

}

