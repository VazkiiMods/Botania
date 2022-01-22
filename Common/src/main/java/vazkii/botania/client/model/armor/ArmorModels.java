package vazkii.botania.client.model.armor;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.client.model.ModModelLayers;
import vazkii.botania.common.item.equipment.armor.elementium.ItemElementiumArmor;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;
import vazkii.botania.common.item.equipment.armor.manaweave.ItemManaweaveArmor;
import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelArmor;

import javax.annotation.Nullable;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class ArmorModels {
	private static Map<EquipmentSlot, ModelArmor> manasteel = Collections.emptyMap();
	private static Map<EquipmentSlot, ModelArmor> manaweave = Collections.emptyMap();
	private static Map<EquipmentSlot, ModelArmor> elementium = Collections.emptyMap();
	private static Map<EquipmentSlot, ModelArmor> terrasteel = Collections.emptyMap();

	private static Map<EquipmentSlot, ModelArmor> make(EntityRendererProvider.Context ctx, ModelLayerLocation inner, ModelLayerLocation outer) {
		Map<EquipmentSlot, ModelArmor> ret = new EnumMap<>(EquipmentSlot.class);
		for (var slot : EquipmentSlot.values()) {
			var mesh = ctx.bakeLayer(slot == EquipmentSlot.LEGS ? inner : outer);
			ret.put(slot, new ModelArmor(mesh, slot));
		}
		return ret;
	}

	public static void init(EntityRendererProvider.Context ctx) {
		manasteel = make(ctx, ModModelLayers.MANASTEEL_INNER_ARMOR, ModModelLayers.MANASTEEL_OUTER_ARMOR);
		manaweave = make(ctx, ModModelLayers.MANAWEAVE_INNER_ARMOR, ModModelLayers.MANAWEAVE_OUTER_ARMOR);
		elementium = make(ctx, ModModelLayers.ELEMENTIUM_INNER_ARMOR, ModModelLayers.ELEMENTIUM_OUTER_ARMOR);
		terrasteel = make(ctx, ModModelLayers.TERRASTEEL_INNER_ARMOR, ModModelLayers.TERRASTEEL_OUTER_ARMOR);
	}

	@Nullable
	public static ModelArmor get(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof ItemManaweaveArmor armor) {
			return manaweave.get(armor.getSlot());
		} else if (item instanceof ItemElementiumArmor armor) {
			return elementium.get(armor.getSlot());
		} else if (item instanceof ItemTerrasteelArmor armor) {
			return terrasteel.get(armor.getSlot());
		} else if (item instanceof ItemManasteelArmor armor) { // manasteel must be last because the other types extend from it
			return manasteel.get(armor.getSlot());
		}

		return null;
	}
}
