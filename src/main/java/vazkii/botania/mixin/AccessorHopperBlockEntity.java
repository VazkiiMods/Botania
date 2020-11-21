package vazkii.botania.mixin;

import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(HopperBlockEntity.class)
public interface AccessorHopperBlockEntity {
	@Invoker
	static boolean callCanInsert(Inventory to, ItemStack stack, int slot, Direction direction) {
		throw new IllegalStateException("");
	}

	@Invoker
	static boolean callCanMergeItems(ItemStack a, ItemStack b) {
		throw new IllegalStateException("");
	}
}
