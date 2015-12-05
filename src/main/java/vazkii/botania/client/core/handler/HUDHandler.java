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

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ILexicon;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.ICreativeManaProvider;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wiki.IWikiProvider;
import vazkii.botania.api.wiki.WikiHooks;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileAltar;
import vazkii.botania.common.block.tile.TileRuneAltar;
import vazkii.botania.common.block.tile.corporea.TileCorporeaCrystalCube;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ItemCraftingHalo;
import vazkii.botania.common.item.ItemSextant;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.item.equipment.bauble.ItemMonocle;
import vazkii.botania.common.lib.LibObfuscation;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;

public final class HUDHandler {

	public static final ResourceLocation manaBar = new ResourceLocation(LibResources.GUI_MANA_HUD);

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onDrawScreenPre(RenderGameOverlayEvent.Pre event) {
		Minecraft mc = Minecraft.getMinecraft();
		Profiler profiler = mc.mcProfiler;

		if(event.type == ElementType.HEALTH) {
			profiler.startSection("botania-hud");
			ItemStack amulet = PlayerHandler.getPlayerBaubles(mc.thePlayer).getStackInSlot(0);
			if(amulet != null && amulet.getItem() == ModItems.flightTiara) {
				profiler.startSection("flugelTiara");
				ItemFlightTiara.renderHUD(event.resolution, mc.thePlayer, amulet);
				profiler.endSection();
			}
			profiler.endSection();
		}
	}

	@SubscribeEvent
	public void onDrawScreenPost(RenderGameOverlayEvent.Post event) {
		Minecraft mc = Minecraft.getMinecraft();
		Profiler profiler = mc.mcProfiler;
		ItemStack equippedStack = mc.thePlayer.getCurrentEquippedItem();

		if(event.type == ElementType.ALL) {
			profiler.startSection("botania-hud");
			MovingObjectPosition pos = mc.objectMouseOver;

			if(pos != null) {
				Block block = mc.theWorld.getBlock(pos.blockX, pos.blockY, pos.blockZ);
				TileEntity tile = mc.theWorld.getTileEntity(pos.blockX, pos.blockY, pos.blockZ);

				if(equippedStack != null) {
					if(pos != null && equippedStack.getItem() == ModItems.twigWand) {
						renderWandModeDisplay(event.resolution);

						if(block instanceof IWandHUD) {
							profiler.startSection("wandItem");
							((IWandHUD) block).renderHUD(mc, event.resolution, mc.theWorld, pos.blockX, pos.blockY, pos.blockZ);
							profiler.endSection();
						}
					} else if(pos != null && equippedStack.getItem() instanceof ILexicon)
						drawLexiconHUD(mc.thePlayer.getCurrentEquippedItem(), block, pos, event.resolution);
					if(tile != null && tile instanceof TilePool)
						renderPoolRecipeHUD(event.resolution, (TilePool) tile, equippedStack);
				}
				if(tile != null && tile instanceof TileAltar)
					((TileAltar) tile).renderHUD(mc, event.resolution);
				else if(tile != null && tile instanceof TileRuneAltar)
					((TileRuneAltar) tile).renderHUD(mc, event.resolution);

				if(tile != null && tile instanceof TileCorporeaCrystalCube)
					renderCrystalCubeHUD(event.resolution, (TileCorporeaCrystalCube) tile);
			}

			if(!TileCorporeaIndex.getInputHandler().getNearbyIndexes(mc.thePlayer).isEmpty() && mc.currentScreen != null && mc.currentScreen instanceof GuiChat) {
				profiler.startSection("nearIndex");
				renderNearIndexDisplay(event.resolution);
				profiler.endSection();
			}

			if(MultiblockRenderHandler.currentMultiblock != null && MultiblockRenderHandler.anchor == null) {
				profiler.startSection("multiblockRightClick");
				String s = StatCollector.translateToLocal("botaniamisc.rightClickToAnchor");
				mc.fontRenderer.drawStringWithShadow(s, event.resolution.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(s) / 2, event.resolution.getScaledHeight() / 2 - 30, 0xFFFFFF);
				profiler.endSection();
			}

			if(equippedStack != null && equippedStack.getItem() instanceof ItemCraftingHalo) {
				profiler.startSection("craftingHalo");
				ItemCraftingHalo.renderHUD(event.resolution, mc.thePlayer, equippedStack);
				profiler.endSection();
			}

			if(equippedStack != null && equippedStack.getItem() instanceof ItemSextant) {
				profiler.startSection("sextant");
				ItemSextant.renderHUD(event.resolution, mc.thePlayer, equippedStack);
				profiler.endSection();
			}

			/*if(equippedStack != null && equippedStack.getItem() == ModItems.flugelEye) {
				profiler.startSection("flugelEye");
				ItemFlugelEye.renderHUD(event.resolution, mc.thePlayer, equippedStack);
				profiler.endSection();
			}*/

			if(Botania.proxy.isClientPlayerWearingMonocle()) {
				profiler.startSection("monocle");
				ItemMonocle.renderHUD(event.resolution, mc.thePlayer);
				profiler.endSection();
			}

			profiler.startSection("manaBar");
			EntityPlayer player = mc.thePlayer;
			int totalMana = 0;
			int totalMaxMana = 0;
			boolean anyRequest = false;
			boolean creative = false;

			IInventory mainInv = player.inventory;
			IInventory baublesInv = PlayerHandler.getPlayerBaubles(player);

			int invSize = mainInv.getSizeInventory();
			int size = invSize;
			if(baublesInv != null)
				size += baublesInv.getSizeInventory();

			for(int i = 0; i < size; i++) {
				boolean useBaubles = i >= invSize;
				IInventory inv = useBaubles ? baublesInv : mainInv;
				ItemStack stack = inv.getStackInSlot(i - (useBaubles ? invSize : 0));

				if(stack != null) {
					Item item = stack.getItem();
					if(item instanceof IManaUsingItem)
						anyRequest = anyRequest || ((IManaUsingItem) item).usesMana(stack);

					if(item instanceof IManaItem) {
						if(!((IManaItem) item).isNoExport(stack)) {
							totalMana += ((IManaItem) item).getMana(stack);
							totalMaxMana += ((IManaItem) item).getMaxMana(stack);
						}
					}

					if(item instanceof ICreativeManaProvider && ((ICreativeManaProvider) item).isCreative(stack))
						creative = true;
				}
			}

			if(anyRequest)
				renderManaInvBar(event.resolution, creative, totalMana, totalMaxMana);

			profiler.endStartSection("bossBar");
			BossBarHandler.render(event.resolution);
			profiler.endStartSection("itemsRemaining");
			ItemsRemainingRenderHandler.render(event.resolution, event.partialTicks);
			profiler.endSection();
			profiler.endSection();

			GL11.glColor4f(1F, 1F, 1F, 1F);
		}
	}

