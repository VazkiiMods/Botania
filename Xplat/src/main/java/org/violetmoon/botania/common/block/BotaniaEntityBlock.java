/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BotaniaEntityBlock extends BaseEntityBlock {

	public BotaniaEntityBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		// This isn't used anyway and will go away in later versions, not worth bothering to implement it correctly.
		return (MapCodec) CODEC;
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
}
