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

import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.fx.ParticleData;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;

public class SubTileThermalily extends SubTileHydroangeas {
	@ObjectHolder(LibMisc.MOD_ID + ":thermalily")
	public static TileEntityType<SubTileThermalily> TYPE;

	public SubTileThermalily() {
		super(TYPE);
	}

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
        ParticleData data = ParticleData.wisp((float) Math.random() / 6, 0.7F, 0.05F, 0.05F, 1);
        world.addParticle(data, getPos().getX() + 0.55 + Math.random() * 0.2 - 0.1, getPos().getY() + 0.9 + Math.random() * 0.2 - 0.1, getPos().getZ() + 0.5, 0, (float) Math.random() / 60, 0);
    }

	@Override
	public Tag<Fluid> getMaterialToSearchFor() {
		return FluidTags.LAVA;
	}

	@Override
	public void playSound() {
		getWorld().playSound(null, getPos(), ModSounds.thermalily, SoundCategory.BLOCKS, 0.2F, 1F);
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
