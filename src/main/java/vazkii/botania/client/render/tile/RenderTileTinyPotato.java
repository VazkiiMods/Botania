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

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.item.TinyPotatoRenderEvent;
import vazkii.botania.client.core.handler.ContributorFancinessHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelTinyPotato;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileTinyPotato;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;

import javax.annotation.Nonnull;

public class RenderTileTinyPotato extends TileEntitySpecialRenderer<TileTinyPotato> {
	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_TINY_POTATO);
	private static final ResourceLocation textureGrayscale = new ResourceLocation(LibResources.MODEL_TINY_POTATO_GS);
	private static final ResourceLocation textureHalloween = new ResourceLocation(LibResources.MODEL_TINY_POTATO_HALLOWEEN);
	private static final ModelTinyPotato model = new ModelTinyPotato();


	private static boolean matches(String name, String match) {
		return name.equals(match) || name.startsWith(match + " ");
	}

	private static String removeFromFront(String name, String match) {
		return name.substring(match.length()).trim();
	}

	@Override
	public void renderTileEntityAt(@Nonnull TileTinyPotato potato, double x, double y, double z, float partialTicks, int destroyStage) {
		if(!potato.getWorld().isBlockLoaded(potato.getPos(), false)
				|| potato.getWorld().getBlockState(potato.getPos()).getBlock() != ModBlocks.tinyPotato)
			return;

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.translate(x, y, z);

		Minecraft mc = Minecraft.getMinecraft();
		mc.renderEngine.bindTexture(ClientProxy.dootDoot ? textureHalloween : texture);
		String name = potato.name.toLowerCase().trim();

		boolean usedShader = false;
		if (matches(name, "gaia")) {
			ShaderHelper.useShader(ShaderHelper.doppleganger);
			name = removeFromFront(name, "gaia");
			usedShader = true;
		} else if (matches(name, "hot")) {
			ShaderHelper.useShader(ShaderHelper.halo);
			name = removeFromFront(name, "hot");
			usedShader = true;
		} else if (matches(name, "magic")) {
			ShaderHelper.useShader(ShaderHelper.enchanterRune);
			name = removeFromFront(name, "magic");
			usedShader = true;
		} else if (matches(name, "gold")) {
			ShaderHelper.useShader(ShaderHelper.gold);
			name = removeFromFront(name, "gold");
			usedShader = true;
		} else if (matches(name, "snoop")) {
			ShaderHelper.useShader(ShaderHelper.terraPlateRune);
			name = removeFromFront(name, "snoop");
			usedShader = true;
		}

		GlStateManager.translate(0.5F, 1.5F, 0.5F);
		GlStateManager.scale(1F, -1F, -1F);
		int meta = potato.getWorld() == null ? 3 : potato.getBlockMetadata();
		float rotY = meta * 90F - 180F;
		GlStateManager.rotate(rotY, 0F, 1F, 0F);

		float jump = potato.jumpTicks;
		if (jump > 0)
			jump -= partialTicks;

		float up = (float) -Math.abs(Math.sin(jump / 10 * Math.PI)) * 0.2F;
		float rotZ = (float) Math.sin(jump / 10 * Math.PI) * 2;

		GlStateManager.translate(0F, up, 0F);
		GlStateManager.rotate(rotZ, 0F, 0F, 1F);

		GlStateManager.pushMatrix();
		switch (name) {
			case "pahimar":
				GlStateManager.scale(1F, 0.3F, 1F);
				GlStateManager.translate(0F, 3.5F, 0F);
				break;
			case "kyle hyde":
				mc.renderEngine.bindTexture(textureGrayscale);
				break;
			case "dinnerbone":
			case "grumm":
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.translate(0F, -2.625F, 0F);
				break;
			case "aureylian":
				GlStateManager.color(1F, 0.5F, 1F);
				break;
		}


		boolean render = !(name.equals("mami") || name.equals("soaryn") || name.equals("eloraam") && jump != 0);
		if (render)
			model.render();
		if (name.equals("kingdaddydmac")) {
			GlStateManager.translate(0.5F, 0F, 0F);
			model.render();
		}

		if (usedShader)
			ShaderHelper.releaseShader();

		GlStateManager.popMatrix();

		if (!name.isEmpty()) {
			GlStateManager.pushMatrix();
			mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			ContributorFancinessHandler.firstStart();

			float scale = 1F / 4F;
			GlStateManager.translate(0F, 1F, 0F);
			GlStateManager.scale(scale, scale, scale);
			if (name.equals("phi") || name.equals("vazkii")) {
				GlStateManager.translate(0.45F, 0F, 0.4F);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.rotate(20F, 1F, 0F, 1F);
				renderIcon(MiscellaneousIcons.INSTANCE.phiFlowerIcon);

				if (name.equals("vazkii")) {
					GlStateManager.rotate(-20F, 1F, 0F, 1F);
					GlStateManager.scale(1.25F, 1.25F, 1.25F);
					GlStateManager.rotate(180F, 0F, 0F, 1F);
					GlStateManager.translate(-1.5F, -1.3F, -0.75F);
					renderIcon(MiscellaneousIcons.INSTANCE.nerfBatIcon);
				}
			} else if (name.equals("skull kid")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.translate(0F, -0.7F, -0.4F);
				renderItem(new ItemStack(ModItems.cosmetic, 1, 23));
			} else if (name.equals("kamina")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(0F, -0.6F, 0.4F);
				renderItem(new ItemStack(ModItems.cosmetic, 1, 26));
			} else if (name.equals("haighyorkie")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-0.5F, -1.2F, -0.4F);
				renderIcon(MiscellaneousIcons.INSTANCE.goldfishIcon);
			} else if (name.equals("chitoge")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(-180F, 0F, 1F, 0F);
				GlStateManager.translate(0F, -0.75F, -0.4F);
				renderItem(new ItemStack(ModItems.cosmetic, 1, 7));
			} else if (name.equals("direwolf20")) {
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(0F, -2.5F, 0.125F);
				renderItem(new ItemStack(ModItems.cosmetic));
			} else if (name.equals("doctor")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(0F, -1.45F, 0F);
				renderItem(new ItemStack(ModItems.cosmetic, 1, 25));
			} else if (name.equals("snoo")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(0.2F, 0F, -0.05F);
				GlStateManager.rotate(-20F, 0F, 0F, 1F);
				renderItem(new ItemStack(ModItems.cosmetic, 1, 24));
			} else if (name.equals("charlotte")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(0F, -1.6F, 0F);
				renderItem(new ItemStack(ModItems.cosmetic, 1, 12));
			} else if (name.equals("greg") || name.equals("gregorioust")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(270F, 0F, 1F, 0F);
				GlStateManager.translate(0F, -1F, 0.4F);
				renderItem(new ItemStack(Items.BOOK));

				mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				GlStateManager.scale(0.3F, 0.3F, 0.3F);
				renderItem(new ItemStack(Blocks.IRON_ORE));
			} else if (name.equals("profmobius")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(270F, 0F, 1F, 0F);
				GlStateManager.translate(0F, -0.7F, 0.4F);
				renderItem(new ItemStack(Items.BREAD));
			} else if (name.equals("martysgames") || name.equals("marty")) {
				GlStateManager.scale(0.7F, 0.7F, 0.7F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(0.3F, -1.8F, 0.7F);
				GlStateManager.rotate(-15F, 0F, 0F, 1F);
				renderItem(new ItemStack(ModItems.infiniteFruit, 1).setStackDisplayName("das boot"));
			} else if (name.equals("tromped")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(270F, 0F, 1F, 0F);
				GlStateManager.translate(0F, -0.7F, 0.4F);
				renderItem(new ItemStack(ModItems.cacophonium));
			} else if (name.equals("kain vinosec")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-0.3F, -1F, 0.4F);
				renderItem(new ItemStack(ModItems.recordGaia1));
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
				GlStateManager.translate(0F, 0F, -0.85F);
				renderItem(new ItemStack(ModItems.recordGaia2));
			} else if (name.equals("mankrik")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(0F, 0.3F, 0.1F);
				renderItem(new ItemStack(ModItems.cosmetic, 1, 31));
			} else if (name.equals("kurumi")) {
				GlStateManager.scale(0.4F, 0.4F, 0.4F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(0.4F, -2F, 1.3F);
				renderItem(new ItemStack(ModItems.cosmetic, 1, 17));
			} else if (name.equals("ichun")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(0F, -1.45F, 0F);
				renderItem(new ItemStack(ModItems.cosmetic, 1, 15));
			} else if (name.equals("wiiv") || name.equals("dylan4ever") || name.equals("dylankaiser")) {
				GlStateManager.scale(1.5F, 1.5F, 1.5F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-0.1F, -0.6F, -0.325F);
				renderItem(new ItemStack(Items.PAINTING));
			} else if (name.equals("jibril")) {
				GlStateManager.scale(1.5F, 1.5F, 1.5F);
				GlStateManager.translate(0F, -0.8F, 0F);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				ItemFlightTiara.renderHalo(null, partialTicks);
				GlStateManager.disableBlend();
				GlStateManager.disableLighting();
			} else if (name.equals("nebris")) {
				GlStateManager.scale(2F, 2F, 2F);
				GlStateManager.rotate(180F, 1F, 0F, 0F);
				mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				renderItem(new ItemStack(Blocks.GLOWSTONE));
			} else if (name.equals("ible")) {
				mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				GlStateManager.scale(1.2F, 1.2F, 1.2F);
				GlStateManager.translate(-0.5F, 0F, -0.5F);
				GlStateManager.rotate(180F, 1F, 1F, 0F);
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				renderBlock(Blocks.PORTAL);
			} else if (name.equals("razz") || name.equals("razzleberryfox")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(0, -0.5F, 0.45F);
				renderItem(new ItemStack(ModItems.cosmetic, 1, 8));
			} else if (name.equals("etho") || name.equals("ethoslab")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(0F, -0.7F, 0.4F);
				renderItem(new ItemStack(Items.COOKIE));
			} else if (name.equals("sethbling")) {
				GlStateManager.scale(1.2F, 1.2F, 1.2F);
				GlStateManager.rotate(-90F, 0F, 1F, 0F);
				GlStateManager.translate(-0.5F, 1.4F, -0.5F);
				GlStateManager.rotate(180F, 1F, 0F, 0F);
				mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				renderBlock(Blocks.COMMAND_BLOCK);
			} else if (name.equals("bdoubleo100") || name.equals("bdoubleo")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(0.5F, -0.6F, 0.1F);
				renderItem(new ItemStack(Items.STICK));
			} else if (name.equals("kingdaddydmac")) {
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(270F, 0F, 1F, 0F);
				GlStateManager.translate(-0.3F, -2F, -1.075F);
				renderItem(new ItemStack(ModItems.manaRing, 1, 0));
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
				GlStateManager.translate(0F, 0F, 4F);
				renderItem(new ItemStack(ModItems.manaRing, 1, 0));

				GlStateManager.scale(0.8F, 0.8F, 0.8F);
				GlStateManager.translate(-1.7F, -1.2F, -0.1F);
				mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				renderBlock(Blocks.CAKE);
			} else if (name.equals("sjin")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(0F, -1.6F, 0F);
				renderItem(new ItemStack(ModItems.cosmetic, 1, 27));
			} else if (name.equals("martyn") || name.equals("inthelittlewood")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(0F, 0.05F, 0.1F);
				mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				renderItem(new ItemStack(Blocks.SAPLING, 0, 0));
			} else if (ContributorFancinessHandler.flowerMap != null && ContributorFancinessHandler.flowerMap.containsKey(name)) {
				ItemStack icon = ContributorFancinessHandler.flowerMap.get(name);
				if (icon != null) {
					mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
					GlStateManager.rotate(180F, 1F, 0F, 0F);
					GlStateManager.rotate(180F, 0F, 1F, 0F);
					GlStateManager.translate(0F, 0F, 0F);
					ShaderHelper.useShader(ShaderHelper.gold);
					renderItem(icon);
					ShaderHelper.releaseShader();
				}
			}

			GlStateManager.popMatrix();
		}

		MinecraftForge.EVENT_BUS.post(new TinyPotatoRenderEvent(potato, potato.name, x, y, z, partialTicks, destroyStage));

		GlStateManager.rotate(-rotZ, 0F, 0F, 1F);
		GlStateManager.rotate(-rotY, 0F, 1F, 0F);
		GlStateManager.color(1F, 1F, 1F);
		GlStateManager.scale(1F, -1F, -1F);

		RayTraceResult pos = mc.objectMouseOver;
		if (!name.isEmpty() && pos != null && pos.getBlockPos() != null && potato.getPos().equals(pos.getBlockPos())) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(0F, -0.6F, 0F);
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
			VertexBuffer worldrenderer = tessellator.getBuffer();
			GlStateManager.disableTexture2D();
			worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
			int i = mc.fontRendererObj.getStringWidth(potato.name) / 2;
			worldrenderer.pos(-i - 1, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			worldrenderer.pos(-i - 1, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			worldrenderer.pos(i + 1, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			worldrenderer.pos(i + 1, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			tessellator.draw();
			GlStateManager.enableTexture2D();
			GlStateManager.depthMask(true);
			mc.fontRendererObj.drawString(potato.name, -mc.fontRendererObj.getStringWidth(potato.name) / 2, 0, 0xFFFFFF);
			if (name.equals("pahimar") || name.equals("soaryn")) {
				GlStateManager.translate(0F, 14F, 0F);
				String s = name.equals("pahimar") ? "[WIP]" : "(soon)";
				GlStateManager.depthMask(false);
				GlStateManager.enableBlend();
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);
				GlStateManager.disableTexture2D();
				worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
				i = mc.fontRendererObj.getStringWidth(s) / 2;
				worldrenderer.pos(-i - 1, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
				worldrenderer.pos(-i - 1, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
				worldrenderer.pos(i + 1, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
				worldrenderer.pos(i + 1, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
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

	private void renderIcon(TextureAtlasSprite icon) {
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
		IconHelper.renderIconIn3D(Tessellator.getInstance(), f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);
	}

	private void renderItem(ItemStack stack) {
		Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.HEAD);
	}

	private void renderBlock(Block block) {
		Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(block.getDefaultState(), 1.0F);
	}
}
