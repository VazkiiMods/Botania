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

public class GlimmeringFlowerBlock extends BotaniaFlowerBlock {

	public GlimmeringFlowerBlock(DyeColor color, Holder<MobEffect> effect, int seconds, Properties builder) {
		super(color, effect, seconds, builder);
	}

}
