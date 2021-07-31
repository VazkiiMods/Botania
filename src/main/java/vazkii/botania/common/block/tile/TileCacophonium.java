/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;

import vazkii.botania.common.item.ItemCacophonium;

public class TileCacophonium extends TileMod {
	private static final String TAG_STACK = "stack";

	public ItemStack stack = ItemStack.EMPTY;

	public TileCacophonium() {
		super(ModTiles.CACOPHONIUM);
	}

	public void annoyDirewolf() {
		ItemCacophonium.playSound(world, stack, pos.getX(), pos.getY(), pos.getZ(), SoundCategory.BLOCKS, 1F);
		if (!world.isClient) {
			float noteColor = world.random.nextInt(25) / 24.0F;
			((ServerWorld) world).spawnParticles(ParticleTypes.NOTE, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 0, noteColor, 0, 0, 1);
		}
	}

	@Override
	public void writePacketNBT(CompoundTag cmp) {
		super.writePacketNBT(cmp);

		CompoundTag cmp1 = new CompoundTag();
		if (!stack.isEmpty()) {
			cmp1 = stack.toTag(cmp1);
		}
		cmp.put(TAG_STACK, cmp1);
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
		super.readPacketNBT(cmp);

		CompoundTag cmp1 = cmp.getCompound(TAG_STACK);
		stack = ItemStack.fromTag(cmp1);
	}

}
