/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 29, 2015, 7:17:35 PM (GMT)]
 */
package vazkii.botania.common.block.decor.biomestone;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.BiomeStoneVariant;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;

public class BlockBiomeStoneA extends BlockBiomeStone {

	public BlockBiomeStoneA() {
		super(LibBlockNames.BIOME_STONE_A);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.BIOMESTONE_VARIANT, BiomeStoneVariant.FOREST));
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BotaniaStateProps.BIOMESTONE_VARIANT);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.BIOMESTONE_VARIANT).ordinal();
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta >= BiomeStoneVariant.values().length) {
			meta = 0;
		}
		return getDefaultState().withProperty(BotaniaStateProps.BIOMESTONE_VARIANT, BiomeStoneVariant.values()[meta]);
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		int cobbleMeta = getMetaFromState(state) | 0x8;
		drops.add(new ItemStack(this, 1, cobbleMeta));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerBlockToState(this, BiomeStoneVariant.values().length);
	}

}
