/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 16, 2014, 10:15:05 PM (GMT)]
 */
package vazkii.botania.common.block.tile.mana;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.tile.TileMod;

public class TileTurntable extends TileMod implements ITickable {

	private static final String TAG_SPEED = "speed";
	private static final String TAG_BACKWARDS = "backwards";

	int speed = 1;
	boolean backwards = false;

	@Override
	public void tick() {
		boolean redstone = false;

		for(EnumFacing dir : EnumFacing.BY_INDEX) {
			int redstoneSide = world.getRedstonePower(pos.offset(dir), dir);
			if(redstoneSide > 0)
				redstone = true;
		}

		if(!redstone) {
			TileEntity tile = world.getTileEntity(pos.up());
			if(tile instanceof TileSpreader) {
				TileSpreader spreader = (TileSpreader) tile;
				spreader.rotationX += speed * (backwards ? -1 : 1);
				if(spreader.rotationX >= 360F)
					spreader.rotationX -= 360F;
				if(!world.isRemote)
					spreader.checkForReceiver();
			}
		}
	}

	@Override
	public void writePacketNBT(NBTTagCompound cmp) {
		cmp.setInt(TAG_SPEED, speed);
		cmp.setBoolean(TAG_BACKWARDS, backwards);
	}

	@Override
	public void readPacketNBT(NBTTagCompound cmp) {
		speed = cmp.getInt(TAG_SPEED);
		backwards = cmp.getBoolean(TAG_BACKWARDS);
	}

	public void onWanded(EntityPlayer player, ItemStack wand) {
		if(player == null)
			return;

		if(player.isSneaking())
			backwards = !backwards;
		else speed = speed == 6 ? 1 : speed + 1;
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);
	}

	@OnlyIn(Dist.CLIENT)
	public void renderHUD(Minecraft mc) {
		int color = 0xAA006600;

		char motion = backwards ? '<' : '>';
		String speed = TextFormatting.BOLD + "";
		for(int i = 0; i < this.speed; i++)
			speed = speed + motion;

		int x = mc.mainWindow.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(speed) / 2;
		int y = mc.mainWindow.getScaledHeight() / 2 - 15;
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		mc.fontRenderer.drawStringWithShadow(speed, x, y, color);
		GlStateManager.disableBlend();
	}

}
