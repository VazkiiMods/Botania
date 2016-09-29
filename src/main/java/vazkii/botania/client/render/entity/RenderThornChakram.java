/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 27, 2015, 10:02:12 PM (GMT)]
 */
package vazkii.botania.client.render.entity;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.entity.EntityThornChakram;
import vazkii.botania.common.item.ModItems;

// Basically a bit of an extension of RenderSnowball
public class RenderThornChakram extends RenderSnowball<EntityThornChakram> {

	public RenderThornChakram(RenderManager renderManager) {
		super(renderManager, ModItems.thornChakram, Minecraft.getMinecraft().getRenderItem());
	}

	@Override
	public ItemStack getStackToRender(EntityThornChakram entityIn) {
		return new ItemStack(ModItems.thornChakram, 1, entityIn.isFire() ? 1 : 0);  
	}
	
}
