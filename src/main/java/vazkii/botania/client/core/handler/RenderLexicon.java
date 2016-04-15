package vazkii.botania.client.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.util.glu.Project;
import vazkii.botania.client.gui.lexicon.GuiLexicon;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.item.ItemLexicon;
import vazkii.botania.common.item.ModItems;

// Hacky way to render 3D lexicon, will be reevaluated in the future.
public class RenderLexicon {

    ModelBook model = new ModelBook();
    ResourceLocation texture = new ResourceLocation(LibResources.MODEL_LEXICA);

    @SubscribeEvent
    public void renderItem(RenderHandEvent evt) {
        Minecraft mc = Minecraft.getMinecraft();
        if(true || !ConfigHandler.lexicon3dModel || mc.gameSettings.thirdPersonView != 0 || !PlayerHelper.hasHeldItem(mc.thePlayer, ModItems.lexicon))
            return;
        evt.setCanceled(true);

        try {
            // Called immediately before EntityRenderer.renderHand
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
                renderItemInFirstPerson();
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

    private void renderItemInFirstPerson() throws Throwable {
        // Cherry picked from ItemRenderer.renderItemInFirstPerson
        float prevEquippedProgress = 0;//((float) ClientMethodHandles.prevEquippedProgress_getter.invokeExact(Minecraft.getMinecraft().getItemRenderer()));
        float equippedProgress = 0;//((float) ClientMethodHandles.equippedProgress_getter.invokeExact(Minecraft.getMinecraft().getItemRenderer()));
        float f = 1.0F - (prevEquippedProgress + (equippedProgress - prevEquippedProgress) * ClientTickHandler.partialTicks);
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        float f1 = player.getSwingProgress(ClientTickHandler.partialTicks);
        float f2 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * ClientTickHandler.partialTicks;
        float f3 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * ClientTickHandler.partialTicks;
        this.func_178101_a(f2, f3);
        this.func_178109_a(player);
        this.func_178110_a(player, ClientTickHandler.partialTicks);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        this.func_178105_d(f1);
        this.transformFirstPersonItem(f, f1);

        doRender();

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
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
        GlStateManager.rotate(ticks * 2.85F, 0F, 0F, 1F);
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

            ItemStack stack = PlayerHelper.getFirstHeldItemClass(Minecraft.getMinecraft().thePlayer, Item.class);
            if(stack != null)
                title = stack.getDisplayName();
            if(title.equals(origTitle) && bevo)
                title = I18n.translateToLocal("item.botania:lexicon.bevo");
            if(title.equals(origTitle) && saice)
                title = I18n.translateToLocal("item.botania:lexicon.saice");

            font.drawString(font.trimStringToWidth(title, 80), 0, 0, 0xD69700);
            GlStateManager.translate(0F, 10F, 0F);
            GlStateManager.scale(0.6F, 0.6F, 0.6F);
            font.drawString(TextFormatting.ITALIC + "" + TextFormatting.BOLD + String.format(I18n.translateToLocal("botaniamisc.edition"), ItemLexicon.getEdition()), 0, 0, 0xA07100);

            GlStateManager.translate(0F, 15F, 0F);
            font.drawString(I18n.translateToLocal("botaniamisc.lexiconcover0"), 0, 0, 0x79ff92);

            GlStateManager.translate(0F, 10F, 0F);
            font.drawString(I18n.translateToLocal("botaniamisc.lexiconcover1"), 0, 0, 0x79ff92);

            GlStateManager.translate(0F, 50F, 0F);
            font.drawString(I18n.translateToLocal("botaniamisc.lexiconcover2"), 0, 0, 0x79ff92);

            GlStateManager.translate(0F, 10F, 0F);
            font.drawString(TextFormatting.UNDERLINE + "" + TextFormatting.ITALIC + I18n.translateToLocal("botaniamisc.lexiconcover3"), 0, 0, 0x79ff92);

            if(bevo || saice) {
                GlStateManager.translate(0F, 10F, 0F);
                font.drawString(I18n.translateToLocal("botaniamisc.lexiconcover" + (bevo ? 4 : 5)), 0, 0, 0x79ff92);
            }
        }

        GlStateManager.popMatrix();
    }

    // Copy - ItemRenderer.func_178101_a
    private void func_178101_a(float angle, float p_178101_2_) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(angle, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(p_178101_2_, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    // Copy - ItemRenderer.func_178109_a
    private void func_178109_a(AbstractClientPlayer clientPlayer) {
        int i = Minecraft.getMinecraft().theWorld.getCombinedLight(new BlockPos(clientPlayer.posX, clientPlayer.posY + (double)clientPlayer.getEyeHeight(), clientPlayer.posZ), 0);
        float f = (float)(i & 65535);
        float f1 = (float)(i >> 16);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f1);
    }

    // Copy - ItemRenderer.func_178110_a
    private void func_178110_a(EntityPlayerSP entityplayerspIn, float partialTicks) {
        float f = entityplayerspIn.prevRenderArmPitch + (entityplayerspIn.renderArmPitch - entityplayerspIn.prevRenderArmPitch) * partialTicks;
        float f1 = entityplayerspIn.prevRenderArmYaw + (entityplayerspIn.renderArmYaw - entityplayerspIn.prevRenderArmYaw) * partialTicks;
        GlStateManager.rotate((entityplayerspIn.rotationPitch - f) * 0.1F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate((entityplayerspIn.rotationYaw - f1) * 0.1F, 0.0F, 1.0F, 0.0F);
    }

    // Copy - ItemRenderer.func_178105_d
    private void func_178105_d(float p_178105_1_) {
        float f = -0.4F * MathHelper.sin(MathHelper.sqrt_float(p_178105_1_) * (float)Math.PI);
        float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt_float(p_178105_1_) * (float)Math.PI * 2.0F);
        float f2 = -0.2F * MathHelper.sin(p_178105_1_ * (float)Math.PI);
        GlStateManager.translate(f, f1, f2);
    }

    // Copy with modification - ItemRenderer.transformFirstPersonItem
    private void transformFirstPersonItem(float equipProgress, float swingProgress) {
        GlStateManager.translate(1.2F, -1F, -0.71999997F); // Botania - x(0.56) -> 1; y(-0.52) -> -1
        GlStateManager.translate(0.0F, equipProgress * -0.6F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F); // Botania - 45 -> 90
        GlStateManager.rotate(-20, 0, 0, 1); // Botania - added
        float f = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
        float f1 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        GlStateManager.rotate(f * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f1 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
        // GlStateManager.scale(0.4F, 0.4F, 0.4F); Botania - removed
    }

}
