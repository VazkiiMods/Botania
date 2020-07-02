/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.Explosion;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.world.ExplosionEvent;

import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.item.ModItems;

import java.util.List;

public class ItemGoddessCharm extends ItemBauble implements IManaUsingItem {

	public static final int COST = 1000;

	public ItemGoddessCharm(Properties props) {
		super(props);
	}

	public static void onExplosion(ExplosionEvent.Detonate event) {
		Explosion e = event.getExplosion();
		Vector3d vec = e.getPosition();
		List<PlayerEntity> players = event.getWorld().getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(vec.x, vec.y, vec.z, vec.x, vec.y, vec.z).grow(8));

		for (PlayerEntity player : players) {
			ItemStack charm = EquipmentHandler.findOrEmpty(ModItems.goddessCharm, player);
			if (!charm.isEmpty() && ManaItemHandler.instance().requestManaExact(charm, player, COST, true)) {
				event.getAffectedBlocks().clear();
				return;
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void doRender(BipedModel<?> bipedModel, ItemStack stack, LivingEntity player, MatrixStack ms, IRenderTypeBuffer buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		bipedModel.bipedHead.translateRotate(ms);
		ms.translate(0.275, -0.4, 0);
		ms.rotate(Vector3f.YP.rotationDegrees(-90F));
		ms.scale(0.55F, -0.55F, -0.55F);
		Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.NONE, light, OverlayTexture.NO_OVERLAY, ms, buffers);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
