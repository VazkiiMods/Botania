/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 25, 2015, 5:55:59 PM (GMT)]
 */
package vazkii.botania.client.render.entity;

import net.minecraft.client.renderer.entity.RenderWither;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.lib.LibResources;

public class RenderPinkWither extends RenderWither {

	private static final ResourceLocation resource = new ResourceLocation(LibResources.MODEL_PINK_WITHER);

	int idk = -1;

	@Override
	public void doRender(EntityWither p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
		super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
		if(BossStatus.bossName.equals(p_76986_1_.func_145748_c_().getFormattedText())) {
			BossStatus.statusBarTime = -1;
			BossStatus.hasColorModifier = false;
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityWither p_110775_1_) {
		return resource;
	}

}
