/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package vazkii.botania.common.item;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.RegistryHelper;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class BotaniaArmorMaterials {
	private static final List<RegistryHelper.HolderProxy<ArmorMaterial>> ALL = new ArrayList<>();

	public static final Holder<ArmorMaterial> MANASTEEL = create("manasteel",
			Map.of(
					ArmorItem.Type.BOOTS, 2,
					ArmorItem.Type.LEGGINGS, 5,
					ArmorItem.Type.CHESTPLATE, 6,
					ArmorItem.Type.HELMET, 2
			),
			18, BotaniaSounds.equipManasteel, () -> Ingredient.of(BotaniaItems.MANASTEEL_INGOT), 0);

	public static final Holder<ArmorMaterial> MANAWEAVE = create("manaweave",
			Map.of(
					ArmorItem.Type.BOOTS, 1,
					ArmorItem.Type.LEGGINGS, 2,
					ArmorItem.Type.CHESTPLATE, 3,
					ArmorItem.Type.HELMET, 1
			),
			18, BotaniaSounds.equipManaweave, () -> Ingredient.of(BotaniaItems.MANAWEAVE_CLOTH), 0);
	public static final Holder<ArmorMaterial> ELEMENTIUM = create("elementium",
			Map.of(
					ArmorItem.Type.BOOTS, 2,
					ArmorItem.Type.LEGGINGS, 5,
					ArmorItem.Type.CHESTPLATE, 6,
					ArmorItem.Type.HELMET, 2
			),
			18, BotaniaSounds.equipElementium, () -> Ingredient.of(BotaniaItems.ELEMENTIUM_INGOT), 0);
	public static final Holder<ArmorMaterial> TERRASTEEL = create("terrasteel",
			Map.of(
					ArmorItem.Type.BOOTS, 3,
					ArmorItem.Type.LEGGINGS, 6,
					ArmorItem.Type.CHESTPLATE, 8,
					ArmorItem.Type.HELMET, 3
			),
			26, BotaniaSounds.equipTerrasteel, () -> Ingredient.of(BotaniaItems.TERRASTEEL_INGOT), 3);

	private static Supplier<Holder<SoundEvent>> getSoundEventHolder(SoundEvent soundEvent) {
		return () -> BuiltInRegistries.SOUND_EVENT.getHolder(soundEvent.getLocation()).orElseThrow();
	}

	private static Holder<ArmorMaterial> create(
			String name,
			Map<ArmorItem.Type, Integer> defense,
			int enchantmentValue,
			Holder<SoundEvent> equipSound,
			Supplier<Ingredient> repairIngredient,
			float toughness) {
		List<ArmorMaterial.Layer> list = List.of(new ArmorMaterial.Layer(botaniaRL(name)));
		return create(name, defense, enchantmentValue, equipSound, toughness, repairIngredient, list);
	}

	private static Holder<ArmorMaterial> create(
			String name,
			Map<ArmorItem.Type, Integer> defense,
			int enchantmentValue,
			Holder<SoundEvent> equipSound,
			float toughness,
			Supplier<Ingredient> repairIngredient,
			List<ArmorMaterial.Layer> layers) {
		EnumMap<ArmorItem.Type, Integer> enummap = new EnumMap<>(ArmorItem.Type.class);

		for (ArmorItem.Type armoritem$type : ArmorItem.Type.values()) {
			enummap.put(armoritem$type, defense.get(armoritem$type));
		}

		ResourceLocation id = botaniaRL(name);
		RegistryHelper.HolderProxy<ArmorMaterial> proxy = RegistryHelper.lazyHolderProxy(Registries.ARMOR_MATERIAL, id,
				() -> new ArmorMaterial(enummap, enchantmentValue, equipSound, repairIngredient, layers, toughness, 0));
		ALL.add(proxy);
		return proxy;
	}

	public static void registerArmorMaterials(Registry<ArmorMaterial> registry) {
		ALL.forEach(proxy -> proxy.register(registry));
	}
}
