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

import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.block.ItemBlockMod;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;

public abstract class BlockModWall extends BlockWall implements ILexiconable {

	public BlockModWall(Block block, int meta) {
		super(block);
		// For backward compat don't kill me
		String name = block.getUnlocalizedName().replaceAll("tile.", "") + meta + "Wall";
		register(name);
		setUnlocalizedName(name);
		setDefaultState(blockState.getBaseState()
				.withProperty(UP, false)
				.withProperty(NORTH, false)
				.withProperty(SOUTH, false)
				.withProperty(WEST, false)
				.withProperty(EAST, false)
				.withProperty(VARIANT, EnumType.NORMAL)
		);
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
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
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT, UP, NORTH, SOUTH, WEST, EAST);
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
	public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
		return true;
	}

	public void register(String name) {
		GameRegistry.register(this, new ResourceLocation(LibMisc.MOD_ID, name));
		GameRegistry.register(new ItemBlockMod(this), getRegistryName());
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
