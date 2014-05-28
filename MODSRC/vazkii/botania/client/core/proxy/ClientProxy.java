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
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.handler.BoundTileRenderer;
import vazkii.botania.client.core.handler.CapeHandler;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.core.handler.LightningHandler;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.fx.FXSparkle;
import vazkii.botania.client.fx.FXWisp;
import vazkii.botania.client.gui.GuiLexicon;
import vazkii.botania.client.gui.GuiLexiconEntry;
import vazkii.botania.client.gui.GuiLexiconIndex;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.client.render.block.RenderAltar;
import vazkii.botania.client.render.block.RenderPool;
import vazkii.botania.client.render.block.RenderPylon;
import vazkii.botania.client.render.block.RenderSpecialFlower;
import vazkii.botania.client.render.block.RenderSpreader;
import vazkii.botania.client.render.item.RenderLens;
import vazkii.botania.client.render.item.RenderLexicon;
import vazkii.botania.client.render.tile.RenderTileAltar;
import vazkii.botania.client.render.tile.RenderTileEnchanter;
import vazkii.botania.client.render.tile.RenderTilePool;
import vazkii.botania.client.render.tile.RenderTilePylon;
import vazkii.botania.client.render.tile.RenderTileRuneAltar;
import vazkii.botania.client.render.tile.RenderTileSpreader;
import vazkii.botania.common.block.tile.TileAltar;
import vazkii.botania.common.block.tile.TileEnchanter;
import vazkii.botania.common.block.tile.TilePylon;
import vazkii.botania.common.block.tile.TileRuneAltar;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.core.proxy.CommonProxy;
import vazkii.botania.common.item.ModItems;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);

		FMLCommonHandler.instance().bus().register(new ClientTickHandler());
		MinecraftForge.EVENT_BUS.register(new HUDHandler());
		MinecraftForge.EVENT_BUS.register(new LightningHandler());
		MinecraftForge.EVENT_BUS.register(new CapeHandler());
		if(ConfigHandler.boundBlockWireframe)
			MinecraftForge.EVENT_BUS.register(new BoundTileRenderer());

		initRenderers();
	}

	private void initRenderers() {
		LibRenderIDs.idAltar = RenderingRegistry.getNextAvailableRenderId();
		LibRenderIDs.idSpecialFlower = RenderingRegistry.getNextAvailableRenderId();
		LibRenderIDs.idSpreader = RenderingRegistry.getNextAvailableRenderId();
		LibRenderIDs.idPool = RenderingRegistry.getNextAvailableRenderId();
		LibRenderIDs.idPylon = RenderingRegistry.getNextAvailableRenderId();

		RenderingRegistry.registerBlockHandler(new RenderAltar());
		RenderingRegistry.registerBlockHandler(new RenderSpecialFlower(LibRenderIDs.idSpecialFlower));
		RenderingRegistry.registerBlockHandler(new RenderSpreader());
		RenderingRegistry.registerBlockHandler(new RenderPool());
		RenderingRegistry.registerBlockHandler(new RenderPylon());

		MinecraftForgeClient.registerItemRenderer(ModItems.lens, new RenderLens());
		if(ConfigHandler.lexicon3dModel)
			MinecraftForgeClient.registerItemRenderer(ModItems.lexicon, new RenderLexicon());

		ClientRegistry.bindTileEntitySpecialRenderer(TileAltar.class, new RenderTileAltar());
		ClientRegistry.bindTileEntitySpecialRenderer(TileSpreader.class, new RenderTileSpreader());
		ClientRegistry.bindTileEntitySpecialRenderer(TilePool.class, new RenderTilePool());
		ClientRegistry.bindTileEntitySpecialRenderer(TileRuneAltar.class, new RenderTileRuneAltar());
		ClientRegistry.bindTileEntitySpecialRenderer(TilePylon.class, new RenderTilePylon());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEnchanter.class, new RenderTileEnchanter());

		ShaderHelper.initShaders();
	}

	@Override
	public void setEntryToOpen(LexiconEntry entry) {
		GuiLexicon.currentOpenLexicon = new GuiLexiconEntry(entry, new GuiLexiconIndex(entry.category));
	}

	@Override
	public long getWorldElapsedTicks() {
		return Minecraft.getMinecraft().theWorld == null ? 0 : Minecraft.getMinecraft().theWorld.getTotalWorldTime();
	}

	@Override
	public void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float size, int m, boolean fake) {
		FXSparkle sparkle = new FXSparkle(world, x, y, z, size, r, g, b, m);
		sparkle.fake = sparkle.noClip = fake;
		Minecraft.getMinecraft().effectRenderer.addEffect(sparkle);
	}

	private static boolean distanceLimit = true;

	@Override
	public void setWispFXDistanceLimit(boolean limit) {
		distanceLimit = limit;
	}

	@Override
	public void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul) {
		FXWisp wisp = new FXWisp(world, x, y, z, size, r, g, b, distanceLimit, maxAgeMul);
		wisp.motionX = motionx;
		wisp.motionY = motiony;
		wisp.motionZ = motionz;

		Minecraft.getMinecraft().effectRenderer.addEffect(wisp);
	}

	@Override
	public void lightningFX(World world, Vector3 vectorStart, Vector3 vectorEnd, float ticksPerMeter, long seed, int colorOuter, int colorInner) {
		LightningHandler.spawnLightningBolt(world, vectorStart, vectorEnd, ticksPerMeter, seed, colorOuter, colorInner);
	}
}
