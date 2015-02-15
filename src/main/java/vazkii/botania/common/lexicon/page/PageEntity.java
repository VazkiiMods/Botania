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

import java.lang.reflect.Constructor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PageEntity extends LexiconPage{

	Entity dummyEntity;
	int relativeMouseX, relativeMouseY;
	boolean tooltipEntity;
	int size;
	Constructor entityConstructor;

	public PageEntity(String unlocalizedName, String entity, int size) {
		super(unlocalizedName);
		Class EntityClass = (Class) EntityList.stringToClassMapping.get(entity);
		this.size = size;
		try {
			entityConstructor = EntityClass.getConstructor(new Class[] {World.class});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderScreen(IGuiLexiconEntry gui, int mx, int my) {
		prepDummy();
		int text_x = gui.getLeft() + 16;
		int text_y = gui.getTop() + gui.getHeight() - 40;
		int entity_scale = getEntityScale(size);
		int entity_x = gui.getLeft() + gui.getWidth() / 2;
		int entity_y = gui.getTop() + gui.getHeight() / 2 + MathHelper.floor_float(dummyEntity.height * entity_scale / 2);

		renderEntity(gui, dummyEntity, entity_x, entity_y, entity_scale, dummyEntity.ticksExisted * 2);

		PageText.renderText(text_x, text_y, gui.getWidth() - 30, gui.getHeight(), getUnlocalizedName());
	}

	@SideOnly(Side.CLIENT)
	public int getEntityScale(int targetSize) {
		float entity_size = dummyEntity.width;

		if(dummyEntity.width < dummyEntity.height)
			entity_size = dummyEntity.height;

		return MathHelper.floor_float(size / entity_size);

	}

	@Override
	public void updateScreen() {
		prepDummy();
		dummyEntity.ticksExisted++;
	}

	@SideOnly(Side.CLIENT)
	public void renderEntity(IGuiLexiconEntry gui, Entity entity, int x, int y, int scale, float rotation) {
		dummyEntity.worldObj = Minecraft.getMinecraft() != null ? Minecraft.getMinecraft().theWorld : null;

		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 50.0F);
		GL11.glScalef(-scale, scale, scale);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glTranslatef(0.0F, entity.yOffset, 0.0F);
		RenderManager.instance.playerViewY = 180.0F;
		RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

		if(relativeMouseX >= x - dummyEntity.width * scale / 2 - 10  && relativeMouseY >= y - dummyEntity.height * scale - 20 && relativeMouseX <= x + dummyEntity.width * scale / 2 + 10 && relativeMouseY <= y + 20)
			tooltipEntity = true;
	}

	public void prepDummy() {
		if(dummyEntity == null || dummyEntity.isDead) {
			try {
				dummyEntity = (Entity) entityConstructor.newInstance(new Object[] {Minecraft.getMinecraft().theWorld});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
