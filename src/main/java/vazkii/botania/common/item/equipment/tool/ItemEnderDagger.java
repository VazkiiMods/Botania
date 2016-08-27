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
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.item.IColorable;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.awt.*;

public class ItemEnderDagger extends ItemManasteelSword implements IColorable {

	public ItemEnderDagger() {
		super(BotaniaAPI.manasteelToolMaterial, LibItemNames.ENDER_DAGGER);
		setMaxDamage(69); // What you looking at?
		setNoRepair();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		if(par2 == 0)
			return 0xFFFFFF;

		return Color.HSBtoRGB(0.75F, 1F, 1.5F - (float) Math.min(1F, Math.sin(System.currentTimeMillis() / 100D) * 0.5 + 1.2F));
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, @Nonnull EntityLivingBase attacker) {
		if(!target.worldObj.isRemote
				&& target instanceof EntityEnderman
				&& attacker instanceof EntityPlayer
				&& ((EntityPlayer) attacker).getCooledAttackStrength(0) == 1) {
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
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -1.25, 0));
		}

		return multimap;
	}

}
