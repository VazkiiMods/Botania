/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 26, 2014, 11:23:27 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.integration.curios.BaseCurio;
import vazkii.botania.common.lib.LibItemNames;

public class ItemLavaPendant extends ItemBauble {

	public ItemLavaPendant(Properties props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		if(player.isBurning())
			player.extinguish();
	}

	public static class Curio extends BaseCurio {
		public Curio(ItemStack stack) {
			super(stack);
		}

		@Override
		public boolean hasRender(String identifier, EntityLivingBase living) {
			return true;
		}

		@Override
		public void doRender(String identifier, EntityLivingBase player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
			Minecraft.getInstance().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			IBaubleRender.Helper.rotateIfSneaking(player);
			boolean armor = !player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty();
			GlStateManager.scalef(0.5F, 0.5F, 0.5F);
			GlStateManager.rotatef(180, 0, 0, 1);
			GlStateManager.translated(-0.5, -0.90, armor ? -0.4 : -0.25);

			TextureAtlasSprite gemIcon = MiscellaneousIcons.INSTANCE.pyroclastGem;
			float f = gemIcon.getMinU();
			float f1 = gemIcon.getMaxU();
			float f2 = gemIcon.getMinV();
			float f3 = gemIcon.getMaxV();
			IconHelper.renderIconIn3D(Tessellator.getInstance(), f1, f2, f, f3, gemIcon.getWidth(), gemIcon.getHeight(), 1F / 32F);
		}
	}
}
