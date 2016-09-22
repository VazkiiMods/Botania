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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.network.PacketDodge;
import vazkii.botania.common.network.PacketHandler;

public class ItemDodgeRing extends ItemBauble {

	public static final String TAG_DODGE_COOLDOWN = "dodgeCooldown";
	int leftDown, rightDown;
	
	public ItemDodgeRing() {
		super(LibItemNames.DODGE_RING);
		
		if(FMLCommonHandler.instance().getSide().isClient())
			MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onKeyDown(KeyInputEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		
		IInventory baublesInv = BaublesApi.getBaubles(mc.thePlayer);
		ItemStack ringStack = baublesInv.getStackInSlot(1);
		if(ringStack == null || !(ringStack.getItem() instanceof ItemDodgeRing)) {
			ringStack = baublesInv.getStackInSlot(2);
			if(ringStack == null || !(ringStack.getItem() instanceof ItemDodgeRing))
				return;
		}
		
		if(ItemNBTHelper.getInt(ringStack, TAG_DODGE_COOLDOWN, 0) > 0)
			return;
		
		int threshold = 4;
		if(mc.gameSettings.keyBindLeft.isKeyDown()) {
			int oldLeft = leftDown;
			leftDown = ClientTickHandler.ticksInGame;
			
			if(leftDown - oldLeft < threshold)
				dodge(mc.thePlayer, true);
		} else if(mc.gameSettings.keyBindRight.isKeyDown()) {
			int oldRight = rightDown;
			rightDown = ClientTickHandler.ticksInGame;
			
			if(rightDown - oldRight < threshold)
				dodge(mc.thePlayer, false);
		}
	}
	
	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		int cd = ItemNBTHelper.getInt(stack, TAG_DODGE_COOLDOWN, 0);
		if(cd > 0)
			ItemNBTHelper.setInt(stack, TAG_DODGE_COOLDOWN, cd - 1);
	}
	
	public void dodge(EntityPlayer player, boolean left) {
		if(!player.onGround || player.moveForward > 0.2 || player.moveForward < 0.2)
			return;
		
		Vector3 lookVec = new Vector3(player.getLookVec());
		Vector3 sideVec = lookVec.crossProduct(new Vector3(0, left ? -1 : 1, 0)).multiply(1.25);
		
		player.motionX = sideVec.x;
		player.motionY = sideVec.y;
		player.motionZ = sideVec.z;
		
		PacketHandler.sendToServer(new PacketDodge());
	}
	
	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.RING;
	}

}
