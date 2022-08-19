/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 18, 2015, 8:15:36 PM (GMT)]
 */
package vazkii.botania.common.block.decor.walls;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.item.block.ItemBlockMod;
import vazkii.botania.common.lexicon.LexiconData;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockModWall extends BlockWall implements ILexiconable {

	Block block;
	int meta;

	public BlockModWall(Block block, int meta) {
		super(block);
		this.block = block;
		this.meta = meta;
		setBlockName(block.getUnlocalizedName().replaceAll("tile.", "") + meta + "Wall");
	}

	@Override
	public boolean canPlaceTorchOnTop(World world, int x, int y, int z) {
		return true;
	}

	@Override
	public Block setBlockName(String par1Str) {
		register(par1Str);
		return super.setBlockName(par1Str);
	}

	public void register(String name) {
		GameRegistry.registerBlock(this, ItemBlockMod.class, name);
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
		list.add(new ItemStack(item));
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return block.getIcon(side, this.meta);
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.decorativeBlocks;
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		// NO-OP
	}

}
