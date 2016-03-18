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

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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

	@Override
	@SideOnly(Side.CLIENT)
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
		String name = GameData.getItemRegistry().getNameForObject(this).toString();

		if (useRemaining == 0) {
			return new ModelResourceLocation(name, "inventory");
		}

		int j = (int) ((getMaxItemUseDuration(stack) - useRemaining) * chargeVelocityMultiplier());

		if(j >= 18)
			return new ModelResourceLocation(name + "_pulling_5", "inventory");
		if(j >= 15)
			return new ModelResourceLocation(name + "_pulling_4", "inventory");
		if(j >= 12)
			return new ModelResourceLocation(name + "_pulling_3", "inventory");
		if(j > 9)
			return new ModelResourceLocation(name + "_pulling_2", "inventory");
		if(j > 6)
			return new ModelResourceLocation(name + "_pulling_1", "inventory");

		return new ModelResourceLocation(name, "inventory");
	}

}
