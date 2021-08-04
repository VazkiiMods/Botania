/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

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
	private static final ResourceLocation GOG_SEEDS = new ResourceLocation(LibMisc.GOG_MOD_ID, "extra_seeds");

	@Unique
	private boolean callingGogTable;

	@Inject(at = @At("RETURN"), method = "getRandomItemsRaw")
	private void addGogSeeds(LootContext context, Consumer<ItemStack> stacksOut, CallbackInfo ci) {
		if (Botania.gardenOfGlassLoaded && !callingGogTable) {
			callingGogTable = true;
			context.getLootTable(GOG_SEEDS).getRandomItems(context, stacksOut);
			callingGogTable = false;
		}
	}

	@ModifyArg(
		method = "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/LootTable;getRandomItemsRaw(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V"),
		index = 1
	)
	private Consumer<ItemStack> filterDisposables(LootContext context, Consumer<ItemStack> inner) {
		return stack -> {
			Entity e = context.getParamOrNull(LootContextParams.THIS_ENTITY);
			ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
			if (e != null && tool != null) {
				if (ItemElementiumPick.shouldFilterOut(e, tool, stack)) {
					return;
				}
			}

			inner.accept(stack);
		};
	}
}
