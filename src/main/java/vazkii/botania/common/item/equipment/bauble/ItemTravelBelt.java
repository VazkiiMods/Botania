/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 24, 2014, 11:14:57 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.lib.LibItemNames;
import baubles.api.BaubleType;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTravelBelt extends ItemBauble implements IBaubleRender, IManaUsingItem {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_TRAVEL_BELT);
	@SideOnly(Side.CLIENT)
	private static ModelBiped model;

	private static final int COST = 1;
	private static final int COST_INTERVAL = 10;

	public static List<String> playersWithStepup = new ArrayList();

	final float speed;
	final float jump;
	final float fallBuffer;

	public ItemTravelBelt() {
		this(LibItemNames.TRAVEL_BELT, 0.035F, 0.2F, 2F);
		MinecraftForge.EVENT_BUS.register(this);
	}

	public ItemTravelBelt(String name, float speed, float jump, float fallBuffer) {
		super(name);
		this.speed = speed;
		this.jump = jump;
		this.fallBuffer = fallBuffer;
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.BELT;
	}

	@SubscribeEvent
	public void updatePlayerStepStatus(LivingUpdateEvent event) {
		if(event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			String s = playerStr(player);

			ItemStack belt = PlayerHandler.getPlayerBaubles(player).getStackInSlot(3);
			if(playersWithStepup.contains(s)) {
				if(shouldPlayerHaveStepup(player)) {
					ItemTravelBelt beltItem = (ItemTravelBelt) belt.getItem();

					if((player.onGround || player.capabilities.isFlying) && player.moveForward > 0F && !player.isInsideOfMaterial(Material.water)) {
						float speed = beltItem.getSpeed(belt);
						player.moveFlying(0F, 1F, player.capabilities.isFlying ? speed : speed);
						beltItem.onMovedTick(belt, player);

						if(player.ticksExisted % COST_INTERVAL == 0)
							ManaItemHandler.requestManaExact(belt, player, COST, true);
					} else beltItem.onNotMovingTick(belt, player);

					if(player.isSneaking())
						player.stepHeight = 0.50001F; // Not 0.5F because that is the default
					else player.stepHeight = 1F;

				} else {
					player.stepHeight = 0.5F;
					playersWithStepup.remove(s);
				}
			} else if(shouldPlayerHaveStepup(player)) {
				playersWithStepup.add(s);
				player.stepHeight = 1F;
			}
		}
	}

	public float getSpeed(ItemStack stack) {
		return speed;
	}

	public void onMovedTick(ItemStack stack, EntityPlayer player) {
		// NO-OP
	}

	public void onNotMovingTick(ItemStack stack, EntityPlayer player) {
		// NO-OP
	}

	@SubscribeEvent
	public void onPlayerJump(LivingJumpEvent event) {
		if(event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			ItemStack belt = PlayerHandler.getPlayerBaubles(player).getStackInSlot(3);

			if(belt != null && belt.getItem() instanceof ItemTravelBelt && ManaItemHandler.requestManaExact(belt, player, COST, false)) {
				player.motionY += ((ItemTravelBelt) belt.getItem()).jump;
				player.fallDistance = -((ItemTravelBelt) belt.getItem()).fallBuffer;
			}
		}
	}

	private boolean shouldPlayerHaveStepup(EntityPlayer player) {
		ItemStack armor = PlayerHandler.getPlayerBaubles(player).getStackInSlot(3);
		return armor != null && armor.getItem() instanceof ItemTravelBelt && ManaItemHandler.requestManaExact(armor, player, COST, false);
	}

	@SubscribeEvent
	public void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		String username = event.player.getGameProfile().getName();
		playersWithStepup.remove(username + ":false");
		playersWithStepup.remove(username + ":true");
	}

	public static String playerStr(EntityPlayer player) {
		return player.getGameProfile().getName() + ":" + player.worldObj.isRemote;
	}

	@SideOnly(Side.CLIENT)
	ResourceLocation getRenderTexture() {
		return texture;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, RenderPlayerEvent event, RenderType type) {
		if(type == RenderType.BODY) {
			Minecraft.getMinecraft().renderEngine.bindTexture(getRenderTexture());
			Helper.rotateIfSneaking(event.entityPlayer);
			GL11.glTranslatef(0F, 0.2F, 0F);

			float s = 1.05F / 16F;
			GL11.glScalef(s, s, s);
			if(model == null)
				model = new ModelBiped();

			model.bipedBody.render(1F);
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
