/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.gui;

import com.google.common.collect.Iterables;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.block.MonocleHud;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.mana.ManaItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.recipe.ManaInfusionRecipe;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.lib.ResourcesLib;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.PetalApothecaryBlockEntity;
import vazkii.botania.common.block.block_entity.RunicAltarBlockEntity;
import vazkii.botania.common.block.block_entity.corporea.CorporeaCrystalCubeBlockEntity;
import vazkii.botania.common.block.block_entity.corporea.CorporeaIndexBlockEntity;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.item.AssemblyHaloItem;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.SextantItem;
import vazkii.botania.common.item.WandOfTheForestItem;
import vazkii.botania.common.item.equipment.bauble.FlugelTiaraItem;
import vazkii.botania.common.item.equipment.bauble.ManaseerMonocleItem;
import vazkii.botania.common.item.equipment.bauble.RingOfDexterousMotionItem;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.xplat.BotaniaConfig;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;

public final class HUDHandler {

	private HUDHandler() {}

	public static final ResourceLocation manaBar = ResourceLocation.parse(ResourcesLib.GUI_MANA_HUD);

	private static boolean didWarningsCheck = false;

	public static void checkForOneTimeWarnings() {
		if (!didWarningsCheck) {
			LocalPlayer player = Minecraft.getInstance().player;
			if (player == null) {
				return;
			}

			try {
				Class.forName("optifine.Installer");
				player.sendSystemMessage(Component.translatable("botaniamisc.optifine_warning"));
			} catch (ClassNotFoundException ignored) {}

			if (XplatAbstractions.instance().getBotaniaVersion().contains("SNAPSHOT")) {
				player.sendSystemMessage(Component.translatable("botaniamisc.dev_build_warning"));
			}
			didWarningsCheck = true;
		}
	}

