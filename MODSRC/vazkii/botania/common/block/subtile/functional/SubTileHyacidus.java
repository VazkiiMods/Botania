/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [May 15, 2014, 4:47:41 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileHyacidus extends SubTileFunctional {

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(redstoneSignal > 0)
			return;

		final int range = 6;
		final int cost = 20;

		List<EntityLivingBase> entities = supertile.getWorldObj().getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(supertile.xCoord - range, supertile.yCoord - range, supertile.zCoord - range, supertile.xCoord + range, supertile.yCoord + range, supertile.zCoord + range));
		for(EntityLivingBase entity : entities) {
			if(!(entity instanceof EntityPlayer) && entity.getActivePotionEffect(Potion.poison) == null && mana >= cost) {
				entity.addPotionEffect(new PotionEffect(Potion.poison.id, 60, 0));
				mana -= cost;
			}
		}
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public int getColor() {
		return 0x8B438F;
	}

	@Override
	public int getMaxMana() {
		return 180;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.hyacidus;
	}

}