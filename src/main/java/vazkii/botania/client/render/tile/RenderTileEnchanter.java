/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 15, 2014, 5:04:42 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.item.ItemEntity;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.common.block.tile.TileEnchanter;

import javax.annotation.Nonnull;

public class RenderTileEnchanter extends TileEntityRenderer<TileEnchanter> {

	private ItemEntity item;

	public RenderTileEnchanter(TileEntityRendererDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nonnull TileEnchanter enchanter, float f, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		float alphaMod = 0F;

		if(enchanter.stage == TileEnchanter.State.GATHER_MANA)
			alphaMod = Math.min(20, enchanter.stageTicks) / 20F;
		else if(enchanter.stage == TileEnchanter.State.RESET)
			alphaMod = (20 - enchanter.stageTicks) / 20F;
		else if(enchanter.stage == TileEnchanter.State.DO_ENCHANT)
			alphaMod = 1F;

		ms.push();
		if(!enchanter.itemToEnchant.isEmpty()) {
			if(item == null)
				item = new ItemEntity(enchanter.getWorld(), enchanter.getPos().getX(), enchanter.getPos().getY() + 1, enchanter.getPos().getZ(), enchanter.itemToEnchant);

			item.age = ClientTickHandler.ticksInGame;
			item.setItem(enchanter.itemToEnchant);

			ms.translate(0.5F, 1.25F, 0.5F);
			Minecraft.getInstance().getRenderManager().render(item, 0, 0, 0, 0, f, ms, buffers, light);
			ms.translate(-0.5F, -1.25F, -0.5F);
		}

		ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90F));
		ms.translate(-2F, -2F, -0.001F);

		float alpha = (float) ((Math.sin((ClientTickHandler.ticksInGame + f) / 8D) + 1D) / 5D + 0.4D) * alphaMod;

		if(alpha > 0) {
			if(enchanter.stage == TileEnchanter.State.DO_ENCHANT || enchanter.stage == TileEnchanter.State.RESET) {
				int ticks = enchanter.stageTicks + enchanter.stage3EndTicks;
				int angle = ticks * 2;
				float yTranslation = Math.min(20, ticks) / 20F * 1.15F;
				float scale = ticks < 10 ? 1F : 1F - Math.min(20, ticks - 10) / 20F * 0.75F;

				ms.translate(2.5F, 2.5F, -yTranslation);
				ms.scale(scale, scale, 1F);
				ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(angle));
				ms.translate(-2.5F, -2.5F, 0F);
			}

			IVertexBuilder buffer = buffers.getBuffer(RenderHelper.ENCHANTER);
			IconHelper.renderIcon(ms, buffer, 0, 0, MiscellaneousIcons.INSTANCE.enchanterOverlay, 5, 5, alpha);
		}

		ms.pop();
	}

}
