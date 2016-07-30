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
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.crafting.IInfusionStabiliser;
import vazkii.botania.api.item.IHornHarvestable;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.client.render.IModelRegister;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.IRegisterCallback;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

@Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.crafting.IInfusionStabiliser", striprefs = true)
public class BlockModMushroom extends BlockMushroom implements IInfusionStabiliser, IHornHarvestable, ILexiconable, IRegisterCallback, IModelRegister {

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.3, 0, 0.3, 0.8, 1, 0.8);

	public BlockModMushroom() {
		ModBlocks.ALL_BLOCKS.add(this);
		setRegistryName(new ResourceLocation(LibMisc.MOD_ID, LibBlockNames.MUSHROOM));
		setUnlocalizedName(LibBlockNames.MUSHROOM);
		setLightLevel(0.2F);
		setHardness(0F);
		setSoundType(SoundType.PLANT);
		setTickRandomly(false);
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.COLOR, EnumDyeColor.WHITE));
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BotaniaStateProps.COLOR);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.COLOR).getMetadata();
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta >= EnumDyeColor.values().length) {
			meta = 0;
		}
		return getDefaultState().withProperty(BotaniaStateProps.COLOR, EnumDyeColor.byMetadata(meta));
	}

	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}

	@Override
	public void updateTick(@Nonnull World world, @Nonnull BlockPos pos, IBlockState state, Random rand) {} // Prevent spreading

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		if(pos.getY() >= 0 && pos.getY() < 256) {
			IBlockState state = world.getBlockState(pos.down());
			Block block = state.getBlock();
			return block == Blocks.MYCELIUM || block == Blocks.DIRT && state.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.PODZOL || block.canSustainPlant(state, world, pos.down(), EnumFacing.UP, this);
		}

		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(@Nonnull Item item, CreativeTabs tab, List<ItemStack> stacks) {
		for(int i = 0; i < 16; i++)
			stacks.add(new ItemStack(item, 1, i));
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		int hex = state.getValue(BotaniaStateProps.COLOR).getMapColor().colorValue;
		int r = (hex & 0xFF0000) >> 16;
		int g = (hex & 0xFF00) >> 8;
		int b = (hex & 0xFF);

		if(rand.nextDouble() < ConfigHandler.flowerParticleFrequency * 0.25F)
			Botania.proxy.sparkleFX(world, pos.getX() + 0.3 + rand.nextFloat() * 0.5, pos.getY() + 0.5 + rand.nextFloat() * 0.5, pos.getZ() + 0.3 + rand.nextFloat() * 0.5, r / 255F, g / 255F, b / 255F, rand.nextFloat(), 5);
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
	public void register() {
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlockWithMetadataAndName(this), getRegistryName());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerCustomItemblock(this, EnumDyeColor.values().length, i -> "mushroom_" + EnumDyeColor.byMetadata(i).getName());
	}
}
