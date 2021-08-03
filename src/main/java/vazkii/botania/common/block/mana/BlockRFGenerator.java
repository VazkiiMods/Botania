/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.mana.TileRFGenerator;

import javax.annotation.Nonnull;

public class BlockRFGenerator extends BlockMod implements EntityBlock {

	public BlockRFGenerator(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockGetter world) {
		return new TileRFGenerator();
	}

}
