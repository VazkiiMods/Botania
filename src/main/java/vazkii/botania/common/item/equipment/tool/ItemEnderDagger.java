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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ItemEnderDagger extends ItemManasteelSword {

	public ItemEnderDagger() {
		super(BotaniaAPI.manasteelToolMaterial, LibItemNames.ENDER_DAGGER);
		setMaxDamage(69); // What you looking at?
		setNoRepair();
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, @Nonnull EntityLivingBase attacker) {
		if(!target.world.isRemote
				&& target instanceof EntityEnderman
				&& attacker instanceof EntityPlayer) {
			target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker), 20);
		}

		stack.damageItem(1, attacker);
		return true;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity player, int par4, boolean par5) {}

	@Override
	public boolean usesMana(ItemStack stack) {
		return false;
	}

	@Nonnull
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

		if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -1.25, 0));
		}

		return multimap;
	}

}
