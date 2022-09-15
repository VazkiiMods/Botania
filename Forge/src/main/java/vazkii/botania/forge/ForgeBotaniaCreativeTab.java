package vazkii.botania.forge;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.client.lib.ResourcesLib;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

public class ForgeBotaniaCreativeTab extends CreativeModeTab {
	public static final ForgeBotaniaCreativeTab INSTANCE = new ForgeBotaniaCreativeTab();

	private ForgeBotaniaCreativeTab() {
		super(LibMisc.MOD_ID);
		hideTitle();
		setBackgroundSuffix(ResourcesLib.GUI_CREATIVE);
	}

	@Override
	public boolean hasSearchBar() {
		return true;
	}

	@NotNull
	@Override
	public ItemStack makeIcon() {
		return new ItemStack(ModItems.lexicon);
	}
}
