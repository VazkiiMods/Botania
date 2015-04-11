/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Dec 4, 2014, 11:03:13 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibItemNames;
import baubles.api.BaubleType;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemHolyCloak extends ItemBauble implements IBaubleRender {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_HOLY_CLOAK);
	@SideOnly(Side.CLIENT)
	private static ModelBiped model;

	private static final String TAG_COOLDOWN = "cooldown";
	private static final String TAG_IN_EFFECT = "inEffect";

	public ItemHolyCloak() {
		this(LibItemNames.HOLY_CLOAK);
		MinecraftForge.EVENT_BUS.register(this);
	}

	public ItemHolyCloak(String name) {
		super(name);
	}

	@SubscribeEvent
	public void onPlayerDamage(LivingHurtEvent event) {
		if(event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
			ItemStack belt = baubles.getStackInSlot(3);
			if(belt != null && belt.getItem() instanceof ItemHolyCloak && !isInEffect(belt)) {
				ItemHolyCloak cloak = (ItemHolyCloak) belt.getItem();
				int cooldown = getCooldown(belt);

				// Used to prevent StackOverflows with mobs that deal damage when damaged
				setInEffect(belt, true);
				if(cooldown == 0 && cloak.effectOnDamage(event, player, belt))
					setCooldown(belt, cloak.getCooldownTime(belt));
				setInEffect(belt, false);
			}
		}
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		int cooldown = getCooldown(stack);
		if(cooldown > 0)
			setCooldown(stack, cooldown - 1);
	}

	public boolean effectOnDamage(LivingHurtEvent event, EntityPlayer player, ItemStack stack) {
		if(!event.source.isMagicDamage()) {
			event.setCanceled(true);
			player.worldObj.playSoundAtEntity(player, "botania:holyCloak", 1F, 1F);
			for(int i = 0; i < 30; i++) {
				double x = player.posX + Math.random() * player.width * 2 - player.width;
				double y = player.posY + Math.random() * player.height;
				double z = player.posZ + Math.random() * player.width * 2 - player.width;
				boolean yellow = Math.random() > 0.5;
				Botania.proxy.sparkleFX(player.worldObj, x, y, z, yellow ? 1F : 0.3F, yellow ? 1F : 0.3F, yellow ? 0.3F : 1F, 0.8F + (float) Math.random() * 0.4F, 3);
			}
			return true;
		}

		return false;
	}

	public int getCooldownTime(ItemStack stack) {
		return 200;
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.BELT;
	}

	public static int getCooldown(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 0);
	}

	public static void setCooldown(ItemStack stack, int cooldown) {
		ItemNBTHelper.setInt(stack, TAG_COOLDOWN, cooldown);
	}

	public static boolean isInEffect(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_IN_EFFECT, false);
	}

	public static void setInEffect(ItemStack stack, boolean effect) {
		ItemNBTHelper.setBoolean(stack, TAG_IN_EFFECT, effect);
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
			boolean armor = event.entityPlayer.getCurrentArmor(2) != null;
			GL11.glTranslatef(0F, armor ? -0.07F : -0.01F, 0F);

			float s = 0.1F;
			GL11.glScalef(s, s, s);
			if(model == null)
				model = new ModelBiped();

			model.bipedBody.render(1F);
		}
	}

}

