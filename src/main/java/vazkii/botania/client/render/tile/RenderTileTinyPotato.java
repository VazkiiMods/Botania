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
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.MinecraftForge;
import vazkii.botania.api.item.TinyPotatoRenderEvent;
import vazkii.botania.api.state.BotaniaStateProps;
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
import vazkii.botania.common.item.block.ItemBlockTinyPotato;
import vazkii.botania.common.item.equipment.bauble.ItemBaubleCosmetic;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class RenderTileTinyPotato extends TileEntitySpecialRenderer<TileTinyPotato> {
	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_TINY_POTATO);
	private static final ResourceLocation textureGrayscale = new ResourceLocation(LibResources.MODEL_TINY_POTATO_GS);
	private static final ResourceLocation textureHalloween = new ResourceLocation(LibResources.MODEL_TINY_POTATO_HALLOWEEN);
	private static final ModelTinyPotato model = new ModelTinyPotato();
	private final ItemStack[] cosmetics = Arrays.stream(ItemBaubleCosmetic.Variants.values()).map(v -> new ItemStack(ModItems.cosmetic, 1, v.ordinal())).toArray(ItemStack[]::new);


	private static boolean matches(String name, String match) {
		return name.equals(match) || name.startsWith(match + " ");
	}

	private static String removeFromFront(String name, String match) {
		return name.substring(match.length()).trim();
	}

	@Override
	public void render(@Nonnull TileTinyPotato potato, double x, double y, double z, float partialTicks, int destroyStage, float unused) {
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

		GlStateManager.pushMatrix();
		mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		GlStateManager.pushMatrix();
		GlStateManager.rotate(180F, 0, 0, 1);
		GlStateManager.translate(0F, -1F, 0F);
		float s = 1F / 3.5F;
		GlStateManager.scale(s, s, s);

		EnumFacing potatoFacing = potato.getWorld().getBlockState(potato.getPos()).getValue(BotaniaStateProps.CARDINALS);

		for(int i = 0; i < potato.getSizeInventory(); i++) {
			ItemStack stack = potato.getItemHandler().getStackInSlot(i);
			if(stack.isEmpty())
				continue;

			GlStateManager.pushMatrix();
			EnumFacing side = EnumFacing.class.getEnumConstants()[i];
			if(side.getAxis() != Axis.Y) {
				float sideAngle = side.getHorizontalAngle() - potatoFacing.getHorizontalAngle();
				side = EnumFacing.fromAngle(sideAngle);
			}

			boolean block = stack.getItem() instanceof ItemBlock;
			boolean mySon = stack.getItem() instanceof ItemBlockTinyPotato;

			switch(side) {
			case UP:
				if(mySon)
					GlStateManager.translate(0F, 0.6F, 0.5F);
				else if(block)
					GlStateManager.translate(0F, 0.3F, 0.5F);
				GlStateManager.translate(0F, -0.5F, -0.4F);
				break;
			case DOWN:
				GlStateManager.translate(0, -2.3, -0.88);
				if(mySon)
					GlStateManager.translate(0, .65, 0.6);
				else if(block)
					GlStateManager.translate(0, 1, 0.6);
				break;
			case NORTH:
				GlStateManager.translate(0, -1.9, 0.02);
				if(mySon)
					GlStateManager.translate(0, 1, 0.6);
				else if(block)
					GlStateManager.translate(0, 1, 0.6);
				break;
			case SOUTH:
				GlStateManager.translate(0, -1.6, -0.89);
				if(mySon)
					GlStateManager.translate(0, 1.4, 0.5);
				else if(block)
					GlStateManager.translate(0, 1.0, 0.5);
				break;
			case EAST:
				if(mySon)
					GlStateManager.translate(-0.4F, 0.65F, 0F);
				else if(block)
					GlStateManager.translate(-0.4F, 0.8F, 0F);
				else GlStateManager.rotate(-90F, 0F, 1F, 0F);
				GlStateManager.translate(-0.3F, -1.9F, 0.04F);
				break;
			case WEST:
				if(mySon)
					GlStateManager.translate(1F, 0.65F, 1F);
				else if(block)
					GlStateManager.translate(1F, 0.8F, 1F);
				else GlStateManager.rotate(-90F, 0F, 1F, 0F);
				GlStateManager.translate(-0.3F, -1.9F, -0.92F);
				break;
			}

			if (mySon)
				GlStateManager.scale(1.1, 1.1, 1.1);
			else if(block)
				GlStateManager.scale(0.5, 0.5, 0.5);
			if(block && side != EnumFacing.NORTH)
				GlStateManager.rotate(180F, 0, 1, 0);
			renderItem(stack);
			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();

		if (!name.isEmpty()) {
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
			} else if (name.equals("haighyorkie")) {
				GlStateManager.scale(1.25F, 1.25F, 1.25F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-0.5F, -1.2F, -0.4F);
				renderIcon(MiscellaneousIcons.INSTANCE.goldfishIcon);
			} else if (name.equals("martysgames") || name.equals("marty")) {
				GlStateManager.scale(0.7F, 0.7F, 0.7F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.translate(-0.3F, -2.7F, -1.2F);
				GlStateManager.rotate(15F, 0F, 0F, 1F);
				renderItem(new ItemStack(ModItems.infiniteFruit, 1).setStackDisplayName("das boot"));
			} else if (name.equals("jibril")) {
				GlStateManager.scale(1.5F, 1.5F, 1.5F);
				GlStateManager.translate(0F, -0.8F, 0F);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				ItemFlightTiara.renderHalo(null, partialTicks);
				GlStateManager.disableBlend();
				GlStateManager.disableLighting();
			} else if (name.equals("kingdaddydmac")) {
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(0F, -3F, 0.65F);
				renderItem(new ItemStack(ModItems.manaRing, 1, 0));
				GlStateManager.translate(0F, 0F, -4F);
				renderItem(new ItemStack(ModItems.manaRing, 1, 0));

				GlStateManager.scale(0.8F, 0.8F, 0.8F);
				GlStateManager.translate(1.25F, -1.25F, 2.25F);
				mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				renderBlock(Blocks.CAKE);
			} else if (ContributorFancinessHandler.flowerMap != null && ContributorFancinessHandler.flowerMap.containsKey(name)) {
				ItemStack icon = ContributorFancinessHandler.flowerMap.getOrDefault(name, ItemStack.EMPTY);
				if (!icon.isEmpty()) {
					mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
					GlStateManager.rotate(180F, 1F, 0F, 0F);
					GlStateManager.rotate(180F, 0F, 1F, 0F);
					GlStateManager.translate(0F, 0F, 0F);
					ShaderHelper.useShader(ShaderHelper.gold);
					renderItem(icon);
					ShaderHelper.releaseShader();
				}
			}
		}
		GlStateManager.popMatrix();

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
			BufferBuilder worldrenderer = tessellator.getBuffer();
			GlStateManager.disableTexture2D();
			worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
			int i = mc.fontRenderer.getStringWidth(potato.name) / 2;
			worldrenderer.pos(-i - 1, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			worldrenderer.pos(-i - 1, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			worldrenderer.pos(i + 1, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			worldrenderer.pos(i + 1, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
			tessellator.draw();
			GlStateManager.enableTexture2D();
			GlStateManager.depthMask(true);
			mc.fontRenderer.drawString(potato.name, -mc.fontRenderer.getStringWidth(potato.name) / 2, 0, 0xFFFFFF);
			if (name.equals("pahimar") || name.equals("soaryn")) {
				GlStateManager.translate(0F, 14F, 0F);
				String str = name.equals("pahimar") ? "[WIP]" : "(soon)";
				GlStateManager.depthMask(false);
				GlStateManager.enableBlend();
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);
				GlStateManager.disableTexture2D();
				worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
				i = mc.fontRenderer.getStringWidth(str) / 2;
				worldrenderer.pos(-i - 1, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
				worldrenderer.pos(-i - 1, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
				worldrenderer.pos(i + 1, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
				worldrenderer.pos(i + 1, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
				tessellator.draw();
				GlStateManager.enableTexture2D();
				GlStateManager.depthMask(true);
				mc.fontRenderer.drawString(str, -mc.fontRenderer.getStringWidth(str) / 2, 0, 0xFFFFFF);
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
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(block.getDefaultState(), 1.0F);
	}
}
