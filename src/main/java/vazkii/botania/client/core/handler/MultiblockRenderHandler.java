/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 27, 2015, 7:28:29 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.lexicon.multiblock.IMultiblockRenderHook;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.common.block.ModBlocks;

import java.util.List;

public final class MultiblockRenderHandler {

	private static MultiblockBlockAccess blockAccess = new MultiblockBlockAccess();
	private static int dimension;

	public static boolean rendering = false;
	public static MultiblockSet currentMultiblock;
	public static BlockPos anchor;
	public static EnumFacing angle;

	static {
		// todo 1.8.8 temporary shim, because cannot renderBlockBrightness directly, see MinecraftForge issue 2353
		IMultiblockRenderHook.renderHooks.put(ModBlocks.pylon, new IMultiblockRenderHook() {
			@Override
			public void renderBlockForMultiblock(IBlockAccess world, Multiblock mb, IBlockState state, MultiblockComponent comp, float alpha) {
				// Steal itemstack model since it has the proper group visibilities configured
				ItemStack stack = new ItemStack(ModBlocks.pylon, 1, state.getBlock().getMetaFromState(state));
				IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
				GlStateManager.scale(0.65F, 0.65, 0.65F);
				GlStateManager.translate(0.5F, -0.75F, 0.5F);
				Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightness(model, state, 1.0F, false);
			}

			@Override
			public boolean needsTranslate(IBlockState state) {
				return true;
			}
		});

		// TODO also a temporary shim for same reason as above
		IMultiblockRenderHook.renderHooks.put(ModBlocks.pool, new IMultiblockRenderHook() {
			@Override
			public void renderBlockForMultiblock(IBlockAccess world, Multiblock mb, IBlockState state, MultiblockComponent comp, float alpha) {
				GlStateManager.translate(-0.5F, -0.5F, 0.5F);
				IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(ModBlocks.pool.getDefaultState());
				Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightness(model, ModBlocks.pool.getDefaultState(), 1.0F, false);
			}

			@Override
			public boolean needsTranslate(IBlockState state) {
				return true;
			}
		});
	}

	public static void setMultiblock(MultiblockSet set) {
		currentMultiblock = set;
		anchor = null;
		angle = EnumFacing.SOUTH;

		Minecraft mc = Minecraft.getMinecraft();
		if(mc.theWorld != null)
			dimension = mc.theWorld.provider.getDimension();
	}

