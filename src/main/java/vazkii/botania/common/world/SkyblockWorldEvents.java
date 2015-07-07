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

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileManaFlame;
import vazkii.botania.common.item.ModItems;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class SkyblockWorldEvents {

	@SubscribeEvent
	public void onPlayerUpdate(LivingUpdateEvent event) {
		if(event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			World world = player.worldObj;
			if(WorldTypeSkyblock.isWorldSkyblock(world)) {
				ChunkCoordinates coords = world.getSpawnPoint();
				if(world.getBlock(coords.posX, coords.posY - 4, coords.posZ) != Blocks.bedrock) {
					createSkyblock(world, coords.posX, coords.posY, coords.posZ);

					if(player instanceof EntityPlayerMP) {
						EntityPlayerMP pmp = (EntityPlayerMP) player;
						pmp.setPositionAndUpdate(coords.posX + 0.5, coords.posY + 1.6, coords.posZ + 0.5);
						pmp.inventory.addItemStackToInventory(new ItemStack(ModItems.lexicon));
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(WorldTypeSkyblock.isWorldSkyblock(event.world) && event.action == Action.RIGHT_CLICK_BLOCK && event.entityPlayer.getCurrentEquippedItem() == null && event.entityPlayer.isSneaking()) {
			Block block = event.world.getBlock(event.x, event.y, event.z);
			if(block == Blocks.grass || block == Blocks.dirt) {
				if(event.world.isRemote)
					event.entityPlayer.swingItem();
				else {
					event.world.playSoundEffect(event.x + 0.5, event.y + 0.5, event.z + 0.5, block.stepSound.getBreakSound(), block.stepSound.getVolume() * 0.4F, block.stepSound.getPitch() + (float) (Math.random() * 0.2 - 0.1));
					if(Math.random() < 0.4)
						event.entityPlayer.dropPlayerItemWithRandomChoice(new ItemStack(ModItems.manaResource, 1, 21), false);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onDrops(HarvestDropsEvent event) {
		if(WorldTypeSkyblock.isWorldSkyblock(event.world) && event.block == Blocks.tallgrass) {
			ItemStack stackToRemove = null;
			for(ItemStack stack : event.drops)
				if(stack.getItem() == Items.wheat_seeds && event.world.rand.nextInt(10) == 0) {
					stackToRemove = stack;
					break;
				}
					
			if(stackToRemove != null) {
				event.drops.remove(stackToRemove);
				event.drops.add(new ItemStack(event.world.rand.nextBoolean() ? Items.pumpkin_seeds : Items.melon_seeds));
			}
		}
	}
	
	public void createSkyblock(World world, int x, int y, int z) {
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 4; j++)
				for(int k = 0; k < 3; k++)
					world.setBlock(x - 1 + i, y - 1 - j, z - 1 + k, j == 0 ? Blocks.grass : Blocks.dirt);
		world.setBlock(x - 1, y - 2, z, Blocks.flowing_water);
		world.setBlock(x + 1, y + 2, z + 1, ModBlocks.manaFlame);
		((TileManaFlame) world.getTileEntity(x + 1, y + 2, z + 1)).setColor(new Color(70 + world.rand.nextInt(185), 70 + world.rand.nextInt(185), 70 + world.rand.nextInt(185)).getRGB());
		
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
			world.setBlock(x + root[0], y + root[1], z + root[2], ModBlocks.root);
		
		world.setBlock(x, y - 4, z, Blocks.bedrock);
	}
	
}
