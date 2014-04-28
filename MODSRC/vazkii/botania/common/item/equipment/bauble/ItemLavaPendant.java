/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Apr 26, 2014, 11:23:27 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibObfuscation;
import baubles.api.BaubleType;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class ItemLavaPendant extends ItemBauble {

	public ItemLavaPendant() {
		super(LibItemNames.LAVA_PENDANT);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		setImmunity(player, true);
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		setImmunity(player, false);
	}

	private void setImmunity(Entity entity, boolean immune) {
		ReflectionHelper.setPrivateValue(Entity.class, entity, immune, LibObfuscation.IS_IMMUNE_TO_FIRE);
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.AMULET;
	}

}
