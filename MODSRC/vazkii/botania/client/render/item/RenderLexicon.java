/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 *
 * File Created @ [Mar 25, 2014, 8:49:01 PM (GMT)]
 */
package vazkii.botania.client.render.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;

public class RenderLexicon implements IItemRenderer {

    ModelBook model = new ModelBook();
    ResourceLocation texture = new ResourceLocation(LibResources.MODEL_LEXICA);

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED_FIRST_PERSON;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();
        Minecraft mc = Minecraft.getMinecraft();
        mc.renderEngine.bindTexture(texture);
        float opening = 0F;
        float pageFlip = 0F;

        int ticks = ClientTickHandler.ticksWithLexicaOpen;
        GL11.glTranslatef(0.3F + 0.02F * ticks, 0.475F + 0.01F * ticks, -0.2F - 0.01F * ticks);
        GL11.glRotatef(95F + ticks * 5, 0F, 1F, 0F);
        GL11.glRotatef(ticks * 2.5F, 0F, 0F, 1F);
        GL11.glScalef(0.9F, 0.9F, 0.9F);
        opening = ticks / 12F;
        pageFlip = ClientTickHandler.pageFlipTicks / 5F;

        model.render(null, 0F, 0F, pageFlip, opening, 0F, 1F / 16F);

        GL11.glPopMatrix();
    }

}
