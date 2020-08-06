/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.entity.boss.WitherEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WitherEntity.class)
public interface AccessorWitherEntity {
	@Invoker
	double callGetHeadX(int idx);

	@Invoker
	double callGetHeadY(int idx);

	@Invoker
	double callGetHeadZ(int idx);
}
