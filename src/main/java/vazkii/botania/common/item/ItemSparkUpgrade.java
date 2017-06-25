/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 21, 2014, 5:32:06 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ItemSparkUpgrade extends ItemMod {

	public static final int VARIANTS = 4;

	public ItemSparkUpgrade() {
		super(LibItemNames.SPARK_UPGRADE);
		setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> list) {
		for(int i = 0; i < VARIANTS; i++)
			list.add(new ItemStack(this, 1, i));
	}

	@Nonnull
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return getUnlocalizedNameLazy(par1ItemStack) + par1ItemStack.getItemDamage();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerItemAppendMeta(this, 4, LibItemNames.SPARK_UPGRADE);
	}

	String getUnlocalizedNameLazy(ItemStack par1ItemStack) {
		return super.getUnlocalizedName(par1ItemStack);
	}
}
