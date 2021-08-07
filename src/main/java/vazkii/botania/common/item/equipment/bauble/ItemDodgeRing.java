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
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.network.PacketDodge;

public class ItemDodgeRing extends ItemBauble {

	public static final String TAG_DODGE_COOLDOWN = "dodgeCooldown";
	public static final int MAX_CD = 20;

	private static boolean oldLeftDown, oldRightDown, oldForwardDown, oldBackDown;
	private static int leftDown, rightDown, forwardDown, backDown;

	public ItemDodgeRing(Properties props) {
		super(props);
	}

	@Environment(EnvType.CLIENT)
	public static void onKeyDown() {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null) {
			return;
		}

		ItemStack ringStack = EquipmentHandler.findOrEmpty(ModItems.dodgeRing, mc.player);
		if (ringStack.isEmpty() || ItemNBTHelper.getInt(ringStack, TAG_DODGE_COOLDOWN, 0) > 0) {
			return;
		}

		int threshold = 5;
		if (mc.options.keyLeft.isDown() && !oldLeftDown) {
			int oldLeft = leftDown;
			leftDown = ClientTickHandler.ticksInGame;

			if (leftDown - oldLeft < threshold) {
				dodge(mc.player, Direction.WEST);
			}
		} else if (mc.options.keyRight.isDown() && !oldRightDown) {
			int oldRight = rightDown;
			rightDown = ClientTickHandler.ticksInGame;

			if (rightDown - oldRight < threshold) {
				dodge(mc.player, Direction.EAST);
			}
		} else if (mc.options.keyUp.isDown() && !oldForwardDown) {
			int oldForward = forwardDown;
			forwardDown = ClientTickHandler.ticksInGame;

			if (forwardDown - oldForward < threshold) {
				dodge(mc.player, Direction.NORTH);
			}
		} else if (mc.options.keyDown.isDown() && !oldBackDown) {
			int oldBack = backDown;
			backDown = ClientTickHandler.ticksInGame;

			if (backDown - oldBack < threshold) {
				dodge(mc.player, Direction.SOUTH);
			}
		}

		oldLeftDown = mc.options.keyLeft.isDown();
		oldRightDown = mc.options.keyRight.isDown();
		oldForwardDown = mc.options.keyUp.isDown();
		oldBackDown = mc.options.keyDown.isDown();
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity player) {
		int cd = ItemNBTHelper.getInt(stack, TAG_DODGE_COOLDOWN, 0);
		if (cd > 0) {
			ItemNBTHelper.setInt(stack, TAG_DODGE_COOLDOWN, cd - 1);
		}
	}

	private static void dodge(Player player, Direction dir) {
		if (player.getAbilities().flying || !player.isOnGround() || dir == Direction.UP || dir == Direction.DOWN) {
			return;
		}

		float yaw = player.getYRot();
		float x = Mth.sin(-yaw * 0.017453292F - (float) Math.PI);
		float z = Mth.cos(-yaw * 0.017453292F - (float) Math.PI);
		if (dir == Direction.NORTH || dir == Direction.SOUTH) {
			x = Mth.cos(-yaw * 0.017453292F);
			z = Mth.sin(yaw * 0.017453292F);
		}
		Vector3 lookVec = new Vector3(x, 0, z);
		Vector3 sideVec = lookVec.crossProduct(new Vector3(0, dir == Direction.WEST || dir == Direction.NORTH ? 1 : (dir == Direction.EAST || dir == Direction.SOUTH ? -1 : 0), 0)).multiply(1.25);

		player.setDeltaMovement(sideVec.toVector3d());

		PacketDodge.send();
	}

	@Environment(EnvType.CLIENT)
	public static void renderHUD(PoseStack ms, Player player, ItemStack stack, float pticks) {
		int xo = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - 20;
		int y = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 + 20;

		if (!player.getAbilities().flying) {
			int cd = ItemNBTHelper.getInt(stack, TAG_DODGE_COOLDOWN, 0);
			int width = Math.min((int) ((cd - pticks) * 2), 40);
			RenderSystem.color4f(1F, 1F, 1F, 1F);
			if (width > 0) {
				GuiComponent.fill(ms, xo, y - 2, xo + 40, y - 1, 0x88000000);
				GuiComponent.fill(ms, xo, y - 2, xo + width, y - 1, 0xFFFFFFFF);
			}
		}

		RenderSystem.enableAlphaTest();
		RenderSystem.color4f(1F, 1F, 1F, 1F);
	}
}
