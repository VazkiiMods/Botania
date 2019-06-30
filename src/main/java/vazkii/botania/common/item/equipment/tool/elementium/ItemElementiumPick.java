package vazkii.botania.common.item.equipment.tool.elementium;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelPick;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;
import vazkii.botania.common.lib.LibMisc;

public class ItemElementiumPick extends ItemManasteelPick {

	public ItemElementiumPick(Properties props) {
		super(BotaniaAPI.ELEMENTIUM_ITEM_TIER, props, -2.8F);
		MinecraftForge.EVENT_BUS.addListener(this::onHarvestDrops);
	}

	private void onHarvestDrops(HarvestDropsEvent event) {
		if(event.getHarvester() != null) {
			ItemStack stack = event.getHarvester().getHeldItemMainhand();
			if(!stack.isEmpty() && (stack.getItem() == this || stack.getItem() == ModItems.terraPick && ItemTerraPick.isTipped(stack))) {
				event.getDrops().removeIf(s -> !s.isEmpty() && (isDisposable(s)
						|| isSemiDisposable(s) && !event.getHarvester().isSneaking()));
			}
		}
	}

	private static final Tag<Item> DISPOSABLE = new ItemTags.Wrapper(new ResourceLocation(LibMisc.MOD_ID, "disposable"));
	private static final Tag<Item> SEMI_DISPOSABLE = new ItemTags.Wrapper(new ResourceLocation(LibMisc.MOD_ID, "semi_disposable"));

	public static boolean isDisposable(Block block) {
		return DISPOSABLE.contains(block.asItem());
	}

	private static boolean isDisposable(ItemStack stack) {
		if(stack.isEmpty())
			return false;

		return DISPOSABLE.contains(stack.getItem());
	}

	private static boolean isSemiDisposable(ItemStack stack) {
		return SEMI_DISPOSABLE.contains(stack.getItem());
	}
}
