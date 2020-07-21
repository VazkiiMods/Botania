/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.Profiler;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.ICreativeManaProvider;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.recipe.IManaInfusionRecipe;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileAltar;
import vazkii.botania.common.block.tile.TileRuneAltar;
import vazkii.botania.common.block.tile.corporea.TileCorporeaCrystalCube;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex.InputHandler;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.item.ItemCraftingHalo;
import vazkii.botania.common.item.ItemSextant;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemDodgeRing;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.item.equipment.bauble.ItemMonocle;

import java.util.List;

public final class HUDHandler {

	private HUDHandler() {}

	public static final Identifier manaBar = new Identifier(LibResources.GUI_MANA_HUD);

	public static void onDrawScreenPost(RenderGameOverlayEvent.Post event) {
		MinecraftClient mc = MinecraftClient.getInstance();
		Profiler profiler = mc.getProfiler();
		ItemStack main = mc.player.getMainHandStack();
		ItemStack offhand = mc.player.getOffHandStack();
		MatrixStack ms = event.getMatrixStack();

		if (event.getType() == ElementType.ALL) {
			profiler.push("botania-hud");

			if (MinecraftClient.getInstance().interactionManager.hasStatusBars()) {
				ItemStack tiara = EquipmentHandler.findOrEmpty(ModItems.flightTiara, mc.player);
				if (!tiara.isEmpty()) {
					profiler.push("flugelTiara");
					ItemFlightTiara.renderHUD(ms, mc.player, tiara);
					profiler.pop();
				}

				ItemStack dodgeRing = EquipmentHandler.findOrEmpty(ModItems.dodgeRing, mc.player);
				if (!dodgeRing.isEmpty()) {
					profiler.push("dodgeRing");
					ItemDodgeRing.renderHUD(ms, mc.player, dodgeRing, event.getPartialTicks());
					profiler.pop();
				}
			}

			HitResult pos = mc.crosshairTarget;

			if (pos != null) {
				BlockPos bpos = pos.getType() == HitResult.Type.BLOCK ? ((BlockHitResult) pos).getBlockPos() : null;
				BlockState state = bpos != null ? mc.world.getBlockState(bpos) : null;
				Block block = state == null ? null : state.getBlock();
				BlockEntity tile = bpos != null ? mc.world.getBlockEntity(bpos) : null;

				if (PlayerHelper.hasAnyHeldItem(mc.player)) {
					if (PlayerHelper.hasHeldItem(mc.player, ModItems.twigWand)) {
						if (block instanceof IWandHUD) {
							profiler.push("wandItem");
							((IWandHUD) block).renderHUD(ms, mc, mc.world, bpos);
							profiler.pop();
						}
					}
					if (tile instanceof TilePool && !mc.player.getMainHandStack().isEmpty()) {
						renderPoolRecipeHUD(ms, (TilePool) tile, mc.player.getMainHandStack());
					}
				}
				if (!PlayerHelper.hasHeldItem(mc.player, ModItems.lexicon)) {
					if (tile instanceof TileAltar) {
						((TileAltar) tile).renderHUD(ms, mc);
					} else if (tile instanceof TileRuneAltar) {
						((TileRuneAltar) tile).renderHUD(ms, mc);
					} else if (tile instanceof TileCorporeaCrystalCube) {
						renderCrystalCubeHUD(ms, (TileCorporeaCrystalCube) tile);
					}
				}
			}

			TileCorporeaIndex.getInputHandler();
			if (!InputHandler.getNearbyIndexes(mc.player).isEmpty() && mc.currentScreen instanceof ChatScreen) {
				profiler.push("nearIndex");
				renderNearIndexDisplay(ms);
				profiler.pop();
			}

			if (!main.isEmpty() && main.getItem() instanceof ItemCraftingHalo) {
				profiler.push("craftingHalo_main");
				ItemCraftingHalo.renderHUD(ms, mc.player, main);
				profiler.pop();
			} else if (!offhand.isEmpty() && offhand.getItem() instanceof ItemCraftingHalo) {
				profiler.push("craftingHalo_off");
				ItemCraftingHalo.renderHUD(ms, mc.player, offhand);
				profiler.pop();
			}

			if (!main.isEmpty() && main.getItem() instanceof ItemSextant) {
				profiler.push("sextant");
				ItemSextant.renderHUD(ms, mc.player, main);
				profiler.pop();
			}

			/*if(equippedStack != null && equippedStack.getItem() == ModItems.flugelEye) {
				profiler.startSection("flugelEye");
				ItemFlugelEye.renderHUD(event.getResolution(), mc.player, equippedStack);
				profiler.endSection();
			}*/

			if (Botania.proxy.isClientPlayerWearingMonocle()) {
				profiler.push("monocle");
				ItemMonocle.renderHUD(ms, mc.player);
				profiler.pop();
			}

			profiler.push("manaBar");

			PlayerEntity player = mc.player;
			if (!player.isSpectator()) {
				int totalMana = 0;
				int totalMaxMana = 0;
				boolean anyRequest = false;
				boolean creative = false;

				Inventory mainInv = player.inventory;
				Inventory accInv = BotaniaAPI.instance().getAccessoriesInventory(player);

				int invSize = mainInv.size();
				int size = invSize + accInv.size();

				for (int i = 0; i < size; i++) {
					boolean useAccessories = i >= invSize;
					Inventory inv = useAccessories ? accInv : mainInv;
					ItemStack stack = inv.getStack(i - (useAccessories ? invSize : 0));

					if (!stack.isEmpty()) {
						Item item = stack.getItem();
						if (item instanceof IManaUsingItem) {
							anyRequest = anyRequest || ((IManaUsingItem) item).usesMana(stack);
						}
					}
				}

				List<ItemStack> items = ManaItemHandler.instance().getManaItems(player);
				for (ItemStack stack : items) {
					Item item = stack.getItem();
					if (!((IManaItem) item).isNoExport(stack)) {
						totalMana += ((IManaItem) item).getMana(stack);
						totalMaxMana += ((IManaItem) item).getMaxMana(stack);
					}
					if (item instanceof ICreativeManaProvider && ((ICreativeManaProvider) item).isCreative(stack)) {
						creative = true;
					}
				}

				List<ItemStack> acc = ManaItemHandler.instance().getManaAccesories(player);
				for (ItemStack stack : acc) {
					Item item = stack.getItem();
					if (!((IManaItem) item).isNoExport(stack)) {
						totalMana += ((IManaItem) item).getMana(stack);
						totalMaxMana += ((IManaItem) item).getMaxMana(stack);
					}
					if (item instanceof ICreativeManaProvider && ((ICreativeManaProvider) item).isCreative(stack)) {
						creative = true;
					}
				}

				if (anyRequest) {
					renderManaInvBar(ms, creative, totalMana, totalMaxMana);
				}
			}

			profiler.swap("itemsRemaining");
			ItemsRemainingRenderHandler.render(ms, event.getPartialTicks());
			profiler.pop();
			profiler.pop();

			RenderSystem.color4f(1F, 1F, 1F, 1F);
		}
	}

