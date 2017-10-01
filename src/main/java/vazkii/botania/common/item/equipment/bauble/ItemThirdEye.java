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

import baubles.api.BaubleType;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.lib.LibItemNames;

import java.util.List;

public class ItemThirdEye extends ItemBauble implements IManaUsingItem, IBaubleRender {

	private static final int COST = 2;
	
	public ItemThirdEye() {
		super(LibItemNames.THIRD_EYE);
	}
	
	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		super.onWornTick(stack, player);
		
		if(!(player instanceof EntityPlayer))
			return;
		EntityPlayer eplayer = (EntityPlayer) player;
		
		double range = 24;
		AxisAlignedBB aabb = new AxisAlignedBB(player.posX, player.posY, player.posZ, player.posX, player.posY, player.posZ).grow(range);
		List<EntityLivingBase> mobs = player.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb, (Entity e) -> e instanceof IMob);
		
		if(!mobs.isEmpty())
			for(EntityLivingBase e : mobs) {
				PotionEffect potion = e.getActivePotionEffect(MobEffects.GLOWING);
				if((potion == null || potion.getDuration() <= 2) && ManaItemHandler.requestManaExact(stack, eplayer, COST, true))
					e.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 12, 0));
			}
	}
	
	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.BODY;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks) {
		if(type == RenderType.BODY) {
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Helper.rotateIfSneaking(player);
			boolean armor = !player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty();
			double scale = 0.6;
			GlStateManager.rotate(180, 0, 0, 1);
			GlStateManager.translate(-0.3, -0.6, armor ? -0.18 : -0.12);
			GlStateManager.scale(scale, scale, scale);

			for(int i = 0; i < 3; i++) {
				GlStateManager.pushMatrix();
				float width = 1F / 16F;
				switch(i) {
				case 0: break;
				case 1:
					double scale1 = 0.75;
					width /= 2F;
					
					GlStateManager.translate(0.15, 0.15, -0.05);
					double time = ClientTickHandler.total * 0.12;
					double dist = 0.05;
					GlStateManager.translate(Math.sin(time) * dist, Math.cos(time * 0.5) * dist, 0);
					
					GlStateManager.scale(scale1, scale1, scale1);
					break;
				case 2:
					GlStateManager.translate(0, 0, -0.05);
					break;
				}
				
				TextureAtlasSprite gemIcon = MiscellaneousIcons.INSTANCE.thirdEyeLayers[i];
				float f = gemIcon.getMinU();
				float f1 = gemIcon.getMaxU();
				float f2 = gemIcon.getMinV();
				float f3 = gemIcon.getMaxV();
				IconHelper.renderIconIn3D(Tessellator.getInstance(), f1, f2, f, f3, gemIcon.getIconWidth(), gemIcon.getIconHeight(), width);
				GlStateManager.popMatrix();
			}
		}
	}

}
