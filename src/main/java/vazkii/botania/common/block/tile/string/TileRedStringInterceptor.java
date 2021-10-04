/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.core.handler.ModSounds;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TileRedStringInterceptor extends TileRedString {
	private static final Set<TileRedStringInterceptor> interceptors = new HashSet<>();

	public TileRedStringInterceptor(BlockPos pos, BlockState state) {
		super(ModTiles.RED_STRING_INTERCEPTOR, pos, state);
	}

	public static void commonTick(Level level, BlockPos worldPosition, BlockState state, TileRedStringInterceptor self) {
		TileRedString.commonTick(level, worldPosition, state, self);
		if (!level.isClientSide) {
			interceptors.add(self);
		}
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		return level.getBlockEntity(pos) != null;
	}

	private boolean saneState() {
		return !isRemoved() && level.getBlockEntity(worldPosition) == this;
	}

	public static InteractionResult onInteract(Player player, Level world, BlockPos pos, InteractionHand hand) {
		if (world.isClientSide) {
			return InteractionResult.PASS;
		}

		List<TileRedStringInterceptor> remove = new ArrayList<>();
		boolean did = false;

		for (TileRedStringInterceptor inter : interceptors) {
			if (!inter.saneState()) {
				remove.add(inter);
				continue;
			}

			if (inter.level == world) {
				BlockPos coords = inter.getBinding();
				if (coords != null && coords.equals(pos)) {
					Block block = inter.getBlockState().getBlock();
					world.setBlockAndUpdate(inter.getBlockPos(), world.getBlockState(inter.getBlockPos()).setValue(BlockStateProperties.POWERED, true));
					world.getBlockTicks().scheduleTick(inter.getBlockPos(), block, 2);
					did = true;
				}
			}
		}

		interceptors.removeAll(remove);
		if (did) {
			player.swing(hand);
			world.playSound(null, pos, ModSounds.redStringInterceptorClick, SoundSource.BLOCKS, 0.3F, 0.6F);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

}
