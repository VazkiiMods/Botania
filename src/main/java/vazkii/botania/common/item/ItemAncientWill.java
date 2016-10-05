/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 30, 2015, 10:59:45 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.crafting.recipe.AncientWillRecipe;
import vazkii.botania.common.lib.LibItemNames;

public class ItemAncientWill extends ItemMod {

	private static final int SUBTYPES = 6;

	public ItemAncientWill() {
		super(LibItemNames.ANCIENT_WILL);
		setHasSubtypes(true);
		setMaxStackSize(1);

		GameRegistry.addRecipe(new AncientWillRecipe());
		RecipeSorter.register("botania:ancientWill", AncientWillRecipe.class, Category.SHAPELESS, "");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(@Nonnull Item item, CreativeTabs tab, List<ItemStack> list) {
		for(int i = 0; i < SUBTYPES; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean adv) {
		addStringToTooltip(I18n.format("botaniamisc.craftToAddWill"), list);
		addStringToTooltip(I18n.format("botania.armorset.will" + stack.getItemDamage() + ".shortDesc"), list);
	}

	public void addStringToTooltip(String s, List<String> tooltip) {
		tooltip.add(s.replaceAll("&", "\u00a7"));
	}

	@Nonnull
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return super.getUnlocalizedName(par1ItemStack) + par1ItemStack.getItemDamage();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerItemAppendMeta(this, 6, LibItemNames.ANCIENT_WILL);
	}

}
