/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core;

import net.fabricmc.fabric.impl.item.group.ItemGroupExtensions;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public final class BotaniaCreativeTab extends CreativeModeTab {

	public static final BotaniaCreativeTab INSTANCE = new BotaniaCreativeTab();

	public BotaniaCreativeTab() {
		super(computeIndex(), LibMisc.MOD_ID);
		setBackgroundSuffix(LibResources.GUI_CREATIVE);
	}

	private static int computeIndex() {
		((ItemGroupExtensions) CreativeModeTab.TAB_BUILDING_BLOCKS).fabric_expandArray();
		return CreativeModeTab.TABS.length - 1;
	}

	@Nonnull
	@Override
	public ItemStack makeIcon() {
		return new ItemStack(ModItems.lexicon);
	}
}
