/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity.flower.generating;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.lib.BotaniaTags;

public class ThermalilyBlockEntity extends FluidGeneratorBlockEntity {
	public static final int COOLDOWN_TICKS_MULTIPLER = 6000;
	public static final int BURN_TIME_TICKS = 600;
	public static final int MANA_PER_TICK = 45;
	public static final int MANA_CAPACITY = 750;

	public ThermalilyBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.THERMALILY, pos, state, BotaniaTags.Fluids.THERMALILY_CONSUMABLE,
				BURN_TIME_TICKS, MANA_PER_TICK, COOLDOWN_TICKS_MULTIPLER);
	}

	@Override
	public int getColor() {
		return 0xD03C00;
	}

	@Override
	public void doBurnParticles() {
		WispParticleData data = WispParticleData.wisp((float) Math.random() / 6, 0.7F, 0.05F, 0.05F, 1);
		emitParticle(data, 0.5 + Math.random() * 0.2 - 0.1, 0.9 + Math.random() * 0.2 - 0.1, 0.5 + Math.random() * 0.2 - 0.1, 0, (float) Math.random() / 60, 0);
	}

	@Override
	public void playSound() {
		getLevel().playSound(null, getEffectivePos(), BotaniaSounds.THERMALILY, SoundSource.BLOCKS, 1F, 1F);
	}

	@Override
	public int getMaxMana() {
		return MANA_CAPACITY;
	}

}
