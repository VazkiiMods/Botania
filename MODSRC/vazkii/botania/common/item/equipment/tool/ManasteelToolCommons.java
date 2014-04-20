/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 *
 * File Created @ [Apr 13, 2014, 7:13:04 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.tool;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.mana.ManaItemHandler;

public final class ManasteelToolCommons {

    public static void damageItem(ItemStack stack, int dmg, EntityLivingBase entity, int manaPerDamage) {
        int manaToRequest = dmg * manaPerDamage;
        int manaRequested = entity instanceof EntityPlayer ? ManaItemHandler.requestMana(stack, (EntityPlayer) entity, manaToRequest, true) : 0;

        int finalDamage = dmg - manaRequested / manaPerDamage;
        if(finalDamage > 0) stack.damageItem(finalDamage, entity);
    }

}
