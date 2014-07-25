/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 19, 2014, 10:16:49 PM (GMT)]
 */
package vazkii.botania.common.core.handler;

import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;
import vazkii.botania.common.block.ModBlocks;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BiomeDecorationHandler {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onWorldDecoration(DecorateBiomeEvent.Decorate event) {
		if((event.getResult() == Result.ALLOW || event.getResult() == Result.DEFAULT) && event.type == EventType.FLOWERS)
			for(int i = 0; i < ConfigHandler.flowerQuantity; i++) {
				int x = event.chunkX + event.rand.nextInt(16) + 8;
				int z = event.chunkZ + event.rand.nextInt(16) + 8;
				int y = event.world.getTopSolidOrLiquidBlock(x, z);

				for(int j = 0; j < ConfigHandler.flowerDensity; j++) {
					int x1 = x + event.rand.nextInt(8) - event.rand.nextInt(8);
					int y1 = y + event.rand.nextInt(4) - event.rand.nextInt(4);
					int z1 = z + event.rand.nextInt(8) - event.rand.nextInt(8);

					if(event.world.isAirBlock(x1, y1, z1) && (!event.world.provider.hasNoSky || y1 < 127) && ModBlocks.flower.canBlockStay(event.world, x1, y1, z1))
						event.world.setBlock(x1, y1, z1, ModBlocks.flower, event.rand.nextInt(16), 2);
				}
			}
	}
}
