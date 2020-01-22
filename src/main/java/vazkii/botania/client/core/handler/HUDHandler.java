/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 25, 2014, 6:11:10 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.IProfiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.ICreativeManaProvider;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.recipe.RecipeManaInfusion;
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
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemDodgeRing;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.item.equipment.bauble.ItemMonocle;
import vazkii.botania.common.lib.LibMisc;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = LibMisc.MOD_ID)
public final class HUDHandler {

	private HUDHandler() {}

	public static final ResourceLocation manaBar = new ResourceLocation(LibResources.GUI_MANA_HUD);

	@SubscribeEvent
	public static void onDrawScreenPost(RenderGameOverlayEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		IProfiler profiler = mc.getProfiler();
		ItemStack main = mc.player.getHeldItemMainhand();
		ItemStack offhand = mc.player.getHeldItemOffhand();

		if(event.getType() == ElementType.ALL) {
			profiler.startSection("botania-hud");

			if (Minecraft.getInstance().playerController.shouldDrawHUD()) {
				ItemStack tiara = EquipmentHandler.findOrEmpty(ModItems.flightTiara, mc.player);
				if(!tiara.isEmpty()) {
					profiler.startSection("flugelTiara");
					ItemFlightTiara.renderHUD(mc.player, tiara);
					profiler.endSection();
				}

				ItemStack dodgeRing = EquipmentHandler.findOrEmpty(ModItems.dodgeRing, mc.player);
				if(!dodgeRing.isEmpty()) {
					profiler.startSection("dodgeRing");
					ItemDodgeRing.renderHUD(mc.player, dodgeRing, event.getPartialTicks());
					profiler.endSection();
				}
			}

			RayTraceResult pos = mc.objectMouseOver;

			if(pos != null) {
				BlockPos bpos = pos.getType() == RayTraceResult.Type.BLOCK ? ((BlockRayTraceResult) pos).getPos() : null;
				BlockState state = bpos != null ? mc.world.getBlockState(bpos) : null;
				Block block = state == null ? null : state.getBlock();
				TileEntity tile = bpos != null ? mc.world.getTileEntity(bpos) : null;

				if(PlayerHelper.hasAnyHeldItem(mc.player)) {
					if(PlayerHelper.hasHeldItem(mc.player, ModItems.twigWand)) {
						renderWandModeDisplay(PlayerHelper.getFirstHeldItem(mc.player, ModItems.twigWand));

						if(block instanceof IWandHUD) {
							profiler.startSection("wandItem");
							((IWandHUD) block).renderHUD(mc, mc.world, bpos);
							profiler.endSection();
						}
					}
					if(tile instanceof TilePool && !mc.player.getHeldItemMainhand().isEmpty())
						renderPoolRecipeHUD((TilePool) tile, mc.player.getHeldItemMainhand());
				}
				if(!PlayerHelper.hasHeldItem(mc.player, ModItems.lexicon)) {
					if(tile instanceof TileAltar)
						((TileAltar) tile).renderHUD(mc);
					else if(tile instanceof TileRuneAltar)
						((TileRuneAltar) tile).renderHUD(mc);
					else if(tile instanceof TileCorporeaCrystalCube)
						renderCrystalCubeHUD((TileCorporeaCrystalCube) tile);
				}
			}

			TileCorporeaIndex.getInputHandler();
			if(!InputHandler.getNearbyIndexes(mc.player).isEmpty() && mc.currentScreen instanceof ChatScreen) {
				profiler.startSection("nearIndex");
				renderNearIndexDisplay();
				profiler.endSection();
			}

			if(!main.isEmpty() && main.getItem() instanceof ItemCraftingHalo) {
				profiler.startSection("craftingHalo_main");
				ItemCraftingHalo.renderHUD(mc.player, main);
				profiler.endSection();
			} else if(!offhand.isEmpty() && offhand.getItem() instanceof ItemCraftingHalo) {
				profiler.startSection("craftingHalo_off");
				ItemCraftingHalo.renderHUD(mc.player, offhand);
				profiler.endSection();
			}

			if(!main.isEmpty() && main.getItem() instanceof ItemSextant) {
				profiler.startSection("sextant");
				ItemSextant.renderHUD(mc.player, main);
				profiler.endSection();
			}

			/*if(equippedStack != null && equippedStack.getItem() == ModItems.flugelEye) {
				profiler.startSection("flugelEye");
				ItemFlugelEye.renderHUD(event.getResolution(), mc.player, equippedStack);
				profiler.endSection();
			}*/

			if(Botania.proxy.isClientPlayerWearingMonocle()) {
				profiler.startSection("monocle");
				ItemMonocle.renderHUD(mc.player);
				profiler.endSection();
			}

			profiler.startSection("manaBar");

			PlayerEntity player = mc.player;
			if(!player.isSpectator()) {
				int totalMana = 0;
				int totalMaxMana = 0;
				boolean anyRequest = false;
				boolean creative = false;

				IItemHandler mainInv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(EmptyHandler.INSTANCE);
				IItemHandler accInv = BotaniaAPI.internalHandler.getAccessoriesInventory(player);

				int invSize = mainInv.getSlots();
				int size = invSize;
				if(accInv != null)
					size += accInv.getSlots();

				for(int i = 0; i < size; i++) {
					boolean useAccessories = i >= invSize;
					IItemHandler inv = useAccessories ? accInv : mainInv;
					ItemStack stack = inv.getStackInSlot(i - (useAccessories ? invSize : 0));

					if(!stack.isEmpty()) {
						Item item = stack.getItem();
						if(item instanceof IManaUsingItem)
							anyRequest = anyRequest || ((IManaUsingItem) item).usesMana(stack);
					}
				}

				List<ItemStack> items = ManaItemHandler.getManaItems(player);
				for (ItemStack stack : items) {
					Item item = stack.getItem();
					if(!((IManaItem) item).isNoExport(stack)) {
						totalMana += ((IManaItem) item).getMana(stack);
						totalMaxMana += ((IManaItem) item).getMaxMana(stack);
					}
					if(item instanceof ICreativeManaProvider && ((ICreativeManaProvider) item).isCreative(stack))
						creative = true;
				}

				Map<Integer, ItemStack> acc = ManaItemHandler.getManaAccesories(player);
				for (Entry<Integer, ItemStack> entry : acc.entrySet()) {
					ItemStack stack = entry.getValue();
					Item item = stack.getItem();
					if(!((IManaItem) item).isNoExport(stack)) {
						totalMana += ((IManaItem) item).getMana(stack);
						totalMaxMana += ((IManaItem) item).getMaxMana(stack);
					}
					if(item instanceof ICreativeManaProvider && ((ICreativeManaProvider) item).isCreative(stack))
						creative = true;
				}

				if(anyRequest)
					renderManaInvBar(creative, totalMana, totalMaxMana);
			}

			profiler.endStartSection("itemsRemaining");
			ItemsRemainingRenderHandler.render(event.getPartialTicks());
			profiler.endSection();
			profiler.endSection();

			RenderSystem.color4f(1F, 1F, 1F, 1F);
		}
	}

