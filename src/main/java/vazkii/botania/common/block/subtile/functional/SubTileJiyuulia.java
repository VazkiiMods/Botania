/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 14, 2014, 11:40:51 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.LivingEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.common.lib.LibMisc;

public class SubTileJiyuulia extends SubTileTangleberrie {
	@ObjectHolder(LibMisc.MOD_ID + ":jiyuulia")
	public static TileEntityType<SubTileJiyuulia> TYPE;

	public SubTileJiyuulia() {
		super(TYPE);
	}

	@Override
	double getMaxDistance() {
		return 0;
	}

	@Override
	double getRange() {
		return 8;
	}

	@Override
	float getMotionVelocity(LivingEntity entity) {
		return -super.getMotionVelocity(entity) * 2;
	}

	@Override
	public int getColor() {
		return 0xBD9ACA;
	}

}
