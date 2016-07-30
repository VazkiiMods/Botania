/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 21, 2014, 7:48:54 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.AltarVariant;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.TileAltar;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.item.rod.ItemWaterRod;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockAltar extends BlockMod implements ILexiconable {

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.125, 0.125, 0.125, 0.875, 20.0/16, 0.875);

	protected BlockAltar() {
		super(Material.ROCK, LibBlockNames.ALTAR);
		setHardness(3.5F);
		setSoundType(SoundType.STONE);
		setDefaultState(blockState.getBaseState()
				.withProperty(BotaniaStateProps.ALTAR_VARIANT, AltarVariant.DEFAULT)
		);
	}

	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BotaniaStateProps.ALTAR_VARIANT);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.ALTAR_VARIANT).ordinal();
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta < 0 || meta >= AltarVariant.values().length ) {
			meta = 0;
		}
		return getDefaultState().withProperty(BotaniaStateProps.ALTAR_VARIANT, AltarVariant.values()[meta]);
	}

	@Nonnull
	@Override
	public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TileAltar) {
			TileAltar altar = ((TileAltar) te);

			if (altar.isMossy) {
				state = state.withProperty(BotaniaStateProps.ALTAR_VARIANT, AltarVariant.MOSSY);
			}
		}
		return state;
	}

	@Nonnull
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public void registerItemForm() {
		GameRegistry.register(new ItemBlockWithMetadataAndName(this), getRegistryName());
	}

	@Override
	public void getSubBlocks(@Nonnull Item item, CreativeTabs tab, List<ItemStack> list) {
		for(int i = 0; i < 9; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity par5Entity) {
		if(par5Entity instanceof EntityItem) {
			TileAltar tile = (TileAltar) world.getTileEntity(pos);
			if(tile.collideEntityItem((EntityItem) par5Entity))
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
		}
	}

	@Override
	public int getLightValue(@Nonnull IBlockState state, IBlockAccess world, @Nonnull BlockPos pos) {
		if(world.getBlockState(pos).getBlock() != this)
			return world.getBlockState(pos).getLightValue(world, pos);
		TileAltar tile = (TileAltar) world.getTileEntity(pos);
		return (tile != null && tile.hasLava) ? 15 : 0;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack stack, EnumFacing par6, float par7, float par8, float par9) {
		TileAltar tile = (TileAltar) world.getTileEntity(pos);
		if(player.isSneaking()) {
			InventoryHelper.withdrawFromInventory(tile, player);
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
		} else if(tile.isEmpty() && tile.hasWater && stack == null)
			tile.trySetLastRecipe(player);
		else {
			if(stack != null && (isValidWaterContainer(stack) || stack.getItem() == ModItems.waterRod && ManaItemHandler.requestManaExact(stack, player, ItemWaterRod.COST, false))) {
				if(!tile.hasWater) {
					if(stack.getItem() == ModItems.waterRod)
						ManaItemHandler.requestManaExact(stack, player, ItemWaterRod.COST, true);
					else if(!player.capabilities.isCreativeMode)
						drain(FluidRegistry.WATER, stack);

					tile.setWater(true);
					world.updateComparatorOutputLevel(pos, this);
					world.checkLight(pos);
				}

				return true;
			} else if(stack != null && stack.getItem() == Items.LAVA_BUCKET) {
				if(!player.capabilities.isCreativeMode)
					drain(FluidRegistry.LAVA, stack);

				tile.setLava(true);
				tile.setWater(false);
				world.updateComparatorOutputLevel(pos, this);
				world.checkLight(pos);

				return true;
			} else if(stack != null && stack.getItem() == Items.BUCKET && (tile.hasWater || tile.hasLava) && !Botania.gardenOfGlassLoaded) {
				ItemStack bucket = tile.hasLava ? new ItemStack(Items.LAVA_BUCKET) : new ItemStack(Items.WATER_BUCKET);
				if(stack.stackSize == 1)
					player.inventory.setInventorySlotContents(player.inventory.currentItem, bucket);
				else {
					if(!player.inventory.addItemStackToInventory(bucket))
						player.dropItem(bucket, false);
					stack.stackSize--;
				}

				if(tile.hasLava)
					tile.setLava(false);
				else tile.setWater(false);
				world.updateComparatorOutputLevel(pos, this);
				world.checkLight(pos);

				return true;
			}
		}

		return false;
	}

	@Override
	public void fillWithRain(World world, BlockPos pos) {
		if(world.rand.nextInt(20) == 1) {
			TileEntity tile = world.getTileEntity(pos);
			if(tile instanceof TileAltar) {
				TileAltar altar = (TileAltar) tile;
				if(!altar.hasLava && !altar.hasWater)
					altar.setWater(true);
				world.updateComparatorOutputLevel(pos, this);
			}
		}
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	private boolean isValidWaterContainer(ItemStack stack) {
		if(stack == null || stack.stackSize != 1)
			return false;

		if(stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
			IFluidHandler handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
			FluidStack simulate = handler.drain(new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME), false);
			if(simulate != null && simulate.getFluid() == FluidRegistry.WATER && simulate.amount == Fluid.BUCKET_VOLUME)
				return true;
		}

		return false;
	}

	private void drain(Fluid fluid, ItemStack stack) {
		stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
				.drain(new FluidStack(fluid, Fluid.BUCKET_VOLUME), true);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileAltar();
	}

	@Override
	public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
		TileSimpleInventory inv = (TileSimpleInventory) world.getTileEntity(pos);

		InventoryHelper.dropInventory(inv, world, state, pos);

		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		TileAltar altar = (TileAltar) world.getTileEntity(pos);
		return altar.hasWater ? 15 : 0;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.apothecary;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerBlockToState(this, AltarVariant.values().length - 1);
	}

}
