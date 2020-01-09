/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [22/09/2016, 12:56:22 (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.network.PacketDodge;
import vazkii.botania.common.network.PacketHandler;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = LibMisc.MOD_ID)
public class ItemDodgeRing extends ItemBauble {

	public static final String TAG_DODGE_COOLDOWN = "dodgeCooldown";
	public static final int MAX_CD = 20;

	private static boolean oldLeftDown, oldRightDown;
	private static int leftDown, rightDown;

	public ItemDodgeRing(Properties props) {
		super(props);
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onKeyDown(InputEvent.KeyInputEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if(mc.player == null)
			return;

		ItemStack ringStack = EquipmentHandler.findOrEmpty(ModItems.dodgeRing, mc.player);
		if(ringStack.isEmpty() || ItemNBTHelper.getInt(ringStack, TAG_DODGE_COOLDOWN, 0) > 0)
			return;

		int threshold = 4;
		if(mc.gameSettings.keyBindLeft.isKeyDown() && !oldLeftDown) {
			int oldLeft = leftDown;
			leftDown = ClientTickHandler.ticksInGame;

			if(leftDown - oldLeft < threshold)
				dodge(mc.player, true);
		} else if(mc.gameSettings.keyBindRight.isKeyDown() && !oldRightDown) {
			int oldRight = rightDown;
			rightDown = ClientTickHandler.ticksInGame;

			if(rightDown - oldRight < threshold)
				dodge(mc.player, false);
		}

		oldLeftDown = mc.gameSettings.keyBindLeft.isKeyDown();
		oldRightDown = mc.gameSettings.keyBindRight.isKeyDown();
	}


	@Override
	public void onWornTick(ItemStack stack, LivingEntity player) {
		int cd = ItemNBTHelper.getInt(stack, TAG_DODGE_COOLDOWN, 0);
		if(cd > 0)
			ItemNBTHelper.setInt(stack, TAG_DODGE_COOLDOWN, cd - 1);
	}

	private static void dodge(PlayerEntity player, boolean left) {
		if(player.abilities.isFlying || !player.onGround || player.moveForward > 0.2 || player.moveForward < -0.2)
			return;

		float yaw = player.rotationYaw;
		float x = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
		float z = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
		Vector3 lookVec = new Vector3(x, 0, z);
		Vector3 sideVec = lookVec.crossProduct(new Vector3(0, left ? 1 : -1, 0)).multiply(1.25);

		player.setMotion(sideVec.toVec3D());

		PacketHandler.sendToServer(new PacketDodge());
	}

	@OnlyIn(Dist.CLIENT)
	public static void renderHUD(PlayerEntity player, ItemStack stack, float pticks) {
		int xo = Minecraft.getInstance().getWindow().getScaledWidth() / 2 - 20;
		int y = Minecraft.getInstance().getWindow().getScaledHeight() / 2 + 20;

		if(!player.abilities.isFlying) {
			int cd = ItemNBTHelper.getInt(stack, TAG_DODGE_COOLDOWN, 0);
			int width = Math.min((int) ((cd - pticks) * 2), 40);
			GlStateManager.color4f(1F, 1F, 1F, 1F);
			if(width > 0) {
				AbstractGui.fill(xo, y - 2, xo + 40, y - 1, 0x88000000);
				AbstractGui.fill(xo, y - 2, xo + width, y - 1, 0xFFFFFFFF);
			}
		}

		GlStateManager.enableAlphaTest();
		GlStateManager.color4f(1F, 1F, 1F, 1F);
	}
}
