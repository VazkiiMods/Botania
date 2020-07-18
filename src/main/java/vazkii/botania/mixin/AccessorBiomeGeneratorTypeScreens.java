/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.client.gui.screen.BiomeGeneratorTypeScreens;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(BiomeGeneratorTypeScreens.class)
public interface AccessorBiomeGeneratorTypeScreens {
	@Accessor("field_239068_c_")
	static List<BiomeGeneratorTypeScreens> getAllTypes() {
		throw new IllegalStateException();
	}
}
