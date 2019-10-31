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

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.tile.TileMod;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

public class TileTurntable extends TileMod implements ITickableTileEntity {
	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.TURNTABLE)
	public static TileEntityType<TileTurntable> TYPE;
	private static final String TAG_SPEED = "speed";
	private static final String TAG_BACKWARDS = "backwards";

	private int speed = 1;
	private boolean backwards = false;

	public TileTurntable() {
		super(TYPE);
	}

	@Override
	public void tick() {
		boolean redstone = false;

		for(Direction dir : Direction.values()) {
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
	public void writePacketNBT(CompoundNBT cmp) {
		cmp.putInt(TAG_SPEED, speed);
		cmp.putBoolean(TAG_BACKWARDS, backwards);
	}

	@Override
	public void readPacketNBT(CompoundNBT cmp) {
		speed = cmp.getInt(TAG_SPEED);
		backwards = cmp.getBoolean(TAG_BACKWARDS);
	}

	public void onWanded(PlayerEntity player, ItemStack wand) {
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
