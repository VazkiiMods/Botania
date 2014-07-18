/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 18, 2014, 8:05:08 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TileTinyPotato extends TileMod {

	public int jumpTicks = 0;
	
	public void jump() {
		if(jumpTicks == 0)
			jumpTicks = 20;
	}
	
	@Override
	public void updateEntity() {
		if(worldObj.rand.nextInt(100) == 0)
			jump();
		
		if(jumpTicks > 0)
			jumpTicks--;
	}
}
