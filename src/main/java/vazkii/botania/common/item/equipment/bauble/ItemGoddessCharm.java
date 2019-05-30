/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Oct 30, 2016, 6:22:03 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.api.item.AccessoryRenderHelper;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class ItemGoddessCharm extends ItemBauble implements IManaUsingItem {

	public static final int COST = 1000;
	
	public ItemGoddessCharm(Properties props) {
		super(props);
	}

	@SubscribeEvent
	public static void onExplosion(ExplosionEvent.Detonate event) {
		Explosion e = event.getExplosion();
		Vec3d vec = e.getPosition();
		List<EntityPlayer> players = event.getWorld().getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(vec.x, vec.y, vec.z, vec.x, vec.y, vec.z).grow(8));
		
		for(EntityPlayer player : players) {
			ItemStack charm = EquipmentHandler.findOrEmpty(ModItems.goddessCharm, player);
			if(!charm.isEmpty() && ManaItemHandler.requestManaExact(charm, player, COST, true))  {
				event.getAffectedBlocks().clear();
				return;
			}
		}
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void doRender(ItemStack stack, EntityLivingBase player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		GlStateManager.pushMatrix();
		AccessoryRenderHelper.translateToHeadLevel(player);
		AccessoryRenderHelper.translateToFace();
		AccessoryRenderHelper.defaultTransforms();
		GlStateManager.rotatef(-90F, 0F, 1F, 0F);
		GlStateManager.translatef(0.5F, 0.2F, 0.45F);
		Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
		GlStateManager.popMatrix();
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
