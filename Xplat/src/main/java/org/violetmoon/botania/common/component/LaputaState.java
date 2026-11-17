/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package vazkii.botania.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import io.netty.buffer.ByteBuf;

public record LaputaState(BlockState blockState, Optional<CustomData> blockEntityData, @Nullable BlockPos nextPos, int yStart, int i, int j, int k) {

	public static final Codec<LaputaState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			BlockState.CODEC.fieldOf("block_state").forGetter(LaputaState::blockState),
			CustomData.CODEC.optionalFieldOf("block_entity_data").forGetter(LaputaState::blockEntityData),
			BlockPos.CODEC.fieldOf("next_pos").forGetter(LaputaState::nextPos),
			Codec.INT.fieldOf("y_start").forGetter(LaputaState::yStart),
			Codec.INT.fieldOf("iteration_i").forGetter(LaputaState::i),
			Codec.INT.fieldOf("iteration_j").forGetter(LaputaState::j),
			Codec.INT.fieldOf("iteration_k").forGetter(LaputaState::k)
	).apply(instance, LaputaState::new));

	/**
	 * Only encodes the block state, as no other data is required by the client.
	 * TODO: is that a good idea to save bandwidth?
	 */
	public static final StreamCodec<ByteBuf, LaputaState> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY), LaputaState::blockState,
			LaputaState::fromNetwork
	);

	private static LaputaState fromNetwork(BlockState blockState) {
		return new LaputaState(blockState, Optional.empty(), null, 0, 0, 0, 0);
	}
}
