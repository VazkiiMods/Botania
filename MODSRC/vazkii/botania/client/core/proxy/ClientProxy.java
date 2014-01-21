/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 13, 2014, 7:46:05 PM (GMT)]
 */
package vazkii.botania.client.core.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import vazkii.botania.api.LexiconEntry;
import vazkii.botania.client.fx.FXSparkle;
import vazkii.botania.client.gui.GuiLexicon;
import vazkii.botania.client.gui.GuiLexiconEntry;
import vazkii.botania.client.gui.GuiLexiconIndex;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.client.render.block.RenderAltar;
import vazkii.botania.client.render.tile.RenderTileAltar;
import vazkii.botania.common.block.tile.TileAltar;
import vazkii.botania.common.core.proxy.CommonProxy;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		
		initRenderers();
	}
	
	private void initRenderers() {
		LibRenderIDs.idAltar = RenderingRegistry.getNextAvailableRenderId();
		
		RenderingRegistry.registerBlockHandler(new RenderAltar());
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileAltar.class, new RenderTileAltar());
	}
	
	@Override
	public void setEntryToOpen(LexiconEntry entry) {
		GuiLexicon.currentOpenLexicon = new GuiLexiconEntry(entry, new GuiLexiconIndex(entry.category));
	}
	
	@Override
	public void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float size, int m) {
		FXSparkle sparkle = new FXSparkle(world, x, y, z, size, r, g, b, m);
		Minecraft.getMinecraft().effectRenderer.addEffect(sparkle);
	}
}
