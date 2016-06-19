/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 23, 2015, 4:24:56 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.lexicon.ILexicon;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.api.lexicon.LexiconRecipeMappings.EntryData;
import vazkii.botania.api.mana.IManaTooltipDisplay;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ItemLexicon;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;
import vazkii.botania.common.lib.LibObfuscation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class TooltipAdditionDisplayHandler {

	private static float lexiconLookupTime = 0F;

	public static void render() {
		Minecraft mc = Minecraft.getMinecraft();
		GuiScreen gui = mc.currentScreen;
		if(gui != null && gui instanceof GuiContainer && mc.thePlayer != null && mc.thePlayer.inventory.getItemStack() == null) {
			GuiContainer container = (GuiContainer) gui;
			Slot slot = container.getSlotUnderMouse();
			if(slot != null && slot.getHasStack()) {
				ItemStack stack = slot.getStack();
				if(stack != null) {
					ScaledResolution res = new ScaledResolution(mc);
					FontRenderer font = mc.fontRendererObj;
					int mouseX = Mouse.getX() * res.getScaledWidth() / mc.displayWidth;
					int mouseY = res.getScaledHeight() - Mouse.getY() * res.getScaledHeight() / mc.displayHeight;

					List<String> tooltip;
					try {
						tooltip = stack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);
					} catch(Exception e) {
						tooltip = new ArrayList<>();
					}
					int width = 0;
					for(String s : tooltip)
						width = Math.max(width, font.getStringWidth(s) + 2);
					int tooltipHeight = (tooltip.size() - 1) * 10 + 5;

					int height = 3;
					int offx = 11;
					int offy = 17;

					boolean offscreen = mouseX + width + 19 >= res.getScaledWidth();

					int fixY = res.getScaledHeight() - (mouseY + tooltipHeight);
					if(fixY < 0)
						offy -= fixY;
					if(offscreen)
						offx = -13 - width;

					if(stack.getItem() instanceof ItemTerraPick)
						drawTerraPick(stack, mouseX, mouseY, offx, offy, width, height, font);
					else if(stack.getItem() instanceof IManaTooltipDisplay)
						drawManaBar(stack, (IManaTooltipDisplay) stack.getItem(), mouseX, mouseY, offx, offy, width, height);

					EntryData data = LexiconRecipeMappings.getDataForStack(stack);
					if(data != null) {
						int lexSlot = -1;
						ItemStack lexiconStack = null;

						for(int i = 0; i < InventoryPlayer.getHotbarSize(); i++) {
							ItemStack stackAt = mc.thePlayer.inventory.getStackInSlot(i);
							if(stackAt != null && stackAt.getItem() instanceof ILexicon && ((ILexicon) stackAt.getItem()).isKnowledgeUnlocked(stackAt, data.entry.getKnowledgeType())) {
								lexiconStack = stackAt;
								lexSlot = i;
								break;
							}
						}

						if(lexSlot > -1) {
							int x = mouseX + offx - 34;
							int y = mouseY - offy;
							GlStateManager.disableDepth();

							Gui.drawRect(x - 4, y - 4, x + 20, y + 26, 0x44000000);
							Gui.drawRect(x - 6, y - 6, x + 22, y + 28, 0x44000000);

							if(ConfigHandler.useShiftForQuickLookup ? GuiScreen.isShiftKeyDown() : GuiScreen.isCtrlKeyDown()) {
								lexiconLookupTime += ClientTickHandler.delta;

								int cx = x + 8;
								int cy = y + 8;
								float r = 12;
								float time = 20F;
								float angles = lexiconLookupTime / time * 360F;

								GlStateManager.disableLighting();
								GlStateManager.disableTexture2D();
								GlStateManager.shadeModel(GL11.GL_SMOOTH);
								GlStateManager.enableBlend();
								GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

								VertexBuffer buf = Tessellator.getInstance().getBuffer();
								buf.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);

								float a = 0.5F + 0.2F * ((float) Math.cos((double) (ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) / 10) * 0.5F + 0.5F);
								buf.pos(cx, cy, 0).color(0F, 0.5F, 0F, a).endVertex();

								for(float i = angles; i > 0; i--) {
									double rad = (i - 90) / 180F * Math.PI;
									buf.pos(cx + Math.cos(rad) * r, cy + Math.sin(rad) * r, 0).color(0F, 1F, 0F, 1F).endVertex();
								}
								
								buf.pos(cx, cy, 0).color(0F, 1F, 0F, 0F).endVertex();
								Tessellator.getInstance().draw();

								GlStateManager.disableBlend();
								GlStateManager.enableTexture2D();
								GlStateManager.shadeModel(GL11.GL_FLAT);

								if(lexiconLookupTime >= time) {
									mc.thePlayer.inventory.currentItem = lexSlot;
									Botania.proxy.setEntryToOpen(data.entry);
									Botania.proxy.setLexiconStack(lexiconStack);
									mc.thePlayer.closeScreen();
									ItemLexicon.openBook(mc.thePlayer, lexiconStack, mc.theWorld, false);

								}
							} else lexiconLookupTime = 0F;

							mc.getRenderItem().renderItemIntoGUI(new ItemStack(ModItems.lexicon), x, y);
							GlStateManager.disableLighting();

							font.drawStringWithShadow("?", x + 10, y + 8, 0xFFFFFFFF);
							GlStateManager.scale(0.5F, 0.5F, 1F);
							boolean mac = Minecraft.IS_RUNNING_ON_MAC;

							mc.fontRendererObj.drawStringWithShadow(TextFormatting.BOLD + (ConfigHandler.useShiftForQuickLookup ? "Shift" : mac ? "Cmd" : "Ctrl"), (x + 10) * 2 - 16, (y + 8) * 2 + 20, 0xFFFFFFFF);
							GlStateManager.scale(2F, 2F, 1F);


							GlStateManager.enableDepth();
						} else lexiconLookupTime = 0F;
					} else lexiconLookupTime = 0F;
				} else lexiconLookupTime = 0F;
			} else lexiconLookupTime = 0F;
		} else lexiconLookupTime = 0F;
	}

	private static void drawTerraPick(ItemStack stack, int mouseX, int mouseY, int offx, int offy, int width, int height, FontRenderer font) {
		int level = ItemTerraPick.getLevel(stack);
		int max = ItemTerraPick.LEVELS[Math.min(ItemTerraPick.LEVELS.length - 1, level + 1)];
		boolean ss = level >= ItemTerraPick.LEVELS.length - 1;
		int curr = ItemTerraPick.getMana_(stack);
		float percent = level == 0 ? 0F : (float) curr / (float) max;
		int rainbowWidth = Math.min(width - (ss ? 0 : 1), (int) (width * percent));
		float huePer = width == 0 ? 0F : 1F / width;
		float hueOff = (ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.01F;

		GlStateManager.disableDepth();
		Gui.drawRect(mouseX + offx - 1, mouseY - offy - height - 1, mouseX + offx + width + 1, mouseY - offy, 0xFF000000);
		for(int i = 0; i < rainbowWidth; i++)
			Gui.drawRect(mouseX + offx + i, mouseY - offy - height, mouseX + offx + i + 1, mouseY - offy, Color.HSBtoRGB(hueOff + huePer * i, 1F, 1F));
		Gui.drawRect(mouseX + offx + rainbowWidth, mouseY - offy - height, mouseX + offx + width, mouseY - offy, 0xFF555555);

		String rank = I18n.format("botania.rank" + level).replaceAll("&", "\u00a7");

		GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
		GlStateManager.disableLighting();
		font.drawStringWithShadow(rank, mouseX + offx, mouseY - offy - 12, 0xFFFFFF);
		if(!ss) {
			rank = I18n.format("botania.rank" + (level + 1)).replaceAll("&", "\u00a7");
			font.drawStringWithShadow(rank, mouseX + offx + width - font.getStringWidth(rank), mouseY - offy - 12, 0xFFFFFF);
		}
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		GL11.glPopAttrib();
	}

	private static void drawManaBar(ItemStack stack, IManaTooltipDisplay display, int mouseX, int mouseY, int offx, int offy, int width, int height) {
		float fraction = display.getManaFractionForDisplay(stack);
		int manaBarWidth = (int) Math.ceil(width * fraction);

		GlStateManager.disableDepth();
		Gui.drawRect(mouseX + offx - 1, mouseY - offy - height - 1, mouseX + offx + width + 1, mouseY - offy, 0xFF000000);
		Gui.drawRect(mouseX + offx, mouseY - offy - height, mouseX + offx + manaBarWidth, mouseY - offy, Color.HSBtoRGB(0.528F, ((float) Math.sin((ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.2) + 1F) * 0.3F + 0.4F, 1F));
		Gui.drawRect(mouseX + offx + manaBarWidth, mouseY - offy - height, mouseX + offx + width, mouseY - offy, 0xFF555555);
	}

}
