/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Apr 14, 2014, 2:58:13 PM (GMT)]
 */
package vazkii.botania.common.item.tool.armor;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.lib.LibResources;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class ItemTerrasteeelArmor extends ItemManasteelArmor {

	public ItemTerrasteeelArmor(int type, String name) {
		super(type, name, BotaniaAPI.terrasteelArmorMaterial);
	}
	
	int getHealthBoost() {
		return 0;
	}
	
	@Override
	public Multimap getItemAttributeModifiers() {
		Multimap map = HashMultimap.create();
		map.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier(new UUID(171328 /** Random number **/, armorType), "Armor modifier" + armorType, getHealthBoost(), 0));
		return map;
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		return slot == 2 ? LibResources.MODEL_TERRASTEEL_1 : LibResources.MODEL_TERRASTEEL_0;
	}

}
