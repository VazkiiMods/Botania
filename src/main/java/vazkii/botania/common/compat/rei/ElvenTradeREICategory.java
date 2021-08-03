/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.crafting.RecipeElvenTrade;
import vazkii.botania.common.lib.ResourceLocationHelper;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;

@Environment(EnvType.CLIENT)
public class ElvenTradeREICategory implements RecipeCategory<ElvenTradeREIDisplay> {
	private EntryStack gateway = EntryStack.create(new ItemStack(ModBlocks.alfPortal));
	private ResourceLocation TRADE_OVERLAY = ResourceLocationHelper.prefix("textures/gui/elven_trade_overlay.png");

	@Override
	public @NotNull ResourceLocation getIdentifier() {
		return RecipeElvenTrade.TYPE_ID;
	}

	@Override
	public @NotNull EntryStack getLogo() {
		return gateway;
	}

	@Override
	public @NotNull String getCategoryName() {
		return I18n.get("botania.nei.elvenTrade");
	}

	@Override
	public @NotNull List<Widget> setupDisplay(ElvenTradeREIDisplay display, Rectangle bounds) {
		List<Widget> widgets = new ArrayList<>();
		Point center = new Point(bounds.getCenterX() - 8, bounds.getCenterY() - 4);
		widgets.add(CategoryUtils.drawRecipeBackground(bounds));
		widgets.add(Widgets.createDrawableWidget((helper, matrices, mouseX, mouseY, delta) -> {
			CategoryUtils.drawOverlay(helper, matrices, TRADE_OVERLAY, center.x - 45, center.y - 34, 20, 19, 71, 75);
			drawPortal(matrices, center);
		}));
		int x = center.x - 20;
		for (List<EntryStack> o : display.getInputEntries()) {
			widgets.add(Widgets.createSlot(new Point(x, center.y - 40)).entries(o).disableBackground());
			x += 18;
		}
		x = center.x + 28;
		for (List<EntryStack> o : display.getResultingEntries()) {
			widgets.add(Widgets.createSlot(new Point(x, center.y)).entries(o).disableBackground());
			x += 18;
		}
		return widgets;
	}

	@Override
	public int getDisplayHeight() {
		return 100;
	}

	void drawPortal(PoseStack matrices, Point point) {
		TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(ResourceLocationHelper.prefix("block/alfheim_portal_swirl"));
		MultiBufferSource.BufferSource immediate = Minecraft.getInstance().renderBuffers().bufferSource();
		VertexConsumer v = immediate.getBuffer(RenderType.solid());
		int startX = point.x - 43;
		int startY = point.y - 17;
		int stopX = startX + 48;
		int stopY = startY + 48;
		Matrix4f mat = matrices.last().pose();
		Matrix3f n = matrices.last().normal();
		v.vertex(mat, startX, startY, 0).color(1f, 1f, 1f, 1f).uv(sprite.getU0(), sprite.getV0()).uv2(0xF000F0).normal(n, 1, 0, 0).endVertex();
		v.vertex(mat, startX, stopY, 0).color(1f, 1f, 1f, 1f).uv(sprite.getU0(), sprite.getV1()).uv2(0xF000F0).normal(n, 1, 0, 0).endVertex();
		v.vertex(mat, stopX, stopY, 0).color(1f, 1f, 1f, 1f).uv(sprite.getU1(), sprite.getV1()).uv2(0xF000F0).normal(n, 1, 0, 0).endVertex();
		v.vertex(mat, stopX, startY, 0).color(1f, 1f, 1f, 1f).uv(sprite.getU1(), sprite.getV0()).uv2(0xF000F0).normal(n, 1, 0, 0).endVertex();
		immediate.endBatch();
	}
}