	private static void renderManaInvBar(MatrixStack ms, boolean hasCreative, int totalMana, int totalMaxMana) {
		MinecraftClient mc = MinecraftClient.getInstance();
		int width = 182;
		int x = mc.getWindow().getScaledWidth() / 2 - width / 2;
		int y = mc.getWindow().getScaledHeight() - ConfigHandler.CLIENT.manaBarHeight.get();

		if (!hasCreative) {
			if (totalMaxMana == 0) {
				width = 0;
			} else {
				width *= (double) totalMana / (double) totalMaxMana;
			}
		}

		if (width == 0) {
			if (totalMana > 0) {
				width = 1;
			} else {
				return;
			}
		}

		int color = MathHelper.hsvToRgb(0.55F, (float) Math.min(1F, Math.sin(Util.getMeasuringTimeMs() / 200D) * 0.5 + 1F), 1F);
		int r = (color >> 16 & 0xFF);
		int g = (color >> 8 & 0xFF);
		int b = color & 0xFF;
		RenderSystem.color4f(r / 255F, g / 255F, b / 255F, 1 - (r / 255F));
		mc.getTextureManager().bindTexture(manaBar);

		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderHelper.drawTexturedModalRect(ms, x, y, 0, 251, width, 5);
		RenderSystem.disableBlend();
		RenderSystem.color4f(1, 1, 1, 1);
	}

