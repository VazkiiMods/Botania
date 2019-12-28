/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 14, 2014, 8:54:11 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.dimension.EndDimension;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;

public class SubTileFallenKanade extends TileEntityFunctionalFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":fallen_kanade")
	public static TileEntityType<SubTileFallenKanade> TYPE;

	private static final int RANGE = 2;
	private static final int COST = 120;

	public SubTileFallenKanade() {
		super(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if(!getWorld().isRemote && !(getWorld().getDimension() instanceof EndDimension)) {
			boolean did = false;
			List<PlayerEntity> players = getWorld().getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(getEffectivePos().add(-RANGE, -RANGE, -RANGE), getEffectivePos().add(RANGE + 1, RANGE + 1, RANGE + 1)));
			for(PlayerEntity player : players) {
				if(player.getActivePotionEffect(Effects.REGENERATION) == null && getMana() >= COST) {
					player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 59, 2, true, true));
					addMana(-COST);
					did = true;
				}
			}
			if(did)
				sync();
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
