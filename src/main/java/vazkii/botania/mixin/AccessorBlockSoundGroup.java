/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockSoundGroup.class)
public interface AccessorBlockSoundGroup {
	@Accessor("breakSound")
	SoundEvent botania_getBreakSound();
}
