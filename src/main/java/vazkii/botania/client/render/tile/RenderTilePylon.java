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

import java.util.Random;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import vazkii.botania.api.lexicon.multiblock.IMultiblockRenderHook;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.PylonVariant;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MultiblockRenderHandler;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.IPylonModel;
import vazkii.botania.client.model.ModelPylonGaia;
import vazkii.botania.client.model.ModelPylonMana;
import vazkii.botania.client.model.ModelPylonNatura;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TilePylon;

public class RenderTilePylon extends TileEntitySpecialRenderer<TilePylon> implements IMultiblockRenderHook {

	private static final ResourceLocation MANA_TEXTURE = new ResourceLocation(LibResources.MODEL_PYLON_MANA);
	private static final ResourceLocation NATURA_TEXTURE = new ResourceLocation(LibResources.MODEL_PYLON_NATURA);
	private static final ResourceLocation GAIA_TEXTURE = new ResourceLocation(LibResources.MODEL_PYLON_GAIA);

	private final ModelPylonMana manaModel = new ModelPylonMana();
	private final ModelPylonNatura naturaModel = new ModelPylonNatura();
	private final ModelPylonGaia gaiaModel = new ModelPylonGaia();

	// Overrides for when we call this TESR without an actual pylon
	private static PylonVariant forceVariant = PylonVariant.MANA;

	@Override
	public void render(@Nonnull TilePylon pylon, double d0, double d1, double d2, float pticks, int digProgress, float unused) {
		boolean renderingItem = pylon == ForwardingTEISR.DUMMY;

		if(!renderingItem && (!pylon.getWorld().isBlockLoaded(pylon.getPos(), false) || pylon.getWorld().getBlockState(pylon.getPos()).getBlock() != ModBlocks.pylon))
			return;

		renderPylon(pylon, d0, d1, d2, pticks, renderingItem);
	}
	
	private void renderPylon(@Nonnull TilePylon pylon, double d0, double d1, double d2, float pticks, boolean renderingItem) {
		PylonVariant type = renderingItem ? forceVariant : ModBlocks.pylon.getStateFromMeta(pylon.getBlockMetadata()).getValue(BotaniaStateProps.PYLON_VARIANT);
		IPylonModel model;
		switch(type) {
		default:
		case MANA: {
			model = manaModel;
			Minecraft.getMinecraft().renderEngine.bindTexture(MANA_TEXTURE);
			break;
		}
		case NATURA: {
			model = naturaModel;
			Minecraft.getMinecraft().renderEngine.bindTexture(NATURA_TEXTURE);
			break;
		}
		case GAIA: {
			model = gaiaModel;
			Minecraft.getMinecraft().renderEngine.bindTexture(GAIA_TEXTURE);
			break;
		}
		}

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		float a = MultiblockRenderHandler.rendering ? 0.6F : 1F;
		GlStateManager.color(1F, 1F, 1F, a);

		double worldTime = (double) (ClientTickHandler.ticksInGame + pticks);

		worldTime += renderingItem ? 0 : new Random(pylon.getPos().hashCode()).nextInt(360);

		GlStateManager.translate(d0, d1 + (renderingItem ? 1.35 : 1.5), d2);
		GlStateManager.scale(1.0F, -1.0F, -1.0F);

		GlStateManager.pushMatrix();
		GlStateManager.translate(0.5F, 0F, -0.5F);
		if(!renderingItem)
			GlStateManager.rotate((float) worldTime * 1.5F, 0F, 1F, 0F);

		model.renderRing(); 
		if(!renderingItem)
			GlStateManager.translate(0D, Math.sin(worldTime / 20D) / 20 - 0.025, 0D);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		if(!renderingItem)
			GlStateManager.translate(0D, Math.sin(worldTime / 20D) / 17.5, 0D);

		GlStateManager.translate(0.5F, 0F, -0.5F);
		if(!renderingItem) 
			GlStateManager.rotate((float) -worldTime, 0F, 1F, 0F);

		GlStateManager.disableCull();
		GlStateManager.disableAlpha();

		if(!renderingItem)
			ShaderHelper.useShader(ShaderHelper.pylonGlow);
		model.renderCrystal();
		if(!renderingItem)
			ShaderHelper.releaseShader();

		GlStateManager.enableAlpha();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();

		GlStateManager.disableBlend();
		GlStateManager.enableRescaleNormal();
		GlStateManager.popMatrix();
	}

	// Dirty hack to make the TEISR aware of the stack metas
	public static class ForwardingTEISR extends TileEntityItemStackRenderer {
		private static final TilePylon DUMMY = new TilePylon();

		private final TileEntityItemStackRenderer compose;

		public ForwardingTEISR(TileEntityItemStackRenderer compose) {
			this.compose = compose;
		}

		@Override
		public void renderByItem(ItemStack stack, float partialTicks) {
			if(stack.getItem() == Item.getItemFromBlock(ModBlocks.pylon)) {
				RenderTilePylon.forceVariant = PylonVariant.values()[MathHelper.clamp(stack.getItemDamage(), 0, PylonVariant.values().length)];
				TileEntityRendererDispatcher.instance.render(DUMMY, 0, 0, 0, partialTicks);
			} else {
				compose.renderByItem(stack, partialTicks);
			}
		}
	}

	@Override
	public void renderBlockForMultiblock(IBlockAccess world, Multiblock mb, IBlockState state, MultiblockComponent comp) {
		forceVariant = state.getValue(BotaniaStateProps.PYLON_VARIANT);
		GlStateManager.translate(-0.5, -0.25, -0.5);
		renderPylon((TilePylon) comp.getTileEntity(), 0, 0, 0, 0, true);
		forceVariant = PylonVariant.MANA;
	}

	@Override
	public boolean needsTranslate(IBlockState state) {
		return true;
	}
}
