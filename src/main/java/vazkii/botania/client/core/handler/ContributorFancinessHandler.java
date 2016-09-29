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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.item.IBaubleRender.Helper;
import vazkii.botania.api.subtile.signature.SubTileSignature;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.version.VersionChecker;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class ContributorFancinessHandler implements LayerRenderer<EntityPlayer> {

	public volatile static Map<String, ItemStack> flowerMap = null;
	private volatile static boolean startedLoading = false;

	private static final boolean phi = false;

	@Override
	public void doRenderLayer(@Nonnull EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		String name = player.getDisplayName().getUnformattedText();

		float yaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * partialTicks;
		float yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks;
		float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;

		GlStateManager.pushMatrix();
		GlStateManager.rotate(yawOffset, 0, -1, 0);
		GlStateManager.rotate(yaw - 270, 0, 1, 0);
		GlStateManager.rotate(pitch, 0, 0, 1);

		if(name.equals("haighyorkie"))
			renderGoldfish(player);

		firstStart();

		name = name.toLowerCase();
		if(player.isWearing(EnumPlayerModelParts.CAPE) && flowerMap != null && flowerMap.containsKey(name))
			renderFlower(player, flowerMap.get(name));

		GlStateManager.popMatrix();
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

	public static void firstStart() {
		if(!startedLoading) {
			new ThreadContributorListLoader();
			startedLoading = true;
		}
	}

	public static void load(Properties props) {
		flowerMap = new HashMap<>();
		for(String key : props.stringPropertyNames()) {
			String value = props.getProperty(key);

			try {
				int i = Integer.parseInt(value);
				if(i < 0 || i >= 16)
					throw new NumberFormatException();
				flowerMap.put(key, new ItemStack(ModBlocks.flower, 1, i));
			} catch(NumberFormatException e) {
				flowerMap.put(key, ItemBlockSpecialFlower.ofType(value));
			}
		}
	}

	private static void renderTwintails(EntityPlayer player, float partialTicks) {
		GlStateManager.pushMatrix();
		TextureAtlasSprite icon = MiscellaneousIcons.INSTANCE.tailIcon;
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
		Helper.translateToHeadLevel(player);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.rotate(90F, 0F, 1F, 0F);
		GlStateManager.translate(0, 1.62, 0);
		float t = 0.13F;
		GlStateManager.translate(t, -0.5F, -0.1F);
		if(player.motionY < 0)
			GlStateManager.rotate((float) player.motionY * 20F, 1F, 0F, 0F);

		float r = -18F + (float) Math.sin((ClientTickHandler.ticksInGame + partialTicks) * 0.1F) * 2F;
		GlStateManager.rotate(r, 0F, 0F, 1F);
		float s = 0.9F;
		GlStateManager.scale(s, s, s);
		IconHelper.renderIconIn3D(Tessellator.getInstance(), f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);
		GlStateManager.rotate(-r, 0F, 0F, 1F);
		GlStateManager.translate(-t, -0F, 0F);
		GlStateManager.scale(-1F, 1F, 1F);
		GlStateManager.translate(t, -0F, 0F);
		GlStateManager.rotate(r, 0F, 0F, 1F);
		IconHelper.renderIconIn3D(Tessellator.getInstance(), f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);
		GlStateManager.popMatrix();
	}

	private static void renderPhiFlower(EntityPlayer player) {
		GlStateManager.pushMatrix();
		TextureAtlasSprite icon = MiscellaneousIcons.INSTANCE.phiFlowerIcon;
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
		Helper.translateToHeadLevel(player);
		GlStateManager.rotate(90F, 0F, 1F, 0F);
		GlStateManager.rotate(180F, 1F, 0F, 0F);
		GlStateManager.translate(-0.4F, 0.1F, -0.25F);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.rotate(90F, 0F, 1F, 0F);
		GlStateManager.scale(0.4F, 0.4F, 0.4F);
		GlStateManager.translate(-1F, -3.5F, 0.125F);
		GlStateManager.rotate(20F, 1F, 0F, 0F);
		IconHelper.renderIconIn3D(Tessellator.getInstance(), f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);
		GlStateManager.popMatrix();
	}

	private static void renderGoldfish(EntityPlayer player) {
		GlStateManager.pushMatrix();
		TextureAtlasSprite icon = MiscellaneousIcons.INSTANCE.goldfishIcon;
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
		Helper.rotateIfSneaking(player);
		GlStateManager.rotate(180F, 0F, 0F, 1F);
		GlStateManager.rotate(90F, 0F, 1F, 0F);
		GlStateManager.scale(0.4F, 0.4F, 0.4F);
		GlStateManager.translate(-0.5F, 1.6F, 0F);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		IconHelper.renderIconIn3D(Tessellator.getInstance(), f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);
		GlStateManager.popMatrix();
	}

	@SuppressWarnings("deprecation")
	private static void renderFlower(EntityPlayer player, ItemStack flower) {
		GlStateManager.pushMatrix();
		Helper.translateToHeadLevel(player);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.rotate(180, 0, 0, 1);
		GlStateManager.translate(0, -0.85, 0);
		GlStateManager.rotate(-90, 0, 1, 0);
		GlStateManager.scale(0.5, 0.5, 0.5);
		ShaderHelper.useShader(ShaderHelper.gold);
		Minecraft.getMinecraft().getRenderItem().renderItem(flower, player, ItemCameraTransforms.TransformType.NONE, false);
		ShaderHelper.releaseShader();
		GlStateManager.popMatrix();
	}

	private static class ThreadContributorListLoader extends Thread {

		public ThreadContributorListLoader() {
			setName("Botania Contributor Fanciness Thread");
			setDaemon(true);
			start();
		}

		@Override
		public void run() {
			try {
				URL url = new URL("https://raw.githubusercontent.com/Vazkii/Botania/master/contributors.properties");
				Properties props = new Properties();
				try (InputStreamReader reader = new InputStreamReader(url.openStream())) {
					props.load(reader);
					load(props);
				}
			} catch (IOException e) {
				Botania.LOGGER.info("Could not load contributors list. Either you're offline or github is down. Nothing to worry about, carry on~");
			}
			VersionChecker.doneChecking = true;
		}

	}

}
