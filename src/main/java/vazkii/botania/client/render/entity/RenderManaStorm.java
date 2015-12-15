/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 25, 2015, 2:32:31 AM (GMT)]
 */
package vazkii.botania.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.entity.EntityManaStorm;

public class RenderManaStorm extends Render {

	@Override
	public void doRender(Entity e, double x, double y, double z, float something, float pticks) {
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		EntityManaStorm storm = (EntityManaStorm) e;
		float maxScale = 1.95F;
		float scale = 0.05F + ((float) storm.burstsFired / EntityManaStorm.TOTAL_BURSTS - (storm.deathTime == 0 ? 0 : storm.deathTime + pticks) / EntityManaStorm.DEATH_TIME) * maxScale;
		RenderHelper.renderStar(0x00FF00, scale, scale, scale, e.getUniqueID().getMostSignificantBits());
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return null;
	}

}
