/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import com.google.common.collect.Multimap;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;

import vazkii.botania.common.core.handler.PixieHandler;

public class ItemPixieRing extends ItemBauble {
	public ItemPixieRing(Settings props) {
		super(props);
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getEquippedAttributeModifiers(ItemStack stack) {
		Multimap<EntityAttribute, EntityAttributeModifier> ret = super.getEquippedAttributeModifiers(stack);
		ret.put(PixieHandler.PIXIE_SPAWN_CHANCE, new EntityAttributeModifier(getBaubleUUID(stack), "Ring modifier", 0.25, EntityAttributeModifier.Operation.ADDITION));
		return ret;
	}
}
