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

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileHydroangeas extends SubTileGenerating {

	private static final String TAG_BURN_TIME = "burnTime";
	private static final String TAG_COOLDOWN = "cooldown";

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

		boolean didSomething = false;

		if(burnTime == 0) {
			if(ticksExisted % 10L == 0)
				if(mana < getMaxMana() && !supertile.getWorldObj().isRemote) {
					Random random = new Random();
					int randomInt = random.nextInt(3);
					int randomInt2 = random.nextInt(3);
					int randomBoolean = random.nextBoolean() ? 1 : -1;
					int[] positions = new int[] {supertile.xCoord + randomBoolean, supertile.zCoord + random.nextInt(1) };
					if(randomInt == 0)
						positions[0] = supertile.xCoord;
					else if(randomInt == 1)
						positions[0] = supertile.xCoord + 1;
					else if(randomInt == 2)
						positions[0] = supertile.xCoord - 1;

					if(randomInt2 == 0)
						positions[1] = supertile.zCoord;
					else if(randomInt2 == 1)
						positions[1] = supertile.zCoord + 1;
					else if(randomInt2 == 2)
						positions[1] = supertile.zCoord - 1;

					if(supertile.getWorldObj().getBlock(positions[0], supertile.yCoord, positions[1]) == getBlockToSearchFor() && (getBlockToSearchBelow() == null || supertile.getWorldObj().getBlock(positions[0], supertile.yCoord - 1, positions[1]) == getBlockToSearchBelow()) && supertile.getWorldObj().getBlockMetadata(positions[0], supertile.yCoord, positions[1]) == 0) {
						supertile.getWorldObj().setBlockToAir(positions[0], supertile.yCoord, positions[1]);
						didSomething = true;
						burnTime += getBurnTime();
						playSound();
					}
				}
		} else {
			if(supertile.getWorldObj().rand.nextInt(8) == 0)
				doBurnParticles();
			burnTime--;
			if(burnTime == 0) {
				cooldown = getCooldown();
				didSomething = true;
			}
		}

		if(didSomething)
			sync();
	}

	@Override
	public boolean isPassiveFlower() {
		return true;
	}

	public void doBurnParticles() {
		Botania.proxy.wispFX(supertile.getWorldObj(), supertile.xCoord + 0.55 + Math.random() * 0.2 - 0.1, supertile.yCoord + 0.55 + Math.random() * 0.2 - 0.1, supertile.zCoord + 0.5, 0.05F, 0.05F, 0.7F, (float) Math.random() / 6, (float) -Math.random() / 60);
	}

	public Block getBlockToSearchFor() {
		return Blocks.water;
	}

	public Block getBlockToSearchBelow() {
		return null;
	}

	public void playSound() {
		supertile.getWorldObj().playSoundEffect(supertile.xCoord, supertile.yCoord, supertile.zCoord, "random.drink", 0.05F, 0.5F + (float) Math.random() * 0.5F);
	}

	public int getBurnTime() {
		return 10;
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
	public ArrayList<ItemStack> getDrops(ArrayList<ItemStack> list) {
		ArrayList<ItemStack> drops = super.getDrops(list);
		if(cooldown > 0)
			ItemNBTHelper.setInt(drops.get(0), TAG_COOLDOWN, getCooldown());
		return drops;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
		super.onBlockPlacedBy(world, x, y, z, entity, stack);
		cooldown = ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 0);
	}

	@Override
	public boolean canGeneratePassively() {
		return burnTime > 0;
	}

	@Override
	public int getDelayBetweenPassiveGeneration() {
		boolean rain = supertile.getWorldObj().getWorldChunkManager().getBiomeGenAt(supertile.xCoord, supertile.zCoord).getIntRainfall() > 0 && (supertile.getWorldObj().isRaining() || supertile.getWorldObj().isThundering());
		return rain ? 3 : 4;
	}

	public int getCooldown() {
		return 0;
	}

}
