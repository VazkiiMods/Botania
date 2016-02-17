/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 30, 2015, 4:10:14 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;

import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.client.model.animation.Event;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelCrystalCube;
import vazkii.botania.common.block.tile.corporea.TileCorporeaCrystalCube;
import vazkii.botania.common.lib.LibObfuscation;

import java.util.concurrent.TimeUnit;

public class RenderTileCorporeaCrystalCube extends TileEntitySpecialRenderer<TileCorporeaCrystalCube> {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_CRYSTAL_CUBE);
	ModelCrystalCube model = new ModelCrystalCube();
	EntityItem entity = null;

	@Override
	public void renderTileEntityAt(TileCorporeaCrystalCube cube, double d0, double d1, double d2, float f, int digProgress) {
		ItemStack stack = null;
		if (cube != null) {
			if(entity == null)
                entity = new EntityItem(cube.getWorld(), cube.getPos().getX(), cube.getPos().getY(), cube.getPos().getZ(), new ItemStack(Blocks.stone));

			ObfuscationReflectionHelper.setPrivateValue(EntityItem.class, entity, ClientTickHandler.ticksInGame, LibObfuscation.AGE);
			stack = cube.getRequestTarget();
			entity.setEntityItemStack(stack);
		}

		double time = ClientTickHandler.ticksInGame + f;
		double worldTicks = cube == null || cube.getWorld() == null ? 0 : time;

		Minecraft mc = Minecraft.getMinecraft();
		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.translate(d0, d1, d2);
		//mc.renderEngine.bindTexture(texture);
		GlStateManager.translate(0.5F, 1.5F, 0.5F);
		GlStateManager.scale(1F, -1F, -1F);
		//model.renderBase();
		GlStateManager.translate(0F, (float) Math.sin(worldTicks / 20.0) * 0.05F, 0F);
		if(stack != null) {
			GlStateManager.pushMatrix();
			float s = stack.getItem() instanceof ItemBlock ? 0.7F : 0.5F;
			GlStateManager.translate(0F, 0.8F, 0F);
			GlStateManager.scale(s, s, s);
			GlStateManager.rotate(180F, 0F, 0F, 1F);
			((Render) mc.getRenderManager().entityRenderMap.get(EntityItem.class)).doRender(entity, 0, 0, 0, 1F, f);
			GlStateManager.popMatrix();
			//mc.renderEngine.bindTexture(texture);
		}

		//GlStateManager.color(1F, 1F, 1F, 0.4F);
		//model.renderCube();
		GlStateManager.color(1F, 1F, 1F);

		if(stack != null) {
			int count = cube.getItemCount();
			String countStr = "" + count;
			int color = 0xFFFFFF;
			if(count > 9999) {
				countStr = count / 1000 + "K";
				color = 0xFFFF00;
				if(count > 9999999) {
					countStr = count / 10000000 + "M";
					color = 0x00FF00;
				}
			}
			color |= 0xA0 << 24;
			int colorShade = (color & 16579836) >> 2 | color & -16777216;

			float s = 1F / 64F;
			GlStateManager.scale(s, s, s);
			GlStateManager.disableLighting();
			int l = mc.fontRendererObj.getStringWidth(countStr);

			GlStateManager.translate(0F, 55F, 0F);
			float tr = -16.5F;
			for(int i = 0; i < 4; i++) {
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(0F, 0F, tr);
				mc.fontRendererObj.drawString(countStr, -l / 2, 0, color);
				GlStateManager.translate(0F, 0F, 0.1F);
				mc.fontRendererObj.drawString(countStr, -l / 2 + 1, 1, colorShade);
				GlStateManager.translate(0F, 0F, -tr - 0.1F);
			}
			GlStateManager.enableLighting();
		}

		GlStateManager.scale(1F, -1F, -1F);
		GlStateManager.enableRescaleNormal();
		GlStateManager.popMatrix();

		renderAnimatedModel(cube, d0, d1, d2, f);
	}

	// Copied from AnimationTESR
	protected static BlockRendererDispatcher blockRenderer;

	// Botania - expanded size to 15 to hold all states, expanded expire time to 1 sec for performance
	protected static final LoadingCache<Pair<IExtendedBlockState, IModelState>, IBakedModel> modelCache = CacheBuilder.newBuilder().maximumSize(15).expireAfterWrite(1000, TimeUnit.MILLISECONDS).build(new CacheLoader<Pair<IExtendedBlockState, IModelState>, IBakedModel>()
	{
		public IBakedModel load(Pair<IExtendedBlockState, IModelState> key) throws Exception
		{
			IBakedModel model = blockRenderer.getBlockModelShapes().getModelForState(key.getLeft().getClean());
			if(model instanceof ISmartBlockModel)
			{
				model = ((ISmartBlockModel)model).handleBlockState(key.getLeft().withProperty(Properties.AnimationProperty, key.getRight()));
			}
			return model;
		}
	});

	private static IBakedModel getModel(IExtendedBlockState state, IModelState modelState)
	{
		return modelCache.getUnchecked(Pair.of(state, modelState));
	}

	private void renderAnimatedModel(TileCorporeaCrystalCube te, double x, double y, double z, float partialTick) {
		// From FastTESR.renderTileEntityAt
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		this.bindTexture(TextureMap.locationBlocksTexture);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableBlend();
		GlStateManager.disableCull();

		if (Minecraft.isAmbientOcclusionEnabled())
		{
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
		}
		else
		{
			GlStateManager.shadeModel(GL11.GL_FLAT);
		}

		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

		// Inlined AnimationTESR.renderTileEntityFast
		if(blockRenderer == null) blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
		BlockPos pos = te.getPos();
		IBlockAccess world = MinecraftForgeClient.getRegionRenderCache(te.getWorld(), pos);
		IBlockState state = world.getBlockState(pos);
		if(state.getPropertyNames().contains(Properties.StaticProperty))
		{
			state = state.withProperty(Properties.StaticProperty, false);
		}
		if(state instanceof IExtendedBlockState)
		{
			IExtendedBlockState exState = (IExtendedBlockState)state;
			if(exState.getUnlistedNames().contains(Properties.AnimationProperty))
			{
				float time = Animation.getWorldTime(getWorld(), partialTick);
				Pair<IModelState, Iterable<Event>> pair = te.asm().apply(time);
				// handleEvents(te, time, pair.getRight());

				IBakedModel model = getModel(exState, pair.getLeft());

				worldRenderer.setTranslation(x - pos.getX(), y - pos.getY(), z - pos.getZ());

				blockRenderer.getBlockModelRenderer().renderModel(world, model, state, pos, worldRenderer, false);
			}
		}
		// End inline AnimationTESR.renderTileEntityFast

		worldRenderer.setTranslation(0, 0, 0);

		tessellator.draw();

		RenderHelper.enableStandardItemLighting();
	}

}
