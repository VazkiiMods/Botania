/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 14, 2014, 5:28:21 PM (GMT)]
 */
package vazkii.botania.client.core.helper;

import com.google.common.base.Function;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ITransformation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

public final class IconHelper {

	public static TextureAtlasSprite forName(TextureMap ir, String name) {
		return ir.registerSprite(new ResourceLocation(LibResources.PREFIX_MOD, name));
	}

	public static TextureAtlasSprite forName(TextureMap ir, String name, String dir) {
		return ir.registerSprite(new ResourceLocation(LibResources.PREFIX_MOD, dir + "/" + name));
	}

	public static TextureAtlasSprite forBlock(TextureMap ir, Block block) {
		return forName(ir, block.getUnlocalizedName().replaceAll("tile\\.", ""));
	}

	public static TextureAtlasSprite forBlock(TextureMap ir, Block block, int i) {
		return forBlock(ir, block, Integer.toString(i));
	}

	public static TextureAtlasSprite forBlock(TextureMap ir, Block block, int i, String dir) {
		return forBlock(ir, block, Integer.toString(i), dir);
	}

	public static TextureAtlasSprite forBlock(TextureMap ir, Block block, String s) {
		return forName(ir, block.getUnlocalizedName().replaceAll("tile\\.", "") + s);
	}

	public static TextureAtlasSprite forBlock(TextureMap ir, Block block, String s, String dir) {
		return forName(ir, block.getUnlocalizedName().replaceAll("tile\\.", "") + s, dir);
	}

	public static TextureAtlasSprite forItem(TextureMap ir, Item item) {
		return forName(ir, item.getUnlocalizedName().replaceAll("item\\.", ""));
	}

	public static TextureAtlasSprite forItem(TextureMap ir, Item item, int i) {
		return forItem(ir, item, Integer.toString(i));
	}

	public static TextureAtlasSprite forItem(TextureMap ir, Item item, String s) {
		return forName(ir, item.getUnlocalizedName().replaceAll("item\\.", "") + s);
	}

	/**
	 * Load and bake an arbitrary model
	 */
	public static IFlexibleBakedModel loadAndBakeArbitraryModel(ResourceLocation location) {

		try {
			IModel unbaked = ModelLoaderRegistry.getModel(location);
			return unbaked.bake(unbaked.getDefaultState(), DefaultVertexFormats.ITEM, new Function<ResourceLocation, TextureAtlasSprite>() {
				@Override
				public TextureAtlasSprite apply(ResourceLocation input) {
					return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(input.toString());
				}
			});
		} catch (IOException ex) {
			return null;
		}

	}

