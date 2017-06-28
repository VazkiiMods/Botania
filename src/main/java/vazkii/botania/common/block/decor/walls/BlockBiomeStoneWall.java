/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 18, 2015, 8:31:47 PM (GMT)]
 */
package vazkii.botania.common.block.decor.walls;

import net.minecraft.block.BlockWall;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.BiomeStoneVariant;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.lexicon.LexiconData;

import javax.annotation.Nonnull;

public class BlockBiomeStoneWall extends BlockModWall {

	public BlockBiomeStoneWall() {
		super(ModFluffBlocks.biomeStoneA, 0);
		setHardness(1.5F);
		setResistance(10F);
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT, UP, NORTH, SOUTH, WEST, EAST, BotaniaStateProps.BIOMESTONEWALL_VARIANT);
	}

	@Override
	protected IBlockState pickDefaultState() {
		return super.pickDefaultState().withProperty(BotaniaStateProps.BIOMESTONEWALL_VARIANT, BiomeStoneVariant.FOREST_COBBLE);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.BIOMESTONEWALL_VARIANT).ordinal() - 8;
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta < 0 || meta >= BotaniaStateProps.BIOMESTONEWALL_VARIANT.getAllowedValues().size()) {
			meta = 0;
		}
		return getDefaultState().withProperty(BotaniaStateProps.BIOMESTONEWALL_VARIANT, BiomeStoneVariant.values()[meta + 8]);
	}

	@Override
	public void getSubBlocks(CreativeTabs tabs, @Nonnull NonNullList<ItemStack> list) {
		for(int i = 0; i < 8; i++)
			list.add(new ItemStack(this, 1, i));
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.marimorphosis;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomStateMapper(this,
				new StateMap.Builder().withName(BotaniaStateProps.BIOMESTONEWALL_VARIANT)
				.ignore(BlockWall.VARIANT).withSuffix("_wall").build());
		ModelHandler.registerCustomItemblock(this, 8, i -> BiomeStoneVariant.values()[i + 8].getName() + "_wall");
	}

}
