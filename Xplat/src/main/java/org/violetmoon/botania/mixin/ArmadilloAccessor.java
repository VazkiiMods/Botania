/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.mixin;

import net.minecraft.world.entity.animal.armadillo.Armadillo;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Armadillo.class)
public interface ArmadilloAccessor {
	@Accessor("scuteTime")
	int botania_getScuteTime();

	@Accessor("scuteTime")
	void botania_setScuteTime(int scuteTime);
}
