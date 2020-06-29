/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.AxisAlignedBB;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;

import java.util.List;

public class SubTileHyacidus extends TileEntityFunctionalFlower {
	private static final int RANGE = 6;
	private static final int COST = 20;

	public SubTileHyacidus() {
		super(ModSubtiles.HYACIDUS);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().isRemote || redstoneSignal > 0) {
			return;
		}

		List<LivingEntity> entities = getWorld().getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(getEffectivePos().add(-RANGE, -RANGE, -RANGE), getEffectivePos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
		for (LivingEntity entity : entities) {
			if (!(entity instanceof PlayerEntity) && entity.getActivePotionEffect(Effects.POISON) == null && getMana() >= COST && !entity.world.isRemote && entity.getCreatureAttribute() != CreatureAttribute.UNDEAD) {
				entity.addPotionEffect(new EffectInstance(Effects.POISON, 60, 0));
				addMana(-COST);
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
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
	}

}
