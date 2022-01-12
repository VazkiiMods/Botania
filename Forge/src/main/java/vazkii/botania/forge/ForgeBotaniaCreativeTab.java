package vazkii.botania.forge;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class ForgeBotaniaCreativeTab extends CreativeModeTab {
	public static final ForgeBotaniaCreativeTab INSTANCE = new ForgeBotaniaCreativeTab();

	private ForgeBotaniaCreativeTab() {
		super(LibMisc.MOD_ID);
		hideTitle();
		setBackgroundSuffix(LibResources.GUI_CREATIVE);
	}

	@Nonnull
	@Override
	public ItemStack makeIcon() {
		return new ItemStack(ModItems.lexicon);
	}
}
