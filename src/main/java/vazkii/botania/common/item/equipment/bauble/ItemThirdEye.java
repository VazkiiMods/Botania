/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [22/10/2016, 20:18:44 (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.integration.curios.BaseCurio;
import vazkii.botania.common.lib.LibItemNames;

import java.util.List;

public class ItemThirdEye extends ItemBauble implements IManaUsingItem {

	private static final int COST = 2;
	
	public ItemThirdEye(Properties props) {
		super(props);
	}

	public static class Curio extends BaseCurio {
		public Curio(ItemStack stack) {
			super(stack);
		}

		@Override
		public void onCurioTick(String identifier, EntityLivingBase living) {
			if(!(living instanceof EntityPlayer))
				return;
			EntityPlayer eplayer = (EntityPlayer) living;

			double range = 24;
			AxisAlignedBB aabb = new AxisAlignedBB(living.posX, living.posY, living.posZ, living.posX, living.posY, living.posZ).grow(range);
			List<EntityLivingBase> mobs = living.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb, (Entity e) -> e instanceof IMob);

			for(EntityLivingBase e : mobs) {
				PotionEffect potion = e.getActivePotionEffect(MobEffects.GLOWING);
				if((potion == null || potion.getDuration() <= 2) && ManaItemHandler.requestManaExact(stack, eplayer, COST, true))
					e.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 12, 0));
			}
		}

		@Override
		public boolean hasRender(String identifier, EntityLivingBase living) {
			return true;
		}

		@Override
		public void doRender(String identifier, EntityLivingBase living, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
			Minecraft.getInstance().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			boolean armor = !living.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty();
			GlStateManager.rotatef(180, 0, 0, 1);
			GlStateManager.translated(-0.3, -0.6, armor ? -0.18 : -0.12);
			GlStateManager.scaled(0.6, 0.6, 0.6);

			for(int i = 0; i < 3; i++) {
				GlStateManager.pushMatrix();
				float width = 1F / 16F;
				switch(i) {
				case 0: break;
				case 1:
					double scale1 = 0.75;
					width /= 2F;

					GlStateManager.translated(0.15, 0.15, -0.05);
					double time = ClientTickHandler.total * 0.12;
					double dist = 0.05;
					GlStateManager.translated(Math.sin(time) * dist, Math.cos(time * 0.5) * dist, 0);

					GlStateManager.scaled(scale1, scale1, scale1);
					break;
				case 2:
					GlStateManager.translated(0, 0, -0.05);
					break;
				}

				TextureAtlasSprite gemIcon = MiscellaneousIcons.INSTANCE.thirdEyeLayers[i];
				float f = gemIcon.getMinU();
				float f1 = gemIcon.getMaxU();
				float f2 = gemIcon.getMinV();
				float f3 = gemIcon.getMaxV();
				IconHelper.renderIconIn3D(Tessellator.getInstance(), f1, f2, f, f3, gemIcon.getWidth(), gemIcon.getHeight(), width);
				GlStateManager.popMatrix();
			}
		}
	}
	
	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
