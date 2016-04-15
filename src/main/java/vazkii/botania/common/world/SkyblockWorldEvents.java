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

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileManaFlame;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import java.awt.*;

public final class SkyblockWorldEvents {

	private static final String TAG_MADE_ISLAND = "Botania-MadeIsland";
	private static final String TAG_HAS_OWN_ISLAND = "Botania-HasOwnIsland";
	private static final String TAG_ISLAND_X = "Botania-IslandX";
	private static final String TAG_ISLAND_Y = "Botania-IslandY";
	private static final String TAG_ISLAND_Z = "Botania-IslandZ";

	@SubscribeEvent
	public void onPlayerUpdate(LivingUpdateEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayer && !event.getEntityLiving().worldObj.isRemote) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			NBTTagCompound data = player.getEntityData();
			if(!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG))
				data.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());

			NBTTagCompound persist = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			if(player.ticksExisted > 3 && !persist.getBoolean(TAG_MADE_ISLAND)) {
				World world = player.worldObj;
				if(WorldTypeSkyblock.isWorldSkyblock(world)) {
					BlockPos coords = world.getSpawnPoint();
					if(world.getBlockState(coords.down(4)).getBlock() != Blocks.bedrock && world.provider.getDimension() == 0)
						spawnPlayer(player, coords, false);
				}


				persist.setBoolean(TAG_MADE_ISLAND, true);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
		if(WorldTypeSkyblock.isWorldSkyblock(event.getWorld())) {
			ItemStack equipped = event.getItemStack();
			if(equipped == null && event.getEntityPlayer().isSneaking()) {
				Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
				if(block == Blocks.grass || block == Blocks.dirt) {
					if(event.getWorld().isRemote)
						event.getEntityPlayer().swingArm(event.getHand());
					else {
						event.getWorld().playSound(null, event.getPos(), block.getSoundType().getBreakSound(), SoundCategory.BLOCKS, block.getSoundType().getVolume() * 0.4F, block.getSoundType().getPitch() + (float) (Math.random() * 0.2 - 0.1));
						if(Math.random() < 0.8)
							event.getEntityPlayer().dropPlayerItemWithRandomChoice(new ItemStack(ModItems.manaResource, 1, 21), false);
					}
				}
			} else if(equipped != null && equipped.getItem() == Items.bowl && !event.getWorld().isRemote) {
				RayTraceResult RayTraceResult = ToolCommons.raytraceFromEntity(event.getWorld(), event.getEntityPlayer(), true, 4.5F);
				if(RayTraceResult != null) {
					if (RayTraceResult.typeOfHit == net.minecraft.util.math.RayTraceResult.Type.BLOCK) {
						if(event.getWorld().getBlockState(RayTraceResult.getBlockPos()).getMaterial() == Material.water) {
							--equipped.stackSize;

							if(equipped.stackSize <= 0)
								event.getEntityPlayer().inventory.setInventorySlotContents(event.getEntityPlayer().inventory.currentItem, new ItemStack(ModItems.waterBowl));
							else event.getEntityPlayer().dropPlayerItemWithRandomChoice(new ItemStack(ModItems.waterBowl), false);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onDrops(BlockEvent.HarvestDropsEvent event) {
		if(WorldTypeSkyblock.isWorldSkyblock(event.getWorld()) && event.getState().getBlock() == Blocks.tallgrass) {
			ItemStack stackToRemove = null;
			for(ItemStack stack : event.getDrops())
				if(stack.getItem() == Items.wheat_seeds && event.getWorld().rand.nextInt(4) == 0) {
					stackToRemove = stack;
					break;
				}

			if(stackToRemove != null) {
				event.getDrops().remove(stackToRemove);
				event.getDrops().add(new ItemStack(event.getWorld().rand.nextBoolean() ? Items.pumpkin_seeds : Items.melon_seeds));
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
			createSkyblock(player.worldObj, pos);

			if(player instanceof EntityPlayerMP) {
				EntityPlayerMP pmp = (EntityPlayerMP) player;
				pmp.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 1.6, pos.getZ() + 0.5);
				pmp.setSpawnChunk(pos, true, player.worldObj.provider.getDimension());
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
					world.setBlockState(pos.add(-1 + i, -1 - j, -1 + k), j == 0 ? Blocks.grass.getDefaultState() : Blocks.dirt.getDefaultState());
		world.setBlockState(pos.add(-1, -2, 0), Blocks.flowing_water.getDefaultState());
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

		world.setBlockState(pos.down(4), Blocks.bedrock.getDefaultState());
	}

}
