package vazkii.botania.common.item.equipment;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public interface ICustomDamageItem {
	// [SoftImplement] the same method in IForgeItem
	<T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken);
}
