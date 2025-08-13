package vazkii.botania.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.api.item.BlockChangedListenerBauble;
import vazkii.botania.common.handler.EquipmentHandler;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@Inject(method = "onChangedBlock", at = @At("HEAD"))
	void notifyBlockChangedListenerItems(ServerLevel level, BlockPos pos, CallbackInfo ci) {
		LivingEntity living = (LivingEntity) (Object) this;
		Container equipment = EquipmentHandler.getAllWorn(living);

		for (int slot = 0; slot < equipment.getContainerSize(); slot++) {
			ItemStack stack = equipment.getItem(slot);
			if (stack.getItem() instanceof BlockChangedListenerBauble bauble) {
				bauble.onChangedBlock(stack, living, level, pos);
			}
		}
	}
}
