/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 19, 2014, 10:16:53 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileAgricarnation extends SubTileFunctional {

	private static final int RANGE = 5;
	private static final int RANGE_MINI = 2;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(ticksExisted % 6 == 0 && redstoneSignal == 0) {
			int range = getRange();
			int x = supertile.xCoord + supertile.getWorldObj().rand.nextInt(range * 2 + 1) - range;
			int z = supertile.zCoord + supertile.getWorldObj().rand.nextInt(range * 2 + 1) - range;

			for(int i = 4; i > -2; i--) {
				int y = supertile.yCoord + i;

				if(supertile.getWorldObj().isAirBlock(x, y, z))
					continue;

				if(isPlant(x, y, z) && mana > 5) {
					Block block = supertile.getWorldObj().getBlock(x, y, z);
					mana -= 5;
					supertile.getWorldObj().scheduleBlockUpdate(x, y, z, block, 1);
					if(ConfigHandler.blockBreakParticles)
						supertile.getWorldObj().playAuxSFX(2005, x, y, z, 6 + supertile.getWorldObj().rand.nextInt(4));
					supertile.getWorldObj().playSoundEffect(x, y, z, "botania:agricarnation", 0.01F, 0.5F + (float) Math.random() * 0.5F);

					break;
				}
			}
		}
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	boolean isPlant(int x, int y, int z) {
		Block block = supertile.getWorldObj().getBlock(x, y, z);
		if(block == Blocks.grass || block == Blocks.leaves || block == Blocks.leaves2 || block instanceof BlockBush && !(block instanceof BlockCrops) && !(block instanceof BlockSapling))
			return false;

		Material mat = block.getMaterial();
		return mat != null && (mat == Material.plants || mat == Material.cactus || mat == Material.grass || mat == Material.leaves || mat == Material.gourd) && block instanceof IGrowable && ((IGrowable) block).func_149851_a(supertile.getWorldObj(), x, y, z, supertile.getWorldObj().isRemote);
	}

	@Override
	public int getColor() {
		return 0x8EF828;
	}

	@Override
	public int getMaxMana() {
		return 200;
	}

	public int getRange() {
		return RANGE;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toChunkCoordinates(), getRange());
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.agricarnation;
	}

	public static class Mini extends SubTileAgricarnation {
		@Override public int getRange() { return RANGE_MINI; }
	}

}
