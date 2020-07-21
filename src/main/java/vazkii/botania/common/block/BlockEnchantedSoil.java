/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

import javax.annotation.Nonnull;

public class BlockEnchantedSoil extends BlockMod {

	public BlockEnchantedSoil(Settings builder) {
		super(builder);
	}

	@Override
	public boolean canSustainPlant(@Nonnull BlockState state, @Nonnull BlockView world, BlockPos pos, @Nonnull Direction direction, IPlantable plantable) {
		return plantable.getPlantType(world, pos.down()) == PlantType.PLAINS;
	}

}
