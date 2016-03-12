package vazkii.botania.client.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.util.glu.Project;
import vazkii.botania.client.gui.lexicon.GuiLexicon;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.item.ItemLexicon;
import vazkii.botania.common.item.ModItems;

public class RenderLexicon {

    ModelBook model = new ModelBook();
    ResourceLocation texture = new ResourceLocation(LibResources.MODEL_LEXICA);

    @SubscribeEvent
    public void renderItem(RenderHandEvent evt) {
        Minecraft mc = Minecraft.getMinecraft();
        if(mc.gameSettings.thirdPersonView != 0 || mc.thePlayer.getHeldItem() == null || mc.thePlayer.getHeldItem().getItem() != ModItems.lexicon)
            return;
        evt.setCanceled(true);

        try {
            GlStateManager.clear(256);
            // Begin copy EntityRenderer.renderHand
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            float f = 0.07F;

            if (mc.gameSettings.anaglyph)
            {
                GlStateManager.translate((float)(-(/*xOffset*/0 * 2 - 1)) * f, 0.0F, 0.0F);
            }

            float fovModifier = ((float) ClientMethodHandles.getFOVModifier.invokeExact(mc.entityRenderer, ClientTickHandler.partialTicks, false));
            float farPlane = ((float) ClientMethodHandles.farPlaneDistance_getter.invokeExact(mc.entityRenderer));
            Project.gluPerspective(fovModifier, (float)mc.displayWidth / (float)mc.displayHeight, 0.05F, farPlane * 2.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();

            if (mc.gameSettings.anaglyph)
            {
                GlStateManager.translate((float)(/*xOffset*/0 * 2 - 1) * 0.1F, 0.0F, 0.0F);
            }

            GlStateManager.pushMatrix();
            ClientMethodHandles.hurtCameraEffect.invokeExact(mc.entityRenderer, ClientTickHandler.partialTicks);

            if (mc.gameSettings.viewBobbing)
            {
                ClientMethodHandles.setupViewBobbing.invokeExact(mc.entityRenderer, ClientTickHandler.partialTicks);
            }

            boolean flag = mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)mc.getRenderViewEntity()).isPlayerSleeping();

            if (mc.gameSettings.thirdPersonView == 0 && !flag && !mc.gameSettings.hideGUI && !mc.playerController.isSpectator())
            {
                ClientMethodHandles.enableLightmap.invokeExact(mc.entityRenderer);
                // this.itemRenderer.renderItemInFirstPerson(partialTicks);
                this.doRender();
                ClientMethodHandles.disableLightmap.invokeExact(mc.entityRenderer);
            }

            GlStateManager.popMatrix();

            if (mc.gameSettings.thirdPersonView == 0 && !flag)
            {
                mc.getItemRenderer().renderOverlays(ClientTickHandler.partialTicks);
                ClientMethodHandles.hurtCameraEffect.invokeExact(mc.entityRenderer, ClientTickHandler.partialTicks);
            }

            if (mc.gameSettings.viewBobbing)
            {
                ClientMethodHandles.setupViewBobbing.invokeExact(mc.entityRenderer, ClientTickHandler.partialTicks);
            }
            // End EntityRenderer.renderHand copy
        } catch (Throwable throwable) {
            FMLLog.warning("[Botania]: Failed to render lexicon");
            throwable.printStackTrace();
        }


    }

    private void doRender() {
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        float opening = 0F;
        float pageFlip = 0F;

        float ticks = ClientTickHandler.ticksWithLexicaOpen;
        if(ticks > 0 && ticks < 10) {
            if(Minecraft.getMinecraft().currentScreen instanceof GuiLexicon)
                ticks += ClientTickHandler.partialTicks;
            else ticks -= ClientTickHandler.partialTicks;
        }

        GlStateManager.translate(0.3F + 0.02F * ticks, 0.475F + 0.01F * ticks, -0.2F - 0.01F * ticks);
        GlStateManager.rotate(87.5F + ticks * 5, 0F, 1F, 0F);
        GlStateManager.rotate(ticks * 2.5F, 0F, 0F, 1F);
        GlStateManager.scale(0.9F, 0.9F, 0.9F);
        opening = ticks / 12F;

        float pageFlipTicks = ClientTickHandler.pageFlipTicks;
        if(pageFlipTicks > 0)
            pageFlipTicks -= ClientTickHandler.partialTicks;

        pageFlip = pageFlipTicks / 5F;

        model.render(null, 0F, 0F, pageFlip, opening, 0F, 1F / 16F);
        if(ticks < 3) {
            FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
            GlStateManager.rotate(180F, 0F, 0F, 1F);
            GlStateManager.translate(-0.3F, -0.21F, -0.07F);
            GlStateManager.scale(0.0035F, 0.0035F, -0.0035F);
            boolean bevo = Minecraft.getMinecraft().thePlayer.getName().equalsIgnoreCase("BevoLJ");
            boolean saice = Minecraft.getMinecraft().thePlayer.getName().equalsIgnoreCase("saice");

            String title = ModItems.lexicon.getItemStackDisplayName(null);
            String origTitle = title;

            if(Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem() != null)
                title = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getDisplayName();
            if(title.equals(origTitle) && bevo)
                title = StatCollector.translateToLocal("item.botania:lexicon.bevo");
            if(title.equals(origTitle) && saice)
                title = StatCollector.translateToLocal("item.botania:lexicon.saice");

            font.drawString(font.trimStringToWidth(title, 80), 0, 0, 0xD69700);
            GlStateManager.translate(0F, 10F, 0F);
            GlStateManager.scale(0.6F, 0.6F, 0.6F);
            font.drawString(EnumChatFormatting.ITALIC + "" + EnumChatFormatting.BOLD + String.format(StatCollector.translateToLocal("botaniamisc.edition"), ItemLexicon.getEdition()), 0, 0, 0xA07100);

            GlStateManager.translate(0F, 15F, 0F);
            font.drawString(StatCollector.translateToLocal("botaniamisc.lexiconcover0"), 0, 0, 0x79ff92);

            GlStateManager.translate(0F, 10F, 0F);
            font.drawString(StatCollector.translateToLocal("botaniamisc.lexiconcover1"), 0, 0, 0x79ff92);

            GlStateManager.translate(0F, 50F, 0F);
            font.drawString(StatCollector.translateToLocal("botaniamisc.lexiconcover2"), 0, 0, 0x79ff92);

            GlStateManager.translate(0F, 10F, 0F);
            font.drawString(EnumChatFormatting.UNDERLINE + "" + EnumChatFormatting.ITALIC + StatCollector.translateToLocal("botaniamisc.lexiconcover3"), 0, 0, 0x79ff92);

            if(bevo || saice) {
                GlStateManager.translate(0F, 10F, 0F);
                font.drawString(StatCollector.translateToLocal("botaniamisc.lexiconcover" + (bevo ? 4 : 5)), 0, 0, 0x79ff92);
            }
        }

        GlStateManager.popMatrix();
    }

}
