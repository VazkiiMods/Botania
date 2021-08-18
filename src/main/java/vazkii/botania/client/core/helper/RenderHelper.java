/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.helper;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.render.tile.RenderTilePylon;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.mixin.AccessorRenderType;

import javax.annotation.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Random;

public final class RenderHelper extends RenderType {
	private static final RenderType STAR;
	public static final RenderType RECTANGLE;
	public static final RenderType CIRCLE;
	public static final RenderType LINE_1;
	public static final RenderType LINE_1_NO_DEPTH;
	public static final RenderType LINE_4_NO_DEPTH;
	public static final RenderType LINE_5_NO_DEPTH;
	public static final RenderType LINE_8_NO_DEPTH;
	public static final RenderType SPARK;
	public static final RenderType LIGHT_RELAY;
	public static final RenderType SPINNING_CUBE;
	public static final RenderType SPINNING_CUBE_GHOST;
	public static final RenderType ICON_OVERLAY;
	public static final RenderType BABYLON_ICON;
	public static final RenderType MANA_POOL_WATER;
	public static final RenderType TERRA_PLATE;
	public static final RenderType ENCHANTER;
	public static final RenderType HALO;
	public static final RenderType MANA_PYLON_GLOW = getPylonGlow("mana_pylon_glow", RenderTilePylon.MANA_TEXTURE);
	public static final RenderType NATURA_PYLON_GLOW = getPylonGlow("natura_pylon_glow", RenderTilePylon.NATURA_TEXTURE);
	public static final RenderType GAIA_PYLON_GLOW = getPylonGlow("gaia_pylon_glow", RenderTilePylon.GAIA_TEXTURE);
	public static final RenderType MANA_PYLON_GLOW_DIRECT = getPylonGlowDirect("mana_pylon_glow_direct", RenderTilePylon.MANA_TEXTURE);
	public static final RenderType NATURA_PYLON_GLOW_DIRECT = getPylonGlowDirect("natura_pylon_glow_direct", RenderTilePylon.NATURA_TEXTURE);
	public static final RenderType GAIA_PYLON_GLOW_DIRECT = getPylonGlowDirect("gaia_pylon_glow_direct", RenderTilePylon.GAIA_TEXTURE);

	public static final RenderType ASTROLABE_PREVIEW;

	private static RenderType makeLayer(String name, VertexFormat format, VertexFormat.Mode mode,
			int bufSize, boolean hasCrumbling, boolean sortOnUpload, CompositeState glState) {
		return AccessorRenderType.create(name, format, mode, bufSize, hasCrumbling, sortOnUpload, glState);
	}

	private static RenderType makeLayer(String name, VertexFormat format, VertexFormat.Mode mode,
			int bufSize, CompositeState glState) {
		return makeLayer(name, format, mode, bufSize, false, false, glState);
	}

	static {
		// todo 1.16 update to match vanilla where necessary (alternate render targets, etc.)
		boolean useShaders = ShaderHelper.useShaders();

		RenderType.CompositeState glState = RenderType.CompositeState.builder()
				// todo 1.17.setShadeModelState(smoothShade)
				.setWriteMaskState(COLOR_WRITE)
				.setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY)
				.createCompositeState(false);
		STAR = makeLayer(LibResources.PREFIX_MOD + "star", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLES, 256, false, false, glState);

		glState = RenderType.CompositeState.builder()
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				.setOutputState(ITEM_ENTITY_TARGET)
				.setCullState(NO_CULL).createCompositeState(false);
		RECTANGLE = makeLayer(LibResources.PREFIX_MOD + "rectangle_highlight", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, true, glState);
		CIRCLE = makeLayer(LibResources.PREFIX_MOD + "circle_highlight", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLES, 256, false, false, glState);

