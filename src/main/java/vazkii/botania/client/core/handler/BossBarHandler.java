/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Oct 29, 2014, 6:46:10 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import java.awt.Rectangle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import vazkii.botania.api.boss.IBotaniaBoss;
import vazkii.botania.api.boss.IBotaniaBossWithShader;
import vazkii.botania.api.internal.ShaderCallback;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.helper.MathHelper;

public final class BossBarHandler {

	public static final ResourceLocation defaultBossBar = new ResourceLocation(LibResources.GUI_BOSS_BAR);
	static IBotaniaBoss currentBoss;

	private static final BarCallback barUniformCallback = new BarCallback();

	public static void setCurrentBoss(IBotaniaBoss status) {
		currentBoss = status;
	}

	public static void render(ScaledResolution res) {
		if(currentBoss == null)
			return;

		Minecraft mc = Minecraft.getMinecraft();
		Rectangle bgRect = currentBoss.getBossBarTextureRect();
		Rectangle fgRect = currentBoss.getBossBarHPTextureRect();
		String name = currentBoss.func_145748_c_().getFormattedText();
		int c = res.getScaledWidth() / 2;
		int x = c - bgRect.width / 2;
		int y = 20;
		int xf = x + (bgRect.width - fgRect.width) / 2;
		int yf = y + (bgRect.height - fgRect.height) / 2;
		int fw = (int) ((double) fgRect.width * (currentBoss.getHealth() / currentBoss.getMaxHealth()));
		int tx = c - mc.fontRenderer.getStringWidth(name) / 2;

		GL11.glColor4f(1F, 1F, 1F, 1F);
		currentBoss.bossBarRenderCallback(res, x, y);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		mc.renderEngine.bindTexture(currentBoss.getBossBarTexture());
		drawBar(x, y, bgRect.x, bgRect.y, bgRect.width, bgRect.height, true);
		drawBar(xf, yf, fgRect.x, fgRect.y, fw, fgRect.height, false);
		mc.fontRenderer.drawStringWithShadow(name, tx, y - 10, 0xA2018C);
		GL11.glEnable(GL11.GL_BLEND);

		Entity e = (Entity) currentBoss;
		EntityPlayer p = mc.thePlayer;
		if(e.isDead || !p.worldObj.loadedEntityList.contains(e) || MathHelper.pointDistanceSpace(e.posX, e.posY, e.posZ, p.posX, p.posY, p.posZ) > 32)
			currentBoss = null;
	}

	public static void drawBar(int x, int y, int u, int v, int w, int h, boolean bg) {
		boolean useShader = currentBoss instanceof IBotaniaBossWithShader;
		if(useShader) {
			IBotaniaBossWithShader shader = (IBotaniaBossWithShader) currentBoss;
			int program = shader.getBossBarShaderProgram(bg);
			ShaderCallback callback = program == 0 ? null : shader.getBossBarShaderCallback(bg, program);
			barUniformCallback.set(u, v, callback);

			ShaderHelper.useShader(program, barUniformCallback);
		}

		RenderHelper.drawTexturedModalRect(x, y, 0, u, v, w, h);

		if(useShader)
			ShaderHelper.releaseShader();
	}

	static class BarCallback extends ShaderCallback {
		int x, y;
		ShaderCallback callback;

		@Override
		public void call(int shader) {
			int startXUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "startX");
			int startYUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "startY");


			ARBShaderObjects.glUniform1iARB(startXUniform, x);
			ARBShaderObjects.glUniform1iARB(startYUniform, y);

			if(callback != null)
				callback.call(shader);
		}

		void set(int x, int y, ShaderCallback callback) {
			this.x = x;
			this.y = y;
			this.callback = callback;
		}
	}

}
