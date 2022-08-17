/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public final class FabricBotaniaCreativeTab extends CreativeModeTab {
	public static final FabricBotaniaCreativeTab INSTANCE;
	static {
		// Use FabricItemGroupBuilder#build only for its side-effect of expanding the size of the global static array of CreativeModeTabs by one.
		CreativeModeTab sacrificial = FabricItemGroupBuilder.build(prefix("sacrificial_tab"), () -> new ItemStack(ModItems.thinkingHand));

		// Slot a new creative tab into its place. (CreativeModeTab does this in its constructor.)
		INSTANCE = new FabricBotaniaCreativeTab(sacrificial.getId(), LibMisc.MOD_ID);
	}

	public FabricBotaniaCreativeTab(int index, String langId) {
		super(index, langId);
		hideTitle();
		setBackgroundSuffix(LibResources.GUI_CREATIVE);
	}

	@NotNull
	@Override
	public ItemStack makeIcon() {
		return new ItemStack(ModItems.lexicon);
	}
}
