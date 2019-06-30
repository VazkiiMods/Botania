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

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.lexicon.multiblock.IMultiblockRenderHook;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.common.lib.LibMisc;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = LibMisc.MOD_ID)
public final class MultiblockRenderHandler {

	private MultiblockRenderHandler() {}

	private static final MultiblockBlockAccess blockAccess = new MultiblockBlockAccess();
	private static DimensionType dimension;

	public static boolean rendering = false;
	public static MultiblockSet currentMultiblock;
	public static BlockPos anchor;
	public static Direction angle;

	public static void setMultiblock(MultiblockSet set) {
		currentMultiblock = set;
		anchor = null;
		angle = Direction.SOUTH;

		Minecraft mc = Minecraft.getInstance();
		if(mc.world != null)
			dimension = mc.world.getDimension().getType();
	}

	@SubscribeEvent
	public static void onWorldRenderLast(RenderWorldLastEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if(mc.player != null && mc.objectMouseOver != null && mc.objectMouseOver.getType() != RayTraceResult.Type.BLOCK
				&& (!mc.player.isSneaking() || anchor != null)) {
			renderPlayerLook(mc.player, (BlockRayTraceResult) mc.objectMouseOver);
		}
	}

	@SubscribeEvent
	public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
		if(currentMultiblock != null && anchor == null && event.getEntityPlayer() == Minecraft.getInstance().player) {
			anchor = event.getPos();
			angle = event.getEntityPlayer().getHorizontalFacing();
			event.setCanceled(true);
			event.setCancellationResult(ActionResultType.SUCCESS);
		}
	}

	private static void renderPlayerLook(PlayerEntity player, BlockRayTraceResult src) {
		if(currentMultiblock != null && dimension == player.world.getDimension().getType()) {
			BlockPos anchorPos = anchor != null ? anchor : src.getPos();

			GlStateManager.pushMatrix();
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.disableLighting();
			rendering = true;
			Multiblock mb = anchor != null ? currentMultiblock.getForFacing(angle) : currentMultiblock.getForEntity(player);
			boolean didAny = false;

			blockAccess.update(player.world, mb, anchorPos);

			ShaderHelper.useShader(ShaderHelper.alpha, shader -> {
				int alpha = ARBShaderObjects.glGetUniformLocationARB(shader, "alpha");
				ARBShaderObjects.glUniform1fARB(alpha, 0.4F);
			});

			for(MultiblockComponent comp : mb.getComponents()) {
				if(renderComponentInWorld(player.world, mb, comp, anchorPos))
					didAny = true;
			}

			ShaderHelper.releaseShader();

			rendering = false;
			GL11.glPopAttrib();
			GlStateManager.popMatrix();

			if(!didAny) {
				setMultiblock(null);
				player.sendMessage(new TranslationTextComponent("botaniamisc.structureComplete").setStyle(new Style().setColor(TextFormatting.GREEN)));
			}
		}
	}

	private static boolean renderComponentInWorld(World world, Multiblock mb, MultiblockComponent comp, BlockPos anchorPos) {
		double renderPosX = Minecraft.getInstance().getRenderManager().renderPosX;
		double renderPosY = Minecraft.getInstance().getRenderManager().renderPosY;
		double renderPosZ = Minecraft.getInstance().getRenderManager().renderPosZ;

		BlockPos pos = comp.getRelativePosition();
		BlockPos pos_ = pos.add(anchorPos);
		if(anchor != null && comp.matches(world, pos_))
			return false;

		GlStateManager.pushMatrix();
		GlStateManager.translated(-renderPosX, -renderPosY, -renderPosZ);
		GlStateManager.disableDepthTest();
		doRenderComponent(mb, comp, pos_);
		GlStateManager.popMatrix();
		return true;
	}

	public static void renderMultiblockOnPage(Multiblock mb) {
		GlStateManager.translated(-0.5, -0.5, -0.5);
		blockAccess.update(null, mb, mb.offPos);
		for(MultiblockComponent comp : mb.getComponents()) {
			BlockPos pos = comp.getRelativePosition();
			doRenderComponent(mb, comp, pos.add(mb.offPos));
		}
	}

	private static void doRenderComponent(Multiblock mb, MultiblockComponent comp, BlockPos pos) {
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		BlockState state = comp.getBlockState();
		Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		if(state == null)
			return;
		if(IMultiblockRenderHook.renderHooks.containsKey(state.getBlock())) {
			GlStateManager.color4f(1F, 1F, 1F, 1F);
			IMultiblockRenderHook renderHook = IMultiblockRenderHook.renderHooks.get(state.getBlock());
			if(renderHook.needsTranslate(state)) {
				GlStateManager.translated(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
			}
			renderHook.renderBlockForMultiblock(blockAccess, mb, state, comp);
		}
		else {
			BlockRendererDispatcher brd = Minecraft.getInstance().getBlockRendererDispatcher();
			GlStateManager.translated(pos.getX(), pos.getY(), pos.getZ() + 1);
			GlStateManager.color4f(1, 1, 1, 1);
			brd.renderBlockBrightness(state, 1.0F);
		}
		GlStateManager.color4f(1F, 1F, 1F, 1F);
		GlStateManager.enableDepthTest();
		GlStateManager.popMatrix();
	}
}