	private static void renderPoolRecipeHUD(MatrixStack ms, TilePool tile, ItemStack stack) {
		MinecraftClient mc = MinecraftClient.getInstance();
		Profiler profiler = mc.getProfiler();

		profiler.push("poolRecipe");
		IManaInfusionRecipe recipe = tile.getMatchingRecipe(stack, tile.getWorld().getBlockState(tile.getPos().down()));
		if (recipe != null) {
			int x = mc.getWindow().getScaledWidth() / 2 - 11;
			int y = mc.getWindow().getScaledHeight() / 2 + 10;

			int u = tile.getCurrentMana() >= recipe.getManaToConsume() ? 0 : 22;
			int v = mc.player.getName().getString().equals("haighyorkie") && mc.player.isSneaking() ? 23 : 8;

			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			mc.getTextureManager().bindTexture(manaBar);
			RenderHelper.drawTexturedModalRect(ms, x, y, u, v, 22, 15);
			RenderSystem.color4f(1F, 1F, 1F, 1F);

			mc.getItemRenderer().renderInGuiWithOverrides(stack, x - 20, y);
			mc.getItemRenderer().renderInGuiWithOverrides(recipe.getOutput(), x + 26, y);
			mc.getItemRenderer().renderGuiItemOverlay(mc.textRenderer, recipe.getOutput(), x + 26, y);

			RenderSystem.disableLighting();
			RenderSystem.disableBlend();
		}
		profiler.pop();
	}

	private static void renderCrystalCubeHUD(MatrixStack ms, TileCorporeaCrystalCube tile) {
		MinecraftClient mc = MinecraftClient.getInstance();
		Profiler profiler = mc.getProfiler();

		profiler.push("crystalCube");
		ItemStack target = tile.getRequestTarget();
		if (!target.isEmpty()) {
			String s1 = target.getName().getString();
			String s2 = tile.getItemCount() + "x";
			int strlen = Math.max(mc.textRenderer.getWidth(s1), mc.textRenderer.getWidth(s2));
			int w = mc.getWindow().getScaledWidth();
			int h = mc.getWindow().getScaledHeight();
			int boxH = h / 2 + (tile.locked ? 20 : 10);
			DrawableHelper.fill(ms, w / 2 + 8, h / 2 - 12, w / 2 + strlen + 32, boxH, 0x44000000);
			DrawableHelper.fill(ms, w / 2 + 6, h / 2 - 14, w / 2 + strlen + 34, boxH + 2, 0x44000000);

			mc.textRenderer.drawWithShadow(ms, s1, w / 2 + 30, h / 2 - 10, 0x6666FF);
			mc.textRenderer.drawWithShadow(ms, tile.getItemCount() + "x", w / 2 + 30, h / 2, 0xFFFFFF);
			if (tile.locked) {
				mc.textRenderer.drawWithShadow(ms, I18n.translate("botaniamisc.locked"), w / 2 + 30, h / 2 + 10, 0xFFAA00);
			}
			RenderSystem.enableRescaleNormal();
			mc.getItemRenderer().renderInGuiWithOverrides(target, w / 2 + 10, h / 2 - 10);
		}

		profiler.pop();
	}

