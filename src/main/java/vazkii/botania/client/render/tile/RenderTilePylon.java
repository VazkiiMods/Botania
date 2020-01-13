/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * <p>
 * File Created @ [Feb 18, 2014, 10:18:36 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.IPylonModel;
import vazkii.botania.client.model.ModelPylonGaia;
import vazkii.botania.client.model.ModelPylonMana;
import vazkii.botania.client.model.ModelPylonNatura;
import vazkii.botania.common.block.BlockPylon;
import vazkii.botania.common.block.tile.TilePylon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class RenderTilePylon extends TileEntityRenderer<TilePylon> {

	private static final ResourceLocation MANA_TEXTURE = new ResourceLocation(LibResources.MODEL_PYLON_MANA);
	private static final ResourceLocation NATURA_TEXTURE = new ResourceLocation(LibResources.MODEL_PYLON_NATURA);
	private static final ResourceLocation GAIA_TEXTURE = new ResourceLocation(LibResources.MODEL_PYLON_GAIA);

	private final ModelPylonMana manaModel = new ModelPylonMana();
	private final ModelPylonNatura naturaModel = new ModelPylonNatura();
	private final ModelPylonGaia gaiaModel = new ModelPylonGaia();

	// Overrides for when we call this TESR without an actual pylon
	private static BlockPylon.Variant forceVariant = BlockPylon.Variant.MANA;

	@Override
	public void render(@Nonnull TilePylon pylon, double d0, double d1, double d2, float pticks, int digProgress) {
		if(!pylon.getWorld().isBlockLoaded(pylon.getPos()) || !(pylon.getBlockState().getBlock() instanceof BlockPylon))
			return;

		renderPylon(pylon, d0, d1, d2, pticks);
	}
	
	private void renderPylon(@Nullable TilePylon pylon, double d0, double d1, double d2, float pticks) {
		BlockPylon.Variant type = pylon == null ? forceVariant : ((BlockPylon) pylon.getBlockState().getBlock()).variant;
		IPylonModel model;
		switch(type) {
		default:
		case MANA: {
			model = manaModel;
			Minecraft.getInstance().textureManager.bindTexture(MANA_TEXTURE);
			break;
		}
		case NATURA: {
			model = naturaModel;
			Minecraft.getInstance().textureManager.bindTexture(NATURA_TEXTURE);
			break;
		}
		case GAIA: {
			model = gaiaModel;
			Minecraft.getInstance().textureManager.bindTexture(GAIA_TEXTURE);
			break;
		}
		}

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color4f(1F, 1F, 1F, 1F);

		double worldTime = (double) (ClientTickHandler.ticksInGame + pticks);

		worldTime += pylon == null ? 0 : new Random(pylon.getPos().hashCode()).nextInt(360);

		GlStateManager.translated(d0, d1 + (pylon == null ? 1.35 : 1.5), d2);
		GlStateManager.scalef(1.0F, -1.0F, -1.0F);

		GlStateManager.pushMatrix();
		GlStateManager.translatef(0.5F, 0F, -0.5F);
		if(pylon != null)
			GlStateManager.rotatef((float) worldTime * 1.5F, 0F, 1F, 0F);

		model.renderRing(); 
		if(pylon != null)
			GlStateManager.translated(0D, Math.sin(worldTime / 20D) / 20 - 0.025, 0D);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		if(pylon != null)
			GlStateManager.translated(0D, Math.sin(worldTime / 20D) / 17.5, 0D);

		GlStateManager.translatef(0.5F, 0F, -0.5F);
		if(pylon != null)
			GlStateManager.rotatef((float) -worldTime, 0F, 1F, 0F);

		GlStateManager.disableCull();
		GlStateManager.disableAlphaTest();

		if(pylon != null)
			ShaderHelper.useShader(ShaderHelper.pylonGlow);
		model.renderCrystal();
		if(pylon != null)
			ShaderHelper.releaseShader();

		GlStateManager.enableAlphaTest();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();

		GlStateManager.disableBlend();
		GlStateManager.enableRescaleNormal();
		GlStateManager.popMatrix();
	}

	public static class TEISR extends ItemStackTileEntityRenderer {
		@Override
		public void renderByItem(ItemStack stack) {
			if(Block.getBlockFromItem(stack.getItem()) instanceof BlockPylon) {
				RenderTilePylon.forceVariant = ((BlockPylon) Block.getBlockFromItem(stack.getItem())).variant;
				TileEntityRenderer r = TileEntityRendererDispatcher.instance.getRenderer(TilePylon.class);
				((RenderTilePylon) r).renderPylon(null, 0, 0, 0, 0);
				GlStateManager.enableBlend();
			}
		}
	}
}
