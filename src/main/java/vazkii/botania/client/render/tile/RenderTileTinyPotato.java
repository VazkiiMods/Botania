/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 18, 2014, 10:48:46 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.item.TinyPotatoRenderEvent;
import vazkii.botania.client.core.handler.ContributorFancinessHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelTinyPotato;
import vazkii.botania.common.block.tile.TileTinyPotato;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;

public class RenderTileTinyPotato extends TileEntitySpecialRenderer<TileTinyPotato> {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_TINY_POTATO);
	private static final ResourceLocation textureGrayscale = new ResourceLocation(LibResources.MODEL_TINY_POTATO_GS);
	private static final ResourceLocation textureHalloween = new ResourceLocation(LibResources.MODEL_TINY_POTATO_HALLOWEEN);
	private static final ModelTinyPotato model = new ModelTinyPotato();

	@Override
	public void renderTileEntityAt(TileTinyPotato potato, double d0, double d1, double d2, float var8, int digProgress) {
		if (potato != null && potato.getWorld() != null && !potato.getWorld().isBlockLoaded(potato.getPos(), false)) {
			return;
		}
		
		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.translate(d0, d1, d2);

		Minecraft mc = Minecraft.getMinecraft();
		mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		String name = potato.name.toLowerCase();

		boolean usedShader = false;
		if(name.startsWith("gaia ")) {
			ShaderHelper.useShader(ShaderHelper.doppleganger);
			name = name.substring(5);
			usedShader = true;
		} else if(name.startsWith("hot ")) {
			ShaderHelper.useShader(ShaderHelper.halo);
			name = name.substring(4);
			usedShader = true;
		} else if(name.startsWith("magic ")) {
			ShaderHelper.useShader(ShaderHelper.enchanterRune);
			name = name.substring(6);
			usedShader = true;
		} else if(name.startsWith("gold ")) {
			ShaderHelper.useShader(ShaderHelper.gold);
			name = name.substring(5);
			usedShader = true;
		} else if(name.startsWith("snoop ")) {
			ShaderHelper.useShader(ShaderHelper.terraPlateRune);
			name = name.substring(6);
			usedShader = true;
		}

		float jump = potato.jumpTicks;
		if(jump > 0)
			jump -= var8;

		float up = (float) Math.abs(Math.sin(jump / 10 * Math.PI)) * 0.2F;
		float rotZ = (float) Math.sin(jump / 10 * Math.PI) * 2;

		GlStateManager.translate(0F, up, 0F);
		GlStateManager.rotate(rotZ, 0F, 0F, 1F);

		GlStateManager.pushMatrix();
		if(name.equals("pahimar")) {
			GlStateManager.scale(1F, 0.3F, 1F);
			//GlStateManager.translate(0F, 3.5F, 0F);
		} else if(name.equals("kyle hyde")) {
			// 1.8: handled using getActualState - mc.renderEngine.bindTexture(textureGrayscale);
		} else if(name.equals("dinnerbone") || name.equals("grumm")) {
			GlStateManager.rotate(180F, 0F, 0F, 1F);
			//GlStateManager.translate(0F, -2.625F, 0F);
		} else if(name.equals("aureylian"))
			GlStateManager.color(1F, 0.5F, 1F);

		IBlockState state = potato.getWorld().getBlockState(potato.getPos());
		state = state.getBlock().getActualState(state, potato.getWorld(), potato.getPos());
		IBakedModel bakedModel = mc.getBlockRendererDispatcher().getBlockModelShapes().getModelForState(state);

		boolean render = !(name.equals("mami") || name.equals("soaryn") || name.equals("eloraam") && jump != 0);
		if(render) {
			GlStateManager.rotate(-90, 0, 1, 0);
			mc.getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightness(bakedModel, potato.getWorld().getBlockState(potato.getPos()), 1.0F, false);
			GlStateManager.rotate(90, 0, 1, 0);
			//model.render();
		}
		if(name.equals("kingdaddydmac")) {
			GlStateManager.translate(-0.5F, 0F, 0F);
			mc.getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightness(bakedModel, potato.getWorld().getBlockState(potato.getPos()), 1.0F, false);
			//model.render();
		}

		if(usedShader)
			ShaderHelper.releaseShader();

		GlStateManager.popMatrix();

		if(!name.isEmpty()) {
			GlStateManager.pushMatrix();
			mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);

			ContributorFancinessHandler.firstStart();
			// todo 1.8 refine all below
			float scale = 1F / 4F;
			GlStateManager.translate(0F, 1F, 0F);
			GlStateManager.scale(scale, scale, scale);
			if(name.equals("phi") || name.equals("vazkii")) {
				GlStateManager.translate(0.45F, 0F, 0.4F);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.rotate(20F, 1F, 0F, 1F);
				renderIcon(MiscellaneousIcons.INSTANCE.phiFlowerIcon);

				if(name.equals("vazkii")) {
					GlStateManager.rotate(-20F, 1F, 0F, 1F);
					GlStateManager.scale(1.25F, 1.25F, 1.25F);
					GlStateManager.rotate(180F, 0F, 0F, 1F);
					GlStateManager.translate(-1.5F, -1.3F, -0.75F);
					renderIcon(MiscellaneousIcons.INSTANCE.nerfBatIcon);
				}
			} else if(name.equals("skull kid")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.translate(-0.5F, -1.2F, -0.4F);
				Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ModItems.cosmetic, 1, 23), ItemCameraTransforms.TransformType.THIRD_PERSON);
			} else if(name.equals("kamina")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.translate(-0.5F, -1.1F, -0.4F);
				Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ModItems.cosmetic, 1, 26), ItemCameraTransforms.TransformType.THIRD_PERSON);
			} else if(name.equals("haighyorkie")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-0.5F, -1.2F, -0.4F);
				renderIcon(MiscellaneousIcons.INSTANCE.goldfishIcon);
			} else if(name.equals("chitoge")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.translate(-0.5F, -0.7F, 0.1F);
				Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ModItems.cosmetic, 1, 7), ItemCameraTransforms.TransformType.THIRD_PERSON);
			} else if(name.equals("direwolf20")) {
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.translate(-0.5F, -2.2F, -0.5F);
				Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ModItems.cosmetic, 1, 0), ItemCameraTransforms.TransformType.THIRD_PERSON);
			} else if(name.equals("doctor")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.translate(-0.5F, -1.15F, -0.4F);
				Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ModItems.cosmetic, 1, 25), ItemCameraTransforms.TransformType.THIRD_PERSON);
			} else if(name.equals("snoo")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.translate(-0.5F, -0.7F, 0.1F);
				GlStateManager.rotate(20F, 0F, 0F, 1F);
				Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ModItems.cosmetic, 1, 24), ItemCameraTransforms.TransformType.THIRD_PERSON);
			} else if(name.equals("charlotte")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.translate(-0.5F, -1.2F, -0.4F);
				Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ModItems.cosmetic, 1, 12), ItemCameraTransforms.TransformType.THIRD_PERSON);
			} else if(name.equals("greg") || name.equals("gregorioust")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-0.5F, -1.5F, -0.4F);
				Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(Items.book, 1, 0), ItemCameraTransforms.TransformType.THIRD_PERSON);

				mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
				GlStateManager.translate(0.5F, 0.5F, 0F);
				GlStateManager.scale(0.3F, 0.3F, 0.3F);

				mc.getBlockRendererDispatcher().renderBlockBrightness(Blocks.iron_ore.getDefaultState(), 1F);
			} else if(name.equals("profmobius")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-0.5F, -1.2F, -0.4F);
				Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(Items.bread, 1, 0), ItemCameraTransforms.TransformType.THIRD_PERSON);
			} else if(name.equals("martysgames") || name.equals("marty")) {
				GlStateManager.scale(0.7F, 0.7F, 0.7F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.translate(-0.75F, -2.4F, -0.7F);
				GlStateManager.rotate(10F, 0F, 0F, 1F);
				ItemStack renderStack = new ItemStack(ModItems.infiniteFruit, 1);
				renderStack.setStackDisplayName("das boot");
				Minecraft.getMinecraft().getRenderItem().renderItem(renderStack, ItemCameraTransforms.TransformType.THIRD_PERSON);
			} else if(name.equals("tromped")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-0.5F, -1.2F, -0.4F);
				Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ModItems.cacophonium, 1, 0), ItemCameraTransforms.TransformType.THIRD_PERSON);
			} else if(name.equals("kain vinosec")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-0.3F, -1.5F, -0.4F);
				Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ModItems.recordGaia1, 1, 0), ItemCameraTransforms.TransformType.THIRD_PERSON);
				GlStateManager.translate(0F, 0F, 0.85F);
				Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ModItems.recordGaia2, 1, 0), ItemCameraTransforms.TransformType.THIRD_PERSON);
			} else if(name.equals("mankrik")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.translate(-0.5F, -0.2F, -0.1F);
				Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ModItems.cosmetic, 1, 31), ItemCameraTransforms.TransformType.THIRD_PERSON);
			} else if(name.equals("kurumi")) {
				GlStateManager.scale(0.4F, 0.4F, 0.4F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.translate(-0.9F, -2.5F, -1.3F);
				Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ModItems.cosmetic, 1, 17), ItemCameraTransforms.TransformType.THIRD_PERSON);
			} else if(name.equals("ichun")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.translate(-0.5F, -1.2F, -0.4F);
				Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ModItems.cosmetic, 1, 15), ItemCameraTransforms.TransformType.THIRD_PERSON);
			} else if(name.equals("wiiv") || name.equals("dylan4ever") || name.equals("dylankaiser")) {
				GlStateManager.scale(1.5F, 1.5F, 1.5F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-0.5F, -1.1F, -0.325F);
				Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(Items.painting, 1, 0), ItemCameraTransforms.TransformType.THIRD_PERSON);
			} else if(name.equals("jibril")) {
				GlStateManager.scale(1.5F, 1.5F, 1.5F);
				GlStateManager.translate(0F, 0.7F, 0F);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				ItemFlightTiara.renderHalo(null, var8);
			} else if(name.equals("nebris")) {
				mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
				GlStateManager.rotate(180F, 1F, 0F, 0F);
				mc.getBlockRendererDispatcher().renderBlockBrightness(Blocks.glowstone.getDefaultState(), 1F);
			} else if(name.equals("ible")) {
				mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
				GlStateManager.scale(1.2F, 1.2F, 1.2F);
				GlStateManager.translate(0F, 0.7F, 0F);
				GlStateManager.rotate(180F, 1F, 0F, 0F);
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				mc.getBlockRendererDispatcher().renderBlockBrightness(Blocks.portal.getDefaultState(), 1F);
			} else if(name.equals("razz") || name.equals("razzleberryfox")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-0.5F, -1F, 0.45F);
				Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ModItems.cosmetic, 1, 8), ItemCameraTransforms.TransformType.THIRD_PERSON);
			} else if(name.equals("etho") || name.equals("ethoslab")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-0.5F, -1.2F, -0.4F);
				Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(Items.cookie, 1, 0), ItemCameraTransforms.TransformType.THIRD_PERSON);
			} else if(name.equals("sethbling")) {
				mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
				GlStateManager.scale(1.2F, 1.2F, 1.2F);
				GlStateManager.translate(0F, 0.9F, 0F);
				GlStateManager.rotate(180F, 1F, 0F, 0F);
				mc.getBlockRendererDispatcher().renderBlockBrightness(Blocks.command_block.getDefaultState(), 1F);
			} else if(name.equals("bdoubleo100") || name.equals("bdoubleo")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.translate(-1F, -1.1F, -0.1F);
				Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(Items.stick, 1, 0), ItemCameraTransforms.TransformType.THIRD_PERSON);
			} else if(name.equals("kingdaddydmac")) {
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-0.3F, -2.5F, 1.075F);
				Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ModItems.manaRing, 1, 0), ItemCameraTransforms.TransformType.THIRD_PERSON);
				GlStateManager.translate(0F, 0F, -4F);
				Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ModItems.manaRing, 1, 0), ItemCameraTransforms.TransformType.THIRD_PERSON);

				mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
				GlStateManager.scale(1.5F, 1.5F, 1.5F);
				GlStateManager.translate(1.5F, -0.5F, 0.7F);
				mc.getBlockRendererDispatcher().renderBlockBrightness(Blocks.cake.getDefaultState(), 1.0F);
			} else if(name.equals("sjin")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.translate(-0.5F, -1.27F, -0.4F);
				Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ModItems.cosmetic, 1, 27), ItemCameraTransforms.TransformType.THIRD_PERSON);
			} else if(name.equals("martyn") || name.equals("inthelittlewood")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.translate(-0.5F, -0.45F, -0.1F);
				mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
				Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(Blocks.sapling, 1, 0), ItemCameraTransforms.TransformType.THIRD_PERSON);
			}else if(ContributorFancinessHandler.flowerMap != null && ContributorFancinessHandler.flowerMap.containsKey(name)) {
				ItemStack icon = ContributorFancinessHandler.flowerMap.get(name);
				if(icon != null) {
					mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
					GlStateManager.rotate(180F, 1F, 0F, 0F);
					GlStateManager.translate(-0.5F, -0.5F, 0F);
					ShaderHelper.useShader(ShaderHelper.gold);
					Minecraft.getMinecraft().getRenderItem().renderItem(icon, ItemCameraTransforms.TransformType.THIRD_PERSON);
					ShaderHelper.releaseShader();
				}
			}

			GlStateManager.popMatrix();
		}

		MinecraftForge.EVENT_BUS.post(new TinyPotatoRenderEvent(potato, potato.name, d0, d1, d2, var8));

		GlStateManager.rotate(-rotZ, 0F, 0F, 1F);
		GlStateManager.color(1F, 1F, 1F);

		MovingObjectPosition pos = mc.objectMouseOver;
		if(!name.isEmpty() && pos != null && potato.getPos().equals(pos.getBlockPos())) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.5F, 0.75F, 0.5F);
			GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
			float f = 1.6F;
			float f1 = 0.016666668F * f;
			GlStateManager.scale(-f1, -f1, f1);
			GlStateManager.disableLighting();
			GlStateManager.translate(0.0F, 0F / f1, 0.0F);
			GlStateManager.depthMask(false);
			GlStateManager.enableBlend();
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			Tessellator tessellator = Tessellator.getInstance();
			GlStateManager.disableTexture2D();
			tessellator.getWorldRenderer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			int i = mc.fontRendererObj.getStringWidth(potato.name) / 2;
			tessellator.getWorldRenderer().pos(-i - 1, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			tessellator.getWorldRenderer().pos(-i - 1, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			tessellator.getWorldRenderer().pos(i + 1, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			tessellator.getWorldRenderer().pos(i + 1, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			tessellator.draw();
			GlStateManager.enableTexture2D();
			GlStateManager.depthMask(true);
			mc.fontRendererObj.drawString(potato.name, -mc.fontRendererObj.getStringWidth(potato.name) / 2, 0, 0xFFFFFF);
			if(name.equals("pahimar") || name.equals("soaryn")) {
				GlStateManager.translate(0F, 14F, 0F);
				String s = name.equals("pahimar") ? "[WIP]" : "(soon)";
				GlStateManager.depthMask(false);
				GlStateManager.enableBlend();
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);
				GlStateManager.disableTexture2D();
				tessellator.getWorldRenderer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
				i = mc.fontRendererObj.getStringWidth(s) / 2;
				tessellator.getWorldRenderer().pos(-i - 1, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
				tessellator.getWorldRenderer().pos(-i - 1, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
				tessellator.getWorldRenderer().pos(i + 1, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
				tessellator.getWorldRenderer().pos(i + 1, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
				tessellator.draw();
				GlStateManager.enableTexture2D();
				GlStateManager.depthMask(true);
				mc.fontRendererObj.drawString(s, -mc.fontRendererObj.getStringWidth(s) / 2, 0, 0xFFFFFF);
			}

			GlStateManager.enableLighting();
			GlStateManager.disableBlend();
			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.scale(1F / -f1, 1F / -f1, 1F / f1);
			GlStateManager.popMatrix();
		}

		GlStateManager.popMatrix();
	}

	public void renderIcon(TextureAtlasSprite icon) {
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
		IconHelper.renderIconIn3D(Tessellator.getInstance(), f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);
	}
}