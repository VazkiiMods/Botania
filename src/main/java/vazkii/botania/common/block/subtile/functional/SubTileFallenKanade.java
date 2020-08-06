/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;

import java.util.List;

public class SubTileFallenKanade extends TileEntityFunctionalFlower {
	private static final int RANGE = 2;
	private static final int COST = 120;

	public SubTileFallenKanade() {
		super(ModSubtiles.FALLEN_KANADE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!getWorld().isRemote && getWorld().func_234923_W_() != World.field_234920_i_) {
			boolean did = false;
			List<PlayerEntity> players = getWorld().getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(getEffectivePos().add(-RANGE, -RANGE, -RANGE), getEffectivePos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
			for (PlayerEntity player : players) {
				if (player.getActivePotionEffect(Effects.REGENERATION) == null && getMana() >= COST) {
					player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 59, 2, true, true));
					addMana(-COST);
					did = true;
				}
			}
			if (did) {
				sync();
			}
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
	}

	@Override
	public int getColor() {
		return 0xFFFF00;
	}

	@Override
	public int getMaxMana() {
		return 900;
	}

}
