package vazkii.botania.client.integration.ears;

import com.unascribed.ears.api.OverrideResult;
import com.unascribed.ears.api.registry.EarsStateOverriderRegistry;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.item.IPhantomInkable;

public class EarsIntegration {
	public void register() {
		EarsStateOverriderRegistry.register("botania", (state, peer) -> {
			if (!(peer instanceof Player player))
				return OverrideResult.DEFAULT;

			EquipmentSlot slot = null;
			switch (state) {
				case WEARING_HELMET -> slot = EquipmentSlot.HEAD;
				case WEARING_CHESTPLATE -> slot = EquipmentSlot.CHEST;
				case WEARING_LEGGINGS -> slot = EquipmentSlot.LEGS;
				case WEARING_BOOTS -> slot = EquipmentSlot.FEET;
			}
			if (slot == null)
				return OverrideResult.DEFAULT;

			ItemStack stack = player.getItemBySlot(slot);
			if (!(stack.getItem() instanceof IPhantomInkable item))
				return OverrideResult.DEFAULT;

			if (!item.hasPhantomInk(stack))
				return OverrideResult.DEFAULT;
			return OverrideResult.FALSE;
		});
	};
}
