/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 12, 2014, 4:07:26 PM (GMT)]
 */
package vazkii.botania.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.common.entity.EntityDoppleganger;

public class RenderDoppleganger extends RenderBiped {

	public RenderDoppleganger() {
		super(new ModelBiped(0.5F), 0F);
	}
	
	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		BossStatus.setBossStatus((EntityDoppleganger) par1Entity, true);
		
		ShaderHelper.useShader(ShaderHelper.doppleganger);
		super.doRender(par1Entity, par2, par4, par6, par8, par9);
		ShaderHelper.releaseShader();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity) {
		return Minecraft.getMinecraft().thePlayer.getLocationSkin();
	}
	
}
