/**
 * This class was created by <Pokefenn>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [? (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.ISubTileContainer;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;

public class SubTileHydroangeas extends SubTilePassiveGenerating {

	private static final String TAG_BURN_TIME = "burnTime";
	private static final String TAG_COOLDOWN = "cooldown";

	private static final int[][] OFFSETS = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 }, { -1, 1 }, { -1, -1 }, { 1, 1 }, { 1, -1 } };

	int burnTime, cooldown;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(cooldown > 0) {
			cooldown--;
			for(int i = 0; i < 3; i++)
				Botania.proxy.wispFX(supertile.getWorldObj(), supertile.xCoord + 0.5 + Math.random() * 0.2 - 0.1, supertile.yCoord + 0.5 + Math.random() * 0.2 - 0.1, supertile.zCoord + 0.5 + Math.random() * 0.2 - 0.1, 0.1F, 0.1F, 0.1F, (float) Math.random() / 6, (float) -Math.random() / 30);
			return;
		}

		if(burnTime == 0) {
			if(mana < getMaxMana() && !supertile.getWorldObj().isRemote) {
				List<int[]> offsets = Arrays.asList(OFFSETS);
				Collections.shuffle(offsets);

				for(int[] offsetArray : offsets) {
					int[] positions = {
							supertile.xCoord + offsetArray[0],
							supertile.zCoord + offsetArray[1]
					};

					Material search = getMaterialToSearchFor();
					if(supertile.getWorldObj().getBlock(positions[0], supertile.yCoord, positions[1]).getMaterial() == search && (getBlockToSearchBelow() == null || supertile.getWorldObj().getBlock(positions[0], supertile.yCoord - 1, positions[1]) == getBlockToSearchBelow()) && supertile.getWorldObj().getBlockMetadata(positions[0], supertile.yCoord, positions[1]) == 0) {
						if(search != Material.water)
							supertile.getWorldObj().setBlockToAir(positions[0], supertile.yCoord, positions[1]);
						else {
							int waterAround = 0;
							for(ForgeDirection dir : LibMisc.CARDINAL_DIRECTIONS)
								if(supertile.getWorldObj().getBlock(positions[0] + dir.offsetX, supertile.yCoord, positions[1] + dir.offsetZ).getMaterial() == search)
									waterAround++;

							if(waterAround < 2)
								supertile.getWorldObj().setBlockToAir(positions[0], supertile.yCoord, positions[1]);
						}

						burnTime += getBurnTime();
						sync();
						playSound();
						break;
					}
				}
			}
		} else {
			if(supertile.getWorldObj().rand.nextInt(8) == 0)
				doBurnParticles();
			burnTime--;
			if(burnTime == 0) {
				cooldown = getCooldown();
				sync();
			}
		}
	}

	public int getSurroundingFlowers() {
		int flowers = 0;
		for(int[] offsetArray : OFFSETS) {
			TileEntity tile = supertile.getWorldObj().getTileEntity(supertile.xCoord + offsetArray[0], supertile.yCoord, supertile.zCoord + offsetArray[1]);
			if(tile != null && tile instanceof ISubTileContainer) {
				ISubTileContainer flower = (ISubTileContainer) tile;
				if(flower.getSubTile() != null && flower.getSubTile().getClass() == getClass()) {
					flowers++;

					Color color = new Color(getColor());
					float r = color.getRed() / 255F;
					float g = color.getGreen() / 255F;
					float b = color.getBlue() / 255F;

					float m = 0.045F;
					if(ticksExisted % 10 == 0)
						Botania.proxy.wispFX(supertile.getWorldObj(), supertile.xCoord + 0.5, supertile.yCoord + 0.05, supertile.zCoord + 0.5, r, g, b, 0.1F, offsetArray[0] * m, 0, offsetArray[1] * m);
				}
			}
		}

		return flowers;
	}

	public void doBurnParticles() {
		Botania.proxy.wispFX(supertile.getWorldObj(), supertile.xCoord + 0.55 + Math.random() * 0.2 - 0.1, supertile.yCoord + 0.55 + Math.random() * 0.2 - 0.1, supertile.zCoord + 0.5, 0.05F, 0.05F, 0.7F, (float) Math.random() / 6, (float) -Math.random() / 60);
	}

	public Material getMaterialToSearchFor() {
		return Material.water;
	}

	public Block getBlockToSearchBelow() {
		return null;
	}

	public void playSound() {
		supertile.getWorldObj().playSoundEffect(supertile.xCoord, supertile.yCoord, supertile.zCoord, "random.drink", 0.01F, 0.5F + (float) Math.random() * 0.5F);
	}

	public int getBurnTime() {
		return 40;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toChunkCoordinates(), 1);
	}

	@Override
	public int getMaxMana() {
		return 150;
	}

	@Override
	public int getColor() {
		return 0x532FE0;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.hydroangeas;
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);

		cmp.setInteger(TAG_BURN_TIME, burnTime);
		cmp.setInteger(TAG_COOLDOWN, cooldown);
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);

		burnTime = cmp.getInteger(TAG_BURN_TIME);
		cooldown = cmp.getInteger(TAG_COOLDOWN);
	}

	@Override
	public void populateDropStackNBTs(List<ItemStack> drops) {
		super.populateDropStackNBTs(drops);
		int cooldown = this.cooldown;
		if(burnTime > 0)
			cooldown = getCooldown();

		if(cooldown > 0)
			ItemNBTHelper.setInt(drops.get(0), TAG_COOLDOWN, getCooldown());
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
		super.onBlockPlacedBy(world, x, y, z, entity, stack);
		cooldown = ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 0);
	}

	@Override
	public boolean canGeneratePassively() {
		int adj = getSurroundingFlowers();
		return supertile.getWorldObj().rand.nextInt(adj + 1) == 0 && burnTime > 0;
	}

	@Override
	public int getDelayBetweenPassiveGeneration() {
		boolean rain = supertile.getWorldObj().getWorldChunkManager().getBiomeGenAt(supertile.xCoord, supertile.zCoord).getIntRainfall() > 0 && (supertile.getWorldObj().isRaining() || supertile.getWorldObj().isThundering());
		return rain ? 1 : 2;
	}

	public int getCooldown() {
		return 0;
	}

}
