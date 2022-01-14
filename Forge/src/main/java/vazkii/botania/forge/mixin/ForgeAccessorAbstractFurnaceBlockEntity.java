package vazkii.botania.forge.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import javax.annotation.Nullable;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface ForgeAccessorAbstractFurnaceBlockEntity {
	@Invoker("canBurn")
	boolean callCanBurn(@Nullable Recipe<?> recipe, NonNullList<ItemStack> items, int maxStackSize);
}
