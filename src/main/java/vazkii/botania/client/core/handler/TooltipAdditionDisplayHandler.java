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
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
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
import vazkii.botania.common.lib.LibMisc;

import java.awt.Color;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = LibMisc.MOD_ID)
public final class TooltipAdditionDisplayHandler {

	private static float lexiconLookupTime = 0F;

	@SubscribeEvent
	public static void onToolTipRender(RenderTooltipEvent.PostText evt) {
		if(evt.getStack().isEmpty())
			return;

		ItemStack stack = evt.getStack();
		Minecraft mc = Minecraft.getMinecraft();
		int width = evt.getWidth();
		int height = 3;
		int tooltipX = evt.getX();
		int tooltipY = evt.getY() - 4;
		FontRenderer font = evt.getFontRenderer();

		if(stack.getItem() instanceof ItemTerraPick)
			drawTerraPick(stack, tooltipX, tooltipY, width, height, font);
		else if(stack.getItem() instanceof IManaTooltipDisplay)
			drawManaBar(stack, (IManaTooltipDisplay) stack.getItem(), tooltipX, tooltipY, width, height);

		EntryData data = LexiconRecipeMappings.getDataForStack(stack);
		if(mc.player != null && data != null) {
			int lexSlot = -1;
			ItemStack lexiconStack = ItemStack.EMPTY;

			for(int i = 0; i < InventoryPlayer.getHotbarSize(); i++) {
				ItemStack stackAt = mc.player.inventory.getStackInSlot(i);
				if(!stackAt.isEmpty() && stackAt.getItem() instanceof ILexicon && ((ILexicon) stackAt.getItem()).isKnowledgeUnlocked(stackAt, data.entry.getKnowledgeType())) {
					lexiconStack = stackAt;
					lexSlot = i;
					break;
				}
			}

			if(lexSlot > -1) {
				int x = tooltipX - 34;
				GlStateManager.disableDepth();

				Gui.drawRect(x - 4, tooltipY - 4, x + 20, tooltipY + 26, 0x44000000);
				Gui.drawRect(x - 6, tooltipY - 6, x + 22, tooltipY + 28, 0x44000000);

				if(ConfigHandler.useShiftForQuickLookup ? GuiScreen.isShiftKeyDown() : GuiScreen.isCtrlKeyDown()) {
					lexiconLookupTime += ClientTickHandler.delta;

					int cx = x + 8;
					int cy = tooltipY + 8;
					float r = 12;
					float time = 20F;
					float angles = lexiconLookupTime / time * 360F;

					GlStateManager.disableLighting();
					GlStateManager.disableTexture2D();
					GlStateManager.shadeModel(GL11.GL_SMOOTH);
					GlStateManager.enableBlend();
					GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

					BufferBuilder buf = Tessellator.getInstance().getBuffer();
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
						mc.player.inventory.currentItem = lexSlot;
						Botania.proxy.setEntryToOpen(data.entry);
						Botania.proxy.setLexiconStack(lexiconStack);
						mc.player.closeScreen();
						ItemLexicon.openBook(mc.player, lexiconStack, mc.world, false);

					}
				} else lexiconLookupTime = 0F;

				mc.getRenderItem().renderItemIntoGUI(new ItemStack(ModItems.lexicon), x, tooltipY);
				GlStateManager.disableLighting();

				font.drawStringWithShadow("?", x + 10, tooltipY + 8, 0xFFFFFFFF);
				GlStateManager.scale(0.5F, 0.5F, 1F);
				boolean mac = Minecraft.IS_RUNNING_ON_MAC;

				mc.fontRenderer.drawStringWithShadow(TextFormatting.BOLD + (ConfigHandler.useShiftForQuickLookup ? "Shift" : mac ? "Cmd" : "Ctrl"), (x + 10) * 2 - 16, (tooltipY + 8) * 2 + 20, 0xFFFFFFFF);
				GlStateManager.scale(2F, 2F, 1F);


				GlStateManager.enableDepth();
			} else lexiconLookupTime = 0F;
		} else lexiconLookupTime = 0F;
	}

	private static void drawTerraPick(ItemStack stack, int mouseX, int mouseY, int width, int height, FontRenderer font) {
		int level = ItemTerraPick.getLevel(stack);
		int max = ItemTerraPick.LEVELS[Math.min(ItemTerraPick.LEVELS.length - 1, level + 1)];
		boolean ss = level >= ItemTerraPick.LEVELS.length - 1;
		int curr = ItemTerraPick.getMana_(stack);
		float percent = level == 0 ? 0F : (float) curr / (float) max;
		int rainbowWidth = Math.min(width - (ss ? 0 : 1), (int) (width * percent));
		float huePer = width == 0 ? 0F : 1F / width;
		float hueOff = (ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.01F;

		GlStateManager.disableDepth();
		Gui.drawRect(mouseX - 1, mouseY - height - 1, mouseX + width + 1, mouseY, 0xFF000000);
		for(int i = 0; i < rainbowWidth; i++)
			Gui.drawRect(mouseX + i, mouseY - height, mouseX + i + 1, mouseY, Color.HSBtoRGB(hueOff + huePer * i, 1F, 1F));
		Gui.drawRect(mouseX + rainbowWidth, mouseY - height, mouseX + width, mouseY, 0xFF555555);

		String rank = I18n.format("botania.rank" + level).replaceAll("&", "\u00a7");

		GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
		GlStateManager.disableLighting();
		font.drawStringWithShadow(rank, mouseX, mouseY - 12, 0xFFFFFF);
		if(!ss) {
			rank = I18n.format("botania.rank" + (level + 1)).replaceAll("&", "\u00a7");
			font.drawStringWithShadow(rank, mouseX + width - font.getStringWidth(rank), mouseY - 12, 0xFFFFFF);
		}
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		GL11.glPopAttrib();
	}

	private static void drawManaBar(ItemStack stack, IManaTooltipDisplay display, int mouseX, int mouseY, int width, int height) {
		float fraction = display.getManaFractionForDisplay(stack);
		int manaBarWidth = (int) Math.ceil(width * fraction);

		GlStateManager.disableDepth();
		Gui.drawRect(mouseX - 1, mouseY - height - 1, mouseX + width + 1, mouseY, 0xFF000000);
		Gui.drawRect(mouseX, mouseY - height, mouseX + manaBarWidth, mouseY, Color.HSBtoRGB(0.528F, ((float) Math.sin((ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.2) + 1F) * 0.3F + 0.4F, 1F));
		Gui.drawRect(mouseX + manaBarWidth, mouseY - height, mouseX + width, mouseY, 0xFF555555);
	}

}
