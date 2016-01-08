/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 16, 2014, 5:50:31 PM (GMT)]
 */
package vazkii.botania.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.Botania;
import vazkii.botania.common.achievement.IPickupAchievement;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockModFlower extends BlockFlower implements ILexiconable, IPickupAchievement, IGrowable {

	public int originalLight;

	public static final String ALT_DIR = "alt";

	protected BlockModFlower() {
		this(LibBlockNames.FLOWER);
	}

	protected BlockModFlower(String name) {
		super();
		setUnlocalizedName(name);
		setHardness(0F);
		setStepSound(soundTypeGrass);
		setBlockBounds(0.3F, 0.0F, 0.3F, 0.8F, 1, 0.8F);
		setTickRandomly(false);
		setCreativeTab(registerInCreative() ? BotaniaCreativeTab.INSTANCE : null);
		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.COLOR, EnumDyeColor.WHITE).withProperty(type, EnumFlowerType.POPPY));
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, getTypeProperty(), BotaniaStateProps.COLOR);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.COLOR).getMetadata();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta >= EnumDyeColor.values().length) {
			meta = 0;
		}
		return getDefaultState().withProperty(BotaniaStateProps.COLOR, EnumDyeColor.byMetadata(meta)).withProperty(type, EnumFlowerType.POPPY);
	}

	@Override
	public EnumFlowerColor getBlockType() {
		return EnumFlowerColor.RED;
	}

	public boolean registerInCreative() {
		return true;
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		for(int i = 0; i < 16; i++)
			par3List.add(new ItemStack(par1, 1, i));
	}

	@Override
	public Block setUnlocalizedName(String par1Str) {
		GameRegistry.registerBlock(this, ItemBlockWithMetadataAndName.class, par1Str);
		return super.setUnlocalizedName(par1Str);
	}

	@Override
	public Block setLightLevel(float p_149715_1_) {
		originalLight = (int) (p_149715_1_ * 15);
		return super.setLightLevel(p_149715_1_);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public void randomDisplayTick(World par1World, BlockPos pos, IBlockState state, Random par5Random) {
		int hex = state.getValue(BotaniaStateProps.COLOR).getMapColor().colorValue;
		int r = (hex & 0xFF0000) >> 16;
		int g = (hex & 0xFF00) >> 8;
		int b = (hex & 0xFF);

		if(par5Random.nextDouble() < ConfigHandler.flowerParticleFrequency)
			Botania.proxy.sparkleFX(par1World, pos.getX() + 0.3 + par5Random.nextFloat() * 0.5, pos.getY() + 0.5 + par5Random.nextFloat() * 0.5, pos.getZ() + 0.3 + par5Random.nextFloat() * 0.5, r / 255F, g / 255F, b / 255F, par5Random.nextFloat(), 5);
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.flowers;
	}

	@Override
	public Achievement getAchievementOnPickup(ItemStack stack, EntityPlayer player, EntityItem item) {
		return ModAchievements.flowerPickup;
	}

	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean fuckifiknow) {
		return world.isAirBlock(pos.up());
	}

	@Override
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state) {
		return canGrow(world, pos, state, false);
	}

	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
		placeDoubleFlower(world, pos, state.getValue(BotaniaStateProps.COLOR), 1 | 2);
	}

	public static void placeDoubleFlower(World world, BlockPos pos, EnumDyeColor color, int flags) {
		Block flower = color.getMetadata() >= 8 ? ModBlocks.doubleFlower2 : ModBlocks.doubleFlower1;
		world.setBlockState(pos, flower.getDefaultState().withProperty(color.getMetadata() >= 8 ? BotaniaStateProps.DOUBLEFLOWER_VARIANT_2 : BotaniaStateProps.DOUBLEFLOWER_VARIANT_1, color).withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.LOWER), flags);
		world.setBlockState(pos.up(), flower.getDefaultState().withProperty(color.getMetadata() >= 8 ? BotaniaStateProps.DOUBLEFLOWER_VARIANT_2 : BotaniaStateProps.DOUBLEFLOWER_VARIANT_1, color).withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER), flags);
	}

}
