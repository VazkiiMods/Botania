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
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileJadedAmaranthus extends SubTileFunctional {

	private static final int COST = 100;
	final int RANGE = 4;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(supertile.getWorld().isRemote || redstoneSignal > 0)
			return;

		if(ticksExisted % 30 == 0 && mana >= COST) {
			BlockPos pos = new BlockPos(
					supertile.getPos().getX() - RANGE + supertile.getWorld().rand.nextInt(RANGE * 2 + 1),
					supertile.getPos().getY() + RANGE,
					supertile.getPos().getZ() - RANGE + supertile.getWorld().rand.nextInt(RANGE * 2 + 1)
					);

			BlockPos up = pos.up();

			for(int i = 0; i < RANGE * 2; i++) {
				IBlockState stateAbove = supertile.getWorld().getBlockState(up);
				Block blockAbove = stateAbove.getBlock();
				if((supertile.getWorld().isAirBlock(up) || blockAbove.isReplaceable(supertile.getWorld(), up)) && stateAbove.getMaterial() != Material.WATER && ModBlocks.flower.canPlaceBlockAt(supertile.getWorld(), up)) {
					EnumDyeColor color = EnumDyeColor.byMetadata(supertile.getWorld().rand.nextInt(16));
					IBlockState state = ModBlocks.flower.getDefaultState().withProperty(BotaniaStateProps.COLOR, color);
					if(ConfigHandler.blockBreakParticles)
						supertile.getWorld().playEvent(2001, up, Block.getStateId(state));
					supertile.getWorld().setBlockState(up, state, 1 | 2);
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
