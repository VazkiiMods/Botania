/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;

public class ItemReachRing extends ItemBauble {

	public ItemReachRing(Settings props) {
		super(props);
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getEquippedAttributeModifiers(ItemStack stack) {
		Multimap<EntityAttribute, EntityAttributeModifier> attributes = HashMultimap.create();
		attributes.put(ReachEntityAttributes.REACH, new EntityAttributeModifier(getBaubleUUID(stack), "Reach Ring", 3.5, EntityAttributeModifier.Operation.ADDITION));
		return attributes;
	}
}
