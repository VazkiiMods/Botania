/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.options.Perspective;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
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
	private static final boolean SHOULD_MISSPELL = Math.random() < 0.004;
	public static final SpriteIdentifier TEXTURE = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(LibResources.MODEL_LEXICA_DEFAULT));
	public static final SpriteIdentifier ELVEN_TEXTURE = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(LibResources.MODEL_LEXICA_ELVEN));

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
			"Banana", "Brotania", "Botanica", "Boat", "Batania", "Bosnia"
	};

	private static int quote = -1;
	private static int misspelling = -1;

	public static boolean renderHand(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if (!ConfigHandler.CLIENT.lexicon3dModel.getValue()
				|| mc.options.getPerspective() != Perspective.FIRST_PERSON
				|| mc.player.getStackInHand(hand).isEmpty()
				|| mc.player.getStackInHand(hand).getItem() != ModItems.lexicon) {
			return false;
		}
		try {
			renderFirstPersonItem(mc.player, tickDelta, pitch, hand, swingProgress, item, equipProgress, matrices, vertexConsumers, light);
			return true;
		} catch (Throwable throwable) {
			Botania.LOGGER.warn("Failed to render lexicon", throwable);
			return false;
		}
	}

	// [VanillaCopy] FirstPersonRenderer, irrelevant branches stripped out
	private static void renderFirstPersonItem(AbstractClientPlayerEntity player, float partialTicks, float pitch, Hand hand, float swingProgress, ItemStack stack, float equipProgress, MatrixStack ms, VertexConsumerProvider buffers, int light) {
		boolean flag = hand == Hand.MAIN_HAND;
		Arm handside = flag ? player.getMainArm() : player.getMainArm().getOpposite();
		ms.push();
		{
			boolean flag3 = handside == Arm.RIGHT;
			{
				float f5 = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
				float f6 = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * ((float) Math.PI * 2F));
				float f10 = -0.2F * MathHelper.sin(swingProgress * (float) Math.PI);
				int l = flag3 ? 1 : -1;
				ms.translate((float) l * f5, f6, f10);
				((AccessorFirstPersonRenderer) MinecraftClient.getInstance().getHeldItemRenderer()).botania_equipOffset(ms, handside, equipProgress);
				((AccessorFirstPersonRenderer) MinecraftClient.getInstance().getHeldItemRenderer()).botania_swingOffset(ms, handside, swingProgress);
			}

			doRender(stack, handside, ms, buffers, light, partialTicks);
		}

		ms.pop();
	}

	private static void doRender(ItemStack stack, Arm side, MatrixStack ms, VertexConsumerProvider buffers, int light, float partialTicks) {
		MinecraftClient mc = MinecraftClient.getInstance();

		ms.push();

		float ticks = ClientTickHandler.ticksWithLexicaOpen;
		if (ticks > 0 && ticks < 10) {
			if (ItemLexicon.isOpen()) {
				ticks += partialTicks;
			} else {
				ticks -= partialTicks;
			}
		}

		if (side == Arm.RIGHT) {
			ms.translate(0.3F + 0.02F * ticks, 0.125F + 0.01F * ticks, -0.2F - 0.035F * ticks);
			ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180F + ticks * 6));
		} else {
			ms.translate(0.1F - 0.02F * ticks, 0.125F + 0.01F * ticks, -0.2F - 0.035F * ticks);
			ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(200F + ticks * 10));
		}
		ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-0.3F + ticks * 2.85F));
		float opening = MathHelper.clamp(ticks / 12F, 0, 1);

		float pageFlipTicks = ClientTickHandler.pageFlipTicks;
		if (pageFlipTicks > 0) {
			pageFlipTicks -= ClientTickHandler.partialTicks;
		}

		float pageFlip = pageFlipTicks / 5F;

		float leftPageAngle = MathHelper.fractionalPart(pageFlip + 0.25F) * 1.6F - 0.3F;
		float rightPageAngle = MathHelper.fractionalPart(pageFlip + 0.75F) * 1.6F - 0.3F;
		model.setPageAngles(ClientTickHandler.total, MathHelper.clamp(leftPageAngle, 0.0F, 1.0F), MathHelper.clamp(rightPageAngle, 0.0F, 1.0F), opening);

		SpriteIdentifier mat = ModItems.lexicon.isElvenItem(stack) ? ELVEN_TEXTURE : TEXTURE;
		VertexConsumer buffer = mat.getVertexConsumer(buffers, RenderLayer::getEntitySolid);
		model.render(ms, buffer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);

		if (ticks < 3) {
			TextRenderer font = MinecraftClient.getInstance().textRenderer;
			ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180F));
			ms.translate(-0.30F, -0.24F, -0.07F);
			ms.scale(0.0030F, 0.0030F, -0.0030F);

			if (misspelling == -1) {
				misspelling = mc.world.random.nextInt(MISSPELLINGS.length);
			}

			String title = ItemLexicon.getTitle(stack).getString();
			if (SHOULD_MISSPELL) {
				title = title.replaceAll(LibMisc.MOD_NAME, MISSPELLINGS[misspelling]);
			}
			font.draw(font.trimToWidth(title, 80), 0, 0, 0xD69700, false, ms.peek().getModel(), buffers, false, 0, light);

			ms.translate(0F, 10F, 0F);
			ms.scale(0.6F, 0.6F, 0.6F);
			Text edition = ItemLexicon.getEdition().shallowCopy().formatted(Formatting.ITALIC, Formatting.BOLD);
			font.draw(edition, 0, 0, 0xA07100, false, ms.peek().getModel(), buffers, false, 0, light);

			if (quote == -1) {
				quote = mc.world.random.nextInt(QUOTES.length);
			}

			String quoteStr = QUOTES[quote];

			ms.translate(-5F, 15F, 0F);
			renderText(0, 0, 140, 100, 0, 0x79ff92, quoteStr, ms.peek().getModel(), buffers, light);

			ms.translate(8F, 110F, 0F);
			String blurb = I18n.translate("botaniamisc.lexiconcover0");
			font.draw(blurb, 0, 0, 0x79ff92, false, ms.peek().getModel(), buffers, false, 0, light);

			ms.translate(0F, 10F, 0F);
			String blurb2 = Formatting.UNDERLINE + "" + Formatting.ITALIC + I18n.translate("botaniamisc.lexiconcover1");
			font.draw(blurb2, 0, 0, 0x79ff92, false, ms.peek().getModel(), buffers, false, 0, light);

			ms.translate(0F, -30F, 0F);

			String authorTitle = I18n.translate("botaniamisc.lexiconcover2");
			int len = font.getWidth(authorTitle);
			font.draw(authorTitle, 58 - len / 2F, -8, 0xD69700, false, ms.peek().getModel(), buffers, false, 0, light);
		}

		ms.pop();
	}

	private static void renderText(int x, int y, int width, int height, int paragraphSize, int color, String unlocalizedText, Matrix4f matrix, VertexConsumerProvider buffers, int light) {
		x += 2;
		y += 10;
		width -= 4;

		TextRenderer font = MinecraftClient.getInstance().textRenderer;
		String text = I18n.translate(unlocalizedText).replaceAll("&", "\u00a7");
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
				if (font.getWidth(lineStr) > width) {
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
				font.draw(s, xi, y, color, false, matrix, buffers, false, 0, light);
				xi += font.getWidth(s) + spacing + extra;
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
