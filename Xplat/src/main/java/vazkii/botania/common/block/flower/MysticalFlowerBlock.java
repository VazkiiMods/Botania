/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.block.flower;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class MysticalFlowerBlock extends BotaniaFlowerBlock implements TallFlowerGrower {

	private final Function<TallFlowerGrower, @Nullable Block> tallFlowerFunction;

	public MysticalFlowerBlock(DyeColor color, Holder<MobEffect> effect, int seconds,
			Function<TallFlowerGrower, @Nullable Block> tallFlowerFunction, Properties builder) {
		super(color, effect, seconds, builder);
		this.tallFlowerFunction = tallFlowerFunction;
	}

	@Override
	public @Nullable Block getTallFlower() {
		return tallFlowerFunction.apply(this);
	}
}
