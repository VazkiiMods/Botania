/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 14, 2014, 5:57:26 PM (GMT)]
 */
package vazkii.botania.common.lexicon.page;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PageTerrasteel extends PageRecipe {

	private static final ResourceLocation terrasteelOverlay = new ResourceLocation(LibResources.GUI_TERRASTEEL_OVERLAY);

	public PageTerrasteel(String unlocalizedName) {
		super(unlocalizedName);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderRecipe(IGuiLexiconEntry gui, int mx, int my) {
		Block block1 = ModBlocks.livingrock;
		Block block2 = Blocks.lapis_block;
		Block block3 = ModBlocks.terraPlate;

		GL11.glTranslatef(0F, 0F, -10F);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8, gui.getTop() + 103, new ItemStack(block1), false);

		GL11.glTranslatef(0F, 0F, 5F);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 + 7, gui.getTop() + 106, new ItemStack(block2), false);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 - 6, gui.getTop() + 106, new ItemStack(block2), false);

		GL11.glTranslatef(0F, 0F, 5F);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8, gui.getTop() + 110, new ItemStack(block1), false);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 + 14, gui.getTop() + 110, new ItemStack(block1), false);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 - 13, gui.getTop() + 110, new ItemStack(block1), false);

		GL11.glTranslatef(0F, 0F, 5F);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 - 6, gui.getTop() + 114, new ItemStack(block2), false);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 + 7, gui.getTop() + 114, new ItemStack(block2), false);

		GL11.glTranslatef(0F, 0F, 5F);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 + 1, gui.getTop() + 117, new ItemStack(block1), false);

		GL11.glTranslatef(0F, 0F, 5F);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8, gui.getTop() + 102, new ItemStack(block3), false);
		GL11.glTranslatef(0F, 0F, -10F);

		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8, gui.getTop() + 30, new ItemStack(ModItems.manaResource, 1, 4), false);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8, gui.getTop() + 80, new ItemStack(ModItems.manaResource, 1, 0), false);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 - 20, gui.getTop() + 86, new ItemStack(ModItems.manaResource, 1, 1), false);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 + 19, gui.getTop() + 86, new ItemStack(ModItems.manaResource, 1, 2), false);

		Minecraft.getMinecraft().renderEngine.bindTexture(terrasteelOverlay);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		((GuiScreen) gui).drawTexturedModalRect(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());
		GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	public void onPageAdded(LexiconEntry entry, int index) {
		LexiconRecipeMappings.map(new ItemStack(ModItems.manaResource, 1, 4), entry, index);
	}

	@Override
	public List<ItemStack> getDisplayedRecipes() {
		ArrayList<ItemStack> list = new ArrayList();
		list.add(new ItemStack(ModItems.manaResource, 1, 4));
		return list;
	}

}
