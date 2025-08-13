package vazkii.botania.api.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Interface for trinkets that want to receive block change events, similar to certain enchantments on equipment.
 */
public interface BlockChangedListenerBauble {
	void onChangedBlock(ItemStack stack, LivingEntity entity, ServerLevel level, BlockPos pos);
}
