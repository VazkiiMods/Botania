/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 31, 2014, 6:09:17 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import vazkii.botania.api.item.AccessoryRenderHelper;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.core.handler.EquipmentHandler;

public class ItemSuperLavaPendant extends ItemBauble {

	public ItemSuperLavaPendant(Properties props) {
		super(props);
		MinecraftForge.EVENT_BUS.addListener(this::onDamage);
	}

	private void onDamage(LivingHurtEvent evt) {
		if(evt.getSource().isFireDamage()
				&& !EquipmentHandler.findOrEmpty(this, evt.getEntityLiving()).isEmpty()) {
			evt.setCanceled(true);
		}
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity living) {
		if (living.isBurning())
			living.extinguish();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void doRender(ItemStack stack, LivingEntity player, MatrixStack ms, IRenderTypeBuffer buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		AccessoryRenderHelper.rotateIfSneaking(player);
		boolean armor = !player.getItemStackFromSlot(EquipmentSlotType.CHEST).isEmpty();
		ms.scale(0.5F, 0.5F, 0.5F);
		ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
		ms.translate(-0.5, -0.90, armor ? -0.4 : -0.25);

		TextureAtlasSprite gemIcon = MiscellaneousIcons.INSTANCE.crimsonGem;
		float f = gemIcon.getMinU();
		float f1 = gemIcon.getMaxU();
		float f2 = gemIcon.getMinV();
		float f3 = gemIcon.getMaxV();
		IconHelper.renderIconIn3D(Tessellator.getInstance(), f1, f2, f, f3, gemIcon.getWidth(), gemIcon.getHeight(), 1F / 32F);
	}
}
