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
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.renderer.*;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.render.tile.RenderTilePylon;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.mixin.AccessorRenderState;

import javax.annotation.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Random;

public final class RenderHelper {
	private static final RenderPhase.Transparency TRANSLUCENT_TRANSPARENCY = AccessorRenderState.getTranslucentTransparency();
	private static final RenderLayer STAR;
	public static final RenderLayer RECTANGLE;
	public static final RenderLayer CIRCLE;
	public static final RenderLayer LINE_1;
	public static final RenderLayer LINE_1_NO_DEPTH;
	public static final RenderLayer LINE_4_NO_DEPTH;
	public static final RenderLayer LINE_5_NO_DEPTH;
	public static final RenderLayer LINE_8_NO_DEPTH;
	public static final RenderLayer SPARK;
	public static final RenderLayer LIGHT_RELAY;
	public static final RenderLayer SPINNING_CUBE;
	public static final RenderLayer SPINNING_CUBE_GHOST;
	public static final RenderLayer ICON_OVERLAY;
	public static final RenderLayer BABYLON_ICON;
	public static final RenderLayer MANA_POOL_WATER;
	public static final RenderLayer TERRA_PLATE;
	public static final RenderLayer ENCHANTER;
	public static final RenderLayer HALO;
	public static final RenderLayer MANA_PYLON_GLOW = getPylonGlow("mana_pylon_glow", RenderTilePylon.MANA_TEXTURE);
	public static final RenderLayer NATURA_PYLON_GLOW = getPylonGlow("natura_pylon_glow", RenderTilePylon.NATURA_TEXTURE);
	public static final RenderLayer GAIA_PYLON_GLOW = getPylonGlow("gaia_pylon_glow", RenderTilePylon.GAIA_TEXTURE);
	public static final RenderLayer MANA_PYLON_GLOW_DIRECT = getPylonGlowDirect("mana_pylon_glow_direct", RenderTilePylon.MANA_TEXTURE);
	public static final RenderLayer NATURA_PYLON_GLOW_DIRECT = getPylonGlowDirect("natura_pylon_glow_direct", RenderTilePylon.NATURA_TEXTURE);
	public static final RenderLayer GAIA_PYLON_GLOW_DIRECT = getPylonGlowDirect("gaia_pylon_glow_direct", RenderTilePylon.GAIA_TEXTURE);

	public static final RenderLayer ASTROLABE_PREVIEW;
	public static final RenderLayer ENTITY_TRANSLUCENT_GOLD;

