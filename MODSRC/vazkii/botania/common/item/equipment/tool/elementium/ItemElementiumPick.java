package vazkii.botania.common.item.equipment.tool.elementium;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelPick;
import vazkii.botania.common.lib.LibItemNames;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ItemElementiumPick extends ItemManasteelPick {

	static final List<Block> validBlocks = new ArrayList() {{
		add(Blocks.dirt);
		add(Blocks.sand);
		add(Blocks.gravel);
		add(Blocks.cobblestone);
		add(Blocks.netherrack);
	}};

	public ItemElementiumPick() {
		super(BotaniaAPI.elementiumToolMaterial, LibItemNames.ELEMENTIUM_PICK);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onHarvestDrops(HarvestDropsEvent event) {
		if(event.harvester != null) {
			ItemStack stack = event.harvester.getCurrentEquippedItem();
			if(stack != null && stack.getItem() == this)
				for(int i = 0; i < event.drops.size(); i++) {
					ItemStack drop = event.drops.get(i);
					if(drop != null) {
						Block block = Block.getBlockFromItem(drop.getItem());
						if(block != null && validBlocks.contains(block))
							event.drops.remove(i);
					}
				}
		}
	}

}
