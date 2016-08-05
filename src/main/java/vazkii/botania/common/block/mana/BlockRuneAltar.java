/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 2, 2014, 2:10:14 PM (GMT)]
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.TileRuneAltar;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.core.helper.InventoryHelper;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockRuneAltar extends BlockMod implements IWandable, ILexiconable {

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 0.75, 1);

	public BlockRuneAltar() {
		super(Material.ROCK, LibBlockNames.RUNE_ALTAR);
		setHardness(2.0F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
		BotaniaAPI.blacklistBlockFromMagnet(this, Short.MAX_VALUE);
	}

	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack stack, EnumFacing side, float par7, float par8, float par9) {
		if(world.isRemote)
			return true;

		TileRuneAltar altar = (TileRuneAltar) world.getTileEntity(pos);

		if(player.isSneaking()) {
			if(altar.manaToGet == 0) {
				InventoryHelper.withdrawFromInventory(altar, player);
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(altar);
			}
		} else if(altar.isEmpty() && stack == null) {
			altar.trySetLastRecipe(player);
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(altar);
		} else if(stack != null) {
			boolean result = altar.addItem(player, stack, hand);
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(altar);
			return result;
		}

		return false;
	}

	@Override
	public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
		TileSimpleInventory inv = (TileSimpleInventory) world.getTileEntity(pos);

		InventoryHelper.dropInventory(inv, world, state, pos);

		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileRuneAltar();
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		TileRuneAltar altar = (TileRuneAltar) world.getTileEntity(pos);
		return altar.signal;
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
		((TileRuneAltar) world.getTileEntity(pos)).onWanded(player, stack);
		return true;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.runicAltar;
	}

}
