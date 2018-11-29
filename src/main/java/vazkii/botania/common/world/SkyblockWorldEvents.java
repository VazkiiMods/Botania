/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 7, 2015, 6:14:18 PM (GMT)]
 */
package vazkii.botania.common.world;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileManaFlame;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import java.awt.Color;

public final class SkyblockWorldEvents {

	private SkyblockWorldEvents() {}

	private static final String TAG_MADE_ISLAND = "Botania-MadeIsland";
	private static final String TAG_HAS_OWN_ISLAND = "Botania-HasOwnIsland";
	private static final String TAG_ISLAND_X = "Botania-IslandX";
	private static final String TAG_ISLAND_Y = "Botania-IslandY";
	private static final String TAG_ISLAND_Z = "Botania-IslandZ";

	@SubscribeEvent
	public static void onPlayerUpdate(LivingUpdateEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayer && !event.getEntityLiving().world.isRemote) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			NBTTagCompound data = player.getEntityData();
			if(!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG))
				data.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());

			NBTTagCompound persist = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			if(player.ticksExisted > 3 && !persist.getBoolean(TAG_MADE_ISLAND)) {
				World world = player.world;
				if(WorldTypeSkyblock.isWorldSkyblock(world)) {
					BlockPos coords = world.getSpawnPoint();
					if(world.getBlockState(coords.down(4)).getBlock() != Blocks.BEDROCK && world.provider.getDimension() == 0)
						spawnPlayer(player, coords, false);
				}


				persist.setBoolean(TAG_MADE_ISLAND, true);
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
		if(Botania.gardenOfGlassLoaded) {
			ItemStack equipped = event.getItemStack();
			if(equipped.isEmpty() && event.getEntityPlayer().isSneaking()) {
				Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
				if(ImmutableSet.of(Blocks.GRASS, Blocks.GRASS_PATH, Blocks.FARMLAND, Blocks.DIRT, ModBlocks.altGrass).contains(block)) {
					if(event.getWorld().isRemote)
						event.getEntityPlayer().swingArm(event.getHand());
					else {
						event.getWorld().playSound(null, event.getPos(), block.getSoundType().getBreakSound(), SoundCategory.BLOCKS, block.getSoundType().getVolume() * 0.4F, block.getSoundType().getPitch() + (float) (Math.random() * 0.2 - 0.1));

						if(Math.random() < 0.8)
							event.getEntityPlayer().dropItem(new ItemStack(ModItems.manaResource, 1, 21), false);
					}

					event.setCanceled(true);
					event.setCancellationResult(EnumActionResult.SUCCESS);
				}
			} else if(!equipped.isEmpty() && equipped.getItem() == Items.BOWL) {
				RayTraceResult rtr = ToolCommons.raytraceFromEntity(event.getWorld(), event.getEntityPlayer(), true, 4.5F);
				if(rtr != null) {
					if (rtr.typeOfHit == net.minecraft.util.math.RayTraceResult.Type.BLOCK) {
						if(event.getWorld().getBlockState(rtr.getBlockPos()).getMaterial() == Material.WATER) {
							if(!event.getWorld().isRemote) {
								equipped.shrink(1);

								if(equipped.isEmpty())
									event.getEntityPlayer().setHeldItem(event.getHand(), new ItemStack(ModItems.waterBowl));
								else ItemHandlerHelper.giveItemToPlayer(event.getEntityPlayer(), new ItemStack(ModItems.waterBowl));
							}

							event.setCanceled(true);
							event.setCancellationResult(EnumActionResult.SUCCESS);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onDrops(BlockEvent.HarvestDropsEvent event) {
		if(Botania.gardenOfGlassLoaded && event.getState().getBlock() == Blocks.TALLGRASS) {
			ItemStack stackToRemove = ItemStack.EMPTY;
			for(ItemStack stack : event.getDrops())
				if(stack.getItem() == Items.WHEAT_SEEDS && event.getWorld().rand.nextInt(4) == 0) {
					stackToRemove = stack;
					break;
				}

			if(!stackToRemove.isEmpty()) {
				event.getDrops().remove(stackToRemove);
				event.getDrops().add(new ItemStack(event.getWorld().rand.nextBoolean() ? Items.PUMPKIN_SEEDS : Items.MELON_SEEDS));
			}
		}
	}

	public static void spawnPlayer(EntityPlayer player, BlockPos pos, boolean fabricated) {
		NBTTagCompound data = player.getEntityData();
		if(!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG))
			data.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
		NBTTagCompound persist = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);

		final boolean test = false;

		if(test || !persist.getBoolean(TAG_HAS_OWN_ISLAND)) {
			createSkyblock(player.world, pos);

			if(player instanceof EntityPlayerMP) {
				EntityPlayerMP pmp = (EntityPlayerMP) player;
				pmp.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 1.6, pos.getZ() + 0.5);
				pmp.setSpawnChunk(pos, true, player.world.provider.getDimension());
				player.inventory.addItemStackToInventory(new ItemStack(ModItems.lexicon));
			}

			if(fabricated) {
				persist.setBoolean(TAG_HAS_OWN_ISLAND, true);
				persist.setDouble(TAG_ISLAND_X, player.posX);
				persist.setDouble(TAG_ISLAND_Y, player.posY);
				persist.setDouble(TAG_ISLAND_Z, player.posZ);
			}
		} else {
			double posX = persist.getDouble(TAG_ISLAND_X);
			double posY = persist.getDouble(TAG_ISLAND_Y);
			double posZ = persist.getDouble(TAG_ISLAND_Z);

			if(player instanceof EntityPlayerMP) {
				EntityPlayerMP pmp = (EntityPlayerMP) player;
				pmp.setPositionAndUpdate(posX, posY, posZ);
			}
		}
	}

	public static void createSkyblock(World world, BlockPos pos) {
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 4; j++)
				for(int k = 0; k < 3; k++)
					world.setBlockState(pos.add(-1 + i, -1 - j, -1 + k), j == 0 ? Blocks.GRASS.getDefaultState() : Blocks.DIRT.getDefaultState());
		world.setBlockState(pos.add(-1, -2, 0), Blocks.FLOWING_WATER.getDefaultState());
		world.setBlockState(pos.add(1, 2, 1), ModBlocks.manaFlame.getDefaultState());
		((TileManaFlame) world.getTileEntity(pos.add(1, 2, 1))).setColor(new Color(70 + world.rand.nextInt(185), 70 + world.rand.nextInt(185), 70 + world.rand.nextInt(185)).getRGB());

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
		for(int[] root : rootPositions)
			world.setBlockState(pos.add(root[0], root[1], root[2]), ModBlocks.root.getDefaultState());

		world.setBlockState(pos.down(4), Blocks.BEDROCK.getDefaultState());
	}

}
