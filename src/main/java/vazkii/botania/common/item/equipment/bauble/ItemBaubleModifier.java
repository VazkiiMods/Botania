/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 26, 2014, 10:28:39 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;

public abstract class ItemBaubleModifier extends ItemBauble {
	public ItemBaubleModifier(String name) {
		super(name);
	}

	@Override
	public void onEquippedOrLoadedIntoWorld(ItemStack stack, EntityLivingBase player) {
		if(!player.world.isRemote) {
			Multimap<String, AttributeModifier> attributes = HashMultimap.create();
			fillModifiers(attributes, stack);
			player.getAttributeMap().applyAttributeModifiers(attributes);
		}
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		if(!player.world.isRemote) {
			Multimap<String, AttributeModifier> attributes = HashMultimap.create();
			fillModifiers(attributes, stack);
			player.getAttributeMap().removeAttributeModifiers(attributes);
		}
	}

	abstract void fillModifiers(Multimap<String, AttributeModifier> attributes, ItemStack stack);

}