	public static void onDrawExperienceBarPost(GuiGraphics gui, DeltaTracker deltaTracker) {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer localPlayer = mc.player;
		if (mc.options.hideGui || localPlayer == null) {
			return;
		}
		ProfilerFiller profiler = mc.getProfiler();
		ItemStack main = localPlayer.getMainHandItem();
		ItemStack offhand = localPlayer.getOffhandItem();

		profiler.push("botania-hud-early");

		float partialTick = deltaTracker.getGameTimeDeltaPartialTick(true);
		if (mc.gameMode.canHurtPlayer()) {
			ItemStack dodgeRing = EquipmentHandler.findOrEmpty(BotaniaItems.RING_OF_DEXTEROUS_MOTION, localPlayer);
			if (!dodgeRing.isEmpty()) {
				profiler.push("dodgeRing");
				RingOfDexterousMotionItem.ClientLogic.renderHUD(gui, localPlayer, dodgeRing, partialTick);
				profiler.pop();
			}
		}

		HitResult pos = mc.hitResult;
		Window window = mc.getWindow();
		Font font = mc.font;
		if (pos instanceof BlockHitResult result) {
			BlockPos bpos = result.getBlockPos();

			ClientLevel level = mc.level;
			BlockState state = level.getBlockState(bpos);
			BlockEntity tile = state.hasBlockEntity() ? level.getBlockEntity(bpos) : null;

			if (PlayerHelper.hasAnyHeldItem(localPlayer)) {
				boolean alternateRecipeHudPosition = false;
				if (PlayerHelper.hasHeldItemClass(localPlayer, WandOfTheForestItem.class)) {
					checkForOneTimeWarnings();
					var hud = WandHUD.BLOCK_LOOKUP.find(level, bpos, state, tile);
					if (hud != null) {
						alternateRecipeHudPosition = true;
						profiler.push("wandItem");
						hud.renderHUD(gui, window, font, partialTick);
						profiler.pop();
					}
				}
				if (tile instanceof ManaPoolBlockEntity pool && !localPlayer.getMainHandItem().isEmpty()) {
					renderPoolRecipeHUD(gui, pool, localPlayer.getMainHandItem(), alternateRecipeHudPosition);
				}
			}
			if (!PlayerHelper.hasHeldItem(localPlayer, BotaniaItems.LEXICA_BOTANIA)) {
				if (tile instanceof PetalApothecaryBlockEntity altar) {
					profiler.push("apothecary");
					PetalApothecaryBlockEntity.Hud.render(altar, gui, window, font, partialTick);
					profiler.pop();
				} else if (tile instanceof RunicAltarBlockEntity runeAltar) {
					profiler.push("runicAltar");
					RunicAltarBlockEntity.Hud.render(runeAltar, gui, window, font, localPlayer, partialTick);
					profiler.pop();
				} else if (tile instanceof CorporeaCrystalCubeBlockEntity cube) {
					profiler.push("crystalCube");
					CorporeaCrystalCubeBlockEntity.Hud.render(cube, gui, window, font, partialTick);
					profiler.pop();
				} else if (ManaseerMonocleItem.hasMonocle(localPlayer)) {
					var hud = MonocleHud.BLOCK_LOOKUP.find(level, bpos, state, tile);
					if (hud != null) {
						profiler.push("monocle");
						hud.renderHUD(gui, window, font, partialTick);
						profiler.pop();
					}
				}
			}
		} else if (pos instanceof EntityHitResult result) {
			if (PlayerHelper.hasHeldItemClass(localPlayer, WandOfTheForestItem.class)) {
				var hud = WandHUD.ENTITY_LOOKUP.find(result.getEntity());
				if (hud != null) {
					profiler.push("wandItemEntityHud");
					hud.renderHUD(gui, window, font, partialTick);
					profiler.pop();
				}
			}
			if (ManaseerMonocleItem.hasMonocle(localPlayer)) {
				var hud = MonocleHud.ENTITY_LOOKUP.find(result.getEntity());
				if (hud != null) {
					profiler.push("monocleEntityHud");
					hud.renderHUD(gui, window, font, partialTick);
					profiler.pop();
				}
			}
		}

		if (!CorporeaIndexBlockEntity.getNearbyValidIndexes(localPlayer).isEmpty() && mc.screen instanceof ChatScreen) {
			profiler.push("nearIndex");
			renderNearIndexDisplay(gui);
			profiler.pop();
		}

		if (!main.isEmpty() && main.getItem() instanceof AssemblyHaloItem) {
			profiler.push("craftingHalo_main");
			AssemblyHaloItem.Rendering.renderHUD(gui, localPlayer, main, window, font);
			profiler.pop();
		} else if (!offhand.isEmpty() && offhand.getItem() instanceof AssemblyHaloItem) {
			profiler.push("craftingHalo_off");
			AssemblyHaloItem.Rendering.renderHUD(gui, localPlayer, offhand, window, font);
			profiler.pop();
		}

		if (!main.isEmpty() && main.getItem() instanceof SextantItem) {
			profiler.push("sextant");
			SextantItem.Hud.render(gui, localPlayer, main);
			profiler.pop();
		}

		profiler.push("manaBar");

		if (!localPlayer.isSpectator()) {
			int totalMana = 0;
			int totalMaxMana = 0;
			boolean anyRequest = false;

			Container mainInv = localPlayer.getInventory();
			Container accInv = BotaniaAPI.instance().getAccessoriesInventory(localPlayer);

			int invSize = mainInv.getContainerSize();
			int size = invSize + accInv.getContainerSize();

			for (int i = 0; i < size; i++) {
				boolean useAccessories = i >= invSize;
				Container inv = useAccessories ? accInv : mainInv;
				ItemStack stack = inv.getItem(i - (useAccessories ? invSize : 0));

				if (!stack.isEmpty()) {
					anyRequest = anyRequest || stack.is(BotaniaTags.Items.MANA_USING_ITEMS);
				}
			}

			List<ItemStack> items = ManaItemHandler.instance().getManaItems(localPlayer);
			List<ItemStack> acc = ManaItemHandler.instance().getManaAccesories(localPlayer);
			for (ItemStack stack : Iterables.concat(items, acc)) {
				var manaItem = ManaItem.LOOKUP.find(stack);
				if (!manaItem.isNoExport()) {
					totalMana += manaItem.getMana();
					totalMaxMana += manaItem.getMaxMana();
				}
			}

			if (anyRequest) {
				renderManaInvBar(gui, window, totalMana, totalMaxMana);
			}
		}

		profiler.popPush("itemsRemaining");
		ItemsRemainingRenderHandler.render(gui, partialTick);
		profiler.pop();
		profiler.pop();

		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
	}

