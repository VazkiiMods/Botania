/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 4, 2014, 10:38:50 PM (GMT)]
 */
package vazkii.botania.common.core.handler;

import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class SheddingHandler {

	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		if(event.entity.worldObj.isRemote)
			return;
			
		if(event.entity instanceof EntityChicken) {
			if(ConfigHandler.shedRateChicken != -1 && event.entity.worldObj.rand.nextInt(ConfigHandler.shedRateChicken) == 0)
				event.entity.dropItem(Items.feather, 1);
		} else if(event.entity instanceof EntitySquid) {
			if(ConfigHandler.shedRateSquid != -1 && event.entity.worldObj.rand.nextInt(ConfigHandler.shedRateSquid) == 0)
				event.entity.dropItem(Items.dye, 1);
		} else if(event.entity instanceof EntityVillager) {
			if(ConfigHandler.shedRateVillager != -1 && event.entity.worldObj.rand.nextInt(ConfigHandler.shedRateVillager) == 0)
				event.entity.dropItem(Items.emerald, 1);
		} else if(event.entity instanceof EntitySpider) {
			if(ConfigHandler.shedRateSpider != -1 && event.entity.worldObj.rand.nextInt(ConfigHandler.shedRateSpider) == 0)
				event.entity.dropItem(Items.string, 1);
		} else if(event.entity instanceof EntityBlaze) {
			if(ConfigHandler.shedRateBlaze != -1 && event.entity.worldObj.rand.nextInt(ConfigHandler.shedRateBlaze) == 0)
				event.entity.dropItem(Items.blaze_rod, 1);
		} else if(event.entity instanceof EntityGhast) {
			if(ConfigHandler.shedRateGhast != -1 && event.entity.worldObj.rand.nextInt(ConfigHandler.shedRateGhast) == 0)
				event.entity.dropItem(Items.ghast_tear, 1);
		} else if(event.entity instanceof EntitySkeleton) {
			if(ConfigHandler.shedRateSkeleton != -1 && event.entity.worldObj.rand.nextInt(ConfigHandler.shedRateSkeleton) == 0)
				event.entity.dropItem(Items.bone, 1);
		} else if(event.entity instanceof EntitySlime) {
			if(ConfigHandler.shedRateSlime != -1 && event.entity.worldObj.rand.nextInt(ConfigHandler.shedRateSlime) == 0)
				event.entity.dropItem(Items.slime_ball, 1);
		}
	}
	
}