	@SubscribeEvent
	public void onWorldRenderLast(RenderWorldLastEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if(mc.thePlayer != null && mc.objectMouseOver != null && mc.objectMouseOver.getBlockPos() != null && (!mc.thePlayer.isSneaking() || anchor != null)) {
			renderPlayerLook(mc.thePlayer, mc.objectMouseOver);
		}
	}

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(currentMultiblock != null && anchor == null && event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getEntityPlayer() == Minecraft.getMinecraft().thePlayer) {
			anchor = event.getPos();
			angle = event.getEntityPlayer().getHorizontalFacing();
			event.setCanceled(true);
		}
	}

	private void renderPlayerLook(EntityPlayer player, RayTraceResult src) {
		if(currentMultiblock != null && dimension == player.worldObj.provider.getDimension()) {
			BlockPos anchorPos = anchor != null ? anchor : src.getBlockPos();

			GlStateManager.pushMatrix();
			GL11.glPushAttrib(GL11.GL_LIGHTING);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.disableLighting();
			rendering = true;
			Multiblock mb = anchor != null ? currentMultiblock.getForFacing(angle) : currentMultiblock.getForEntity(player);
			boolean didAny = false;

			blockAccess.update(player.worldObj, mb, anchorPos);

			for(MultiblockComponent comp : mb.getComponents())
				if(renderComponentInWorld(player.worldObj, mb, comp, anchorPos))
					didAny = true;
			rendering = false;
			GL11.glPopAttrib();
			GlStateManager.popMatrix();
			
			if(!didAny) {
				setMultiblock(null);
				player.addChatComponentMessage(new TextComponentTranslation("botaniamisc.structureComplete").setStyle(new Style().setColor(TextFormatting.GREEN)));
			}
		}
	}

	private boolean renderComponentInWorld(World world, Multiblock mb, MultiblockComponent comp, BlockPos anchorPos) {
		double renderPosX, renderPosY, renderPosZ;

		try {
			renderPosX = (double) ClientMethodHandles.renderPosX_getter.invokeExact(Minecraft.getMinecraft().getRenderManager());
			renderPosY = (double) ClientMethodHandles.renderPosY_getter.invokeExact(Minecraft.getMinecraft().getRenderManager());
			renderPosZ = (double) ClientMethodHandles.renderPosZ_getter.invokeExact(Minecraft.getMinecraft().getRenderManager());
		} catch (Throwable t) {
			return true;
		}

		BlockPos pos = comp.getRelativePosition();
		BlockPos pos_ = pos.add(anchorPos);
		if(anchor != null && comp.matches(world, pos_))
			return false;

		GlStateManager.pushMatrix();
		GlStateManager.translate(-renderPosX, -renderPosY, -renderPosZ);
		GlStateManager.disableDepth();
		doRenderComponent(mb, comp, pos_, 0.4F);
		GlStateManager.popMatrix();
		return true;
	}

	public static void renderMultiblockOnPage(Multiblock mb) {
		GlStateManager.translate(-0.5, -0.5, -0.5);
		blockAccess.update(null, mb, mb.offPos);
		for(MultiblockComponent comp : mb.getComponents()) {
			BlockPos pos = comp.getRelativePosition();
			doRenderComponent(mb, comp, pos.add(mb.offPos), 1);
		}
	}

	private static void doRenderComponent(Multiblock mb, MultiblockComponent comp, BlockPos pos, float alpha) {
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		IBlockState state = comp.getBlockState();
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		if(state == null)
			return;
		if(IMultiblockRenderHook.renderHooks.containsKey(state.getBlock())) {
			GlStateManager.color(1F, 1F, 1F, alpha);
			IMultiblockRenderHook renderHook = IMultiblockRenderHook.renderHooks.get(state.getBlock());
			if(renderHook.needsTranslate(state)) {
				GlStateManager.translate(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
			}
			renderHook.renderBlockForMultiblock(blockAccess, mb, state, comp, alpha);
		}
		else {
			BlockRendererDispatcher brd = Minecraft.getMinecraft().getBlockRendererDispatcher();
			GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ() + 1); // todo 1.8 bandaid for things rendering one block off...why?
			GlStateManager.color(1, 1, 1, alpha);
			renderModelBrightness(brd.getModelForState(state), state, 1, true, alpha); // todo 1.9 make this a shader so we don't have to copypasta 3 methods?
		}
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.enableDepth();
		GlStateManager.popMatrix();
	}

	// Copy of BlockModelRenderer.renderModelBrightness + alpha arg
	private static void renderModelBrightness(IBakedModel model, IBlockState p_178266_2_, float brightness, boolean p_178266_4_, float alpha)
	{
		GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
		int i = Minecraft.getMinecraft().getBlockColors().colorMultiplier(p_178266_2_, null, null, 0);

		if (EntityRenderer.anaglyphEnable)
		{
			i = TextureUtil.anaglyphColor(i);
		}

		float f = (float)(i >> 16 & 255) / 255.0F;
		float f1 = (float)(i >> 8 & 255) / 255.0F;
		float f2 = (float)(i & 255) / 255.0F;

		if (!p_178266_4_)
		{
			GlStateManager.color(brightness, brightness, brightness, 1.0F);
		}

		renderModelBrightnessColor(p_178266_2_, model, brightness, f, f1, f2, alpha);
	}

	// Copy of BlockModelRenderer.renderModelBrightnessColor + alpha arg
	private static void renderModelBrightnessColor(IBlockState p_187495_1_, IBakedModel p_187495_2_, float p_187495_3_, float p_187495_4_, float p_187495_5_, float p_187495_6_, float alpha) {
		for (EnumFacing enumfacing : EnumFacing.values())
		{
			renderModelBrightnessColorQuads(p_187495_3_, p_187495_4_, p_187495_5_, p_187495_6_, p_187495_2_.getQuads(p_187495_1_, enumfacing, 0L), alpha);
		}

		renderModelBrightnessColorQuads(p_187495_3_, p_187495_4_, p_187495_5_, p_187495_6_, p_187495_2_.getQuads(p_187495_1_, (EnumFacing)null, 0L), alpha);
	}

	// Copy of BlockModelRenderer.renderModelBrightnessColorQuads + alpha arg
	private static void renderModelBrightnessColorQuads(float brightness, float red, float green, float blue, List<BakedQuad> listQuads, float alpha)
	{
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		int i = 0;

		for (int j = listQuads.size(); i < j; ++i)
		{
			BakedQuad bakedquad = (BakedQuad)listQuads.get(i);
			vertexbuffer.begin(7, DefaultVertexFormats.ITEM);
			vertexbuffer.addVertexData(bakedquad.getVertexData());

			// Botania - alpha
			int a = ((int) (255 * alpha));
			int r = ((int) (255 * brightness * (bakedquad.hasTintIndex() ? red : 1)));
			int g = ((int) (255 * brightness * (bakedquad.hasTintIndex() ? green : 1)));
			int b = ((int) (255 * brightness * (bakedquad.hasTintIndex() ? blue : 1)));
			int argb = a << 24 | r << 16 | g << 8 | b;
			vertexbuffer.putColor4(argb);

			Vec3i vec3i = bakedquad.getFace().getDirectionVec();
			vertexbuffer.putNormal((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
			tessellator.draw();
		}
	}
}