	public static void onDrawAirLevelPost(GuiGraphics gui, DeltaTracker deltaTracker) {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer localPlayer = mc.player;
		if (mc.options.hideGui || localPlayer == null) {
			return;
		}
		ProfilerFiller profiler = mc.getProfiler();

		profiler.push("botania-hud-late");

		float partialTick = deltaTracker.getGameTimeDeltaPartialTick(true);
		if (mc.gameMode.canHurtPlayer()) {
			ItemStack tiara = EquipmentHandler.findOrEmpty(BotaniaItems.FLUEGEL_TIARA, localPlayer);
			if (!tiara.isEmpty()) {
				profiler.push("flugelTiara");
				FlugelTiaraItem.ClientLogic.renderHUD(gui, partialTick, localPlayer, tiara);
				profiler.pop();
			}
		}

		profiler.pop();

		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
	}

	private static void renderManaInvBar(GuiGraphics gui, Window window, int totalMana, int totalMaxMana) {
		int width = 182;
		int x = window.getGuiScaledWidth() / 2 - width / 2;
		int y = window.getGuiScaledHeight() - BotaniaConfig.client().manaBarHeight();

		if (totalMaxMana == 0) {
			width = 0;
		} else {
			width = (int) (width * ((double) totalMana / (double) totalMaxMana));
		}

		if (width == 0) {
			if (totalMana > 0) {
				width = 1;
			} else {
				return;
			}
		}

		int color = Mth.hsvToRgb(0.55f, Math.min(1, Mth.sin(ClientTickHandler.getUiAnimationTicks() / 4) * 0.5f + 1), 1);
		float r = FastColor.ARGB32.red(color) / 255f;
		float g = FastColor.ARGB32.green(color) / 255f;
		float b = FastColor.ARGB32.blue(color) / 255f;
		RenderSystem.setShaderColor(r, g, b, 1 - r);

		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderHelper.drawTexturedModalRect(gui, manaBar, x, y, 0, 251, width, 5);
		RenderSystem.disableBlend();
		RenderSystem.setShaderColor(1, 1, 1, 1);
	}

	private static void renderPoolRecipeHUD(GuiGraphics gui, ManaPoolBlockEntity tile, ItemStack stack, boolean alternateRecipeHudPosition) {
		Minecraft mc = Minecraft.getInstance();
		ProfilerFiller profiler = mc.getProfiler();

		profiler.push("poolRecipe");
		RecipeHolder<ManaInfusionRecipe> recipe = tile.getMatchingRecipe(stack, tile.getLevel().getBlockState(tile.getBlockPos().below()));
		if (recipe != null) {
			int x = mc.getWindow().getGuiScaledWidth() / 2 - 11;
			int y = mc.getWindow().getGuiScaledHeight() / 2 + (alternateRecipeHudPosition ? -25 : 10);

			int u = tile.getCurrentMana() >= recipe.value().getManaToConsume() ? 0 : 22;
			int v = mc.player.getName().getString().equals("haighyorkie") && mc.player.isShiftKeyDown() ? 23 : 8;

			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			RenderHelper.drawTexturedModalRect(gui, manaBar, x, y, u, v, 22, 15);
			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

			gui.renderItem(stack, x - 20, y);
			ItemStack result = recipe.value().getResultItem(mc.level.registryAccess());
			gui.renderItem(result, x + 26, y);
			gui.renderItemDecorations(mc.font, result, x + 26, y);

			RenderSystem.disableBlend();
		}
		profiler.pop();
	}

