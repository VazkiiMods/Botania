/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.item.AccessoryRenderHelper;
import vazkii.botania.api.item.ICosmeticBauble;
import vazkii.botania.client.core.helper.RenderHelper;

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
		if (variant == Variant.THINKING_HAND) {
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
		if (variant.isHead) {
			AccessoryRenderHelper.translateToHeadLevel(ms, player, partialTicks);
			AccessoryRenderHelper.translateToFace(ms);
			AccessoryRenderHelper.defaultTransforms(ms);
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
				int color = 0xFFFFFF | (178 << 24);
				RenderHelper.renderItemCustomColor(player, stack, color, ms, buffers, light, OverlayTexture.DEFAULT_UV);
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
			AccessoryRenderHelper.rotateIfSneaking(ms, player);
			AccessoryRenderHelper.translateToChest(ms);
			AccessoryRenderHelper.defaultTransforms(ms);
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
				RenderHelper.renderItemCustomColor(player, stack, 0xFF00004C, ms, buffers, light, OverlayTexture.DEFAULT_UV);
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

}
