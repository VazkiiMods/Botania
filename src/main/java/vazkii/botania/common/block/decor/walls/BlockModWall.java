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
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.item.block.ItemBlockMod;
import vazkii.botania.common.lexicon.LexiconData;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class BlockModWall extends BlockWall implements ILexiconable {

	Block block;

	public BlockModWall(Block block, int meta) {
		super(block);
		this.block = block;
		setUnlocalizedName(block.getUnlocalizedName().replaceAll("tile.", "") + meta + "Wall");
		setDefaultState(blockState.getBaseState()
				.withProperty(UP, false)
				.withProperty(NORTH, false)
				.withProperty(SOUTH, false)
				.withProperty(WEST, false)
				.withProperty(EAST, false)
				.withProperty(VARIANT, EnumType.NORMAL)
		);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return super.getActualState(state, world, pos).withProperty(VARIANT, EnumType.NORMAL);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, VARIANT, UP, NORTH, SOUTH, WEST, EAST);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState();
	}

	@Override
	public boolean canPlaceTorchOnTop(IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public Block setUnlocalizedName(String par1Str) {
		register(par1Str);
		return super.setUnlocalizedName(par1Str);
	}

	public void register(String name) {
		GameRegistry.registerBlock(this, ItemBlockMod.class, name);
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tabs, List<ItemStack> list) {
		list.add(new ItemStack(item));
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.decorativeBlocks;
	}

}
