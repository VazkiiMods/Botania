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
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;

public class SubTileAgricarnation extends TileEntityFunctionalFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":agricarnation")
	public static TileEntityType<SubTileAgricarnation> TYPE;

	private static final int RANGE = 5;
	private static final int RANGE_MINI = 2;

	public SubTileAgricarnation(TileEntityType<?> type) {
		super(type);
	}

	public SubTileAgricarnation() {
		this(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if(getWorld().isRemote)
			return;

		if(ticksExisted % 200 == 0)
			sync();

		if(ticksExisted % 6 == 0 && redstoneSignal == 0) {
			int range = getRange();
			int x = getPos().getX() + getWorld().rand.nextInt(range * 2 + 1) - range;
			int z = getPos().getZ() + getWorld().rand.nextInt(range * 2 + 1) - range;

			for(int i = 4; i > -2; i--) {
				int y = getPos().getY() + i;
				BlockPos pos = new BlockPos(x, y, z);
				if(getWorld().isAirBlock(pos))
					continue;

				if(isPlant(pos) && mana > 5) {
					Block block = getWorld().getBlockState(pos).getBlock();
					mana -= 5;
					getWorld().getPendingBlockTicks().scheduleTick(pos, block, 1);
					if(ConfigHandler.COMMON.blockBreakParticles.get())
						getWorld().playEvent(2005, pos, 6 + getWorld().rand.nextInt(4));
					getWorld().playSound(null, x, y, z, ModSounds.agricarnation, SoundCategory.BLOCKS, 0.01F, 0.5F + (float) Math.random() * 0.5F);

					break;
				}
			}
		}
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	private boolean isPlant(BlockPos pos) {
		IBlockState state = getWorld().getBlockState(pos);
		Block block = state.getBlock();
		if(block == Blocks.GRASS || BlockTags.LEAVES.contains(block) || block instanceof BlockBush && !(block instanceof BlockCrops) && !(block instanceof BlockSapling))
			return false;

		Material mat = state.getMaterial();
		return mat != null && (mat == Material.PLANTS || mat == Material.CACTUS || mat == Material.GRASS || mat == Material.LEAVES || mat == Material.GOURD) && block instanceof IGrowable && ((IGrowable) block).canGrow(getWorld(), pos, getWorld().getBlockState(pos), getWorld().isRemote);
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
		return new RadiusDescriptor.Square(toBlockPos(), getRange());
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.agricarnation;
	}

	public static class Mini extends SubTileAgricarnation {
		@ObjectHolder(LibMisc.MOD_ID + ":agricarnation_chibi")
		public static TileEntityType<SubTileAgricarnation> TYPE;

		public Mini() {
			super(TYPE);
		}

		@Override public int getRange() { return RANGE_MINI; }
	}

}
