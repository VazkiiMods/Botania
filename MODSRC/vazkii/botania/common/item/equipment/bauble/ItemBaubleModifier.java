/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Apr 26, 2014, 10:28:39 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public abstract class ItemBaubleModifier extends ItemBauble {

	Multimap<String, AttributeModifier> attributes = HashMultimap.create();

	public ItemBaubleModifier(String name) {
		super(name);
		fillModifiers(attributes);
	}
	
	@Override
	public void onEquippedOrLoadedIntoWorld(ItemStack stack, EntityLivingBase player) {
		player.getAttributeMap().applyAttributeModifiers(attributes);
	}
	
	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		player.getAttributeMap().removeAttributeModifiers(attributes);
	}
	
	abstract void fillModifiers(Multimap<String, AttributeModifier> attributes);

}
