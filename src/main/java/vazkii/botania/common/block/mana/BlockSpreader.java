/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 25, 2014, 9:38:23 PM (GMT)]
 */
package vazkii.botania.common.block.mana;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.botania.api.ColorHelper;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.api.wand.IWireframeAABBProvider;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

public class BlockSpreader extends BlockMod implements IWandable, IWandHUD, ILexiconable, IWireframeAABBProvider {
	private static final VoxelShape RENDER_SHAPE = makeCuboidShape(1, 1, 1, 15, 15, 15);
	public enum Variant {
		MANA,
		REDSTONE,
		ELVEN,
		GAIA
	}

	public final Variant variant;
	public BlockSpreader(Variant v, Properties builder) {
		super(builder);
		this.variant = v;
	}

	@Nonnull
	@Override
	public VoxelShape getRenderShape(IBlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos) {
		return RENDER_SHAPE;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		EnumFacing orientation = EnumFacing.getFacingDirections(placer)[0].getOpposite();
		TileSpreader spreader = (TileSpreader) world.getTileEntity(pos);

		switch(orientation) {
		case DOWN:
			spreader.rotationY = -90F;
			break;
		case UP:
			spreader.rotationY = 90F;
			break;
		case NORTH:
			spreader.rotationX = 270F;
			break;
		case SOUTH:
			spreader.rotationX = 90F;
			break;
		case WEST:
			break;
		default:
			spreader.rotationX = 180F;
			break;
		}
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Nonnull
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing par6, float par7, float par8, float par9) {
		TileEntity tile = world.getTileEntity(pos);
		if(!(tile instanceof TileSpreader))
			return false;

		TileSpreader spreader = (TileSpreader) tile;
		ItemStack lens = spreader.getItemHandler().getStackInSlot(0);
		ItemStack heldItem = player.getHeldItem(hand);
		boolean isHeldItemLens = !heldItem.isEmpty() && heldItem.getItem() instanceof ILens;
		boolean wool = !heldItem.isEmpty() && ColorHelper.WOOL_MAP.values().contains(Block.getBlockFromItem(heldItem.getItem()));

		if(!heldItem.isEmpty())
			if(heldItem.getItem() == ModItems.twigWand)
				return false;

		if(lens.isEmpty() && isHeldItemLens) {
			if (!player.abilities.isCreativeMode)
				player.setHeldItem(hand, ItemStack.EMPTY);

			spreader.getItemHandler().setStackInSlot(0, heldItem.copy());
			spreader.markDirty();
		} else if(!lens.isEmpty() && !wool) {
			ItemHandlerHelper.giveItemToPlayer(player, lens);
			spreader.getItemHandler().setStackInSlot(0, ItemStack.EMPTY);
			spreader.markDirty();
		}

		if(wool && spreader.paddingColor == null) {
			Block block = Block.getBlockFromItem(heldItem.getItem());
			spreader.paddingColor = ColorHelper.WOOL_MAP.inverse().get(block);
			heldItem.shrink(1);
			if(heldItem.isEmpty())
				player.setHeldItem(hand, ItemStack.EMPTY);
		} else if(heldItem.isEmpty() && spreader.paddingColor != null && lens.isEmpty()) {
			ItemStack pad = new ItemStack(ColorHelper.WOOL_MAP.get(spreader.paddingColor));
			ItemHandlerHelper.giveItemToPlayer(player, pad);
			spreader.paddingColor = null;
			spreader.markDirty();
		}

		return true;
	}

	@Override
	public void onReplaced(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState newState, boolean isMoving) {
		TileEntity tile = world.getTileEntity(pos);
		if(!(tile instanceof TileSpreader))
			return;

		TileSpreader inv = (TileSpreader) tile;

		if(inv.paddingColor != null) {
			ItemStack padding = new ItemStack(ColorHelper.WOOL_MAP.get(inv.paddingColor));
			world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), padding));
		}

		InventoryHelper.dropInventory(inv, world, state, pos);

		super.onReplaced(state, world, pos, newState, isMoving);
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
		((TileSpreader) world.getTileEntity(pos)).onWanded(player, stack);
		return true;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull IBlockState state, @Nonnull IBlockReader world) {
		return new TileSpreader();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(Minecraft mc, World world, BlockPos pos) {
		((TileSpreader) world.getTileEntity(pos)).renderHUD(mc);
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return variant == Variant.MANA ? LexiconData.spreader : variant == Variant.REDSTONE ? LexiconData.redstoneSpreader : LexiconData.dreamwoodSpreader;
	}

	@Override
	public List<AxisAlignedBB> getWireframeAABB(World world, BlockPos pos) {
		return ImmutableList.of(new AxisAlignedBB(pos).shrink(1.0/16.0));
	}

	@Nonnull
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader world, IBlockState state, BlockPos pos, EnumFacing side) {
		return BlockFaceShape.UNDEFINED;
	}
}
