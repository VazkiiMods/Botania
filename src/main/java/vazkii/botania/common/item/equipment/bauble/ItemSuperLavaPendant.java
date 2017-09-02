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

import baubles.api.BaubleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.lib.LibItemNames;

public class ItemSuperLavaPendant extends ItemBauble implements IBaubleRender {

	public ItemSuperLavaPendant() {
		super(LibItemNames.SUPER_LAVA_PENDANT);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase living) {
		living.isImmuneToFire = true;
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase living) {
		living.isImmuneToFire = false;
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.AMULET;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks) {
		if(type == RenderType.BODY) {
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Helper.rotateIfSneaking(player);
			boolean armor = !player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty();
			GlStateManager.scale(0.5, 0.5, 0.5);
			GlStateManager.rotate(180, 0, 0, 1);
			GlStateManager.translate(-0.5, -0.90, armor ? -0.4 : -0.25);

			TextureAtlasSprite gemIcon = MiscellaneousIcons.INSTANCE.crimsonGem;
			float f = gemIcon.getMinU();
			float f1 = gemIcon.getMaxU();
			float f2 = gemIcon.getMinV();
			float f3 = gemIcon.getMaxV();
			IconHelper.renderIconIn3D(Tessellator.getInstance(), f1, f2, f, f3, gemIcon.getIconWidth(), gemIcon.getIconHeight(), 1F / 32F);
		}
	}
}
