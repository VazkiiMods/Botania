/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 10, 2014, 11:48:12 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.tool;

import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword;

import javax.annotation.Nonnull;

public class ItemEnderDagger extends ItemManasteelSword {

	public ItemEnderDagger(Properties props) {
		super(BotaniaAPI.MANASTEEL_ITEM_TIER, props);
	}

	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, @Nonnull LivingEntity attacker) {
		if(!target.world.isRemote
				&& target instanceof EndermanEntity
				&& attacker instanceof PlayerEntity) {
			target.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity) attacker), 20);
		}

		stack.damageItem(1, attacker, e -> e.sendBreakAnimation(Hand.MAIN_HAND));
		return true;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity player, int par4, boolean par5) {}

	@Override
	public boolean usesMana(ItemStack stack) {
		return false;
	}

	@Nonnull
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot);

		if (equipmentSlot == EquipmentSlotType.MAINHAND) {
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
					new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -1.25, AttributeModifier.Operation.ADDITION));
		}

		return multimap;
	}

}
