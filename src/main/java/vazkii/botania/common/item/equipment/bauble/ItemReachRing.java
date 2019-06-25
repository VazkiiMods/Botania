/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 22, 2014, 3:00:09 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class ItemReachRing extends ItemBauble {

	public ItemReachRing(Properties props) {
		super(props);
	}
	
	@Override
	public Multimap<String, AttributeModifier> getEquippedAttributeModifiers(ItemStack stack) {
		Multimap<String, AttributeModifier> attributes = HashMultimap.create();
		attributes.put(PlayerEntity.REACH_DISTANCE.getName(), new AttributeModifier(getBaubleUUID(stack), "Reach Ring", 3.5, AttributeModifier.Operation.ADDITION).setSaved(false));
		return attributes;
	}
}