	/**
	 * From 1.7, takes a sprite and renders it in the "flat 3D" style of held items
	 * Use this ONLY for sprites that don't have a corresponding item!
	 * For items, use built in methods that use the bakedmodel since those are much more efficient
	 * Args: tess, maxu, minv, minu, maxv, width, height, thickness(?)
	 * todo 1.8 find a replacement for this method, possibly the above bakeModelFromIcon
     */
	public static void renderItemIn2D(Tessellator tess, float p_78439_1_, float p_78439_2_, float p_78439_3_, float p_78439_4_, int width, int height, float p_78439_7_)
	{
		WorldRenderer wr = tess.getWorldRenderer();
		wr.startDrawingQuads();
		wr.setNormal(0.0F, 0.0F, 1.0F);
		wr.addVertexWithUV(0.0D, 0.0D, 0.0D, (double)p_78439_1_, (double)p_78439_4_);
		wr.addVertexWithUV(1.0D, 0.0D, 0.0D, (double)p_78439_3_, (double)p_78439_4_);
		wr.addVertexWithUV(1.0D, 1.0D, 0.0D, (double)p_78439_3_, (double)p_78439_2_);
		wr.addVertexWithUV(0.0D, 1.0D, 0.0D, (double)p_78439_1_, (double)p_78439_2_);
		tess.draw();
		wr.startDrawingQuads();
		wr.setNormal(0.0F, 0.0F, -1.0F);
		wr.addVertexWithUV(0.0D, 1.0D, (double)(0.0F - p_78439_7_), (double)p_78439_1_, (double)p_78439_2_);
		wr.addVertexWithUV(1.0D, 1.0D, (double)(0.0F - p_78439_7_), (double)p_78439_3_, (double)p_78439_2_);
		wr.addVertexWithUV(1.0D, 0.0D, (double)(0.0F - p_78439_7_), (double)p_78439_3_, (double)p_78439_4_);
		wr.addVertexWithUV(0.0D, 0.0D, (double)(0.0F - p_78439_7_), (double)p_78439_1_, (double)p_78439_4_);
		tess.draw();
		float f5 = 0.5F * (p_78439_1_ - p_78439_3_) / (float)width;
		float f6 = 0.5F * (p_78439_4_ - p_78439_2_) / (float)height;
		wr.startDrawingQuads();
		wr.setNormal(-1.0F, 0.0F, 0.0F);
		int k;
		float f7;
		float f8;

		for (k = 0; k < width; ++k)
		{
			f7 = (float)k / (float)width;
			f8 = p_78439_1_ + (p_78439_3_ - p_78439_1_) * f7 - f5;
			wr.addVertexWithUV((double)f7, 0.0D, (double)(0.0F - p_78439_7_), (double)f8, (double)p_78439_4_);
			wr.addVertexWithUV((double)f7, 0.0D, 0.0D, (double)f8, (double)p_78439_4_);
			wr.addVertexWithUV((double)f7, 1.0D, 0.0D, (double)f8, (double)p_78439_2_);
			wr.addVertexWithUV((double)f7, 1.0D, (double)(0.0F - p_78439_7_), (double)f8, (double)p_78439_2_);
		}

		tess.draw();
		wr.startDrawingQuads();
		wr.setNormal(1.0F, 0.0F, 0.0F);
		float f9;

		for (k = 0; k < width; ++k)
		{
			f7 = (float)k / (float)width;
			f8 = p_78439_1_ + (p_78439_3_ - p_78439_1_) * f7 - f5;
			f9 = f7 + 1.0F / (float)width;
			wr.addVertexWithUV((double)f9, 1.0D, (double)(0.0F - p_78439_7_), (double)f8, (double)p_78439_2_);
			wr.addVertexWithUV((double)f9, 1.0D, 0.0D, (double)f8, (double)p_78439_2_);
			wr.addVertexWithUV((double)f9, 0.0D, 0.0D, (double)f8, (double)p_78439_4_);
			wr.addVertexWithUV((double)f9, 0.0D, (double)(0.0F - p_78439_7_), (double)f8, (double)p_78439_4_);
		}

		tess.draw();
		wr.startDrawingQuads();
		wr.setNormal(0.0F, 1.0F, 0.0F);

		for (k = 0; k < height; ++k)
		{
			f7 = (float)k / (float)height;
			f8 = p_78439_4_ + (p_78439_2_ - p_78439_4_) * f7 - f6;
			f9 = f7 + 1.0F / (float)height;
			wr.addVertexWithUV(0.0D, (double)f9, 0.0D, (double)p_78439_1_, (double)f8);
			wr.addVertexWithUV(1.0D, (double)f9, 0.0D, (double)p_78439_3_, (double)f8);
			wr.addVertexWithUV(1.0D, (double)f9, (double)(0.0F - p_78439_7_), (double)p_78439_3_, (double)f8);
			wr.addVertexWithUV(0.0D, (double)f9, (double)(0.0F - p_78439_7_), (double)p_78439_1_, (double)f8);
		}

		tess.draw();
		wr.startDrawingQuads();
		wr.setNormal(0.0F, -1.0F, 0.0F);

		for (k = 0; k < height; ++k)
		{
			f7 = (float)k / (float)height;
			f8 = p_78439_4_ + (p_78439_2_ - p_78439_4_) * f7 - f6;
			wr.addVertexWithUV(1.0D, (double)f7, 0.0D, (double)p_78439_3_, (double)f8);
			wr.addVertexWithUV(0.0D, (double)f7, 0.0D, (double)p_78439_1_, (double)f8);
			wr.addVertexWithUV(0.0D, (double)f7, (double)(0.0F - p_78439_7_), (double)p_78439_1_, (double)f8);
			wr.addVertexWithUV(1.0D, (double)f7, (double)(0.0F - p_78439_7_), (double)p_78439_3_, (double)f8);
		}

		tess.draw();
	}

}