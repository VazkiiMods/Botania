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

import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.BiomeStoneVariant;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.List;

public class BlockBiomeStoneWall extends BlockModWall {

	public BlockBiomeStoneWall() {
		super(ModFluffBlocks.biomeStoneA, 0);
		setHardness(1.5F);
		setResistance(10F);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.BIOMESTONEWALL_VARIANT, BiomeStoneVariant.FOREST_COBBLE).withProperty(VARIANT, EnumType.NORMAL));
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, VARIANT, UP, NORTH, SOUTH, WEST, EAST, BotaniaStateProps.BIOMESTONEWALL_VARIANT);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.BIOMESTONEWALL_VARIANT).ordinal() - 8;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta < 0 || meta >= BotaniaStateProps.BIOMESTONEWALL_VARIANT.getAllowedValues().size()) {
			meta = 0;
		}
		return getDefaultState().withProperty(BotaniaStateProps.BIOMESTONEWALL_VARIANT, BiomeStoneVariant.values()[meta + 8]);
	}

	@Override
	public void register(String name) {
		GameRegistry.registerBlock(this, ItemBlockWithMetadataAndName.class, name);
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
		for(int i = 0; i < 8; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.marimorphosis;
	}

}
