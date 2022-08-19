/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Oct 11, 2015, 5:05:05 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.ISubTileContainer;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileRafflowsia extends SubTileGenerating {

	private static final String TAG_LAST_FLOWER = "lastFlower";
	private static final String TAG_LAST_FLOWER_TIMES = "lastFlowerTimes";

	String lastFlower;
	int lastFlowerTimes;

	private static final int RANGE = 5;

	@Override
	public void onUpdate() {
		super.onUpdate();

		int mana = 2100;

		if(getMaxMana() - this.mana >= mana && !supertile.getWorldObj().isRemote && ticksExisted % 40 == 0) {
			for(int i = 0; i < RANGE * 2 + 1; i++)
				for(int j = 0; j < RANGE * 2 + 1; j++)
					for(int k = 0; k < RANGE * 2 + 1; k++) {
						int x = supertile.xCoord + i - RANGE;
						int y = supertile.yCoord + j - RANGE;
						int z = supertile.zCoord + k - RANGE;
						Block block = supertile.getWorldObj().getBlock(x, y, z);
						TileEntity tile = supertile.getWorldObj().getTileEntity(x, y, z);
						if(tile instanceof ISubTileContainer) {
							SubTileEntity stile = ((ISubTileContainer) tile).getSubTile();
							String name = stile.getUnlocalizedName();

							if(stile instanceof SubTileGenerating && ((SubTileGenerating) stile).isPassiveFlower()) {
								boolean last = name.equals(lastFlower);
								if(last)
									lastFlowerTimes++;
								else {
									lastFlower = name;
									lastFlowerTimes = 1;
								}

								float mod = 1F / lastFlowerTimes;

								int meta = supertile.getWorldObj().getBlockMetadata(x, y, z) + 1;
								supertile.getWorldObj().setBlockToAir(x, y, z);

								supertile.getWorldObj().playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
								this.mana += mana * mod;
								sync();
								return;
							}
						}
					}
		}
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);

		cmp.setString(TAG_LAST_FLOWER, lastFlower);
		cmp.setInteger(TAG_LAST_FLOWER_TIMES, lastFlowerTimes);
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);

		lastFlower = cmp.getString(TAG_LAST_FLOWER);
		lastFlowerTimes = cmp.getInteger(TAG_LAST_FLOWER_TIMES);
	}

	@Override
	public void populateDropStackNBTs(List<ItemStack> drops) {
		super.populateDropStackNBTs(drops);

		ItemStack stack = drops.get(0);
		ItemNBTHelper.setString(stack, TAG_LAST_FLOWER, lastFlower);
		ItemNBTHelper.setInt(stack, TAG_LAST_FLOWER_TIMES, lastFlowerTimes);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
		super.onBlockPlacedBy(world, x, y, z, entity, stack);

		lastFlower = ItemNBTHelper.getString(stack, TAG_LAST_FLOWER, "");
		lastFlowerTimes = ItemNBTHelper.getInt(stack, TAG_LAST_FLOWER_TIMES, 0);
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toChunkCoordinates(), RANGE);
	}

	@Override
	public int getColor() {
		return 0x502C76;
	}

	@Override
	public int getMaxMana() {
		return 9000;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.rafflowsia;
	}

}
