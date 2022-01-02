/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.saveddata.SavedData;

import vazkii.botania.api.BotaniaCapabilities;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.lens.LensPiston;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.xplat.IXplatAbstractions;

import javax.annotation.Nonnull;

import java.util.*;

public class BlockPistonRelay extends BlockMod {

	public final Map<UUID, GlobalPos> activeBindingAttempts = new HashMap<>();

	public BlockPistonRelay(Properties builder) {
		super(builder);
		BotaniaCapabilities.WANDABLE.registerForBlocks(
				(world, pos, state, blockEntity, context) -> (player, stack, side) -> onUsedByWand(player, stack, world, pos),
				this
		);
	}

	@Override
	public PushReaction getPistonPushReaction(@Nonnull BlockState blockState) {
		return PushReaction.PUSH_ONLY;
	}

	@Override
	public void onRemove(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (!world.isClientSide) {
			var data = WorldData.get(world);

			if (isMoving && newState.is(Blocks.MOVING_PISTON)) {
				var direction = newState.getValue(MovingPistonBlock.FACING);

				var destPos = data.mapping.get(pos);
				if (destPos != null) {
					BlockPos newSrcPos = pos.relative(direction);

					{
						// Move source side of our binding along
						data.mapping.remove(pos);
						data.mapping.put(newSrcPos, destPos);
						data.setDirty();
					}

					if (newState.getValue(MovingPistonBlock.TYPE) == PistonType.DEFAULT) {
						// Move the actual bound blocks
						LensPiston.moveBlocks(world, destPos.relative(direction.getOpposite()), direction);
						// Move dest side of our binding
						data.mapping.put(newSrcPos, data.mapping.get(newSrcPos).relative(direction));
					}
				}
			} else {
				if (data.mapping.remove(pos) != null) {
					data.setDirty();
				}
			}
		}
	}

	private boolean onUsedByWand(Player player, ItemStack stack, Level world, BlockPos pos) {
		if (world.isClientSide) {
			return false;
		}

		if (player == null || player.isShiftKeyDown()) {
			world.destroyBlock(pos, true);
		} else {
			GlobalPos clicked = GlobalPos.of(world.dimension(), pos.immutable());
			if (ItemTwigWand.getBindMode(stack)) {
				activeBindingAttempts.put(player.getUUID(), clicked);
				world.playSound(null, pos, ModSounds.ding, SoundSource.BLOCKS, 0.5F, 1F);
			} else {
				var data = WorldData.get(world);
				if (IXplatAbstractions.INSTANCE.isDevEnvironment()) {
					Botania.LOGGER.info("PistonRelay pairs");
					for (var e : data.mapping.entrySet()) {
						Botania.LOGGER.info("{} -> {}", e.getKey(), e.getValue());
					}
				}
				BlockPos dest = data.mapping.get(pos);
				if (dest != null) {
					PacketBotaniaEffect.sendNearby(world, pos, PacketBotaniaEffect.EffectType.PARTICLE_BEAM,
							pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
							dest.getX(), dest.getY(), dest.getZ());
				}
			}
		}

		return true;
	}

	public static class WorldData extends SavedData {

		private static final String ID = "PistonRelayPairs";
		public final Map<BlockPos, BlockPos> mapping = new HashMap<>();

		public WorldData(@Nonnull CompoundTag cmp) {
			ListTag list = cmp.getList("list", 11);
			for (int i = 0; i < list.size(); i += 2) {
				Tag from = list.get(i);
				Tag to = list.get(i + 1);
				BlockPos fromPos = BlockPos.CODEC.decode(NbtOps.INSTANCE, from).result().get().getFirst();
				BlockPos toPos = BlockPos.CODEC.decode(NbtOps.INSTANCE, to).result().get().getFirst();

				mapping.put(fromPos, toPos);
			}
		}

		@Nonnull
		@Override
		public CompoundTag save(@Nonnull CompoundTag cmp) {
			ListTag list = new ListTag();
			for (Map.Entry<BlockPos, BlockPos> e : mapping.entrySet()) {
				Tag from = BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, e.getKey()).result().get();
				Tag to = BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue()).result().get();
				list.add(from);
				list.add(to);
			}
			cmp.put("list", list);
			return cmp;
		}

		public static WorldData get(Level world) {
			WorldData data = ((ServerLevel) world).getDataStorage().get(WorldData::new, ID);
			if (data == null) {
				data = new WorldData(new CompoundTag());
				data.setDirty();
				((ServerLevel) world).getDataStorage().set(ID, data);
			}
			return data;
		}
	}
}
