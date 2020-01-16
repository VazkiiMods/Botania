/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 19, 2014, 5:40:38 PM (GMT)]
 */
package vazkii.botania.client.core.helper;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Random;

public final class RenderHelper {
	private static final RenderType STAR;
	public static final RenderType RECTANGLE;
	public static final RenderType CIRCLE;
	public static final RenderType LINE_1;
	public static final RenderType LINE_4;
	public static final RenderType LINE_5;
	public static final RenderType LINE_8;
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

	// todo 1.15 AT's for necessary fields
	static {
		RenderState.TransparencyState lightningTransparency = ObfuscationReflectionHelper.getPrivateValue(RenderState.class, null, "field_228512_d_");
		RenderState.TransparencyState translucentTransparency = ObfuscationReflectionHelper.getPrivateValue(RenderState.class, null, "field_228515_g_");
		RenderState.TextureState mipmapBlockAtlasTexture = ObfuscationReflectionHelper.getPrivateValue(RenderState.class, null, "field_228521_m_");
		RenderState.CullState disableCull = ObfuscationReflectionHelper.getPrivateValue(RenderState.class, null, "field_228491_A_");
		RenderState.LayerState projectionLayering = ObfuscationReflectionHelper.getPrivateValue(RenderState.class, null, "field_228500_J_");
		RenderState.WriteMaskState colorMask = ObfuscationReflectionHelper.getPrivateValue(RenderState.class, null, "field_228496_F_");
		RenderType.ShadeModelState smoothShade = new RenderState.ShadeModelState(true);
		RenderState.LightmapState enableLightmap = new RenderState.LightmapState(true);

		RenderType.State glState = RenderType.State.builder().shadeModel(smoothShade)
				.writeMaskState(colorMask)
				.transparency(lightningTransparency)
				.build(false);
		STAR = RenderType.of(LibResources.PREFIX_MOD + "star", DefaultVertexFormats.POSITION_COLOR, GL11.GL_TRIANGLES, 256, false, true, glState);

		glState = RenderType.State.builder().transparency(translucentTransparency).cull(disableCull).build(false);
		RECTANGLE = RenderType.of(LibResources.PREFIX_MOD + "rectangle_highlight", DefaultVertexFormats.POSITION_COLOR, GL11.GL_QUADS, 256, false, true, glState);
		CIRCLE = RenderType.of(LibResources.PREFIX_MOD + "circle_highlight", DefaultVertexFormats.POSITION_COLOR, GL11.GL_TRIANGLES, 256, false, true, glState);

		glState = RenderType.State.builder().lineWidth(new RenderState.LineState(OptionalDouble.of(1))).layering(projectionLayering).transparency(translucentTransparency).writeMaskState(colorMask).build(false);
		LINE_1 = RenderType.of(LibResources.PREFIX_MOD + "line_1", DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 128, glState);
		glState = RenderType.State.builder().lineWidth(new RenderState.LineState(OptionalDouble.of(4))).layering(projectionLayering).transparency(translucentTransparency).writeMaskState(colorMask).build(false);
		LINE_4 = RenderType.of(LibResources.PREFIX_MOD + "line_4", DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 128, glState);
		glState = RenderType.State.builder().lineWidth(new RenderState.LineState(OptionalDouble.of(5))).layering(projectionLayering).transparency(translucentTransparency).writeMaskState(colorMask).build(false);
		LINE_5 = RenderType.of(LibResources.PREFIX_MOD + "line_5", DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 64, glState);
		glState = RenderType.State.builder().lineWidth(new RenderState.LineState(OptionalDouble.of(8))).layering(projectionLayering).transparency(translucentTransparency).writeMaskState(colorMask).build(false);
		LINE_8 = RenderType.of(LibResources.PREFIX_MOD + "line_8", DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 64, glState);

		glState = RenderType.State.builder().texture(mipmapBlockAtlasTexture).transparency(translucentTransparency).alpha(new RenderState.AlphaState(0.05F)).lightmap(enableLightmap).build(true);
		// todo 1.15: need normals?
		SPARK = RenderType.of(LibResources.PREFIX_MOD + "spark", DefaultVertexFormats.POSITION_COLOR_TEXTURE_LIGHT, GL11.GL_QUADS, 256, glState);
		LIGHT_RELAY = new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.HALO, null, RenderType.of(LibResources.PREFIX_MOD + "light_relay", DefaultVertexFormats.POSITION_COLOR_TEXTURE_LIGHT, GL11.GL_QUADS, 64, glState));

