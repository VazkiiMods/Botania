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

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.lib.LibItemNames;

import java.util.ArrayList;
import java.util.List;

public class ItemTravelBelt extends ItemBauble implements IBaubleRender, IManaUsingItem {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_TRAVEL_BELT);
	@SideOnly(Side.CLIENT)
	private static ModelBiped model;

	private static final int COST = 1;
	private static final int COST_INTERVAL = 10;

	public static final List<String> playersWithStepup = new ArrayList<>();

	public final float speed;
	public final float jump;
	public final float fallBuffer;

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
		if(event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			String s = playerStr(player);

			ItemStack belt = BaublesApi.getBaublesHandler(player).getStackInSlot(3);
			if(playersWithStepup.contains(s)) {
				if(shouldPlayerHaveStepup(player)) {
					ItemTravelBelt beltItem = (ItemTravelBelt) belt.getItem();

					if(player.world.isRemote) {
						if((player.onGround || player.capabilities.isFlying) && player.moveForward > 0F && !player.isInsideOfMaterial(Material.WATER)) {
							float speed = beltItem.getSpeed(belt);
							player.moveRelative(0F, 0F, 1F, player.capabilities.isFlying ? speed : speed);
							beltItem.onMovedTick(belt, player);

							if(player.ticksExisted % COST_INTERVAL == 0)
								ManaItemHandler.requestManaExact(belt, player, COST, true);
						} else beltItem.onNotMovingTick(belt, player);
					}

					if(player.isSneaking())
						player.stepHeight = 0.60001F; // Not 0.6F because that is the default
						else player.stepHeight = 1.25F;

				} else {
					player.stepHeight = 0.6F;
					playersWithStepup.remove(s);
				}
			} else if(shouldPlayerHaveStepup(player)) {
				playersWithStepup.add(s);
				player.stepHeight = 1.25F;
			}
		}
	}

	public float getSpeed(ItemStack stack) {
		return speed;
	}

	public void onMovedTick(ItemStack stack, EntityPlayer player) {}

	public void onNotMovingTick(ItemStack stack, EntityPlayer player) {}

	@SubscribeEvent
	public void onPlayerJump(LivingJumpEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			ItemStack belt = BaublesApi.getBaublesHandler(player).getStackInSlot(3);

			if(!belt.isEmpty() && belt.getItem() instanceof ItemTravelBelt && ManaItemHandler.requestManaExact(belt, player, COST, false)) {
				player.motionY += ((ItemTravelBelt) belt.getItem()).jump;
				player.fallDistance = -((ItemTravelBelt) belt.getItem()).fallBuffer;
			}
		}
	}

	private boolean shouldPlayerHaveStepup(EntityPlayer player) {
		ItemStack armor = BaublesApi.getBaublesHandler(player).getStackInSlot(3);
		return !armor.isEmpty() && armor.getItem() instanceof ItemTravelBelt && ManaItemHandler.requestManaExact(armor, player, COST, false);
	}

	@SubscribeEvent
	public void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		String username = event.player.getGameProfile().getName();
		playersWithStepup.remove(username + ":false");
		playersWithStepup.remove(username + ":true");
	}

	public static String playerStr(EntityPlayer player) {
		return player.getGameProfile().getName() + ":" + player.world.isRemote;
	}

	@SideOnly(Side.CLIENT)
	ResourceLocation getRenderTexture() {
		return texture;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks) {
		if(type == RenderType.BODY) {
			Minecraft.getMinecraft().renderEngine.bindTexture(getRenderTexture());
			Helper.rotateIfSneaking(player);

			GlStateManager.translate(0F, 0.2F, 0F);

			float s = 1.05F / 16F;
			GlStateManager.scale(s, s, s);
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
