/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 19, 2014, 10:16:49 PM (GMT)]
 */
package vazkii.botania.common.core.handler;

import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;
import vazkii.botania.api.item.IFlowerlessBiome;
import vazkii.botania.api.item.IFlowerlessWorld;
import vazkii.botania.common.block.BlockModFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.subtile.generating.SubTileDaybloom;
import vazkii.botania.common.block.tile.TileSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BiomeDecorationHandler {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onWorldDecoration(DecorateBiomeEvent.Decorate event) {
		if((event.getResult() == Result.ALLOW || event.getResult() == Result.DEFAULT) && event.type == EventType.FLOWERS) {
			boolean flowers = true;
			if(event.world.provider instanceof IFlowerlessWorld)
				flowers = ((IFlowerlessWorld) event.world.provider).generateFlowers(event.world);
			else if(event.world.getBiomeGenForCoords(event.chunkX, event.chunkZ) instanceof IFlowerlessBiome)
				flowers = ((IFlowerlessBiome) event.world.getBiomeGenForCoords(event.chunkX, event.chunkZ)).canGenerateFlowers(event.world, event.chunkX, event.chunkZ);

			if(!flowers)
				return;

			int dist = Math.min(8, Math.max(1, ConfigHandler.flowerPatchSize));
			for(int i = 0; i < ConfigHandler.flowerQuantity; i++) {
				if(event.rand.nextInt(ConfigHandler.flowerPatchChance) == 0) {
					int x = event.chunkX + event.rand.nextInt(16) + 8;
					int z = event.chunkZ + event.rand.nextInt(16) + 8;
					int y = event.world.getTopSolidOrLiquidBlock(x, z);

					int color = event.rand.nextInt(16);
					boolean primus = event.rand.nextInt(380) == 0;

					for(int j = 0; j < ConfigHandler.flowerDensity * ConfigHandler.flowerPatchChance; j++) {
						int x1 = x + event.rand.nextInt(dist * 2) - dist;
						int y1 = y + event.rand.nextInt(4) - event.rand.nextInt(4);
						int z1 = z + event.rand.nextInt(dist * 2) - dist;

						if(event.world.isAirBlock(x1, y1, z1) && (!event.world.provider.hasNoSky || y1 < 127) && ModBlocks.flower.canBlockStay(event.world, x1, y1, z1)) {
							if(primus) {
								event.world.setBlock(x1, y1, z1, ModBlocks.specialFlower, 0, 2);
								TileSpecialFlower flower = (TileSpecialFlower) event.world.getTileEntity(x1, y1, z1);
								flower.setSubTile(event.rand.nextBoolean() ? LibBlockNames.SUBTILE_NIGHTSHADE_PRIME : LibBlockNames.SUBTILE_DAYBLOOM_PRIME);
								SubTileDaybloom subtile = (SubTileDaybloom) flower.getSubTile();
								subtile.setPrimusPosition();
							} else {
								event.world.setBlock(x1, y1, z1, ModBlocks.flower, color, 2);
								if(event.rand.nextDouble() < ConfigHandler.flowerTallChance && ((BlockModFlower) ModBlocks.flower).func_149851_a(event.world, x1, y1, z1, false))
									BlockModFlower.placeDoubleFlower(event.world, x1, y1, z1, color, 0);
							}
						}
					}
				}
			}

			for(int i = 0; i < ConfigHandler.mushroomQuantity; i++) {
				int x = event.chunkX + event.rand.nextInt(16) + 8;
				int z = event.chunkZ + event.rand.nextInt(16) + 8;
				int y = event.rand.nextInt(26) + 4;

				int color = event.rand.nextInt(16);
				if(event.world.isAirBlock(x, y, z) && ModBlocks.mushroom.canBlockStay(event.world, x, y, z))
					event.world.setBlock(x, y, z, ModBlocks.mushroom, color, 2);
			}
		}
	}
}