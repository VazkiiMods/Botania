/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 15, 2014, 4:30:08 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.core.helper.MathHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class SubTileVinculotus extends SubTileFunctional {

	public static Set<SubTileVinculotus> existingFlowers = Collections.newSetFromMap(new WeakHashMap());
	private static boolean registered = false;
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		if(!existingFlowers.contains(this)) {
			existingFlowers.add(this);
			if(!registered) {
				MinecraftForge.EVENT_BUS.register(new EndermanIntercepter());
				registered = true;
			}
		}
	}
	
	@Override
	public int getColor() {
		return 0x0A6051;
	}
	
	@Override
	public int getMaxMana() {
		return 500;
	}
	
	public static class EndermanIntercepter {
		
		@SubscribeEvent
		public void onEndermanTeleport(EnderTeleportEvent event) {
			if(event.entity.worldObj.isRemote)
				return;
			
			int cost = 50;
			int range = 64;
			
			if(event.entity instanceof EntityEnderman) {
				for(SubTileVinculotus flower : existingFlowers) {
					if(flower.mana <= cost)
						continue;
					
					double x = flower.supertile.xCoord + 0.5;
					double y = flower.supertile.yCoord + 1.5;
					double z = flower.supertile.zCoord + 0.5;

					if(MathHelper.pointDistanceSpace(x, y, z, event.targetX, event.targetY, event.targetZ) < range) {
						event.targetX = x + Math.random() * 3 - 1;
						event.targetY = y;
						event.targetZ = z + Math.random() * 3 - 1;
						flower.mana -= cost;
						flower.sync();
					}
				}
			}
		}
		
	}
	
}
