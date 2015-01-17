/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 17, 2015, 7:27:04 PM (GMT)]
 */
package vazkii.botania.common.block.tile.mana;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.lib.LibBlockNames;
import codechicken.lib.vec.Vector3;

public class TilePrism extends TileSimpleInventory implements IManaCollisionGhost {

	public void onBurstCollision(IManaBurst burst) {
		ItemStack lens = getStackInSlot(0);
		boolean valid = lens != null && lens.getItem() instanceof ILens; 
		
		burst.setSourceLens(valid ? lens.copy() : null);
		burst.setColor(0xFFFFFF);
		burst.setGravity(0F);
		
		if(valid) {
			Entity burstEntity = (Entity) burst;
			BurstProperties properties = new BurstProperties(burst.getStartingMana(), burst.getMinManaLoss(), burst.getManaLossPerTick(), burst.getGravity(), 1F, burst.getColor());
			
			((ILens) lens.getItem()).apply(lens, properties);
			
			burst.setColor(properties.color);
			burst.setStartingMana(properties.maxMana);
			burst.setMinManaLoss(properties.ticksBeforeManaLoss);
			burst.setManaLossPerTick(properties.manaLossPerTick);
			burst.setGravity(properties.gravity);
			burst.setMotion(burstEntity.motionX * properties.motionModifier, burstEntity.motionY * properties.motionModifier,burstEntity.motionZ * properties.motionModifier);
		}
	}
	
	@Override
	public boolean isGhost() {
		return true;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public String getInventoryName() {
		return LibBlockNames.PRISM;
	}

}