	private static void renderNearIndexDisplay(MatrixStack ms) {
		MinecraftClient mc = MinecraftClient.getInstance();
		String txt0 = I18n.translate("botaniamisc.nearIndex0");
		String txt1 = Formatting.GRAY + I18n.translate("botaniamisc.nearIndex1");
		String txt2 = Formatting.GRAY + I18n.translate("botaniamisc.nearIndex2");

		int l = Math.max(mc.textRenderer.getWidth(txt0), Math.max(mc.textRenderer.getWidth(txt1), mc.textRenderer.getWidth(txt2))) + 20;
		int x = mc.getWindow().getScaledWidth() - l - 20;
		int y = mc.getWindow().getScaledHeight() - 60;

		DrawableHelper.fill(ms, x - 6, y - 6, x + l + 6, y + 37, 0x44000000);
		DrawableHelper.fill(ms, x - 4, y - 4, x + l + 4, y + 35, 0x44000000);
		RenderSystem.enableRescaleNormal();
		mc.getItemRenderer().renderInGuiWithOverrides(new ItemStack(ModBlocks.corporeaIndex), x, y + 10);

		mc.textRenderer.drawWithShadow(ms, txt0, x + 20, y, 0xFFFFFF);
		mc.textRenderer.drawWithShadow(ms, txt1, x + 20, y + 14, 0xFFFFFF);
		mc.textRenderer.drawWithShadow(ms, txt2, x + 20, y + 24, 0xFFFFFF);
	}

	public static void drawSimpleManaHUD(MatrixStack ms, int color, int mana, int maxMana, String name) {
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		MinecraftClient mc = MinecraftClient.getInstance();
		int x = mc.getWindow().getScaledWidth() / 2 - mc.textRenderer.getWidth(name) / 2;
		int y = mc.getWindow().getScaledHeight() / 2 + 10;

		mc.textRenderer.drawWithShadow(ms, name, x, y, color);

		x = mc.getWindow().getScaledWidth() / 2 - 51;
		y += 10;

		renderManaBar(ms, x, y, color, 1F, mana, maxMana);

		RenderSystem.disableBlend();
	}

	public static void drawComplexManaHUD(int color, MatrixStack ms, int mana, int maxMana, String name, ItemStack bindDisplay, boolean properlyBound) {
		drawSimpleManaHUD(ms, color, mana, maxMana, name);

		MinecraftClient mc = MinecraftClient.getInstance();

		int x = mc.getWindow().getScaledWidth() / 2 + 55;
		int y = mc.getWindow().getScaledHeight() / 2 + 12;

		RenderSystem.enableRescaleNormal();
		mc.getItemRenderer().renderInGuiWithOverrides(bindDisplay, x, y);

		RenderSystem.disableDepthTest();
		if (properlyBound) {
			mc.textRenderer.drawWithShadow(ms, "\u2714", x + 10, y + 9, 0x004C00);
			mc.textRenderer.drawWithShadow(ms, "\u2714", x + 10, y + 8, 0x0BD20D);
		} else {
			mc.textRenderer.drawWithShadow(ms, "\u2718", x + 10, y + 9, 0x4C0000);
			mc.textRenderer.drawWithShadow(ms, "\u2718", x + 10, y + 8, 0xD2080D);
		}
		RenderSystem.enableDepthTest();
	}

	public static void renderManaBar(MatrixStack ms, int x, int y, int color, float alpha, int mana, int maxMana) {
		MinecraftClient mc = MinecraftClient.getInstance();

		RenderSystem.color4f(1F, 1F, 1F, alpha);
		mc.getTextureManager().bindTexture(manaBar);
		RenderHelper.drawTexturedModalRect(ms, x, y, 0, 0, 102, 5);

		int manaPercentage = Math.max(0, (int) ((double) mana / (double) maxMana * 100));

		if (manaPercentage == 0 && mana > 0) {
			manaPercentage = 1;
		}

		RenderHelper.drawTexturedModalRect(ms, x + 1, y + 1, 0, 5, 100, 3);

		float red = (color >> 16 & 0xFF) / 255F;
		float green = (color >> 8 & 0xFF) / 255F;
		float blue = (color & 0xFF) / 255F;
		RenderSystem.color4f(red, green, blue, alpha);
		RenderHelper.drawTexturedModalRect(ms, x + 1, y + 1, 0, 5, Math.min(100, manaPercentage), 3);
		RenderSystem.color4f(1, 1, 1, 1);
	}
}
