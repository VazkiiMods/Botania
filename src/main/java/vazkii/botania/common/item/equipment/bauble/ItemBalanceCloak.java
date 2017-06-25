/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [14/10/2016, 19:55:51 (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.lib.LibItemNames;

public class ItemBalanceCloak extends ItemHolyCloak {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_BALANCE_CLOAK);
	private static final ResourceLocation textureGlow = new ResourceLocation(LibResources.MODEL_BALANCE_CLOAK_GLOW);

	public ItemBalanceCloak() {
		super(LibItemNames.BALANCE_CLOAK);
	}
	
	public boolean effectOnDamage(LivingHurtEvent event, EntityPlayer player, ItemStack stack) {
		if(!event.getSource().isMagicDamage()) {
			event.setAmount(event.getAmount() / 2);
			
			if(event.getSource().getTrueSource() != null)
				event.getSource().getTrueSource().attackEntityFrom(DamageSource.causeIndirectMagicDamage(player, player), event.getAmount());
			
			if(event.getAmount() > player.getHealth())
				event.setAmount(player.getHealth() - 1);
			
			player.world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.holyCloak, SoundCategory.PLAYERS, 1F, 1F);
			for(int i = 0; i < 30; i++) {
				double x = player.posX + Math.random() * player.width * 2 - player.width;
				double y = player.posY + Math.random() * player.height;
				double z = player.posZ + Math.random() * player.width * 2 - player.width;
				boolean green = Math.random() > 0.5;
				Botania.proxy.sparkleFX(x, y, z, 0.3F, green ? 1F : 0.3F, green ? 0.3F : 1F, 0.8F + (float) Math.random() * 0.4F, 3);
			}
			return true;
		}

		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	ResourceLocation getCloakTexture() {
		return texture;
	}

	@Override
	@SideOnly(Side.CLIENT)
	ResourceLocation getCloakGlowTexture() {
		return textureGlow;
	}

}