		glState = RenderType.CompositeState.builder().setLineState(new RenderStateShard.LineStateShard(OptionalDouble.of(1))).setLayeringState(VIEW_OFFSET_Z_LAYERING).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setWriteMaskState(COLOR_WRITE).createCompositeState(false);
		LINE_1 = makeLayer(LibResources.PREFIX_MOD + "line_1", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.LINES, 128, glState);
		glState = RenderType.CompositeState.builder().setLineState(new RenderStateShard.LineStateShard(OptionalDouble.of(1))).setLayeringState(VIEW_OFFSET_Z_LAYERING).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setWriteMaskState(COLOR_WRITE).setDepthTestState(NO_DEPTH_TEST).createCompositeState(false);
		LINE_1_NO_DEPTH = makeLayer(LibResources.PREFIX_MOD + "line_1_no_depth", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.LINES, 128, glState);
		glState = RenderType.CompositeState.builder().setLineState(new RenderStateShard.LineStateShard(OptionalDouble.of(4))).setLayeringState(VIEW_OFFSET_Z_LAYERING).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setWriteMaskState(COLOR_WRITE).setDepthTestState(NO_DEPTH_TEST).createCompositeState(false);
		LINE_4_NO_DEPTH = makeLayer(LibResources.PREFIX_MOD + "line_4_no_depth", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.LINES, 128, glState);
		glState = RenderType.CompositeState.builder().setLineState(new RenderStateShard.LineStateShard(OptionalDouble.of(5))).setLayeringState(VIEW_OFFSET_Z_LAYERING).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setWriteMaskState(COLOR_WRITE).setDepthTestState(NO_DEPTH_TEST).createCompositeState(false);
		LINE_5_NO_DEPTH = makeLayer(LibResources.PREFIX_MOD + "line_5_no_depth", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.LINES, 64, glState);
		glState = RenderType.CompositeState.builder().setLineState(new RenderStateShard.LineStateShard(OptionalDouble.of(8))).setLayeringState(VIEW_OFFSET_Z_LAYERING).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setWriteMaskState(COLOR_WRITE).setDepthTestState(NO_DEPTH_TEST).createCompositeState(false);
		LINE_8_NO_DEPTH = makeLayer(LibResources.PREFIX_MOD + "line_8_no_depth", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.LINES, 64, glState);

