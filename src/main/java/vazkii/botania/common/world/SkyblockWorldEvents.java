/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileManaFlame;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

public final class SkyblockWorldEvents {

	private SkyblockWorldEvents() {}

	private static final ITag.INamedTag<Block> PEBBLE_SOURCES = BlockTags.makeWrapperTag("gardenofglass:pebble_sources");

	public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		World world = event.getPlayer().world;
		if (SkyblockChunkGenerator.isWorldSkyblock(world)) {
			SkyblockSavedData data = SkyblockSavedData.get((ServerWorld) world);
			if (!data.skyblocks.containsValue(Util.DUMMY_UUID)) {
				IslandPos islandPos = data.getSpawn();
				((ServerWorld) world).func_241124_a__(islandPos.getCenter(), 0);
				spawnPlayer(event.getPlayer(), islandPos);
				Botania.LOGGER.info("Created the spawn GoG island");
			}
		}
	}

	public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
		if (Botania.gardenOfGlassLoaded) {
			ItemStack equipped = event.getItemStack();
			PlayerEntity player = event.getPlayer();

			if (equipped.isEmpty() && player.isSneaking()) {
				BlockState state = event.getWorld().getBlockState(event.getPos());
				Block block = state.getBlock();

				if (PEBBLE_SOURCES.contains(block)) {
					SoundType st = state.getSoundType(event.getWorld(), event.getPos(), player);
					player.playSound(st.getBreakSound(), st.getVolume() * 0.4F, st.getPitch() + (float) (Math.random() * 0.2 - 0.1));

					if (event.getWorld().isRemote) {
						player.swingArm(event.getHand());
					} else if (Math.random() < 0.8) {
						player.dropItem(new ItemStack(ModItems.pebble), false);
					}

					event.setCanceled(true);
					event.setCancellationResult(ActionResultType.SUCCESS);
				}
			} else if (!equipped.isEmpty() && equipped.getItem() == Items.BOWL) {
				BlockRayTraceResult rtr = ToolCommons.raytraceFromEntity(player, 4.5F, true);
				if (rtr.getType() == RayTraceResult.Type.BLOCK) {
					BlockPos pos = rtr.getPos();
					if (event.getWorld().getBlockState(pos).getMaterial() == Material.WATER) {
						if (!event.getWorld().isRemote) {
							equipped.shrink(1);

							if (equipped.isEmpty()) {
								player.setHeldItem(event.getHand(), new ItemStack(ModItems.waterBowl));
							} else {
								player.inventory.placeItemBackInInventory(player.world, new ItemStack(ModItems.waterBowl));
							}
						}

						event.setCanceled(true);
						event.setCancellationResult(ActionResultType.SUCCESS);
					}
				}
			}
		}
	}

	public static void spawnPlayer(PlayerEntity player, IslandPos islandPos) {
		BlockPos pos = islandPos.getCenter();
		createSkyblock(player.world, pos);

		if (player instanceof ServerPlayerEntity) {
			ServerPlayerEntity pmp = (ServerPlayerEntity) player;
			pmp.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 1.6, pos.getZ() + 0.5);
			pmp.func_242111_a(pmp.world.func_234923_W_(), pos, 0, true, false);
			if (ConfigHandler.COMMON.gogSpawnWithLexicon.get()) {
				player.inventory.addItemStackToInventory(new ItemStack(ModItems.lexicon));
			}
		}
	}

	public static void createSkyblock(World world, BlockPos pos) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < 3; k++) {
					world.setBlockState(pos.add(-1 + i, -1 - j, -1 + k), j == 0 ? Blocks.GRASS_BLOCK.getDefaultState() : Blocks.DIRT.getDefaultState());
				}
			}
		}
		world.setBlockState(pos.add(-1, -2, 0), Blocks.WATER.getDefaultState());
		world.setBlockState(pos.add(1, 2, 1), ModBlocks.manaFlame.getDefaultState());
		int r = 70 + world.rand.nextInt(185);
		int g = 70 + world.rand.nextInt(185);
		int b = 70 + world.rand.nextInt(185);
		int color = r << 16 | g << 8 | b;
		((TileManaFlame) world.getTileEntity(pos.add(1, 2, 1))).setColor(color);

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
			world.setBlockState(pos.add(root[0], root[1], root[2]), ModBlocks.root.getDefaultState());
		}

		world.setBlockState(pos.down(4), Blocks.BEDROCK.getDefaultState());
	}

}