	private static void renderWandModeDisplay(ItemStack stack) {
		Minecraft mc = Minecraft.getInstance();
		IProfiler profiler = mc.getProfiler();

		profiler.startSection("wandMode");
		int ticks = mc.ingameGUI.remainingHighlightTicks;
		ticks -= 15;
		if(ticks > 0) {
			int alpha = Math.min(255, (int) (ticks * 256.0F / 10.0F));
			int color = 0x00CC00 + (alpha << 24);
			String disp = I18n.format(ItemTwigWand.getModeString(stack));

			int x = mc.getWindow().getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(disp) / 2;
			int y = mc.getWindow().getScaledHeight() - 70;

			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			mc.fontRenderer.drawStringWithShadow(disp, x, y, color);
			RenderSystem.disableBlend();
		}
		profiler.endSection();
	}

	private static void renderManaInvBar(boolean hasCreative, int totalMana, int totalMaxMana) {
		Minecraft mc = Minecraft.getInstance();
		int width = 182;
		int x = mc.getWindow().getScaledWidth() / 2 - width / 2;
		int y = mc.getWindow().getScaledHeight() - ConfigHandler.CLIENT.manaBarHeight.get();

		if(!hasCreative) {
			if(totalMaxMana == 0)
				width = 0;
			else width *= (double) totalMana / (double) totalMaxMana;
		}

		if(width == 0) {
			if(totalMana > 0)
				width = 1;
			else return;
		}

		Color color = new Color(Color.HSBtoRGB(0.55F, (float) Math.min(1F, Math.sin(Util.milliTime() / 200D) * 0.5 + 1F), 1F));
		RenderSystem.color4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1 - (color.getRed() / 255F));
		mc.textureManager.bindTexture(manaBar);

		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderHelper.drawTexturedModalRect(x, y, 0, 0, 251, width, 5);
		RenderSystem.disableBlend();
		RenderSystem.color4f(1, 1, 1, 1);
	}

	private static void renderPoolRecipeHUD(TilePool tile, ItemStack stack) {
		Minecraft mc = Minecraft.getInstance();
		IProfiler profiler = mc.getProfiler();

		profiler.startSection("poolRecipe");
		RecipeManaInfusion recipe = TilePool.getMatchingRecipe(stack, tile.getWorld().getBlockState(tile.getPos().down()));
		if(recipe != null) {
			int x = mc.getWindow().getScaledWidth() / 2 - 11;
			int y = mc.getWindow().getScaledHeight() / 2 + 10;

			int u = tile.getCurrentMana() >= recipe.getManaToConsume() ? 0 : 22;
			int v = mc.player.getName().getString().equals("haighyorkie") && mc.player.isSneaking() ? 23 : 8;

			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			mc.textureManager.bindTexture(manaBar);
			RenderHelper.drawTexturedModalRect(x, y, 0, u, v, 22, 15);
			RenderSystem.color4f(1F, 1F, 1F, 1F);

			mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, x - 20, y);
			mc.getItemRenderer().renderItemAndEffectIntoGUI(recipe.getOutput(), x + 26, y);
			mc.getItemRenderer().renderItemOverlays(mc.fontRenderer, recipe.getOutput(), x + 26, y);

			RenderSystem.disableLighting();
			RenderSystem.disableBlend();
		}
		profiler.endSection();
	}

	private static void renderCrystalCubeHUD(TileCorporeaCrystalCube tile) {
		Minecraft mc = Minecraft.getInstance();
		IProfiler profiler = mc.getProfiler();

		profiler.startSection("crystalCube");
		ItemStack target = tile.getRequestTarget();
		if(!target.isEmpty()) {
			String s1 = target.getDisplayName().getString();
			String s2 = tile.getItemCount() + "x";
			int strlen = Math.max(mc.fontRenderer.getStringWidth(s1), mc.fontRenderer.getStringWidth(s2));
			int w = mc.getWindow().getScaledWidth();
			int h = mc.getWindow().getScaledHeight();
			int boxH = h / 2 + (tile.locked ? 20 : 10);
			AbstractGui.fill(w / 2 + 8, h / 2 - 12, w / 2 + strlen + 32, boxH, 0x44000000);
			AbstractGui.fill(w / 2 + 6, h / 2 - 14, w / 2 + strlen + 34, boxH + 2, 0x44000000);

			mc.fontRenderer.drawStringWithShadow(s1, w / 2 + 30, h / 2 - 10, 0x6666FF);
			mc.fontRenderer.drawStringWithShadow(tile.getItemCount() + "x", w / 2 + 30, h / 2, 0xFFFFFF);
			if(tile.locked)
				mc.fontRenderer.drawStringWithShadow(I18n.format("botaniamisc.locked"), w / 2 + 30, h / 2 + 10, 0xFFAA00);
			RenderSystem.enableRescaleNormal();
			mc.getItemRenderer().renderItemAndEffectIntoGUI(target, w / 2 + 10, h / 2 - 10);
		}

		profiler.endSection();
	}

	private static void renderNearIndexDisplay() {
		Minecraft mc = Minecraft.getInstance();
		String txt0 = I18n.format("botaniamisc.nearIndex0");
		String txt1 = TextFormatting.GRAY + I18n.format("botaniamisc.nearIndex1");
		String txt2 = TextFormatting.GRAY + I18n.format("botaniamisc.nearIndex2");

		int l = Math.max(mc.fontRenderer.getStringWidth(txt0), Math.max(mc.fontRenderer.getStringWidth(txt1), mc.fontRenderer.getStringWidth(txt2))) + 20;
		int x = mc.getWindow().getScaledWidth() - l - 20;
		int y = mc.getWindow().getScaledHeight() - 60;

		AbstractGui.fill(x - 6, y - 6, x + l + 6, y + 37, 0x44000000);
		AbstractGui.fill(x - 4, y - 4, x + l + 4, y + 35, 0x44000000);
		RenderSystem.enableRescaleNormal();
		mc.getItemRenderer().renderItemAndEffectIntoGUI(new ItemStack(ModBlocks.corporeaIndex), x, y + 10);

		mc.fontRenderer.drawStringWithShadow(txt0, x + 20, y, 0xFFFFFF);
		mc.fontRenderer.drawStringWithShadow(txt1, x + 20, y + 14, 0xFFFFFF);
		mc.fontRenderer.drawStringWithShadow(txt2, x + 20, y + 24, 0xFFFFFF);
	}

	public static void drawSimpleManaHUD(int color, int mana, int maxMana, String name) {
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Minecraft mc = Minecraft.getInstance();
		int x = mc.getWindow().getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(name) / 2;
		int y = mc.getWindow().getScaledHeight() / 2 + 10;

		mc.fontRenderer.drawStringWithShadow(name, x, y, color);

		x = mc.getWindow().getScaledWidth() / 2 - 51;
		y += 10;

		renderManaBar(x, y, color, mana < 0 ? 0.5F : 1F, mana, maxMana);

		if(mana < 0) {
			String text = I18n.format("botaniamisc.statusUnknown");
			x = mc.getWindow().getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(text) / 2;
			y -= 1;
			mc.fontRenderer.drawString(text, x, y, color);
		}

		RenderSystem.disableBlend();
	}

	public static void drawComplexManaHUD(int color, int mana, int maxMana, String name, ItemStack bindDisplay, boolean properlyBound) {
		drawSimpleManaHUD(color, mana, maxMana, name);

		Minecraft mc = Minecraft.getInstance();

		int x = mc.getWindow().getScaledWidth() / 2 + 55;
		int y = mc.getWindow().getScaledHeight() / 2 + 12;

		RenderSystem.enableRescaleNormal();
		mc.getItemRenderer().renderItemAndEffectIntoGUI(bindDisplay, x, y);

		RenderSystem.disableDepthTest();
		if(properlyBound) {
			mc.fontRenderer.drawStringWithShadow("\u2714", x + 10, y + 9, 0x004C00);
			mc.fontRenderer.drawStringWithShadow("\u2714", x + 10, y + 8, 0x0BD20D);
		} else {
			mc.fontRenderer.drawStringWithShadow("\u2718", x + 10, y + 9, 0x4C0000);
			mc.fontRenderer.drawStringWithShadow("\u2718", x + 10, y + 8, 0xD2080D);
		}
		RenderSystem.enableDepthTest();
	}

	public static void renderManaBar(int x, int y, int color, float alpha, int mana, int maxMana) {
		Minecraft mc = Minecraft.getInstance();

		RenderSystem.color4f(1F, 1F, 1F, alpha);
		mc.textureManager.bindTexture(manaBar);
		RenderHelper.drawTexturedModalRect(x, y, 0, 0, 0, 102, 5);

		int manaPercentage = Math.max(0, (int) ((double) mana / (double) maxMana * 100));

		if(manaPercentage == 0 && mana > 0)
			manaPercentage = 1;

		RenderHelper.drawTexturedModalRect(x + 1, y + 1, 0, 0, 5, 100, 3);

		Color color_ = new Color(color);
		RenderSystem.color4f(color_.getRed() / 255F, color_.getGreen() / 255F, color_.getBlue() / 255F, alpha);
		RenderHelper.drawTexturedModalRect(x + 1, y + 1, 0, 0, 5, Math.min(100, manaPercentage), 3);
		RenderSystem.color4f(1, 1, 1, 1);
	}
}
