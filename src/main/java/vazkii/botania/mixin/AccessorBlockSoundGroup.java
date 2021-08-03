/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SoundType.class)
public interface AccessorBlockSoundGroup {
	@Accessor("breakSound")
	SoundEvent botania_getBreakSound();
}
