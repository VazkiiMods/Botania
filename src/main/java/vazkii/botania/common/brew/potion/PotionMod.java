/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 2, 2014, 10:12:45 PM (GMT)]
 */
package vazkii.botania.common.brew.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.lib.LibMisc;

public class PotionMod extends Potion {

	private static final ResourceLocation resource = new ResourceLocation(LibResources.GUI_POTIONS);
	private final int iconIndex;

	public PotionMod(String name, boolean badEffect, int color, int iconIndex) {
		super(badEffect, color);
		setRegistryName(new ResourceLocation(LibMisc.MOD_ID, name));
		setPotionName("botania.potion." + name);
		this.iconIndex = iconIndex;
	}

	public boolean hasEffect(EntityLivingBase entity) {
		return hasEffect(entity, this);
	}

	public boolean hasEffect(EntityLivingBase entity, Potion potion) {
		return entity.getActivePotionEffect(potion) != null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
		render(x + 6, y + 7, 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
		render(x + 3, y + 3, alpha);
	}

	@SideOnly(Side.CLIENT)
	private void render(int x, int y, float alpha) {
		Minecraft.getMinecraft().renderEngine.bindTexture(resource);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buf = tessellator.getBuffer();
		buf.begin(7, DefaultVertexFormats.POSITION_TEX);
		GlStateManager.color(1, 1, 1, alpha);

		int textureX = iconIndex % 8 * 18;
		int textureY = 198 + iconIndex / 8 * 18;

		buf.pos(x, y + 18, 0).tex(textureX * 0.00390625, (textureY + 18) * 0.00390625).endVertex();
		buf.pos(x + 18, y + 18, 0).tex((textureX + 18) * 0.00390625, (textureY + 18) * 0.00390625).endVertex();
		buf.pos(x + 18, y, 0).tex((textureX + 18) * 0.00390625, textureY * 0.00390625).endVertex();
		buf.pos(x, y, 0).tex(textureX * 0.00390625, textureY * 0.00390625).endVertex();

		tessellator.draw();
	}

}
