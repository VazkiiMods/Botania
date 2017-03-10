/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 7, 2014, 9:46:24 PM (GMT)]
 */
package vazkii.botania.common.item.material;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.item.IPetalApothecary;
import vazkii.botania.api.recipe.IFlowerComponent;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.achievement.IPickupAchievement;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;

public class ItemRune extends ItemMod implements IFlowerComponent, IPickupAchievement {

	public ItemRune() {
		super(LibItemNames.RUNE);
		setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(@Nonnull Item item, CreativeTabs tab, NonNullList<ItemStack> stacks) {
		for(int i = 0; i < 16; i++)
			stacks.add(new ItemStack(item, 1, i));
	}

	@Nonnull
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return getUnlocalizedNameLazy(par1ItemStack) + par1ItemStack.getItemDamage();
	}

	String getUnlocalizedNameLazy(ItemStack par1ItemStack) {
		return super.getUnlocalizedName(par1ItemStack);
	}

	@Override
	public boolean canFit(ItemStack stack, IPetalApothecary apothecary) {
		return true;
	}

	@Override
	public int getParticleColor(ItemStack stack) {
		return 0xA8A8A8;
	}

	@Override
	public Achievement getAchievementOnPickup(ItemStack stack, EntityPlayer player, EntityItem item) {
		return ModAchievements.runePickup;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		String[] runeNames = { "water", "fire", "earth", "air", "spring", "summer", "autumn", "winter", "mana", "lust", "gluttony", "greed", "sloth", "wrath", "envy", "pride" };
		ModelHandler.registerItemMetas(this, runeNames.length, i -> "rune_" + runeNames[i]);
	}

}
