/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 24, 2014, 7:02:37 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import java.awt.Color;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.opengl.GL11;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IExtendedWireframeCoordinateListProvider;
import vazkii.botania.api.item.IWireframeCoordinateListProvider;
import vazkii.botania.api.wand.ICoordBoundItem;
import vazkii.botania.api.wand.IWireframeAABBProvider;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.common.lib.LibObfuscation;

public final class BoundTileRenderer {

	@SubscribeEvent
	public void onWorldRenderLast(RenderWorldLastEvent event) {
		GlStateManager.pushMatrix();
		GL11.glPushAttrib(GL11.GL_LIGHTING);
		GlStateManager.disableDepth();
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();

		// todo 1.8 Tessellator.renderingWorldRenderer = false;

		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		ItemStack stack = player.getCurrentEquippedItem();
		int color = Color.HSBtoRGB(ClientTickHandler.ticksInGame % 200 / 200F, 0.6F, 1F);
		if(stack != null && stack.getItem() instanceof ICoordBoundItem) {
			BlockPos coords = ((ICoordBoundItem) stack.getItem()).getBinding(stack);
			if(coords != null)
				renderBlockOutlineAt(coords, color);
		}

		IInventory mainInv = player.inventory;
		IInventory baublesInv = BotaniaAPI.internalHandler.getBaublesInventory(player);

		int invSize = mainInv.getSizeInventory();
		int size = invSize;
		if(baublesInv != null)
			size += baublesInv.getSizeInventory();

		for(int i = 0; i < size; i++) {
			boolean useBaubles = i >= invSize;
			IInventory inv = useBaubles ? baublesInv : mainInv;
			ItemStack stackInSlot = inv.getStackInSlot(i - (useBaubles ? invSize : 0));

			if(stackInSlot != null && stackInSlot.getItem() instanceof IWireframeCoordinateListProvider) {
				IWireframeCoordinateListProvider provider = (IWireframeCoordinateListProvider) stackInSlot.getItem();
				List<BlockPos> coordsList = provider.getWireframesToDraw(player, stackInSlot);
				if(coordsList != null)
					for(BlockPos coords : coordsList)
						renderBlockOutlineAt(coords, color);

				if(stackInSlot.getItem() instanceof IExtendedWireframeCoordinateListProvider) {
					BlockPos coords = ((IExtendedWireframeCoordinateListProvider) stackInSlot.getItem()).getSourceWireframe(player, stackInSlot);
					if(coords != null && coords.getY() > -1)
						renderBlockOutlineAt(coords, color, 5F);
				}
			}
		}

		GlStateManager.enableDepth();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GL11.glPopAttrib();
		GlStateManager.popMatrix();
	}

	private void renderBlockOutlineAt(BlockPos pos, int color) {
		renderBlockOutlineAt(pos, color, 1F);
	}

	private double getRenderPosX() { // todo 1.8
		return ObfuscationReflectionHelper.getPrivateValue(RenderManager.class, Minecraft.getMinecraft().getRenderManager(), LibObfuscation.RENDERPOSX);
	}

	private double getRenderPosY() {
		return ObfuscationReflectionHelper.getPrivateValue(RenderManager.class, Minecraft.getMinecraft().getRenderManager(), LibObfuscation.RENDERPOSY);
	}

	private double getRenderPosZ() {
		return ObfuscationReflectionHelper.getPrivateValue(RenderManager.class, Minecraft.getMinecraft().getRenderManager(), LibObfuscation.RENDERPOSZ);
	}

	private void renderBlockOutlineAt(BlockPos pos, int color, float thickness) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(pos.getX() - getRenderPosX(), pos.getY() - getRenderPosY(), pos.getZ() - getRenderPosZ() + 1);
		Color colorRGB = new Color(color);
		GlStateManager.color((byte) colorRGB.getRed(), (byte) colorRGB.getGreen(), (byte) colorRGB.getBlue(), (byte) 255);

		World world = Minecraft.getMinecraft().theWorld;
		Block block = world.getBlockState(pos).getBlock();
		drawWireframe : {
			if(block != null) {
				AxisAlignedBB axis;

				if(block instanceof IWireframeAABBProvider)
					axis = ((IWireframeAABBProvider) block).getWireframeAABB(world, pos);
				else axis = block.getSelectedBoundingBox(world, pos);

				if(axis == null)
					break drawWireframe;

				axis = axis.offset(-pos.getX(), -pos.getY(), -(pos.getZ() + 1));

				GlStateManager.scale(1F, 1F, 1F);

				GL11.glLineWidth(thickness);
				renderBlockOutline(axis);

				GL11.glLineWidth(thickness + 3F);
				GL11.glColor3ub((byte) colorRGB.getRed(), (byte) colorRGB.getGreen(), (byte) colorRGB.getBlue());
				renderBlockOutline(axis);
			}
		}

		GlStateManager.popMatrix();
	}

	private void renderBlockOutline(AxisAlignedBB aabb) {
		Tessellator tessellator = Tessellator.getInstance();

		double ix = aabb.minX;
		double iy = aabb.minY;
		double iz = aabb.minZ;
		double ax = aabb.maxX;
		double ay = aabb.maxY;
		double az = aabb.maxZ;

		tessellator.getWorldRenderer().begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
		tessellator.getWorldRenderer().pos(ix, iy, iz).endVertex();
		tessellator.getWorldRenderer().pos(ix, ay, iz).endVertex();

		tessellator.getWorldRenderer().pos(ix, ay, iz).endVertex();
		tessellator.getWorldRenderer().pos(ax, ay, iz).endVertex();

		tessellator.getWorldRenderer().pos(ax, ay, iz).endVertex();
		tessellator.getWorldRenderer().pos(ax, iy, iz).endVertex();

		tessellator.getWorldRenderer().pos(ax, iy, iz).endVertex();
		tessellator.getWorldRenderer().pos(ix, iy, iz).endVertex();

		tessellator.getWorldRenderer().pos(ix, iy, az).endVertex();
		tessellator.getWorldRenderer().pos(ix, ay, az).endVertex();

		tessellator.getWorldRenderer().pos(ix, iy, az).endVertex();
		tessellator.getWorldRenderer().pos(ax, iy, az).endVertex();

		tessellator.getWorldRenderer().pos(ax, iy, az).endVertex();
		tessellator.getWorldRenderer().pos(ax, ay, az).endVertex();

		tessellator.getWorldRenderer().pos(ix, ay, az).endVertex();
		tessellator.getWorldRenderer().pos(ax, ay, az).endVertex();

		tessellator.getWorldRenderer().pos(ix, iy, iz).endVertex();
		tessellator.getWorldRenderer().pos(ix, iy, az).endVertex();

		tessellator.getWorldRenderer().pos(ix, ay, iz).endVertex();
		tessellator.getWorldRenderer().pos(ix, ay, az).endVertex();

		tessellator.getWorldRenderer().pos(ax, iy, iz).endVertex();
		tessellator.getWorldRenderer().pos(ax, iy, az).endVertex();

		tessellator.getWorldRenderer().pos(ax, ay, iz).endVertex();
		tessellator.getWorldRenderer().pos(ax, ay, az).endVertex();

		tessellator.draw();
	}
}
