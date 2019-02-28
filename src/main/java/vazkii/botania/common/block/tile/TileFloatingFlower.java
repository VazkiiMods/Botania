/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 8, 2014, 10:17:28 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

public class TileFloatingFlower extends TileMod implements IFloatingFlower {

	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.MINI_ISLAND)
	public static TileEntityType<TileFloatingFlower> TYPE;
	private static final String TAG_ISLAND_TYPE = "islandType";
	public static ItemStack forcedStack = ItemStack.EMPTY;

	IslandType type = IslandType.GRASS;

	public TileFloatingFlower() {
		super(TYPE);
	}

	@Override
	public ItemStack getDisplayStack() {
		if(!forcedStack.isEmpty()) {
			ItemStack retStack = forcedStack;
			forcedStack = ItemStack.EMPTY;
			return retStack;
		}
		return new ItemStack(getBlockState().getBlock());
	}

	@Override
	public IslandType getIslandType() {
		return type;
	}

	@Override
	public void setIslandType(IslandType type) {
		this.type = type;
	}

	@Override
	public void writePacketNBT(NBTTagCompound cmp) {
		cmp.putString(TAG_ISLAND_TYPE, type.toString());
	}

	@Override
	public void readPacketNBT(NBTTagCompound cmp) {
		type = IslandType.ofType(cmp.getString(TAG_ISLAND_TYPE));
	}

}
