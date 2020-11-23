package vazkii.botania.mixin;

import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(HopperBlockEntity.class)
public interface AccessorHopperBlockEntity {
	@Invoker("canInsert")
	static boolean botania_canInsert(Inventory to, ItemStack stack, int slot, Direction direction) {
		throw new IllegalStateException("");
	}

	@Invoker("canMergeItems")
	static boolean botania_canMerge(ItemStack a, ItemStack b) {
		throw new IllegalStateException("");
	}
}
