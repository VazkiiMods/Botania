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

import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileJiyuulia extends SubTileTangleberrie {

	@Override
	double getMaxDistance() {
		return 0;
	}

	@Override
	double getRange() {
		return 8;
	}

	@Override
	float getMotionVelocity() {
		return -super.getMotionVelocity() * 2;
	}

	@Override
	public int getColor() {
		return 0xBD9ACA;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.jiyuulia;
	}

}
