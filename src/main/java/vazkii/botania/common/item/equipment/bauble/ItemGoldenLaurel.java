/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 27, 2014, 8:49:01 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import baubles.api.BaubleType;
import baubles.common.lib.PlayerHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.api.sound.BotaniaSoundEvents;
import vazkii.botania.common.lib.LibItemNames;

public class ItemGoldenLaurel extends ItemBauble implements IBaubleRender {

	public ItemGoldenLaurel() {
		super(LibItemNames.GOLDEN_LAUREL);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(LivingDeathEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			ItemStack amulet = PlayerHandler.getPlayerBaubles(player).getStackInSlot(0);

			if(amulet != null && amulet.getItem() == this) {
				event.setCanceled(true);
				player.setHealth(player.getMaxHealth());
				player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 300, 6));
				player.addChatMessage(new TextComponentTranslation("botaniamisc.savedByLaurel"));
				player.worldObj.playSound(null, player.posX, player.posY, player.posZ, BotaniaSoundEvents.goldenLaurel, SoundCategory.PLAYERS, 1F, 0.3F);
				PlayerHandler.getPlayerBaubles(player).setInventorySlotContents(0, null);
			}
		}
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.AMULET;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks) {
		if(type == RenderType.HEAD) {
			boolean armor = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD) != null;
			Helper.translateToHeadLevel(player);
			Helper.defaultTransforms();
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			GlStateManager.translate(0F, -3.2F, -1.825F);
			GlStateManager.rotate(-90F, 0F, 1F, 0F);
			GlStateManager.scale(1.625F, 1.625F, 1.625F);
			GlStateManager.rotate(-80F, 1F, 0F, 0F);
			if(armor) {
				GlStateManager.scale(1.1F, 1.1F, 1.1F);
			}
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
		}
	}
}
