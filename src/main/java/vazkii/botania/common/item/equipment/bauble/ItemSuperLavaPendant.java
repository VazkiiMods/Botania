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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.item.AccessoryRenderHelper;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.integration.curios.RenderableCurio;

public class ItemSuperLavaPendant extends ItemBauble {

	public ItemSuperLavaPendant(Properties props) {
		super(props);
	}

	public static class Curio extends RenderableCurio {
		public Curio(ItemStack stack) {
			super(stack);
		}

		@Override
		public void onCurioTick(String identifier, EntityLivingBase living) {
			living.isImmuneToFire = true;
		}

		@Override
		public void onUnequipped(String identifier, EntityLivingBase living) {
			living.isImmuneToFire = false;
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		public void doRender(String identifier, EntityLivingBase player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
			Minecraft.getInstance().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			AccessoryRenderHelper.rotateIfSneaking(player);
			boolean armor = !player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty();
			GlStateManager.scaled(0.5, 0.5, 0.5);
			GlStateManager.rotatef(180, 0, 0, 1);
			GlStateManager.translated(-0.5, -0.90, armor ? -0.4 : -0.25);

			TextureAtlasSprite gemIcon = MiscellaneousIcons.INSTANCE.crimsonGem;
			float f = gemIcon.getMinU();
			float f1 = gemIcon.getMaxU();
			float f2 = gemIcon.getMinV();
			float f3 = gemIcon.getMaxV();
			IconHelper.renderIconIn3D(Tessellator.getInstance(), f1, f2, f, f3, gemIcon.getWidth(), gemIcon.getHeight(), 1F / 32F);
		}
	}
}