	private void renderWandModeDisplay(ScaledResolution res) {
		Minecraft mc = Minecraft.getMinecraft();
		Profiler profiler = mc.mcProfiler;

		profiler.startSection("wandMode");
		int ticks = ReflectionHelper.getPrivateValue(GuiIngame.class, mc.ingameGUI, LibObfuscation.REMAINING_HIGHLIGHT_TICKS);
		ticks -= 15;
		if(ticks > 0) {
			int alpha = Math.min(255, (int) (ticks * 256.0F / 10.0F));
			int color = 0x00CC00 + (alpha << 24);
			String disp = StatCollector.translateToLocal(ItemTwigWand.getModeString(mc.thePlayer.getCurrentEquippedItem()));

			int x = res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(disp) / 2;
			int y = res.getScaledHeight() - 70;

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			mc.fontRenderer.drawStringWithShadow(disp, x, y, color);
			GL11.glDisable(GL11.GL_BLEND);
		}
		profiler.endSection();
	}

	private void renderManaInvBar(ScaledResolution res, boolean hasCreative, int totalMana, int totalMaxMana) {
		Minecraft mc = Minecraft.getMinecraft();
		int width = 182;
		int x = res.getScaledWidth() / 2 - width / 2;
		int y = res.getScaledHeight() - ConfigHandler.manaBarHeight;

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

		Color color = new Color(Color.HSBtoRGB(0.55F, (float) Math.min(1F, Math.sin(System.currentTimeMillis() / 200D) * 0.5 + 1F), 1F));
		GL11.glColor4ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue(), (byte) (255 - color.getRed()));
		mc.renderEngine.bindTexture(manaBar);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderHelper.drawTexturedModalRect(x, y, 0, 0, 251, width, 5);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void renderPoolRecipeHUD(ScaledResolution res, TilePool tile, ItemStack stack) {
		Minecraft mc = Minecraft.getMinecraft();
		Profiler profiler = mc.mcProfiler;

		profiler.startSection("poolRecipe");
		for(RecipeManaInfusion recipe : BotaniaAPI.manaInfusionRecipes) {
			if(recipe.matches(stack)) {
				if((!recipe.isAlchemy() || tile.alchemy) && (!recipe.isConjuration() || tile.conjuration)) {
					int x = res.getScaledWidth() / 2 - 11;
					int y = res.getScaledHeight() / 2 + 10;

					int u = tile.getCurrentMana() >= recipe.getManaToConsume() ? 0 : 22;
					int v = mc.thePlayer.getCommandSenderName().equals("haighyorkie") && mc.thePlayer.isSneaking() ? 23 : 8;

					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

					mc.renderEngine.bindTexture(manaBar);
					RenderHelper.drawTexturedModalRect(x, y, 0, u, v, 22, 15);
					GL11.glColor4f(1F, 1F, 1F, 1F);

					net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
					RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, stack, x - 20, y);
					RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, recipe.getOutput(), x + 26, y);
					RenderItem.getInstance().renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, recipe.getOutput(), x + 26, y);
					net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

					GL11.glDisable(GL11.GL_LIGHTING);
					GL11.glDisable(GL11.GL_BLEND);

					break;
				}
			}
		}
		profiler.endSection();
	}

	private void renderCrystalCubeHUD(ScaledResolution res, TileCorporeaCrystalCube tile) {
		Minecraft mc = Minecraft.getMinecraft();
		Profiler profiler = mc.mcProfiler;

		profiler.startSection("crystalCube");
		ItemStack target = tile.getRequestTarget();
		if(target != null) {
			String s1 = target.getDisplayName();
			String s2 = tile.getItemCount() + "x";
			int strlen = Math.max(mc.fontRenderer.getStringWidth(s1), mc.fontRenderer.getStringWidth(s2));
			int w = res.getScaledWidth();
			int h = res.getScaledHeight();
			Gui.drawRect(w / 2 + 8, h / 2 - 12, w / 2 + strlen + 32, h / 2 + 10, 0x44000000);
			Gui.drawRect(w / 2 + 6, h / 2 - 14, w / 2 + strlen + 34, h / 2 + 12, 0x44000000);

			mc.fontRenderer.drawStringWithShadow(target.getDisplayName(), w / 2 + 30, h / 2 - 10, 0x6666FF);
			mc.fontRenderer.drawStringWithShadow(tile.getItemCount() + "x", w / 2 + 30, h / 2, 0xFFFFFF);
			net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, target, w / 2 + 10, h / 2 - 10);
			net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
		}

		profiler.endSection();
	}

	private void drawLexiconHUD(ItemStack stack, Block block, MovingObjectPosition pos, ScaledResolution res) {
		Minecraft mc = Minecraft.getMinecraft();
		Profiler profiler = mc.mcProfiler;

		profiler.startSection("lexicon");
		FontRenderer font = mc.fontRenderer;
		boolean draw = false;
		String drawStr = "";
		String secondLine = "";

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		int sx = res.getScaledWidth() / 2 - 17;
		int sy = res.getScaledHeight() / 2 + 2;

		if(block instanceof ILexiconable) {
			LexiconEntry entry = ((ILexiconable) block).getEntry(mc.theWorld, pos.blockX, pos.blockY, pos.blockZ, mc.thePlayer, mc.thePlayer.getCurrentEquippedItem());
			if(entry != null) {
				if(!((ILexicon) stack.getItem()).isKnowledgeUnlocked(stack, entry.getKnowledgeType()))
					font = mc.standardGalacticFontRenderer;

				drawStr = StatCollector.translateToLocal(entry.getUnlocalizedName());
				secondLine = EnumChatFormatting.ITALIC + StatCollector.translateToLocal(entry.getTagline());
				draw = true;
			}
		}

		if(!draw && pos.entityHit == null) {
			profiler.startSection("wikiLookup");
			if(!block.isAir(mc.theWorld, pos.blockX, pos.blockY, pos.blockZ) && !(block instanceof BlockLiquid)) {
				IWikiProvider provider = WikiHooks.getWikiFor(block);
				String url = provider.getWikiURL(mc.theWorld, pos);
				if(url != null && !url.isEmpty()) {
					String name = provider.getBlockName(mc.theWorld, pos);
					String wikiName = provider.getWikiName(mc.theWorld, pos);
					drawStr = name + " @ " + EnumChatFormatting.AQUA + wikiName;
					draw = true;
				}
			}
			profiler.endSection();
		}

		if(draw) {
			if(!mc.thePlayer.isSneaking()) {
				drawStr = "?";
				secondLine = "";
				font = mc.fontRenderer;
			}

			RenderItem.getInstance().renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack(ModItems.lexicon), sx, sy);
			GL11.glDisable(GL11.GL_LIGHTING);
			font.drawStringWithShadow(drawStr, sx + 10, sy + 8, 0xFFFFFFFF);
			font.drawStringWithShadow(secondLine, sx + 10, sy + 18, 0xFFAAAAAA);

			if(!mc.thePlayer.isSneaking()) {
				GL11.glScalef(0.5F, 0.5F, 1F);
				mc.fontRenderer.drawStringWithShadow(EnumChatFormatting.BOLD + "Shift", (sx + 10) * 2 - 16, (sy + 8) * 2 + 20, 0xFFFFFFFF);
				GL11.glScalef(2F, 2F, 1F);
			}
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		profiler.endSection();
	}

	private void renderNearIndexDisplay(ScaledResolution res) {
		Minecraft mc = Minecraft.getMinecraft();
		String txt0 = StatCollector.translateToLocal("botaniamisc.nearIndex0");
		String txt1 = EnumChatFormatting.GRAY + StatCollector.translateToLocal("botaniamisc.nearIndex1");
		String txt2 = EnumChatFormatting.GRAY + StatCollector.translateToLocal("botaniamisc.nearIndex2");

		int l = Math.max(mc.fontRenderer.getStringWidth(txt0), Math.max(mc.fontRenderer.getStringWidth(txt1), mc.fontRenderer.getStringWidth(txt2))) + 20;
		int x = res.getScaledWidth() - l - 20;
		int y = res.getScaledHeight() - 60;

		Gui.drawRect(x - 6, y - 6, x + l + 6, y + 37, 0x44000000);
		Gui.drawRect(x - 4, y - 4, x + l + 4, y + 35, 0x44000000);
		net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack(ModBlocks.corporeaIndex), x, y + 10);
		net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

		mc.fontRenderer.drawStringWithShadow(txt0, x + 20, y, 0xFFFFFF);
		mc.fontRenderer.drawStringWithShadow(txt1, x + 20, y + 14, 0xFFFFFF);
		mc.fontRenderer.drawStringWithShadow(txt2, x + 20, y + 24, 0xFFFFFF);
	}

	public static void drawSimpleManaHUD(int color, int mana, int maxMana, String name, ScaledResolution res) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Minecraft mc = Minecraft.getMinecraft();
		int x = res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(name) / 2;
		int y = res.getScaledHeight() / 2 + 10;

		mc.fontRenderer.drawStringWithShadow(name, x, y, color);

		x = res.getScaledWidth() / 2 - 51;
		y += 10;

		renderManaBar(x, y, color, mana < 0 ? 0.5F : 1F, mana, maxMana);

		if(mana < 0) {
			String text = StatCollector.translateToLocal("botaniamisc.statusUnknown");
			x = res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(text) / 2;
			y -= 1;
			mc.fontRenderer.drawString(text, x, y, color);
		}

		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void drawComplexManaHUD(int color, int mana, int maxMana, String name, ScaledResolution res, ItemStack bindDisplay, boolean properlyBound) {
		drawSimpleManaHUD(color, mana, maxMana, name, res);

		Minecraft mc = Minecraft.getMinecraft();

		int x = res.getScaledWidth() / 2 + 55;
		int y = res.getScaledHeight() / 2 + 12;

		net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, bindDisplay, x, y);
		net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		if(properlyBound) {
			mc.fontRenderer.drawStringWithShadow("\u2714", x + 10, y + 9, 0x004C00);
			mc.fontRenderer.drawStringWithShadow("\u2714", x + 10, y + 8, 0x0BD20D);
		} else {
			mc.fontRenderer.drawStringWithShadow("\u2718", x + 10, y + 9, 0x4C0000);
			mc.fontRenderer.drawStringWithShadow("\u2718", x + 10, y + 8, 0xD2080D);
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public static void renderManaBar(int x, int y, int color, float alpha, int mana, int maxMana) {
		Minecraft mc = Minecraft.getMinecraft();

		GL11.glColor4f(1F, 1F, 1F, alpha);
		mc.renderEngine.bindTexture(manaBar);
		RenderHelper.drawTexturedModalRect(x, y, 0, 0, 0, 102, 5);

		int manaPercentage = Math.max(0, (int) ((double) mana / (double) maxMana * 100));

		if(manaPercentage == 0 && mana > 0)
			manaPercentage = 1;

		RenderHelper.drawTexturedModalRect(x + 1, y + 1, 0, 0, 5, 100, 3);

		Color color_ = new Color(color);
		GL11.glColor4ub((byte) color_.getRed(), (byte) color_.getGreen(),(byte) color_.getBlue(), (byte) (255F * alpha));
		RenderHelper.drawTexturedModalRect(x + 1, y + 1, 0, 0, 5, Math.min(100, manaPercentage), 3);
	}
}
