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

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.item.IPetalApothecary;
import vazkii.botania.api.recipe.IFlowerComponent;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ItemRune extends ItemMod implements IFlowerComponent {

	public ItemRune() {
		super(LibItemNames.RUNE);
		setHasSubtypes(true);
	}

	@Override
	public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> stacks) {
		if(isInCreativeTab(tab)) {
			for(int i = 0; i < 16; i++)
				stacks.add(new ItemStack(this, 1, i));
		}
	}

	@Nonnull
	@Override
	public String getTranslationKey(ItemStack stack) {
		return getUnlocalizedNameLazy(stack) + stack.getItemDamage();
	}

	String getUnlocalizedNameLazy(ItemStack stack) {
		return super.getTranslationKey(stack);
	}

	@Override
	public int getParticleColor(ItemStack stack) {
		return 0xA8A8A8;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		String[] runeNames = { "water", "fire", "earth", "air", "spring", "summer", "autumn", "winter", "mana", "lust", "gluttony", "greed", "sloth", "wrath", "envy", "pride" };
		ModelHandler.registerItemMetas(this, runeNames.length, i -> "rune_" + runeNames[i]);
	}

}