	private static void renderNearIndexDisplay(GuiGraphics gui) {
		Minecraft mc = Minecraft.getInstance();
		String txt0 = I18n.get("botaniamisc.nearIndex0");
		String txt1 = ChatFormatting.GRAY + I18n.get("botaniamisc.nearIndex1");
		String txt2 = ChatFormatting.GRAY + I18n.get("botaniamisc.nearIndex2");

		int l = Math.max(mc.font.width(txt0), Math.max(mc.font.width(txt1), mc.font.width(txt2))) + 20;
		int x = mc.getWindow().getGuiScaledWidth() - l - 20;
		int y = mc.getWindow().getGuiScaledHeight() - 60;

		RenderHelper.renderHUDBox(gui, x - 4, y - 4, x + l + 4, y + 35);
		gui.renderItem(new ItemStack(BotaniaBlocks.CORPOREA_INDEX), x, y + 10);

		gui.drawString(mc.font, txt0, x + 20, y, 0xFFFFFF);
		gui.drawString(mc.font, txt1, x + 20, y + 14, 0xFFFFFF);
		gui.drawString(mc.font, txt2, x + 20, y + 24, 0xFFFFFF);
	}

	/**
	 * Renders a mana HUD below the crosshair, containing just a mana bar and a name above
	 */
	public static void drawSimpleManaHUD(GuiGraphics gui, Window window, Font font, int color, int mana, int maxMana, String name) {
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Minecraft mc = Minecraft.getInstance();
		int x = window.getGuiScaledWidth() / 2 - font.width(name) / 2;
		int y = window.getGuiScaledHeight() / 2 + 10;

		gui.drawString(font, name, x, y, color);

		x = window.getGuiScaledWidth() / 2 - 51;
		y += 10;

		renderManaBar(gui, x, y, color, 1F, mana, maxMana);

		RenderSystem.disableBlend();
	}

	/**
	 * Renders a mana HUD below the crosshair, containing a mana bar, a name above, and a bound item status to the right
	 */
	public static void drawComplexManaHUD(int color, GuiGraphics gui, Window window, Font font, int mana, int maxMana, String name, ItemStack bindDisplay, boolean properlyBound) {
		PoseStack ms = gui.pose();
		drawSimpleManaHUD(gui, window, font, color, mana, maxMana, name);

		int x = window.getGuiScaledWidth() / 2 + Math.max(51, font.width(name) / 2) + 4;
		int y = window.getGuiScaledHeight() / 2 + 12;

		gui.renderItem(bindDisplay, x, y);

		RenderSystem.disableDepthTest();
		ms.pushPose();
		// Magic number to get the string above the item we just rendered.
		ms.translate(0, 0, 200);
		if (properlyBound) {
			gui.drawString(font, "✔", x + 10, y + 9, 0x004C00);
			gui.drawString(font, "✔", x + 10, y + 8, 0x0BD20D);
		} else {
			gui.drawString(font, "✘", x + 10, y + 9, 0x4C0000);
			gui.drawString(font, "✘", x + 10, y + 8, 0xD2080D);
		}
		ms.popPose();
		RenderSystem.enableDepthTest();
	}

	public static void renderManaBar(GuiGraphics gui, int x, int y, int color, float alpha, int mana, int maxMana) {
		RenderSystem.setShaderColor(1F, 1F, 1F, alpha);
		RenderHelper.drawTexturedModalRect(gui, manaBar, x, y, 0, 0, 102, 5);

		int manaPercentage = Math.max(0, (int) ((double) mana / (double) maxMana * 100));

		if (manaPercentage == 0 && mana > 0) {
			manaPercentage = 1;
		}

		RenderHelper.drawTexturedModalRect(gui, manaBar, x + 1, y + 1, 0, 5, 100, 3);

		float red = (color >> 16 & 0xFF) / 255F;
		float green = (color >> 8 & 0xFF) / 255F;
		float blue = (color & 0xFF) / 255F;
		RenderSystem.setShaderColor(red, green, blue, alpha);
		RenderHelper.drawTexturedModalRect(gui, manaBar, x + 1, y + 1, 0, 5, Math.min(100, manaPercentage), 3);
		RenderSystem.setShaderColor(1, 1, 1, 1);
	}
}
