/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BookModel;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderHandEvent;

import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ItemLexicon;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.mixin.AccessorFirstPersonRenderer;

import java.util.ArrayList;
import java.util.List;

// Hacky way to render 3D lexicon, will be reevaluated in the future.
public class RenderLexicon {
	private static final BookModel model = new BookModel();
	private static final boolean SHOULD_MISSPELL = Math.random() < 0.0004;
	public static final RenderMaterial TEXTURE = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, new ResourceLocation(LibResources.MODEL_LEXICA_DEFAULT));
	public static final RenderMaterial ELVEN_TEXTURE = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, new ResourceLocation(LibResources.MODEL_LEXICA_ELVEN));

	private static final String[] QUOTES = new String[] {
			"\"Neat!\" - Direwolf20",
			"\"It's pretty ledge.\" - Haighyorkie",
			"\"I don't really like it.\" - CrustyMustard",
			"\"It's a very thinky mod.\" - AdamG3691",
			"\"You must craft the tiny potato.\" - TheFractangle",
			"\"Vazkii did a thing.\" - cpw"
	};

	private static final String[] MISSPELLINGS = {
			"Bonito", "Bonita", "Bonitia", "Botnaia", "Bontonio",
			"Botnia", "Bonitaaaaaaaaaa", "Botonio", "Botonia",
			"Banana", "Brotania", "Botanica", "Boat", "Batania"
	};

	private static int quote = -1;
	private static int misspelling = -1;

	public static void renderHand(RenderHandEvent evt) {
		Minecraft mc = Minecraft.getInstance();
		if (!ConfigHandler.CLIENT.lexicon3dModel.get()
				|| mc.gameSettings.thirdPersonView != 0
				|| mc.player.getHeldItem(evt.getHand()).isEmpty()
				|| mc.player.getHeldItem(evt.getHand()).getItem() != ModItems.lexicon) {
			return;
		}
		evt.setCanceled(true);
		try {
			renderFirstPersonItem(mc.player, evt.getPartialTicks(), evt.getInterpolatedPitch(), evt.getHand(), evt.getSwingProgress(), evt.getItemStack(), evt.getEquipProgress(), evt.getMatrixStack(), evt.getBuffers(), evt.getLight());
		} catch (Throwable throwable) {
			Botania.LOGGER.warn("Failed to render lexicon", throwable);
		}
	}

	// [VanillaCopy] FirstPersonRenderer, irrelevant branches stripped out
	private static void renderFirstPersonItem(AbstractClientPlayerEntity player, float partialTicks, float pitch, Hand hand, float swingProgress, ItemStack stack, float equipProgress, MatrixStack ms, IRenderTypeBuffer buffers, int light) {
		boolean flag = hand == Hand.MAIN_HAND;
		HandSide handside = flag ? player.getPrimaryHand() : player.getPrimaryHand().opposite();
		ms.push();
		{
			boolean flag3 = handside == HandSide.RIGHT;
			{
				float f5 = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
				float f6 = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * ((float) Math.PI * 2F));
				float f10 = -0.2F * MathHelper.sin(swingProgress * (float) Math.PI);
				int l = flag3 ? 1 : -1;
				ms.translate((double) ((float) l * f5), (double) f6, (double) f10);
				((AccessorFirstPersonRenderer) Minecraft.getInstance().getFirstPersonRenderer()).callTransformSideFirstPerson(ms, handside, equipProgress);
				((AccessorFirstPersonRenderer) Minecraft.getInstance().getFirstPersonRenderer()).callTransformFirstPerson(ms, handside, swingProgress);
			}

			doRender(stack, handside, ms, buffers, light, partialTicks);
		}

		ms.pop();
	}

	private static void doRender(ItemStack stack, HandSide side, MatrixStack ms, IRenderTypeBuffer buffers, int light, float partialTicks) {
		Minecraft mc = Minecraft.getInstance();

		ms.push();

		float ticks = ClientTickHandler.ticksWithLexicaOpen;
		if (ticks > 0 && ticks < 10) {
			if (ItemLexicon.isOpen()) {
				ticks += partialTicks;
			} else {
				ticks -= partialTicks;
			}
		}

		if (side == HandSide.RIGHT) {
			ms.translate(0.3F + 0.02F * ticks, 0.125F + 0.01F * ticks, -0.2F - 0.035F * ticks);
			ms.rotate(Vector3f.YP.rotationDegrees(180F + ticks * 6));
		} else {
			ms.translate(0.1F - 0.02F * ticks, 0.125F + 0.01F * ticks, -0.2F - 0.035F * ticks);
			ms.rotate(Vector3f.YP.rotationDegrees(200F + ticks * 10));
		}
		ms.rotate(Vector3f.ZP.rotationDegrees(-0.3F + ticks * 2.85F));
		float opening = MathHelper.clamp(ticks / 12F, 0, 1);

		float pageFlipTicks = ClientTickHandler.pageFlipTicks;
		if (pageFlipTicks > 0) {
			pageFlipTicks -= ClientTickHandler.partialTicks;
		}

		float pageFlip = pageFlipTicks / 5F;

		float leftPageAngle = MathHelper.frac(pageFlip + 0.25F) * 1.6F - 0.3F;
		float rightPageAngle = MathHelper.frac(pageFlip + 0.75F) * 1.6F - 0.3F;
		model.func_228247_a_(ClientTickHandler.total, MathHelper.clamp(leftPageAngle, 0.0F, 1.0F), MathHelper.clamp(rightPageAngle, 0.0F, 1.0F), opening);

		RenderMaterial mat = ((ItemLexicon) ModItems.lexicon).isElvenItem(stack) ? ELVEN_TEXTURE : TEXTURE;
		IVertexBuilder buffer = mat.getBuffer(buffers, RenderType::getEntitySolid);
		model.render(ms, buffer, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

		if (ticks < 3) {
			FontRenderer font = Minecraft.getInstance().fontRenderer;
			ms.rotate(Vector3f.ZP.rotationDegrees(180F));
			ms.translate(-0.30F, -0.24F, -0.07F);
			ms.scale(0.0030F, 0.0030F, -0.0030F);

			if (misspelling == -1) {
				misspelling = mc.world.rand.nextInt(MISSPELLINGS.length);
			}

			String title = ItemLexicon.getTitle(stack).getString();
			if (SHOULD_MISSPELL) {
				title = title.replaceAll(LibMisc.MOD_NAME, MISSPELLINGS[misspelling]);
			}
			font.renderString(font.func_238412_a_(title, 80), 0, 0, 0xD69700, false, ms.getLast().getMatrix(), buffers, false, 0, light);

			ms.translate(0F, 10F, 0F);
			ms.scale(0.6F, 0.6F, 0.6F);
			ITextComponent edition = ItemLexicon.getEdition().deepCopy().func_240701_a_(TextFormatting.ITALIC, TextFormatting.BOLD);
			font.func_238416_a_(edition, 0, 0, 0xA07100, false, ms.getLast().getMatrix(), buffers, false, 0, light);

			if (quote == -1) {
				quote = mc.world.rand.nextInt(QUOTES.length);
			}

			String quoteStr = QUOTES[quote];

			ms.translate(-5F, 15F, 0F);
			renderText(0, 0, 140, 100, 0, 0x79ff92, quoteStr, ms.getLast().getMatrix(), buffers, light);

			ms.translate(8F, 110F, 0F);
			String blurb = I18n.format("botaniamisc.lexiconcover0");
			font.renderString(blurb, 0, 0, 0x79ff92, false, ms.getLast().getMatrix(), buffers, false, 0, light);

			ms.translate(0F, 10F, 0F);
			String blurb2 = TextFormatting.UNDERLINE + "" + TextFormatting.ITALIC + I18n.format("botaniamisc.lexiconcover1");
			font.renderString(blurb2, 0, 0, 0x79ff92, false, ms.getLast().getMatrix(), buffers, false, 0, light);

			ms.translate(0F, -30F, 0F);

			String authorTitle = I18n.format("botaniamisc.lexiconcover2");
			int len = font.getStringWidth(authorTitle);
			font.renderString(authorTitle, 58 - len / 2F, -8, 0xD69700, false, ms.getLast().getMatrix(), buffers, false, 0, light);
		}

		ms.pop();
	}

	private static void renderText(int x, int y, int width, int height, int paragraphSize, int color, String unlocalizedText, Matrix4f matrix, IRenderTypeBuffer buffers, int light) {
		x += 2;
		y += 10;
		width -= 4;

		FontRenderer font = Minecraft.getInstance().fontRenderer;
		String text = I18n.format(unlocalizedText).replaceAll("&", "\u00a7");
		String[] textEntries = text.split("<br>");

		List<List<String>> lines = new ArrayList<>();

		String controlCodes;
		for (String s : textEntries) {
			List<String> words = new ArrayList<>();
			String lineStr = "";
			String[] tokens = s.split(" ");
			for (String token : tokens) {
				String prev = lineStr;
				String spaced = token + " ";
				lineStr += spaced;

				controlCodes = toControlCodes(getControlCodes(prev));
				if (font.getStringWidth(lineStr) > width) {
					lines.add(words);
					lineStr = controlCodes + spaced;
					words = new ArrayList<>();
				}

				words.add(controlCodes + token);
			}

			if (!lineStr.isEmpty()) {
				lines.add(words);
			}
			lines.add(new ArrayList<>());
		}

		int i = 0;
		for (List<String> words : lines) {
			int xi = x;
			int spacing = 4;

			for (String s : words) {
				int extra = 0;
				font.renderString(s, xi, y, color, false, matrix, buffers, false, 0, light);
				xi += font.getStringWidth(s) + spacing + extra;
			}

			y += words.isEmpty() ? paragraphSize : 10;
			i++;
		}
	}

	private static String getControlCodes(String s) {
		String controls = s.replaceAll("(?<!\u00a7)(.)", "");
		return controls.replaceAll(".*r", "r");
	}

	private static String toControlCodes(String s) {
		return s.replaceAll(".", "\u00a7$0");
	}
}
