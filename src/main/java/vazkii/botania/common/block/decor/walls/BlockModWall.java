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
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.render.IModelRegister;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.lexicon.LexiconData;

import javax.annotation.Nonnull;

public abstract class BlockModWall extends BlockWall implements ILexiconable, IModelRegister {

	public BlockModWall(Block block, int meta) {
		super(block);
		// For backward compat don't kill me
		String name = block.getTranslationKey().replaceAll("tile.", "") + meta + "Wall";
		setRegistryName(name);
		setTranslationKey(name);
		setDefaultState(pickDefaultState());
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
	}

	@Nonnull
	@Override
	public IBlockState getActualState(IBlockState state, IBlockReader world, BlockPos pos) {
		return super.getActualState(state, world, pos).with(VARIANT, EnumType.NORMAL);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT, UP, NORTH, SOUTH, WEST, EAST);
	}

	protected IBlockState pickDefaultState() {
		return blockState.getBaseState()
				.with(UP, false)
				.with(NORTH, false)
				.with(SOUTH, false)
				.with(WEST, false)
				.with(EAST, false)
				.with(VARIANT, EnumType.NORMAL);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState();
	}

	@Override
	public boolean canPlaceTorchOnTop(IBlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos) {
		return true;
	}

	@Override
	public void getSubBlocks(CreativeTabs tabs, @Nonnull NonNullList<ItemStack> list) {
		list.add(new ItemStack(this));
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.decorativeBlocks;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(BlockWall.VARIANT).build());
	}

}
