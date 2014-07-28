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
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.handler.BotaniaPlayerController;
import vazkii.botania.client.core.handler.BoundTileRenderer;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.core.handler.LightningHandler;
import vazkii.botania.client.core.handler.TooltipHandler;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.fx.FXSparkle;
import vazkii.botania.client.fx.FXWisp;
import vazkii.botania.client.gui.lexicon.GuiLexicon;
import vazkii.botania.client.gui.lexicon.GuiLexiconEntry;
import vazkii.botania.client.gui.lexicon.GuiLexiconIndex;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.client.render.block.RenderAltar;
import vazkii.botania.client.render.block.RenderMiniIsland;
import vazkii.botania.client.render.block.RenderPool;
import vazkii.botania.client.render.block.RenderPylon;
import vazkii.botania.client.render.block.RenderSpawnerClaw;
import vazkii.botania.client.render.block.RenderSpecialFlower;
import vazkii.botania.client.render.block.RenderSpreader;
import vazkii.botania.client.render.block.RenderTinyPotato;
import vazkii.botania.client.render.entity.RenderDoppleganger;
import vazkii.botania.client.render.entity.RenderPixie;
import vazkii.botania.client.render.item.RenderLens;
import vazkii.botania.client.render.item.RenderLexicon;
import vazkii.botania.client.render.tile.RenderTileAlfPortal;
import vazkii.botania.client.render.tile.RenderTileAltar;
import vazkii.botania.client.render.tile.RenderTileEnchanter;
import vazkii.botania.client.render.tile.RenderTileMiniIsland;
import vazkii.botania.client.render.tile.RenderTilePool;
import vazkii.botania.client.render.tile.RenderTilePylon;
import vazkii.botania.client.render.tile.RenderTileRuneAltar;
import vazkii.botania.client.render.tile.RenderTileSkullOverride;
import vazkii.botania.client.render.tile.RenderTileSpawnerClaw;
import vazkii.botania.client.render.tile.RenderTileSpreader;
import vazkii.botania.client.render.tile.RenderTileTinyPotato;
import vazkii.botania.common.block.tile.TileAlfPortal;
import vazkii.botania.common.block.tile.TileAltar;
import vazkii.botania.common.block.tile.TileEnchanter;
import vazkii.botania.common.block.tile.TileMiniIsland;
import vazkii.botania.common.block.tile.TilePylon;
import vazkii.botania.common.block.tile.TileRuneAltar;
import vazkii.botania.common.block.tile.TileSpawnerClaw;
import vazkii.botania.common.block.tile.TileTinyPotato;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.core.proxy.CommonProxy;
import vazkii.botania.common.core.version.VersionChecker;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.entity.EntityPixie;
import vazkii.botania.common.entity.EntityVineBall;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibObfuscation;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class ClientProxy extends CommonProxy {

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);

		FMLCommonHandler.instance().bus().register(new ClientTickHandler());
		MinecraftForge.EVENT_BUS.register(new HUDHandler());
		MinecraftForge.EVENT_BUS.register(new LightningHandler());
		if(ConfigHandler.boundBlockWireframe)
			MinecraftForge.EVENT_BUS.register(new BoundTileRenderer());
		MinecraftForge.EVENT_BUS.register(new TooltipHandler());

		if(ConfigHandler.versionCheckEnabled)
			new VersionChecker().init();

		initRenderers();
	}

	private void initRenderers() {
		LibRenderIDs.idAltar = RenderingRegistry.getNextAvailableRenderId();
		LibRenderIDs.idSpecialFlower = RenderingRegistry.getNextAvailableRenderId();
		LibRenderIDs.idSpreader = RenderingRegistry.getNextAvailableRenderId();
		LibRenderIDs.idPool = RenderingRegistry.getNextAvailableRenderId();
		LibRenderIDs.idPylon = RenderingRegistry.getNextAvailableRenderId();
		LibRenderIDs.idMiniIsland = RenderingRegistry.getNextAvailableRenderId();
		LibRenderIDs.idTinyPotato = RenderingRegistry.getNextAvailableRenderId();
		LibRenderIDs.idSpawnerClaw = RenderingRegistry.getNextAvailableRenderId();

		RenderingRegistry.registerBlockHandler(new RenderAltar());
		RenderingRegistry.registerBlockHandler(new RenderSpecialFlower(LibRenderIDs.idSpecialFlower));
		RenderingRegistry.registerBlockHandler(new RenderSpreader());
		RenderingRegistry.registerBlockHandler(new RenderPool());
		RenderingRegistry.registerBlockHandler(new RenderPylon());
		RenderingRegistry.registerBlockHandler(new RenderMiniIsland());
		RenderingRegistry.registerBlockHandler(new RenderTinyPotato());
		RenderingRegistry.registerBlockHandler(new RenderSpawnerClaw());

		MinecraftForgeClient.registerItemRenderer(ModItems.lens, new RenderLens());
		if(ConfigHandler.lexicon3dModel)
			MinecraftForgeClient.registerItemRenderer(ModItems.lexicon, new RenderLexicon());

		ClientRegistry.bindTileEntitySpecialRenderer(TileAltar.class, new RenderTileAltar());
		ClientRegistry.bindTileEntitySpecialRenderer(TileSpreader.class, new RenderTileSpreader());
		ClientRegistry.bindTileEntitySpecialRenderer(TilePool.class, new RenderTilePool());
		ClientRegistry.bindTileEntitySpecialRenderer(TileRuneAltar.class, new RenderTileRuneAltar());
		ClientRegistry.bindTileEntitySpecialRenderer(TilePylon.class, new RenderTilePylon());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEnchanter.class, new RenderTileEnchanter());
		ClientRegistry.bindTileEntitySpecialRenderer(TileAlfPortal.class, new RenderTileAlfPortal());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMiniIsland.class, new RenderTileMiniIsland());
		ClientRegistry.bindTileEntitySpecialRenderer(TileTinyPotato.class, new RenderTileTinyPotato());
		ClientRegistry.bindTileEntitySpecialRenderer(TileSpawnerClaw.class, new RenderTileSpawnerClaw());
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySkull.class, new RenderTileSkullOverride());
		
		RenderingRegistry.registerEntityRenderingHandler(EntityPixie.class, new RenderPixie());
		RenderingRegistry.registerEntityRenderingHandler(EntityVineBall.class, new RenderSnowball(ModItems.vineBall));
		RenderingRegistry.registerEntityRenderingHandler(EntityDoppleganger.class, new RenderDoppleganger());

		ShaderHelper.initShaders();
	}

	@Override
	public void setEntryToOpen(LexiconEntry entry) {
		GuiLexicon.currentOpenLexicon = new GuiLexiconEntry(entry, new GuiLexiconIndex(entry.category));
	}

	@Override
	public void setExtraReach(EntityLivingBase entity, float reach) {
		super.setExtraReach(entity, reach);
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.thePlayer;
		if(entity == player) {
			if(!(mc.playerController instanceof BotaniaPlayerController)) {
				GameType type = ReflectionHelper.getPrivateValue(PlayerControllerMP.class, mc.playerController, LibObfuscation.CURRENT_GAME_TYPE);
				NetHandlerPlayClient net = ReflectionHelper.getPrivateValue(PlayerControllerMP.class, mc.playerController, LibObfuscation.NET_CLIENT_HANDLER);
				BotaniaPlayerController controller = new BotaniaPlayerController(mc, net);
				controller.setGameType(type);
				mc.playerController = controller;
			}

			((BotaniaPlayerController) mc.playerController).distance = reach;
		}
	}

	@Override
	public long getWorldElapsedTicks() {
		return Minecraft.getMinecraft().theWorld == null ? 0 : Minecraft.getMinecraft().theWorld.getTotalWorldTime();
	}

	private static boolean noclipEnabled = false;

	@Override
	public void setSparkleFXNoClip(boolean noclip) {
		noclipEnabled = noclip;
	}

	@Override
	public void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float size, int m, boolean fake) {
		FXSparkle sparkle = new FXSparkle(world, x, y, z, size, r, g, b, m);
		sparkle.fake = sparkle.noClip = fake;
		if(noclipEnabled)
			sparkle.noClip = true;
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
