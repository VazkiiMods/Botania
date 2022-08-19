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

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.ISubTileSlowableContainer;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.api.wand.IWandBindable;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.string.TileRedStringRelay;

public class TileSpecialFlower extends TileMod implements IWandBindable, ISubTileSlowableContainer {

	private static final String TAG_SUBTILE_NAME = "subTileName";
	private static final String TAG_SUBTILE_CMP = "subTileCmp";

	public String subTileName = "";
	SubTileEntity subTile;

	@Override
	public SubTileEntity getSubTile() {
		return subTile;
	}

	@Override
	public void setSubTile(String name) {
		subTileName = name;
		provideSubTile(subTileName);
	}

	public void setSubTile(SubTileEntity tile) {
		subTile = tile;
		subTile.setSupertile(this);
	}

	private void provideSubTile(String name) {
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
	public void updateEntity() {
		if(subTile != null) {
			TileEntity tileBelow = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
			if(tileBelow instanceof TileRedStringRelay) {
				ChunkCoordinates coords = ((TileRedStringRelay) tileBelow).getBinding();
				if(coords != null) {
					int currX = xCoord;
					int currY = yCoord;
					int currZ = zCoord;
					xCoord = coords.posX;
					yCoord = coords.posY;
					zCoord = coords.posZ;
					subTile.onUpdate();
					xCoord = currX;
					yCoord = currY;
					zCoord = currZ;

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
		}
	}

	public boolean isOnSpecialSoil() {
		return worldObj.getBlock(xCoord, yCoord - 1, zCoord) == ModBlocks.enchantedSoil;
	}

	@Override
	public boolean canUpdate() {
		return subTile == null || subTile.canUpdate();
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		super.writeCustomNBT(cmp);

		cmp.setString(TAG_SUBTILE_NAME, subTileName);
		NBTTagCompound subCmp = new NBTTagCompound();
		cmp.setTag(TAG_SUBTILE_CMP, subCmp);

		if(subTile != null)
			subTile.writeToPacketNBTInternal(subCmp);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		super.readCustomNBT(cmp);

		subTileName = cmp.getString(TAG_SUBTILE_NAME);
		NBTTagCompound subCmp = cmp.getCompoundTag(TAG_SUBTILE_CMP);

		if(subTile == null || !BotaniaAPI.getSubTileStringMapping(subTile.getClass()).equals(subTileName))
			provideSubTile(subTileName);

		if(subTile != null)
			subTile.readFromPacketNBTInternal(subCmp);
	}

	public IIcon getIcon() {
		return subTile == null ? Blocks.red_flower.getIcon(0, 0) : subTile.getIcon();
	}

	public LexiconEntry getEntry() {
		return subTile == null ? null : subTile.getEntry();
	}

	public boolean onWanded(ItemStack wand, EntityPlayer player) {
		return subTile == null ? false : subTile.onWanded(player, wand);
	}

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
		if (subTile != null)
			subTile.onBlockPlacedBy(world, x, y, z, entity, stack);
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		return subTile == null ? false : subTile.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		if (subTile != null)
			subTile.onBlockAdded(world, x, y, z);
	}

	public void onBlockHarvested(World world, int x, int y, int z, int side, EntityPlayer player) {
		if (subTile != null)
			subTile.onBlockHarvested(world, x, y, z, side, player);
	}

	public ArrayList<ItemStack> getDrops(ArrayList<ItemStack> list) {
		if (subTile != null)
			subTile.getDrops(list);

		return list;
	}

	public void renderHUD(Minecraft mc, ScaledResolution res) {
		if(subTile != null)
			subTile.renderHUD(mc, res);
	}

	@Override
	public ChunkCoordinates getBinding() {
		if(subTile == null)
			return null;
		return subTile.getBinding();
	}

	@Override
	public boolean canSelect(EntityPlayer player, ItemStack wand, int x, int y, int z, int side) {
		if(subTile == null)
			return false;
		return subTile.canSelect(player, wand, x, y, z, side);
	}

	@Override
	public boolean bindTo(EntityPlayer player, ItemStack wand, int x, int y, int z, int side) {
		if(subTile == null)
			return false;
		return subTile.bindTo(player, wand, x, y, z, side);
	}

	public int getLightValue() {
		if(subTile == null)
			return -1;
		return subTile.getLightValue();
	}

	public int getComparatorInputOverride(int side) {
		if(subTile == null)
			return 0;
		return subTile.getComparatorInputOverride(side);
	}

	public int getPowerLevel(int side) {
		if(subTile == null)
			return 0;
		return subTile.getPowerLevel(side);
	}

	@Override
	public int getSlowdownFactor() {
		Block below = worldObj.getBlock(xCoord, yCoord - 1, zCoord);
		if(below == Blocks.mycelium)
			return SLOWDOWN_FACTOR_MYCEL;
		
		if(below == Blocks.dirt) {
			int meta = worldObj.getBlockMetadata(xCoord, yCoord - 1, zCoord);
			if(meta == 2)
				return SLOWDOWN_FACTOR_PODZOL;
		}
		
		return 0;
	}
}
