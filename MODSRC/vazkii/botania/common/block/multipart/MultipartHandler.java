/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 3, 2014, 4:37:28 PM (GMT)]
 */
package vazkii.botania.common.block.multipart;

import net.minecraft.block.Block;
import vazkii.botania.common.block.ModBlocks;

public final class MultipartHandler {

	public MultipartHandler() {
		registerMultipartMetadataLine(ModBlocks.livingrock, 4);
		registerMultipartMetadataLine(ModBlocks.livingwood, 4);
	}

	private static void registerMultipartMetadataLine(Block block, int maxMeta) {
		for(int i = 0; i < maxMeta; i++)
			registerMultipart(block, i);
	}

	private static void registerMultipart(Block block, int meta) {
		MicroMaterialRegistry.registerMaterial(new BlockMicroMaterial(block, meta), block.getUnlocalizedName() + (meta == 0 ? "" : "_" + meta));

	}

}