		glState = RenderType.State.builder().texture(new RenderState.TextureState()).build(false);
		SPINNING_CUBE = RenderType.of(LibResources.PREFIX_MOD + "spinning_cube", DefaultVertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, GL11.GL_QUADS, 64, glState);

		glState = RenderType.State.builder().texture(new RenderState.TextureState()).transparency(translucentTransparency).build(false);
		SPINNING_CUBE_GHOST = RenderType.of(LibResources.PREFIX_MOD + "spinning_cube_ghost", DefaultVertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, GL11.GL_QUADS, 64, glState);

		glState = RenderType.State.builder().texture(mipmapBlockAtlasTexture).transparency(translucentTransparency).lightmap(enableLightmap).build(true);
		ICON_OVERLAY = RenderType.of(LibResources.PREFIX_MOD + "icon_overlay", DefaultVertexFormats.POSITION_COLOR_TEXTURE_LIGHT, GL11.GL_QUADS, 128, glState);

		RenderState.TextureState babylonTexture = new RenderState.TextureState(new ResourceLocation(LibResources.MISC_BABYLON), false, true);
		glState = RenderType.State.builder().texture(babylonTexture).transparency(translucentTransparency).cull(disableCull).shadeModel(smoothShade).build(true);
		BABYLON_ICON = new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.HALO, null, RenderType.of(LibResources.PREFIX_MOD + "babylon", DefaultVertexFormats.POSITION_COLOR_TEXTURE, GL11.GL_QUADS, 64, glState));

		MANA_POOL_WATER = new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.MANA_POOL, null, ICON_OVERLAY);
		TERRA_PLATE = new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.TERRA_PLATE, null, ICON_OVERLAY);
		ENCHANTER = new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.ENCHANTER_RUNE, null, ICON_OVERLAY);

		RenderState.TextureState haloTexture = new RenderState.TextureState(ItemFlightTiara.textureHalo, false, true);
		glState = RenderType.State.builder().texture(haloTexture).transparency(translucentTransparency).cull(disableCull).shadeModel(smoothShade).build(true);
		HALO = new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.HALO, null, RenderType.of(LibResources.PREFIX_MOD + "halo", DefaultVertexFormats.POSITION_TEX, GL11.GL_QUADS, 64, glState));
	}

	public static void drawTexturedModalRect(int par1, int par2, float z, int par3, int par4, int par5, int par6) {
		drawTexturedModalRect(par1, par2, z, par3, par4, par5, par6, 0.00390625F, 0.00390625F);
	}

	public static void drawTexturedModalRect(int par1, int par2, float z, int par3, int par4, int par5, int par6, float f, float f1) {
		Tessellator tessellator = Tessellator.getInstance();
		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		tessellator.getBuffer().vertex(par1, par2 + par6, z).texture(par3 * f, (par4 + par6) * f1).endVertex();
		tessellator.getBuffer().vertex(par1 + par5, par2 + par6, z).texture((par3 + par5) * f, (par4 + par6) * f1).endVertex();
		tessellator.getBuffer().vertex(par1 + par5, par2, z).texture((par3 + par5) * f, par4 * f1).endVertex();
		tessellator.getBuffer().vertex(par1, par2, z).texture(par3 * f, par4 * f1).endVertex();
		tessellator.draw();
	}

	public static void renderStar(MatrixStack ms, IRenderTypeBuffer buffers, int color, float xScale, float yScale, float zScale, long seed) {
		IVertexBuilder buffer = buffers.getBuffer(STAR);

		float ticks = (ClientTickHandler.ticksInGame % 200) + ClientTickHandler.partialTicks;
		if (ticks >= 100)
			ticks = 200 - ticks - 1;

		float f1 = ticks / 200F;
		float f2 = f1 > 0.F ? (f1 - 0.7F) / 0.2F : 0;
		Random random = new Random(seed);

		ms.push();
		ms.scale(xScale, yScale, zScale);

		for (int i = 0; i < (f1 + f1 * f1) / 2F * 90F + 30F; i++) {
			// todo 1.15 compose the quaternions before multiplying into ms?
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
			Runnable center = () -> buffer.vertex(mat, 0, 0, 0).color(r, g, b, 1F - f2).endVertex();
			Runnable[] vertices = {
					() -> buffer.vertex(mat, -0.866F * f4, f3, -0.5F * f4).color(0, 0, 0, 0).endVertex(),
					() -> buffer.vertex(mat, 0.866F * f4, f3, -0.5F * f4).color(0, 0, 0, 0).endVertex(),
					() -> buffer.vertex(mat, 0, f3, 1F * f4).color(0, 0, 0, 0).endVertex(),
					() -> buffer.vertex(mat, -0.866F * f4, f3, -0.5F * f4).color(0, 0, 0, 0).endVertex()
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
	 * Primitive restart would also work but it's not easy to use in Minecraft
	 */
	public static void triangleFan(Runnable center, List<Runnable> vertices) {
		for (int i = 0; i < vertices.size() - 1; i++) {
			center.run();
			vertices.get(i).run();
			vertices.get(i + 1).run();
		}
	}

	public static void renderProgressPie(int x, int y, float progress, ItemStack stack) {
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

		BufferBuilder buf = Tessellator.getInstance().getBuffer();
		buf.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
		buf.vertex(centerX, centerY, 0).color(0, 0.5F, 0.5F, a).endVertex();

		for(int i = degs; i > 0; i--) {
			double rad = (i - 90) / 180F * Math.PI;
			buf.vertex(centerX + Math.cos(rad) * r, centerY + Math.sin(rad) * r, 0).color(0F, 1F, 0.5F, a).endVertex();
		}

		buf.vertex(centerX, centerY, 0).color(0F, 1F, 0.5F, a).endVertex();
		Tessellator.getInstance().draw();

		RenderSystem.disableBlend();
		RenderSystem.enableTexture();
		RenderSystem.shadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_STENCIL_TEST);
	}

	public static String getKeyDisplayString(String keyName) {
		String key = null;
		KeyBinding[] keys = Minecraft.getInstance().gameSettings.keyBindings;
		for(KeyBinding otherKey : keys)
			if(otherKey.getKeyDescription().equals(keyName)) {
				key = otherKey.getLocalizedName();
				break;
			}

		return key;
	}

	/**
	 * @param color Must include alpha
	 */
	// [VanillaCopy] ItemRenderer.renderItem with simplifications + color support
	public static void renderItemCustomColor(LivingEntity entity, ItemStack stack, int color, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		ms.push();
		IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(stack, entity.world, entity);
		model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(ms, model, ItemCameraTransforms.TransformType.NONE, false);
		ms.translate(-0.5D, -0.5D, -0.5D);
		if (!model.isBuiltInRenderer()) {
			RenderType rendertype = RenderTypeLookup.getItemLayer(stack);

			IVertexBuilder ivertexbuilder = ItemRenderer.getArmorVertexConsumer(buffers, rendertype, true, stack.hasEffect());
			renderBakedItemModel(model, stack, color, light, overlay, ms, ivertexbuilder);
		} else {
			stack.getItem().getTileEntityItemStackRenderer().render(stack, ms, buffers, light, overlay);
		}

		ms.pop();
	}

	// [VanillaCopy] ItemRenderer with custom color
	private static void renderBakedItemModel(IBakedModel model, ItemStack stack, int color, int light, int overlay, MatrixStack ms, IVertexBuilder buffer) {
		Random random = new Random();
		long i = 42L;

		for(Direction direction : Direction.values()) {
			random.setSeed(42L);
			renderBakedItemQuads(ms, buffer, color, model.getQuads((BlockState)null, direction, random), stack, light, overlay);
		}

		random.setSeed(42L);
		renderBakedItemQuads(ms, buffer, color, model.getQuads((BlockState)null, (Direction)null, random), stack, light, overlay);
	}

	// [VanillaCopy] ItemRenderer, with custom color
	private static void renderBakedItemQuads(MatrixStack ms, IVertexBuilder buffer, int color, List<BakedQuad> quads, ItemStack stack, int light, int overlay) {
		MatrixStack.Entry matrixstack$entry = ms.peek();

		for(BakedQuad bakedquad : quads) {
			int i = color;

			float f = (float)(i >> 16 & 255) / 255.0F;
			float f1 = (float)(i >> 8 & 255) / 255.0F;
			float f2 = (float)(i & 255) / 255.0F;
			// Botania: support alpha
			float alpha = ((color >> 24) & 0xFF) / 255.0F;
			buffer.addVertexData(matrixstack$entry, bakedquad, f, f1, f2, alpha, light, overlay, true);
		}

	}
}
