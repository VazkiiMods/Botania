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

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.lib.LibItemNames;
import baubles.api.BaubleType;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTravelBelt extends ItemBauble implements IBaubleRender {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_TRAVEL_BELT);
	@SideOnly(Side.CLIENT)
	private static ModelBiped model;

	final float speed;
	final float jump;
	final float fallBuffer;

	public ItemTravelBelt() {
		this(LibItemNames.TRAVEL_BELT, 0.035F, 0.2F, 2F);
	}

	public ItemTravelBelt(String name, float speed, float jump, float fallBuffer) {
		super(name);
		this.speed = speed;
		this.jump = jump;
		this.fallBuffer = fallBuffer;
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
			if((player.onGround || player.capabilities.isFlying) && player.moveForward > 0F && !player.isInsideOfMaterial(Material.water))
				player.moveFlying(0F, 1F, player.capabilities.isFlying ? speed : speed * 2);

			if(player.isSneaking())
				player.stepHeight = 0.50001F; // Not 0.5F because that is the default
			else if(player.stepHeight <= 0.50001F)
				player.stepHeight = 1F;
		}
	}

	@SubscribeEvent
	public void onPlayerJump(LivingJumpEvent event) {
		if(event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			ItemStack belt = PlayerHandler.getPlayerBaubles(player).getStackInSlot(3);

			if(belt != null && belt.getItem() == this) {
				player.motionY += jump;
				player.fallDistance = -fallBuffer;
			}
		}
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		player.stepHeight = 0.5F;
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
			boolean armor = event.entityPlayer.getCurrentArmor(1) != null;
			GL11.glTranslatef(0F, 0.1F, 0F);

			float s = (armor ? 1.3F : 1.05F) / 16F;
			GL11.glScalef(s, s, s);
			if(model == null)
				model = new ModelBiped();

			model.bipedBody.render(1F);
		}
	}

}
