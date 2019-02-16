/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 22, 2015, 7:46:55 PM (GMT)]
 */
package vazkii.botania.common.block;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallFlower;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IShearable;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.render.IModelRegister;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockModDoubleFlower extends BlockTallFlower implements ILexiconable, IShearable {
	private final EnumDyeColor color;

	public BlockModDoubleFlower(EnumDyeColor color, Properties builder) {
		super(builder);
		this.color = color;
	}

	@Nonnull
	@Override
	public Item getItemDropped(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, int fortune) {
		return Items.AIR;
	}

	@Override
	public boolean canGrow(@Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean fuckifiknow) {
		return false;
	}

	// todo 1.13 re-evaluate shearability of these
	@Override
	public boolean canHarvestBlock(IBlockReader world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player) {
		return false;
	}

	@Override
	public boolean isShearable(@Nonnull ItemStack item, IWorldReader world, BlockPos pos) {
		return true;
	}

	@Nonnull
	@Override
	public List<ItemStack> onSheared(@Nonnull ItemStack item, IWorld world, @Nonnull BlockPos pos, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<>();
		IBlockState state = world.getBlockState(pos);
		IBlockState stateBelow = world.getBlockState(pos.down());

		if (stateBelow.getBlock() == this && stateBelow.getValue(HALF) == EnumBlockHalf.LOWER && state.get(HALF) == EnumBlockHalf.UPPER) {
			ret.add(new ItemStack(this, 1, getMetaFromState(world.getBlockState(pos.down()))));
		}

		if (state.get(HALF) == EnumBlockHalf.LOWER) {
			ret.add(new ItemStack(this, 1, getMetaFromState(state)));
		}

		return ret;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(IBlockState state, World world, BlockPos pos, Random rand) {
		int hex = color.getColorValue();
		int r = (hex & 0xFF0000) >> 16;
		int g = (hex & 0xFF00) >> 8;
		int b = hex & 0xFF;

		if(rand.nextDouble() < ConfigHandler.flowerParticleFrequency)
			Botania.proxy.sparkleFX(pos.getX() + 0.3 + rand.nextFloat() * 0.5, pos.getY() + 0.5 + rand.nextFloat() * 0.5, pos.getZ() + 0.3 + rand.nextFloat() * 0.5, r / 255F, g / 255F, b / 255F, rand.nextFloat(), 5);

	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.flowers;
	}
}
