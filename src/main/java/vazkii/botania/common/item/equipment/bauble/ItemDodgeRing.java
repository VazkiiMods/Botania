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

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.network.PacketDodge;
import vazkii.botania.common.network.PacketHandler;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = LibMisc.MOD_ID)
public class ItemDodgeRing extends ItemBauble {

	public static final String TAG_DODGE_COOLDOWN = "dodgeCooldown";
	public static final int MAX_CD = 20;

	private static boolean oldLeftDown, oldRightDown;
	private static int leftDown, rightDown;

	public ItemDodgeRing() {
		super(LibItemNames.DODGE_RING);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onKeyDown(KeyInputEvent event) {
		Minecraft mc = Minecraft.getMinecraft();

		IItemHandler baublesInv = BaublesApi.getBaublesHandler(mc.player);
		int slot = BaublesApi.isBaubleEquipped(mc.player, ModItems.dodgeRing);
		if(slot < 0) {
				return;
		}
		ItemStack ringStack = baublesInv.getStackInSlot(slot);
		if(ItemNBTHelper.getInt(ringStack, TAG_DODGE_COOLDOWN, 0) > 0)
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
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		int cd = ItemNBTHelper.getInt(stack, TAG_DODGE_COOLDOWN, 0);
		if(cd > 0)
			ItemNBTHelper.setInt(stack, TAG_DODGE_COOLDOWN, cd - 1);
	}

	private static void dodge(EntityPlayer player, boolean left) {
		if(player.capabilities.isFlying || !player.onGround || player.moveForward > 0.2 || player.moveForward < -0.2)
			return;

		float yaw = player.rotationYaw;
		float x = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
		float z = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
		Vector3 lookVec = new Vector3(x, 0, z);
		Vector3 sideVec = lookVec.crossProduct(new Vector3(0, left ? 1 : -1, 0)).multiply(1.25);

		player.motionX = sideVec.x;
		player.motionY = sideVec.y;
		player.motionZ = sideVec.z;

		PacketHandler.sendToServer(new PacketDodge());
	}

	@SideOnly(Side.CLIENT)
	public static void renderHUD(ScaledResolution resolution, EntityPlayer player, ItemStack stack, float pticks) {
		int xo = resolution.getScaledWidth() / 2 - 20;
		int y = resolution.getScaledHeight() / 2 + 20;

		if(!player.capabilities.isFlying) {
			int cd = ItemNBTHelper.getInt(stack, TAG_DODGE_COOLDOWN, 0);
			int width = Math.min((int) ((cd - pticks) * 2), 40);
			GlStateManager.color(1F, 1F, 1F, 1F);
			if(width > 0) {
				Gui.drawRect(xo, y - 2, xo + 40, y - 1, 0x88000000);
				Gui.drawRect(xo, y - 2, xo + width, y - 1, 0xFFFFFFFF);
			}
		}

		GlStateManager.enableAlpha();
		GlStateManager.color(1F, 1F, 1F, 1F);
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.RING;
	}

}
