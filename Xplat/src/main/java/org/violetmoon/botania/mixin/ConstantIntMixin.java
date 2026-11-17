/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.mixin;

import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

/**
 * No idea why {@link ConstantInt} doesn't override equals and hashCode, like
 * {@link net.minecraft.world.level.storage.loot.providers.number.ConstantValue} does. It changes to a record in later
 * versions, but having it now is of great convenience for datagen and codecs using a default value other than zero.
 * Since instances of the class are immutable, this shouldn't cause issues elsewhere.
 */
@Mixin(ConstantInt.class)
public abstract class ConstantIntMixin extends IntProvider {
	@Shadow
	@Final
	private int value;

	@Override
	public boolean equals(Object o) {
		return o instanceof ConstantInt that && getType() == that.getType() && value == that.getValue();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
