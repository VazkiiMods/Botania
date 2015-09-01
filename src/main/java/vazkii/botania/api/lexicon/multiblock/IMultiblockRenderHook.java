/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 27, 2015, 7:48:47 PM (GMT)]
 */
package vazkii.botania.api.lexicon.multiblock;

import java.util.HashMap;
import java.util.Map;

import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

/**
 * A hook for rendering blocks in the multiblock display.
 */
public interface IMultiblockRenderHook {

	public static Map<Block, IMultiblockRenderHook> renderHooks = new HashMap();

	public void renderBlockForMultiblock(IBlockAccess world, Multiblock mb, Block block, int meta, RenderBlocks renderBlocks, MultiblockComponent comp, float alpha);

	public boolean needsTranslate(Block block);

}
