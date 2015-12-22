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

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Lots of copy paste from BlockDoublePlant because we can no longer extend it in 1.8.x
public class BlockModDoubleFlower extends BlockBush implements IGrowable, IShearable, ILexiconable {
	public static final PropertyEnum<BlockDoublePlant.EnumBlockHalf> HALF = BlockDoublePlant.HALF;
	private static final int COUNT = 8;

	final int offset;

	public BlockModDoubleFlower(boolean second) {
		super(Material.vine);
		this.setHardness(0.0F);
		this.setStepSound(soundTypeGrass);
		offset = second ? 8 : 0;
		setUnlocalizedName(LibBlockNames.DOUBLE_FLOWER + (second ? 2 : 1));
		setHardness(0F);
		setStepSound(soundTypeGrass);
		setTickRandomly(false);
		setCreativeTab(BotaniaCreativeTab.INSTANCE);

		if (second) {
			setDefaultState(blockState.getBaseState().withProperty(HALF, BlockDoublePlant.EnumBlockHalf.LOWER).withProperty(BotaniaStateProps.DOUBLEFLOWER_VARIANT_2, EnumDyeColor.SILVER));
		} else {
			setDefaultState(blockState.getBaseState().withProperty(HALF, BlockDoublePlant.EnumBlockHalf.LOWER).withProperty(BotaniaStateProps.DOUBLEFLOWER_VARIANT_1, EnumDyeColor.WHITE));
		}
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, offset == 8 ? BotaniaStateProps.DOUBLEFLOWER_VARIANT_2 : BotaniaStateProps.DOUBLEFLOWER_VARIANT_1, HALF);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = ((EnumDyeColor) state.getValue(offset == 8 ? BotaniaStateProps.DOUBLEFLOWER_VARIANT_2 : BotaniaStateProps.DOUBLEFLOWER_VARIANT_1)).getMetadata();
		meta -= offset;
		if (state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) {
			meta |= 8;
		}
		return meta;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		BlockDoublePlant.EnumBlockHalf half = (meta & 8) > 0 ? BlockDoublePlant.EnumBlockHalf.UPPER : BlockDoublePlant.EnumBlockHalf.LOWER;
		meta &= -9;
		meta += offset;
		EnumDyeColor color = EnumDyeColor.byMetadata(meta);
		return getDefaultState().withProperty(HALF, half).withProperty(offset == 8 ? BotaniaStateProps.DOUBLEFLOWER_VARIANT_2 : BotaniaStateProps.DOUBLEFLOWER_VARIANT_1, color);
	}

	@Override
	public Block setUnlocalizedName(String par1Str) {
		if(!par1Str.equals("doublePlant"))
			GameRegistry.registerBlock(this, ItemBlockWithMetadataAndName.class, par1Str);
		return super.setUnlocalizedName(par1Str);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state.withProperty(HALF, BlockDoublePlant.EnumBlockHalf.LOWER));
	}

	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean fuckifiknow) {
		return false;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return true;
	}

	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
		// todo 1.8.8 drop
	}

	@Override
	public void harvestBlock(World p_149636_1_, EntityPlayer p_149636_2_, BlockPos pos, IBlockState state, TileEntity te) {
		if(p_149636_1_.isRemote || p_149636_2_.getCurrentEquippedItem() == null || p_149636_2_.getCurrentEquippedItem().getItem() != Items.shears || state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.LOWER)
			harvestBlockCopy(p_149636_1_, p_149636_2_, pos, state);
	}

	// This is how I get around encapsulation
	public void harvestBlockCopy(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state) {
		player.triggerAchievement(StatList.mineBlockStatArray[getIdFromBlock(this)]);
		player.addExhaustion(0.025F);

		if (this.canSilkHarvest(worldIn, pos, worldIn.getBlockState(pos), player) && EnchantmentHelper.getSilkTouchModifier(player))
		{
			java.util.ArrayList<ItemStack> items = new java.util.ArrayList<ItemStack>();
			ItemStack itemstack = this.createStackedBlock(state);

			if (itemstack != null)
			{
				items.add(itemstack);
			}

			net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, worldIn.getBlockState(pos), 0, 1.0f, true, player);
			for (ItemStack stack : items)
			{
				spawnAsEntity(worldIn, pos, stack);
			}
		}
		else
		{
			harvesters.set(player);
			int i = EnchantmentHelper.getFortuneModifier(player);
			this.dropBlockAsItem(worldIn, pos, state, i);
			harvesters.set(null);
		}
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		if(state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) {
			if(world.getBlockState(pos.down()).getBlock() == this) {
				if (!player.capabilities.isCreativeMode) {
					// IBlockState iblockstate = worldIn.getBlockState(pos.down());
					// BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = (BlockDoublePlant.EnumPlantType) iblockstate.getValue(VARIANT);

					//if (blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.FERN && blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.GRASS) {
						// worldIn.destroyBlock(pos.down(), true);
					//} else if (!world.isRemote) {
					//	if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears) {
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
			world.setBlockState(pos.up(), Blocks.air.getDefaultState(), 2);

		//super.onBlockHarvested(p_149681_1_, p_149681_2_, p_149681_3_, p_149681_4_, p_149681_5_, p_149681_6_);
	}

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(this, 1, getMetaFromState(world.getBlockState(pos).withProperty(HALF, BlockDoublePlant.EnumBlockHalf.LOWER))));
		return ret;
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return new ArrayList();
	}

	@Override
	public int colorMultiplier(IBlockAccess blockAccess, BlockPos pos, int pass) {
		return 16777215;
	}

	@Override
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
		for(int i = 0; i < COUNT; ++i)
			p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
	}

	@Override
	public int getRenderType() {
		return LibRenderIDs.idDoubleFlower;
	}

	@Override
	public void randomDisplayTick(World par1World, BlockPos pos, IBlockState state, Random par5Random) {
		int hex = ((EnumDyeColor) state.getValue(BotaniaStateProps.COLOR)).getMapColor().colorValue;
		int r = (hex & 0xFF0000) >> 16;
		int g = (hex & 0xFF00) >> 8;
		int b = (hex & 0xFF);

		if(par5Random.nextDouble() < ConfigHandler.flowerParticleFrequency)
			Botania.proxy.sparkleFX(par1World, pos.getX() + 0.3 + par5Random.nextFloat() * 0.5, pos.getY() + 0.5 + par5Random.nextFloat() * 0.5, pos.getZ() + 0.3 + par5Random.nextFloat() * 0.5, r, g, b, par5Random.nextFloat(), 5);

	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.flowers;
	}



	/** BEGIN COPY BlockDoublePlant **/

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
	{
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return super.canPlaceBlockAt(worldIn, pos) && worldIn.isAirBlock(pos.up());
	}

	@Override
	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!this.canBlockStay(worldIn, pos, state))
		{
			boolean flag = state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER;
			BlockPos blockpos = flag ? pos : pos.up();
			BlockPos blockpos1 = flag ? pos.down() : pos;
			Block block = (Block)(flag ? this : worldIn.getBlockState(blockpos).getBlock());
			Block block1 = (Block)(flag ? worldIn.getBlockState(blockpos1).getBlock() : this);

			if (!flag) this.dropBlockAsItem(worldIn, pos, state, 0); //Forge move above the setting to air.

			if (block == this)
			{
				worldIn.setBlockState(blockpos, Blocks.air.getDefaultState(), 2);
			}

			if (block1 == this)
			{
				worldIn.setBlockState(blockpos1, Blocks.air.getDefaultState(), 3);
			}
		}
	}

	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
	{
		if (state.getBlock() != this) return super.canBlockStay(worldIn, pos, state); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
		if (state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER)
		{
			return worldIn.getBlockState(pos.down()).getBlock() == this;
		}
		else
		{
			IBlockState iblockstate = worldIn.getBlockState(pos.up());
			return iblockstate.getBlock() == this && super.canBlockStay(worldIn, pos, iblockstate);
		}
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		worldIn.setBlockState(pos.up(), this.getDefaultState().withProperty(HALF, BlockDoublePlant.EnumBlockHalf.UPPER), 2);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		if (state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER)
		{
			IBlockState iblockstate = worldIn.getBlockState(pos.down());

			if (iblockstate.getBlock() == this)
			{
				state = state.withProperty(BotaniaStateProps.COLOR, iblockstate.getValue(BotaniaStateProps.COLOR));
			}
		}

		return state;
	}

	@Override
	public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		//Forge: Break both parts on the client to prevent the top part flickering as default type for a few frames.
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() ==  this && state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.LOWER && world.getBlockState(pos.up()).getBlock() == this)
			world.setBlockToAir(pos.up());
		return world.setBlockToAir(pos);
	}


}
