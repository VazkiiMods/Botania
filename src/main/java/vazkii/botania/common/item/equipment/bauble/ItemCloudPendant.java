/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [22/10/2016, 16:52:36 (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import vazkii.botania.api.item.AccessoryRenderHelper;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.network.PacketHandler;
import vazkii.botania.common.network.PacketJump;

public class ItemCloudPendant extends ItemBauble {

	private static int timesJumped;
	private static boolean jumpDown;
	
	public ItemCloudPendant(Properties props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			if(player instanceof EntityPlayerSP && player == Minecraft.getInstance().player) {
				EntityPlayerSP playerSp = (EntityPlayerSP) player;

				if (playerSp.onGround)
					timesJumped = 0;
				else {
					if(playerSp.movementInput.jump) {
						if (!jumpDown && timesJumped < ((ItemCloudPendant) stack.getItem()).getMaxAllowedJumps()) {
							playerSp.jump();
							PacketHandler.sendToServer(new PacketJump());
							timesJumped++;
						}
						jumpDown = true;
					} else jumpDown = false;
				}
			}
		});
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void doRender(ItemStack stack, EntityLivingBase player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		Minecraft.getInstance().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		AccessoryRenderHelper.rotateIfSneaking(player);
		boolean armor = !player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty();
		GlStateManager.rotatef(180F, 1F, 0F, 0F);
		GlStateManager.translatef(-0.2F, -0.3F, armor ? 0.2F : 0.15F);
		GlStateManager.scalef(0.5F, 0.5F, 0.5F);

		TextureAtlasSprite gemIcon = stack.getItem() == ModItems.superCloudPendant
				? MiscellaneousIcons.INSTANCE.nimbusGem
				: MiscellaneousIcons.INSTANCE.cirrusGem;
		float f = gemIcon.getMinU();
		float f1 = gemIcon.getMaxU();
		float f2 = gemIcon.getMinV();
		float f3 = gemIcon.getMaxV();
		IconHelper.renderIconIn3D(Tessellator.getInstance(), f1, f2, f, f3, gemIcon.getWidth(), gemIcon.getHeight(), 1F / 32F);
	}

	public int getMaxAllowedJumps() {
		return 2;
	}

}
