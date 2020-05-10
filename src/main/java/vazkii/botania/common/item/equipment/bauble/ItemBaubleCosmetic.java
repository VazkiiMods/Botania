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
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.item.AccessoryRenderHelper;
import vazkii.botania.api.item.ICosmeticBauble;
import vazkii.botania.client.core.handler.BaubleRenderHandler;
import vazkii.botania.client.core.helper.RenderHelper;

import java.util.List;

public class ItemBaubleCosmetic extends ItemBauble implements ICosmeticBauble {

	public enum Variant {
		BLACK_BOWTIE, BLACK_TIE, RED_GLASSES(true), PUFFY_SCARF,
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
	public void doRender(BaubleRenderHandler layer, ItemStack stack, LivingEntity player, MatrixStack ms, IRenderTypeBuffer buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		Variant variant = ((ItemBaubleCosmetic) stack.getItem()).variant;
		if (variant.isHead) {
			layer.getEntityModel().bipedHead.translateRotate(ms);
			switch (variant) {
			case RED_GLASSES:
			case ENGINEER_GOGGLES:
			case ANAGLYPH_GLASSES:
				ms.translate(0, -0.225, -0.3);
				ms.scale(0.7F, -0.7F, -0.7F);
				renderItem(stack, ms, buffers, light);
				break;
			case EYEPATCH:
				ms.translate(0.125, -0.225, -0.3);
				ms.rotate(Vector3f.YP.rotationDegrees(180F));
				ms.scale(0.3F, -0.3F, -0.3F);
				renderItem(stack, ms, buffers, light);
				break;
			case WICKED_EYEPATCH:
				ms.translate(-0.125, -0.225, -0.3);
				ms.scale(0.3F, -0.3F, -0.3F);
				renderItem(stack, ms, buffers, light);
				break;
			case RED_RIBBONS:
				ms.translate(0, -0.65, 0.2);
				ms.rotate(Vector3f.YP.rotationDegrees(180F));
				ms.scale(0.5F, -0.5F, -0.5F);
				renderItem(stack, ms, buffers, light);
				break;
			case PINK_FLOWER_BUD:
				ms.translate(0.275, -0.6, 0);
				ms.rotate(Vector3f.YP.rotationDegrees(-90F));
				ms.scale(0.5F, -0.5F, -0.5F);
				renderItem(stack, ms, buffers, light);
				break;
			case POLKA_DOTTED_BOWS:
				ms.push();
				ms.translate(0.275, -0.4, 0);
				ms.rotate(Vector3f.YP.rotationDegrees(-90F));
				ms.scale(0.5F, -0.5F, -0.5F);
				renderItem(stack, ms, buffers, light);
				ms.pop();

				ms.translate(-0.275, -0.4, 0);
				ms.rotate(Vector3f.YP.rotationDegrees(90F));
				ms.scale(0.5F, -0.5F, -0.5F);
				renderItem(stack, ms, buffers, light);
				break;
			case BLUE_BUTTERFLY:
				ms.push();
				ms.translate(0.275, -0.4, 0);
				ms.rotate(Vector3f.YP.rotationDegrees(45F));
				ms.scale(0.5F, -0.5F, -0.5F);
				renderItem(stack, ms, buffers, light);
				ms.pop();

				ms.translate(0.275, -0.4, 0);
				ms.rotate(Vector3f.YP.rotationDegrees(-45F));
				ms.scale(0.5F, -0.5F, -0.5F);
				renderItem(stack, ms, buffers, light);
				break;
			case CAT_EARS:
				ms.translate(0F, -0.5F, -0.175F);
				ms.scale(0.5F, -0.5F, -0.5F);
				renderItem(stack, ms, buffers, light);
				break;
			case GOOGLY_EYES:
				ms.translate(0, -0.225, -0.3);
				ms.scale(0.9F, -0.9F, -0.9F);
				renderItem(stack, ms, buffers, light);
				break;
			case CLOCK_EYE:
				ms.translate(0.1, -0.225, -0.3F);
				ms.scale(0.4F, -0.4F, -0.4F);
				renderItem(stack, ms, buffers, light);
				break;
			case UNICORN_HORN:
				ms.translate(0, -0.7, -0.3);
				ms.rotate(Vector3f.YP.rotationDegrees(-90F));
				ms.scale(0.6F, -0.6F, -0.6F);
				renderItem(stack, ms, buffers, light);
				break;
			case DEVIL_HORNS:
				ms.translate(0F, -0.4F, -0.175F);
				ms.scale(0.5F, -0.5F, -0.5F);
				renderItem(stack, ms, buffers, light);
				break;
			case HYPER_PLUS:
				ms.translate(-0.15F, -0.45F, -0.3F);
				ms.scale(0.2F, -0.2F, -0.2F);
				renderItem(stack, ms, buffers, light);
				ms.translate(1.45F, 0F, 0F);
				renderItem(stack, ms, buffers, light);
				break;
			case ANCIENT_MASK:
				ms.translate(0, -0.3, -0.3);
				ms.scale(0.7F, -0.7F, -0.7F);
				renderItem(stack, ms, buffers, light);
				break;
			case EERIE_MASK:
				ms.translate(0, -0.25, -0.3);
				ms.scale(0.5F, -0.5F, -0.5F);
				renderItem(stack, ms, buffers, light);
				break;
			case ALIEN_ANTENNA:
				ms.translate(0, -0.65, 0.2);
				ms.scale(0.5F, -0.5F, -0.5F);
				renderItem(stack, ms, buffers, light);
				break;
			case ORANGE_SHADES:
				ms.translate(0, -0.3, -0.3);
				ms.scale(0.7F, -0.7F, -0.7F);
				int color = 0xFFFFFF | (178 << 24);
				RenderHelper.renderItemCustomColor(player, stack, color, ms, buffers, light, OverlayTexture.NO_OVERLAY);
				break;
			case GROUCHO_GLASSES:
				ms.translate(0, -0.1, -0.3);
				ms.scale(0.75F, -0.75F, -0.75F);
				renderItem(stack, ms, buffers, light);
				break;
			case THICK_EYEBROWS:
				ms.push();
				ms.translate(-0.1, -0.3, -0.3);
				ms.scale(0.3F, -0.3F, -0.3F);
				renderItem(stack, ms, buffers, light);
				ms.pop();

				ms.translate(0.1, -0.3, -0.3);
				ms.rotate(Vector3f.YP.rotationDegrees(180F));
				ms.scale(0.3F, -0.3F, -0.3F);
				renderItem(stack, ms, buffers, light);
				break;
			case TINY_POTATO_MASK:
				ms.translate(0, -0.3, -0.3);
				ms.scale(0.6F, -0.6F, -0.6F);
				renderItem(stack, ms, buffers, light);
				break;
			case QUESTGIVER_MARK:
				ms.translate(0, -0.8, -0.2);
				ms.scale(0.5F, -0.5F, -0.5F);
				renderItem(stack, ms, buffers, light);
				break;
			case THINKING_HAND:
				ms.translate(-0.1, 0, -0.3);
				ms.rotate(Vector3f.ZP.rotationDegrees(-15F));
				ms.scale(0.5F, -0.5F, -0.5F);
				renderItem(stack, ms, buffers, light);
				break;
			default:
				break;
			}
		} else { // body cosmetics
			layer.getEntityModel().bipedBody.translateRotate(ms);
			switch (variant) {
			case BLACK_BOWTIE:
				ms.translate(0, 0.1, -0.13);
				ms.scale(0.6F, -0.6F, -0.6F);
				renderItem(stack, ms, buffers, light);
				break;
			case BLACK_TIE:
			case PUFFY_SCARF:
				ms.translate(0, 0.25, -0.15);
				ms.scale(0.5F, -0.5F, -0.5F);
				renderItem(stack, ms, buffers, light);
				break;
			case WITCH_PIN:
				ms.translate(-0.1, 0.15, -0.15);
				ms.scale(0.2F, -0.2F, -0.2F);
				renderItem(stack, ms, buffers, light);
				break;
			case DEVIL_TAIL:
				ms.translate(0, 0.55, 0.2);
				ms.rotate(Vector3f.YP.rotationDegrees(-90F));
				ms.scale(0.6F, -0.6F, -0.6F);
				renderItem(stack, ms, buffers, light);
				break;
			case KAMUI_EYE: // DON'T LOSE YOUR WAAAAAAAAY
				ms.push();
				ms.translate(0.4, 0.1, -0.2);
				ms.scale(0.5F, -0.5F, -0.5F);
				renderItem(stack, ms, buffers, light);
				ms.pop();

				ms.translate(-0.4, 0.1, -0.2);
				ms.rotate(Vector3f.YP.rotationDegrees(180F));
				ms.scale(0.5F, -0.5F, -0.5F);
				RenderHelper.renderItemCustomColor(player, stack, 0xFF00004C, ms, buffers, light, OverlayTexture.NO_OVERLAY);
				break;
			case FOUR_LEAF_CLOVER:
				ms.translate(0.1, 0.1, -0.13);
				ms.scale(0.3F, -0.3F, -0.3F);
				renderItem(stack, ms, buffers, light);
				break;
			case BOTANIST_EMBLEM:
				ms.translate(0F, 0.375, -0.13);
				ms.scale(0.3F, -0.3F, -0.3F);
				renderItem(stack, ms, buffers, light);
				break;
			case LUSITANIC_SHIELD:
				ms.translate(0F, 0.35, 0.13);
				ms.rotate(Vector3f.ZP.rotationDegrees(8F));
				ms.rotate(Vector3f.YP.rotationDegrees(180F));
				ms.scale(0.6F, -0.6F, -0.6F);
				renderItem(stack, ms, buffers, light);
				break;
			default:
				break;
			}
		}
	}

	public static void renderItem(ItemStack stack, MatrixStack ms, IRenderTypeBuffer buffers, int light) {
		Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.NONE, light, OverlayTexture.NO_OVERLAY, ms, buffers);
	}

}
