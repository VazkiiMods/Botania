/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Dec 4, 2014, 11:12:51 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibItemNames;

public class ItemUnholyCloak extends ItemHolyCloak {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_UNHOLY_CLOAK);

	public ItemUnholyCloak() {
		super(LibItemNames.UNHOLY_CLOAK);
	}

	@Override
	public boolean effectOnDamage(LivingHurtEvent event, EntityPlayer player, ItemStack stack) {
		if(!event.source.isUnblockable()) {
			int range = 6;
			List<IMob> mobs = player.worldObj.getEntitiesWithinAABB(IMob.class, AxisAlignedBB.getBoundingBox(player.posX - range, player.posY - range, player.posZ - range, player.posX + range, player.posY + range, player.posZ + range));
			for(IMob mob : mobs)
				if(mob instanceof EntityLivingBase) {
					EntityLivingBase entity = (EntityLivingBase) mob;
					entity.attackEntityFrom(DamageSource.causePlayerDamage(player), event.ammount);
				}

			player.worldObj.playSoundAtEntity(player, "botania:unholyCloak", 1F, 1F);
			for(int i = 0; i < 90; i++) {
				float rad = i * 4F * (float) Math.PI / 180F;
				float xMotion = (float) Math.cos(rad) * 0.2F;
				float zMotion = (float) Math.sin(rad) * 0.2F;
				Botania.proxy.wispFX(player.worldObj, player.posX, player.posY + 0.5, player.posZ, 0.4F + (float) Math.random() + 0.25F, 0F, 0F, 0.6F + (float) Math.random() * 0.2F, xMotion, 0F, zMotion);
			}

			return true;
		}

		return false;
	}

	@Override
	ResourceLocation getRenderTexture() {
		return texture;
	}

}
