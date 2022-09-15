package vazkii.botania.client.model.armor;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.client.model.BotaniaModelLayers;
import vazkii.botania.common.item.equipment.armor.elementium.ElementiumArmorItem;
import vazkii.botania.common.item.equipment.armor.manasteel.ManasteelArmorItem;
import vazkii.botania.common.item.equipment.armor.manaweave.ManaweaveArmorItem;
import vazkii.botania.common.item.equipment.armor.terrasteel.TerrasteelArmorItem;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class ArmorModels {
	private static Map<EquipmentSlot, ArmorModel> manasteel = Collections.emptyMap();
	private static Map<EquipmentSlot, ArmorModel> manaweave = Collections.emptyMap();
	private static Map<EquipmentSlot, ArmorModel> elementium = Collections.emptyMap();
	private static Map<EquipmentSlot, ArmorModel> terrasteel = Collections.emptyMap();

	private static Map<EquipmentSlot, ArmorModel> make(EntityRendererProvider.Context ctx, ModelLayerLocation inner, ModelLayerLocation outer) {
		Map<EquipmentSlot, ArmorModel> ret = new EnumMap<>(EquipmentSlot.class);
		for (var slot : EquipmentSlot.values()) {
			var mesh = ctx.bakeLayer(slot == EquipmentSlot.LEGS ? inner : outer);
			ret.put(slot, new ArmorModel(mesh, slot));
		}
		return ret;
	}

	public static void init(EntityRendererProvider.Context ctx) {
		manasteel = make(ctx, BotaniaModelLayers.MANASTEEL_INNER_ARMOR, BotaniaModelLayers.MANASTEEL_OUTER_ARMOR);
		manaweave = make(ctx, BotaniaModelLayers.MANAWEAVE_INNER_ARMOR, BotaniaModelLayers.MANAWEAVE_OUTER_ARMOR);
		elementium = make(ctx, BotaniaModelLayers.ELEMENTIUM_INNER_ARMOR, BotaniaModelLayers.ELEMENTIUM_OUTER_ARMOR);
		terrasteel = make(ctx, BotaniaModelLayers.TERRASTEEL_INNER_ARMOR, BotaniaModelLayers.TERRASTEEL_OUTER_ARMOR);
	}

	@Nullable
	public static ArmorModel get(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof ManaweaveArmorItem armor) {
			return manaweave.get(armor.getSlot());
		} else if (item instanceof ElementiumArmorItem armor) {
			return elementium.get(armor.getSlot());
		} else if (item instanceof TerrasteelArmorItem armor) {
			return terrasteel.get(armor.getSlot());
		} else if (item instanceof ManasteelArmorItem armor) { // manasteel must be last because the other types extend from it
			return manasteel.get(armor.getSlot());
		}

		return null;
	}
}
