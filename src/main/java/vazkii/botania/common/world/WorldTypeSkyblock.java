/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 7, 2015, 2:06:01 AM (GMT)]
 */
package vazkii.botania.common.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderFlat;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class WorldTypeSkyblock extends WorldType {

	public WorldTypeSkyblock() {
		super("botania-skyblock");
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public static boolean isWorldSkyblock(World world) {
		return world.getWorldInfo().getTerrainType() instanceof WorldTypeSkyblock;
	}
	
	@Override
	public boolean showWorldInfoNotice() {
		return true;
	}
	
	@Override
	public boolean hasVoidParticles(boolean flag) {
		return false;
	}
	
	@Override
	public int getMinimumSpawnHeight(World world) {
		return 86;
	}
	
	@Override
	public int getSpawnFuzz() {
		return 1;
	}
	
	@Override
	public float getCloudHeight() {
		return 260f;
	}
	

	@Override
	public IChunkProvider getChunkGenerator(World world, String generatorOptions) {
		return new ChunkProviderFlat(world, world.getSeed(), false, "2;1x0;");
	}
	
	@SubscribeEvent
	public void onPlayerUpdate(LivingUpdateEvent event) {
		if(event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			World world = player.worldObj;
			if(isWorldSkyblock(world)) {
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
	
	public void createSkyblock(World world, int x, int y, int z) {
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 4; j++)
				for(int k = 0; k < 3; k++)
					world.setBlock(x - 1 + i, y - 1 - j, z - 1 + k, j == 0 ? Blocks.grass : Blocks.dirt);
		world.setBlock(x - 1, y - 2, z, Blocks.flowing_water);
		world.setBlock(x + 1, y + 2, z + 1, ModBlocks.manaFlame);
		
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
