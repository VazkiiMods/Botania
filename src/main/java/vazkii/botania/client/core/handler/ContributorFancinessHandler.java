/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 9, 2015, 5:35:26 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DefaultUncaughtExceptionHandler;
import vazkii.botania.api.item.AccessoryRenderHelper;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public final class ContributorFancinessHandler extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

	public static final Map<String, ItemStack> flowerMap = new HashMap<>();
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
		if(player.isInvisible())
			return;

		String name = player.getDisplayName().getString();

		RenderSystem.pushMatrix();
		AccessoryRenderHelper.translateToHeadLevel(player, partialTicks);
		
		if(name.equals("haighyorkie"))
			renderGoldfish(ms, buffers, light, player);

		firstStart();

		name = name.toLowerCase();
		if(player.isWearing(PlayerModelPart.CAPE) && flowerMap.containsKey(name))
			renderFlower(ms, buffers, player, flowerMap.get(name));

		RenderSystem.popMatrix();
	}

	public static void firstStart() {
		if(!startedLoading) {
			new ThreadContributorListLoader();
			startedLoading = true;
		}
	}

	public static void load(Properties props) {
		flowerMap.clear();
		for(String key : props.stringPropertyNames()) {
			String value = props.getProperty(key);

			try {
				int i = Integer.parseInt(value);
				if(i < 0 || i >= 16)
					throw new NumberFormatException();
				flowerMap.put(key, new ItemStack(ModBlocks.getFlower(DyeColor.byId(i))));
			} catch (NumberFormatException e) {
				String rawName = value.toLowerCase(Locale.ROOT);
				String flowerName = LEGACY_FLOWER_NAMES.getOrDefault(rawName, rawName);

				Item item = ModTags.Items.SPECIAL_FLOWERS.getAllElements().stream()
						.filter(flower -> flower.getRegistryName().getPath().equals(flowerName))
						.findFirst().orElse(Items.POPPY);
				flowerMap.put(key, new ItemStack(item));
			}
		}
	}

	private static void renderGoldfish(MatrixStack ms, IRenderTypeBuffer buffers, int light, PlayerEntity player) {
		ms.push();
		ms.translate(0, player.getEyeHeight(), 0);
		AccessoryRenderHelper.rotateIfSneaking(player);
		ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90));
		ms.scale(0.4F, 0.4F, 0.4F);
		ms.translate(-0.5F, 1.6F, 0F);
		Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().render(ms.peek(), buffers.getBuffer(Atlases.getEntityTranslucent()), null, MiscellaneousIcons.INSTANCE.goldfishModel, 1, 1, 1, light, OverlayTexture.DEFAULT_UV);
		ms.pop();
	}

	@SuppressWarnings("deprecation")
	private static void renderFlower(MatrixStack ms, IRenderTypeBuffer buffers, PlayerEntity player, ItemStack flower) {
		ms.push();
		ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
		ms.translate(0, -0.85, 0);
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-90));
		ms.scale(0.5F, 0.5F, 0.5F);
		// todo 1.15 shader
		ShaderHelper.useShader(ShaderHelper.BotaniaShader.GOLD);
		Minecraft.getInstance().getItemRenderer().renderItem(player, flower, ItemCameraTransforms.TransformType.NONE, false, ms, buffers, player.world, 0xF000F0, OverlayTexture.DEFAULT_UV);
		ShaderHelper.releaseShader();
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
