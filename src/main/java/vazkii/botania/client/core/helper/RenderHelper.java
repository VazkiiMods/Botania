/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.helper;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.render.tile.RenderTilePylon;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.lib.LibObfuscation;

import javax.annotation.Nullable;

import java.lang.invoke.MethodHandle;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Random;

public final class RenderHelper {
	private static final RenderState.TransparencyState TRANSLUCENT_TRANSPARENCY = getTranslucentTransparency();
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
	public static final RenderType ENTITY_TRANSLUCENT_GOLD;

	static {
		// todo 1.16 update to match vanilla where necessary (alternate render targets, etc.)
		RenderState.TransparencyState lightningTransparency = ObfuscationReflectionHelper.getPrivateValue(RenderState.class, null, "field_228512_d_");
		RenderState.TextureState mipmapBlockAtlasTexture = new RenderState.TextureState(AtlasTexture.LOCATION_BLOCKS_TEXTURE, false, true);
		RenderState.CullState disableCull = new RenderState.CullState(false);
		RenderState.LayerState viewOffsetZLayering = ObfuscationReflectionHelper.getPrivateValue(RenderState.class, null, "field_239235_M_");
		RenderState.WriteMaskState colorMask = new RenderState.WriteMaskState(true, false);
		RenderType.ShadeModelState smoothShade = new RenderState.ShadeModelState(true);
		RenderState.LightmapState enableLightmap = new RenderState.LightmapState(true);
		RenderState.OverlayState enableOverlay = new RenderState.OverlayState(true);
		RenderState.DiffuseLightingState enableDiffuse = new RenderState.DiffuseLightingState(true);
		RenderState.AlphaState oneTenthAlpha = new RenderState.AlphaState(0.004F);
		RenderState.DepthTestState noDepth = new RenderState.DepthTestState("always", GL11.GL_ALWAYS);
		RenderState.TargetState itemTarget = ObfuscationReflectionHelper.getPrivateValue(RenderState.class, null, "field_241712_U_");
		boolean useShaders = ShaderHelper.useShaders();

		RenderType.State glState = RenderType.State.getBuilder().shadeModel(smoothShade)
				.writeMask(colorMask)
				.transparency(lightningTransparency)
				.build(false);
		STAR = RenderType.makeType(LibResources.PREFIX_MOD + "star", DefaultVertexFormats.POSITION_COLOR, GL11.GL_TRIANGLES, 256, false, false, glState);

		glState = RenderType.State.getBuilder()
				.transparency(TRANSLUCENT_TRANSPARENCY)
				.target(itemTarget)
				.cull(disableCull).build(false);
		RECTANGLE = RenderType.makeType(LibResources.PREFIX_MOD + "rectangle_highlight", DefaultVertexFormats.POSITION_COLOR, GL11.GL_QUADS, 256, false, true, glState);
		CIRCLE = RenderType.makeType(LibResources.PREFIX_MOD + "circle_highlight", DefaultVertexFormats.POSITION_COLOR, GL11.GL_TRIANGLES, 256, false, false, glState);

		glState = RenderType.State.getBuilder().line(new RenderState.LineState(OptionalDouble.of(1))).layer(viewOffsetZLayering).transparency(TRANSLUCENT_TRANSPARENCY).writeMask(colorMask).build(false);
		LINE_1 = RenderType.makeType(LibResources.PREFIX_MOD + "line_1", DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 128, glState);
		glState = RenderType.State.getBuilder().line(new RenderState.LineState(OptionalDouble.of(1))).layer(viewOffsetZLayering).transparency(TRANSLUCENT_TRANSPARENCY).writeMask(colorMask).depthTest(noDepth).build(false);
		LINE_1_NO_DEPTH = RenderType.makeType(LibResources.PREFIX_MOD + "line_1_no_depth", DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 128, glState);
		glState = RenderType.State.getBuilder().line(new RenderState.LineState(OptionalDouble.of(4))).layer(viewOffsetZLayering).transparency(TRANSLUCENT_TRANSPARENCY).writeMask(colorMask).depthTest(noDepth).build(false);
		LINE_4_NO_DEPTH = RenderType.makeType(LibResources.PREFIX_MOD + "line_4_no_depth", DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 128, glState);
		glState = RenderType.State.getBuilder().line(new RenderState.LineState(OptionalDouble.of(5))).layer(viewOffsetZLayering).transparency(TRANSLUCENT_TRANSPARENCY).writeMask(colorMask).depthTest(noDepth).build(false);
		LINE_5_NO_DEPTH = RenderType.makeType(LibResources.PREFIX_MOD + "line_5_no_depth", DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 64, glState);
		glState = RenderType.State.getBuilder().line(new RenderState.LineState(OptionalDouble.of(8))).layer(viewOffsetZLayering).transparency(TRANSLUCENT_TRANSPARENCY).writeMask(colorMask).depthTest(noDepth).build(false);
		LINE_8_NO_DEPTH = RenderType.makeType(LibResources.PREFIX_MOD + "line_8_no_depth", DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 64, glState);

		glState = RenderType.State.getBuilder()
				.texture(mipmapBlockAtlasTexture)
				.transparency(TRANSLUCENT_TRANSPARENCY)
				.target(itemTarget)
				.alpha(new RenderState.AlphaState(0.05F))
				.lightmap(enableLightmap).build(true);
		SPARK = RenderType.makeType(LibResources.PREFIX_MOD + "spark", DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP, GL11.GL_QUADS, 256, glState);
		RenderType lightRelay = RenderType.makeType(LibResources.PREFIX_MOD + "light_relay", DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP, GL11.GL_QUADS, 64, glState);
		LIGHT_RELAY = useShaders ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.HALO, null, lightRelay) : lightRelay;

