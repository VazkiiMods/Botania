/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 26, 2015, 6:08:29 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockEnchantedSoil extends BlockMod implements ILexiconable {

	public BlockEnchantedSoil(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public IItemProvider getItemDropped(IBlockState state, World world, BlockPos pos, int fortune) {
		return Blocks.DIRT.getItemDropped(state, world, pos, fortune);
	}

	@Override
	public boolean canSustainPlant(@Nonnull IBlockState state, @Nonnull IBlockReader world, BlockPos pos, @Nonnull EnumFacing direction, IPlantable plantable) {
		return plantable.getPlantType(world, pos.down()) == EnumPlantType.Plains;
	}

	@Override
	public boolean canSilkHarvest(@Nonnull IBlockState state, IWorldReader world, BlockPos pos, EntityPlayer player) {
		return false;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.overgrowthSeed;
	}

}
