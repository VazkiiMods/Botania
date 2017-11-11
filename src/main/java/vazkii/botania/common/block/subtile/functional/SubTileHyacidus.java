/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 15, 2014, 4:47:41 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.List;

public class SubTileHyacidus extends SubTileFunctional {

	private static final int RANGE = 6;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(supertile.getWorld().isRemote || redstoneSignal > 0)
			return;

		final int cost = 20;

		List<EntityLivingBase> entities = supertile.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(supertile.getPos().add(-RANGE, -RANGE, -RANGE), supertile.getPos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
		for(EntityLivingBase entity : entities) {
			if(!(entity instanceof EntityPlayer) && entity.getActivePotionEffect(MobEffects.POISON) == null && mana >= cost && !entity.world.isRemote && entity.getCreatureAttribute() != EnumCreatureAttribute.UNDEAD) {
				entity.addPotionEffect(new PotionEffect(MobEffects.POISON, 60, 0));
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
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), RANGE);
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.hyacidus;
	}

}