/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.client.lib.ResourcesLib;
import vazkii.botania.common.component.BotaniaDataComponents;

public class PlanestridersSashItem extends SojournersSashItem {

	private static final ResourceLocation texture = ResourceLocation.parse(ResourcesLib.MODEL_SPEED_UP_BELT);

	public PlanestridersSashItem(Properties props) {
		super(props, 0F, 0.2F, 2F);
	}

	@Override
	public ResourceLocation getRenderTexture() {
		return texture;
	}

	@Override
	public float getSpeed(ItemStack stack) {
		return stack.getOrDefault(BotaniaDataComponents.SPEED, 0f);
	}

	@Override
	public void onMovedTick(ItemStack stack, Player player) {
		float speed = getSpeed(stack);
		float newSpeed = Math.min(0.25F, speed + 0.00035F);
		stack.set(BotaniaDataComponents.SPEED, newSpeed);
		commitPositionAndCompare(stack, player);
	}

	@Override
	public void onNotMovingTick(ItemStack stack, Player player) {
		if (!commitPositionAndCompare(stack, player)) {
			stack.remove(BotaniaDataComponents.SPEED);
		}
	}

	public boolean commitPositionAndCompare(ItemStack stack, Player player) {
		Vec3 old = stack.getOrDefault(BotaniaDataComponents.OLD_POS, Vec3.ZERO);
		stack.set(BotaniaDataComponents.OLD_POS, player.position());

		return Math.abs(old.x() - player.getX()) > 0.001 || Math.abs(old.y() - player.getY()) > 0.001 || Math.abs(old.z() - player.getZ()) > 0.001;
	}

}
