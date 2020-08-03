/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public final class BotaniaCreativeTab extends ItemGroup {

	public static final BotaniaCreativeTab INSTANCE = new BotaniaCreativeTab();

	public BotaniaCreativeTab() {
		super(LibMisc.MOD_ID);
		setNoTitle();
		setBackgroundImageName(LibResources.GUI_CREATIVE);
	}

	@Nonnull
	@Override
	public ItemStack createIcon() {
		return new ItemStack(ModItems.lexicon);
	}

	@Override
	public boolean hasSearchBar() {
		return true;
	}
}
