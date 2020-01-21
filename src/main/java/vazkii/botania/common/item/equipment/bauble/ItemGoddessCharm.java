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

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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
		List<PlayerEntity> players = event.getWorld().getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(vec.x, vec.y, vec.z, vec.x, vec.y, vec.z).grow(8));
		
		for(PlayerEntity player : players) {
			ItemStack charm = EquipmentHandler.findOrEmpty(ModItems.goddessCharm, player);
			if(!charm.isEmpty() && ManaItemHandler.requestManaExact(charm, player, COST, true))  {
				event.getAffectedBlocks().clear();
				return;
			}
		}
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void doRender(ItemStack stack, LivingEntity player, MatrixStack ms, IRenderTypeBuffer buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		AccessoryRenderHelper.translateToHeadLevel(ms, player, partialTicks);
		AccessoryRenderHelper.translateToFace(ms);
		AccessoryRenderHelper.defaultTransforms(ms);
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-90F));
		ms.translate(0.5F, 0.2F, 0.45F);
		Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.NONE, light, OverlayTexture.DEFAULT_UV, ms, buffers);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
