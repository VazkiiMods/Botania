package vazkii.botania.fabric.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface FabricAccessorAbstractFurnaceBlockEntity {
	@Invoker("canBurn")
	static boolean callCanBurn(@Nullable Recipe<?> recipe, NonNullList<ItemStack> items, int maxStackSize) {
		throw new IllegalStateException();
	}
}
