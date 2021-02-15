/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.Identifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.Botania;
import vazkii.botania.common.item.equipment.tool.elementium.ItemElementiumPick;
import vazkii.botania.common.lib.LibMisc;

import java.util.function.Consumer;

@Mixin(LootTable.class)
public class MixinLootTable {
	private static final Identifier GOG_SEEDS = new Identifier(LibMisc.GOG_MOD_ID, "extra_seeds");

	@Unique
	private boolean callingGogTable;

	@Inject(at = @At("RETURN"), method = "generateLoot(Lnet/minecraft/loot/context/LootContext;Ljava/util/function/Consumer;)V")
	private void addGogSeeds(LootContext context, Consumer<ItemStack> stacksOut, CallbackInfo ci) {
		if (Botania.gardenOfGlassLoaded && !callingGogTable) {
			callingGogTable = true;
			context.getSupplier(GOG_SEEDS).generateLoot(context, stacksOut);
			callingGogTable = false;
		}
	}

	@ModifyArg(
		method = "generateLoot(Lnet/minecraft/loot/context/LootContext;Ljava/util/function/Consumer;)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/loot/LootTable;generateUnprocessedLoot(Lnet/minecraft/loot/context/LootContext;Ljava/util/function/Consumer;)V")
	)
	private Consumer<ItemStack> filterDisposables(LootContext context, Consumer<ItemStack> inner) {
		return stack -> {
			Entity e = context.get(LootContextParameters.THIS_ENTITY);
			ItemStack tool = context.get(LootContextParameters.TOOL);
			if (e != null && tool != null) {
				if (ItemElementiumPick.shouldFilterOut(e, tool, stack)) {
					return;
				}
			}

			inner.accept(stack);
		};
	}
}
