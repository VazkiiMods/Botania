/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 21, 2014, 8:44:35 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

public class ItemManaCookie extends ItemFood {

	public ItemManaCookie() {
		super(0, 0.1F, false);
		setPotionEffect(new PotionEffect(MobEffects.saturation, 1, 0), 1F);
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
		GameRegistry.register(this, new ResourceLocation(LibMisc.MOD_ID, LibItemNames.MANA_COOKIE));
		setUnlocalizedName(LibItemNames.MANA_COOKIE);
	}

	@Override
	protected void onFoodEaten(ItemStack p_77849_1_, World p_77849_2_, EntityPlayer p_77849_3_) {
		super.onFoodEaten(p_77849_1_, p_77849_2_, p_77849_3_);
		p_77849_3_.addStat(ModAchievements.manaCookieEat, 1);
	}

	@Override
	public String getUnlocalizedNameInefficiently(ItemStack par1ItemStack) {
		return super.getUnlocalizedNameInefficiently(par1ItemStack).replaceAll("item.", "item." + LibResources.PREFIX_MOD);
	}

}
