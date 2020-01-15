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

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
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
	public void doRender(ItemStack stack, LivingEntity player, MatrixStack ms, IRenderTypeBuffer buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		Variant variant = ((ItemBaubleCosmetic) stack.getItem()).variant;
		if(variant.isHead) {
			AccessoryRenderHelper.translateToHeadLevel(player, partialTicks);
			AccessoryRenderHelper.translateToFace();
			AccessoryRenderHelper.defaultTransforms();
			switch (variant) {
			case RED_GLASSES:
			case ENGINEER_GOGGLES:
				ms.scale(1.25F, 1.25F, 1.25F);
				ms.translate(0F, -0.085F, 0.045F);
				renderItem(stack, ms, buffers, light);
				break;
			case EYEPATCH:
				ms.scale(0.55F, 0.55F, 0.55F);
				ms.translate(-0.45F, -0.25F, 0F);
				renderItem(stack, ms, buffers, light);
				break;
			case WICKED_EYEPATCH:
				ms.scale(0.55F, 0.55F, 0.55F);
				ms.translate(0.45F, -0.25F, 0F);
				renderItem(stack, ms, buffers, light);
				break;
			case RED_RIBBONS:
				ms.scale(0.9F, 0.9F, 0.9F);
				ms.translate(0F, 0.75F, 1F);
				renderItem(stack, ms, buffers, light);
				break;
			case PINK_FLOWER_BUD:
				ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-90F));
				ms.translate(0.4F, 0.6F, 0.45F);
				renderItem(stack, ms, buffers, light);
				break;
			case POLKA_DOTTED_BOWS:
				ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-90F));
				ms.translate(0.65F, 0.3F, 0.5F);
				renderItem(stack, ms, buffers, light);
				ms.translate(0F, 0F, -1F);
				renderItem(stack, ms, buffers, light);
				break;
			case BLUE_BUTTERFLY:
				ms.translate(-0.75F, 0.1F, 1F);
				ms.push();
				ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(45F));
				renderItem(stack, ms, buffers, light);
				ms.pop();

				ms.translate(0F, 0F, -0.75F);
				ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-45F));
				renderItem(stack, ms, buffers, light);
				break;
			case CAT_EARS:
				ms.translate(0F, 0.25F, 0.25F);
				renderItem(stack, ms, buffers, light);
				break;
			case GOOGLY_EYES:
				ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180F));
				ms.scale(1.5F, 1.5F, 1F);
				ms.translate(0F, -0.05F, 0F);
				renderItem(stack, ms, buffers, light);
				break;
			case CLOCK_EYE:
				ms.scale(0.75F, 0.75F, 0.75F);
				ms.translate(-0.25F, -0.1F, 0F);
				ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180F));
				renderItem(stack, ms, buffers, light);
				break;
			case UNICORN_HORN:
				ms.scale(1.25F, 1.25F, 1.25F);
				ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-90F));
				ms.translate(0F, 0.4F, 0F);
				renderItem(stack, ms, buffers, light);
				break;
			case DEVIL_HORNS:
				ms.translate(0F, 0.2F, 0.25F);
				renderItem(stack, ms, buffers, light);
				break;
			case HYPER_PLUS:
				ms.scale(0.35F, 0.35F, 0.35F);
				ms.translate(-0.7F, 1F, -0.5F);
				renderItem(stack, ms, buffers, light);
				ms.translate(1.45F, 0F, 0F);
				renderItem(stack, ms, buffers, light);
				break;
			case ANCIENT_MASK:
				ms.scale(1.25F, 1.25F, 1.25F);
				ms.translate(0F, 0.025F, 0.01F);
				renderItem(stack, ms, buffers, light);
				break;
			case EERIE_MASK:
				renderItem(stack, ms, buffers, light);
				break;
			case ALIEN_ANTENNA:
				ms.scale(0.9F, 0.9F, 0.9F);
				ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180F));
				ms.translate(0F, 0.75F, -1F);
				renderItem(stack, ms, buffers, light);
				break;
			case ANAGLYPH_GLASSES:
				ms.scale(1.25F, 1.25F, 1.25F);
				ms.translate(0F, -0.025F, 0F);
				renderItem(stack, ms, buffers, light);
				break;
			case ORANGE_SHADES:
				ms.scale(1.25f, 1.25f, 1.25f);
				ms.translate(0F, 0.04F, 0F);
				/* todo 1.15
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GlStateManager.color4f(1F, 1F, 1F, 0.7F);
				 */
				renderItem(stack, ms, buffers, light);
				break;
			case GROUCHO_GLASSES:
				ms.scale(1.5F, 1.5F, 1.5F);
				ms.translate(0F, -0.2125F, 0F);
				renderItem(stack, ms, buffers, light);
				break;
			case THICK_EYEBROWS:
				ms.scale(0.5F, 0.5F, 0.5F);
				ms.translate(-0.4F, 0.05F, 0F);
				renderItem(stack, ms, buffers, light);
				ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180F));
				ms.translate(-0.775F, 0F, 0F);
				renderItem(stack, ms, buffers, light);
				break;
			case TINY_POTATO_MASK:
				ms.scale(1.25F, 1.25F, 1.25F);
				ms.translate(0F, 0.025F, 0F);
				renderItem(stack, ms, buffers, light);
				break;
			case QUESTGIVER_MARK:
				ms.scale(0.8F, 0.8F, 0.8F);
				ms.translate(0F, 1F, 0.3F);
				renderItem(stack, ms, buffers, light);
				break;
			case THINKING_HAND:
				ms.scale(0.9f, 0.9f, 0.9f);
				ms.translate(0.2F, -0.5F, 0F);
				ms.scale(-1, 1, 1);
				ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(15F));
				renderItem(stack, ms, buffers, light);
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
				ms.translate(0F, 0.15F, 0F);
				renderItem(stack, ms, buffers, light);
				break;
			case BLACK_TIE:
			case PUFFY_SCARF:
				ms.translate(0F, -0.15F, 0F);
				renderItem(stack, ms, buffers, light);
				break;
			case WITCH_PIN:
				ms.scale(0.35F, 0.35F, 0.35F);
				ms.translate(-0.35F, 0.35F, 0.15F);
				renderItem(stack, ms, buffers, light);
				break;
			case DEVIL_TAIL:
				ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90F));
				ms.translate(0.5F, -0.75F, 0F);
				renderItem(stack, ms, buffers, light);
				break;
			case KAMUI_EYE: // DON'T LOSE YOUR WAAAAAAAAY
				ms.scale(0.9F, 0.9F, 0.9F);
				ms.translate(0.9F, 0.35F, 0F);
				renderItem(stack, ms, buffers, light);
				ms.translate(-1.3F, -0.5F, 0.5F);
				ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180F));
				ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180F));
				renderKamuiBlack(stack);
				break;
			case FOUR_LEAF_CLOVER:
				ms.scale(0.5F, 0.5F, 0.5F);
				ms.translate(0.35F, 0.3F, -0.075F);
				renderItem(stack, ms, buffers, light);
				break;
			case BOTANIST_EMBLEM:
				ms.scale(0.5F, 0.5F, 0.5F);
				ms.translate(0F, -0.75F, 0F);
				renderItem(stack, ms, buffers, light);
				break;
			case LUSITANIC_SHIELD:
				ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180F));
				ms.translate(0.035F, -0.2F, 0.55F);
				ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(8F));
				renderItem(stack, ms, buffers, light);
				break;
			default:
				break;
			}
		}
	}

	public static void renderItem(ItemStack stack, MatrixStack ms, IRenderTypeBuffer buffers, int light) {
		Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.NONE, light, OverlayTexture.DEFAULT_UV, ms, buffers);
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
