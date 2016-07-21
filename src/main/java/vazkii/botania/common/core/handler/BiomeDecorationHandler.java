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

import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.api.item.IFlowerlessBiome;
import vazkii.botania.api.item.IFlowerlessWorld;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.BlockModFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.subtile.generating.SubTileDaybloom;
import vazkii.botania.common.block.tile.TileSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;

public class BiomeDecorationHandler {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onWorldDecoration(DecorateBiomeEvent.Decorate event) {
		if((event.getResult() == Result.ALLOW || event.getResult() == Result.DEFAULT) && event.getType() == EventType.FLOWERS) {
			boolean flowers = true;
			if(event.getWorld().provider instanceof IFlowerlessWorld)
				flowers = ((IFlowerlessWorld) event.getWorld().provider).generateFlowers(event.getWorld());
			else if(event.getWorld().getBiome(event.getPos()) instanceof IFlowerlessBiome)
				flowers = ((IFlowerlessBiome) event.getWorld().getBiome(event.getPos())).canGenerateFlowers(event.getWorld(), event.getPos().getX(), event.getPos().getZ());

			if(!flowers)
				return;

			int dist = Math.min(8, Math.max(1, ConfigHandler.flowerPatchSize));
			for(int i = 0; i < ConfigHandler.flowerQuantity; i++) {
				if(event.getRand().nextInt(ConfigHandler.flowerPatchChance) == 0) {
					int x = event.getPos().getX() + event.getRand().nextInt(16) + 8;
					int z = event.getPos().getZ() + event.getRand().nextInt(16) + 8;
					int y = event.getWorld().getTopSolidOrLiquidBlock(event.getPos()).getY();

					EnumDyeColor color = EnumDyeColor.byMetadata(event.getRand().nextInt(16));
					boolean primus = event.getRand().nextInt(380) == 0;

					for(int j = 0; j < ConfigHandler.flowerDensity * ConfigHandler.flowerPatchChance; j++) {
						int x1 = x + event.getRand().nextInt(dist * 2) - dist;
						int y1 = y + event.getRand().nextInt(4) - event.getRand().nextInt(4);
						int z1 = z + event.getRand().nextInt(dist * 2) - dist;
						BlockPos pos2 = new BlockPos(x1, y1, z1);
						if(event.getWorld().isAirBlock(pos2) && (!event.getWorld().provider.getHasNoSky() || y1 < 127) && ModBlocks.flower.canPlaceBlockAt(event.getWorld(), pos2)) {
							if(primus) {
								event.getWorld().setBlockState(pos2, ModBlocks.specialFlower.getDefaultState(), 2);
								TileSpecialFlower flower = (TileSpecialFlower) event.getWorld().getTileEntity(pos2);
								flower.setSubTile(event.getRand().nextBoolean() ? LibBlockNames.SUBTILE_NIGHTSHADE_PRIME : LibBlockNames.SUBTILE_DAYBLOOM_PRIME);
								SubTileDaybloom subtile = (SubTileDaybloom) flower.getSubTile();
								subtile.setPrimusPosition();
							} else {
								event.getWorld().setBlockState(pos2, ModBlocks.flower.getDefaultState().withProperty(BotaniaStateProps.COLOR, color), 2);
								if(event.getRand().nextDouble() < ConfigHandler.flowerTallChance && ((BlockModFlower) ModBlocks.flower).canGrow(event.getWorld(), pos2, event.getWorld().getBlockState(pos2), false))
									BlockModFlower.placeDoubleFlower(event.getWorld(), pos2, color, 0);
							}
						}
					}
				}
			}

			for(int i = 0; i < ConfigHandler.mushroomQuantity; i++) {
				int x = event.getPos().getX() + event.getRand().nextInt(16) + 8;
				int z = event.getPos().getZ() + event.getRand().nextInt(16) + 8;
				int y = event.getRand().nextInt(26) + 4;
				BlockPos pos3 = new BlockPos(x, y, z);
				EnumDyeColor color = EnumDyeColor.byMetadata(event.getRand().nextInt(16));
				if(event.getWorld().isAirBlock(pos3) && ModBlocks.mushroom.canPlaceBlockAt(event.getWorld(), pos3))
					event.getWorld().setBlockState(pos3, ModBlocks.mushroom.getDefaultState().withProperty(BotaniaStateProps.COLOR, color), 2);
			}
		}
	}
}