	static {
		// todo 1.16 update to match vanilla where necessary (alternate render targets, etc.)
		RenderPhase.Transparency lightningTransparency = AccessorRenderState.getLightningTransparency();
		RenderPhase.Texture mipmapBlockAtlasTexture = new RenderPhase.Texture(SpriteAtlasTexture.BLOCK_ATLAS_TEX, false, true);
		RenderPhase.Cull disableCull = new RenderPhase.Cull(false);
		RenderPhase.Layering viewOffsetZLayering = AccessorRenderState.getViewOffsetZLayer();
		RenderPhase.WriteMaskState colorMask = new RenderPhase.WriteMaskState(true, false);
		RenderLayer.ShadeModelState smoothShade = new RenderPhase.ShadeModel(true);
		RenderPhase.Lightmap enableLightmap = new RenderPhase.Lightmap(true);
		RenderPhase.Overlay enableOverlay = new RenderPhase.Overlay(true);
		RenderPhase.DiffuseLighting enableDiffuse = new RenderPhase.DiffuseLighting(true);
		RenderPhase.Alpha oneTenthAlpha = new RenderPhase.Alpha(0.004F);
		RenderPhase.DepthTest noDepth = new RenderPhase.DepthTest("always", GL11.GL_ALWAYS);
		RenderPhase.Target itemTarget = AccessorRenderState.getItemEntityTarget();
		boolean useShaders = ShaderHelper.useShaders();

		RenderLayer.MultiPhaseParameters glState = RenderLayer.MultiPhaseParameters.builder().shadeModel(smoothShade)
				.writeMaskState(colorMask)
				.transparency(lightningTransparency)
				.build(false);
		STAR = RenderLayer.of(LibResources.PREFIX_MOD + "star", VertexFormats.POSITION_COLOR, GL11.GL_TRIANGLES, 256, false, false, glState);

		glState = RenderLayer.MultiPhaseParameters.builder()
				.transparency(TRANSLUCENT_TRANSPARENCY)
				.target(itemTarget)
				.cull(disableCull).build(false);
		RECTANGLE = RenderLayer.of(LibResources.PREFIX_MOD + "rectangle_highlight", VertexFormats.POSITION_COLOR, GL11.GL_QUADS, 256, false, true, glState);
		CIRCLE = RenderLayer.of(LibResources.PREFIX_MOD + "circle_highlight", VertexFormats.POSITION_COLOR, GL11.GL_TRIANGLES, 256, false, false, glState);

		glState = RenderLayer.MultiPhaseParameters.builder().lineWidth(new RenderPhase.LineWidth(OptionalDouble.of(1))).layering(viewOffsetZLayering).transparency(TRANSLUCENT_TRANSPARENCY).writeMaskState(colorMask).build(false);
		LINE_1 = RenderLayer.of(LibResources.PREFIX_MOD + "line_1", VertexFormats.POSITION_COLOR, GL11.GL_LINES, 128, glState);
		glState = RenderLayer.MultiPhaseParameters.builder().lineWidth(new RenderPhase.LineWidth(OptionalDouble.of(1))).layering(viewOffsetZLayering).transparency(TRANSLUCENT_TRANSPARENCY).writeMaskState(colorMask).depthTest(noDepth).build(false);
		LINE_1_NO_DEPTH = RenderLayer.of(LibResources.PREFIX_MOD + "line_1_no_depth", VertexFormats.POSITION_COLOR, GL11.GL_LINES, 128, glState);
		glState = RenderLayer.MultiPhaseParameters.builder().lineWidth(new RenderPhase.LineWidth(OptionalDouble.of(4))).layering(viewOffsetZLayering).transparency(TRANSLUCENT_TRANSPARENCY).writeMaskState(colorMask).depthTest(noDepth).build(false);
		LINE_4_NO_DEPTH = RenderLayer.of(LibResources.PREFIX_MOD + "line_4_no_depth", VertexFormats.POSITION_COLOR, GL11.GL_LINES, 128, glState);
		glState = RenderLayer.MultiPhaseParameters.builder().lineWidth(new RenderPhase.LineWidth(OptionalDouble.of(5))).layering(viewOffsetZLayering).transparency(TRANSLUCENT_TRANSPARENCY).writeMaskState(colorMask).depthTest(noDepth).build(false);
		LINE_5_NO_DEPTH = RenderLayer.of(LibResources.PREFIX_MOD + "line_5_no_depth", VertexFormats.POSITION_COLOR, GL11.GL_LINES, 64, glState);
		glState = RenderLayer.MultiPhaseParameters.builder().lineWidth(new RenderPhase.LineWidth(OptionalDouble.of(8))).layering(viewOffsetZLayering).transparency(TRANSLUCENT_TRANSPARENCY).writeMaskState(colorMask).depthTest(noDepth).build(false);
		LINE_8_NO_DEPTH = RenderLayer.of(LibResources.PREFIX_MOD + "line_8_no_depth", VertexFormats.POSITION_COLOR, GL11.GL_LINES, 64, glState);

		glState = RenderLayer.MultiPhaseParameters.builder()
				.texture(mipmapBlockAtlasTexture)
				.transparency(TRANSLUCENT_TRANSPARENCY)
				.target(itemTarget)
				.alpha(new RenderPhase.Alpha(0.05F))
				.lightmap(enableLightmap).build(true);
		SPARK = RenderLayer.of(LibResources.PREFIX_MOD + "spark", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT, GL11.GL_QUADS, 256, glState);
		RenderLayer lightRelay = RenderLayer.of(LibResources.PREFIX_MOD + "light_relay", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT, GL11.GL_QUADS, 64, glState);
		LIGHT_RELAY = useShaders ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.HALO, null, lightRelay) : lightRelay;

		glState = RenderLayer.MultiPhaseParameters.builder().texture(new RenderPhase.Texture()).diffuseLighting(enableDiffuse).build(false);
		SPINNING_CUBE = RenderLayer.of(LibResources.PREFIX_MOD + "spinning_cube", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, GL11.GL_QUADS, 64, glState);

		glState = RenderLayer.MultiPhaseParameters.builder()
				.texture(new RenderPhase.Texture())
				.diffuseLighting(enableDiffuse)
				.transparency(TRANSLUCENT_TRANSPARENCY)
				.target(itemTarget)
				.build(false);
		SPINNING_CUBE_GHOST = RenderLayer.of(LibResources.PREFIX_MOD + "spinning_cube_ghost", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, GL11.GL_QUADS, 64, glState);

