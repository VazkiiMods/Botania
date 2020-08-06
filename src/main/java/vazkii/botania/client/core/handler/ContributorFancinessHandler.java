/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DefaultUncaughtExceptionHandler;
import net.minecraft.util.registry.Registry;

import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class ContributorFancinessHandler extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

	private static volatile Map<String, ItemStack> flowerMap = Collections.emptyMap();
	private static boolean startedLoading = false;

	private static final ImmutableMap<String, String> LEGACY_FLOWER_NAMES = ImmutableMap.<String, String>builder()
			.put("puredaisy", LibBlockNames.SUBTILE_PUREDAISY.getPath())
			.put("fallenkanade", LibBlockNames.SUBTILE_FALLEN_KANADE.getPath())
			.put("heiseidream", LibBlockNames.SUBTILE_HEISEI_DREAM.getPath())
			.put("arcanerose", LibBlockNames.SUBTILE_ARCANE_ROSE.getPath())
			.put("jadedamaranthus", LibBlockNames.SUBTILE_JADED_AMARANTHUS.getPath())
			.put("orechidignem", LibBlockNames.SUBTILE_ORECHID_IGNEM.getPath())
			.build();

	public ContributorFancinessHandler(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> renderer) {
		super(renderer);
	}

	@Override
	public void render(MatrixStack ms, IRenderTypeBuffer buffers, int light, @Nonnull AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		firstStart();

		if (player.isInvisible()) {
			return;
		}

		String name = player.getGameProfile().getName();

		if (name.equals("haighyorkie")) {
			renderGoldfish(ms, buffers);
		}

		if (player.isWearing(PlayerModelPart.CAPE)) {
			ItemStack flower = getFlower(name.toLowerCase(Locale.ROOT));
			if (!flower.isEmpty()) {
				renderFlower(ms, buffers, player, flower);
			}
		}

	}

	public static void firstStart() {
		if (!startedLoading) {
			new ThreadContributorListLoader();
			startedLoading = true;
		}
	}

	public static ItemStack getFlower(String name) {
		return flowerMap.getOrDefault(name, ItemStack.EMPTY);
	}

	public static void load(Properties props) {
		Map<String, ItemStack> m = new HashMap<>();
		for (String key : props.stringPropertyNames()) {
			String value = props.getProperty(key);

			try {
				int i = Integer.parseInt(value);
				if (i < 0 || i >= 16) {
					throw new NumberFormatException();
				}
				m.put(key, new ItemStack(ModBlocks.getFlower(DyeColor.byId(i))));
			} catch (NumberFormatException e) {
				String rawName = value.toLowerCase(Locale.ROOT);
				String flowerName = LEGACY_FLOWER_NAMES.getOrDefault(rawName, rawName);

				Item item = ModTags.Items.SPECIAL_FLOWERS.getAllElements().stream()
						.filter(flower -> Registry.ITEM.getKey(flower).getPath().equals(flowerName))
						.findFirst().orElse(Items.POPPY);
				m.put(key, new ItemStack(item));
			}
		}
		flowerMap = m;
	}

	private void renderGoldfish(MatrixStack ms, IRenderTypeBuffer buffers) {
		ms.push();
		getEntityModel().bipedHead.translateRotate(ms);
		ms.translate(-0.15F, -0.60F, 0F);
		ms.scale(0.4F, -0.4F, -0.4F);
		Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightnessColor(ms.getLast(), buffers.getBuffer(Atlases.getTranslucentCullBlockType()), null, MiscellaneousIcons.INSTANCE.goldfishModel, 1, 1, 1, 0xF000F0, OverlayTexture.NO_OVERLAY);
		ms.pop();
	}

	private void renderFlower(MatrixStack ms, IRenderTypeBuffer buffers, PlayerEntity player, ItemStack flower) {
		ms.push();
		getEntityModel().bipedHead.translateRotate(ms);
		ms.translate(0, -0.75, 0);
		ms.scale(0.5F, -0.5F, -0.5F);
		RenderHelper.renderItemModelGold(player, flower, ItemCameraTransforms.TransformType.NONE, ms, buffers, player.world, 0xF000F0, OverlayTexture.NO_OVERLAY);
		ms.pop();
	}

	private static class ThreadContributorListLoader extends Thread {

		public ThreadContributorListLoader() {
			setName("Botania Contributor Fanciness Thread");
			setDaemon(true);
			setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(Botania.LOGGER));
			start();
		}

		@Override
		public void run() {
			try {
				URL url = new URL("https://raw.githubusercontent.com/Vazkii/Botania/master/contributors.properties");
				Properties props = new Properties();
				try (InputStreamReader reader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8)) {
					props.load(reader);
					load(props);
				}
			} catch (IOException e) {
				Botania.LOGGER.info("Could not load contributors list. Either you're offline or github is down. Nothing to worry about, carry on~");
			}
		}

	}

}
