/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 22, 2015, 2:01:01 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.item.AccessoryRenderHelper;
import vazkii.botania.api.item.ICosmeticBauble;

import java.util.List;

public class ItemBaubleCosmetic extends ItemBauble implements ICosmeticBauble {

	public enum Variant {
		BLACK_BOWTIE, BLACK_TIE, RED_GLASSES, PUFFY_SCARF,
		ENGINEER_GOGGLES(true), EYEPATCH(true), WICKED_EYEPATCH(true), RED_RIBBONS(true),
		PINK_FLOWER_BUD(true), POLKA_DOTTED_BOWS(true), BLUE_BUTTERFLY(true), CAT_EARS(true),
		WITCH_PIN, DEVIL_TAIL, KAMUI_EYE, GOOGLY_EYES(true),
		FOUR_LEAF_CLOVER, CLOCK_EYE(true), UNICORN_HORN(true), DEVIL_HORNS(true),
		HYPER_PLUS(true), BOTANIST_EMBLEM, ANCIENT_MASK(true), EERIE_MASK(true),
		ALIEN_ANTENNA(true), ANAGLYPH_GLASSES(true), ORANGE_SHADES(true), GROUCHO_GLASSES(true),
		THICK_EYEBROWS(true), LUSITANIC_SHIELD, TINY_POTATO_MASK(true), QUESTGIVER_MARK(true),
		THINKING_HAND(true);

		private final boolean isHead;

		Variant(boolean isHead) {
			this.isHead = isHead;
		}

		Variant() {
			this(false);
		}
	}

	private final Variant variant;
	public static final int SUBTYPES = Variant.values().length;

