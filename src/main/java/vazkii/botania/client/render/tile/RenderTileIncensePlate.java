/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 15, 2015, 4:27:27 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileIncensePlate;

import javax.annotation.Nonnull;
import java.util.Map;

public class RenderTileIncensePlate extends TileEntityRenderer<TileIncensePlate> {

	private static final Map<Direction, Integer> ROTATIONS = ImmutableMap.of(Direction.NORTH, 180, Direction.SOUTH, 0, Direction.WEST, 270, Direction.EAST, 90);

	@SuppressWarnings("deprecation")
	@Override
	public void render(@Nonnull TileIncensePlate plate, double d0, double d1, double d2, float ticks, int digProgress) {
		if(!plate.getWorld().isBlockLoaded(plate.getPos())
				|| plate.getWorld().getBlockState(plate.getPos()).getBlock() != ModBlocks.incensePlate)
			return;

		ItemStack stack = plate.getItemHandler().getStackInSlot(0);
		if(stack.isEmpty())
			return;

		Direction facing = plate.getWorld().getBlockState(plate.getPos()).get(BotaniaStateProps.CARDINALS);

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.translated(d0, d1, d2);
		GlStateManager.translatef(0.5F, 1.5F, 0.5F);
		GlStateManager.rotatef(ROTATIONS.get(facing), 0F, 1F, 0F);
		Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		float s = 0.6F;
		GlStateManager.translatef(-0.11F, -1.35F, 0F);
		GlStateManager.scalef(s, s, s);
		Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
		GlStateManager.color3f(1F, 1F, 1F);
		GlStateManager.enableRescaleNormal();
		GlStateManager.popMatrix();
	}

}
