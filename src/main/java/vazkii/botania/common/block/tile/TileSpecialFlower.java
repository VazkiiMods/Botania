/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 22, 2014, 7:21:51 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.ISubTileSlowableContainer;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.api.wand.IWandBindable;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.string.TileRedStringRelay;

import java.util.List;

public class TileSpecialFlower extends TileMod implements IWandBindable, ISubTileSlowableContainer, ITickable {

	private static final String TAG_SUBTILE_NAME = "subTileName";
	private static final String TAG_SUBTILE_CMP = "subTileCmp";

	public ResourceLocation subTileName = BotaniaAPI.DUMMY_SUBTILE_NAME;
	private SubTileEntity subTile;

	@Override
	public SubTileEntity getSubTile() {
		return subTile;
	}

	@Override
	public void setSubTile(ResourceLocation name) {
		subTileName = name;
		provideSubTile(subTileName);
	}

	public void setSubTile(SubTileEntity tile) {
		subTile = tile;
		subTile.setSupertile(this);
	}

	private void provideSubTile(ResourceLocation name) {
		subTileName = name;

		Class<? extends SubTileEntity> tileClass = BotaniaAPI.getSubTileMapping(name);
		try {
			SubTileEntity tile = tileClass.newInstance();
			setSubTile(tile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void tick() {
		if(subTile != null) {
			world.profiler.startSection(() -> subTileName.toString());
			TileEntity tileBelow = world.getTileEntity(pos.down());
			if(tileBelow instanceof TileRedStringRelay) {
				BlockPos coords = ((TileRedStringRelay) tileBelow).getBinding();
				if(coords != null) {
					BlockPos currPos = pos;
					setPos(coords);
					subTile.onUpdate();
					setPos(currPos);

					world.profiler.endSection();
					return;
				}
			}

			boolean special = isOnSpecialSoil();
			if(special) {
				subTile.overgrowth = true;
				if(subTile.isOvergrowthAffected()) {
					subTile.onUpdate();
					subTile.overgrowthBoost = true;
				}
			}
			subTile.onUpdate();
			subTile.overgrowth = false;
			subTile.overgrowthBoost = false;
			world.profiler.endSection();
		}
	}

	public boolean isOnSpecialSoil() {
		return world.getBlockState(pos.down()).getBlock() == ModBlocks.enchantedSoil;
	}

	@Override
	public void writePacketNBT(NBTTagCompound cmp) {
		super.writePacketNBT(cmp);

		cmp.putString(TAG_SUBTILE_NAME, subTileName.toString());
		NBTTagCompound subCmp = new NBTTagCompound();
		cmp.put(TAG_SUBTILE_CMP, subCmp);

		if(subTile != null)
			subTile.writeToPacketNBTInternal(subCmp);
	}

	@Override
	public void readPacketNBT(NBTTagCompound cmp) {
		super.readPacketNBT(cmp);

		subTileName = new ResourceLocation(cmp.getString(TAG_SUBTILE_NAME));
		NBTTagCompound subCmp = cmp.getCompound(TAG_SUBTILE_CMP);

		if(subTile == null || !BotaniaAPI.getSubTileStringMapping(subTile.getClass()).equals(subTileName))
			provideSubTile(subTileName);

		if(subTile != null)
			subTile.readFromPacketNBTInternal(subCmp);
	}

	public LexiconEntry getEntry() {
		return subTile == null ? null : subTile.getEntry();
	}

	public boolean onWanded(ItemStack wand, EntityPlayer player) {
		return subTile == null ? false : subTile.onWanded(player, wand);
	}

	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
		if (subTile != null)
			subTile.onBlockPlacedBy(world, pos, state, entity, stack);
	}

	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		return subTile == null ? false : subTile.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
	}

	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		if (subTile != null)
			subTile.onBlockAdded(world, pos, state);
	}

	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (subTile != null)
			subTile.onBlockHarvested(world, pos, state, player);
	}

	public List<ItemStack> getDrops(List<ItemStack> list) {
		if (subTile != null)
			subTile.getDrops(list);

		return list;
	}

	@Override
	public boolean receiveClientEvent(int id, int param) {
		if(subTile != null)
			return subTile.receiveClientEvent(id, param);
		return super.receiveClientEvent(id, param);
	}

	@OnlyIn(Dist.CLIENT)
	public void renderHUD(Minecraft mc) {
		if(subTile != null)
			subTile.renderHUD(mc);
	}

	@Override
	public BlockPos getBinding() {
		if(subTile == null)
			return null;
		return subTile.getBinding();
	}

	@Override
	public boolean canSelect(EntityPlayer player, ItemStack wand, BlockPos pos, EnumFacing side) {
		if(subTile == null)
			return false;
		return subTile.canSelect(player, wand, pos, side);
	}

	@Override
	public boolean bindTo(EntityPlayer player, ItemStack wand, BlockPos pos, EnumFacing side) {
		if(subTile == null)
			return false;
		return subTile.bindTo(player, wand, pos, side);
	}

	public int getLightValue() {
		if(subTile == null)
			return 0;
		return subTile.getLightValue();
	}

	public int getComparatorInputOverride() {
		if(subTile == null)
			return 0;
		return subTile.getComparatorInputOverride();
	}

	public int getPowerLevel(EnumFacing side) {
		if(subTile == null)
			return 0;
		return subTile.getPowerLevel(side);
	}

	@Override
	public int getSlowdownFactor() {
		Block below = world.getBlockState(getPos().down()).getBlock();
		if(below == Blocks.MYCELIUM)
			return SLOWDOWN_FACTOR_MYCEL;

		if(below == Blocks.PODZOL) {
			return SLOWDOWN_FACTOR_PODZOL;
		}

		return 0;
	}
}
