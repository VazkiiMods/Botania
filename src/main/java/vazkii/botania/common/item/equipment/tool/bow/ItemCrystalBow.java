/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 21, 2015, 6:33:40 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.tool.bow;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.lib.LibItemNames;

public class ItemCrystalBow extends ItemLivingwoodBow {

	private final int ARROW_COST = 200;

	public ItemCrystalBow() {
		super(LibItemNames.CRYSTAL_BOW);
	}

	@Override
	float chargeVelocityMultiplier() {
		return 2F;
	}

	@Override
	boolean postsEvent() {
		return false;
	}

	@Override
	boolean canFire(ItemStack p_77615_1_, World p_77615_2_, EntityPlayer p_77615_3_, int p_77615_4_) {
		int infinity = EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, p_77615_1_);
		return ManaItemHandler.requestManaExactForTool(p_77615_1_, p_77615_3_, ARROW_COST / (infinity + 1), false);
	}

	@Override
	void onFire(ItemStack p_77615_1_, World p_77615_2_, EntityPlayer p_77615_3_, int p_77615_4_, boolean infinity, EntityArrow arrow) {
		arrow.canBePickedUp = 2;
		ManaItemHandler.requestManaExactForTool(p_77615_1_, p_77615_3_, ARROW_COST / (infinity ? 2 : 1), false);
	}

}
