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
import net.minecraft.block.state.IBlockState;
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
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.TileAltar;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.rod.ItemWaterRod;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Random;

public class BlockAltar extends BlockMod implements ILexiconable {

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.125, 0.125, 0.125, 0.875, 20.0/16, 0.875);

	public enum Variant {
		DEFAULT,
		FOREST,
		PLAINS,
		MOUNTAIN,
		FUNGAL,
		SWAMP,
		DESERT,
		TAIGA,
		MESA,
		MOSSY
	}

	public final Variant variant;

	protected BlockAltar(Variant v) {
		super(Material.ROCK, LibBlockNames.APOTHECARY_PREFIX + v.name().toLowerCase(Locale.ROOT));
		setHardness(3.5F);
		setSoundType(SoundType.STONE);
		this.variant = v;
	}

	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}

	@SideOnly(Side.CLIENT)
	@Nonnull
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Nonnull
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		if(variant == Variant.MOSSY) {
			return Item.getItemFromBlock(ModBlocks.defaultAltar);
		} else {
			return super.getItemDropped(state, rand, fortune);
		}
	}

	@Override
	public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
		if(!world.isRemote && entity instanceof EntityItem) {
			TileAltar tile = (TileAltar) world.getTileEntity(pos);
			if(tile.collideEntityItem((EntityItem) entity))
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
		}
	}

	@Override
	public int getLightValue(@Nonnull IBlockState state, IBlockAccess world, @Nonnull BlockPos pos) {
		if(world.getBlockState(pos).getBlock() != this)
			return world.getBlockState(pos).getLightValue(world, pos);
		TileAltar tile = (TileAltar) world.getTileEntity(pos);
		return tile != null && tile.hasLava ? 15 : 0;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing par6, float par7, float par8, float par9) {
		TileAltar tile = (TileAltar) world.getTileEntity(pos);
		ItemStack stack = player.getHeldItem(hand);
		if(player.isSneaking()) {
			InventoryHelper.withdrawFromInventory(tile, player);
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
			return true;
		} else if(tile.isEmpty() && tile.hasWater && stack.isEmpty()) {
			tile.trySetLastRecipe(player);
			return true;
		}
		else {
			if(!stack.isEmpty() && (isValidWaterContainer(stack) || stack.getItem() == ModItems.waterRod && ManaItemHandler.requestManaExact(stack, player, ItemWaterRod.COST, false))) {
				if(!tile.hasWater) {
					if(stack.getItem() == ModItems.waterRod)
						ManaItemHandler.requestManaExact(stack, player, ItemWaterRod.COST, true);
					else if(!player.capabilities.isCreativeMode)
						player.setHeldItem(hand, drain(FluidRegistry.WATER, stack));

					tile.setWater(true);
					world.updateComparatorOutputLevel(pos, this);
					world.checkLight(pos);
				}

				return true;
			} else if(!stack.isEmpty() && stack.getItem() == Items.LAVA_BUCKET) {
				if(!player.capabilities.isCreativeMode)
					player.setHeldItem(hand, drain(FluidRegistry.LAVA, stack));

				tile.setLava(true);
				tile.setWater(false);
				world.updateComparatorOutputLevel(pos, this);
				world.checkLight(pos);

				return true;
			} else if(!stack.isEmpty() && stack.getItem() == Items.BUCKET && (tile.hasWater || tile.hasLava) && !Botania.gardenOfGlassLoaded) {
				ItemStack bucket = tile.hasLava ? new ItemStack(Items.LAVA_BUCKET) : new ItemStack(Items.WATER_BUCKET);
				if(stack.getCount() == 1)
					player.setHeldItem(hand, bucket);
				else {
					ItemHandlerHelper.giveItemToPlayer(player, bucket);
					stack.shrink(1);
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

	private boolean isValidWaterContainer(ItemStack stack) {
		if(stack.isEmpty() || stack.getCount() != 1)
			return false;

		if(stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
			IFluidHandler handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			FluidStack simulate = handler.drain(new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME), false);
			if(simulate != null && simulate.getFluid() == FluidRegistry.WATER && simulate.amount == Fluid.BUCKET_VOLUME)
				return true;
		}

		return false;
	}

	private ItemStack drain(Fluid fluid, ItemStack stack) {
		IFluidHandlerItem handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		handler.drain(new FluidStack(fluid, Fluid.BUCKET_VOLUME), true);
		return handler.getContainer();
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
}
