/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import vazkii.botania.common.item.equipment.tool.elementium.ElementiumPickaxeItem;

import java.util.function.Consumer;

@Mixin(LootTable.class)
public class LootTableMixin {
	@ModifyVariable(method = "getRandomItemsRaw(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V", at = @At("HEAD"), argsOnly = true)
	private Consumer<ItemStack> filterDisposables(Consumer<ItemStack> inner, LootContext context) {
		return stack -> {
			Entity e = context.getParamOrNull(LootContextParams.THIS_ENTITY);
			ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
			if (e != null && tool != null) {
				if (ElementiumPickaxeItem.shouldFilterOut(e, tool, stack)) {
					return;
				}
			}

			inner.accept(stack);
		};
	}
}
