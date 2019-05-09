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

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.capability.FloatingFlowerImpl;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.BlockFloatingFlower;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileFloatingFlower extends TileMod {
	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.MINI_ISLAND)
	public static TileEntityType<TileFloatingFlower> TYPE;

	private static final String TAG_FLOATING_DATA = "floating";
	public static ItemStack forcedStack = ItemStack.EMPTY;
	private final IFloatingFlower floatingData = new FloatingFlowerImpl() {
		@Override
		public ItemStack getDisplayStack() {
			if(!forcedStack.isEmpty()) {
				ItemStack retStack = forcedStack;
				forcedStack = ItemStack.EMPTY;
				return retStack;
			} else {
				Block b = getBlockState().getBlock();
				if(b instanceof BlockFloatingFlower) {
					return new ItemStack(ModBlocks.getShinyFlower(((BlockFloatingFlower) b).color));
				} else {
					return ItemStack.EMPTY;
				}
			}
		}
	};
	private final LazyOptional<IFloatingFlower> floatingDataCap = LazyOptional.of(() -> floatingData);

	public TileFloatingFlower() {
		super(TYPE);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing side) {
		if(cap == BotaniaAPI.FLOATING_FLOWER_CAP) {
			return floatingDataCap.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		IFloatingFlower.IslandType oldType = floatingData.getIslandType();
		super.onDataPacket(net, packet);
		if(oldType != floatingData.getIslandType()) {
			world.markBlockRangeForRenderUpdate(pos, pos);
		}
	}

	@Override
	public void writePacketNBT(NBTTagCompound cmp) {
		cmp.put(TAG_FLOATING_DATA, BotaniaAPI.FLOATING_FLOWER_CAP.writeNBT(floatingData, null));
	}

	@Override
	public void readPacketNBT(NBTTagCompound cmp) {
		BotaniaAPI.FLOATING_FLOWER_CAP.readNBT(floatingData, null, cmp.getCompound(TAG_FLOATING_DATA));
	}

}
