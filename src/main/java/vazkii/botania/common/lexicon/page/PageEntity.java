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
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;

public class PageEntity extends LexiconPage{

	Entity dummyEntity;
	int relativeMouseX, relativeMouseY;
	boolean tooltipEntity;
	final int size;
	Constructor entityConstructor;

	public PageEntity(String unlocalizedName, String entity, int size) {
		super(unlocalizedName);
		Class EntityClass = EntityList.NAME_TO_CLASS.get(entity);
		this.size = size;
		try {
			entityConstructor = EntityClass.getConstructor(World.class);
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

		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, 50.0F);
		GlStateManager.scale(-scale, scale, scale);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(rotation, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		Minecraft.getMinecraft().getRenderManager().playerViewY = 180.0F;
		Minecraft.getMinecraft().getRenderManager().doRenderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

		if(relativeMouseX >= x - dummyEntity.width * scale / 2 - 10  && relativeMouseY >= y - dummyEntity.height * scale - 20 && relativeMouseX <= x + dummyEntity.width * scale / 2 + 10 && relativeMouseY <= y + 20)
			tooltipEntity = true;
	}

	public void prepDummy() {
		if(dummyEntity == null || dummyEntity.isDead) {
			try {
				dummyEntity = (Entity) entityConstructor.newInstance(Minecraft.getMinecraft().theWorld);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