		glState = RenderLayer.MultiPhaseParameters.builder().texture(mipmapBlockAtlasTexture)
				.transparency(TRANSLUCENT_TRANSPARENCY)
				.target(itemTarget)
				.diffuseLighting(new RenderPhase.DiffuseLighting(true))
				.alpha(oneTenthAlpha)
				.lightmap(enableLightmap).build(true);
		ICON_OVERLAY = RenderLayer.of(LibResources.PREFIX_MOD + "icon_overlay", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT, GL11.GL_QUADS, 128, glState);

		RenderPhase.Texture babylonTexture = new RenderPhase.Texture(new Identifier(LibResources.MISC_BABYLON), false, true);
		glState = RenderLayer.MultiPhaseParameters.builder().texture(babylonTexture)
				.transparency(TRANSLUCENT_TRANSPARENCY)
				.target(itemTarget)
				.cull(disableCull)
				.shadeModel(smoothShade).build(true);
		RenderLayer babylonIcon = RenderLayer.of(LibResources.PREFIX_MOD + "babylon", VertexFormats.POSITION_COLOR_TEXTURE, GL11.GL_QUADS, 64, glState);
		BABYLON_ICON = useShaders ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.HALO, null, babylonIcon) : babylonIcon;

		MANA_POOL_WATER = useShaders ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.MANA_POOL, null, ICON_OVERLAY) : ICON_OVERLAY;
		TERRA_PLATE = useShaders ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.TERRA_PLATE, null, ICON_OVERLAY) : ICON_OVERLAY;
		ENCHANTER = useShaders ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.ENCHANTER_RUNE, null, ICON_OVERLAY) : ICON_OVERLAY;

		RenderPhase.Texture haloTexture = new RenderPhase.Texture(ItemFlightTiara.textureHalo, false, true);
		glState = RenderLayer.MultiPhaseParameters.builder().texture(haloTexture)
				.transparency(TRANSLUCENT_TRANSPARENCY)
				.diffuseLighting(new RenderPhase.DiffuseLighting(true))
				.alpha(oneTenthAlpha)
				.cull(disableCull)
				.build(true);
		RenderLayer halo = RenderLayer.of(LibResources.PREFIX_MOD + "halo", VertexFormats.POSITION_TEXTURE, GL11.GL_QUADS, 64, glState);
		HALO = useShaders ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.HALO, null, halo) : halo;

		// Same as entity_translucent, with no depth test and a shader
		glState = RenderLayer.MultiPhaseParameters.builder().depthTest(new RenderPhase.DepthTest("always", GL11.GL_ALWAYS)).texture(new RenderPhase.Texture(SpriteAtlasTexture.BLOCK_ATLAS_TEX, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).diffuseLighting(enableDiffuse).alpha(oneTenthAlpha).cull(disableCull).lightmap(enableLightmap).overlay(enableOverlay).build(true);
		ShaderCallback cb = shader -> {
			int alpha = GlStateManager.getUniformLocation(shader, "alpha");
			ShaderHelper.FLOAT_BUF.position(0);
			ShaderHelper.FLOAT_BUF.put(0, 0.4F);
			RenderSystem.glUniform1(alpha, ShaderHelper.FLOAT_BUF);
		};
		RenderLayer astrolabePreview = RenderLayer.of(LibResources.PREFIX_MOD + "astrolabe_preview", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, 7, 256, true, true, glState);
		ASTROLABE_PREVIEW = useShaders ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.ALPHA, cb, astrolabePreview) : astrolabePreview;

		glState = RenderLayer.MultiPhaseParameters.builder().texture(new RenderPhase.Texture(SpriteAtlasTexture.BLOCK_ATLAS_TEX, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).diffuseLighting(enableDiffuse).alpha(oneTenthAlpha).cull(disableCull).lightmap(enableLightmap).overlay(enableOverlay).build(true);
		RenderLayer gold = RenderLayer.of(LibResources.PREFIX_MOD + "entity_translucent_gold", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, GL11.GL_QUADS, 128, true, true, glState);
		ENTITY_TRANSLUCENT_GOLD = useShaders ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.GOLD, null, gold) : gold;
	}

	private static RenderLayer getPylonGlowDirect(String name, Identifier texture) {
		return getPylonGlow(name, texture, true);
	}

	private static RenderLayer getPylonGlow(String name, Identifier texture) {
		return getPylonGlow(name, texture, false);
	}

	private static RenderLayer getPylonGlow(String name, Identifier texture, boolean direct) {
		RenderLayer.MultiPhaseParameters.Builder glState = RenderLayer.MultiPhaseParameters.builder()
				.texture(new RenderPhase.Texture(texture, false, false))
				.transparency(TRANSLUCENT_TRANSPARENCY)
				.diffuseLighting(new RenderPhase.DiffuseLighting(true))
				.alpha(new RenderPhase.Alpha(0))
				.cull(new RenderPhase.Cull(false))
				.lightmap(new RenderPhase.Lightmap(true));
		if (!direct) {
			glState = glState.target(AccessorRenderState.getItemEntityTarget());
		}
		RenderLayer layer = RenderLayer.of(LibResources.PREFIX_MOD + name, VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, GL11.GL_QUADS, 128, glState.build(false));
		return ShaderHelper.useShaders() ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.PYLON_GLOW, null, layer) : layer;
	}

	public static RenderLayer getHaloLayer(Identifier texture) {
		RenderLayer.MultiPhaseParameters glState = RenderLayer.MultiPhaseParameters.builder()
				.texture(new RenderPhase.Texture(texture, true, false))
				.cull(new RenderPhase.Cull(false))
				.transparency(TRANSLUCENT_TRANSPARENCY).build(false);
		return RenderLayer.of(LibResources.PREFIX_MOD + "crafting_halo", VertexFormats.POSITION_COLOR_TEXTURE, GL11.GL_QUADS, 64, false, true, glState);
	}

	public static void drawTexturedModalRect(MatrixStack ms, int x, int y, int u, int v, int width, int height) {
		DrawableHelper.drawTexture(ms, x, y, u, v, width, height, 256, 256);
	}

	public static void renderStar(MatrixStack ms, VertexConsumerProvider buffers, int color, float xScale, float yScale, float zScale, long seed) {
		VertexConsumer buffer = buffers.getBuffer(STAR);

		float ticks = (ClientTickHandler.ticksInGame % 200) + ClientTickHandler.partialTicks;
		if (ticks >= 100) {
			ticks = 200 - ticks - 1;
		}

		float f1 = ticks / 200F;
		float f2 = f1 > 0.F ? (f1 - 0.7F) / 0.2F : 0;
		Random random = new Random(seed);

		ms.push();
		ms.scale(xScale, yScale, zScale);

		for (int i = 0; i < (f1 + f1 * f1) / 2F * 90F + 30F; i++) {
			ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(random.nextFloat() * 360F));
			ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(random.nextFloat() * 360F));
			ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(random.nextFloat() * 360F));
			ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(random.nextFloat() * 360F));
			ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(random.nextFloat() * 360F));
			ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(random.nextFloat() * 360F + f1 * 90F));
			float f3 = random.nextFloat() * 20F + 5F + f2 * 10F;
			float f4 = random.nextFloat() * 2F + 1F + f2 * 2F;
			float r = ((color & 0xFF0000) >> 16) / 255F;
			float g = ((color & 0xFF00) >> 8) / 255F;
			float b = (color & 0xFF) / 255F;
			Matrix4f mat = ms.peek().getModel();
			Runnable center = () -> buffer.vertex(mat, 0, 0, 0).color(r, g, b, 1F - f2).next();
			Runnable[] vertices = {
					() -> buffer.vertex(mat, -0.866F * f4, f3, -0.5F * f4).color(0, 0, 0, 0).next(),
					() -> buffer.vertex(mat, 0.866F * f4, f3, -0.5F * f4).color(0, 0, 0, 0).next(),
					() -> buffer.vertex(mat, 0, f3, 1F * f4).color(0, 0, 0, 0).next(),
					() -> buffer.vertex(mat, -0.866F * f4, f3, -0.5F * f4).color(0, 0, 0, 0).next()
			};
			triangleFan(center, vertices);
		}

		ms.pop();
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

	public static void renderProgressPie(MatrixStack ms, int x, int y, float progress, ItemStack stack) {
		MinecraftClient mc = MinecraftClient.getInstance();
		mc.getItemRenderer().renderInGuiWithOverrides(stack, x, y);

		RenderSystem.clear(GL11.GL_DEPTH_BUFFER_BIT, true);
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		RenderSystem.colorMask(false, false, false, false);
		RenderSystem.depthMask(false);
		RenderSystem.stencilFunc(GL11.GL_NEVER, 1, 0xFF);
		RenderSystem.stencilOp(GL11.GL_REPLACE, GL11.GL_KEEP, GL11.GL_KEEP);
		RenderSystem.stencilMask(0xFF);
		mc.getItemRenderer().renderInGuiWithOverrides(stack, x, y);

		mc.getTextureManager().bindTexture(new Identifier(LibResources.GUI_MANA_HUD));
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

		Matrix4f mat = ms.peek().getModel();
		BufferBuilder buf = Tessellator.getInstance().getBuffer();
		buf.begin(GL11.GL_TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
		buf.vertex(mat, centerX, centerY, 0).color(0, 0.5F, 0.5F, a).next();

		for (int i = degs; i > 0; i--) {
			float rad = (i - 90) / 180F * (float) Math.PI;
			buf.vertex(mat, centerX + MathHelper.cos(rad) * r, centerY + MathHelper.sin(rad) * r, 0).color(0F, 1F, 0.5F, a).next();
		}

		buf.vertex(mat, centerX, centerY, 0).color(0F, 1F, 0.5F, a).next();
		Tessellator.getInstance().draw();

		RenderSystem.disableBlend();
		RenderSystem.enableTexture();
		RenderSystem.shadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_STENCIL_TEST);
	}

	/**
	 * @param color Must include alpha
	 */
	// [VanillaCopy] ItemRenderer.renderItem with simplifications + color support + custom model
	public static void renderItemCustomColor(LivingEntity entity, ItemStack stack, int color, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay, @Nullable BakedModel model) {
		ms.push();
		if (model == null) {
			model = MinecraftClient.getInstance().getItemRenderer().getHeldItemModel(stack, entity.world, entity);
		}
		model = ForgeHooksClient.handleCameraTransforms(ms, model, ModelTransformation.Mode.NONE, false);
		ms.translate(-0.5D, -0.5D, -0.5D);

		if (!model.isBuiltin() && (stack.getItem() != Items.TRIDENT)) {
			RenderLayer rendertype = RenderLayers.getItemLayer(stack, true);
			VertexConsumer ivertexbuilder = ItemRenderer.method_29711(buffers, rendertype, true, stack.hasGlint());
			renderBakedItemModel(model, stack, color, light, overlay, ms, ivertexbuilder);
		} else {
			stack.getItem().getItemStackTileEntityRenderer().render(stack, ModelTransformation.Mode.NONE, ms, buffers, light, overlay);
		}

		ms.pop();
	}

	public static void renderItemCustomColor(LivingEntity entity, ItemStack stack, int color, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay) {
		renderItemCustomColor(entity, stack, color, ms, buffers, light, overlay, null);
	}

	// [VanillaCopy] ItemRenderer with custom color
	private static void renderBakedItemModel(BakedModel model, ItemStack stack, int color, int light, int overlay, MatrixStack ms, VertexConsumer buffer) {
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
	private static void renderBakedItemQuads(MatrixStack ms, VertexConsumer buffer, int color, List<BakedQuad> quads, ItemStack stack, int light, int overlay) {
		MatrixStack.Entry matrixstack$entry = ms.peek();

		for (BakedQuad bakedquad : quads) {
			int i = color;

			float f = (float) (i >> 16 & 255) / 255.0F;
			float f1 = (float) (i >> 8 & 255) / 255.0F;
			float f2 = (float) (i & 255) / 255.0F;
			float alpha = ((color >> 24) & 0xFF) / 255.0F;
			buffer.addVertexData(matrixstack$entry, bakedquad, f, f1, f2, alpha, light, overlay, true);
		}

	}

	// [VanillaCopy] Portions of ItemRenderer.renderItem
	// Does not support TEISRs
	public static void renderItemModelGold(@Nullable LivingEntity entity, ItemStack stack, ModelTransformation.Mode transform, MatrixStack ms, VertexConsumerProvider buffers, @Nullable World world, int light, int overlay) {
		ItemRenderer ir = MinecraftClient.getInstance().getItemRenderer();
		if (!stack.isEmpty()) {
			BakedModel ibakedmodel = ir.getHeldItemModel(stack, world, entity);
			ms.push();
			boolean flag = transform == ModelTransformation.Mode.GUI;
			boolean flag1 = flag || transform == ModelTransformation.Mode.GROUND || transform == ModelTransformation.Mode.FIXED;
			if (stack.getItem() == Items.TRIDENT && flag1) {
				ibakedmodel = ir.getModels().getModelManager().getModel(new ModelIdentifier("minecraft:trident#inventory"));
			}

			ibakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(ms, ibakedmodel, transform, false);
			ms.translate(-0.5D, -0.5D, -0.5D);
			if (!ibakedmodel.isBuiltin() && (stack.getItem() != Items.TRIDENT || flag1)) {
				VertexConsumer ivertexbuilder = ItemRenderer.getArmorVertexConsumer(buffers, ENTITY_TRANSLUCENT_GOLD, true, stack.hasGlint());
				ir.renderBakedItemModel(ibakedmodel, stack, light, overlay, ms, ivertexbuilder);
			}

			ms.pop();
		}
	}
}