		glState = RenderType.CompositeState.builder()
				.setTextureState(RenderStateShard.BLOCK_SHEET_MIPPED)
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				.setOutputState(ITEM_ENTITY_TARGET)
				// todo 1.17 .setAlphaState(new RenderStateShard.AlphaStateShard(0.05F))
				.setLightmapState(LIGHTMAP).createCompositeState(true);
		SPARK = makeLayer(LibResources.PREFIX_MOD + "spark", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, glState);
		RenderType lightRelay = makeLayer(LibResources.PREFIX_MOD + "light_relay", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 64, glState);
		LIGHT_RELAY = useShaders ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.HALO, null, lightRelay) : lightRelay;

		glState = RenderType.CompositeState.builder().setTextureState(NO_TEXTURE).setDiffuseLightingState(enableDiffuse).createCompositeState(false);
		SPINNING_CUBE = makeLayer(LibResources.PREFIX_MOD + "spinning_cube", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 64, glState);

		glState = RenderType.CompositeState.builder()
				.setTextureState(RenderStateShard.NO_TEXTURE)
				// todo 1.17 .setDiffuseLightingState(enableDiffuse)
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				.setOutputState(ITEM_ENTITY_TARGET)
				.createCompositeState(false);
		SPINNING_CUBE_GHOST = makeLayer(LibResources.PREFIX_MOD + "spinning_cube_ghost", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 64, glState);

		glState = RenderType.CompositeState.builder().setTextureState(BLOCK_SHEET_MIPPED)
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				.setOutputState(ITEM_ENTITY_TARGET)
				// todo 1.17 .setDiffuseLightingState(new RenderStateShard.DiffuseLightingStateShard(true))
				// todo 1.17 .setAlphaState(oneTenthAlpha)
				.setLightmapState(LIGHTMAP).createCompositeState(true);
		ICON_OVERLAY = makeLayer(LibResources.PREFIX_MOD + "icon_overlay", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 128, glState);

		RenderStateShard.TextureStateShard babylonTexture = new RenderStateShard.TextureStateShard(new ResourceLocation(LibResources.MISC_BABYLON), false, true);
		glState = RenderType.CompositeState.builder().setTextureState(babylonTexture)
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				.setOutputState(ITEM_ENTITY_TARGET)
				.setCullState(NO_CULL)
				// todo 1.17 .setShadeModelState(smoothShade)
				// todo 1.17 .setAlphaState(new RenderStateShard.AlphaStateShard(0.05F))
				.setLightmapState(LIGHTMAP).createCompositeState(true);
		RenderType babylonIcon = makeLayer(LibResources.PREFIX_MOD + "babylon", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 64, glState);
		BABYLON_ICON = useShaders ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.HALO, null, babylonIcon) : babylonIcon;

		MANA_POOL_WATER = useShaders ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.MANA_POOL, null, ICON_OVERLAY) : ICON_OVERLAY;
		TERRA_PLATE = useShaders ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.TERRA_PLATE, null, ICON_OVERLAY) : ICON_OVERLAY;
		ENCHANTER = useShaders ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.ENCHANTER_RUNE, null, ICON_OVERLAY) : ICON_OVERLAY;

		RenderStateShard.TextureStateShard haloTexture = new RenderStateShard.TextureStateShard(ItemFlightTiara.textureHalo, false, true);
		glState = RenderType.CompositeState.builder().setTextureState(haloTexture)
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				// todo 1.17 .setDiffuseLightingState(new RenderStateShard.DiffuseLightingStateShard(true))
				// todo 1.17 .setAlphaState(oneTenthAlpha)
				.setCullState(NO_CULL)
				.createCompositeState(true);
		RenderType halo = makeLayer(LibResources.PREFIX_MOD + "halo", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 64, glState);
		HALO = useShaders ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.HALO, null, halo) : halo;

		// Same as entity_translucent, with no depth test and a shader
		glState = RenderType.CompositeState.builder().setDepthTestState(new RenderStateShard.DepthTestStateShard("always", GL11.GL_ALWAYS)).setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, false)).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setDiffuseLightingState(enableDiffuse).setAlphaState(oneTenthAlpha).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(true);
		ShaderCallback cb = shader -> {
			int alpha = GlStateManager._glGetUniformLocation(shader, "alpha");
			ShaderHelper.FLOAT_BUF.position(0);
			ShaderHelper.FLOAT_BUF.put(0, 0.4F);
			RenderSystem.glUniform1(alpha, ShaderHelper.FLOAT_BUF);
		};
		RenderType astrolabePreview = makeLayer(LibResources.PREFIX_MOD + "astrolabe_preview", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, glState);
		ASTROLABE_PREVIEW = useShaders ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.ALPHA, cb, astrolabePreview) : astrolabePreview;
	}

	private RenderHelper(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int i, boolean bl, boolean bl2, Runnable runnable, Runnable runnable2) {
		super(string, vertexFormat, mode, i, bl, bl2, runnable, runnable2);
		throw new UnsupportedOperationException("Should not be instantiated");
	}

	private static RenderType getPylonGlowDirect(String name, ResourceLocation texture) {
		return getPylonGlow(name, texture, true);
	}

	private static RenderType getPylonGlow(String name, ResourceLocation texture) {
		return getPylonGlow(name, texture, false);
	}

	private static RenderType getPylonGlow(String name, ResourceLocation texture, boolean direct) {
		RenderType.CompositeState.CompositeStateBuilder glState = RenderType.CompositeState.builder()
				.setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				// todo 1.17 .setDiffuseLightingState(new RenderStateShard.DiffuseLightingStateShard(true))
				// todo 1.17 .setAlphaState(new RenderStateShard.AlphaStateShard(0))
				.setCullState(new RenderStateShard.CullStateShard(false))
				.setLightmapState(new RenderStateShard.LightmapStateShard(true));
		if (!direct) {
			glState = glState.setOutputState(RenderStateShard.ITEM_ENTITY_TARGET);
		}
		RenderType layer = makeLayer(LibResources.PREFIX_MOD + name, DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 128, glState.createCompositeState(false));
		return ShaderHelper.useShaders() ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.PYLON_GLOW, null, layer) : layer;
	}

	public static RenderType getHaloLayer(ResourceLocation texture) {
		RenderType.CompositeState glState = RenderType.CompositeState.builder()
				.setTextureState(new RenderStateShard.TextureStateShard(texture, true, false))
				.setCullState(new RenderStateShard.CullStateShard(false))
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY).createCompositeState(false);
		return makeLayer(LibResources.PREFIX_MOD + "crafting_halo", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 64, false, true, glState);
	}

	public static void drawTexturedModalRect(PoseStack ms, int x, int y, int u, int v, int width, int height) {
		GuiComponent.blit(ms, x, y, u, v, width, height, 256, 256);
	}

	public static void renderStar(PoseStack ms, MultiBufferSource buffers, int color, float xScale, float yScale, float zScale, long seed) {
		VertexConsumer buffer = buffers.getBuffer(STAR);

		float ticks = ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks;
		float semiPeriodTicks = 200;
		float f1 = Mth.abs(Mth.sin((float) Math.PI / semiPeriodTicks * ticks))
				* 0.9F + 0.1F; // shift to [0.1, 1.0]

		float f2 = f1 > 0.F ? (f1 - 0.7F) / 0.2F : 0;
		Random random = new Random(seed);

		ms.pushPose();
		ms.scale(xScale, yScale, zScale);

		for (int i = 0; i < (f1 + f1 * f1) / 2F * 90F + 30F; i++) {
			ms.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360F));
			ms.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360F));
			ms.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360F));
			ms.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360F));
			ms.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360F));
			ms.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360F + f1 * 90F));
			float f3 = random.nextFloat() * 20F + 5F + f2 * 10F;
			float f4 = random.nextFloat() * 2F + 1F + f2 * 2F;
			float r = ((color & 0xFF0000) >> 16) / 255F;
			float g = ((color & 0xFF00) >> 8) / 255F;
			float b = (color & 0xFF) / 255F;
			Matrix4f mat = ms.last().pose();
			Runnable center = () -> buffer.vertex(mat, 0, 0, 0).color(r, g, b, f1).endVertex();
			Runnable[] vertices = {
					() -> buffer.vertex(mat, -0.866F * f4, f3, -0.5F * f4).color(0, 0, 0, 0).endVertex(),
					() -> buffer.vertex(mat, 0.866F * f4, f3, -0.5F * f4).color(0, 0, 0, 0).endVertex(),
					() -> buffer.vertex(mat, 0, f3, 1F * f4).color(0, 0, 0, 0).endVertex(),
					() -> buffer.vertex(mat, -0.866F * f4, f3, -0.5F * f4).color(0, 0, 0, 0).endVertex()
			};
			triangleFan(center, vertices);
		}

		ms.popPose();
	}

	public static void triangleFan(Runnable center, Runnable... vertices) {
		triangleFan(center, Arrays.asList(vertices));
	}

	/**
	 * With a buffer in GL_TRIANGLES mode, emulates GL_TRIANGLE_FAN on the CPU.
	 * This is because batching of GL_TRIANGLE_FAN makes no sense (the vertices would bleed into one massive fan)
	 */
	public static void triangleFan(Runnable center, List<Runnable> vertices) {
		for (int i = 0; i < vertices.size() - 1; i++) {
			center.run();
			vertices.get(i).run();
			vertices.get(i + 1).run();
		}
	}

	public static void renderProgressPie(PoseStack ms, int x, int y, float progress, ItemStack stack) {
		Minecraft mc = Minecraft.getInstance();
		mc.getItemRenderer().renderAndDecorateItem(stack, x, y);

		RenderSystem.clear(GL11.GL_DEPTH_BUFFER_BIT, true);
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		RenderSystem.colorMask(false, false, false, false);
		RenderSystem.depthMask(false);
		RenderSystem.stencilFunc(GL11.GL_NEVER, 1, 0xFF);
		RenderSystem.stencilOp(GL11.GL_REPLACE, GL11.GL_KEEP, GL11.GL_KEEP);
		RenderSystem.stencilMask(0xFF);
		mc.getItemRenderer().renderAndDecorateItem(stack, x, y);

		mc.getTextureManager().bindForSetup(new ResourceLocation(LibResources.GUI_MANA_HUD));
		int r = 10;
		int centerX = x + 8;
		int centerY = y + 8;
		int degs = (int) (360 * progress);
		float a = 0.5F + 0.2F * ((float) Math.cos((double) (ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) / 10) * 0.5F + 0.5F);

		RenderSystem.disableLighting();
		RenderSystem.disableTexture();
		RenderSystem.shadeModel(GL11.GL_SMOOTH);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderSystem.colorMask(true, true, true, true);
		RenderSystem.depthMask(true);
		RenderSystem.stencilMask(0x00);
		RenderSystem.stencilFunc(GL11.GL_EQUAL, 1, 0xFF);

		Matrix4f mat = ms.last().pose();
		BufferBuilder buf = Tesselator.getInstance().getBuilder();
		buf.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		buf.vertex(mat, centerX, centerY, 0).color(0, 0.5F, 0.5F, a).endVertex();

		for (int i = degs; i > 0; i--) {
			float rad = (i - 90) / 180F * (float) Math.PI;
			buf.vertex(mat, centerX + Mth.cos(rad) * r, centerY + Mth.sin(rad) * r, 0).color(0F, 1F, 0.5F, a).endVertex();
		}

		buf.vertex(mat, centerX, centerY, 0).color(0F, 1F, 0.5F, a).endVertex();
		Tesselator.getInstance().end();

		RenderSystem.disableBlend();
		RenderSystem.enableTexture();
		RenderSystem.shadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_STENCIL_TEST);
	}

	/**
	 * @param color Must include alpha
	 */
	// [VanillaCopy] ItemRenderer.renderItem with simplifications + color support + custom model
	public static void renderItemCustomColor(LivingEntity entity, ItemStack stack, int color, PoseStack ms, MultiBufferSource buffers, int light, int overlay, @Nullable BakedModel model) {
		ms.pushPose();
		if (model == null) {
			model = Minecraft.getInstance().getItemRenderer().getModel(stack, entity.level, entity, entity.getId());
		}
		model.getTransforms().getTransform(ItemTransforms.TransformType.NONE).apply(false, ms);
		ms.translate(-0.5D, -0.5D, -0.5D);

		if (!model.isCustomRenderer() && (stack.getItem() != Items.TRIDENT)) {
			RenderType rendertype = ItemBlockRenderTypes.getRenderType(stack, true);
			VertexConsumer ivertexbuilder = ItemRenderer.getFoilBufferDirect(buffers, rendertype, true, stack.hasFoil());
			renderBakedItemModel(model, stack, color, light, overlay, ms, ivertexbuilder);
		} else {
			BlockEntityWithoutLevelRenderer.instance.renderByItem(stack, ItemTransforms.TransformType.NONE, ms, buffers, light, overlay);
		}

		ms.popPose();
	}

	public static void renderItemCustomColor(LivingEntity entity, ItemStack stack, int color, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		renderItemCustomColor(entity, stack, color, ms, buffers, light, overlay, null);
	}

	// [VanillaCopy] ItemRenderer with custom color
	private static void renderBakedItemModel(BakedModel model, ItemStack stack, int color, int light, int overlay, PoseStack ms, VertexConsumer buffer) {
		Random random = new Random();
		long i = 42L;

		for (Direction direction : Direction.values()) {
			random.setSeed(42L);
			renderBakedItemQuads(ms, buffer, color, model.getQuads((BlockState) null, direction, random), stack, light, overlay);
		}

		random.setSeed(42L);
		renderBakedItemQuads(ms, buffer, color, model.getQuads((BlockState) null, (Direction) null, random), stack, light, overlay);
	}

	// [VanillaCopy] ItemRenderer, with custom color + alpha support
	private static void renderBakedItemQuads(PoseStack ms, VertexConsumer buffer, int color, List<BakedQuad> quads, ItemStack stack, int light, int overlay) {
		PoseStack.Pose matrixstack$entry = ms.last();

		for (BakedQuad bakedquad : quads) {
			int i = color;

			float f = (float) (i >> 16 & 255) / 255.0F;
			float f1 = (float) (i >> 8 & 255) / 255.0F;
			float f2 = (float) (i & 255) / 255.0F;
			float alpha = ((color >> 24) & 0xFF) / 255.0F;
			// todo 1.16-fabric buffer.addVertexData(matrixstack$entry, bakedquad, f, f1, f2, alpha, light, overlay, true);
		}

	}
}
