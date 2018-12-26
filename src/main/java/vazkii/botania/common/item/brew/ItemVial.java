/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 1, 2014, 5:45:50 PM (GMT)]
 */
package vazkii.botania.common.item.brew;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.IBrewContainer;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ItemVial extends ItemMod implements IBrewContainer {

	public ItemVial() {
		this(LibItemNames.VIAL);
	}

	public ItemVial(String name) {
		super(name);
		setHasSubtypes(true);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if(isInCreativeTab(tab)) {
			for(int i = 0; i < 2; i++)
				list.add(new ItemStack(this, 1, i));
		}
	}

	@Nonnull
	@Override
	public String getTranslationKey(ItemStack stack) {
		return super.getTranslationKey(stack) + stack.getItemDamage();
	}

	@Override
	public ItemStack getItemForBrew(Brew brew, ItemStack stack) {
		ItemStack brewStack = new ItemStack(stack.getItemDamage() == 1 ? ModItems.brewFlask : ModItems.brewVial);
		ItemBrewBase.setBrew(brewStack, brew);
		return brewStack;
	}

	@Override
	public int getManaCost(Brew brew, ItemStack stack) {
		return brew.getManaCost() * (stack.getItemDamage() + 1);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("botania:vial", "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, 1, new ModelResourceLocation("botania:flask", "inventory"));
	}

}
