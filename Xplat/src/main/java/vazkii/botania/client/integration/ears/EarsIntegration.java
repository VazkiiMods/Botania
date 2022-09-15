package vazkii.botania.client.integration.ears;

import com.unascribed.ears.api.EarsFeatureType;
import com.unascribed.ears.api.OverrideResult;
import com.unascribed.ears.api.registry.EarsInhibitorRegistry;
import com.unascribed.ears.api.registry.EarsStateOverriderRegistry;

import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.item.PhantomInkable;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.lib.LibMisc;

public class EarsIntegration {
	public static void register() {
		EarsStateOverriderRegistry.register(LibMisc.MOD_ID, (state, peer) -> {
			if (!(peer instanceof Player player)) {
				return OverrideResult.DEFAULT;
			}

			EquipmentSlot slot = null;
			switch (state) {
				case WEARING_HELMET -> slot = EquipmentSlot.HEAD;
				case WEARING_CHESTPLATE -> slot = EquipmentSlot.CHEST;
				case WEARING_LEGGINGS -> slot = EquipmentSlot.LEGS;
				case WEARING_BOOTS -> slot = EquipmentSlot.FEET;
			}

			if (slot == null) {
				return OverrideResult.DEFAULT;
			}

			ItemStack stack = player.getItemBySlot(slot);
			if (!(stack.getItem() instanceof PhantomInkable item)) {
				return OverrideResult.DEFAULT;
			}

			if (!item.hasPhantomInk(stack)) {
				return OverrideResult.DEFAULT;
			}

			return OverrideResult.FALSE;
		});

		EarsInhibitorRegistry.register(LibMisc.MOD_ID, ((type, peer) -> {
			if (type != EarsFeatureType.WINGS) {
				return false;
			}

			if (!(peer instanceof Player player)) {
				return false;
			}

			Container equipment = EquipmentHandler.getAllWorn(player);

			for (int slot = 0; slot < equipment.getContainerSize(); slot++) {
				ItemStack item = equipment.getItem(slot);
				if (item.getItem() instanceof ItemFlightTiara tiara &&
						tiara.hasRender(item, player) &&
						ItemFlightTiara.getVariant(item) > 0) {
					return true;
				}
			}

			return false;
		}));
	};
}
