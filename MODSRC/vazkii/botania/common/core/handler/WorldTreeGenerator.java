/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 22, 2014, 4:43:32 PM (GMT)]
 */
package vazkii.botania.common.core.handler;

import net.minecraft.world.World;
import vazkii.botania.common.block.ModBlocks;

public class WorldTreeGenerator {

	World world;
	int srcx, srcy, srcz;

	int x, y, z;

	int blocksPlaced;

	public WorldTreeGenerator(World world, int srcx, int srcy, int srcz) {
		this.world = world;
		this.srcx = srcx;
		this.srcy = srcy;
		this.srcz = srcz;
	}

	public void generate() {
		for(int i = 0; i < 116; i++)
			generateRing(i, (int) Math.sqrt(256 - (Math.min(i, 64) - 5) * 2));

		System.out.println("Blocks Placed: " + blocksPlaced);
	}

	public void generateRing(int yoff, int radius) {
		int id = ModBlocks.livingwood.blockID;
		y = yoff;

		for(int i = 0; i < 360; i++) {
			if(i % 90 == 0)
				continue;

			int meta = world.rand.nextInt(200) == 0 ? 5 : 0;
			generateRingVal(radius, i, id, meta);
		}

		int mul = 4;
		for(int i = 0; i < mul; i++)
			for(int j = 0; j < mul; j++) {
				int meta = world.rand.nextInt(4) == 0 ? 5 : 0;
				int angle = ((y - j) * mul) % 360;

				generateRingVal(radius - i - 3, angle, id, meta);
				generateRingVal(radius - i - 3, angle + 180, id, meta);
			}

		mul = 3;

		for(int i = 0; i < mul; i++)
			for(int j = 0; j < mul; j++) {
				int meta = world.rand.nextInt(20) == 0 ? 5 : 0;
				int angle = ((y - j) * mul) % 360;
				generateRingVal(radius + i - 1, angle + 90, id, meta);
				generateRingVal(radius + i - 1, angle + 270, id, meta);
			}
	}

	public void generateRingVal(int radius, int angle, int blockid, int meta) {
		int nx = (int) (Math.cos(angle * Math.PI / 180D) * radius);
		int nz = (int) (Math.sin(angle * Math.PI / 180D) * radius);

		if(x != nx || z != nz) {
			x = nx;
			z = nz;
			place(srcx + x, srcy + y, srcz + z, blockid, meta);
		}
	}


	public void place(int x, int y, int z, int id, int meta) {
		world.setBlock(x, y, z, id, meta, 2);
		blocksPlaced++;
	}
}
