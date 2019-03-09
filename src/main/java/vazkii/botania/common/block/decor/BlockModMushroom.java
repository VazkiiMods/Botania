/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 23, 2015, 4:24:08 PM (GMT)]
 */
package vazkii.botania.common.block.decor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import thaumcraft.api.crafting.IInfusionStabiliser;
import vazkii.botania.api.item.IHornHarvestable;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.recipe.ICustomApothecaryColor;
import vazkii.botania.client.render.IModelRegister;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lexicon.LexiconData;

import javax.annotation.Nonnull;
import java.util.Random;

@Optional.Interface(modid = "thaumcraft", iface = "thaumcraft.api.crafting.IInfusionStabiliser", striprefs = true)
public class BlockModMushroom extends BlockMushroom implements IInfusionStabiliser, IHornHarvestable, ILexiconable, ICustomApothecaryColor {

	private static final VoxelShape SHAPE = makeCuboidShape(4.8, 0, 4.8, 12.8, 16, 12.8);
	private final EnumDyeColor color;

	public BlockModMushroom(EnumDyeColor color, Properties builder) {
		super(builder);
		this.color = color;
	}

	@Nonnull
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader world, BlockPos pos) {
		return SHAPE;
	}

	@Override
	public void tick(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, Random rand) {} // Prevent spreading

	// [VanillaCopy] super, without light level requirement
	@Override
	public boolean isValidPosition(IBlockState state, IWorldReaderBase worldIn, BlockPos pos) {
		BlockPos blockpos = pos.down();
		IBlockState iblockstate = worldIn.getBlockState(blockpos);
		Block block = iblockstate.getBlock();
		if (block != Blocks.MYCELIUM && block != Blocks.PODZOL) {
			return iblockstate.canSustainPlant(worldIn, blockpos, net.minecraft.util.EnumFacing.UP, this);
		} else {
			return true;
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(IBlockState state, World world, BlockPos pos, Random rand) {
		int hex = color.colorValue;
		int r = (hex & 0xFF0000) >> 16;
		int g = (hex & 0xFF00) >> 8;
		int b = hex & 0xFF;

		if(rand.nextDouble() < ConfigHandler.flowerParticleFrequency * 0.25F)
			Botania.proxy.sparkleFX(pos.getX() + 0.3 + rand.nextFloat() * 0.5, pos.getY() + 0.5 + rand.nextFloat() * 0.5, pos.getZ() + 0.3 + rand.nextFloat() * 0.5, r / 255F, g / 255F, b / 255F, rand.nextFloat(), 5);
	}

	@Override
	public boolean canStabaliseInfusion(World world, BlockPos pos) {
		return ConfigHandler.enableThaumcraftStablizers;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.mushrooms;
	}

	@Override
	public boolean canHornHarvest(World world, BlockPos pos, ItemStack stack, EnumHornType hornType) {
		return false;
	}

	@Override
	public boolean hasSpecialHornHarvest(World world, BlockPos pos, ItemStack stack, EnumHornType hornType) {
		return false;
	}

	@Override
	public void harvestByHorn(World world, BlockPos pos, ItemStack stack, EnumHornType hornType) {}

	@Override
	public int getParticleColor(ItemStack stack) {
		return color.colorValue;
	}
}
