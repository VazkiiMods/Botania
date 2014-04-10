/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Apr 9, 2014, 5:11:34 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibItemNames;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class ItemGrassSeeds extends ItemMod {

	private static Map<Integer, List<BlockSwapper>> blockSwappers = new HashMap();
	
	public ItemGrassSeeds() {
		super();
		setUnlocalizedName(LibItemNames.GRASS_SEEDS);
		FMLCommonHandler.instance().bus().register(this);
	}
	
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		Block block = par3World.getBlock(par4, par5, par6);
		if(block == Blocks.dirt) {
			addBlockSwapper(par3World, par4, par5, par6);
			
			par3World.setBlock(par4, par5, par6, Blocks.grass);
			for(int i = 0; i < 50; i++) {
				double x = (Math.random() - 0.5) * 3;
				double y = (Math.random() - 0.5) + 1;
				double z = (Math.random() - 0.5) * 3;

				float velMul = 0.025F;

				Botania.proxy.wispFX(par3World, par4 + 0.5 + x, par5 + 0.5 + y, par6 + 0.5 + z, 0F, 0.4F, 0F, (float) Math.random() * 0.15F + 0.15F, (float) -x * velMul, (float) -y * velMul, (float) -z * velMul);
			}

			par1ItemStack.stackSize--;
		}
			
		return true;
	}
	
	@SubscribeEvent
	public void onTickEnd(TickEvent.WorldTickEvent event) {
		if(event.phase == Phase.END) {
			int dim = event.world.provider.dimensionId;
			if(blockSwappers.containsKey(dim)) {
				List<BlockSwapper> swappers = blockSwappers.get(dim);
				List<BlockSwapper> swappersSafe = new ArrayList(swappers);
				
				for(BlockSwapper s : swappersSafe)
					s.tick(swappers);
			}
		}
	}
	
	private static void addBlockSwapper(World world, int x, int y, int z) {
		BlockSwapper swapper = new BlockSwapper(world, new ChunkCoordinates(x, y, z));
		
		int dim = world.provider.dimensionId;
		if(!blockSwappers.containsKey(dim))
			blockSwappers.put(dim, new ArrayList());
		blockSwappers.get(dim).add(swapper);
	}
	
	private static class BlockSwapper {
		
		final World world;
		final Random rand;
		
		ChunkCoordinates startCoords;
		int ticksExisted = 0;
		
		BlockSwapper(World world, ChunkCoordinates coords) {
			this.world = world;
			rand = new Random(coords.posX ^ coords.posY ^ coords.posZ);
			startCoords = coords;
		}
		
		void tick(List<BlockSwapper> list) {
			++ticksExisted;
			
			int range = 3;
			for(int i = -range; i < range + 1; i++)
				for(int j = -range; j < range + 1; j++) {
					int x = startCoords.posX + i;
					int y = startCoords.posY;
					int z = startCoords.posZ + j;
					Block block = world.getBlock(x, y, z);
					if(block == Blocks.grass) {
						if(ticksExisted % 20 == 0) {
							List<ChunkCoordinates> validCoords = new ArrayList();
							for(int k = -1; k < 2; k++)
								for(int l = -1; l < 2; l++) {
									int x1 = x + k;
									int z1 = z + l;
									Block block1 = world.getBlock(x1, y, z1);
									if(block1 == Blocks.dirt)
										validCoords.add(new ChunkCoordinates(x1, y, z1));
								}
							
							if(!validCoords.isEmpty() && !world.isRemote) {
								ChunkCoordinates coords = validCoords.get(rand.nextInt(validCoords.size()));
								world.setBlock(coords.posX, coords.posY, coords.posZ, Blocks.grass);
							}
						}
					}
				}

			
			if(ticksExisted >= 80)
				list.remove(this);
		}
	}
	
}
