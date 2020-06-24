/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lib.LibMisc;

public class SubTileJadedAmaranthus extends TileEntityFunctionalFlower {
	private static final int COST = 100;
	final int RANGE = 4;

	public SubTileJadedAmaranthus() {
		super(ModSubtiles.JADED_AMARANTHUS);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().isRemote || redstoneSignal > 0) {
			return;
		}

		if (ticksExisted % 30 == 0 && getMana() >= COST) {
			BlockPos pos = new BlockPos(
					getEffectivePos().getX() - RANGE + getWorld().rand.nextInt(RANGE * 2 + 1),
					getEffectivePos().getY() + RANGE,
					getEffectivePos().getZ() - RANGE + getWorld().rand.nextInt(RANGE * 2 + 1)
			);

			BlockPos up = pos.up();

			for (int i = 0; i < RANGE * 2; i++) {
				DyeColor color = DyeColor.byId(getWorld().rand.nextInt(16));
				BlockState flower = ModBlocks.getFlower(color).getDefaultState();

				if (getWorld().isAirBlock(up) && flower.isValidPosition(getWorld(), up)) {
					if (ConfigHandler.COMMON.blockBreakParticles.get()) {
						getWorld().playEvent(2001, up, Block.getStateId(flower));
					}
					getWorld().setBlockState(up, flower);
					addMana(-COST);
					sync();

					break;
				}

				up = pos;
				pos = pos.down();
			}
		}
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public int getColor() {
		return 0x961283;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
	}

	@Override
	public int getMaxMana() {
		return COST;
	}

}
