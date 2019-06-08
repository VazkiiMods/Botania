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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IWireframeCoordinateListProvider;
import vazkii.botania.api.wand.ICoordBoundItem;
import vazkii.botania.api.wand.IWireframeAABBProvider;

import java.awt.Color;
import java.util.List;

public final class BoundTileRenderer {

	private BoundTileRenderer() {}

	@SubscribeEvent
	public static void onWorldRenderLast(RenderWorldLastEvent event) {
		GlStateManager.pushMatrix();
		GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
		GlStateManager.disableDepthTest();
		GlStateManager.disableTexture();
		GlStateManager.enableBlend();

		PlayerEntity player = Minecraft.getInstance().player;
		int color = Color.HSBtoRGB(ClientTickHandler.ticksInGame % 200 / 200F, 0.6F, 1F);

		if(!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() instanceof ICoordBoundItem) {
			BlockPos coords = ((ICoordBoundItem) player.getHeldItemMainhand().getItem()).getBinding(player.getHeldItemMainhand());
			if(coords != null)
				renderBlockOutlineAt(coords, color);
		}

		if(!player.getHeldItemOffhand().isEmpty() && player.getHeldItemOffhand().getItem() instanceof ICoordBoundItem) {
			BlockPos coords = ((ICoordBoundItem) player.getHeldItemOffhand().getItem()).getBinding(player.getHeldItemOffhand());
			if(coords != null)
				renderBlockOutlineAt(coords, color);
		}

		LazyOptional<IItemHandler> mainInvCap = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP);
		mainInvCap.ifPresent(mainInv -> {
			IItemHandlerModifiable acc = BotaniaAPI.internalHandler.getAccessoriesInventory(player);
			IItemHandler joined = acc != null ? new CombinedInvWrapper((IItemHandlerModifiable) mainInv, acc) : mainInv;

			for (int i = 0; i < joined.getSlots(); i++) {
				ItemStack stackInSlot = joined.getStackInSlot(i);

				if (!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof IWireframeCoordinateListProvider) {
					IWireframeCoordinateListProvider provider = (IWireframeCoordinateListProvider) stackInSlot.getItem();
					List<BlockPos> coordsList = provider.getWireframesToDraw(player, stackInSlot);
					for (BlockPos coords : coordsList)
						renderBlockOutlineAt(coords, color);

					BlockPos coords = provider.getSourceWireframe(player, stackInSlot);
					if (coords != null && coords.getY() > -1)
						renderBlockOutlineAt(coords, color, 5F);
				}
			}
		});


		GlStateManager.enableDepthTest();
		GlStateManager.enableTexture();
		GlStateManager.disableBlend();
		GL11.glPopAttrib();
		GlStateManager.popMatrix();
	}

	private static void renderBlockOutlineAt(BlockPos pos, int color) {
		renderBlockOutlineAt(pos, color, 1F);
	}

	private static void renderBlockOutlineAt(BlockPos pos, int color, float thickness) {
		double renderPosX = Minecraft.getInstance().getRenderManager().renderPosX;
		double renderPosY = Minecraft.getInstance().getRenderManager().renderPosY;
		double renderPosZ = Minecraft.getInstance().getRenderManager().renderPosZ;

		GlStateManager.pushMatrix();
		GlStateManager.translated(pos.getX() - renderPosX, pos.getY() - renderPosY, pos.getZ() - renderPosZ + 1);
		Color colorRGB = new Color(color);
		GL11.glColor4ub((byte) colorRGB.getRed(), (byte) colorRGB.getGreen(), (byte) colorRGB.getBlue(), (byte) 255);

		World world = Minecraft.getInstance().world;
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		drawWireframe : {
			List<AxisAlignedBB> list;

			if(block instanceof IWireframeAABBProvider)
				list = ((IWireframeAABBProvider) block).getWireframeAABB(world, pos);
			else list = state.getShape(world, pos).toBoundingBoxList();

			if(list == null)
				break drawWireframe;

			GlStateManager.scalef(1F, 1F, 1F);

			GL11.glLineWidth(thickness);
			for(AxisAlignedBB axis : list) {
				axis = axis.offset(-pos.getX(), -pos.getY(), -(pos.getZ() + 1));
				renderBlockOutline(axis);
			}

			GL11.glLineWidth(thickness + 3F);
			GL11.glColor4ub((byte) colorRGB.getRed(), (byte) colorRGB.getGreen(), (byte) colorRGB.getBlue(), (byte) 64);
			for(AxisAlignedBB axis : list) {
				axis = axis.offset(-pos.getX(), -pos.getY(), -(pos.getZ() + 1));
				renderBlockOutline(axis);
			}
		}

		GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) 255);
		GlStateManager.popMatrix();
	}

	private static void renderBlockOutline(AxisAlignedBB aabb) {
		Tessellator tessellator = Tessellator.getInstance();

		double ix = aabb.minX;
		double iy = aabb.minY;
		double iz = aabb.minZ;
		double ax = aabb.maxX;
		double ay = aabb.maxY;
		double az = aabb.maxZ;

		tessellator.getBuffer().begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
		tessellator.getBuffer().pos(ix, iy, iz).endVertex();
		tessellator.getBuffer().pos(ix, ay, iz).endVertex();

		tessellator.getBuffer().pos(ix, ay, iz).endVertex();
		tessellator.getBuffer().pos(ax, ay, iz).endVertex();

		tessellator.getBuffer().pos(ax, ay, iz).endVertex();
		tessellator.getBuffer().pos(ax, iy, iz).endVertex();

		tessellator.getBuffer().pos(ax, iy, iz).endVertex();
		tessellator.getBuffer().pos(ix, iy, iz).endVertex();

		tessellator.getBuffer().pos(ix, iy, az).endVertex();
		tessellator.getBuffer().pos(ix, ay, az).endVertex();

		tessellator.getBuffer().pos(ix, iy, az).endVertex();
		tessellator.getBuffer().pos(ax, iy, az).endVertex();

		tessellator.getBuffer().pos(ax, iy, az).endVertex();
		tessellator.getBuffer().pos(ax, ay, az).endVertex();

		tessellator.getBuffer().pos(ix, ay, az).endVertex();
		tessellator.getBuffer().pos(ax, ay, az).endVertex();

		tessellator.getBuffer().pos(ix, iy, iz).endVertex();
		tessellator.getBuffer().pos(ix, iy, az).endVertex();

		tessellator.getBuffer().pos(ix, ay, iz).endVertex();
		tessellator.getBuffer().pos(ix, ay, az).endVertex();

		tessellator.getBuffer().pos(ax, iy, iz).endVertex();
		tessellator.getBuffer().pos(ax, iy, az).endVertex();

		tessellator.getBuffer().pos(ax, ay, iz).endVertex();
		tessellator.getBuffer().pos(ax, ay, az).endVertex();

		tessellator.draw();
	}
}
