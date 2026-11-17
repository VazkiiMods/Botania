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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.MathHelper;
import vazkii.botania.common.internal_caps.SlimeChunkSpawned;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;

public class NarslimmusBlockEntity extends GeneratingFlowerBlockEntity {

	private static final int RANGE = 2;
	private static final int MAX_MANA = manaForSize(4);
	public static final int MANA_BASE = 1200;
	public static final int MANA_BASE_GOG = MANA_BASE / 4;

	public NarslimmusBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.NARSLIMMUS, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!shouldUpdateThisTick()) {
			return;
		}

		List<Slime> slimes = getLevel().getEntitiesOfClass(Slime.class,
				MathHelper.inflateBoxAround(getEffectivePos(), RANGE),
				slime -> slime.isAlive() && SlimeChunkSpawned.MARKER.existsFor(slime));
		for (Slime slime : slimes) {
			int size = slime.getSize();
			if (!slime.level().isClientSide()) {
				slime.discard();
				slime.playSound(size > 1 ? BotaniaSounds.NARSLIMMUS_EAT_BIG : BotaniaSounds.NARSLIMMUS_EAT_SMALL, 1F, 1F);
				addMana(manaForSize(size));
			}

			int times = 8 * (int) Math.pow(2, size);
			for (int j = 0; j < times; ++j) {
				float f = slime.level().getRandom().nextFloat() * (float) Math.PI * 2.0F;
				float f1 = slime.level().getRandom().nextFloat() * 0.5F + 0.5F;
				float f2 = Mth.sin(f) * size * 0.5F * f1;
				float f3 = Mth.cos(f) * size * 0.5F * f1;
				float f4 = slime.level().getRandom().nextFloat() * size * 0.5F * f1;
				slime.level().addParticle(ParticleTypes.ITEM_SLIME,
						slime.getX() + f2, slime.getBoundingBox().minY + f4, slime.getZ() + f3,
						0.0D, 0.0D, 0.0D);
			}
			break;
		}
	}

	@Override
	protected int getUpdateInterval() {
		return 5;
	}

	private static int manaForSize(int size) {
		size = Math.min(size, 4);
		return (XplatAbstractions.INSTANCE.gogLoaded() ? MANA_BASE_GOG : MANA_BASE) * (int) Math.pow(2, size);
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
	}

	@Override
	public int getMaxMana() {
		return MAX_MANA;
	}

	@Override
	public int getColor() {
		return 0x71C373;
	}
}