	public ItemBaubleCosmetic(Variant variant, Properties props) {
		super(props);
		this.variant = variant;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addHiddenTooltip(ItemStack stack, World world, List<ITextComponent> stacks, ITooltipFlag flags) {
		if(variant == Variant.THINKING_HAND) {
			stacks.add(new TranslationTextComponent("botaniamisc.cosmeticThinking").applyTextStyle(TextFormatting.GRAY));
		} else {
			stacks.add(new TranslationTextComponent("botaniamisc.cosmeticBauble").applyTextStyle(TextFormatting.GRAY));
		}
		super.addHiddenTooltip(stack, world, stacks, flags);
	}


	@Override
	@OnlyIn(Dist.CLIENT)
	public void doRender(ItemStack stack, LivingEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		Variant variant = ((ItemBaubleCosmetic) stack.getItem()).variant;
		Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		if(variant.isHead) {
			AccessoryRenderHelper.translateToHeadLevel(player, partialTicks);
			AccessoryRenderHelper.translateToFace();
			AccessoryRenderHelper.defaultTransforms();
			switch (variant) {
			case RED_GLASSES:
			case ENGINEER_GOGGLES:
				scale(1.25F);
				GlStateManager.translatef(0F, -0.085F, 0.045F);
				renderItem(stack);
				break;
			case EYEPATCH:
				scale(0.55F);
				GlStateManager.translatef(-0.45F, -0.25F, 0F);
				renderItem(stack);
				break;
			case WICKED_EYEPATCH:
				scale(0.55F);
				GlStateManager.translatef(0.45F, -0.25F, 0F);
				renderItem(stack);
				break;
			case RED_RIBBONS:
				scale(0.9F);
				GlStateManager.translatef(0F, 0.75F, 1F);
				renderItem(stack);
				break;
			case PINK_FLOWER_BUD:
				GlStateManager.rotatef(-90F, 0F, 1F, 0F);
				GlStateManager.translatef(0.4F, 0.6F, 0.45F);
				renderItem(stack);
				break;
			case POLKA_DOTTED_BOWS:
				GlStateManager.rotatef(-90F, 0F, 1F, 0F);
				GlStateManager.translatef(0.65F, 0.3F, 0.5F);
				renderItem(stack);
				GlStateManager.translatef(0F, 0F, -1F);
				renderItem(stack);
				break;
			case BLUE_BUTTERFLY:
				GlStateManager.translatef(-0.75F, 0.1F, 1F);
				GlStateManager.pushMatrix();
				GlStateManager.rotatef(45F, 0F, 1F, 0F);
				renderItem(stack);
				GlStateManager.popMatrix();

				GlStateManager.translatef(0F, 0F, -0.75F);
				GlStateManager.rotatef(-45F, 0F, 1F, 0F);
				renderItem(stack);
				break;
			case CAT_EARS:
				GlStateManager.translatef(0F, 0.25F, 0.25F);
				renderItem(stack);
				break;
			case GOOGLY_EYES:
				GlStateManager.rotatef(180F, 0F, 1F, 0F);
				GlStateManager.scalef(1.5F, 1.5F, 1F);
				GlStateManager.translatef(0F, -0.05F, 0F);
				renderItem(stack);
				break;
			case CLOCK_EYE:
				scale(0.75F);
				GlStateManager.translatef(-0.25F, -0.1F, 0F);
				GlStateManager.rotatef(180F, 0F, 0F, 1F);
				renderItem(stack);
				break;
			case UNICORN_HORN:
				scale(1.25F);
				GlStateManager.rotatef(-90F, 0F, 1F, 0F);
				GlStateManager.translatef(0F, 0.4F, 0F);
				renderItem(stack);
				break;
			case DEVIL_HORNS:
				GlStateManager.translatef(0F, 0.2F, 0.25F);
				renderItem(stack);
				break;
			case HYPER_PLUS:
				scale(0.35F);
				GlStateManager.translatef(-0.7F, 1F, -0.5F);
				renderItem(stack);
				GlStateManager.translatef(1.45F, 0F, 0F);
				renderItem(stack);
				break;
			case ANCIENT_MASK:
				scale(1.25F);
				GlStateManager.translatef(0F, 0.025F, 0.01F);
				renderItem(stack);
				break;
			case EERIE_MASK:
				renderItem(stack);
				break;
			case ALIEN_ANTENNA:
				scale(0.9F);
				GlStateManager.rotatef(180F, 0F, 1F, 0F);
				GlStateManager.translatef(0F, 0.75F, -1F);
				renderItem(stack);
				break;
			case ANAGLYPH_GLASSES:
				scale(1.25F);
				GlStateManager.translatef(0F, -0.025F, 0F);
				renderItem(stack);
				break;
			case ORANGE_SHADES:
				scale(1.25f);
				GlStateManager.translatef(0F, 0.04F, 0F);
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GlStateManager.color4f(1F, 1F, 1F, 0.7F);
				renderItem(stack);
				break;
			case GROUCHO_GLASSES:
				scale(1.5F);
				GlStateManager.translatef(0F, -0.2125F, 0F);
				renderItem(stack);
				break;
			case THICK_EYEBROWS:
				scale(0.5F);
				GlStateManager.translatef(-0.4F, 0.05F, 0F);
				renderItem(stack);
				GlStateManager.rotatef(180F, 0F, 1F, 0F);
				GlStateManager.translatef(-0.775F, 0F, 0F);
				renderItem(stack);
				break;
			case TINY_POTATO_MASK:
				scale(1.25F);
				GlStateManager.translatef(0F, 0.025F, 0F);
				renderItem(stack);
				break;
			case QUESTGIVER_MARK:
				scale(0.8F);
				GlStateManager.translatef(0F, 1F, 0.3F);
				renderItem(stack);
				break;
			case THINKING_HAND:
				scale(0.9f);
				GlStateManager.translatef(0.2F, -0.5F, 0F);
				GlStateManager.scalef(-1, 1, 1);
				GlStateManager.rotatef(15F, 0F, 0F, 1F);
				renderItem(stack);
				break;
			default:
				break;
			}
		} else { // body cosmetics
			AccessoryRenderHelper.rotateIfSneaking(player);
			AccessoryRenderHelper.translateToChest();
			AccessoryRenderHelper.defaultTransforms();
			switch (variant) {
			case BLACK_BOWTIE:
				GlStateManager.translatef(0F, 0.15F, 0F);
				renderItem(stack);
				break;
			case BLACK_TIE:
			case PUFFY_SCARF:
				GlStateManager.translatef(0F, -0.15F, 0F);
				renderItem(stack);
				break;
			case WITCH_PIN:
				scale(0.35F);
				GlStateManager.translatef(-0.35F, 0.35F, 0.15F);
				renderItem(stack);
				break;
			case DEVIL_TAIL:
				GlStateManager.rotatef(90F, 0F, 1F, 0F);
				GlStateManager.translatef(0.5F, -0.75F, 0F);
				renderItem(stack);
				break;
			case KAMUI_EYE: // DON'T LOSE YOUR WAAAAAAAAY
				scale(0.9F);
				GlStateManager.translatef(0.9F, 0.35F, 0F);
				renderItem(stack);
				GlStateManager.translatef(-1.3F, -0.5F, 0.5F);
				GlStateManager.rotatef(180F, 0F, 0F, 1F);
				GlStateManager.rotatef(180F, 1F, 0F, 0F);
				renderKamuiBlack(stack);
				break;
			case FOUR_LEAF_CLOVER:
				scale(0.5F);
				GlStateManager.translatef(0.35F, 0.3F, -0.075F);
				renderItem(stack);
				break;
			case BOTANIST_EMBLEM:
				scale(0.5F);
				GlStateManager.translatef(0F, -0.75F, 0F);
				renderItem(stack);
				break;
			case LUSITANIC_SHIELD:
				GlStateManager.rotatef(180F, 0F, 1F, 0F);
				GlStateManager.translatef(0.035F, -0.2F, 0.55F);
				GlStateManager.rotatef(8F, 0F, 0F, 1F);
				renderItem(stack);
				break;
			default:
				break;
			}
		}
	}

	public static void scale(float f) {
		GlStateManager.scalef(f, f, f);
	}

	public static void renderItem(ItemStack stack) {
		GlStateManager.pushMatrix();
		Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
		GlStateManager.popMatrix();
	}

	// todo 1.13 recheck vanilla copying
	private static void renderKamuiBlack(ItemStack stack) {

		// Modified copy of RenderItem.renderItem(stack, transformtype)
		Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getInstance().textureManager.getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);

		IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(stack);

		GlStateManager.enableRescaleNormal();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableBlend();
		GlStateManager.blendFuncSeparate(770, 771, 1, 0);
		GlStateManager.pushMatrix();
		model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.NONE, false);

