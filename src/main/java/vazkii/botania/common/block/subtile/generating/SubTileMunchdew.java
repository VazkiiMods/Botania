/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 15, 2014, 7:25:47 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileMunchdew extends SubTileGenerating {

	private static final String TAG_COOLDOWN = "cooldown";
	private static final String TAG_ATE_ONCE = "ateOnce";

	private static final int RANGE = 8;
	private static final int RANGE_Y = 16;

	boolean ateOnce = false;
	int ticksWithoutEating = -1;
	int cooldown = 0;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(cooldown > 0) {
			cooldown--;
			ticksWithoutEating = 0;
			return;
		}

		int manaPerLeaf = 160;
		eatLeaves : {
			if(getMaxMana() - mana >= manaPerLeaf && !supertile.getWorldObj().isRemote && ticksExisted % 4 == 0) {
				List<ChunkCoordinates> coords = new ArrayList();
				int x = supertile.xCoord;
				int y = supertile.yCoord;
				int z = supertile.zCoord;

				for(int i = -RANGE; i < RANGE + 1; i++)
					for(int j = 0; j < RANGE_Y; j++)
						for(int k = -RANGE; k < RANGE + 1; k++) {
							int xp = x + i;
							int yp = y + j;
							int zp = z + k;
							Block block = supertile.getWorldObj().getBlock(xp, yp, zp);
							if(block.getMaterial() == Material.leaves) {
								boolean exposed = false;
								for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
									if(supertile.getWorldObj().getBlock(xp + dir.offsetX, yp + dir.offsetY, zp + dir.offsetZ).isAir(supertile.getWorldObj(), xp + dir.offsetX, yp + dir.offsetY, zp + dir.offsetZ)) {
										exposed = true;
										break;
									}

								if(exposed)
									coords.add(new ChunkCoordinates(xp, yp, zp));
							}
						}

				if(coords.isEmpty())
					break eatLeaves;

				Collections.shuffle(coords);
				ChunkCoordinates breakCoords = coords.get(0);
				Block block = supertile.getWorldObj().getBlock(breakCoords.posX, breakCoords.posY, breakCoords.posZ);
				int meta = supertile.getWorldObj().getBlockMetadata(breakCoords.posX, breakCoords.posY, breakCoords.posZ);
				supertile.getWorldObj().setBlockToAir(breakCoords.posX, breakCoords.posY, breakCoords.posZ);
				ticksWithoutEating = 0;
				ateOnce = true;
				if(ConfigHandler.blockBreakParticles)
					supertile.getWorldObj().playAuxSFX(2001, breakCoords.posX, breakCoords.posY, breakCoords.posZ, Block.getIdFromBlock(block) + (meta << 12));
				mana += manaPerLeaf;
			}
		}

		if(ateOnce) {
			ticksWithoutEating++;
			if(ticksWithoutEating >= 5)
				cooldown = 1600;
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toChunkCoordinates(), RANGE);
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);

		cmp.setInteger(TAG_COOLDOWN, cooldown);
		cmp.setBoolean(TAG_ATE_ONCE, ateOnce);
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);

		cooldown = cmp.getInteger(TAG_COOLDOWN);
		ateOnce = cmp.getBoolean(TAG_ATE_ONCE);
	}

	@Override
	public ArrayList<ItemStack> getDrops(ArrayList<ItemStack> list) {
		ArrayList<ItemStack> drops = super.getDrops(list);
		if(cooldown > 0)
			ItemNBTHelper.setInt(drops.get(0), TAG_COOLDOWN, cooldown);
		return drops;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
		super.onBlockPlacedBy(world, x, y, z, entity, stack);
		cooldown = ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 0);
	}

	@Override
	public int getColor() {
		return 0x79C42F;
	}

	@Override
	public int getMaxMana() {
		return 10000;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.munchdew;
	}
}
