/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.brew.potion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.InstantStatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import javax.annotation.Nonnull;

public class PotionClear extends InstantStatusEffect {

	public PotionClear() {
		super(StatusEffectType.NEUTRAL, 0xFFFFFF);
	}

	@Override
	public void applyInstantEffect(Entity e, Entity e1, @Nonnull LivingEntity e2, int t, double d) {
		e2.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
	}

}
