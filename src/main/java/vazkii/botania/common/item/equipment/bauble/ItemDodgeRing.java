/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.network.PacketDodge;
import vazkii.botania.common.network.PacketHandler;

public class ItemDodgeRing extends ItemBauble {

	public static final String TAG_DODGE_COOLDOWN = "dodgeCooldown";
	public static final int MAX_CD = 20;

	private static boolean oldLeftDown, oldRightDown, oldForwardDown, oldBackDown;
	private static int leftDown, rightDown, forwardDown, backDown;

	public ItemDodgeRing(Settings props) {
		super(props);
	}

	@Environment(EnvType.CLIENT)
	public static void onKeyDown() {
		MinecraftClient mc = MinecraftClient.getInstance();
		if (mc.player == null) {
			return;
		}

		ItemStack ringStack = EquipmentHandler.findOrEmpty(ModItems.dodgeRing, mc.player);
		if (ringStack.isEmpty() || ItemNBTHelper.getInt(ringStack, TAG_DODGE_COOLDOWN, 0) > 0) {
			return;
		}

		int threshold = 5;
		if (mc.options.keyLeft.isPressed() && !oldLeftDown) {
			int oldLeft = leftDown;
			leftDown = ClientTickHandler.ticksInGame;

			if (leftDown - oldLeft < threshold) {
				dodge(mc.player, Direction.WEST);
			}
		} else if (mc.options.keyRight.isPressed() && !oldRightDown) {
			int oldRight = rightDown;
			rightDown = ClientTickHandler.ticksInGame;

			if (rightDown - oldRight < threshold) {
				dodge(mc.player, Direction.EAST);
			}
		} else if (mc.options.keyForward.isPressed() && !oldForwardDown) {
			int oldForward = forwardDown;
			forwardDown = ClientTickHandler.ticksInGame;

			if (forwardDown - oldForward < threshold) {
				dodge(mc.player, Direction.NORTH);
			}
		} else if (mc.options.keyBack.isPressed() && !oldBackDown) {
			int oldBack = backDown;
			backDown = ClientTickHandler.ticksInGame;

			if (backDown - oldBack < threshold) {
				dodge(mc.player, Direction.SOUTH);
			}
		}

		oldLeftDown = mc.options.keyLeft.isPressed();
		oldRightDown = mc.options.keyRight.isPressed();
		oldForwardDown = mc.options.keyForward.isPressed();
		oldBackDown = mc.options.keyBack.isPressed();
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity player) {
		int cd = ItemNBTHelper.getInt(stack, TAG_DODGE_COOLDOWN, 0);
		if (cd > 0) {
			ItemNBTHelper.setInt(stack, TAG_DODGE_COOLDOWN, cd - 1);
		}
	}

	private static void dodge(PlayerEntity player, Direction dir) {
		if (player.abilities.flying || !player.isOnGround() || dir == Direction.UP || dir == Direction.DOWN) {
			return;
		}

		float yaw = player.yaw;
		float x = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
		float z = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
		if (dir == Direction.NORTH || dir == Direction.SOUTH) {
			x = MathHelper.cos(-yaw * 0.017453292F);
			z = MathHelper.sin(yaw * 0.017453292F);
		}
		Vector3 lookVec = new Vector3(x, 0, z);
		Vector3 sideVec = lookVec.crossProduct(new Vector3(0, dir == Direction.WEST || dir == Direction.NORTH ? 1 : (dir == Direction.EAST || dir == Direction.SOUTH ? -1 : 0), 0)).multiply(1.25);

		player.setVelocity(sideVec.toVector3d());

		PacketDodge.send();
	}

	@Environment(EnvType.CLIENT)
	public static void renderHUD(MatrixStack ms, PlayerEntity player, ItemStack stack, float pticks) {
		int xo = MinecraftClient.getInstance().getWindow().getScaledWidth() / 2 - 20;
		int y = MinecraftClient.getInstance().getWindow().getScaledHeight() / 2 + 20;

		if (!player.abilities.flying) {
			int cd = ItemNBTHelper.getInt(stack, TAG_DODGE_COOLDOWN, 0);
			int width = Math.min((int) ((cd - pticks) * 2), 40);
			RenderSystem.color4f(1F, 1F, 1F, 1F);
			if (width > 0) {
				DrawableHelper.fill(ms, xo, y - 2, xo + 40, y - 1, 0x88000000);
				DrawableHelper.fill(ms, xo, y - 2, xo + width, y - 1, 0xFFFFFFFF);
			}
		}

		RenderSystem.enableAlphaTest();
		RenderSystem.color4f(1F, 1F, 1F, 1F);
	}
}
