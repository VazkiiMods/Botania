/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import net.minecraft.client.world.GeneratorType;

@Mixin(GeneratorType.class)
public interface AccessorBiomeGeneratorTypeScreens {
	@Accessor("VALUES")
	static List<GeneratorType> getAllTypes() {
		throw new IllegalStateException();
	}
}
