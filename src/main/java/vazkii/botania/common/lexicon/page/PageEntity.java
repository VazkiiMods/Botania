/**
 * This class was created by <SoundLogic>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 4, 2014, 10:38:50 PM (GMT)]
 */
package vazkii.botania.common.lexicon.page;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GLX;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;

import java.lang.reflect.Constructor;

public class PageEntity extends LexiconPage{

	Entity dummyEntity;
	int relativeMouseX, relativeMouseY;
	boolean tooltipEntity;
	final int size;
	private final EntityType<?> type;

	public PageEntity(String unlocalizedName, EntityType<?> type, int size) {
		super(unlocalizedName);
		this.type = type;
		this.size = size;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderScreen(IGuiLexiconEntry gui, int mx, int my) {
		prepDummy();
		int text_x = gui.getLeft() + 16;
		int text_y = gui.getTop() + gui.getHeight() - 40;
		int entity_scale = getEntityScale(size);
		int entity_x = gui.getLeft() + gui.getWidth() / 2;
		int entity_y = gui.getTop() + gui.getHeight() / 2 + MathHelper.floor(dummyEntity.height * entity_scale / 2);

		renderEntity(gui, dummyEntity, entity_x, entity_y, entity_scale, dummyEntity.ticksExisted * 2);

		PageText.renderText(text_x, text_y, gui.getWidth() - 30, gui.getHeight(), getUnlocalizedName());
	}

	@OnlyIn(Dist.CLIENT)
	public int getEntityScale(int targetSize) {
		float entity_size = dummyEntity.width;

		if(dummyEntity.width < dummyEntity.height)
			entity_size = dummyEntity.height;

		return MathHelper.floor(size / entity_size);

	}

	@Override
	public void updateScreen() {
		prepDummy();
		dummyEntity.ticksExisted++;
	}

	@OnlyIn(Dist.CLIENT)
	public void renderEntity(IGuiLexiconEntry gui, Entity entity, int x, int y, int scale, float rotation) {
		dummyEntity.world = Minecraft.getInstance() != null ? Minecraft.getInstance().world : null;

		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translatef(x, y, 50.0F);
		GlStateManager.scalef(-scale, scale, scale);
		GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotatef(rotation, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		Minecraft.getInstance().getRenderManager().playerViewY = 180.0F;
		Minecraft.getInstance().getRenderManager().renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GLX.glActiveTexture(GLX.GL_TEXTURE1);
		GlStateManager.disableTexture();
		GLX.glActiveTexture(GLX.GL_TEXTURE0);

		if(relativeMouseX >= x - dummyEntity.width * scale / 2 - 10  && relativeMouseY >= y - dummyEntity.height * scale - 20 && relativeMouseX <= x + dummyEntity.width * scale / 2 + 10 && relativeMouseY <= y + 20)
			tooltipEntity = true;
	}

	public void prepDummy() {
		if(dummyEntity == null || !dummyEntity.isAlive()) {
			try {
				dummyEntity = type.create(Minecraft.getInstance().world);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
