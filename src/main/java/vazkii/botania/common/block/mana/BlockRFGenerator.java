/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.mana.TileRFGenerator;

import javax.annotation.Nonnull;

public class BlockRFGenerator extends BlockMod implements BlockEntityProvider {

	public BlockRFGenerator(Settings builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileRFGenerator();
	}

}
