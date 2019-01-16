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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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

public abstract class BlockModDoubleFlower extends BlockDoublePlant implements ILexiconable, IModelRegister {
	private static final int COUNT = 8;

	private final boolean second;

	public BlockModDoubleFlower(boolean second) {
		this.second = second;
		setHardness(0.0F);
		setSoundType(SoundType.PLANT);
		String name = LibBlockNames.DOUBLE_FLOWER + (second ? 2 : 1);
		setDefaultState(pickDefaultState());
		setRegistryName(new ResourceLocation(LibMisc.MOD_ID, name));
		setTranslationKey(name);
		setHardness(0F);
		setTickRandomly(false);
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
	}

	@Nonnull
	@Override
	public abstract BlockStateContainer createBlockState();

	protected abstract IBlockState pickDefaultState();

	@Override
	public abstract int getMetaFromState(IBlockState state);

	@Nonnull
	@Override
	public abstract IBlockState getStateFromMeta(int meta);

	@Nonnull
	@Override
	public Item getItemDropped(IBlockState state, @Nonnull Random rand, int fortune) {
		return Items.AIR;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 0;
	}

	@Override
	public boolean canGrow(@Nonnull World world, @Nonnull BlockPos pos, IBlockState state, boolean fuckifiknow) {
		return false;
	}

	@Override
	public boolean canHarvestBlock(IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player) {
		return false;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return true;
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, @Nonnull EntityPlayer player) {
		if(state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) {
			if(world.getBlockState(pos.down()).getBlock() == this) {
				if (!player.capabilities.isCreativeMode) {
					// IBlockState iblockstate = worldIn.getBlockState(pos.down());
					// BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = (BlockDoublePlant.EnumPlantType) iblockstate.getValue(VARIANT);

					//if (blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.FERN && blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.GRASS) {
					// worldIn.destroyBlock(pos.down(), true);
					//} else if (!world.isRemote) {
					//	if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.SHEARS) {
					//		this.onHarvest(worldIn, pos, iblockstate, player);
					//		world.setBlockToAir(pos.down());
					//	} else {
					//		world.destroyBlock(pos.down(), true);
					//	}
					//} else {
					world.setBlockToAir(pos.down());
					//}
				} else {
					world.setBlockToAir(pos.down());
				}
			}
		} else if(player.capabilities.isCreativeMode && world.getBlockState(pos.up()).getBlock() == this)
			world.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 2);
		player.addStat(StatList.getBlockStats(this));
		//super.onBlockHarvested(p_149681_1_, p_149681_2_, p_149681_3_, p_149681_4_, p_149681_5_, p_149681_6_);
	}

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, @Nonnull BlockPos pos) {
		return true;
	}

	@Nonnull
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, @Nonnull BlockPos pos, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<>();
		IBlockState state = world.getBlockState(pos);
		IBlockState stateBelow = world.getBlockState(pos.down());

		if (stateBelow.getBlock() == this && stateBelow.getValue(HALF) == EnumBlockHalf.LOWER && state.getValue(HALF) == EnumBlockHalf.UPPER) {
			ret.add(new ItemStack(this, 1, getMetaFromState(world.getBlockState(pos.down()))));
		}

		if (state.getValue(HALF) == EnumBlockHalf.LOWER) {
			ret.add(new ItemStack(this, 1, getMetaFromState(state)));
		}

		return ret;
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, @Nonnull NonNullList<ItemStack> stacks) {
		for(int i = 0; i < COUNT; ++i)
			stacks.add(new ItemStack(this, 1, i));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		state = getActualState(state, world, pos);
		int hex = state.getValue(second ? BotaniaStateProps.DOUBLEFLOWER_VARIANT_2 : BotaniaStateProps.DOUBLEFLOWER_VARIANT_1).getColorValue();
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

	@Nonnull
	@Override
	public IBlockState getActualState(IBlockState state, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
		if (state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) {
			IBlockState iblockstate = worldIn.getBlockState(pos.down());

			if (iblockstate.getBlock() == this) {
				PropertyEnum<EnumDyeColor> prop = second ? BotaniaStateProps.DOUBLEFLOWER_VARIANT_2 : BotaniaStateProps.DOUBLEFLOWER_VARIANT_1;
				state = state.withProperty(prop, iblockstate.getValue(prop));
			}
		}

		return state.withProperty(VARIANT, EnumPlantType.SUNFLOWER).withProperty(FACING, EnumFacing.SOUTH);
	}

	@Nonnull
	@Override
	public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
		state = state.getBlock().getActualState(state, world, pos);
		PropertyEnum<EnumDyeColor> prop = second ? BotaniaStateProps.DOUBLEFLOWER_VARIANT_2 : BotaniaStateProps.DOUBLEFLOWER_VARIANT_1;
		return new ItemStack(Item.getItemFromBlock(state.getBlock()), 1, state.getValue(prop).ordinal() - (second ? 8 : 0));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(BlockDoublePlant.VARIANT, BlockDoublePlant.FACING).build());
	}

}
