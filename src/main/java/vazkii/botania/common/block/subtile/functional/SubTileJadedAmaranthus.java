/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 3, 2014, 10:31:29 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;

public class SubTileJadedAmaranthus extends TileEntityFunctionalFlower {
	@ObjectHolder(LibMisc.MOD_ID + ":jaded_amaranthus")
	public static TileEntityType<SubTileJadedAmaranthus> TYPE;

	private static final int COST = 100;
	final int RANGE = 4;

	public SubTileJadedAmaranthus() {
		super(TYPE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if(getWorld().isRemote || redstoneSignal > 0)
			return;

		if(ticksExisted % 30 == 0 && mana >= COST) {
			BlockPos pos = new BlockPos(
					getPos().getX() - RANGE + getWorld().rand.nextInt(RANGE * 2 + 1),
					getPos().getY() + RANGE,
					getPos().getZ() - RANGE + getWorld().rand.nextInt(RANGE * 2 + 1)
					);

			BlockPos up = pos.up();

			for(int i = 0; i < RANGE * 2; i++) {
				BlockState stateAbove = getWorld().getBlockState(up);
				DyeColor color = DyeColor.byId(getWorld().rand.nextInt(16));
				BlockState flower = ModBlocks.getFlower(color).getDefaultState();

				if((getWorld().isAirBlock(up) || stateAbove.getMaterial() != Material.WATER && flower.isValidPosition(getWorld(), up))) {
					if(ConfigHandler.COMMON.blockBreakParticles.get())
						getWorld().playEvent(2001, up, Block.getStateId(flower));
					getWorld().setBlockState(up, flower);
					mana -= COST;
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
		return new RadiusDescriptor.Square(toBlockPos(), RANGE);
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.jadedAmaranthus;
	}

	@Override
	public int getMaxMana() {
		return COST;
	}

}
