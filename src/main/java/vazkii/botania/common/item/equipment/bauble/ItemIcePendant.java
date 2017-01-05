/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 26, 2014, 2:06:17 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import baubles.api.BaubleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.enchantment.EnchantmentFrostWalker;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.lib.LibItemNames;

public class ItemIcePendant extends ItemBauble implements IBaubleRender {

	public ItemIcePendant() {
		super(LibItemNames.ICE_PENDANT);
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.AMULET;
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase entity) {
		super.onWornTick(stack, entity);
		if(!entity.world.isRemote) {
			boolean lastOnGround = entity.onGround;
			entity.onGround = true;
			EnchantmentFrostWalker.freezeNearby(entity, entity.world, new BlockPos(entity), 8);
			entity.onGround = lastOnGround;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks) {
		if(type == RenderType.BODY) {
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Helper.rotateIfSneaking(player);
			boolean armor = !player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty();
			GlStateManager.rotate(180F, 1F, 0F, 0F);
			GlStateManager.translate(-0.36F, -0.3F, armor ? 0.2F : 0.15F);
			GlStateManager.rotate(-45F, 0F, 0F, 1F);
			GlStateManager.scale(0.5F, 0.5F, 0.5F);

			TextureAtlasSprite gemIcon = MiscellaneousIcons.INSTANCE.snowflakePendantGem;
			float f = gemIcon.getMinU();
			float f1 = gemIcon.getMaxU();
			float f2 = gemIcon.getMinV();
			float f3 = gemIcon.getMaxV();
			IconHelper.renderIconIn3D(Tessellator.getInstance(), f1, f2, f, f3, gemIcon.getIconWidth(), gemIcon.getIconHeight(), 1F / 32F);
		}
	}

}
