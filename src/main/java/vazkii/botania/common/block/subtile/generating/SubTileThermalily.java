/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 10, 2014, 9:44:02 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.block.material.Material;
import net.minecraft.util.SoundCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileThermalily extends SubTileHydroangeas {

	@Override
	public int getColor(){
		return 0xD03C00;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.thermalily;
	}

	@Override
	public void doBurnParticles() {
		Botania.proxy.wispFX(supertile.getPos().getX() + 0.55 + Math.random() * 0.2 - 0.1, supertile.getPos().getY() + 0.9 + Math.random() * 0.2 - 0.1, supertile.getPos().getZ() + 0.5, 0.7F, 0.05F, 0.05F, (float) Math.random() / 6, (float) -Math.random() / 60);
	}

	@Override
	public Material getMaterialToSearchFor() {
		return Material.LAVA;
	}

	@Override
	public void playSound() {
		supertile.getWorld().playSound(null, supertile.getPos(), ModSounds.thermalily, SoundCategory.BLOCKS, 0.2F, 1F);
	}

	@Override
	public int getDelayBetweenPassiveGeneration() {
		return 1;
	}

	@Override
	public int getBurnTime() {
		return 900;
	}

	@Override
	public int getValueForPassiveGeneration() {
		return 20;
	}

	@Override
	public int getMaxMana() {
		return 500;
	}

	@Override
	public int getCooldown() {
		return 6000;
	}

	@Override
	public boolean isPassiveFlower() {
		return false;
	}
}
