/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 18, 2015, 8:42:16 PM (GMT)]
 */
package vazkii.botania.common.block.decor.walls;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockVariantWall extends BlockModWall {

	int metaStates;
	int metaShift;

	public BlockVariantWall(Block block, int metaStates, int metaShift) {
		super(block, 0);
		this.metaStates = metaStates;
		this.metaShift = metaShift;
	}

	public BlockVariantWall(Block block, int metaStates) {
		this(block, metaStates, 0);
	}

	@Override
	public void register(String name) {
		GameRegistry.registerBlock(this, ItemBlockWithMetadataAndName.class, name);
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
		for(int i = 0; i < metaStates; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return block.getIcon(side, meta + metaShift);
	}


}
