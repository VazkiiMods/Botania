/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.block.subtile.functional.SubTileTigerseye;

@Mixin(Creeper.class)
public class MixinCreeper extends Monster {
	protected MixinCreeper(EntityType<? extends Monster> type, Level level) {
		super(type, level);
	}

	// Lithium expects no mod adding AI goals late, so we add a goal that is initially disabled.
	@Inject(method = "registerGoals", at = @At("TAIL"))
	private void addTigerseyeGoal(CallbackInfo ci) {
		goalSelector.addGoal(3, new SubTileTigerseye.CreeperAvoidPlayerGoal((Creeper) (Object) this));
	}
}
