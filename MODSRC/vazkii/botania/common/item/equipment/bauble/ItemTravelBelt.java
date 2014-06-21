/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Apr 24, 2014, 11:14:57 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import vazkii.botania.common.lib.LibItemNames;
import baubles.api.BaubleType;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ItemTravelBelt extends ItemBauble {

	final float speed;

	public ItemTravelBelt() {
		this(LibItemNames.TRAVEL_BELT, 0.035F);
	}

	public ItemTravelBelt(String name, float speed) {
		super(name);
		this.speed = speed;
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.BELT;
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase entity) {
		super.onWornTick(stack, entity);

		if(entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if((player.onGround || player.capabilities.isFlying) && player.moveForward > 0F)
				player.moveFlying(0F, 1F, player.capabilities.isFlying ? speed : speed * 2);

			if(player.isSneaking())
				player.stepHeight = 0.50001F; // Not 0.5F because that is the default
			else if(player.stepHeight == 0.50001F)
				player.stepHeight = 1F;
		}
	}

	@SubscribeEvent
	public void onPlayerJump(LivingJumpEvent event) {
		if(event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			ItemStack belt = PlayerHandler.getPlayerBaubles(player).getStackInSlot(3);

			if(belt != null && belt.getItem() == this) {
				player.motionY += 0.2;
				player.fallDistance = -1F;
			}
		}
	}

	@Override
	public void onEquippedOrLoadedIntoWorld(ItemStack stack, EntityLivingBase player) {
		player.stepHeight = 1F;
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		player.stepHeight = 0.5F;
	}

}
