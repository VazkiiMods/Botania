/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.world.entity.boss.wither.WitherBoss;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WitherBoss.class)
public interface AccessorWitherEntity {
	@Invoker("getHeadX")
	double botania_getHeadX(int idx);

	@Invoker("getHeadY")
	double botania_getHeadY(int idx);

	@Invoker("getHeadZ")
	double botania_getHeadZ(int idx);
}