		renderModel(model, stack, 0xFF00004C);

		GlStateManager.cullFace(GlStateManager.CullFace.BACK);
		GlStateManager.popMatrix();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
		Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getInstance().textureManager.getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
	}

	// Adapted from RenderItem.renderModel(model, stack), added extra color param
	private static void renderModel(IBakedModel model, ItemStack stack, int color) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldrenderer = tessellator.getBuffer();
		worldrenderer.begin(7, DefaultVertexFormats.ITEM);

		for(Direction enumfacing : Direction.values()) {
			renderQuads(worldrenderer, model.getQuads(null, enumfacing, random), color, stack);
		}

		renderQuads(worldrenderer, model.getQuads(null, null, random), color, stack);
		tessellator.draw();
	}

	// Copy of RenderItem.renderQuads
	private static void renderQuads(BufferBuilder renderer, List<BakedQuad> quads, int color, ItemStack stack) {
		boolean flag = color == -1 && !stack.isEmpty();
		int i = 0;

		for(int j = quads.size(); i < j; ++i) {
			BakedQuad bakedquad = quads.get(i);
			int k = color;

			if(flag && bakedquad.hasTintIndex()) {
				k = Minecraft.getInstance().getItemColors().getColor(stack, bakedquad.getTintIndex());
				k = k | -16777216;
			}

			net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);
		}
	}

}
