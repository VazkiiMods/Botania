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

import net.minecraft.block.Block;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;
import vazkii.botania.common.block.ModBlocks;

public class BiomeDecorationHandler {

	@ForgeSubscribe(priority = EventPriority.LOWEST)
	public void onWorldDecoration(DecorateBiomeEvent.Decorate event) {
		System.out.println("do " + event.type);

		if((event.getResult() == Result.ALLOW || event.getResult() == Result.DEFAULT) && event.type == EventType.FLOWERS)
			for(int i = 0; i < 3; i++) {
				System.out.println("do1");

				int x = event.chunkX + event.world.rand.nextInt(16) + 8;
				int y = event.world.rand.nextInt(128);
				int z = event.chunkZ + event.world.rand.nextInt(16) + 8;
				
				 for (int j = 0; j < 64; j++) {
			            int x1 = x + event.world.rand.nextInt(8) - event.world.rand.nextInt(8);
			            int y1 = y + event.world.rand.nextInt(4) - event.world.rand.nextInt(4);
			            int z1 = z + event.world.rand.nextInt(8) - event.world.rand.nextInt(8);
	
			            if (event.world.isAirBlock(x1, y1, z1) && (!event.world.provider.hasNoSky || y1 < 127) && Block.blocksList[ModBlocks.flower.blockID].canBlockStay(event.world, x1, y1, z1))
			            	event.world.setBlock(x1, y1, z1, ModBlocks.flower.blockID, event.world.rand.nextInt(16), 2);
			        }
			}
	}
}
