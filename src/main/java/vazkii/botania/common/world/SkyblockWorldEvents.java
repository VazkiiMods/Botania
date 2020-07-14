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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileManaFlame;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.lib.ModTags;

public final class SkyblockWorldEvents {

	private SkyblockWorldEvents() {}

	private static final ITag.INamedTag<Block> PEBBLE_SOURCES = BlockTags.makeWrapperTag("gardenofglass:pebble_sources");
	private static final String TAG_MADE_ISLAND = "Botania-MadeIsland";
	private static final String TAG_HAS_OWN_ISLAND = "Botania-HasOwnIsland";
	private static final String TAG_ISLAND_X = "Botania-IslandX";
	private static final String TAG_ISLAND_Y = "Botania-IslandY";
	private static final String TAG_ISLAND_Z = "Botania-IslandZ";

	public static void onPlayerUpdate(LivingUpdateEvent event) {
		if (event.getEntityLiving() instanceof PlayerEntity && !event.getEntityLiving().world.isRemote) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			CompoundNBT data = player.getPersistentData();
			if (!data.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
				data.put(PlayerEntity.PERSISTED_NBT_TAG, new CompoundNBT());
			}

			CompoundNBT persist = data.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
			if (player.ticksExisted > 3 && !persist.getBoolean(TAG_MADE_ISLAND)) {
				World overworld = ServerLifecycleHooks.getCurrentServer().getWorld(World.field_234918_g_);
				ServerWorld world = (ServerWorld) player.world;
				if (SkyblockChunkGenerator.isWorldSkyblock(world)) {
					BlockPos coords = world.func_241135_u_();
					if (coords.getY() <= 0) {
						coords = new BlockPos(coords.getX(), 64, coords.getZ());
						world.func_241124_a__(coords);
					}
					if (world.getBlockState(coords.down(4)).getBlock() != Blocks.BEDROCK && world == overworld) {
						spawnPlayer(player, coords, false);
					}
				}

				persist.putBoolean(TAG_MADE_ISLAND, true);
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

	public static void spawnPlayer(PlayerEntity player, BlockPos pos, boolean fabricated) {
		CompoundNBT data = player.getPersistentData();
		if (!data.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
			data.put(PlayerEntity.PERSISTED_NBT_TAG, new CompoundNBT());
		}
		CompoundNBT persist = data.getCompound(PlayerEntity.PERSISTED_NBT_TAG);

		final boolean test = false;

		if (test || !persist.getBoolean(TAG_HAS_OWN_ISLAND)) {
			createSkyblock(player.world, pos);

			if (player instanceof ServerPlayerEntity) {
				ServerPlayerEntity pmp = (ServerPlayerEntity) player;
				pmp.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 1.6, pos.getZ() + 0.5);
				pmp.func_241153_a_(pmp.world.func_234923_W_(), pos, true, false);
				if (ConfigHandler.COMMON.gogSpawnWithLexicon.get()) {
					player.inventory.addItemStackToInventory(new ItemStack(ModItems.lexicon));
				}
			}

			if (fabricated) {
				persist.putBoolean(TAG_HAS_OWN_ISLAND, true);
				persist.putDouble(TAG_ISLAND_X, player.getPosX());
				persist.putDouble(TAG_ISLAND_Y, player.getPosY());
				persist.putDouble(TAG_ISLAND_Z, player.getPosZ());
			}
		} else {
			double posX = persist.getDouble(TAG_ISLAND_X);
			double posY = persist.getDouble(TAG_ISLAND_Y);
			double posZ = persist.getDouble(TAG_ISLAND_Z);

			if (player instanceof ServerPlayerEntity) {
				ServerPlayerEntity pmp = (ServerPlayerEntity) player;
				pmp.setPositionAndUpdate(posX, posY, posZ);
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
