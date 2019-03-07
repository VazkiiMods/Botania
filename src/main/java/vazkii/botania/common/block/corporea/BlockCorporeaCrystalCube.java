/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 30, 2015, 3:56:19 PM (GMT)]
 */
package vazkii.botania.common.block.corporea;

import net.minecraft.block.Block;
import javax.annotation.Nonnull;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.block.tile.corporea.TileCorporeaBase;
import vazkii.botania.common.block.tile.corporea.TileCorporeaCrystalCube;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockCorporeaCrystalCube extends BlockCorporeaBase implements ILexiconable, IWandable {

	private static final AxisAlignedBB AABB = new AxisAlignedBB(3.0/16, 0, 3.0/16, 13.0/16, 1, 13.0/16);

	public BlockCorporeaCrystalCube(Block.Properties builder) {
		super(builder);
	}

	@Override
	public void onBlockClicked(IBlockState state, World world, BlockPos pos, EntityPlayer player) {
		if(!world.isRemote) {
			TileCorporeaCrystalCube cube = (TileCorporeaCrystalCube) world.getTileEntity(pos);
			cube.doRequest(player.isSneaking());
		}
	}

	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockReader world, BlockPos pos) {
		return AABB;
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing s, float xs, float ys, float zs) {
		ItemStack stack = player.getHeldItem(hand);
		if(!stack.isEmpty()) {
			if(stack.getItem() == ModItems.twigWand && player.isSneaking())
				return false;
			TileCorporeaCrystalCube cube = (TileCorporeaCrystalCube) world.getTileEntity(pos);
			if(cube.locked) {
				if(!world.isRemote)
					player.sendStatusMessage(new TextComponentTranslation("botaniamisc.crystalCubeLocked"), false);
			} else
				cube.setRequestTarget(stack);
			return true;
		}
		return false;
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
		if(player == null || player.isSneaking()) {
			TileCorporeaCrystalCube cube = (TileCorporeaCrystalCube) world.getTileEntity(pos);
			cube.locked = !cube.locked;
			if(!world.isRemote)
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(cube);
			return true;
		}
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Nonnull
	@Override
	public TileCorporeaBase createTileEntity(@Nonnull IBlockState state, @Nonnull IBlockReader world) {
		return new TileCorporeaCrystalCube();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.corporeaCrystalCube;
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		return ((TileCorporeaCrystalCube) world.getTileEntity(pos)).getComparatorValue();
	}

	@Nonnull
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader world, IBlockState state, BlockPos pos, EnumFacing side) {
		return BlockFaceShape.UNDEFINED;
	}
}
