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
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileIncensePlate;

import javax.annotation.Nonnull;
import java.util.Map;

public class RenderTileIncensePlate extends TileEntitySpecialRenderer<TileIncensePlate> {

	private static final Map<EnumFacing, Integer> ROTATIONS = ImmutableMap.of(EnumFacing.NORTH, 180, EnumFacing.SOUTH, 0, EnumFacing.WEST, 270, EnumFacing.EAST, 90);

	@SuppressWarnings("deprecation")
	@Override
	public void render(@Nonnull TileIncensePlate plate, double d0, double d1, double d2, float ticks, int digProgress, float unused) {
		if(!plate.getWorld().isBlockLoaded(plate.getPos(), false)
				|| plate.getWorld().getBlockState(plate.getPos()).getBlock() != ModBlocks.incensePlate)
			return;

		ItemStack stack = plate.getItemHandler().getStackInSlot(0);
		if(stack.isEmpty())
			return;

		EnumFacing facing = plate.getWorld().getBlockState(plate.getPos()).getValue(BotaniaStateProps.CARDINALS);

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.translate(d0, d1, d2);
		GlStateManager.translate(0.5F, 1.5F, 0.5F);
		GlStateManager.rotate(ROTATIONS.get(facing), 0F, 1F, 0F);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		float s = 0.6F;
		GlStateManager.translate(-0.11F, -1.35F, 0F);
		GlStateManager.scale(s, s, s);
		Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
		GlStateManager.color(1F, 1F, 1F);
		GlStateManager.enableRescaleNormal();
		GlStateManager.popMatrix();
	}

}
