/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.item.CosmeticAttachable;
import vazkii.botania.api.item.CosmeticBauble;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.integration.shared.LocaleHelper;
import vazkii.botania.client.render.AccessoryRenderRegistry;
import vazkii.botania.client.render.AccessoryRenderer;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.helper.FilterHelper;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.proxy.Proxy;

import java.util.List;

public class ManaseerMonocleItem extends BaubleItem implements CosmeticBauble {

	public ManaseerMonocleItem(Properties props) {
		super(props);
		Proxy.INSTANCE.runOnClient(() -> () -> AccessoryRenderRegistry.register(this, new Renderer()));
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {
		tooltip.add(Component.translatable("botaniamisc.cosmeticBauble").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
		super.appendHoverText(stack, world, tooltip, flags);
	}

	public static class Renderer implements AccessoryRenderer {
		@Override
		public void doRender(HumanoidModel<?> bipedModel, ItemStack stack, LivingEntity living, PoseStack ms, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			bipedModel.head.translateAndRotate(ms);
			ms.translate(0.15, -0.2, -0.25);
			ms.scale(0.3F, -0.3F, -0.3F);
			Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.NONE,
					light, OverlayTexture.NO_OVERLAY, ms, buffers, living.level(), living.getId());
		}
	}

	public static class Hud {
		private static final int MAX_CONTENTS_COLUMNS = 9;
		private static final int MAX_CONTENTS_ROWS = 3;
		public static final int TEXT_ROW_HEIGHT = 12;

		public static void render(GuiGraphics gui, Player player) {
			Minecraft mc = Minecraft.getInstance();
			HitResult hitResult = mc.hitResult;
			if (hitResult instanceof BlockHitResult bhr && bhr.getType() == HitResult.Type.BLOCK) {
				renderBlockInfo(gui, bhr, mc, player.level());
			} else if (hitResult instanceof EntityHitResult ehr && ehr.getType() == HitResult.Type.ENTITY) {
				renderEntityInfo(gui, ehr, mc);
			}
		}

		private static void renderEntityInfo(GuiGraphics gui, EntityHitResult hitResult, Minecraft mc) {
			Entity entity = hitResult.getEntity();

			if (entity instanceof ItemFrame frame && !frame.getItem().isEmpty()) {
				ItemStack frameItem = frame.getItem();
				List<ItemStack> contentItems = FilterHelper.getFilterStacks(frameItem);
				if (contentItems.isEmpty() || contentItems.size() == 1 && ItemStack.isSameItemSameTags(frameItem, contentItems.get(0))) {
					return;
				}

				int x = mc.getWindow().getGuiScaledWidth() / 2 + 15;
				int y = mc.getWindow().getGuiScaledHeight() / 2 - 24;
				int maxWidth = mc.getWindow().getGuiScaledWidth() - x - 30;

				MutableComponent itemName = Component.empty().append(frameItem.getHoverName())
						.withStyle(frameItem.getRarity().color);
				if (frameItem.hasCustomHoverName()) {
					itemName.withStyle(ChatFormatting.ITALIC);
				}
				MutableComponent text = Component.translatable("botaniamisc.monocle.frame.contains", itemName);

				List<FormattedCharSequence> lines = mc.font.split(text, maxWidth);
				int textWidth = lines.stream().mapToInt(mc.font::width).max().orElseThrow();
				int textYOffset = (lines.size() - 1) * TEXT_ROW_HEIGHT;

				int contentsWidth = Math.min(MAX_CONTENTS_COLUMNS, contentItems.size()) * 18;
				int contentsHeight = Math.min(MAX_CONTENTS_ROWS + 1,
						(contentItems.size() - 1) / MAX_CONTENTS_COLUMNS + 1) * 18;
				RenderHelper.renderHUDBox(gui, x - 4, y - 4,
						x + Math.max(textWidth, contentsWidth) + 24, y + textYOffset + contentsHeight + 20);
				gui.renderItem(frameItem, x, y);

				int textRow = 0;
				for (var line : lines) {
					gui.drawString(mc.font, line, x + 20, y + TEXT_ROW_HEIGHT * textRow + 4, 0xFFFFFF);
					textRow++;
				}

				int row = 1;
				int column = 0;
				for (ItemStack contentItem : contentItems) {
					if (++column > MAX_CONTENTS_COLUMNS) {
						column = 1;
						if (++row > MAX_CONTENTS_ROWS) {
							break;
						}
					}

					gui.renderItem(contentItem, x + 18 * column, y + 18 * row + textYOffset);
					gui.renderItemDecorations(mc.font, contentItem, x + 18 * column, y + 18 * row + textYOffset);
				}

				if (row > MAX_CONTENTS_ROWS) {
					MutableComponent remainingItemsHint = Component.translatable(
							"botaniamisc.monocle.frame.additional_stacks",
							contentItems.size() - MAX_CONTENTS_COLUMNS * MAX_CONTENTS_ROWS);
					gui.drawString(mc.font, remainingItemsHint, x + 24, y + 18 * row + 6 + textYOffset, 0xFFFFFF);
				}
			}
		}

		private static void renderBlockInfo(GuiGraphics gui, BlockHitResult hitResult, Minecraft mc, Level level) {
			BlockPos pos = hitResult.getBlockPos();
			BlockState state = level.getBlockState(pos);

			ItemStack dispStack;
			MutableComponent text;

			if (state.hasProperty(BlockStateProperties.POWER)) {
				// redstone wire, sculk sensors, daylight detector, target block, weighted pressure plates
				dispStack = new ItemStack(state.getBlock());
				text = Component.literal(state.getValue(BlockStateProperties.POWER).toString())
						.withStyle(ChatFormatting.RED);
			} else if (state.is(Blocks.REPEATER)) {
				dispStack = new ItemStack(Blocks.REPEATER);
				text = Component.translatable("botaniamisc.monocle.repeater.delay",
						LocaleHelper.formatAsDecimalFraction(0.1f * state.getValue(RepeaterBlock.DELAY), 1));
			} else if (state.is(Blocks.COMPARATOR)) {
				dispStack = new ItemStack(Blocks.COMPARATOR);
				text = Component.translatable("botaniamisc.monocle.comparator."
						+ state.getValue(ComparatorBlock.MODE).getSerializedName());
			} else {
				return;
			}

			int x = mc.getWindow().getGuiScaledWidth() / 2 + 15;
			int y = mc.getWindow().getGuiScaledHeight() / 2 - 8;

			int textWidth = mc.font.width(text.getVisualOrderText());
			RenderHelper.renderHUDBox(gui, x - 4, y - 4, x + textWidth + 24, y + 20);
			gui.renderItem(dispStack, x, y);
			gui.drawString(mc.font, text, x + 20, y + 4, 0xFFFFFF);
		}

	}

	public static boolean hasMonocle(LivingEntity living) {
		return !EquipmentHandler.findOrEmpty(stack -> {
			if (!stack.isEmpty()) {
				Item item = stack.getItem();
				if (stack.is(BotaniaTags.Items.BURST_VIEWERS)) {
					return true;
				}
				if (item instanceof CosmeticAttachable attach) {
					ItemStack cosmetic = attach.getCosmeticItem(stack);
					return !cosmetic.isEmpty() && cosmetic.is(BotaniaTags.Items.BURST_VIEWERS);
				}
			}
			return false;
		}, living).isEmpty();
	}

}