		glState = RenderType.State.getBuilder().texture(new RenderState.TextureState()).diffuseLighting(enableDiffuse).build(false);
		SPINNING_CUBE = RenderType.makeType(LibResources.PREFIX_MOD + "spinning_cube", DefaultVertexFormats.ENTITY, GL11.GL_QUADS, 64, glState);

		glState = RenderType.State.getBuilder().texture(new RenderState.TextureState()).diffuseLighting(enableDiffuse).transparency(TRANSLUCENT_TRANSPARENCY).build(false);
		SPINNING_CUBE_GHOST = RenderType.makeType(LibResources.PREFIX_MOD + "spinning_cube_ghost", DefaultVertexFormats.ENTITY, GL11.GL_QUADS, 64, glState);

		glState = RenderType.State.getBuilder().texture(mipmapBlockAtlasTexture)
				.transparency(TRANSLUCENT_TRANSPARENCY)
				.target(itemTarget)
				.diffuseLighting(new RenderState.DiffuseLightingState(true))
				.alpha(oneTenthAlpha)
				.lightmap(enableLightmap).build(true);
		ICON_OVERLAY = RenderType.makeType(LibResources.PREFIX_MOD + "icon_overlay", DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP, GL11.GL_QUADS, 128, glState);

		RenderState.TextureState babylonTexture = new RenderState.TextureState(new ResourceLocation(LibResources.MISC_BABYLON), false, true);
		glState = RenderType.State.getBuilder().texture(babylonTexture)
				.transparency(TRANSLUCENT_TRANSPARENCY)
				.target(itemTarget)
				.cull(disableCull)
				.shadeModel(smoothShade).build(true);
		RenderType babylonIcon = RenderType.makeType(LibResources.PREFIX_MOD + "babylon", DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS, 64, glState);
		BABYLON_ICON = useShaders ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.HALO, null, babylonIcon) : babylonIcon;

		MANA_POOL_WATER = useShaders ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.MANA_POOL, null, ICON_OVERLAY) : ICON_OVERLAY;
		TERRA_PLATE = useShaders ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.TERRA_PLATE, null, ICON_OVERLAY) : ICON_OVERLAY;
		ENCHANTER = useShaders ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.ENCHANTER_RUNE, null, ICON_OVERLAY) : ICON_OVERLAY;

		RenderState.TextureState haloTexture = new RenderState.TextureState(ItemFlightTiara.textureHalo, false, true);
		glState = RenderType.State.getBuilder().texture(haloTexture)
				.transparency(TRANSLUCENT_TRANSPARENCY)
				.diffuseLighting(new RenderState.DiffuseLightingState(true))
				.alpha(oneTenthAlpha)
				.cull(disableCull)
				.build(true);
		RenderType halo = RenderType.makeType(LibResources.PREFIX_MOD + "halo", DefaultVertexFormats.POSITION_TEX, GL11.GL_QUADS, 64, glState);
		HALO = useShaders ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.HALO, null, halo) : halo;

		// Same as entity_translucent, with no depth test and a shader
		glState = RenderType.State.getBuilder().depthTest(new RenderState.DepthTestState("always", GL11.GL_ALWAYS)).texture(new RenderState.TextureState(AtlasTexture.LOCATION_BLOCKS_TEXTURE, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).diffuseLighting(enableDiffuse).alpha(oneTenthAlpha).cull(disableCull).lightmap(enableLightmap).overlay(enableOverlay).build(true);
		ShaderCallback cb = shader -> {
			int alpha = GlStateManager.getUniformLocation(shader, "alpha");
			ShaderHelper.FLOAT_BUF.position(0);
			ShaderHelper.FLOAT_BUF.put(0, 0.4F);
			RenderSystem.glUniform1(alpha, ShaderHelper.FLOAT_BUF);
		};
		RenderType astrolabePreview = RenderType.makeType(LibResources.PREFIX_MOD + "astrolabe_preview", DefaultVertexFormats.ENTITY, 7, 256, true, true, glState);
		ASTROLABE_PREVIEW = useShaders ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.ALPHA, cb, astrolabePreview) : astrolabePreview;

		glState = RenderType.State.getBuilder().texture(new RenderState.TextureState(AtlasTexture.LOCATION_BLOCKS_TEXTURE, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).diffuseLighting(enableDiffuse).alpha(oneTenthAlpha).cull(disableCull).lightmap(enableLightmap).overlay(enableOverlay).build(true);
		RenderType gold = RenderType.makeType(LibResources.PREFIX_MOD + "entity_translucent_gold", DefaultVertexFormats.ENTITY, GL11.GL_QUADS, 128, true, true, glState);
		ENTITY_TRANSLUCENT_GOLD = useShaders ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.GOLD, null, gold) : gold;
	}

	private static RenderState.TransparencyState getTranslucentTransparency() {
		return ObfuscationReflectionHelper.getPrivateValue(RenderState.class, null, "field_228515_g_");
	}

	private static RenderType getPylonGlowDirect(String name, ResourceLocation texture) {
		return getPylonGlow(name, texture, true);
	}

	private static RenderType getPylonGlow(String name, ResourceLocation texture) {
		return getPylonGlow(name, texture, false);
	}

	private static RenderType getPylonGlow(String name, ResourceLocation texture, boolean direct) {
		RenderType.State.Builder glState = RenderType.State.getBuilder()
				.texture(new RenderState.TextureState(texture, false, false))
				.transparency(TRANSLUCENT_TRANSPARENCY)
				.diffuseLighting(new RenderState.DiffuseLightingState(true))
				.alpha(new RenderState.AlphaState(0))
				.cull(new RenderState.CullState(false))
				.lightmap(new RenderState.LightmapState(true));
		if (!direct) {
			RenderState.TargetState itemTarget = ObfuscationReflectionHelper.getPrivateValue(RenderState.class, null, "field_241712_U_");
			glState = glState.target(itemTarget);
		}
		RenderType layer = RenderType.makeType(LibResources.PREFIX_MOD + name, DefaultVertexFormats.ENTITY, GL11.GL_QUADS, 128, glState.build(false));
		return ShaderHelper.useShaders() ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.PYLON_GLOW, null, layer) : layer;
	}

	public static RenderType getHaloLayer(ResourceLocation texture) {
		RenderType.State glState = RenderType.State.getBuilder()
				.texture(new RenderState.TextureState(texture, true, false))
				.cull(new RenderState.CullState(false))
				.transparency(TRANSLUCENT_TRANSPARENCY).build(false);
		return RenderType.makeType(LibResources.PREFIX_MOD + "crafting_halo", DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS, 64, false, true, glState);
	}

	public static void drawTexturedModalRect(MatrixStack ms, int x, int y, int u, int v, int width, int height) {
		AbstractGui.blit(ms, x, y, u, v, width, height, 256, 256);
	}

	public static void renderStar(MatrixStack ms, IRenderTypeBuffer buffers, int color, float xScale, float yScale, float zScale, long seed) {
		IVertexBuilder buffer = buffers.getBuffer(STAR);

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
			ms.rotate(Vector3f.XP.rotationDegrees(random.nextFloat() * 360F));
			ms.rotate(Vector3f.YP.rotationDegrees(random.nextFloat() * 360F));
			ms.rotate(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360F));
			ms.rotate(Vector3f.XP.rotationDegrees(random.nextFloat() * 360F));
			ms.rotate(Vector3f.YP.rotationDegrees(random.nextFloat() * 360F));
			ms.rotate(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360F + f1 * 90F));
			float f3 = random.nextFloat() * 20F + 5F + f2 * 10F;
			float f4 = random.nextFloat() * 2F + 1F + f2 * 2F;
			float r = ((color & 0xFF0000) >> 16) / 255F;
			float g = ((color & 0xFF00) >> 8) / 255F;
			float b = (color & 0xFF) / 255F;
			Matrix4f mat = ms.getLast().getMatrix();
			Runnable center = () -> buffer.pos(mat, 0, 0, 0).color(r, g, b, 1F - f2).endVertex();
			Runnable[] vertices = {
					() -> buffer.pos(mat, -0.866F * f4, f3, -0.5F * f4).color(0, 0, 0, 0).endVertex(),
					() -> buffer.pos(mat, 0.866F * f4, f3, -0.5F * f4).color(0, 0, 0, 0).endVertex(),
					() -> buffer.pos(mat, 0, f3, 1F * f4).color(0, 0, 0, 0).endVertex(),
					() -> buffer.pos(mat, -0.866F * f4, f3, -0.5F * f4).color(0, 0, 0, 0).endVertex()
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
		Minecraft mc = Minecraft.getInstance();
		mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, x, y);

		RenderSystem.clear(GL11.GL_DEPTH_BUFFER_BIT, true);
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		RenderSystem.colorMask(false, false, false, false);
		RenderSystem.depthMask(false);
		RenderSystem.stencilFunc(GL11.GL_NEVER, 1, 0xFF);
		RenderSystem.stencilOp(GL11.GL_REPLACE, GL11.GL_KEEP, GL11.GL_KEEP);
		RenderSystem.stencilMask(0xFF);
		mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, x, y);

		mc.textureManager.bindTexture(new ResourceLocation(LibResources.GUI_MANA_HUD));
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

		Matrix4f mat = ms.getLast().getMatrix();
		BufferBuilder buf = Tessellator.getInstance().getBuffer();
		buf.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
		buf.pos(mat, centerX, centerY, 0).color(0, 0.5F, 0.5F, a).endVertex();

		for (int i = degs; i > 0; i--) {
			float rad = (i - 90) / 180F * (float) Math.PI;
			buf.pos(mat, centerX + MathHelper.cos(rad) * r, centerY + MathHelper.sin(rad) * r, 0).color(0F, 1F, 0.5F, a).endVertex();
		}

		buf.pos(mat, centerX, centerY, 0).color(0F, 1F, 0.5F, a).endVertex();
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
	public static void renderItemCustomColor(LivingEntity entity, ItemStack stack, int color, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay, @Nullable IBakedModel model) {
		ms.push();
		if (model == null) {
			model = Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(stack, entity.world, entity);
		}
		model = ForgeHooksClient.handleCameraTransforms(ms, model, ItemCameraTransforms.TransformType.NONE, false);
		ms.translate(-0.5D, -0.5D, -0.5D);

		if (!model.isBuiltInRenderer() && (stack.getItem() != Items.TRIDENT)) {
			RenderType rendertype = RenderTypeLookup.func_239219_a_(stack, true);
			IVertexBuilder ivertexbuilder = ItemRenderer.func_239391_c_(buffers, rendertype, true, stack.hasEffect());
			renderBakedItemModel(model, stack, color, light, overlay, ms, ivertexbuilder);
		} else {
			stack.getItem().getItemStackTileEntityRenderer().func_239207_a_(stack, ItemCameraTransforms.TransformType.NONE, ms, buffers, light, overlay);
		}

		ms.pop();
	}

	public static void renderItemCustomColor(LivingEntity entity, ItemStack stack, int color, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		renderItemCustomColor(entity, stack, color, ms, buffers, light, overlay, null);
	}

	// [VanillaCopy] ItemRenderer with custom color
	private static void renderBakedItemModel(IBakedModel model, ItemStack stack, int color, int light, int overlay, MatrixStack ms, IVertexBuilder buffer) {
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
	private static void renderBakedItemQuads(MatrixStack ms, IVertexBuilder buffer, int color, List<BakedQuad> quads, ItemStack stack, int light, int overlay) {
		MatrixStack.Entry matrixstack$entry = ms.getLast();

		for (BakedQuad bakedquad : quads) {
			int i = color;

			float f = (float) (i >> 16 & 255) / 255.0F;
			float f1 = (float) (i >> 8 & 255) / 255.0F;
			float f2 = (float) (i & 255) / 255.0F;
			float alpha = ((color >> 24) & 0xFF) / 255.0F;
			buffer.addVertexData(matrixstack$entry, bakedquad, f, f1, f2, alpha, light, overlay, true);
		}

	}

	private static final MethodHandle RENDER_BAKED_ITEM_MODEL = LibObfuscation.getMethod(ItemRenderer.class, "func_229114_a_", IBakedModel.class, ItemStack.class, int.class, int.class, MatrixStack.class, IVertexBuilder.class);

	// [VanillaCopy] Portions of ItemRenderer.renderItem
	// Does not support TEISRs
	public static void renderItemModelGold(@Nullable LivingEntity entity, ItemStack stack, ItemCameraTransforms.TransformType transform, MatrixStack ms, IRenderTypeBuffer buffers, @Nullable World world, int light, int overlay) {
		ItemRenderer ir = Minecraft.getInstance().getItemRenderer();
		if (!stack.isEmpty()) {
			IBakedModel ibakedmodel = ir.getItemModelWithOverrides(stack, world, entity);
			ms.push();
			boolean flag = transform == ItemCameraTransforms.TransformType.GUI;
			boolean flag1 = flag || transform == ItemCameraTransforms.TransformType.GROUND || transform == ItemCameraTransforms.TransformType.FIXED;
			if (stack.getItem() == Items.TRIDENT && flag1) {
				ibakedmodel = ir.getItemModelMesher().getModelManager().getModel(new ModelResourceLocation("minecraft:trident#inventory"));
			}

			ibakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(ms, ibakedmodel, transform, false);
			ms.translate(-0.5D, -0.5D, -0.5D);
			if (!ibakedmodel.isBuiltInRenderer() && (stack.getItem() != Items.TRIDENT || flag1)) {
				IVertexBuilder ivertexbuilder = ItemRenderer.getBuffer(buffers, ENTITY_TRANSLUCENT_GOLD, true, stack.hasEffect());
				try {
					RENDER_BAKED_ITEM_MODEL.invokeExact(ir, ibakedmodel, stack, light, overlay, ms, ivertexbuilder);
				} catch (Throwable ex) {
					throw new RuntimeException("Error calling renderBakedItemModel", ex);
				}
			}

			ms.pop();
		}
	}
}
