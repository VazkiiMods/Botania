/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.common.item.ItemCacophonium;

public class TileCacophonium extends TileMod {
	private static final String TAG_STACK = "stack";

	public ItemStack stack = ItemStack.EMPTY;

	public TileCacophonium() {
		super(ModTiles.CACOPHONIUM);
	}

	public void annoyDirewolf() {
		ItemCacophonium.playSound(level, stack, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), SoundSource.BLOCKS, 1F);
		if (!level.isClientSide) {
			float noteColor = level.random.nextInt(25) / 24.0F;
			((ServerLevel) level).sendParticles(ParticleTypes.NOTE, worldPosition.getX() + 0.5, worldPosition.getY() + 1.2, worldPosition.getZ() + 0.5, 0, noteColor, 0, 0, 1);
		}
	}

	@Override
	public void writePacketNBT(CompoundTag cmp) {
		super.writePacketNBT(cmp);

		CompoundTag cmp1 = new CompoundTag();
		if (!stack.isEmpty()) {
			cmp1 = stack.save(cmp1);
		}
		cmp.put(TAG_STACK, cmp1);
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
		super.readPacketNBT(cmp);

		CompoundTag cmp1 = cmp.getCompound(TAG_STACK);
		stack = ItemStack.of(cmp1);
	}

}
