/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 16, 2014, 6:24:12 PM (GMT)]
 */
package vazkii.botania.common.core.handler;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import vazkii.botania.client.lib.LibResources;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.registry.GameRegistry.Type;

public final class AliasHandler {

	public static void onMissingMappings(FMLMissingMappingsEvent event) {
		List<MissingMapping> mappings = event.get();
		for(MissingMapping mapping : mappings) {
			String name = mapping.name.substring(LibResources.PREFIX_MOD.length() * 2);

			if(mapping.type == Type.ITEM)
				remapItem(mapping, getItem(name));
			else remapBlock(mapping, getBlock(name));
		}
	}

	private static void remapItem(MissingMapping mapping, Item item) {
		if(item != null)
			mapping.remap(item);
	}

	private static void remapBlock(MissingMapping mapping, Block block) {
		if(block != null)
			mapping.remap(block);
	}

	private static Item getItem(String name) {
		for(Object o : Item.itemRegistry.getKeys()) {
			Item i = (Item) Item.itemRegistry.getObject(o);
			if(i.getUnlocalizedName().substring("item.".length()).equals(name))
				return i;
		}

		return null;
	}

	private static Block getBlock(String name) {
		for(Object o : Block.blockRegistry.getKeys()) {
			Block b = (Block) Block.blockRegistry.getObject(o);
			if(b.getUnlocalizedName().substring("tile.".length()).equals(name))
				return b;
		}

		return null;
	}
}
