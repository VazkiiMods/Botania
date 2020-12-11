/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei.elventrade;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.compat.rei.CategoryUtils;
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
	private Identifier TRADE_OVERLAY = ResourceLocationHelper.prefix("textures/gui/elven_trade_overlay.png");

	@Override
	public @NotNull Identifier getIdentifier() {
		return RecipeElvenTrade.TYPE_ID;
	}

	@Override
	public @NotNull EntryStack getLogo() {
		return gateway;
	}

	@Override
	public @NotNull String getCategoryName() {
		return I18n.translate("botania.nei.elvenTrade");
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

	void drawPortal(MatrixStack matrices, Point point) {
		Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(ResourceLocationHelper.prefix("block/alfheim_portal_swirl"));
		VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
		VertexConsumer v = immediate.getBuffer(RenderLayer.getSolid());
		int startX = point.x - 43;
		int startY = point.y - 17;
		int stopX = startX + 48;
		int stopY = startY + 48;
		Matrix4f mat = matrices.peek().getModel();
		Matrix3f n = matrices.peek().getNormal();
		v.vertex(mat, startX, startY, 0).color(1f, 1f, 1f, 1f).texture(sprite.getMinU(), sprite.getMinV()).light(0xF000F0).normal(n, 1, 0, 0).next();
		v.vertex(mat, startX, stopY, 0).color(1f, 1f, 1f, 1f).texture(sprite.getMinU(), sprite.getMaxV()).light(0xF000F0).normal(n, 1, 0, 0).next();
		v.vertex(mat, stopX, stopY, 0).color(1f, 1f, 1f, 1f).texture(sprite.getMaxU(), sprite.getMaxV()).light(0xF000F0).normal(n, 1, 0, 0).next();
		v.vertex(mat, stopX, startY, 0).color(1f, 1f, 1f, 1f).texture(sprite.getMaxU(), sprite.getMinV()).light(0xF000F0).normal(n, 1, 0, 0).next();
		immediate.draw();
	}
}
