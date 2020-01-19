/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 8, 2014, 7:20:26 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.common.block.tile.TileTerraPlate;

import javax.annotation.Nonnull;

public class RenderTileTerraPlate extends TileEntityRenderer<TileTerraPlate> {

	public RenderTileTerraPlate(TileEntityRendererDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nonnull TileTerraPlate plate, float f, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		float max = TileTerraPlate.MAX_MANA / 10F;
		float alphaMod = Math.min(max, plate.getCurrentMana()) / max;

		ms.push();
		ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90F));
		ms.translate(0F, 0F, -3F / 16F - 0.001F);

		float alpha = (float) ((Math.sin((ClientTickHandler.ticksInGame + f) / 8D) + 1D) / 5D + 0.6D) * alphaMod;

		IVertexBuilder buffer = buffers.getBuffer(RenderHelper.TERRA_PLATE);
		IconHelper.renderIcon(ms, buffer, 0, 0, MiscellaneousIcons.INSTANCE.terraPlateOverlay, 1, 1, alpha);

		ms.pop();
	}



}
