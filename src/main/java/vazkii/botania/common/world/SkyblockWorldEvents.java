/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.world;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileManaFlame;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.network.PacketGogWorld;
import vazkii.botania.mixin.AccessorSoundType;

public final class SkyblockWorldEvents {

	private SkyblockWorldEvents() {}

	private static final ResourceLocation PEBBLE_SOURCES = new ResourceLocation("minecraft:dirt");

	public static void syncGogStatus(ServerPlayer e) {
		boolean isGog = SkyblockChunkGenerator.isWorldSkyblock(e.level);
		if (isGog) {
			PacketGogWorld.send(e);
		}
	}

	public static void onPlayerJoin(ServerPlayer player) {
		Level world = player.level;
		if (SkyblockChunkGenerator.isWorldSkyblock(world)) {
			SkyblockSavedData data = SkyblockSavedData.get((ServerLevel) world);
			if (!data.skyblocks.containsValue(Util.NIL_UUID)) {
				IslandPos islandPos = data.getSpawn();
				((ServerLevel) world).setDefaultSpawnPos(islandPos.getCenter(), 0);
				spawnPlayer(player, islandPos);
				Botania.LOGGER.info("Created the spawn GoG island");
			}
		}
	}

	public static InteractionResult onPlayerInteract(Player player, Level world, InteractionHand hand, BlockHitResult hit) {
		if (Botania.gardenOfGlassLoaded) {
			ItemStack equipped = player.getItemInHand(hand);

			if (equipped.isEmpty() && player.isShiftKeyDown()) {
				BlockState state = world.getBlockState(hit.getBlockPos());
				Block block = state.getBlock();

				Tag<Block> tag = world.getTagManager().getOrEmpty(Registry.BLOCK_REGISTRY).getTagOrEmpty(PEBBLE_SOURCES);
				if (tag.contains(block)) {
					SoundType st = state.getSoundType();
					SoundEvent sound = ((AccessorSoundType) st).botania_getBreakSound();
					player.playSound(sound, st.getVolume() * 0.4F, st.getPitch() + (float) (Math.random() * 0.2 - 0.1));

					if (world.isClientSide) {
						player.swing(hand);
					} else if (Math.random() < 0.8) {
						player.drop(new ItemStack(ModItems.pebble), false);
					}

					return InteractionResult.SUCCESS;
				}
			} else if (!equipped.isEmpty() && equipped.is(Items.BOWL)) {
				BlockHitResult rtr = ToolCommons.raytraceFromEntity(player, 4.5F, true);
				if (rtr.getType() == HitResult.Type.BLOCK) {
					BlockPos pos = rtr.getBlockPos();
					if (world.getBlockState(pos).getMaterial() == Material.WATER) {
						if (!world.isClientSide) {
							equipped.shrink(1);

							if (equipped.isEmpty()) {
								player.setItemInHand(hand, new ItemStack(ModItems.waterBowl));
							} else {
								player.getInventory().placeItemBackInInventory(new ItemStack(ModItems.waterBowl));
							}
						}

						return InteractionResult.SUCCESS;
					}
				}
			}
		}
		return InteractionResult.PASS;
	}

	public static void spawnPlayer(Player player, IslandPos islandPos) {
		BlockPos pos = islandPos.getCenter();
		createSkyblock(player.level, pos);

		if (player instanceof ServerPlayer pmp) {
			pmp.teleportTo(pos.getX() + 0.5, pos.getY() + 1.6, pos.getZ() + 0.5);
			pmp.setRespawnPosition(pmp.level.dimension(), pos, 0, true, false);
			if (ConfigHandler.COMMON.gogSpawnWithLexicon.getValue()) {
				player.getInventory().add(new ItemStack(ModItems.lexicon));
			}
		}
	}

	public static void createSkyblock(Level world, BlockPos pos) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < 3; k++) {
					world.setBlockAndUpdate(pos.offset(-1 + i, -1 - j, -1 + k), j == 0 ? Blocks.GRASS_BLOCK.defaultBlockState() : Blocks.DIRT.defaultBlockState());
				}
			}
		}
		world.setBlockAndUpdate(pos.offset(-1, -2, 0), Blocks.WATER.defaultBlockState());
		world.setBlockAndUpdate(pos.offset(1, 2, 1), ModBlocks.manaFlame.defaultBlockState());
		int r = 70 + world.random.nextInt(185);
		int g = 70 + world.random.nextInt(185);
		int b = 70 + world.random.nextInt(185);
		int color = r << 16 | g << 8 | b;
		((TileManaFlame) world.getBlockEntity(pos.offset(1, 2, 1))).setColor(color);

		int[][] rootPositions = new int[][] {
				{ -1, -3, -1 },
				{ -2, -4, -1 },
				{ -2, -4, -2 },
				{ +1, -4, -1 },
				{ +1, -5, -1 },
				{ +2, -5, -1 },
				{ +2, -6, +0 },
				{ +0, -4, +2 },
				{ +0, -5, +2 },
				{ +0, -5, +3 },
				{ +0, -6, +3 },
		};
		for (int[] root : rootPositions) {
			world.setBlockAndUpdate(pos.offset(root[0], root[1], root[2]), ModBlocks.root.defaultBlockState());
		}

		world.setBlockAndUpdate(pos.below(4), Blocks.BEDROCK.defaultBlockState());
	}

}
