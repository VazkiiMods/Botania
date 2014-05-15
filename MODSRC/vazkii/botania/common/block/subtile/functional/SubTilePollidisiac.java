/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [May 15, 2014, 5:56:47 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.lib.LibObfuscation;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class SubTilePollidisiac extends SubTileFunctional {

	@Override
	public void onUpdate() {
		super.onUpdate();
		
		if(!supertile.getWorldObj().isRemote) {
			int range = 6;
			int manaCost = 12;
			
			List<EntityItem> items = supertile.getWorldObj().getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(supertile.xCoord - range, supertile.yCoord, supertile.zCoord - range, supertile.xCoord + range, supertile.yCoord + 1, supertile.zCoord + range));
			List<EntityAnimal> animals = supertile.getWorldObj().getEntitiesWithinAABB(EntityAnimal.class, AxisAlignedBB.getBoundingBox(supertile.xCoord - range, supertile.yCoord, supertile.zCoord - range, supertile.xCoord + range, supertile.yCoord + 1, supertile.zCoord + range));
			for(EntityAnimal animal : animals) {
				if(mana < manaCost)
					break;
				
				int love = ReflectionHelper.getPrivateValue(EntityAnimal.class, animal, LibObfuscation.IN_LOVE);
				if(animal.getGrowingAge() == 0 && love <= 0) {
					for(EntityItem item : items) {
						if(item.isDead || item.age < 60)
							continue;
						
						ItemStack stack = item.getEntityItem();
						if(animal.isBreedingItem(stack)) {
							stack.stackSize--;
							if(stack.stackSize == 0)
								item.setDead();
							
							mana -= manaCost;
							
							ReflectionHelper.setPrivateValue(EntityAnimal.class, animal, 800, LibObfuscation.IN_LOVE);
							animal.setTarget(null);
							supertile.getWorldObj().setEntityState(animal, (byte)18);
						}
					}
				}
			}
		}
	}
	
	@Override
	public int getMaxMana() {
		return 120;
	}
	
	@Override
	public int getColor() {
		return 0xCF4919;
	}
	
}
