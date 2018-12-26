/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 11, 2014, 2:16:47 AM (GMT)]
 */
package vazkii.botania.common.item.material;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.recipe.IElvenItem;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ItemQuartz extends ItemMod implements IElvenItem {

	private static final int SUBTYPES = 7;

	public ItemQuartz() {
		super(LibItemNames.QUARTZ);
		setHasSubtypes(true);
	}

	@Override
	public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> list) {
		if(isInCreativeTab(tab)) {
			for(int i = ConfigHandler.darkQuartzEnabled ? 0 : 1; i < SUBTYPES; i++)
				list.add(new ItemStack(this, 1, i));
		}
	}

	@Nonnull
	@Override
	public String getTranslationKey(ItemStack stack) {
		return getUnlocalizedNameLazy(stack) + stack.getItemDamage();
	}

	private String getUnlocalizedNameLazy(ItemStack par1ItemStack) {
		return super.getTranslationKey(par1ItemStack);
	}

	@Override
	public boolean isElvenItem(ItemStack stack) {
		return stack.getItemDamage() == 5;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerItemAppendMeta(this, 7, LibItemNames.QUARTZ);
	}
}
